package wanion.creatingtables.block.normal;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.RegistryManager;
import wanion.creatingtables.block.ContainerCreatingTable;
import wanion.creatingtables.common.control.ShapeControl;
import wanion.lib.common.Util;
import wanion.lib.common.matching.AbstractMatching;
import wanion.lib.common.matching.matcher.AnyDamageMatcher;
import wanion.lib.common.matching.matcher.ItemStackMatcher;
import wanion.lib.common.matching.matcher.OreDictMatcher;
import wanion.lib.inventory.slot.MatchingSlot;

import javax.annotation.Nonnull;
import java.util.List;

public final class ContainerNormalCreatingTable extends ContainerCreatingTable<TileEntityNormalCreatingTable>
{
	public ContainerNormalCreatingTable(@Nonnull final TileEntityNormalCreatingTable tileEntityNormalCreatingTable, final InventoryPlayer inventoryPlayer)
	{
		super(30, 17, 8, 84, 124, 35, tileEntityNormalCreatingTable, inventoryPlayer);
	}

	@Override
	public void defineShape(@Nonnull final ResourceLocation resourceLocation)
	{
		final IRecipe recipe = RegistryManager.ACTIVE.<IRecipe>getRegistry(GameData.RECIPES).getValue(resourceLocation);
		if (recipe == null)
			return;
		clearShape();
		final List<Ingredient> inputs = recipe.getIngredients();
		final int slotCount = inputs.size();
		final boolean recipeIsShapedRecipes = recipe instanceof ShapedRecipes;
		final TileEntityNormalCreatingTable normalCreatingTable = getTile();
		normalCreatingTable.shapeControl.setState(recipeIsShapedRecipes ? ShapeControl.ShapeState.SHAPED : ShapeControl.ShapeState.SHAPELESS);
		normalCreatingTable.old_recipe.setContent(resourceLocation.toString());
		final int height = recipeIsShapedRecipes ? ((ShapedRecipes) recipe).getHeight() : 3;
		final int width = recipeIsShapedRecipes ? ((ShapedRecipes) recipe).getWidth() : 3;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				final int ingredientPos = y * width + x;
				if (ingredientPos >= slotCount)
					break;
				final MatchingSlot slot = (MatchingSlot) inventorySlots.get(y * 3 + x);
				final Ingredient ingredient = inputs.get(ingredientPos);
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
		final ItemStack outputStack = recipe.getCraftingResult(new InventoryCrafting(this, 3, 3));
		inventorySlots.get(9).putStack(!outputStack.isEmpty() ? outputStack : recipe.getRecipeOutput().copy());
		detectAndSendChanges();
		getTile().markDirty();
	}
}