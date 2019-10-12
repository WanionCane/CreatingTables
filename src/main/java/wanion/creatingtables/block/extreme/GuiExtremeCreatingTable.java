package wanion.creatingtables.block.extreme;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.creatingtables.block.GuiCreatingTable;
import wanion.creatingtables.block.TileEntityCreatingTable;

import javax.annotation.Nonnull;

import static wanion.creatingtables.Reference.MOD_ID;

@SideOnly(Side.CLIENT)
public final class GuiExtremeCreatingTable extends GuiCreatingTable
{
	private static final ResourceLocation extremeCreatingTexture = new ResourceLocation(MOD_ID, "textures/gui/extreme_creating_table.png");

	public GuiExtremeCreatingTable(@Nonnull final TileEntityCreatingTable tileEntityCreatingTable, final InventoryPlayer inventoryPlayer)
	{
		super(tileEntityCreatingTable, extremeCreatingTexture, new ContainerExtremeCreatingTable(tileEntityCreatingTable, inventoryPlayer));
		xSize = 211;
		ySize = 276;
	}
}