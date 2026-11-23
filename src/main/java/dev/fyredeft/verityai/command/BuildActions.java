package dev.fyredeft.verityai.command;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public final class BuildActions {
    private BuildActions() {}

    public static void buildStarterHut(ServerPlayerEntity player) {
        ServerWorld world = player.getLevel();
        BlockPos base = player.blockPosition().offset(3, 0, 3);

        for (int x = 0; x < 5; x++) {
            for (int z = 0; z < 5; z++) {
                world.setBlock(base.offset(x, 0, z), Blocks.OAK_PLANKS.defaultBlockState(), 3);
                for (int y = 1; y <= 3; y++) {
                    boolean edge = x == 0 || x == 4 || z == 0 || z == 4;
                    boolean doorway = z == 0 && x == 2 && y <= 2;
                    boolean window = y == 2 && ((x == 0 && z == 2) || (x == 4 && z == 2));
                    if (!edge || doorway) {
                        world.setBlock(base.offset(x, y, z), Blocks.AIR.defaultBlockState(), 3);
                    } else if (window) {
                        world.setBlock(base.offset(x, y, z), Blocks.GLASS.defaultBlockState(), 3);
                    } else {
                        world.setBlock(base.offset(x, y, z), Blocks.OAK_PLANKS.defaultBlockState(), 3);
                    }
                }
                world.setBlock(base.offset(x, 4, z), Blocks.OAK_PLANKS.defaultBlockState(), 3);
            }
        }

        world.setBlock(base.offset(1, 1, 1), Blocks.TORCH.defaultBlockState(), 3);
        world.setBlock(base.offset(3, 1, 1), Blocks.CRAFTING_TABLE.defaultBlockState(), 3);
        BlockPos chestPos = base.offset(3, 1, 3);
        world.setBlock(chestPos, Blocks.CHEST.defaultBlockState(), 3);

        // The starter bundle is granted once per player, even if they rebuild the hut.
        if (!player.getPersistentData().getBoolean("verityai_starter_kit")) {
            if (world.getBlockEntity(chestPos) instanceof ChestTileEntity) {
                ChestTileEntity chest = (ChestTileEntity) world.getBlockEntity(chestPos);
                chest.setItem(0, new ItemStack(Items.BREAD, 8));
                chest.setItem(1, new ItemStack(Items.TORCH, 16));
                chest.setItem(2, new ItemStack(Items.WHEAT_SEEDS, 12));
                chest.setItem(3, new ItemStack(Items.OAK_LOG, 16));
                player.getPersistentData().putBoolean("verityai_starter_kit", true);
            }
        }
        player.sendMessage(new net.minecraft.util.text.StringTextComponent(
            "Verity: Your starter hut is ready. I left a few supplies in the chest."), player.getUUID());
    }

    public static void buildCamp(ServerPlayerEntity player) {
        ServerWorld world = player.getLevel();
        BlockPos base = player.blockPosition().offset(3, 0, 3);
        world.setBlock(base, Blocks.CAMPFIRE.defaultBlockState(), 3);
        world.setBlock(base.offset(1, 0, 0), Blocks.OAK_LOG.defaultBlockState(), 3);
        world.setBlock(base.offset(-1, 0, 0), Blocks.OAK_LOG.defaultBlockState(), 3);
        world.setBlock(base.offset(0, 0, 1), Blocks.OAK_PLANKS.defaultBlockState(), 3);
        world.setBlock(base.offset(0, 0, -1), Blocks.OAK_PLANKS.defaultBlockState(), 3);
        world.setBlock(base.offset(2, 0, 2), Blocks.LANTERN.defaultBlockState(), 3);
        player.sendMessage(new net.minecraft.util.text.StringTextComponent(
            "Verity: A small camp. We can stay here until morning."), player.getUUID());
    }

    public static void buildFarm(ServerPlayerEntity player) {
        ServerWorld world = player.getLevel();
        BlockPos base = player.blockPosition().offset(4, 0, 4);
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                BlockPos plot = base.offset(x, 0, z);
                if (x == 0 && z == 0) {
                    world.setBlock(plot, Blocks.WATER.defaultBlockState(), 3);
                } else {
                    world.setBlock(plot, Blocks.FARMLAND.defaultBlockState(), 3);
                    world.setBlock(plot.above(), Blocks.WHEAT.defaultBlockState()
                        .setValue(net.minecraft.block.CropsBlock.AGE, 7), 3);
                }
            }
        }
        player.sendMessage(new net.minecraft.util.text.StringTextComponent(
            "Verity: I planted a small wheat patch. It should keep growing."), player.getUUID());
    }

    public static void buildGarden(ServerPlayerEntity player) {
        ServerWorld world = player.getLevel();
        BlockPos base = player.blockPosition().offset(3, 0, 3);
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                BlockPos flower = base.offset(x, 0, z);
                world.setBlock(flower, Blocks.GRASS_BLOCK.defaultBlockState(), 3);
                if ((x + z) % 3 == 0) world.setBlock(flower.above(), Blocks.POPPY.defaultBlockState(), 3);
                else if ((x * z) % 3 == 0) world.setBlock(flower.above(), Blocks.DANDELION.defaultBlockState(), 3);
                else if ((x - z) % 4 == 0) world.setBlock(flower.above(), Blocks.ALLIUM.defaultBlockState(), 3);
            }
        }
        player.sendMessage(new net.minecraft.util.text.StringTextComponent(
            "Verity: A little garden. The flowers make this place feel less empty."), player.getUUID());
    }

    public static void buildWatchtower(ServerPlayerEntity player) {
        ServerWorld world = player.getLevel();
        BlockPos base = player.blockPosition().offset(3, 0, 3);
        for (int y = 0; y <= 5; y++) {
            world.setBlock(base.offset(-1, y, -1), Blocks.OAK_LOG.defaultBlockState(), 3);
            world.setBlock(base.offset(1, y, -1), Blocks.OAK_LOG.defaultBlockState(), 3);
            world.setBlock(base.offset(-1, y, 1), Blocks.OAK_LOG.defaultBlockState(), 3);
            world.setBlock(base.offset(1, y, 1), Blocks.OAK_LOG.defaultBlockState(), 3);
        }
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                world.setBlock(base.offset(x, 6, z), Blocks.OAK_PLANKS.defaultBlockState(), 3);
            }
        }
        world.setBlock(base.offset(0, 7, 0), Blocks.TORCH.defaultBlockState(), 3);
        player.sendMessage(new net.minecraft.util.text.StringTextComponent(
            "Verity: A lookout. You can see a little farther from up there."), player.getUUID());
    }

}
