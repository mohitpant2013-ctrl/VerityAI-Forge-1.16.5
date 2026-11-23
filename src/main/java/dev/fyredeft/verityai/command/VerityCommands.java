package dev.fyredeft.verityai.command;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.fyredeft.verityai.VerityAI;
import dev.fyredeft.verityai.ai.GeminiClient;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
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
   .then(Commands.literal("ask").then(Commands.argument("message",StringArgumentType.greedyString()).executes(ctx->{
    String key=VerityAI.API_KEY.get(); if(key==null||key.trim().isEmpty()){ctx.getSource().sendFailure(new StringTextComponent("Set apiKey in config/verityai-common.toml, then restart."));return 0;}
    String prompt=StringArgumentType.getString(ctx,"message"); String model=VerityAI.MODEL.get();
    ctx.getSource().sendSuccess(new StringTextComponent("Verity is thinking..."),false);
    GeminiClient.ask(key,model,prompt,answer->ctx.getSource().getServer().execute(()->ctx.getSource().sendSuccess(new StringTextComponent("Verity: "+answer),false)));
    return 1;
   })))
   .then(Commands.literal("status").executes(ctx->{ctx.getSource().sendSuccess(new StringTextComponent("Verity: Friendly | Mood: Curious | Story: First nights"),false);return 1;}))
  );
 }
}
