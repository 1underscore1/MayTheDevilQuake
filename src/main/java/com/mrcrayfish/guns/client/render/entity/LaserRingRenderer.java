package com.mrcrayfish.guns.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mrcrayfish.guns.client.GunRenderType;
import com.mrcrayfish.guns.client.MuzzleFlashType;
import com.mrcrayfish.guns.client.SpecialModels;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.entity.LaserRingEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;

/**
 * Author: 1_1
 */
public class LaserRingRenderer extends EntityRenderer<LaserRingEntity>
{
	private static final ResourceLocation LASER_RING_LOCATION = new ResourceLocation("cgm:textures/items/laser_ring.png");
    public LaserRingRenderer(EntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(LaserRingEntity entity)
    {
        return null;
    }

    @Override
    public void render(LaserRingEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource renderTypeBuffer, int light)
    {
        if(!entity.getProjectile().isVisible() || entity.tickCount <= -1)
        {
            return;
        }

        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180F));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(entityYaw));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(entity.getXRot() - 0F));
        poseStack.scale(0.2F, 0.2F, 0.2F);
        
        float minU = 0.0F;
        float maxU = 0.5F;
        float mintypeU = 0.0F;
        float maxtypeU = 0.5F;
        float size = 0.5F;
        VertexConsumer builder = renderTypeBuffer.getBuffer(GunRenderType.getEnergyProjectile(LASER_RING_LOCATION));
        for(int i = 0; i < 2; ++i) {
            poseStack.mulPose(Vector3f.YP.rotationDegrees(180F));
	        Matrix4f matrix = poseStack.last().pose();
	        Matrix3f matrix3f = poseStack.last().normal();
	        builder.vertex(matrix, -size, -size, 0).color(1.0F, 1.0F, 1.0F, 1.0F).uv(maxU, maxtypeU).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
	        builder.vertex(matrix, size, -size, 0).color(1.0F, 1.0F, 1.0F, 1.0F).uv(minU, maxtypeU).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
	        builder.vertex(matrix, size, size, 0).color(1.0F, 1.0F, 1.0F, 1.0F).uv(minU, mintypeU).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
	        builder.vertex(matrix, -size, size, 0).color(1.0F, 1.0F, 1.0F, 1.0F).uv(maxU, mintypeU).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
	    }
        //Minecraft.getInstance().getItemRenderer().renderStatic(entity.getItem(), ItemTransforms.TransformType.NONE, 16777215, OverlayTexture.NO_OVERLAY, poseStack, renderTypeBuffer, 0);
        poseStack.popPose();
    }
}
