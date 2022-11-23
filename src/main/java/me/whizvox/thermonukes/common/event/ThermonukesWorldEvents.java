package me.whizvox.thermonukes.common.event;

import me.whizvox.thermonukes.common.item.HazmatSuitArmorItem;
import me.whizvox.thermonukes.common.lib.ThermonukesCapabilities;
import me.whizvox.thermonukes.common.lib.ThermonukesDamageSources;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class ThermonukesWorldEvents {

  private static void onLivingEntityTick(final LivingEvent.LivingTickEvent event) {
    // update entity radiation dosage
    LivingEntity entity = event.getEntity();
    if (!(entity instanceof Player player) || !player.isCreative()) {
      entity.level.getCapability(ThermonukesCapabilities.WORLD_RADIATION).ifPresent(worldRadiation -> {
        float chunkDosage = worldRadiation.getDosage(entity.chunkPosition());
        entity.getCapability(ThermonukesCapabilities.ENTITY_RADIATION).ifPresent(entityRadiation -> {
          if (chunkDosage > 0.0F || entityRadiation.getDosage() > 0.0F) {
            boolean fullHazmat = true;
            EquipmentSlot[] slots = new EquipmentSlot[] {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
            for (EquipmentSlot slot : slots) {
              ItemStack armor = entity.getItemBySlot(slot);
              if (armor.getItem() instanceof HazmatSuitArmorItem) {
                if (chunkDosage > 200_000.0F || entity.getRandom().nextInt(200_000) < chunkDosage) {
                  armor.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(slot));
                }
              } else {
                fullHazmat = false;
              }
            }
            if (fullHazmat) {
              entityRadiation.tick(entity, entity.getRandom(), Mth.clamp(chunkDosage - 10_000.0F, 0.0F, Float.MAX_VALUE));
            } else {
              entityRadiation.tick(entity, entity.getRandom(), chunkDosage);
            }
          }
          if (entityRadiation.getDosage() > 0.0F/* && entity.getRandom().nextFloat() < entityRadiation.getDosage() / 10_000.0F*/) {
            entity.level.addParticle(ParticleTypes.ENTITY_EFFECT, entity.getX(), entity.getY(), entity.getZ(), 0.0F, 1.0F, 0.0F);
          }
        });
      });
    }

    // deal radiation damage to entity
    // don't affect players in creative mode
    if (!(entity instanceof Player player) || !player.isCreative()) {
      entity.getCapability(ThermonukesCapabilities.ENTITY_RADIATION).ifPresent(entityRadiation -> {
        float dosage = entityRadiation.getDosage();
        if (dosage > 0) {
          if (dosage > 10_000.0F) {
            entity.hurt(ThermonukesDamageSources.RADIATION, (dosage - 9900.0F) / 100.0F);
          } else if (entity.getRandom().nextFloat() < dosage / 10_000.0F) {
            entity.hurt(ThermonukesDamageSources.RADIATION, 1.0F);
          }
        }
      });
    }
  }

  private static void onServerTick(final TickEvent.ServerTickEvent event) {
    // decay radiation levels
    event.getServer().getAllLevels().forEach(level ->
        level.getCapability(ThermonukesCapabilities.WORLD_RADIATION).ifPresent(worldRadiation ->
            worldRadiation.tickDecay(level.random)
        )
    );
  }

  public static void register(IEventBus bus) {
    bus.addListener(ThermonukesWorldEvents::onLivingEntityTick);
    bus.addListener(ThermonukesWorldEvents::onServerTick);
  }

}
