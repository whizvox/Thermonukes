package me.whizvox.thermonukes.data.client;

import me.whizvox.thermonukes.Thermonukes;
import me.whizvox.thermonukes.common.lib.ThermonukesSounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class ThermonukesSoundProvider extends SoundDefinitionsProvider {

  public ThermonukesSoundProvider(DataGenerator generator, ExistingFileHelper helper) {
    super(generator, Thermonukes.MOD_ID, helper);
  }

  protected static SoundDefinition definitionWithSubtitle(ResourceLocation name) {
    return definition().subtitle("subtitles." + name.getNamespace() + "." + name.getPath());
  }

  protected static ResourceLocation convertToFileLocation(ResourceLocation name) {
    return new ResourceLocation(name.getNamespace(), name.getPath().replaceAll("\\.", "/"));
  }

  private void add(SoundEvent soundEvent) {
    add(soundEvent, definition().with(sound(convertToFileLocation(soundEvent.getLocation()))));
  }

  private void addWithSubtitle(SoundEvent soundEvent, String path) {
    ResourceLocation name = soundEvent.getLocation();
    add(soundEvent, definitionWithSubtitle(name).with(sound(new ResourceLocation(name.getNamespace(), path))));
  }

  private void addWithSubtitle(SoundEvent soundEvent) {
    ResourceLocation name = soundEvent.getLocation();
    add(soundEvent, definitionWithSubtitle(name).with(sound(convertToFileLocation(name))));
  }

  private void addMultipleWithSubtitle(SoundEvent soundEvent, SoundDefinition.Sound... sounds) {
    if (sounds.length == 0) {
      return;
    }
    SoundDefinition def = definitionWithSubtitle(soundEvent.getLocation())
        .with(sounds);
    add(soundEvent, def);
  }

  private void addMultipleWithSubtitle(SoundEvent soundEvent, ResourceLocation... locations) {
    SoundDefinition.Sound[] sounds = new SoundDefinition.Sound[locations.length];
    for (int i = 0; i < locations.length; i++) {
      sounds[i] = sound(locations[i]);
    }
    addMultipleWithSubtitle(soundEvent, sounds);
  }

  private void addMultipleWithSubtitle(SoundEvent soundEvent, String basePath, int count) {
    ResourceLocation[] locations = new ResourceLocation[count];
    for (int i = 0; i < count; i++) {
      locations[i] = new ResourceLocation(soundEvent.getLocation().getNamespace(), basePath + "_" + i);
    }
    addMultipleWithSubtitle(soundEvent, locations);
  }

  private void addMultipleWithSubtitle(SoundEvent soundEvent, int count) {
    addMultipleWithSubtitle(soundEvent, convertToFileLocation(soundEvent.getLocation()).getPath(), count);
  }

  @Override
  public void registerSounds() {
    addWithSubtitle(ThermonukesSounds.ANTIMATTER_EXPLOSIVE_DETONATION.get());
    addWithSubtitle(ThermonukesSounds.BLACK_HOLE_WHOOSHING.get(), "black_hole_wooshing");
    addWithSubtitle(ThermonukesSounds.BLACK_HOLE_DISAPPEAR.get(), "black_hole_disappear");
    addWithSubtitle(ThermonukesSounds.WATER_SWIRL.get(), "water_swirl");
    addWithSubtitle(ThermonukesSounds.MACHINE_PUMPING.get(), "machine_pumping");
    add(ThermonukesSounds.GEIGER_COUNTER_MODE_CHANGE.get());
    addWithSubtitle(ThermonukesSounds.GEIGER_COUNTER_CLICK.get());
    addMultipleWithSubtitle(ThermonukesSounds.GEIGER_COUNTER_BURST.get(), 3);
    addMultipleWithSubtitle(ThermonukesSounds.GEIGER_COUNTER_FAST_BURST.get(), 3);
    addMultipleWithSubtitle(ThermonukesSounds.GEIGER_COUNTER_EXTREME_BURST.get(), 3);
  }

}
