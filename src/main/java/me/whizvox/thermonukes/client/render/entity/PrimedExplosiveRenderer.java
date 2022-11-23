package me.whizvox.thermonukes.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import me.whizvox.thermonukes.common.entity.AbstractPrimedExplosive;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class PrimedExplosiveRenderer extends EntityRenderer<AbstractPrimedExplosive> {

  private final BlockState blockState;

  public PrimedExplosiveRenderer(EntityRendererProvider.Context context, BlockState blockState) {
    super(context);
    this.blockState = blockState;
  }

  @Override
  public void render(AbstractPrimedExplosive entity, float yaw, float partialTick, PoseStack pose, MultiBufferSource buffer, int packedLight) {
    int packedOverlay;
    if ((entity.getFuse() / 5) % 2 == 0) {
      packedOverlay = OverlayTexture.pack(OverlayTexture.u(1.0F), 10);
    } else {
      packedOverlay = OverlayTexture.NO_OVERLAY;
    }
    pose.pushPose();
    pose.translate(-0.5, 0.0, -0.5);
    Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockState, pose, buffer, packedLight, packedOverlay);
    pose.popPose();
    super.render(entity, yaw, partialTick, pose, buffer, packedLight);
  }

  @Override
  public ResourceLocation getTextureLocation(AbstractPrimedExplosive entity) {
    return TextureAtlas.LOCATION_BLOCKS;
  }

}
