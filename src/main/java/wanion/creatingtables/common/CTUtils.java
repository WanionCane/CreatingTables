package wanion.creatingtables.common;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import net.minecraft.item.ItemStack;
import wanion.creatingtables.block.TileEntityCreatingTable;
import wanion.creatingtables.common.control.ShapeControl;
import wanion.lib.common.control.ControlController;
import wanion.lib.common.matching.Matching;
import wanion.lib.common.matching.MatchingController;
import wanion.lib.common.matching.matcher.NbtMatcher;

import javax.annotation.Nonnull;
import java.util.Collections;

public final class CTUtils
{
	private static final String NEW_LINE = System.lineSeparator();
	private static final String TAB = "\t";

	private CTUtils() {}

	public static String toCTScript(@Nonnull final TileEntityCreatingTable tileEntityCreatingTable)
	{
		final int outputSlot = tileEntityCreatingTable.getSizeInventory() - 1;
		final ItemStack outputStack = tileEntityCreatingTable.getStackInSlot(outputSlot);
		if (outputStack.isEmpty())
			return null;
		final StringBuilder scriptBuilder = new StringBuilder();
		final boolean shaped = tileEntityCreatingTable.getController(ControlController.class).get(ShapeControl.class).getState() == ShapeControl.ShapeState.SHAPED;
		scriptBuilder.append(tileEntityCreatingTable.getCTPrefix(shaped));
		scriptBuilder.append('(');
		final Matching outputMatching = new Matching(Collections.singletonList(outputStack), 0);
		scriptBuilder.append('"').append(outputStack.getCount()).append("x_").append(outputStack.getUnlocalizedName().replace('.', '_')).append(shaped ? "_shaped" : "_shapeless").append("\", ");
		if (outputStack.hasTagCompound())
			outputMatching.setMatcher(new NbtMatcher(outputMatching));
		scriptBuilder.append(outputMatching.getMatcher().ctFormat());
		scriptBuilder.append(", [");
		final MatchingController matchingController = tileEntityCreatingTable.getMatchingController();
		if (shaped) {
			final int root = tileEntityCreatingTable.getRoot();
			final int last = root - 1;
			for (int y = 0; y < root; y++) {
				boolean hasNextY = y < last;
				scriptBuilder.append(NEW_LINE);
				scriptBuilder.append(TAB).append('[');
				for (int x = 0; x < root; x++) {
					boolean hasNextX = x < last;
					final int actualSlot = y * root + x;
					if (!tileEntityCreatingTable.getStackInSlot(actualSlot).isEmpty()) {
						final Matching matchingControl = matchingController.getMatching(actualSlot);
						scriptBuilder.append(matchingControl.getMatcher().ctFormat());
						if (hasNextX)
							scriptBuilder.append(", ");
					} else scriptBuilder.append(hasNextX ? "null, " : "null");
				}
				scriptBuilder.append(hasNextY ? "]," : ']');
			}
			scriptBuilder.append(NEW_LINE);
			scriptBuilder.append(']');
		} else {
			int charPerLineCount = 0;
			final TIntList validList = new TIntArrayList();
			for (int i = 0; i < outputSlot; i++)
				if (!tileEntityCreatingTable.getStackInSlot(i).isEmpty())
					validList.add(i);
			for (final TIntIterator validIterator = validList.iterator(); validIterator.hasNext(); ) {
				final String format = matchingController.getMatching((validIterator.next())).getMatcher().ctFormat();
				charPerLineCount += format.length();
				if (charPerLineCount >= 320 && validIterator.hasNext()) {
					scriptBuilder.deleteCharAt(scriptBuilder.length() - 1);
					scriptBuilder.append(NEW_LINE).append(TAB);
					charPerLineCount = 0;
				}
				scriptBuilder.append(format);
				if (validIterator.hasNext())
					scriptBuilder.append(", ");
			}
			scriptBuilder.append(']');
		}
		scriptBuilder.append(");");
		scriptBuilder.append(NEW_LINE).append(NEW_LINE);
		return scriptBuilder.toString();
	}
}