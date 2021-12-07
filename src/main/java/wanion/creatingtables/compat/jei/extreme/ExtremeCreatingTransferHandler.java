package wanion.creatingtables.compat.jei.extreme;

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
import mezz.jei.gui.recipes.RecipeLayout;
import mezz.jei.transfer.RecipeTransferErrorTooltip;
import morph.avaritia.compat.jei.extreme.ExtremeRecipeWrapper;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.extreme.IExtremeRecipe;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import wanion.creatingtables.CreatingTables;
import wanion.creatingtables.block.extreme.ContainerExtremeCreatingTable;
import wanion.lib.common.Util;
import wanion.lib.network.DefineShapeMessage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public final class ExtremeCreatingTransferHandler implements IRecipeTransferHandler<ContainerExtremeCreatingTable>
{
	@Nonnull
	@Override
	public Class<ContainerExtremeCreatingTable> getContainerClass()
	{
		return ContainerExtremeCreatingTable.class;
	}

	@Nullable
	@Override
	public IRecipeTransferError transferRecipe(@Nonnull final ContainerExtremeCreatingTable container, @Nonnull final IRecipeLayout recipeLayout, @Nonnull final EntityPlayer player, final boolean maxTransfer, final boolean doTransfer)
	{
		final IRecipeWrapper recipeWrapper = Util.getField(RecipeLayout.class, "recipeWrapper", recipeLayout, IRecipeWrapper.class);
		if (!(recipeWrapper instanceof ExtremeRecipeWrapper))
			return null;
		final IExtremeRecipe extremeRecipe = Util.getField(ExtremeRecipeWrapper.class, "recipe", recipeWrapper, IExtremeRecipe.class);
		for (Map.Entry<ResourceLocation, IExtremeRecipe> extremeEntry : AvaritiaRecipeManager.EXTREME_RECIPES.entrySet()) {
			if (extremeEntry.getValue() == extremeRecipe) {
				final ResourceLocation recipeRegistryName = extremeEntry.getKey();
				if (recipeRegistryName == null)
					return new RecipeTransferErrorTooltip(I18n.format("creating.transfer.error"));
				if (!doTransfer)
					return null;
				DefineShapeMessage.sendToServer(container, recipeRegistryName);
				break;
			}
		}
		return null;
	}
}