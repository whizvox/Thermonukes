package me.whizvox.thermonukes.common.entity;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import me.whizvox.thermonukes.client.sound.BlackHoleSoundInstance;
import me.whizvox.thermonukes.common.lib.ThermonukesDamageSources;
import me.whizvox.thermonukes.common.lib.ThermonukesEntities;
import me.whizvox.thermonukes.common.lib.ThermonukesSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class BlackHole extends Entity {

  protected static final EntityDataAccessor<Integer> TIMER_ID = SynchedEntityData.defineId(BlackHole.class, EntityDataSerializers.INT);
  protected static final String TIMER_TAG = "Timer";
  protected static final int TIMER_DEFAULT_VALUE = 6;

  protected static final EntityDataAccessor<Integer> RADIUS_ID = SynchedEntityData.defineId(BlackHole.class, EntityDataSerializers.INT);
  protected static final String RADIUS_TAG = "Radius";
  protected static final int RADIUS_DEFAULT_VALUE = 2;

  private static final float
      ENTITY_INFLUENCE_MULTIPLIER = 2.0F,
      ENTITY_KILL_RADIUS_MULTIPLIER = 0.25F;

  private final LongOpenHashSet knownBlocks;
  private AABB entityInfluenceBounds;

  public BlackHole(EntityType<?> type, Level level) {
    super(type, level);
    knownBlocks = new LongOpenHashSet();
    entityInfluenceBounds = null;
  }

  public BlackHole(Level level, Vec3 pos) {
    this(ThermonukesEntities.BLACK_HOLE.get(), level);
    moveTo(pos);
  }

  private static int calculateFreshTimer(int radius) {
    return (int) (0.3 * radius * radius * radius);
  }

  private void recalculateKnownBlocks() {
    knownBlocks.clear();
    int radius = getRadius();
    int radiusSqr = radius * radius;
    BlockPos centerPos = blockPosition();
    for (int xOff = -radius; xOff <= radius; xOff++) {
      for (int yOff = -radius; yOff <= radius; yOff++) {
        for (int zOff = -radius; zOff <= radius; zOff++) {
          BlockPos pos = centerPos.offset(xOff, yOff, zOff);
          if (pos.distSqr(centerPos) <= radiusSqr) {
            knownBlocks.add(pos.asLong());
          }
        }
      }
    }
    int eRadius = (int) (radius * ENTITY_INFLUENCE_MULTIPLIER);
    entityInfluenceBounds = new AABB(centerPos.offset(-eRadius, -eRadius, -eRadius), centerPos.offset(eRadius, eRadius, eRadius));
  }

  public void setTimer(int timer) {
    entityData.set(TIMER_ID, timer);
  }

  public int getTimer() {
    return entityData.get(TIMER_ID);
  }

  public int getRadius() {
    return entityData.get(RADIUS_ID);
  }

  public void setRadius(int radius) {
    entityData.set(RADIUS_ID, radius);
  }

  public void initRadius(int radius) {
    setRadius(radius);
    setTimer(calculateFreshTimer(radius));
  }

  @Override
  public void tick() {
    int radius = getRadius();
    int radiusSqr = radius * radius;
    int diameter = radius * 2;
    if (level.isClientSide) {
      Vec3 pos;
      do {
        pos = new Vec3(
            position().x + random.nextDouble() * diameter - radius,
            position().y + random.nextDouble() * diameter - radius,
            position().z + random.nextDouble() * diameter - radius
        );
      } while (pos.distanceToSqr(position()) > radiusSqr);
      Vec3 vel = new Vec3(position().x - pos.x, position().y - pos.y, position().z - pos.z).normalize().scale(0.3F);
      level.addParticle(ParticleTypes.SMOKE, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);
    }
    if (firstTick) {
      recalculateKnownBlocks();
      if (level.isClientSide) {
        Minecraft.getInstance().getSoundManager().play(new BlackHoleSoundInstance(this));
      }
    }
    knownBlocks.longStream().mapToObj(BlockPos::of).forEach(pos -> {
      if (!level.getBlockState(pos).isAir() || !level.getFluidState(pos).isEmpty()) {
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
      }
    });
    float eRadius = radius * ENTITY_INFLUENCE_MULTIPLIER;
    float eRadiusSqr = eRadius * eRadius;
    float killRadius = radius * ENTITY_KILL_RADIUS_MULTIPLIER;
    level.getEntities((Entity) null, entityInfluenceBounds, entity -> true)
        .stream()
        .filter(entity -> !(entity instanceof BlackHole) && !(entity instanceof Player player && player.isCreative()) && entity.distanceToSqr(this) <= eRadiusSqr)
        .forEach(entity -> {
      double dist = entity.distanceTo(this);
      if (dist < killRadius) {
        if (entity instanceof PrimedAntimatterExplosive antimatterExplosive) {
          level.playSound(null, position().x, position().y, position().z, ThermonukesSounds.BLACK_HOLE_DISAPPEAR.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
          discard();
          antimatterExplosive.detonate();
          antimatterExplosive.discard();
        } else {
          if (entity instanceof LivingEntity livEntity) {
            livEntity.hurt(ThermonukesDamageSources.BLACK_HOLE, livEntity.getHealth());
          } else {
            entity.kill();
          }
        }
      } else {
        double pullForce = eRadius - dist;
        double dx = entity.position().x - position().x;
        double dy = entity.position().y - position().y;
        double dz = entity.position().z - position().z;
        Vec3 delta = new Vec3(dx, dy, dz).scale(-0.1 * pullForce / dist);
        entity.setDeltaMovement(entity.getDeltaMovement().add(delta));
      }
    });
    int timer = getTimer() - 1;
    if (timer <= 0) {
      radius++;
      setRadius(radius);
      timer = calculateFreshTimer(radius);
      recalculateKnownBlocks();
    }
    setTimer(timer);
    super.tick();
  }

  @Override
  protected void defineSynchedData() {
    entityData.define(TIMER_ID, TIMER_DEFAULT_VALUE);
    entityData.define(RADIUS_ID, RADIUS_DEFAULT_VALUE);
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag tag) {
    setTimer(tag.getInt(TIMER_TAG));
    setRadius(tag.getInt(RADIUS_TAG));
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag tag) {
    tag.putInt(TIMER_TAG, getTimer());
    tag.putInt(RADIUS_TAG, getRadius());
  }

  @Override
  public Packet<?> getAddEntityPacket() {
    return new ClientboundAddEntityPacket(this);
  }

}
