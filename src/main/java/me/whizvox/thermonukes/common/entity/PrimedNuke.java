package me.whizvox.thermonukes.common.entity;

import me.whizvox.thermonukes.common.lib.ThermonukesCapabilities;
import me.whizvox.thermonukes.common.lib.ThermonukesEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

public class PrimedNuke extends AbstractPrimedExplosive {

  private static final EntityDataAccessor<Float> DOSAGE_ID = SynchedEntityData.defineId(PrimedNuke.class, EntityDataSerializers.FLOAT);
  private static final String DOSAGE_TAG = "Dosage";
  private static final float DOSAGE_DEFAULT_VALUE = 50_000.0F;

  public PrimedNuke(EntityType<?> type, Level level) {
    super(type, level);
    entityData.define(DOSAGE_ID, DOSAGE_DEFAULT_VALUE);
  }

  public PrimedNuke(Level level, BlockPos pos) {
    this(ThermonukesEntities.PRIMED_NUKE.get(), level);
    moveTo(pos);
  }

  public float getDosage() {
    return entityData.get(DOSAGE_ID);
  }

  public void setDosage(float dosage) {
    entityData.set(DOSAGE_ID, dosage);
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
    tag.putFloat(DOSAGE_TAG, getDosage());
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
    setDosage(tag.getFloat(DOSAGE_TAG));
  }

  @Override
  protected void detonate() {
    float power = getPower();
    float dosage = getDosage();
    if (!level.isClientSide) {
      level.explode(null, position().x, position().y, position().z, power, Explosion.BlockInteraction.BREAK);
    }
    level.getCapability(ThermonukesCapabilities.WORLD_RADIATION).ifPresent(radiation -> {
      radiation.irradiateArea(blockPosition(), dosage);
    });
  }

}
