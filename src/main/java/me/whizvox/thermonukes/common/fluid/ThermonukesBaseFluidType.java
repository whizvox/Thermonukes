package me.whizvox.thermonukes.common.fluid;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Vector3f;
import me.whizvox.thermonukes.common.util.FluidClientProperties;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ThermonukesBaseFluidType extends FluidType {

  private final FluidClientProperties clientProperties;

  public ThermonukesBaseFluidType(Properties properties, FluidClientProperties clientProperties) {
    super(properties);
    this.clientProperties = clientProperties;
  }

  public FluidClientProperties getClientProperties() {
    return clientProperties;
  }

  @Override
  public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
    consumer.accept(new IClientFluidTypeExtensions() {

      @Override
      public ResourceLocation getStillTexture() {
        return clientProperties.stillTexture();
      }

      @Override
      public ResourceLocation getFlowingTexture() {
        return clientProperties.flowingTexture();
      }

      @Override
      public int getTintColor() {
        return clientProperties.tintColor();
      }

      @Override
      public @Nullable ResourceLocation getOverlayTexture() {
        return clientProperties.hasOverlay() ? clientProperties.overlayTexture() : null;
      }

      @Override
      public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
        return clientProperties.fogColor();
      }

      @Override
      public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
        RenderSystem.setShaderFogStart(1.0F);
        RenderSystem.setShaderFogEnd(6.0F);
      }

    });

  }

}
