package dev.fyredeft.verityai;

import dev.fyredeft.verityai.entity.VerityEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.api.distmarker.Dist;
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
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);
    public static final RegistryObject<EntityType<VerityEntity>> VERITY =
            ENTITIES.register("verity", () -> EntityType.Builder
                    .of(VerityEntity::new, EntityClassification.CREATURE)
                    .sized(1.0F, 1.0F).clientTrackingRange(8).build("verityai:verity"));

    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<String> API_KEY;
    public static final ForgeConfigSpec.ConfigValue<String> MODEL;
    public static final ForgeConfigSpec.BooleanValue HORROR_ENABLED;
    public static final ForgeConfigSpec.BooleanValue STALKER_ENABLED;
    public static final ForgeConfigSpec.BooleanValue AUTO_SPAWN;
    public static final ForgeConfigSpec.IntValue HORROR_INTENSITY;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("ai");
        API_KEY = builder.comment("Private Gemini API key. Enter it in Mods > Verity AI > Config.")
                .define("apiKey", "");
        MODEL = builder.define("model", "gemini-2.5-flash");
        builder.pop();

        builder.push("story");
        HORROR_ENABLED = builder.define("horrorEnabled", true);
        STALKER_ENABLED = builder.define("stalkerEnabled", true);
        AUTO_SPAWN = builder.define("autoSpawn", true);
        HORROR_INTENSITY = builder.defineInRange("horrorIntensity", 2, 0, 3);
        builder.pop();
        SPEC = builder.build();
    }

    public VerityAI() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ENTITIES.register(modBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC);

        // Keep all GUI class references inside a client-only helper.
        DistExecutor.safeRunWhenOn(Dist.CLIENT,
                () -> dev.fyredeft.verityai.client.VerityClientSetup::registerConfigScreen);
    }
}
