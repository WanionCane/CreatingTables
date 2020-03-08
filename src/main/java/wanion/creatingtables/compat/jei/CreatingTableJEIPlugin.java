package wanion.creatingtables.compat.jei;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraftforge.fml.common.Loader;
import wanion.creatingtables.block.normal.GuiNormalCreatingTable;
import wanion.creatingtables.block.extreme.GuiExtremeCreatingTable;
import wanion.creatingtables.compat.jei.normal.NormalCreatingTransferHandler;
import wanion.creatingtables.compat.jei.extreme.ExtremeCreatingTransferHandler;

@JEIPlugin
public final class CreatingTableJEIPlugin implements IModPlugin
{
	public static final String NORMAL = "minecraft.crafting";
	public static final String EXTREME = "Avatitia.Extreme";

	@Override
	public void register(final IModRegistry modRegistry)
	{
		// Normal
		modRegistry.addGhostIngredientHandler(GuiNormalCreatingTable.class, new CreatingGhostHandler<>());
		modRegistry.getRecipeTransferRegistry().addRecipeTransferHandler(new NormalCreatingTransferHandler(), NORMAL);
		modRegistry.addRecipeClickArea(GuiNormalCreatingTable.class, 90, 35, 21, 13, NORMAL);
		// Extreme
		modRegistry.addGhostIngredientHandler(GuiExtremeCreatingTable.class, new CreatingGhostHandler<>());
		if (!Loader.isModLoaded("avaritia"))
			return;
		modRegistry.getRecipeTransferRegistry().addRecipeTransferHandler(new ExtremeCreatingTransferHandler(), EXTREME);
		modRegistry.addRecipeClickArea(GuiExtremeCreatingTable.class, 172, 95, 3, 6, EXTREME);
	}
}