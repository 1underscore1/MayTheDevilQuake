package com.mrcrayfish.guns.client.handler;

import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.common.SpecialAttributeType;
import com.mrcrayfish.guns.event.GunFireEvent;
import com.mrcrayfish.guns.event.GunReloadEvent;
import com.mrcrayfish.guns.init.ModEffects;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageAim;
import com.mrcrayfish.guns.network.message.MessageBFGSickness;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Author: 1_1
 */

public class BFGSicknessHandler {
	private static BFGSicknessHandler instance;
	
	public static BFGSicknessHandler get()
	{
		if(instance == null)
		{
			instance = new BFGSicknessHandler();
		}
		return instance;
	}
	
	private int BFGSicknessStage = 0;
	private int BFGSlot = -99;
	private int BFGSicknessTicks = 0;
	
	private BFGSicknessHandler() {}
	
	@SubscribeEvent
	public void afterGunFired(GunFireEvent.Post event)
	{
		if (!event.getPlayer().isLocalPlayer())
		{
			return;
		}
		if (event.getStack().getItem() instanceof GunItem gunItem)
		{
			Gun gun = gunItem.getModifiedGun(event.getStack());
			if (gun.getSpecial().hasProperty(SpecialAttributeType.BFG) && this.BFGSicknessStage == 0)
			{
				this.BFGSicknessStage = 1;
				this.BFGSlot = event.getPlayer().getInventory().selected;
				this.BFGSicknessTicks = (int) gun.getSpecial().getPropertyValue(SpecialAttributeType.BFG);
			}
		}
	}
	
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event)
	{
		if(event.phase != TickEvent.Phase.END)
		{
			return;
		}
		Player player = Minecraft.getInstance().player;
		if(player != null)
		{
			if(this.BFGSicknessStage == 1)
			{
				if(this.BFGSlot == player.getInventory().selected)
				{
					ItemStack stack = player.getMainHandItem();
					if(stack.getItem() instanceof GunItem)
					{
						if (Gun.hasAmmo(stack))
						{
							return;
						}
					}
				}
				this.applySickness(player);
				return;
				
			}
			if(this.BFGSicknessStage == 2 && player.hasEffect(ModEffects.BFG_SICKNESS.get()))
			{
				this.BFGSicknessStage = 3;
				return;
			}
			if(this.BFGSicknessStage == 3 && !player.hasEffect(ModEffects.BFG_SICKNESS.get()))
			{
				this.BFGSicknessStage = 0;
				return;
			}
			
		}
	}
	
	@SubscribeEvent
	public void postReload(GunReloadEvent.Post event)
	{
		if(!event.getPlayer().isLocalPlayer())
		{
			return;
		}
		if(this.BFGSicknessStage == 1)
		{
			applySickness(event.getPlayer());
		}
	}
	
	public void applySickness(Player player)
	{
        //PacketHandler.getPlayChannel().sendToServer(new MessageBFGSickness(this.BFGSicknessTicks));
		this.BFGSicknessStage = 2;
		this.BFGSlot = -99;
	}
}
