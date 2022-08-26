package com.mrcrayfish.guns.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import com.mojang.math.Quaternion;
import net.minecraft.world.phys.Vec3;
import com.mojang.math.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Author: 1_1
 */
@OnlyIn(Dist.CLIENT)
public class LaserBlastParticle extends TextureSheetParticle
{
    public LaserBlastParticle(ClientLevel world, double x, double y, double z)
    {
        super(world, x, y, z, 0.1, 0.1, 0.1);
        this.gravity = 0.0F;
        this.quadSize = 0.03F;
        this.lifetime = 5;
        this.friction = 0.9F;
    }

    @Override
    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
    
    @Override
    public void tick()
    {
        super.tick();
        this.quadSize += 0.01F;
        this.alpha -= 0.2F;
    }
    
    @Override
    public int getLightColor(float light)
    {
    	return 240;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet)
        {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            LaserBlastParticle particle = new LaserBlastParticle(worldIn, x, y, z);
            particle.setParticleSpeed(xSpeed, ySpeed, zSpeed);
            particle.pickSprite(this.spriteSet);
            return particle;
        }
    }
}
