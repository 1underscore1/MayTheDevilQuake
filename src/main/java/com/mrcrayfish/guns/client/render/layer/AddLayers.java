package com.mrcrayfish.guns.client.render.layer;

import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.interfaces.IGetModelParts;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AddLayers {
	@SubscribeEvent
    public static void onAddRenderLayers(EntityRenderersEvent.AddLayers event)
    {
		/*
		 * LivingEntityRenderer<PolarBear, AgeableListModel<PolarBear>>
		 * PolarBearRenderer = event.getRenderer(EntityType.POLAR_BEAR);
		 * PolarBearRenderer.addLayer(new StuckNailsLayer<>(PolarBearRenderer)); int
		 * num1 = AddLayersIfApplicable(event, EntityType.BLAZE); int num2 =
		 * AddLayersIfApplicable(event, EntityType.ZOMBIE); int num3 =
		 * AddLayersIfApplicable(event, EntityType.GLOW_ITEM_FRAME);
		 * GunMod.LOGGER.debug("First result (should be 1): " + Integer.toString(num1));
		 * GunMod.LOGGER.debug("Second result (should be 2): " +
		 * Integer.toString(num2)); GunMod.LOGGER.debug("Third result (should be 0): " +
		 * Integer.toString(num3));
		 */
		ForgeRegistries.ENTITIES.forEach(Entitytype -> {
			AddLayersIfApplicable(event, Entitytype);
		});
    }
	@SuppressWarnings("unchecked")
	public static <T extends Entity> String AddLayersIfApplicable(EntityRenderersEvent.AddLayers event, EntityType<T> Entitytype) {
		try
		{
			if (Entitytype.getRegistryName() == EntityType.IRON_GOLEM.getRegistryName()) {
				GunMod.LOGGER.debug("Iron Golem Found");
			}
			String result = AddLayersIfGoodModel(event.getRenderer((EntityType<? extends LivingEntity>) Entitytype));
			if (Entitytype.getRegistryName() == EntityType.IRON_GOLEM.getRegistryName()) {
				GunMod.LOGGER.debug("Result is " + result);
			}
			return "Success";
		}
		catch(ClassCastException e)
		{
			return "Class Cast Exception";
		}
		
	}
	
	public static <T extends LivingEntity, M extends EntityModel<T>> String AddLayersIfGoodModel(LivingEntityRenderer<T, M> ler) {
		if (ler == null) {
			return "Renderer was null";
		}
		
		EntityModel<T> m = (EntityModel<T>) ler.getModel();
		if (m instanceof AgeableListModel<T>) {
			ler.addLayer(new StuckNailsLayer<>(ler));
			return "Successfully added render layer for AgeableListModel";
		}
		if (m instanceof HierarchicalModel<T>) {
			ler.addLayer(new StuckNailsLayer<>(ler));
			return "Successfully added render layer for HierarchicalModel";
		}
		return "EntityModel did not implement IGetModelParts";
	}
}
