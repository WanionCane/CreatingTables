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
import wanion.creatingtables.block.ContainerCreatingTable;
import wanion.creatingtables.block.TileEntityCreatingTable;
import wanion.creatingtables.common.AvaritiaHelper;

import javax.annotation.Nonnull;

public final class ContainerExtremeCreatingTable extends ContainerCreatingTable
{
	public ContainerExtremeCreatingTable(@Nonnull final TileEntityCreatingTable tileEntityCreatingTable, final InventoryPlayer inventoryPlayer)
	{
		super(8, 18, 8, 194, 183, 90, tileEntityCreatingTable, inventoryPlayer);
	}

	@Override
	public void defineShape(@Nonnull final ResourceLocation resourceLocation)
	{
		AvaritiaHelper.defineShape(this, resourceLocation);
	}
}