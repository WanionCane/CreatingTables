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
import wanion.lib.client.gui.field.CheckBoxWElement;
import wanion.lib.common.field.CheckBox;

import javax.annotation.Nonnull;

import static wanion.creatingtables.Reference.MOD_ID;

@SideOnly(Side.CLIENT)
public final class GuiExtremeCreatingTable extends GuiCreatingTable<TileEntityExtremeCreatingTable>
{
	private static final ResourceLocation extremeCreatingTexture = new ResourceLocation(MOD_ID, "textures/gui/extreme_creating_table.png");

	public GuiExtremeCreatingTable(@Nonnull final TileEntityExtremeCreatingTable tileEntityExtremeCreatingTable, final InventoryPlayer inventoryPlayer)
	{
		super(new ContainerExtremeCreatingTable(tileEntityExtremeCreatingTable, inventoryPlayer), extremeCreatingTexture);
		xSize = 211;
		ySize = 276;
		addElement(new CheckBoxWElement((CheckBox) getField("creating.remove_old_recipe"), this, guiLeft + outputSlot.xPos - 10, guiTop + outputSlot.yPos - 50));
	}
}