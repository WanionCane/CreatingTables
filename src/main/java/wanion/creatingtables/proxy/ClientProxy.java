package wanion.creatingtables.proxy;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import wanion.creatingtables.Reference;
import wanion.creatingtables.block.BlockCreatingTable;

import javax.annotation.Nonnull;

public final class ClientProxy extends CommonProxy
{
	@Override
	public void init() {}

	public void modelInit()
	{
		// Big
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockCreatingTable.INSTANCE), 0, new ModelResourceLocation(Reference.MOD_ID + ":creatingtables", "tabletype=normal"));

		// Giant
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockCreatingTable.INSTANCE), 1, new ModelResourceLocation(Reference.MOD_ID + ":creatingtables", "tabletype=extreme"));
	}

	@Override
	public EntityPlayer getEntityPlayerFromContext(@Nonnull final MessageContext messageContext)
	{
		return messageContext.side.isClient() ? Minecraft.getMinecraft().player : super.getEntityPlayerFromContext(messageContext);
	}

	@Override
	public IThreadListener getThreadListener()
	{
		return Minecraft.getMinecraft();
	}
}