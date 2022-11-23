package me.whizvox.thermonukes.common.entity;

import me.whizvox.thermonukes.common.lib.ThermonukesEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class PrimedRedMatterExplosive extends AbstractPrimedExplosive {

  public PrimedRedMatterExplosive(EntityType<?> type, Level level) {
    super(type, level);
  }

  public PrimedRedMatterExplosive(Level level, BlockPos pos) {
    this(ThermonukesEntities.PRIMED_RED_MATTER_EXPLOSIVE.get(), level);
    moveTo(pos);
  }

  @Override
  protected void detonate() {
    BlackHole blackHole = new BlackHole(level, position());
    blackHole.initRadius((int) getPower());
    level.addFreshEntity(blackHole);
  }

}
