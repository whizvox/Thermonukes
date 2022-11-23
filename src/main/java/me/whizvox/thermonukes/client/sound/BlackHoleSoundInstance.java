package me.whizvox.thermonukes.client.sound;

import me.whizvox.thermonukes.common.entity.BlackHole;
import me.whizvox.thermonukes.common.lib.ThermonukesSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;

public class BlackHoleSoundInstance extends AbstractTickableSoundInstance {

  private final BlackHole blackHole;

  public BlackHoleSoundInstance(BlackHole blackHole) {
    super(ThermonukesSounds.BLACK_HOLE_WHOOSHING.get(), SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
    this.blackHole = blackHole;
    looping = true;
    delay = 0;
    volume = 0.0F;
    x = blackHole.getX();
    y = blackHole.getY();
    z = blackHole.getZ();
  }

  @Override
  public boolean canStartSilent() {
    return true;
  }

  @Override
  public void tick() {
    if (blackHole.isRemoved()) {
      stop();
    } else {
      int radius = blackHole.getRadius(); // when volume is at max
      int fullAreaRadius = radius * 3;
      float dist = Minecraft.getInstance().player.distanceTo(blackHole);
      volume = Mth.clamp(1.0F - (dist - radius) / (fullAreaRadius - radius), 0.0F, 1.0F);
    }
  }

}
