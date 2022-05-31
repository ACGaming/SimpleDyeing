package mod.acgaming.simpledyeing.events;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;

import mod.acgaming.simpledyeing.SimpleDyeing;
import mod.acgaming.simpledyeing.config.SDConfig;
import mod.acgaming.simpledyeing.util.SDArrays;

@Mod.EventBusSubscriber(modid = SimpleDyeing.MOD_ID)
public class SDEventHandler
{
    @SubscribeEvent
    public static void dyeBlock(PlayerInteractEvent.RightClickBlock event)
    {
        World world = event.getWorld();
        EntityPlayer player = event.getEntityPlayer();
        EnumHand hand = event.getHand();
        ItemStack itemStack = event.getItemStack();
        Item item = itemStack.getItem();
        BlockPos blockPos = event.getPos();
        IBlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();

        if (SDConfig.sdSneaking && !player.isSneaking()) return;

        if (!(block instanceof BlockColored
            || block instanceof BlockCarpet
            || block instanceof BlockStainedGlass
            || block instanceof BlockStainedGlassPane
            || block instanceof BlockGlass
            || block instanceof BlockPane && blockState.getMaterial() == Material.GLASS
            || block instanceof BlockHardenedClay
            || block instanceof BlockGlazedTerracotta
            || block instanceof BlockConcretePowder
            || block instanceof BlockBed
            //|| block instanceof BlockShulkerBox
        )) return;

        // DYEING
        if (item == Items.DYE)
        {
            int metadataDye = itemStack.getMetadata();

            // HANDLE COLORLESS BLOCKS
            if (block instanceof BlockGlass) blockState = Blocks.STAINED_GLASS.getDefaultState().withProperty(BlockStainedGlass.COLOR, EnumDyeColor.byMetadata(15 - metadataDye));
            else if (block instanceof BlockPane && blockState.getMaterial() == Material.GLASS) blockState = Blocks.STAINED_GLASS_PANE.getDefaultState().withProperty(BlockStainedGlassPane.COLOR, EnumDyeColor.byMetadata(15 - metadataDye));
            else if (block instanceof BlockHardenedClay) blockState = Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockStainedHardenedClay.COLOR, EnumDyeColor.byMetadata(15 - metadataDye));
            else if (block instanceof BlockGlazedTerracotta)
            {
                // ALREADY SAME COLOR?
                if (block == SDArrays.arrayGlazedTerracotta[15 - metadataDye]) return;
                blockState = SDArrays.arrayGlazedTerracotta[15 - metadataDye].getDefaultState();
            }
            else if (block instanceof BlockBed)
            {
                EnumFacing enumFacing;
                if (blockState.getValue(BlockBed.PART) == BlockBed.EnumPartType.FOOT)
                {
                    enumFacing = blockState.getValue(BlockBed.FACING);
                }
                else
                {
                    enumFacing = blockState.getValue(BlockBed.FACING).getOpposite();
                }

                BlockPos blockPosOffset = blockPos.offset(enumFacing);
                IBlockState blockStateOffset = world.getBlockState(blockPosOffset);

                world.setBlockState(blockPos, blockState, 10);
                world.setBlockState(blockPosOffset, blockStateOffset, 10);

                TileEntity tileentity = world.getTileEntity(blockPosOffset);

                if (tileentity instanceof TileEntityBed)
                {
                    if (((TileEntityBed) tileentity).getColor().getMetadata() == 15 - metadataDye) return;
                    ((TileEntityBed) tileentity).setColor(EnumDyeColor.byMetadata(15 - metadataDye));
                }

                TileEntity tileentity1 = world.getTileEntity(blockPos);

                if (tileentity1 instanceof TileEntityBed)
                {
                    if (((TileEntityBed) tileentity1).getColor().getMetadata() == 15 - metadataDye) return;
                    ((TileEntityBed) tileentity1).setColor(EnumDyeColor.byMetadata(15 - metadataDye));
                }

                world.notifyNeighborsRespectDebug(blockPos, block, false);
                world.notifyNeighborsRespectDebug(blockPosOffset, blockStateOffset.getBlock(), false);
            }
//            else if (block instanceof BlockShulkerBox)
//            {
//                // ALREADY SAME COLOR?
//                if (block == SDArrays.arrayShulkerBoxes[15 - metadataDye]) return;
//                blockState = SDArrays.arrayShulkerBoxes[15 - metadataDye].getDefaultState();
//            }
            // HANDLE REGULAR COLORED BLOCKS
            else
            {
                // ALREADY SAME COLOR?
                if (blockState.getValue(BlockColored.COLOR).getMetadata() == 15 - metadataDye) return;
                blockState = blockState.withProperty(BlockColored.COLOR, EnumDyeColor.byMetadata(15 - metadataDye));
            }

            world.setBlockState(blockPos, blockState);

            // SOUNDS
            if (SDConfig.sdSounds)
            {
                world.playSound(player, blockPos, SoundEvents.ENTITY_IRONGOLEM_ATTACK, SoundCategory.BLOCKS, 0.4F, 1.0F);
                world.playSound(player, blockPos, block.getSoundType().getHitSound(), SoundCategory.BLOCKS, 0.4F, 1.5F);
            }

            // PARTICLES
            if (SDConfig.sdParticles)
            {
                for (int i = 0; i < 8; ++i)
                {
                    double d0 = blockPos.getX() + world.rand.nextDouble();
                    double d1 = blockPos.getY() + world.rand.nextDouble();
                    double d2 = blockPos.getZ() + world.rand.nextDouble();
                    double d3 = (world.rand.nextDouble() * 2.0D - 1.0D) * 0.3D;
                    double d4 = 0.3D + world.rand.nextDouble() * 0.3D;
                    double d5 = (world.rand.nextDouble() * 2.0D - 1.0D) * 0.3D;
                    world.spawnParticle(EnumParticleTypes.CRIT, d0, d1, d2, d3, d4, d5);
                }
            }

            // HANDLE USED ITEMS IN SURVIVAL
            if (!player.isCreative())
            {
                player.getHeldItem(hand).shrink(1);
            }
        }
        // CLEANING
        else if (item == Items.POTIONITEM && PotionUtils.getPotionFromItem(itemStack) == PotionTypes.WATER)
        {
            if (block instanceof BlockGlazedTerracotta)
            {
                // ALREADY CLEANED?
                if (block == SDArrays.arrayGlazedTerracotta[0]) return;
                blockState = SDArrays.arrayGlazedTerracotta[0].getDefaultState();
                world.setBlockState(blockPos, blockState);
            }
            // ALREADY CLEANED?
            else if (blockState == block.getDefaultState()) return;
            else if (block == Blocks.WOOL) world.setBlockState(blockPos, Blocks.WOOL.getDefaultState());
            else if (block == Blocks.CARPET) world.setBlockState(blockPos, Blocks.CARPET.getDefaultState());
            else if (block == Blocks.STAINED_GLASS) world.setBlockState(blockPos, Blocks.GLASS.getDefaultState());
            else if (block == Blocks.STAINED_GLASS_PANE) world.setBlockState(blockPos, Blocks.GLASS_PANE.getDefaultState());
            else if (block == Blocks.STAINED_HARDENED_CLAY) world.setBlockState(blockPos, Blocks.HARDENED_CLAY.getDefaultState());
            else if (block == Blocks.CONCRETE) world.setBlockState(blockPos, Blocks.CONCRETE.getDefaultState());
            else if (block == Blocks.CONCRETE_POWDER) world.setBlockState(blockPos, Blocks.CONCRETE_POWDER.getDefaultState());
            else if (block == Blocks.BED)
            {
                EnumFacing enumFacing;
                if (blockState.getValue(BlockBed.PART) == BlockBed.EnumPartType.FOOT)
                {
                    enumFacing = blockState.getValue(BlockBed.FACING);
                }
                else
                {
                    enumFacing = blockState.getValue(BlockBed.FACING).getOpposite();
                }

                BlockPos blockPosOffset = blockPos.offset(enumFacing);
                IBlockState blockStateOffset = world.getBlockState(blockPosOffset);

                world.setBlockState(blockPos, blockState, 10);
                world.setBlockState(blockPosOffset, blockStateOffset, 10);

                TileEntity tileentity = world.getTileEntity(blockPosOffset);

                if (tileentity instanceof TileEntityBed)
                {
                    if (((TileEntityBed) tileentity).getColor() == EnumDyeColor.WHITE) return;
                    ((TileEntityBed) tileentity).setColor(EnumDyeColor.WHITE);
                }

                TileEntity tileentity1 = world.getTileEntity(blockPos);

                if (tileentity1 instanceof TileEntityBed)
                {
                    if (((TileEntityBed) tileentity1).getColor() == EnumDyeColor.WHITE) return;
                    ((TileEntityBed) tileentity1).setColor(EnumDyeColor.WHITE);
                }

                world.notifyNeighborsRespectDebug(blockPos, block, false);
                world.notifyNeighborsRespectDebug(blockPosOffset, blockStateOffset.getBlock(), false);
            }
//            else if (block instanceof BlockShulkerBox)
//            {
//                blockState = SDArrays.arrayShulkerBoxes[0].getDefaultState();
//                world.setBlockState(blockPos, blockState);
//            }

            // SOUNDS
            if (SDConfig.sdSounds)
            {
                world.playSound(player, blockPos, SoundEvents.ENTITY_GENERIC_SWIM, SoundCategory.BLOCKS, 0.2F, 1.0F);
                world.playSound(player, blockPos, block.getSoundType().getHitSound(), SoundCategory.BLOCKS, 0.4F, 1.5F);
            }

            // PARTICLES
            if (SDConfig.sdParticles)
            {
                for (int i = 0; i < 24; ++i)
                {
                    double d0 = blockPos.getX() + world.rand.nextDouble();
                    double d1 = blockPos.getY() + world.rand.nextDouble();
                    double d2 = blockPos.getZ() + world.rand.nextDouble();
                    double d3 = (world.rand.nextDouble() * 2.0D - 1.0D) * 0.3D;
                    double d4 = 0.3D + world.rand.nextDouble() * 0.3D;
                    double d5 = (world.rand.nextDouble() * 2.0D - 1.0D) * 0.3D;
                    world.spawnParticle(EnumParticleTypes.WATER_SPLASH, d0, d1, d2, d3, d4, d5);
                }
            }

            // HANDLE USED ITEMS IN SURVIVAL
            if (!player.isCreative())
            {
                player.getHeldItem(hand).shrink(1);
                ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.GLASS_BOTTLE));
            }
        }
    }
}