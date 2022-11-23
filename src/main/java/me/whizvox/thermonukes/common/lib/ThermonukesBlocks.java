package me.whizvox.thermonukes.common.lib;

import me.whizvox.thermonukes.Thermonukes;
import me.whizvox.thermonukes.common.ExplosiveProperties;
import me.whizvox.thermonukes.common.block.AirScrubberBlock;
import me.whizvox.thermonukes.common.block.ExplosiveBlock;
import me.whizvox.thermonukes.common.block.IPrimedExplosiveCreator;
import me.whizvox.thermonukes.common.entity.PrimedAntimatterExplosive;
import me.whizvox.thermonukes.common.entity.PrimedNuke;
import me.whizvox.thermonukes.common.entity.PrimedRedMatterExplosive;
import me.whizvox.thermonukes.common.block.CleansingWaterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ThermonukesBlocks {

  private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Thermonukes.MOD_ID);

  public static void register(IEventBus bus) {
    BLOCKS.register(bus);
  }

  private static RegistryObject<ExplosiveBlock> registerExplosive(String name, ExplosiveProperties explosiveProps, IPrimedExplosiveCreator primedExplosiveCreator) {
    return BLOCKS.register(name, () -> new ExplosiveBlock(explosiveProps, primedExplosiveCreator));
  }

  public static final RegistryObject<AirScrubberBlock> AIR_SCRUBBER = BLOCKS.register("air_scrubber", AirScrubberBlock::new);

  public static final RegistryObject<LiquidBlock>
      CLEANSING_WATER = BLOCKS.register("cleansing_water", CleansingWaterBlock::new);

  public static final RegistryObject<ExplosiveBlock>
      ANTIMATTER_EXPLOSIVE = registerExplosive("antimatter_explosive", ExplosiveProperties.builder().build(), PrimedAntimatterExplosive::new),
      RED_MATTER_EXPLOSIVE = registerExplosive("red_matter_explosive", ExplosiveProperties.builder().build(), PrimedRedMatterExplosive::new),
      NUKE = registerExplosive("nuke", ExplosiveProperties.builder().build(), PrimedNuke::new);

}
