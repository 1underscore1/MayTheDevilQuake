package com.mrcrayfish.guns.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrcrayfish.guns.client.render.layer.StuckProjectileLayer;
import com.mrcrayfish.guns.init.ModItems;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class StuckNailsLayer<T extends LivingEntity, M extends EntityModel<T>> extends StuckProjectileLayer<T, M> {
	public StuckNailsLayer(LivingEntityRenderer<T, M> ler) {
		super(ler);
	}

	@Override
	protected int stuckProjectileCount(T entity) {
		return ModSyncedDataKeys.STUCK_NAILS.getValue(entity);
	}

	@Override
	protected void renderStuckProjectile(PoseStack posestack, MultiBufferSource multibuffersource, int light, Entity p_117569_, float p_117570_, float p_117571_, float p_117572_, float p_117573_) {
		posestack.pushPose();
		posestack.scale(0.2F, 0.2F, 0.2F);
		Minecraft.getInstance().getItemRenderer().renderStatic(ModItems.NAIL.get().getDefaultInstance(), ItemTransforms.TransformType.NONE, 16777215, OverlayTexture.NO_OVERLAY, posestack, multibuffersource, 0);
		posestack.popPose();
	}
}
