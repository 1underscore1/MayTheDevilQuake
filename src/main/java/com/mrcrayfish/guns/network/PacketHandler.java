package com.mrcrayfish.guns.network;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.network.message.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.FMLHandshakeHandler;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class PacketHandler
{
    public static final String PROTOCOL_VERSION = "1";
    private static final SimpleChannel HANDSHAKE_CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(Reference.MOD_ID, "handshake"), () -> PROTOCOL_VERSION, s -> true, s -> true);
    private static final SimpleChannel PLAY_CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(Reference.MOD_ID, "play"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);;
    private static int nextMessageId = 0;

    public static void init()
    {
        HANDSHAKE_CHANNEL.messageBuilder(HandshakeMessages.C2SAcknowledge.class, 99)
                .loginIndex(HandshakeMessages.LoginIndexedMessage::getLoginIndex, HandshakeMessages.LoginIndexedMessage::setLoginIndex)
                .decoder(HandshakeMessages.C2SAcknowledge::decode)
                .encoder(HandshakeMessages.C2SAcknowledge::encode)
                .consumer(FMLHandshakeHandler.indexFirst((handler, msg, s) -> HandshakeHandler.handleAcknowledge(msg, s)))
                .add();

        HANDSHAKE_CHANNEL.messageBuilder(HandshakeMessages.S2CUpdateGuns.class, 1)
                .loginIndex(HandshakeMessages.LoginIndexedMessage::getLoginIndex, HandshakeMessages.LoginIndexedMessage::setLoginIndex)
                .decoder(HandshakeMessages.S2CUpdateGuns::decode)
                .encoder(HandshakeMessages.S2CUpdateGuns::encode)
                .consumer(FMLHandshakeHandler.biConsumerFor((handler, msg, supplier) -> HandshakeHandler.handleUpdateGuns(msg, supplier)))
                .markAsLoginPacket()
                .add();

        registerPlayMessage(MessageAim.class, MessageAim::new, LogicalSide.SERVER);
        registerPlayMessage(MessageReload.class, MessageReload::new, LogicalSide.SERVER);
        registerPlayMessage(MessageShoot.class, MessageShoot::new, LogicalSide.SERVER);
        registerPlayMessage(MessageUnload.class, MessageUnload::new, LogicalSide.SERVER);
        registerPlayMessage(MessageStunGrenade.class, MessageStunGrenade::new, LogicalSide.CLIENT);
        registerPlayMessage(MessageCraft.class, MessageCraft::new, LogicalSide.SERVER);
        registerPlayMessage(MessageBulletTrail.class, MessageBulletTrail::new, LogicalSide.CLIENT);
        registerPlayMessage(MessageAttachments.class, MessageAttachments::new, LogicalSide.SERVER);
        registerPlayMessage(MessageUpdateGuns.class, MessageUpdateGuns::new, LogicalSide.CLIENT);
        registerPlayMessage(MessageBlood.class, MessageBlood::new, LogicalSide.CLIENT);
        registerPlayMessage(MessageShooting.class, MessageShooting::new, LogicalSide.SERVER);
        registerPlayMessage(MessageGunSound.class, MessageGunSound::new, LogicalSide.CLIENT);
        registerPlayMessage(MessageProjectileHitBlock.class, MessageProjectileHitBlock::new, LogicalSide.CLIENT);
        registerPlayMessage(MessageProjectileHitEntity.class, MessageProjectileHitEntity::new, LogicalSide.CLIENT);
        registerPlayMessage(MessageRemoveProjectile.class, MessageRemoveProjectile::new, LogicalSide.CLIENT);
    }

    /**
     * Register an {@link IMessage} to the play network channel.
     *
     * @param clazz the class of the message
     * @param messageSupplier a supplier to create an get of the message
     * @param side the logical side this message is to be handled on
     * @param <T> inferred by first parameter, class must implement {@link IMessage}
     */
    private static <T extends IMessage> void registerPlayMessage(Class<T> clazz, Supplier<T> messageSupplier, LogicalSide side)
    {
        PLAY_CHANNEL.registerMessage(nextMessageId++, clazz, IMessage::encode, buffer -> {
            T t = messageSupplier.get();
            t.decode(buffer);
            return t;
        }, (t, supplier) -> {
            if(supplier.get().getDirection().getReceptionSide() != side)
                throw new RuntimeException("Attempted to handle message " + clazz.getSimpleName() + " on the wrong logical side!");
            t.handle(supplier);
        });
    }

    /**
     * Gets the handshake network channel for MrCrayfish's Gun Mod
     */
    public static SimpleChannel getHandshakeChannel()
    {
        return HANDSHAKE_CHANNEL;
    }

    /**
     * Gets the play network channel for MrCrayfish's Gun Mod
     */
    public static SimpleChannel getPlayChannel()
    {
        return PLAY_CHANNEL;
    }
}
