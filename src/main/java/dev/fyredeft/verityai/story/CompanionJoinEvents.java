package dev.fyredeft.verityai.story;

import dev.fyredeft.verityai.VerityAI;
import dev.fyredeft.verityai.entity.VerityEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Gives each player their own first-meeting companion on their first join. */
@Mod.EventBusSubscriber(modid = VerityAI.MODID)
public final class CompanionJoinEvents {
    private CompanionJoinEvents() {}

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        if (player.getPersistentData().getBoolean("verityai_first_meeting")) return;

        ServerWorld world = (ServerWorld) player.level;
        Entity entity = VerityAI.VERITY.get().create(world);
        if (!(entity instanceof VerityEntity)) return;

        VerityEntity verity = (VerityEntity) entity;
        verity.setOwnerUUID(player.getUUID());
        verity.moveTo(player.getX() + 2.0D, player.getY(), player.getZ() + 2.0D, 0.0F, 0.0F);
        if (world.addFreshEntity(verity)) {
            player.getPersistentData().putBoolean("verityai_first_meeting", true);
            player.sendMessage(new StringTextComponent("Verity: Oh! Hello. I was just looking around. Need a hand with your base?"), player.getUUID());
        }
    }
}
