package dev.fyredeft.verityai.command;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.fyredeft.verityai.VerityAI;
import dev.fyredeft.verityai.ai.GeminiClient;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=VerityAI.MODID)
public class VerityCommands {
 @SubscribeEvent public static void register(RegisterCommandsEvent event) {
  event.getDispatcher().register(Commands.literal("verity")
   .then(Commands.literal("summon").requires(s->s.hasPermission(2)).executes(ctx->{
    ServerWorld world=ctx.getSource().getLevel(); Entity entity=VerityAI.VERITY.get().create(world); if(entity==null)return 0;
    net.minecraft.util.math.vector.Vector3d p=ctx.getSource().getPosition(); entity.moveTo(p.x,p.y,p.z,0,0); world.addFreshEntity(entity);
    ctx.getSource().sendSuccess(new StringTextComponent("A small yellow visitor has arrived."),true); return 1;
   }))
   .then(Commands.literal("build")
    .then(Commands.literal("hut").executes(ctx->{
     ServerPlayerEntity player=ctx.getSource().getPlayerOrException();
     BuildActions.buildStarterHut(player);
     return 1;
    }))
    .then(Commands.literal("camp").executes(ctx->{
     ServerPlayerEntity player=ctx.getSource().getPlayerOrException();
     BuildActions.buildCamp(player);
     return 1;
    }))
    .then(Commands.literal("farm").executes(ctx->{
     ServerPlayerEntity player=ctx.getSource().getPlayerOrException();
     BuildActions.buildFarm(player);
     return 1;
    }))
    .then(Commands.literal("garden").executes(ctx->{
     ServerPlayerEntity player=ctx.getSource().getPlayerOrException();
     BuildActions.buildGarden(player);
     return 1;
    }))
    .then(Commands.literal("watchtower").executes(ctx->{
     ServerPlayerEntity player=ctx.getSource().getPlayerOrException();
     BuildActions.buildWatchtower(player);
     return 1;
    })))
   .then(Commands.literal("ask").then(Commands.argument("message",StringArgumentType.greedyString()).executes(ctx->{
    String key=VerityAI.API_KEY.get(); if(key==null||key.trim().isEmpty()){ctx.getSource().sendFailure(new StringTextComponent("Set apiKey in config/verityai-common.toml, then restart."));return 0;}
    ServerPlayerEntity player=ctx.getSource().getPlayerOrException();
    final String playerQuestion=StringArgumentType.getString(ctx,"message");
    int trust=player.getPersistentData().getInt("verityai_trust");
    int nights=player.getPersistentData().getInt("verityai_nights_slept");
    net.minecraft.nbt.CompoundNBT memory=player.getPersistentData();
    String prompt="Minecraft-only player context: trust="+trust+", nights slept="+nights+
      ", last biome="+memory.getString("verityai_last_biome")+
      ", last dimension="+memory.getString("verityai_last_dimension")+
      ", last coordinates="+memory.getInt("verityai_last_x")+","+memory.getInt("verityai_last_y")+","+memory.getInt("verityai_last_z")+
      ". Recent biome journal="+memory.getList("verityai_biome_journal",8).toString()+
      ". Recent conversation="+memory.getList("verityai_chat_memory",8).toString()+
      ". Player asks: "+playerQuestion;
    String model=VerityAI.MODEL.get();
    ctx.getSource().sendSuccess(new StringTextComponent("Verity is thinking..."),false);
    GeminiClient.ask(key,model,prompt,answer->ctx.getSource().getServer().execute(()->{
     net.minecraft.nbt.CompoundNBT data=player.getPersistentData();
     net.minecraft.nbt.ListNBT chat=data.getList("verityai_chat_memory",8);
     chat.add(net.minecraft.nbt.StringNBT.valueOf("You: "+playerQuestion));
     chat.add(net.minecraft.nbt.StringNBT.valueOf("Verity: "+answer));
     while(chat.size()>10) chat.remove(0);
     data.put("verityai_chat_memory",chat);
     player.sendMessage(new StringTextComponent("Verity: "+answer),player.getUUID());
    }));
    return 1;
   })))
   .then(Commands.literal("memory").executes(ctx->{
    ServerPlayerEntity p=ctx.getSource().getPlayerOrException();
    net.minecraft.nbt.CompoundNBT n=p.getPersistentData();
    ctx.getSource().sendSuccess(new StringTextComponent("Verity remembers: trust "+n.getInt("verityai_trust")+", nights slept "+n.getInt("verityai_nights_slept")+", last sleep at "+n.getInt("verityai_last_sleep_x")+", "+n.getInt("verityai_last_sleep_y")+", "+n.getInt("verityai_last_sleep_z")+"."),false);
    return 1;
   }))
   .then(Commands.literal("journal").executes(ctx->{
    ServerPlayerEntity p=ctx.getSource().getPlayerOrException();
    net.minecraft.nbt.CompoundNBT n=p.getPersistentData();
    String biomes=n.getList("verityai_biome_journal",8).toString();
    ctx.getSource().sendSuccess(new StringTextComponent("Verity's journal | Last biome: "+n.getString("verityai_last_biome")+
      " | Dimension: "+n.getString("verityai_last_dimension")+" | Last known position: "+
      n.getInt("verityai_last_x")+", "+n.getInt("verityai_last_y")+", "+n.getInt("verityai_last_z")+
      " | Visited biomes: "+biomes),false);
    return 1;
   }))
   .then(Commands.literal("help").executes(ctx->{
    ctx.getSource().sendSuccess(new StringTextComponent(
     "Verity commands: /verity summon (OP), /verity ask <message>, /verity build hut, /verity build camp, /verity build farm, /verity build garden, /verity build watchtower, /verity memory, /verity journal, /verity status"),false);
    return 1;
   }))
   .then(Commands.literal("status").executes(ctx->{
    ServerPlayerEntity p=ctx.getSource().getPlayerOrException();
    int trust=p.getPersistentData().getInt("verityai_trust");
    int nights=p.getPersistentData().getInt("verityai_nights_slept");
    String mood=nights>=3?"Uneasy":(trust>=2?"Happy":"Curious");
    ctx.getSource().sendSuccess(new StringTextComponent("Verity | Mood: "+mood+" | Trust: "+trust+" | Nights: "+nights),false);
    return 1;
   }))
  );
 }
}
