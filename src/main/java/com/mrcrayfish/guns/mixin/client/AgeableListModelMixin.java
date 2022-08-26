package com.mrcrayfish.guns.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.mrcrayfish.guns.client.handler.AimingHandler;
import com.mrcrayfish.guns.client.handler.GunRenderingHandler;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.interfaces.IGetModelParts;
import com.mrcrayfish.guns.item.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.google.common.collect.Iterables;

/**
 * Author: 1_1
 */
@Mixin(AgeableListModel.class)
public abstract class AgeableListModelMixin<E extends Entity> implements IGetModelParts
{
	@Shadow
	abstract Iterable<ModelPart> bodyParts();
	@Shadow
	abstract Iterable<ModelPart> headParts();
	
    public Iterable<ModelPart> getAllModelParts() {
    	return Iterables.concat(this.bodyParts(), this.headParts());
    }
}
