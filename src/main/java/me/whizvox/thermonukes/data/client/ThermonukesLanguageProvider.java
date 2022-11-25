package me.whizvox.thermonukes.data.client;

import me.whizvox.thermonukes.Thermonukes;
import me.whizvox.thermonukes.common.lib.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;

public class ThermonukesLanguageProvider extends LanguageProvider {

  public ThermonukesLanguageProvider(DataGenerator gen) {
    super(gen, Thermonukes.MOD_ID, "en_us");
  }

  private String objToLang(String format, ResourceLocation location) {
    return format.formatted(location.getNamespace(), location.getPath().replaceAll("/", "."));
  }

  private void addSoundSubtitle(RegistryObject<SoundEvent> soundEvent, String value) {
    add(objToLang("subtitles.%s.%s", soundEvent.get().getLocation()), value);
  }

  private void addCreativeTab(String group, String value) {
    if (group == null) {
      add("itemGroup." + Thermonukes.MOD_ID, value);
    } else {
      add("itemGroup." + Thermonukes.MOD_ID + "." + group, value);
    }
  }

  private void addDamageSource(DamageSource source, String value) {
    add("death.attack." + source.msgId, value);
  }

  @Override
  protected void addTranslations() {
    addBlock(ThermonukesBlocks.ANTIMATTER_EXPLOSIVE, "Antimatter Explosive");
    addBlock(ThermonukesBlocks.RED_MATTER_EXPLOSIVE, "Red Matter Explosive");
    addBlock(ThermonukesBlocks.NUKE, "Nuke");
    addBlock(ThermonukesBlocks.CLEANSING_WATER, "Cleansing Water");
    addBlock(ThermonukesBlocks.AIR_SCRUBBER, "Air Scrubber");
    addItem(ThermonukesItems.GEIGER_COUNTER, "Geiger Counter");
    addItem(ThermonukesItems.SALTY_MIXTURE_TUBE, "Tube of a Salty Mixture");
    addItem(ThermonukesItems.ANTI_RADIATIVE_FABRIC_SHEET, "Sheet of Anti-Radiative Fabric");
    addItem(ThermonukesItems.RUBBER_SHEET, "Rubber Sheet");
    addItem(ThermonukesItems.CLEANSING_SALT, "Cleansing Salt");
    addItem(ThermonukesItems.CLEANSING_WATER_BUCKET, "Bucket of Cleansing Water");
    addItem(ThermonukesItems.HAZMAT_SUIT_HEADPIECE, "Hazmat Headpiece");
    addItem(ThermonukesItems.HAZMAT_SUIT_COAT, "Hazmat Coat");
    addItem(ThermonukesItems.HAZMAT_SUIT_LEGGINGS, "Hazmat Leggings");
    addItem(ThermonukesItems.RUBBER_BOOTS, "Rubber Boots");
    addItem(ThermonukesItems.GAS_MASK, "Gas Mask");

    addCreativeTab("main", "Thermonukes");
    addSoundSubtitle(ThermonukesSounds.ANTIMATTER_EXPLOSIVE_DETONATION, "Antimatter Explosive detonates");
    addSoundSubtitle(ThermonukesSounds.BLACK_HOLE_WHOOSHING, "Black Hole wooshing");
    addSoundSubtitle(ThermonukesSounds.BLACK_HOLE_DISAPPEAR, "Black Hole dissipates");
    addSoundSubtitle(ThermonukesSounds.WATER_SWIRL, "Water swirling");
    addSoundSubtitle(ThermonukesSounds.MACHINE_PUMPING, "Machine pumping");
    addSoundSubtitle(ThermonukesSounds.GEIGER_COUNTER_CLICK, "Geiger Counter clicks");
    addSoundSubtitle(ThermonukesSounds.GEIGER_COUNTER_BURST, "Several Geiger Counter clicks");
    addSoundSubtitle(ThermonukesSounds.GEIGER_COUNTER_FAST_BURST, "Many Geiger Counter clicks");
    addSoundSubtitle(ThermonukesSounds.GEIGER_COUNTER_EXTREME_BURST, "Geiger Counter going haywire");

    addDamageSource(ThermonukesDamageSources.ANTIMATTER_EXPLOSION, "%s was atomically deconstructed by an antimatter explosion");
    addDamageSource(ThermonukesDamageSources.BLACK_HOLE, "%s was sucked into a black hole");
    addDamageSource(ThermonukesDamageSources.RADIATION, "%s died of radiation sickness");

    add(ThermonukesLang.Keys.COMMAND_GENERIC_NOT_LOOKING_AT_ENTITY, "Not looking at any entity");
    add(ThermonukesLang.Keys.COMMAND_GENERIC_MUST_BE_ENTITY, "Must be an entity");
    add(ThermonukesLang.Keys.COMMAND_RADIATION_SET_CHUNK, "Radiation dosage set to %1$s for %2$s in %3$s");
    add(ThermonukesLang.Keys.COMMAND_RADIATION_SET_ENTITY, "Radiation dosage set to %1$s for %2$s");
    add(ThermonukesLang.Keys.COMMAND_RADIATION_SET_ENTITIES, "Radiation dosage set to %1$s for %2$s entities");
    add(ThermonukesLang.Keys.COMMAND_RADIATION_CLEAR_ALL, "Cleared radiation in all levels and entities");
    add(ThermonukesLang.Keys.COMMAND_RADIATION_CLEAR_ENTITY, "Cleared radiation for %s");
    add(ThermonukesLang.Keys.COMMAND_RADIATION_CLEAR_ENTITIES, "Cleared radiation for %s entities");
    add(ThermonukesLang.Keys.COMMAND_RADIATION_CLEAR_CHUNK, "Cleared radiation for %1$s in %2$s");
    add(ThermonukesLang.Keys.COMMAND_RADIATION_CLEAR_LEVEL, "Cleared all radiation in level %s");
    add(ThermonukesLang.Keys.GEIGER_COUNTER_CHUNK_READING, "Reading for Chunk: %s");
    add(ThermonukesLang.Keys.GEIGER_COUNTER_ENTITY_READING, "Reading for %1$s: %2$s");
    add(ThermonukesLang.Keys.GEIGER_COUNTER_SELF_READING, "Reading for Self: %s");
    add(ThermonukesLang.Keys.GEIGER_COUNTER_NEW_MODE, "Mode: %s");
    add(ThermonukesLang.Keys.AIR_SCRUBBER_CONTAINER, "Air Scrubber");

    add("thermonukes.geiger_counter.mode.chunk", "Chunk");
    add("thermonukes.geiger_counter.mode.entity", "Entity");
  }

}
