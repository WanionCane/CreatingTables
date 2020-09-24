package wanion.creatingtables.block.extreme;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.creatingtables.Reference;
import wanion.creatingtables.block.TileEntityCreatingTable;

import javax.annotation.Nonnull;

public final class TileEntityExtremeCreatingTable extends TileEntityCreatingTable
{
	@Nonnull
	@Override
	public Reference.TableType getTableType()
	{
		return Reference.TableType.EXTREME;
	}

	@Nonnull
	@Override
	public String getDefaultName() {
		return "container.extremecreatingtable.name";
	}

	@SideOnly(Side.CLIENT)
	@Nonnull
	@Override
	public String getCTPrefix(final boolean shaped)
	{
		return "mods.avaritia.ExtremeCrafting.add" + (shaped ? "Shaped" : "Shapeless");
	}
}