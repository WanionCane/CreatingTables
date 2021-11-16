package wanion.creatingtables;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.creatingtables.block.BlockCreatingTable;
import wanion.creatingtables.proxy.CommonProxy;

import javax.annotation.Nonnull;
import java.util.Map;

import static wanion.creatingtables.Reference.*;

@SuppressWarnings("unused")
@Mod(modid = MOD_ID, name = MOD_NAME, version = MOD_VERSION, dependencies = DEPENDENCIES, acceptedMinecraftVersions = TARGET_MC_VERSION)
public class CreatingTables
{
	public static final CreativeTabs creativeTabs = new CreativeTabs(MOD_ID)
	{
		@Override
		@Nonnull
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem()
		{
			return new ItemStack(BlockCreatingTable.INSTANCE, 1, 0);
		}
	};

	@Mod.Instance
	public static CreatingTables instance;
	@SidedProxy(clientSide = CLIENT_PROXY, serverSide = SERVER_PROXY)
	public static CommonProxy proxy;
	public static SimpleNetworkWrapper networkWrapper;

	@Mod.EventHandler
	public void preInit(final FMLPreInitializationEvent event)
	{
		networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);
		proxy.preInit();
	}

	@Mod.EventHandler
	public void init(final FMLInitializationEvent event)
	{
		proxy.init();
	}

	@NetworkCheckHandler
	public boolean matchModVersions(final Map<String, String> remoteVersions, final Side side)
	{
		return side == Side.CLIENT ? remoteVersions.containsKey(MOD_ID) : !remoteVersions.containsKey(MOD_ID) || remoteVersions.get(MOD_ID).equals(MOD_VERSION);
	}
}