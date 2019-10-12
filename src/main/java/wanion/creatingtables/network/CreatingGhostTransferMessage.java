package wanion.creatingtables.network;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import wanion.creatingtables.CreatingTables;
import wanion.lib.common.IGhostAcceptorContainer;

public class CreatingGhostTransferMessage implements IMessage
{
	private int windowId, slot;
	private ItemStack itemStack;

	public CreatingGhostTransferMessage() {}

	public CreatingGhostTransferMessage(final int windowId, final int slot, final ItemStack itemStack)
	{
		this.windowId = windowId;
		this.slot = slot;
		this.itemStack = itemStack;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.windowId = ByteBufUtils.readVarInt(buf, 5);
		slot = ByteBufUtils.readVarInt(buf, 5);
		itemStack = ByteBufUtils.readItemStack(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeVarInt(buf, windowId, 5);
		ByteBufUtils.writeVarInt(buf, slot, 5);
		ByteBufUtils.writeItemStack(buf, itemStack);
	}

	public static class Handler implements IMessageHandler<CreatingGhostTransferMessage, IMessage>
	{
		@Override
		public IMessage onMessage(final CreatingGhostTransferMessage message, final MessageContext ctx)
		{
			CreatingTables.proxy.getThreadListener().addScheduledTask(() -> {
				final EntityPlayer entityPlayer = CreatingTables.proxy.getEntityPlayerFromContext(ctx);
				if (entityPlayer != null && entityPlayer.openContainer instanceof IGhostAcceptorContainer && entityPlayer.openContainer.windowId == message.windowId)
					((IGhostAcceptorContainer) entityPlayer.openContainer).acceptGhostStack(message.slot, message.itemStack);

			});
			return null;
		}
	}
}