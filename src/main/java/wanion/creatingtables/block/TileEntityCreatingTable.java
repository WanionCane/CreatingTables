package wanion.creatingtables.block;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.creatingtables.Reference;
import wanion.creatingtables.common.control.ShapeControl;
import wanion.lib.common.IControlMatchingInventory;
import wanion.lib.common.control.ControlController;
import wanion.lib.common.matching.Matching;
import wanion.lib.common.matching.MatchingController;
import wanion.lib.common.matching.matcher.ItemStackMatcher;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class TileEntityCreatingTable extends TileEntity implements ISidedInventory, IControlMatchingInventory
{
	private final List<ItemStack> itemStacks = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
	private final ControlController controlController = new ControlController(this);
	private final MatchingController matchingController = new MatchingController(this);

	public TileEntityCreatingTable()
	{
		final int max = getSizeInventory() - 1;
		for (int i = 0; i < max; i++)
			matchingController.add(new Matching(itemStacks, i));
		controlController.add(new ShapeControl());
	}

	public final int getRoot()
	{
		return getTableType().getRoot();
	}

	@Nonnull
	public final ShapeControl getShapeControl()
	{
		return controlController.get(ShapeControl.class);
	}

	@Nonnull
	public abstract Reference.TableType getTableType();

	@Override
	public final int getSizeInventory()
	{
		final int root = getRoot();
		return root * root + 1;
	}

	@Override
	public boolean isEmpty()
	{
		for (final ItemStack itemStack : itemStacks)
			if (!itemStack.isEmpty())
				return false;
		return true;
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(final int slot)
	{
		return itemStacks.get(slot);
	}

	@Nonnull
	@Override
	public ItemStack decrStackSize(final int slot, final int howMuch)
	{
		final ItemStack slotStack = itemStacks.get(slot);
		if (slotStack.isEmpty())
			return ItemStack.EMPTY;
		final ItemStack newStack = slotStack.copy();
		newStack.setCount(howMuch);
		slotStack.setCount(slotStack.getCount() - howMuch);
		if ((slotStack.getCount()) == 0)
			itemStacks.set(slot, ItemStack.EMPTY);
		markDirty();
		return newStack;
	}

	@Nonnull
	@Override
	public ItemStack removeStackFromSlot(final int index)
	{
		final ItemStack itemStack = itemStacks.get(index);
		itemStacks.set(index, ItemStack.EMPTY);
		return itemStack;
	}

	@Override
	public void setInventorySlotContents(final int slot, @Nonnull final ItemStack itemStack)
	{
		itemStacks.set(slot, itemStack);
		markDirty();
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(@Nonnull final EntityPlayer entityPlayer)
	{
		return world.getTileEntity(pos) == this && entityPlayer.getDistanceSq((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) getPos().getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(@Nonnull final EntityPlayer player) {}

	@Override
	public void closeInventory(@Nonnull final EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(final int slot, @Nonnull final ItemStack itemStack)
	{
		return true;
	}

	@Override
	public int getField(final int id)
	{
		return 0;
	}

	@Override
	public void setField(final int id, final int value) {}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear() {}

	@Override
	public final void readFromNBT(final NBTTagCompound nbtTagCompound)
	{
		super.readFromNBT(nbtTagCompound);
		readCustomNBT(nbtTagCompound);
	}

	@Nonnull
	@Override
	public NBTTagCompound writeToNBT(@Nonnull final NBTTagCompound nbtTagCompound)
	{
		super.writeToNBT(nbtTagCompound);
		writeCustomNBT(nbtTagCompound);
		return nbtTagCompound;
	}

	@Nonnull
	public ITextComponent getDisplayName()
	{
		return new TextComponentTranslation(getName());
	}

	void readCustomNBT(final NBTTagCompound nbtTagCompound)
	{
		final NBTTagList nbtTagList = nbtTagCompound.getTagList("Contents", 10);
		for (int i = 0; i < nbtTagList.tagCount(); i++) {
			final NBTTagCompound slotCompound = nbtTagList.getCompoundTagAt(i);
			final int slot = slotCompound.getShort("Slot");
			if (slot >= 0 && slot < getSizeInventory())
				setInventorySlotContents(slot, new ItemStack(slotCompound));
		}
		controlController.getInstances().forEach(control -> control.readFromNBT(nbtTagCompound));
		matchingController.getInstances().forEach(matching -> matching.readFromNBT(nbtTagCompound));
	}

	NBTTagCompound writeCustomNBT(final NBTTagCompound nbtTagCompound)
	{
		final NBTTagList nbtTagList = new NBTTagList();
		final int max = getSizeInventory();
		for (int i = 0; i < max; i++) {
			final ItemStack itemStack = getStackInSlot(i);
			if (itemStack.isEmpty())
				continue;
			final NBTTagCompound slotCompound = new NBTTagCompound();
			slotCompound.setShort("Slot", (short) i);
			nbtTagList.appendTag(itemStack.writeToNBT(slotCompound));
		}
		nbtTagCompound.setTag("Contents", nbtTagList);
		controlController.getInstances().forEach(control -> control.writeToNBT(nbtTagCompound));
		matchingController.getInstances().forEach(matching -> {
			if (!(matching.getMatcher() instanceof ItemStackMatcher))
				matching.writeToNBT(nbtTagCompound);
		});
		return nbtTagCompound;
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Nonnull
	@Override
	public int[] getSlotsForFace(@Nonnull final EnumFacing side)
	{
		return new int[0];
	}

	@Override
	public boolean canInsertItem(int index, @Nonnull final ItemStack itemStackIn, @Nonnull final EnumFacing direction)
	{
		return false;
	}

	@Override
	public boolean canExtractItem(final int index, @Nonnull final ItemStack stack, @Nonnull final EnumFacing direction)
	{
		return false;
	}

	@Nonnull
	@Override
	public ControlController getControlController()
	{
		return controlController;
	}

	@Nonnull
	@Override
	public MatchingController getMatchingController()
	{
		return matchingController;
	}

	@SideOnly(Side.CLIENT)
	@Nonnull
	public abstract String getCTPrefix(final boolean shaped);
}