package dev.fyredeft.verityai.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.fyredeft.verityai.entity.VerityEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * Original Verity companion model.
 *
 * A small hovering brass automaton with a dark cyan-lit faceplate, layered
 * shell, side stabilizers, chest sigil and a segmented rotating halo. All
 * geometry uses the vanilla 1.16.5 ModelRenderer pipeline so it does not
 * require renderer mixins or OptiFine-specific hooks.
 */
public class VerityModel extends EntityModel<VerityEntity> {
    private final ModelRenderer root;
    private final ModelRenderer shell;
    private final ModelRenderer crown;
    private final ModelRenderer lowerShell;
    private final ModelRenderer face;
    private final ModelRenderer leftWing;
    private final ModelRenderer rightWing;
    private final ModelRenderer leftWingTip;
    private final ModelRenderer rightWingTip;
    private final ModelRenderer chestSigil;
    private final ModelRenderer antennaStem;
    private final ModelRenderer antennaCore;
    private final ModelRenderer halo;
    private final ModelRenderer haloFront;
    private final ModelRenderer haloBack;
    private final ModelRenderer haloLeft;
    private final ModelRenderer haloRight;
    private final ModelRenderer leftThruster;
    private final ModelRenderer rightThruster;

    public VerityModel() {
        texWidth = 64;
        texHeight = 64;

        root = new ModelRenderer(this);
        root.setPos(0.0F, 16.0F, 0.0F);

        shell = new ModelRenderer(this, 0, 0);
        shell.addBox(-4.0F, -4.0F, -3.0F, 8.0F, 8.0F, 6.0F);
        root.addChild(shell);

        crown = new ModelRenderer(this, 0, 16);
        crown.addBox(-3.5F, -5.5F, -2.5F, 7.0F, 2.0F, 5.0F);
        root.addChild(crown);

        lowerShell = new ModelRenderer(this, 40, 32);
        lowerShell.addBox(-2.5F, 3.2F, -2.1F, 5.0F, 2.0F, 4.2F);
        root.addChild(lowerShell);

        // Deep glass faceplate; the custom atlas carries the cyan eye pixels.
        face = new ModelRenderer(this, 40, 0);
        face.addBox(-3.0F, -1.6F, -3.75F, 6.0F, 3.2F, 1.0F);
        root.addChild(face);

        chestSigil = new ModelRenderer(this, 32, 16);
        chestSigil.addBox(-1.0F, 1.5F, -3.28F, 2.0F, 2.0F, 0.25F);
        root.addChild(chestSigil);

        leftWing = new ModelRenderer(this, 0, 32);
        leftWing.setPos(-3.6F, -0.2F, 0.0F);
        leftWing.addBox(-3.0F, -0.65F, -1.5F, 3.2F, 1.3F, 3.0F);
        root.addChild(leftWing);

        rightWing = new ModelRenderer(this, 0, 32);
        rightWing.setPos(3.6F, -0.2F, 0.0F);
        rightWing.addBox(-0.2F, -0.65F, -1.5F, 3.2F, 1.3F, 3.0F);
        root.addChild(rightWing);

        leftWingTip = new ModelRenderer(this, 24, 32);
        leftWingTip.addBox(-6.2F, -0.35F, -0.8F, 1.5F, 0.7F, 1.6F);
        leftWing.addChild(leftWingTip);

        rightWingTip = new ModelRenderer(this, 24, 32);
        rightWingTip.addBox(4.7F, -0.35F, -0.8F, 1.5F, 0.7F, 1.6F);
        rightWing.addChild(rightWingTip);

        leftThruster = new ModelRenderer(this, 24, 32);
        leftThruster.addBox(-2.3F, 5.0F, -1.4F, 1.2F, 1.0F, 1.2F);
        root.addChild(leftThruster);

        rightThruster = new ModelRenderer(this, 24, 32);
        rightThruster.addBox(1.1F, 5.0F, -1.4F, 1.2F, 1.0F, 1.2F);
        root.addChild(rightThruster);

        antennaStem = new ModelRenderer(this, 24, 32);
        antennaStem.addBox(-0.25F, -7.1F, -0.25F, 0.5F, 1.8F, 0.5F);
        root.addChild(antennaStem);

        antennaCore = new ModelRenderer(this, 24, 32);
        antennaCore.addBox(-0.75F, -6.3F, -0.75F, 1.5F, 1.5F, 1.5F);
        root.addChild(antennaCore);

        halo = new ModelRenderer(this);
        halo.setPos(0.0F, -6.1F, 0.0F);
        root.addChild(halo);

        haloFront = new ModelRenderer(this, 24, 32);
        haloFront.addBox(-2.3F, -0.2F, -0.2F, 4.6F, 0.4F, 0.55F);
        halo.addChild(haloFront);

        haloBack = new ModelRenderer(this, 24, 32);
        haloBack.addBox(-2.3F, -0.2F, -0.35F, 4.6F, 0.4F, 0.55F);
        halo.addChild(haloBack);

        haloLeft = new ModelRenderer(this, 24, 32);
        haloLeft.addBox(-0.25F, -0.2F, -2.3F, 0.5F, 0.4F, 4.6F);
        halo.addChild(haloLeft);

        haloRight = new ModelRenderer(this, 24, 32);
        haloRight.addBox(-0.25F, -0.2F, -2.3F, 0.5F, 0.4F, 4.6F);
        halo.addChild(haloRight);
    }

    @Override
    public void setupAnim(VerityEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        float bob = (float) Math.sin(ageInTicks * 0.075F) * 0.8F;
        float drift = (float) Math.sin(ageInTicks * 0.035F) * 0.09F;
        float wingBeat = (float) Math.sin(ageInTicks * 0.16F) * 0.22F;

        root.y = 16.0F + bob;
        root.yRot = drift + netHeadYaw * 0.0025F;
        root.xRot = (float) Math.sin(ageInTicks * 0.045F) * 0.035F;

        leftWing.zRot = wingBeat;
        rightWing.zRot = -wingBeat;
        leftWing.xRot = (float) Math.sin(ageInTicks * 0.11F) * 0.07F;
        rightWing.xRot = -leftWing.xRot;

        halo.yRot = ageInTicks * 0.035F;
        antennaCore.yRot = (float) Math.sin(ageInTicks * 0.09F) * 0.18F;
        antennaCore.xRot = (float) Math.cos(ageInTicks * 0.08F) * 0.12F;
    }

    @Override
    public void renderToBuffer(MatrixStack stack, IVertexBuilder buffer, int packedLight,
                               int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(stack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
