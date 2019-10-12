package wanion.creatingtables.compat.jei.normal;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import mezz.jei.gui.recipes.RecipeLayout;
import net.minecraft.entity.player.EntityPlayer;
import wanion.creatingtables.CreatingTables;
import wanion.creatingtables.block.normal.ContainerNormalCreatingTable;
import wanion.creatingtables.network.CreatingJeiTransferMessage;
import wanion.lib.common.Util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class NormalCreatingTransferHandler implements IRecipeTransferHandler<ContainerNormalCreatingTable>
{
	@Nonnull
	@Override
	public Class<ContainerNormalCreatingTable> getContainerClass()
	{
		return ContainerNormalCreatingTable.class;
	}

	@Nullable
	@Override
	public final IRecipeTransferError transferRecipe(@Nonnull final ContainerNormalCreatingTable container, @Nonnull final IRecipeLayout recipeLayout, @Nonnull final EntityPlayer player, final boolean maxTransfer, final boolean doTransfer)
	{
		if (!doTransfer)
			return null;
		final IRecipeWrapper recipeWrapper = Util.getField(RecipeLayout.class, "recipeWrapper", recipeLayout, IRecipeWrapper.class);
		if (recipeWrapper instanceof ICraftingRecipeWrapper)
			CreatingTables.networkWrapper.sendToServer(new CreatingJeiTransferMessage(container.windowId, ((ICraftingRecipeWrapper) recipeWrapper).getRegistryName()));
		return null;
	}
}