package dev.fyredeft.verityai.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.fyredeft.verityai.entity.VerityEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class VerityModel extends EntityModel<VerityEntity> {
    private final ModelRenderer body;
    public VerityModel() {
        texWidth = 32;
        texHeight = 32;
        body = new ModelRenderer(this, 0, 0);
        body.setPos(0.0F, 16.0F, 0.0F);
        body.addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F);
    }
    @Override
    public void setupAnim(VerityEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        body.y = 16.0F + (float)Math.sin(ageInTicks * 0.08F) * 1.5F;
    }
    @Override
    public void renderToBuffer(MatrixStack stack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body.render(stack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
