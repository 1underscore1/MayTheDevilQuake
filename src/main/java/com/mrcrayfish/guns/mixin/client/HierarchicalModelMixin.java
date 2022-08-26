package com.mrcrayfish.guns.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.mrcrayfish.guns.client.handler.AimingHandler;
import com.mrcrayfish.guns.client.handler.GunRenderingHandler;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.interfaces.IGetModelParts;
import com.mrcrayfish.guns.item.GunItem;

import it.unimi.dsi.fastutil.objects.ObjectIterables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.HierarchicalModel;
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

import org.abego.treelayout.internal.util.java.lang.IterableUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.google.common.collect.Iterables;

/**
 * Author: 1_1
 */
@Mixin(HierarchicalModel.class)
public abstract class HierarchicalModelMixin<E extends Entity> implements IGetModelParts
{	
	@Shadow
	abstract ModelPart root();
	
    public Iterable<ModelPart> getAllModelParts() {
    	return this.root().getAllParts().toList();
    }
}