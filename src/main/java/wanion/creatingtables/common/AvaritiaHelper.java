package wanion.creatingtables.common;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.extreme.ExtremeShapedRecipe;
import morph.avaritia.recipe.extreme.IExtremeRecipe;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import wanion.creatingtables.block.extreme.ContainerExtremeCreatingTable;
import wanion.creatingtables.block.extreme.TileEntityExtremeCreatingTable;
import wanion.creatingtables.block.normal.TileEntityNormalCreatingTable;
import wanion.creatingtables.common.control.ShapeControl;
import wanion.lib.common.Util;
import wanion.lib.common.matching.AbstractMatching;
import wanion.lib.common.matching.matcher.AnyDamageMatcher;
import wanion.lib.common.matching.matcher.ItemStackMatcher;
import wanion.lib.common.matching.matcher.OreDictMatcher;
import wanion.lib.inventory.slot.MatchingSlot;

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
		final TileEntityExtremeCreatingTable extremeCreatingTable = containerExtremeCreatingTable.getTile();
		extremeCreatingTable.shapeControl.setState(extremeRecipe instanceof ExtremeShapedRecipe ? ShapeControl.ShapeState.SHAPED : ShapeControl.ShapeState.SHAPELESS);
		extremeCreatingTable.old_recipe.setContent(resourceLocation.toString());
		final List<Ingredient> inputs = extremeRecipe.getIngredients();
		if (extremeRecipe.isShapedRecipe()) {
			int i = 0;
			for (int y = 0; y < 9; y++) {
				for (int x = 0; x < 9; x++) {
					if (i >= inputs.size() || x >= extremeRecipe.getWidth() || y >= extremeRecipe.getHeight())
						continue;
					final MatchingSlot slot = (MatchingSlot) inventorySlots.get(x + (9 * y));
					final Ingredient ingredient = inputs.get(i++);
					final ItemStack stackInput = Util.getStackFromIngredient(ingredient);
					slot.putStack(stackInput);
					final AbstractMatching<?> matching = slot.getMatching();
					if (ingredient instanceof OreIngredient)
						matching.setMatcher(new OreDictMatcher(matching, Util.getOreNameFromOreIngredient((OreIngredient) ingredient)));
					else if (stackInput.getItemDamage() == OreDictionary.WILDCARD_VALUE)
						matching.setMatcher(new AnyDamageMatcher(matching));
					else
						matching.setMatcher(new ItemStackMatcher(matching));
					matching.validate();
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
		inventorySlots.get(81).putStack(extremeRecipe.getRecipeOutput());
		containerExtremeCreatingTable.detectAndSendChanges();
	}
}