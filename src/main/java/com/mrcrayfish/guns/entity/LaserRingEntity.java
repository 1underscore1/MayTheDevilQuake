package com.mrcrayfish.guns.entity;

import java.util.Random;

import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.init.ModParticleTypes;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.client.handler.AimingHandler;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.core.Direction;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

/**
 * Author: 1_1
 */
public class LaserRingEntity extends ProjectileEntity
{
    public LaserRingEntity(EntityType<? extends ProjectileEntity> entityType, Level worldIn)
    {
        super(entityType, worldIn);
    }

    public LaserRingEntity(EntityType<? extends ProjectileEntity> entityType, Level worldIn, LivingEntity shooter, ItemStack weapon, GunItem item, Gun modifiedGun)
    {
        super(entityType, worldIn, shooter, weapon, item, modifiedGun);
    }

    @Override
    protected void onProjectileTick()
    {
        
    }

    @Override
    protected void onHitEntity(Entity entity, Vec3 hitVec, Vec3 startVec, Vec3 endVec, boolean headshot)
    {
    	super.onHitEntity(entity, hitVec, startVec, endVec, false);
    	makeParticles();
    }

    @Override
    protected void onHitBlock(BlockState state, BlockPos pos, Direction face, double x, double y, double z)
    {
    	makeParticles();
    }

    @Override
    public void onExpired()
    {
    	makeParticles();
    }
    
    private void makeParticles()
    {
    	if (!this.level.isClientSide)
        {
            
            ((ServerLevel) this.level).sendParticles(ModParticleTypes.LASER_BLAST.get(), this.getX(), this.getY(), this.getZ(), 5, 0, 0, 0, 0.1);
            
        }
    }
}
