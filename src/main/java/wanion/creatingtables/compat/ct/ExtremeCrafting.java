package wanion.creatingtables.compat.ct;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import morph.avaritia.compat.crafttweaker.RemoveRecipeAction;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.stream.Collectors;

@ZenRegister
@ZenClass("mods.creatingtables.ExtremeCrafting")
public class ExtremeCrafting
{
	@ZenMethod
	public static void removeByRecipeName(String name) {
		final ResourceLocation resourceToRemove = new ResourceLocation(name);
		CraftTweakerAPI.apply(new RemoveRecipeAction<>("Extreme", AvaritiaRecipeManager.EXTREME_RECIPES,
				recipes -> recipes.stream().map(IForgeRegistryEntry::getRegistryName).filter(resourceToRemove::equals).collect(Collectors.toList()), name
		));
	}
}