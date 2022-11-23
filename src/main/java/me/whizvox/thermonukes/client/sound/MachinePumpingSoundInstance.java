package me.whizvox.thermonukes.client.sound;

import me.whizvox.thermonukes.common.block.entity.AirScrubberBlockEntity;
import me.whizvox.thermonukes.common.lib.ThermonukesSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

public class MachinePumpingSoundInstance extends AbstractTickableSoundInstance {

  private final AirScrubberBlockEntity machine;

  public MachinePumpingSoundInstance(AirScrubberBlockEntity machine) {
    super(ThermonukesSounds.MACHINE_PUMPING.get(), SoundSource.BLOCKS, SoundInstance.createUnseededRandom());
    this.machine = machine;
    looping = true;
    delay = 0;
    volume = 0.0F;
    x = machine.getBlockPos().getX();
    y = machine.getBlockPos().getY();
    z = machine.getBlockPos().getZ();
  }

  @Override
  public boolean canStartSilent() {
    return true;
  }

  @Override
  public void tick() {
    if (machine.shouldPlaySound()) {
      double distSqr = machine.getBlockPos().distToCenterSqr(Minecraft.getInstance().player.position());
      if (distSqr < 400.0) {
        if (distSqr < 64.0) {
          volume = 1.0F;
        } else {
          volume = (float) (-(1.0 / 12.0) * Math.sqrt(distSqr) + 5.0 / 3.0);
        }
      }
    } else {
      stop();
    }
  }

}
