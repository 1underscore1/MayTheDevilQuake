package com.mrcrayfish.guns.client.render.layer;

import java.util.Random;

import com.google.common.collect.Iterables;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.mrcrayfish.guns.interfaces.IGetModelParts;
import com.mrcrayfish.guns.mixin.client.AgeableListModelMixin;

@OnlyIn(Dist.CLIENT)
public abstract class StuckProjectileLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
   public StuckProjectileLayer(LivingEntityRenderer<T, M> ler) {
      super(ler);
   }

   protected abstract int stuckProjectileCount(T p_117565_);

   protected abstract void renderStuckProjectile(PoseStack p_117566_, MultiBufferSource p_117567_, int light, Entity p_117569_, float p_117570_, float p_117571_, float p_117572_, float p_117573_);

   public void render(PoseStack posestack, MultiBufferSource multibuffersource, int light, T p_117589_, float p_117590_, float p_117591_, float p_117592_, float p_117593_, float p_117594_, float p_117595_) {
      int i = this.stuckProjectileCount(p_117589_);
      Random random = new Random((long)p_117589_.getId());
      if (i > 0) {
    	  M parentmodel = this.getParentModel();
          Iterable<ModelPart> modelparts = ((IGetModelParts) parentmodel).getAllModelParts();
          int size = Iterables.size(modelparts);
         for(int j = 0; j < i; ++j) {
            
        	int c = random.nextInt(6);
            float f = random.nextFloat();
            float f1 = random.nextFloat();
            float f2 = random.nextFloat();
            float c1 = (c % 2 == 0) ? 0.0F : 1.0F;
            f = (c % 3 == 0) ? c1 : f;
            f1 = (c % 3 == 1) ? c1 : f1;
            f2 = (c % 3 == 2) ? c1 : f2;

            ModelPart modelpart = Iterables.get(modelparts, random.nextInt(size));
        	if (modelpart.isEmpty()) {
        		
        	}
        	else
        	{
	            ModelPart.Cube modelpart$cube = modelpart.getRandomCube(random);
	            
	            float f3 = Mth.lerp(f, modelpart$cube.minX, modelpart$cube.maxX) / 16.0F;
	            float f4 = Mth.lerp(f1, modelpart$cube.minY, modelpart$cube.maxY) / 16.0F;
	            float f5 = Mth.lerp(f2, modelpart$cube.minZ, modelpart$cube.maxZ) / 16.0F;
	            posestack.pushPose();
	            modelpart.translateAndRotate(posestack);
	            posestack.translate((double)f3, (double)f4, (double)f5);
	            f = -1.0F * (f * 2.0F - 1.0F);
	            f1 = -1.0F * (f1 * 2.0F - 1.0F);
	            f2 = -1.0F * (f2 * 2.0F - 1.0F);
	            float f6 = Mth.sqrt(f * f + f2 * f2);
	            float f7 = ((float)(Math.atan2((double)f, (double)f2) * (double)(180F / (float)Math.PI)));
	            float f8 = ((float)(Math.atan2((double)f1, (double)f6) * (double)(180F / (float)Math.PI)));
	            posestack.mulPose(Vector3f.YP.rotationDegrees(180F));
	            posestack.mulPose(Vector3f.YP.rotationDegrees(f7));
	            posestack.mulPose(Vector3f.XP.rotationDegrees(f8 - 90.0F));
	            posestack.translate(0D, 0D, 0D);
	            this.renderStuckProjectile(posestack, multibuffersource, light, p_117589_, f, f1, f2, p_117592_);
	            posestack.popPose();
        	}
         }

      }
   }
}