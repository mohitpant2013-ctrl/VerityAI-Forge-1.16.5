package dev.fyredeft.verityai.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.fyredeft.verityai.entity.VerityEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * Original low-poly Verity model: a floating golden cube, tiny side fins,
 * two dark eyes and a small mouth. Uses a vanilla solid-yellow texture so
 * no external texture dependency or coremod is required.
 */
public class VerityModel extends EntityModel<VerityEntity> {
    private final ModelRenderer body;
    private final ModelRenderer crown;
    private final ModelRenderer leftFin;
    private final ModelRenderer rightFin;
    private final ModelRenderer leftEye;
    private final ModelRenderer rightEye;
    private final ModelRenderer mouth;
    private final ModelRenderer antenna;
    private final ModelRenderer antennaTip;

    public VerityModel() {
        texWidth = 64;
        texHeight = 32;

        body = new ModelRenderer(this, 0, 0);
        body.setPos(0.0F, 16.0F, 0.0F);
        body.addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F);

        crown = new ModelRenderer(this, 0, 0);
        crown.setPos(0.0F, 11.8F, 0.0F);
        crown.addBox(-2.4F, -1.0F, -2.4F, 4.8F, 1.2F, 4.8F);

        leftFin = new ModelRenderer(this, 0, 0);
        leftFin.setPos(-4.0F, 16.0F, 0.0F);
        leftFin.addBox(-1.5F, -1.2F, -1.0F, 1.5F, 2.4F, 2.0F);

        rightFin = new ModelRenderer(this, 0, 0);
        rightFin.setPos(4.0F, 16.0F, 0.0F);
        rightFin.addBox(0.0F, -1.2F, -1.0F, 1.5F, 2.4F, 2.0F);

        leftEye = new ModelRenderer(this, 0, 0);
        leftEye.setPos(-1.55F, 15.2F, -4.05F);
        leftEye.addBox(-0.55F, -0.65F, -0.15F, 1.1F, 1.3F, 0.3F);

        rightEye = new ModelRenderer(this, 0, 0);
        rightEye.setPos(1.55F, 15.2F, -4.05F);
        rightEye.addBox(-0.55F, -0.65F, -0.15F, 1.1F, 1.3F, 0.3F);

        mouth = new ModelRenderer(this, 0, 0);
        mouth.setPos(0.0F, 17.3F, -4.05F);
        mouth.addBox(-0.65F, -0.15F, -0.16F, 1.3F, 0.3F, 0.3F);

        antenna = new ModelRenderer(this, 0, 0);
        antenna.setPos(0.0F, 12.0F, 0.0F);
        antenna.addBox(-0.25F, -1.8F, -0.25F, 0.5F, 1.8F, 0.5F);

        antennaTip = new ModelRenderer(this, 0, 0);
        antennaTip.setPos(0.0F, 10.2F, 0.0F);
        antennaTip.addBox(-0.65F, -0.65F, -0.65F, 1.3F, 1.3F, 1.3F);
    }

    @Override
    public void setupAnim(VerityEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        float bob = (float) Math.sin(ageInTicks * 0.09F) * 1.15F;
        body.y = 16.0F + bob;
        crown.y = 11.8F + bob;
        leftFin.y = 16.0F + bob;
        rightFin.y = 16.0F + bob;
        leftEye.y = 15.2F + bob;
        rightEye.y = 15.2F + bob;
        mouth.y = 17.3F + bob;
        antenna.y = 12.0F + bob;
        antennaTip.y = 10.2F + bob;
        body.yRot = (float) Math.sin(ageInTicks * 0.035F) * 0.08F;
        leftFin.zRot = (float) Math.sin(ageInTicks * 0.12F) * 0.12F;
        rightFin.zRot = -(float) Math.sin(ageInTicks * 0.12F) * 0.12F;
        antennaTip.xRot = (float) Math.sin(ageInTicks * 0.1F) * 0.15F;
    }

    @Override
    public void renderToBuffer(MatrixStack stack, IVertexBuilder buffer, int packedLight,
                               int packedOverlay, float red, float green, float blue, float alpha) {
        // Yellow concrete supplies a clean golden base; black-tinted face pieces
        // create readable eyes and a tiny mouth without an extra binary texture.
        body.render(stack, buffer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, alpha);
        crown.render(stack, buffer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, alpha);
        leftFin.render(stack, buffer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, alpha);
        rightFin.render(stack, buffer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, alpha);
        antenna.render(stack, buffer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, alpha);
        antennaTip.render(stack, buffer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, alpha);
        leftEye.render(stack, buffer, packedLight, packedOverlay, 0.03F, 0.03F, 0.03F, alpha);
        rightEye.render(stack, buffer, packedLight, packedOverlay, 0.03F, 0.03F, 0.03F, alpha);
        mouth.render(stack, buffer, packedLight, packedOverlay, 0.03F, 0.03F, 0.03F, alpha);
    }
}
