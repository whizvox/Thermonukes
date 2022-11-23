package me.whizvox.thermonukes.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;

public abstract class AbstractPrimedExplosive extends Entity {

  protected static final EntityDataAccessor<Integer> FUSE_ID = SynchedEntityData.defineId(AbstractPrimedExplosive.class, EntityDataSerializers.INT);
  protected static final String FUSE_TAG = "Fuse";
  protected static final int FUSE_DEFAULT_VALUE = 100;

  protected static final EntityDataAccessor<Float> POWER_ID = SynchedEntityData.defineId(AbstractPrimedExplosive.class, EntityDataSerializers.FLOAT);
  protected static final String POWER_TAG = "Power";
  protected static final float POWER_DEFAULT_VALUE = 5.0F;

  public AbstractPrimedExplosive(EntityType<?> type, Level level) {
    super(type, level);
    setFuse(FUSE_DEFAULT_VALUE);
  }

  public void moveTo(BlockPos pos) {
    moveTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
  }

  public int getFuse() {
    return entityData.get(FUSE_ID);
  }

  public void setFuse(int fuse) {
    entityData.set(FUSE_ID, fuse);
  }

  public float getPower() {
    return entityData.get(POWER_ID);
  }

  public void setPower(float power) {
    entityData.set(POWER_ID, power);
  }

  protected boolean shouldDetonate() {
    return true;
  }

  protected abstract void detonate();

  @Override
  public void tick() {
    if (!isNoGravity()) {
      setDeltaMovement(getDeltaMovement().add(0.0, -0.04, 0.0));
    }
    move(MoverType.SELF, getDeltaMovement());
    if (onGround) {
      setDeltaMovement(getDeltaMovement().multiply(0.7, -0.5, 0.7));
    }
    int fuse = getFuse() - 1;
    if (fuse <= 0) {
      if (shouldDetonate()) {
        discard();
        detonate();
      }
    } else {
      setFuse(fuse);
    }
  }

  @Override
  protected void defineSynchedData() {
    entityData.define(FUSE_ID, FUSE_DEFAULT_VALUE);
    entityData.define(POWER_ID, POWER_DEFAULT_VALUE);
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag tag) {
    tag.putInt(FUSE_TAG, getFuse());
    tag.putFloat(POWER_TAG, getPower());
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag tag) {
    setFuse(tag.getInt(FUSE_TAG));
    setPower(tag.getInt(POWER_TAG));
  }

  @Override
  public Packet<?> getAddEntityPacket() {
    return new ClientboundAddEntityPacket(this);
  }

}
