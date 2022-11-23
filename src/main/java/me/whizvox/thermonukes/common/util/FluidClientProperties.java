package me.whizvox.thermonukes.common.util;

import com.mojang.math.Vector3f;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public record FluidClientProperties(ResourceLocation stillTexture,
                                    ResourceLocation flowingTexture,
                                    @Nullable ResourceLocation overlayTexture,
                                    int tintColor,
                                    Vector3f fogColor) {

  public boolean hasOverlay() {
    return overlayTexture != null;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private static final ResourceLocation
        TEXTURE_WATER_STILL = new ResourceLocation("block/water_still"),
        TEXTURE_WATER_FLOWING = new ResourceLocation("block/water_flow");

    private ResourceLocation stillTexture;
    private ResourceLocation flowingTexture;
    private ResourceLocation overlayTexture;
    private int tintColor;
    private int fogColor;

    private Builder() {
      stillTexture = TEXTURE_WATER_STILL;
      flowingTexture = TEXTURE_WATER_FLOWING;
      overlayTexture = null;
      tintColor = 0xA00000FF;
      fogColor = 0xA00000FF;
    }

    public Builder stillTexture(ResourceLocation stillTexture) {
      this.stillTexture = stillTexture;
      return this;
    }

    public Builder flowingTexture(ResourceLocation flowingTexture) {
      this.flowingTexture = flowingTexture;
      return this;
    }

    public Builder overlayTexture(ResourceLocation overlayTexture) {
      this.overlayTexture = overlayTexture;
      return this;
    }

    public Builder tintColor(int tintColor) {
      this.tintColor = tintColor;
      return this;
    }

    public Builder fogColor(int fogColor) {
      this.fogColor = fogColor;
      return this;
    }

    public FluidClientProperties build() {
      return new FluidClientProperties(stillTexture, flowingTexture, overlayTexture, tintColor, new Vector3f(((fogColor >> 16) & 0xFF) / 255.0F, ((fogColor >> 8) & 0xFF) / 255.0F, (fogColor & 0xFF) / 255.0F));
    }
  }

}
