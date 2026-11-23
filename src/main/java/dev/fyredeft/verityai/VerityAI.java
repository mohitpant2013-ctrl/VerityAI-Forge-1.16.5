package dev.fyredeft.verityai;

import dev.fyredeft.verityai.entity.VerityEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.RegistryObject;

@Mod(VerityAI.MODID)
public class VerityAI {
    public static final String MODID = "verityai";
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);
    public static final RegistryObject<EntityType<VerityEntity>> VERITY = ENTITIES.register("verity",
        () -> EntityType.Builder.of(VerityEntity::new, EntityClassification.CREATURE)
            .sized(0.7F, 0.7F).clientTrackingRange(8).build("verityai:verity"));

    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<String> API_KEY;
    public static final ForgeConfigSpec.ConfigValue<String> MODEL;
    public static final ForgeConfigSpec.BooleanValue HORROR_ENABLED;
    public static final ForgeConfigSpec.BooleanValue STALKER_ENABLED;
    public static final ForgeConfigSpec.BooleanValue AUTO_SPAWN;
    public static final ForgeConfigSpec.IntValue HORROR_INTENSITY;
    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();
        b.push("ai");
        API_KEY = b.comment("Your private Gemini API key. Never share or commit this file.")
            .define("apiKey", "");
        MODEL = b.define("model", "gemini-2.5-flash");
        b.pop();
        b.push("story");
        HORROR_ENABLED = b.define("horrorEnabled", true);
        STALKER_ENABLED = b.define("stalkerEnabled", true);
        AUTO_SPAWN = b.define("autoSpawn", true);
        HORROR_INTENSITY = b.defineInRange("horrorIntensity", 2, 0, 3);
        b.pop();
        SPEC = b.build();
    }

    public VerityAI() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ENTITIES.register(bus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC);
    }
}
