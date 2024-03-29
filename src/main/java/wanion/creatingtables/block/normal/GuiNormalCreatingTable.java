package wanion.creatingtables.block.normal;

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

@SideOnly(Side.CLIENT)
public final class GuiNormalCreatingTable extends GuiCreatingTable<TileEntityNormalCreatingTable>
{
	private static final ResourceLocation normalCreatingTexture = new ResourceLocation("textures/gui/container/crafting_table.png");

	public GuiNormalCreatingTable(@Nonnull final TileEntityNormalCreatingTable tileEntityNormalCreatingTable, final InventoryPlayer inventoryPlayer)
	{
		super(new ContainerNormalCreatingTable(tileEntityNormalCreatingTable, inventoryPlayer), normalCreatingTexture);
		xSize = 176;
		ySize = 166;
		addElement(new CheckBoxWElement((CheckBox) getField("creating.remove_old_recipe"), this, guiLeft + outputSlot.xPos - 32, guiTop + outputSlot.yPos - 28));
	}
}