package com.mrcrayfish.guns.effect;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.common.SpecialAttributeType;
import com.mrcrayfish.guns.event.GunFireEvent;
import com.mrcrayfish.guns.init.ModEffects;
import com.mrcrayfish.guns.item.GunItem;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class BFGSicknessEffect extends MobEffect {

    public BFGSicknessEffect(MobEffectCategory typeIn, int liquidColorIn)
    {
        super(typeIn, liquidColorIn);
    }
    
    @SubscribeEvent
    public static void canFireBFG(GunFireEvent.Pre event)
    {
    	if (event.getStack().getItem() instanceof GunItem gunItem)
    	{
    		if (gunItem.getModifiedGun(event.getStack()).getSpecial().hasProperty(SpecialAttributeType.BFG))
    		{
    			if (event.getPlayer().hasEffect(ModEffects.BFG_SICKNESS.get()))
    			{
    				event.setCanceled(true);
    			}
    		}
    	}
    }
}
