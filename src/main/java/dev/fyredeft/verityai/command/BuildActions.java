package dev.fyredeft.verityai.command;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public final class BuildActions {
 private BuildActions() {}
 public static void buildStarterHut(ServerPlayerEntity player) {
  ServerWorld world=player.getLevel();
  BlockPos base=player.blockPosition().offset(3,0,3);
  for(int x=0;x<5;x++) for(int z=0;z<5;z++) {
   world.setBlock(base.offset(x,0,z),Blocks.OAK_PLANKS.defaultBlockState(),3);
   for(int y=1;y<=3;y++) {
    boolean edge=x==0||x==4||z==0||z==4;
    if(edge) {
     boolean doorway=(z==0&&x==2&&y<=2);
     boolean window=(y==2&&((x==0&&z==2)||(x==4&&z==2)));
     if(doorway) world.setBlock(base.offset(x,y,z),Blocks.AIR.defaultBlockState(),3);
     else if(window) world.setBlock(base.offset(x,y,z),Blocks.GLASS.defaultBlockState(),3);
     else world.setBlock(base.offset(x,y,z),Blocks.OAK_PLANKS.defaultBlockState(),3);
    } else {
     world.setBlock(base.offset(x,y,z),Blocks.AIR.defaultBlockState(),3);
    }
   }
   world.setBlock(base.offset(x,4,z),Blocks.OAK_PLANKS.defaultBlockState(),3);
  }
  world.setBlock(base.offset(1,1,1),Blocks.TORCH.defaultBlockState(),3);
  world.setBlock(base.offset(3,1,1),Blocks.CRAFTING_TABLE.defaultBlockState(),3);
  world.setBlock(base.offset(3,1,3),Blocks.CHEST.defaultBlockState(),3);
  player.sendMessage(new net.minecraft.util.text.StringTextComponent("Verity: Your little starter hut is ready. I left the chest empty... for now."),player.getUUID());
 }
}
