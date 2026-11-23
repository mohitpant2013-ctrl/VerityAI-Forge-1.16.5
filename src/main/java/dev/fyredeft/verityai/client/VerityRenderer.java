package dev.fyredeft.verityai.client;

import dev.fyredeft.verityai.entity.VerityEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

/** Standard Forge renderer with separate companion and Stalker atlases. */
public class VerityRenderer extends MobRenderer<VerityEntity, VerityModel> {
    private static final ResourceLocation VERITY_TEXTURE =
        new ResourceLocation("verityai", "textures/entity/verity.png");
    private static final ResourceLocation STALKER_TEXTURE =
        new ResourceLocation("verityai", "textures/entity/stalker.png");

    public VerityRenderer(EntityRendererManager manager) {
        super(manager, new VerityModel(), 0.25F);
    }

    @Override
    public ResourceLocation getTextureLocation(VerityEntity entity) {
        return entity.isStalkerStage() ? STALKER_TEXTURE : VERITY_TEXTURE;
    }
}
