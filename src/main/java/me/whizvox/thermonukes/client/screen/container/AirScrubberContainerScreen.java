package me.whizvox.thermonukes.client.screen.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.whizvox.thermonukes.Thermonukes;
import me.whizvox.thermonukes.common.menu.AirScrubberMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AirScrubberContainerScreen extends AbstractContainerScreen<AirScrubberMenu> {

  private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(Thermonukes.MOD_ID, "textures/gui/container/air_scrubber.png");

  public AirScrubberContainerScreen(AirScrubberMenu menu, Inventory playerInv, Component title) {
    super(menu, playerInv, title);
  }

  @Override
  protected void renderBg(PoseStack pose, float partialTick, int mouseX, int mouseY) {
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
    blit(pose, leftPos, topPos, 0, 0, imageWidth, imageHeight);
  }

  @Override
  public void render(PoseStack pose, int mouseX, int mouseY, float partialTick) {
    renderBackground(pose);
    super.render(pose, mouseX, mouseY, partialTick);
    renderTooltip(pose, mouseX, mouseY);
  }

}
