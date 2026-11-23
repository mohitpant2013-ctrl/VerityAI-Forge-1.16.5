package dev.fyredeft.verityai.client;

import dev.fyredeft.verityai.entity.VerityEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

/** Standard Forge entity renderer; no OptiFine hooks or coremod patches. */
public class VerityRenderer extends MobRenderer<VerityEntity, VerityModel> {
    private static final ResourceLocation TEXTURE =
        new ResourceLocation("verityai", "textures/entity/verity.png");

    public VerityRenderer(EntityRendererManager manager) {
        super(manager, new VerityModel(), 0.25F);
    }

    @Override
    public ResourceLocation getTextureLocation(VerityEntity entity) {
        return TEXTURE;
    }
}
