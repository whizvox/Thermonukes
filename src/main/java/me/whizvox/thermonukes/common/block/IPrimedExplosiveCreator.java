package me.whizvox.thermonukes.common.block;

import me.whizvox.thermonukes.common.entity.AbstractPrimedExplosive;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IPrimedExplosiveCreator {

  AbstractPrimedExplosive create(Level level, BlockPos pos);

}
