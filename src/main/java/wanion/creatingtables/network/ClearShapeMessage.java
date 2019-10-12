package wanion.creatingtables.network;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import wanion.creatingtables.CreatingTables;
import wanion.lib.common.IResourceShapedContainer;
import wanion.lib.network.EmptyMessage;

public class ClearShapeMessage extends EmptyMessage
{
	public ClearShapeMessage() {}

	public ClearShapeMessage(final int windowId)
	{
		super(windowId);
	}

	public static class Handler implements IMessageHandler<ClearShapeMessage, IMessage>
	{
		@Override
		public IMessage onMessage(final ClearShapeMessage message, final MessageContext ctx)
		{
			CreatingTables.proxy.getThreadListener().addScheduledTask(() -> {
				final EntityPlayer entityPlayer = CreatingTables.proxy.getEntityPlayerFromContext(ctx);
				if (entityPlayer != null && entityPlayer.openContainer instanceof IResourceShapedContainer && entityPlayer.openContainer.windowId == message.windowId)
					((IResourceShapedContainer) entityPlayer.openContainer).clearShape();
			});
			return null;
		}
	}
}