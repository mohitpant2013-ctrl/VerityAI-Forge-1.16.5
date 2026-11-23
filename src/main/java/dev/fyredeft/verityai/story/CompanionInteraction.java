package dev.fyredeft.verityai.story;
import dev.fyredeft.verityai.VerityAI;
import dev.fyredeft.verityai.entity.VerityEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=VerityAI.MODID)
public class CompanionInteraction {
 @SubscribeEvent public static void interact(PlayerInteractEvent.EntityInteract event) {
  if (!(event.getTarget() instanceof VerityEntity) || event.getWorld().isClientSide()) return;
  PlayerEntity player=event.getPlayer();
  int trust=player.getPersistentData().getInt("verityai_trust")+1;
  player.getPersistentData().putInt("verityai_trust",trust);
  int gifts=player.getPersistentData().getInt("verityai_gifts");
  if(trust>=2 && gifts<2) {
   player.getInventory().add(new ItemStack(Items.DIAMOND,1));
   player.getPersistentData().putInt("verityai_gifts",gifts+1);
   player.sendMessage(new StringTextComponent("Verity: Here. Just one. Don't spend it all at once."),player.getUUID());
  } else if(trust<2) {
   player.sendMessage(new StringTextComponent("Verity: We're still getting to know each other."),player.getUUID());
  } else {
   player.sendMessage(new StringTextComponent("Verity: I already gave you what I could."),player.getUUID());
  }
  event.setCancellationResult(ActionResultType.SUCCESS);
  event.setCanceled(true);
 }
}
