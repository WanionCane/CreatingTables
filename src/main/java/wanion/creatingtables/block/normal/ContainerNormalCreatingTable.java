package wanion.creatingtables.block.normal;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.RegistryManager;
import wanion.creatingtables.block.ContainerCreatingTable;
import wanion.creatingtables.block.TileEntityCreatingTable;
import wanion.lib.common.Util;

import javax.annotation.Nonnull;
import java.util.List;

public final class ContainerNormalCreatingTable extends ContainerCreatingTable
{
	public ContainerNormalCreatingTable(@Nonnull final TileEntityCreatingTable tileEntityCreatingTable, final InventoryPlayer inventoryPlayer)
	{
		super(30, 17, 8, 84, 124, 35, tileEntityCreatingTable, inventoryPlayer);
	}

	@Override
	public void defineShape(@Nonnull final ResourceLocation resourceLocation)
	{
		final IRecipe recipe = RegistryManager.ACTIVE.<IRecipe>getRegistry(GameData.RECIPES).getValue(resourceLocation);
		if (recipe == null)
			return;
		final int slotCount = inventorySlots.size();
		clearShape();
		final List<Ingredient> inputs = recipe.getIngredients();
		for (int i = 0; i < slotCount && i < 9; i++) {
			final Slot slot = inventorySlots.get(i);
			final ItemStack stackInput = Util.getStackFromIngredient(inputs.get(i));
			if (stackInput != null)
				slot.putStack(stackInput);
		}
		detectAndSendChanges();
	}
}