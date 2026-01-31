package net.jerika.furmutage.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jerika.furmutage.entity.animations.LooseBehemothHandAnimation;
import net.jerika.furmutage.entity.custom.LooseBehemothHand;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Loose Behemoth Hand model – geometry from Blockbench (Java Entity export).
 */
@OnlyIn(Dist.CLIENT)
public class LooseBehemothHandModel extends HierarchicalModel<LooseBehemothHand> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation(furmutage.MOD_ID, "loose_behemoth_hand"), "main");

    private final ModelPart root;

    public LooseBehemothHandModel(ModelPart root) {
        this.root = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Hand = partdefinition.addOrReplaceChild("Hand", CubeListBuilder.create().texOffs(84, 22).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 23.0F, 6.0F, 1.2217F, 0.0F, 0.0F));

        PartDefinition cube_r1 = Hand.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 44).addBox(-10.0F, -15.0F, 3.5F, 20.0F, 15.0F, 1.0F, new CubeDeformation(0.0F))
        .texOffs(0, 0).addBox(-10.0F, -14.0F, -4.5F, 20.0F, 16.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, -0.3491F, 0.0F, 0.0F));

        PartDefinition cube_r2 = Hand.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(82, 41).addBox(-5.0F, -8.0F, 0.0F, 10.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.0F, 6.0F, 0.3491F, 0.0F, 0.0F));

        PartDefinition cube_r3 = Hand.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(80, 36).addBox(-5.0F, -7.0F, 0.0F, 10.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 9.0F, 10.0F, 0.3491F, 0.0F, 0.0F));

        PartDefinition cube_r4 = Hand.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(84, 0).addBox(5.0F, -3.0F, 0.0F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
        .texOffs(82, 46).addBox(15.0F, -3.0F, 0.0F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
        .texOffs(56, 62).addBox(5.0F, -10.0F, 0.0F, 10.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.0F, 7.0F, 4.0F, 0.3491F, 0.0F, 0.0F));

        PartDefinition cube_r5 = Hand.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 24).addBox(-9.0F, -10.0F, -5.0F, 18.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.0F, 0.3491F, 0.0F, 0.0F));

        PartDefinition Pinkie = Hand.addOrReplaceChild("Pinkie", CubeListBuilder.create(), PartPose.offset(7.0F, -23.0F, 4.0F));

        PartDefinition cube_r6 = Pinkie.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(82, 56).addBox(-10.0F, -7.2143F, -2.9557F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(7.5F, -3.75F, 0.0F, 0.3491F, 0.0F, 0.0F));

        PartDefinition cube_r7 = Pinkie.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(56, 0).addBox(4.0F, -8.2143F, -1.9557F, 6.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -3.25F, -0.5F, 0.3491F, 0.0F, 0.0F));

        PartDefinition cube_r8 = Pinkie.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(56, 74).addBox(-11.0F, -21.0F, -3.5F, 6.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 14.75F, -5.5F, -0.3491F, 0.0F, 0.0F));

        PartDefinition Middle = Hand.addOrReplaceChild("Middle", CubeListBuilder.create(), PartPose.offset(0.0F, -23.0F, 4.0F));

        PartDefinition cube_r9 = Middle.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(84, 10).addBox(-10.0F, -7.2143F, -2.9557F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(7.5F, -3.75F, 0.0F, 0.3491F, 0.0F, 0.0F));

        PartDefinition cube_r10 = Middle.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(56, 18).addBox(-3.0F, -8.2143F, -1.9557F, 6.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.25F, -0.5F, 0.3491F, 0.0F, 0.0F));

        PartDefinition cube_r11 = Middle.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 78).addBox(-11.0F, -21.0F, -3.5F, 6.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 14.75F, -5.5F, -0.3491F, 0.0F, 0.0F));

        PartDefinition Index = Hand.addOrReplaceChild("Index", CubeListBuilder.create(), PartPose.offset(-7.0F, -23.0F, 4.0F));

        PartDefinition cube_r12 = Index.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(84, 16).addBox(-10.0F, -7.2143F, -2.9557F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(7.5F, -3.75F, 0.0F, 0.3491F, 0.0F, 0.0F));

        PartDefinition cube_r13 = Index.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 60).addBox(-10.0F, -8.2143F, -1.9557F, 6.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -3.25F, -0.5F, 0.3491F, 0.0F, 0.0F));

        PartDefinition cube_r14 = Index.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(28, 80).addBox(-10.0F, -21.0F, -3.5F, 6.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 14.75F, -5.5F, -0.3491F, 0.0F, 0.0F));

        PartDefinition Thumb = Hand.addOrReplaceChild("Thumb", CubeListBuilder.create(), PartPose.offset(10.0F, -16.0F, -1.0F));

        PartDefinition cube_r15 = Thumb.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(42, 44).addBox(-5.5F, -4.5F, -2.25F, 11.0F, 9.0F, 9.0F, new CubeDeformation(0.0F))
        .texOffs(56, 36).addBox(-6.5F, -3.5F, -2.75F, 11.0F, 7.0F, 1.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(5.1723F, 0.386F, -1.311F, -0.4181F, 0.5692F, -0.2351F));

        PartDefinition cube_r16 = Thumb.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(28, 62).addBox(-5.0F, -4.3015F, -2.7899F, 5.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 1.0F, 1.0F, -0.3491F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(LooseBehemothHand entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity.idleAnimationState, LooseBehemothHandAnimation.idle, ageInTicks, 1.0F);
        this.animate(entity.walkAnimationState, LooseBehemothHandAnimation.walk, ageInTicks, 1.0F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
