package dev.fyredeft.verityai;
import dev.fyredeft.verityai.entity.VerityEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.MobEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=VerityAI.MODID,bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {
 @SubscribeEvent public static void attributes(EntityAttributeCreationEvent event) {
  event.put(VerityAI.VERITY.get(), MobEntity.createMobAttributes()
   .add(Attributes.MAX_HEALTH,20.0D).add(Attributes.MOVEMENT_SPEED,0.25D).build());
 }
}
