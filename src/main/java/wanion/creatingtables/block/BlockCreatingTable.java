package wanion.creatingtables.block;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import wanion.creatingtables.CreatingTables;
import wanion.creatingtables.Reference;
import wanion.creatingtables.block.normal.TileEntityNormalCreatingTable;
import wanion.creatingtables.block.extreme.TileEntityExtremeCreatingTable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public final class BlockCreatingTable extends BlockContainer
{
	public static final BlockCreatingTable INSTANCE = new BlockCreatingTable();

	private BlockCreatingTable()
	{
		super(Material.WOOD);
		blockParticleGravity = 0.0F;
		setHardness(2.5F).setCreativeTab(CreatingTables.creativeTabs);
		setRegistryName(new ResourceLocation(Reference.MOD_ID, "creatingtables"));
		setDefaultState(blockState.getBaseState().withProperty(Reference.TABLE_TYPES, Reference.TableType.NORMAL));
	}

	@Override
	public int getLightValue(@Nonnull final IBlockState state, @Nonnull final IBlockAccess world, @Nonnull final BlockPos pos)
	{
		return getMetaFromState(state)  == 1 ? 15 : 0;
	}

	@Override
	public TileEntity createNewTileEntity(@Nonnull final World world, final int metadata)
	{
		switch (metadata) {
			case 0:
				return new TileEntityNormalCreatingTable();
			case 1:
				return new TileEntityExtremeCreatingTable();
			default:
				return null;
		}
	}

	@Override
	public IBlockState getStateFromMeta(final int metadata)
	{
		return getDefaultState().withProperty(Reference.TABLE_TYPES, Reference.TableType.getByValue(metadata));
	}

	@Override
	public int getMetaFromState(final IBlockState blockState)
	{
		return blockState.getValue(Reference.TABLE_TYPES).getMetadata();
	}

	@Nonnull
	@Override
	public Item getItemDropped(final IBlockState blockState, final Random random, final int fortune)
	{
		return Items.AIR;
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return getMetaFromState(state);
	}

	@Override
	public boolean onBlockActivated(final World world, final BlockPos blockPos, final IBlockState state, final EntityPlayer entityPlayer, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ)
	{
		if (world != null) {
			final TileEntity tileEntity = world.getTileEntity(blockPos);
			if (tileEntity instanceof TileEntityCreatingTable)
				FMLNetworkHandler.openGui(entityPlayer, CreatingTables.instance, ((TileEntityCreatingTable) tileEntity).getTableType().ordinal(), world, blockPos.getX(), blockPos.getY(), blockPos.getZ());
			else
				return false;
		}
		return true;
	}

	@Override
	public void onBlockPlacedBy(final World world, final BlockPos blockPos, final IBlockState blockState, final EntityLivingBase entityLivingBase, final ItemStack itemStack)
	{
		if (world == null)
			return;
		final TileEntity tileEntity = world.getTileEntity(blockPos);
		if (tileEntity instanceof TileEntityCreatingTable && itemStack.hasTagCompound())
			((TileEntityCreatingTable) tileEntity).readCustomNBT(itemStack.getTagCompound());
	}

	@Override
	public void getSubBlocks(final CreativeTabs creativeTabs, final NonNullList<ItemStack> items)
	{
		if (creativeTabs == this.getCreativeTabToDisplayOn())
			for (int i = 0; i < Reference.TableType.values().length; i++)
				items.add(new ItemStack(this, 1, i));
	}

	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, Reference.TABLE_TYPES);
	}

	@Nonnull
	@Override
	public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity)
	{
		return SoundType.WOOD;
	}

	@Nonnull
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public void breakBlock(final World world, @Nonnull final BlockPos blockPos, @Nonnull final IBlockState blockState)
	{
		if (world == null)
			return;
		final TileEntityCreatingTable tileEntityCreatingTable = (TileEntityCreatingTable) world.getTileEntity(blockPos);
		if (tileEntityCreatingTable != null) {
			final ItemStack droppedStack = new ItemStack(this, 1, getMetaFromState(blockState));
			final NBTTagCompound nbtTagCompound = tileEntityCreatingTable.writeCustomNBT(new NBTTagCompound());
			if (nbtTagCompound.getTagList("Contents", 10).tagCount() > 0)
				droppedStack.setTagCompound(nbtTagCompound);
			world.spawnEntity(new EntityItem(world, blockPos.getX() + Reference.RANDOM.nextFloat() * 0.8F + 0.1F, blockPos.getY() + Reference.RANDOM.nextFloat() * 0.8F + 0.1F, blockPos.getZ() + Reference.RANDOM.nextFloat() * 0.8F + 0.1F, droppedStack));
		}
		super.breakBlock(world, blockPos, blockState);
	}
}