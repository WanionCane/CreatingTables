package wanion.creatingtables.proxy;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import wanion.creatingtables.CreatingTables;
import wanion.creatingtables.block.BlockCreatingTable;
import wanion.creatingtables.block.ItemBlockCreatingTable;
import wanion.creatingtables.block.TileEntityCreatingTable;
import wanion.creatingtables.block.extreme.ContainerExtremeCreatingTable;
import wanion.creatingtables.block.extreme.GuiExtremeCreatingTable;
import wanion.creatingtables.block.extreme.TileEntityExtremeCreatingTable;
import wanion.creatingtables.block.normal.ContainerNormalCreatingTable;
import wanion.creatingtables.block.normal.GuiNormalCreatingTable;
import wanion.creatingtables.block.normal.TileEntityNormalCreatingTable;
import wanion.creatingtables.network.ClearShapeMessage;
import wanion.creatingtables.network.CreatingGhostTransferMessage;
import wanion.creatingtables.network.CreatingJeiTransferMessage;

import javax.annotation.Nonnull;

import static wanion.creatingtables.CreatingTables.networkWrapper;
import static wanion.creatingtables.Reference.MOD_ID;

@SuppressWarnings("unused")
public class CommonProxy implements IGuiHandler
{
	public final void preInit()
	{
		MinecraftForge.EVENT_BUS.register(this);
		NetworkRegistry.INSTANCE.registerGuiHandler(CreatingTables.instance, this);
		GameRegistry.registerTileEntity(TileEntityNormalCreatingTable.class, new ResourceLocation(MOD_ID, "normalcreatingtable"));
		GameRegistry.registerTileEntity(TileEntityExtremeCreatingTable.class, new ResourceLocation(MOD_ID, "extremecreatingtable"));
		int d = 0;
		networkWrapper.registerMessage(ClearShapeMessage.Handler.class, ClearShapeMessage.class, d++, Side.SERVER);
		networkWrapper.registerMessage(ClearShapeMessage.Handler.class, ClearShapeMessage.class, d++, Side.CLIENT);
		networkWrapper.registerMessage(CreatingGhostTransferMessage.Handler.class, CreatingGhostTransferMessage.class, d++, Side.SERVER);
		networkWrapper.registerMessage(CreatingGhostTransferMessage.Handler.class, CreatingGhostTransferMessage.class, d++, Side.CLIENT);
		networkWrapper.registerMessage(CreatingJeiTransferMessage.Handler.class, CreatingJeiTransferMessage.class, d++, Side.SERVER);
		networkWrapper.registerMessage(CreatingJeiTransferMessage.Handler.class, CreatingJeiTransferMessage.class, d, Side.CLIENT);
	}

	public void init() {}

	public void postInit() {}

	@SubscribeEvent
	public void registerItems(final RegistryEvent.Register<Item> event)
	{
		event.getRegistry().register(ItemBlockCreatingTable.INSTANCE);
	}

	@SubscribeEvent
	public void registerBlocks(final RegistryEvent.Register<Block> event)
	{
		event.getRegistry().register(BlockCreatingTable.INSTANCE);
	}

	@SubscribeEvent
	public void modelRegistryEvent(final ModelRegistryEvent event)
	{
		modelInit();
	}

	public void modelInit() {}

	@Override
	public Object getServerGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z)
	{
		final TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if (!(tileEntity instanceof TileEntityCreatingTable))
			return null;
		switch (((TileEntityCreatingTable) tileEntity).getTableType()) {
			case NORMAL:
				return new ContainerNormalCreatingTable((TileEntityNormalCreatingTable) tileEntity, player.inventory);
			case EXTREME:
				return new ContainerExtremeCreatingTable((TileEntityExtremeCreatingTable) tileEntity, player.inventory);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z)
	{
		final TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if (!(tileEntity instanceof TileEntityCreatingTable))
			return null;
		switch (((TileEntityCreatingTable) tileEntity).getTableType()) {
			case NORMAL:
				return new GuiNormalCreatingTable((TileEntityNormalCreatingTable) tileEntity, player.inventory);
			case EXTREME:
				return new GuiExtremeCreatingTable((TileEntityExtremeCreatingTable) tileEntity, player.inventory);
		}
		return null;
	}

	public EntityPlayer getEntityPlayerFromContext(@Nonnull final MessageContext messageContext)
	{
		return messageContext.getServerHandler().player;
	}

	public final boolean isClient()
	{
		return FMLCommonHandler.instance().getEffectiveSide().isClient();
	}

	public final boolean isServer()
	{
		return FMLCommonHandler.instance().getEffectiveSide().isServer();
	}

	public IThreadListener getThreadListener()
	{
		return FMLCommonHandler.instance().getMinecraftServerInstance();
	}
}