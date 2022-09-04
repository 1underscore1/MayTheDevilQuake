package com.mrcrayfish.guns.entity;

import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.item.GunItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

/**
 * Author: MrCrayfish
 */
public class RocketPodEntity extends ProjectileEntity
{
    public RocketPodEntity(EntityType<? extends ProjectileEntity> entityType, Level world)
    {
        super(entityType, world);
    }

    public RocketPodEntity(EntityType<? extends ProjectileEntity> entityType, Level world, LivingEntity shooter, ItemStack weapon, GunItem item, Gun modifiedGun)
    {
        super(entityType, world, shooter, weapon, item, modifiedGun);
    }

    @Override
    protected void onHitEntity(Entity entity, Vec3 hitVec, Vec3 startVec, Vec3 endVec, boolean headshot)
    {
        createExplosion(this, 1.0F, true);
        super.onHitEntity(entity, hitVec, startVec, endVec, false);
    }

    @Override
    protected void onHitBlock(BlockState state, BlockPos pos, Direction face, double x, double y, double z)
    {
        createExplosion(this, 1.0F, true);
    }

    @Override
    public void onExpired()
    {
        createExplosion(this, 1.0F, true);
    }
    
    @Override
    public boolean ignoreExplosion()
    {
    	return true;
    }
}
