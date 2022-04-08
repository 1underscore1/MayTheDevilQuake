package com.mrcrayfish.guns.mixin.common;

import com.mrcrayfish.guns.entity.ProjectileEntity;
import net.minecraft.entity.boss.dragon.phase.SittingPhase;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Author: MrCrayfish
 */
@Mixin(SittingPhase.class)
public class SittingPhaseMixin
{
    @Inject(method = "onHurt", at = @At(value = "HEAD"), cancellable = true)
    public void sittingPhaseMixin(DamageSource source, float damage, CallbackInfoReturnable<Float> cir)
    {
        if(source.getDirectEntity() instanceof ProjectileEntity)
        {
            cir.setReturnValue(0.0F);
        }
    }
}
