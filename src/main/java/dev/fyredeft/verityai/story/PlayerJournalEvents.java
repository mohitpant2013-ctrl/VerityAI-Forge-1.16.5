package dev.fyredeft.verityai.story;

import dev.fyredeft.verityai.VerityAI;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Stores only Minecraft-world facts in the player's own persistent data. */
@Mod.EventBusSubscriber(modid = VerityAI.MODID)
public final class PlayerJournalEvents {
    private PlayerJournalEvents() {}

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.player;
        if (player.level.isClientSide || player.tickCount % 1200 != 0) return;

        CompoundNBT data = player.getPersistentData();
        ResourceLocation biomeId = player.level.getBiome(player.blockPosition()).getRegistryName();
        String biome = biomeId == null ? "unknown" : biomeId.toString();
        String dimension = player.level.dimension().location().toString();

        data.putInt("verityai_last_x", player.blockPosition().getX());
        data.putInt("verityai_last_y", player.blockPosition().getY());
        data.putInt("verityai_last_z", player.blockPosition().getZ());
        data.putString("verityai_last_biome", biome);
        data.putString("verityai_last_dimension", dimension);

        String previous = data.getString("verityai_previous_biome");
        if (!biome.equals(previous)) {
            data.putString("verityai_previous_biome", biome);
            ListNBT journal = data.getList("verityai_biome_journal", 8);
            journal.add(StringNBT.valueOf(biome));
            while (journal.size() > 6) journal.remove(0);
            data.put("verityai_biome_journal", journal);
        }
    }
}
