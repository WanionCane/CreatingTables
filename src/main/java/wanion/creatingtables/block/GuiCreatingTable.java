package wanion.creatingtables.block;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import joptsimple.internal.Strings;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import wanion.creatingtables.client.button.ClearShapeButton;
import wanion.creatingtables.client.button.CopyToClipBoardButton;
import wanion.creatingtables.client.button.ExportToFileButton;
import wanion.creatingtables.client.button.ShapeControlButton;
import wanion.creatingtables.common.control.ShapeControl;
import wanion.lib.client.ClientHelper;
import wanion.lib.common.IClickAction;
import wanion.lib.common.matching.Matching;
import wanion.lib.inventory.slot.MatchingSlot;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class GuiCreatingTable extends GuiContainer
{
	private final TileEntityCreatingTable tileEntityCreatingTable;
	private final ResourceLocation guiTexture;
	private final Slot firstPlayerSlot = inventorySlots.getSlot(inventorySlots.inventorySlots.size() - 36);
	private final Slot outputSlot = inventorySlots.getSlot(firstPlayerSlot.slotNumber - 1);
	private final List<String> matchingDescription;
	private final List<String> outputDescription;

	public GuiCreatingTable(@Nonnull final TileEntityCreatingTable tileEntityCreatingTable, @Nonnull final ResourceLocation guiTexture, @Nonnull final Container inventorySlotsIn)
	{
		super(inventorySlotsIn);
		this.tileEntityCreatingTable = tileEntityCreatingTable;
		this.guiTexture = guiTexture;
		matchingDescription = Arrays.asList(
				ClientHelper.getHowToUse(),
				TextFormatting.GRAY + I18n.format("wanionlib.matching.desc"));
		outputDescription = Arrays.asList("",
				TextFormatting.GOLD + I18n.format("creating.usage"),
				TextFormatting.GRAY + I18n.format("creating.usage.desc.line1"),
				TextFormatting.GRAY + I18n.format("creating.usage.desc.line2"),
				TextFormatting.GRAY + I18n.format("creating.usage.desc.line3"),
				TextFormatting.GRAY + I18n.format("creating.usage.desc.line4"));
	}

	@Override
	public void initGui()
	{
		super.initGui();
		final ShapeControlButton shapeControlButton = addButton(new ShapeControlButton(this, tileEntityCreatingTable.getControlController().get(ShapeControl.class), guiLeft + outputSlot.xPos - 10, guiTop + outputSlot.yPos - 28, 0));
		addButton(new ClearShapeButton(1, this, shapeControlButton.x + 22, shapeControlButton.y + 5));
		addButton(new ExportToFileButton(2, this, guiLeft + outputSlot.xPos - 1, guiTop + outputSlot.yPos + 23));
		addButton(new CopyToClipBoardButton(3, this, guiLeft + outputSlot.xPos + 9, guiTop + outputSlot.yPos + 23));
	}

	public TileEntityCreatingTable getTileEntityCreatingTable()
	{
		return tileEntityCreatingTable;
	}

	@Override
	public final void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected final void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY)
	{
		fontRenderer.drawString(I18n.format(tileEntityCreatingTable.getName()), 7, 7, 0x404040);
		fontRenderer.drawString(I18n.format("container.inventory"), firstPlayerSlot.xPos - 1, firstPlayerSlot.yPos - 11, 0x404040);
		for (final GuiButton guibutton : this.buttonList)
			if (guibutton.isMouseOver())
				guibutton.drawButtonForegroundLayer(mouseX, mouseY);
	}

	@Override
	protected final void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(guiTexture);
		final boolean smallGui = xSize < 256 && ySize < 256;
		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, xSize, ySize, smallGui ? 256 : xSize > ySize ? xSize : ySize, smallGui ? 256 : xSize > ySize ? xSize : ySize);
	}

	@Override
	protected final void renderToolTip(@Nonnull final ItemStack stack, final int x, final int y)
	{
		final MatchingSlot matchingSlot = findSlot(x, y);
		if (matchingSlot != null)
			drawMatchingTooltip(matchingSlot, stack, x, y);
		else if (super.isPointInRegion(outputSlot.xPos, outputSlot.yPos, 16, 16, x, y))
			drawOutputSlotToolTip(stack, x, y);
		else super.renderToolTip(stack, x, y);
	}

	private MatchingSlot findSlot(final int x, final int y)
	{
		final int max = inventorySlots.inventorySlots.size() - 37;
		for (int i = 0; i < max; i++) {
			final Slot slot = inventorySlots.getSlot(i);
			if (slot instanceof MatchingSlot && super.isPointInRegion(slot.xPos, slot.yPos, 16, 16, x, y))
				return (MatchingSlot) slot;
		}
		return null;
	}

	private void drawMatchingTooltip(@Nonnull final MatchingSlot matchingSlot, @Nonnull final ItemStack stack, final int x, final int y)
	{
		final FontRenderer font = stack.getItem().getFontRenderer(stack);
		GuiUtils.preItemToolTip(stack);
		final List<String> toolTip = this.getItemToolTip(stack);
		final Matching matching = matchingSlot.getMatching();
		toolTip.add(Strings.EMPTY);
		toolTip.add(TextFormatting.RED + I18n.format(matching.getControlName()) + ": " + TextFormatting.WHITE + matching.getMatcher().getDescription());
		toolTip.addAll(matchingDescription);
		drawHoveringText(toolTip, x, y, (font == null ? fontRenderer : font));
		GuiUtils.postItemToolTip();
	}

	private void drawOutputSlotToolTip(@Nonnull final ItemStack stack, final int x, final int y)
	{
		final FontRenderer font = stack.getItem().getFontRenderer(stack);
		GuiUtils.preItemToolTip(stack);
		final List<String> toolTip = this.getItemToolTip(stack);
		toolTip.addAll(outputDescription);
		drawHoveringText(toolTip, x, y, (font == null ? fontRenderer : font));
		GuiUtils.postItemToolTip();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		if (mouseButton == 0 || mouseButton == 1) {
			for (final GuiButton button : buttonList) {
				if (button instanceof IClickAction && button.mousePressed(this.mc, mouseX, mouseY)) {
					((IClickAction) button).action(mouseButton == 0);
					return;
				}
			}
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}