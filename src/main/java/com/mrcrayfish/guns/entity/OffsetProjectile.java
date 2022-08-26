package com.mrcrayfish.guns.entity;

import com.mrcrayfish.guns.entity.ProjectileEntity;

import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.common.BoundingBoxManager;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.common.Gun.Projectile;
import com.mrcrayfish.guns.common.SpreadTracker;
import com.mrcrayfish.guns.event.GunProjectileHitEvent;
import com.mrcrayfish.guns.init.ModEnchantments;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.guns.interfaces.IDamageable;
import com.mrcrayfish.guns.interfaces.IExplosionDamageable;
import com.mrcrayfish.guns.interfaces.IHeadshotBox;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageBlood;
import com.mrcrayfish.guns.network.message.MessageProjectileHitBlock;
import com.mrcrayfish.guns.network.message.MessageProjectileHitEntity;
import com.mrcrayfish.guns.network.message.MessageRemoveProjectile;
import com.mrcrayfish.guns.util.BufferUtil;
import com.mrcrayfish.guns.util.GunEnchantmentHelper;
import com.mrcrayfish.guns.util.GunModifierHelper;
import com.mrcrayfish.guns.util.ReflectionUtil;
import com.mrcrayfish.guns.util.math.ExtendedEntityRayTraceResult;
import com.mrcrayfish.guns.world.ProjectileExplosion;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.TargetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;


import net.minecraft.client.Minecraft;
import com.mrcrayfish.guns.client.handler.AimingHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class OffsetProjectile extends ProjectileEntity
{
	
	abstract protected Vec3 GetOffsetRight();
	abstract protected Vec3 GetOffsetCenter();
	
	public OffsetProjectile(EntityType<? extends ProjectileEntity> entityType, Level worldIn)
    {
        super(entityType, worldIn);
    }
	
	public OffsetProjectile(EntityType<? extends ProjectileEntity> entityType, Level worldIn, LivingEntity shooter, ItemStack weapon, GunItem item, Gun modifiedGun)
	{
		super(entityType, worldIn, shooter, weapon, item, modifiedGun);
		this.setPos(this.position().add(this.getProjectileOffset(shooter)));
		this.setPos(this.position().add(new Vec3(0.0, 3.0, 0.0)));
	}
	
	public Vec3 getProjectileOffset(Entity shooter)
    {
        float aimProgress = 0.0F;
        boolean leftHanded = false;
        if (shooter instanceof Player) { // no idea why they wouldn't be but i'll check anyway
        	Player player = ((Player) shooter);
        	AimingHandler aimingHandler = AimingHandler.get();
        	aimProgress = aimingHandler.getAimProgress(player, Minecraft.getInstance().getFrameTime());
        	leftHanded = (player.getMainArm() == HumanoidArm.LEFT);
        }
        Vec3 dir0 = this.GetOffsetRight().lerp(this.GetOffsetCenter(), (double) aimProgress);
        Vec3 dir1 = this.getVectorFromRotation(shooter.getXRot(), shooter.getYRot()).scale(dir0.x()); // forward
        Vec3 dir2 = this.getVectorFromRotation(0.0F, shooter.getYRot() + 90.0F).scale(dir0.y()); // to the right
        if (leftHanded) {
        	dir2 = dir2.scale(-1D); // actually to the left, if you're left handed
        }
        Vec3 dir3 = this.getVectorFromRotation(shooter.getXRot() + 90.0F, shooter.getYRot()).scale(dir0.z()); // down
        return dir1.add(dir2).add(dir3);
    }
	
	@Override
	public void tick()
	{
		//when projectiles are created by guns, they get ticked once. this gets around that so they appear at the specified offset position.
		if (this.firstTick)
		{
			super.tick();
		}
		else
		{
			super.tick();
		}
	}
    
    private Vec3 getVectorFromRotation(float pitch, float yaw)
    {
        float f = Mth.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = Mth.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -Mth.cos(-pitch * 0.017453292F);
        float f3 = Mth.sin(-pitch * 0.017453292F);
        return new Vec3((double) (f1 * f2), (double) f3, (double) (f * f2));
    }
}