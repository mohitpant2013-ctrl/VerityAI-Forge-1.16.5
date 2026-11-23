package dev.fyredeft.verityai.client;

import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;

/**
 * Client-only registration kept in its own class so dedicated servers never
 * resolve Minecraft GUI classes while loading the common mod entry point.
 */
public final class VerityClientSetup {
    private VerityClientSetup() {}

    public static void registerConfigScreen() {
        ModLoadingContext.get().registerExtensionPoint(
            ExtensionPoint.CONFIGGUIFACTORY,
            () -> (minecraft, parent) -> new VerityConfigScreen(parent)
        );
    }
}
