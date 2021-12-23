package wanion.creatingtables.block;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.creatingtables.Reference;
import wanion.creatingtables.common.control.ShapeControl;
import wanion.lib.common.WTileEntity;
import wanion.lib.common.control.ControlController;
import wanion.lib.common.field.CheckBox;
import wanion.lib.common.field.FieldController;
import wanion.lib.common.field.text.TextField;
import wanion.lib.common.matching.IMatchingInventory;
import wanion.lib.common.matching.Matching;
import wanion.lib.common.matching.MatchingController;

import javax.annotation.Nonnull;

public abstract class TileEntityCreatingTable extends WTileEntity implements IMatchingInventory
{
	public final ShapeControl shapeControl = new ShapeControl();
	public final CheckBox removeOldRecipeCheckBox = new CheckBox("creating.remove_old_recipe");
	public final TextField old_recipe = new TextField("old.recipe.name");

	public TileEntityCreatingTable()
	{
		final int max = getSizeInventory() - 1;
		final MatchingController matchingController = getController(MatchingController.class);
		for (int i = 0; i < max; i++)
			matchingController.add(new Matching(itemStacks, i, true));
		getController(ControlController.class).add(shapeControl);
		final FieldController fieldController = getController(FieldController.class);
		fieldController.add(removeOldRecipeCheckBox);
		fieldController.add(old_recipe);
	}

	public final int getRoot()
	{
		return getTableType().getRoot();
	}

	@Nonnull
	public abstract Reference.TableType getTableType();

	@Override
	public final int getSizeInventory()
	{
		final int root = getRoot();
		return root * root + 1;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean canInsertItem(int index, @Nonnull final ItemStack itemStackIn, @Nonnull final EnumFacing direction)
	{
		return false;
	}

	@Override
	public boolean canExtractItem(final int index, @Nonnull final ItemStack stack, @Nonnull final EnumFacing direction)
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Nonnull
	public abstract String getCTRemovalPrefix();

	@SideOnly(Side.CLIENT)
	@Nonnull
	public abstract String getCTPrefix(final boolean shaped);

	@Nonnull
	@Override
	public MatchingController getMatchingController()
	{
		return getController(MatchingController.class);
	}
}