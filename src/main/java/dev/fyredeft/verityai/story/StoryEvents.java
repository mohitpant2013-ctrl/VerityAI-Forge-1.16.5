package dev.fyredeft.verityai.story;
import dev.fyredeft.verityai.VerityAI;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=VerityAI.MODID)
public class StoryEvents {
 @SubscribeEvent public static void onWake(PlayerWakeUpEvent event) {
  if(!(event.getPlayer() instanceof ServerPlayerEntity)||!VerityAI.HORROR_ENABLED.get())return;
  ServerPlayerEntity player=(ServerPlayerEntity)event.getPlayer(); if(player.level.isClientSide)return;
  CompoundNBT data=player.getPersistentData(); int nights=data.getInt("verityai_nights_slept")+1; data.putInt("verityai_nights_slept",nights);
  data.putInt("verityai_last_sleep_x",player.blockPosition().getX()); data.putInt("verityai_last_sleep_y",player.blockPosition().getY()); data.putInt("verityai_last_sleep_z",player.blockPosition().getZ());
  if(nights==3) player.sendMessage(new StringTextComponent("Verity: You always wake up in the same place. I wonder what happens when you don't."),player.getUUID());
  else if(nights>3&&nights%2==0) player.sendMessage(new StringTextComponent("Verity: I remembered where you slept. I was nearby."),player.getUUID());
 }
}
