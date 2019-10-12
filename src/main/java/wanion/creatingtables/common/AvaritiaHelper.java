package wanion.creatingtables.common;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.extreme.IExtremeRecipe;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import wanion.creatingtables.block.extreme.ContainerExtremeCreatingTable;
import wanion.lib.common.Util;

import java.util.List;

public final class AvaritiaHelper
{
	public static void defineShape(final ContainerExtremeCreatingTable containerExtremeCreatingTable, final ResourceLocation resourceLocation)
	{
		IExtremeRecipe extremeRecipe = AvaritiaRecipeManager.EXTREME_RECIPES.get(resourceLocation);
		if (extremeRecipe == null)
			return;
		final List<Slot> inventorySlots = containerExtremeCreatingTable.inventorySlots;
		containerExtremeCreatingTable.clearShape();
		final List<Ingredient> inputs = extremeRecipe.getIngredients();
		if (extremeRecipe.isShapedRecipe()) {
			int i = 0;
			for (int y = 0; y < 9; y++) {
				for (int x = 0; x < 9; x++) {
					if (i >= inputs.size() || x >= extremeRecipe.getWidth() || y >= extremeRecipe.getHeight())
						continue;
					final Slot slot = inventorySlots.get(x + (9 * y));
					final ItemStack stackInput = Util.getStackFromIngredient(inputs.get(i++));
					if (stackInput != null)
						slot.putStack(stackInput);
				}
			}
		} else {
			for (int i = 0; i < inputs.size() && i < 81; i++) {
				final Slot slot = inventorySlots.get(i);
				final ItemStack stackInput = Util.getStackFromIngredient(inputs.get(i));
				if (stackInput != null)
					slot.putStack(stackInput);
			}
		}
		containerExtremeCreatingTable.detectAndSendChanges();
	}
}