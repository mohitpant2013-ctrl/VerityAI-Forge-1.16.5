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
   .then(Commands.literal("build").then(Commands.literal("hut").executes(ctx->{
    ServerPlayerEntity player=ctx.getSource().getPlayerOrException();
    BuildActions.buildStarterHut(player);
    return 1;
   })))
   .then(Commands.literal("ask").then(Commands.argument("message",StringArgumentType.greedyString()).executes(ctx->{
    String key=VerityAI.API_KEY.get(); if(key==null||key.trim().isEmpty()){ctx.getSource().sendFailure(new StringTextComponent("Set apiKey in config/verityai-common.toml, then restart."));return 0;}
    ServerPlayerEntity player=ctx.getSource().getPlayerOrException();
    String prompt=StringArgumentType.getString(ctx,"message");
    int trust=player.getPersistentData().getInt("verityai_trust");
    int nights=player.getPersistentData().getInt("verityai_nights_slept");
    prompt="Minecraft-only player context: trust="+trust+", nights slept="+nights+". Player asks: "+prompt;
    String model=VerityAI.MODEL.get();
    ctx.getSource().sendSuccess(new StringTextComponent("Verity is thinking..."),false);
    GeminiClient.ask(key,model,prompt,answer->ctx.getSource().getServer().execute(()->ctx.getSource().sendSuccess(new StringTextComponent("Verity: "+answer),false)));
    return 1;
   })))
   .then(Commands.literal("memory").executes(ctx->{
    ServerPlayerEntity p=ctx.getSource().getPlayerOrException();
    net.minecraft.nbt.CompoundNBT n=p.getPersistentData();
    ctx.getSource().sendSuccess(new StringTextComponent("Verity remembers: trust "+n.getInt("verityai_trust")+", nights slept "+n.getInt("verityai_nights_slept")+", last sleep at "+n.getInt("verityai_last_sleep_x")+", "+n.getInt("verityai_last_sleep_y")+", "+n.getInt("verityai_last_sleep_z")+"."),false);
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
