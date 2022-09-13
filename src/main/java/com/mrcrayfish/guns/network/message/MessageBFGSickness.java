package com.mrcrayfish.guns.network.message;

import java.util.function.Supplier;

import com.mrcrayfish.framework.api.network.PlayMessage;
import com.mrcrayfish.guns.init.ModEffects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.network.NetworkEvent;

public class MessageBFGSickness extends PlayMessage<MessageBFGSickness> {
	private int tickCount;
	
	public MessageBFGSickness() {}
	
	public MessageBFGSickness(int tickCount)
	{
		this.tickCount = tickCount;
	}
	
	@Override
	public void encode(MessageBFGSickness message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.tickCount);
	}
	
	@Override
	public MessageBFGSickness decode(FriendlyByteBuf buffer)
	{
		return new MessageBFGSickness(buffer.readInt());
	}
	


	@Override
	public void handle(MessageBFGSickness message, Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() ->
		{
			ServerPlayer player = supplier.get().getSender();
			if(player != null && !player.isSpectator())
			{
				player.addEffect(new MobEffectInstance(ModEffects.BFG_SICKNESS.get(), message.tickCount));
			}
		});
		supplier.get().setPacketHandled(true);
	}
}
