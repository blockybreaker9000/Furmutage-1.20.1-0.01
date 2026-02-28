package net.jerika.furmutage.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jerika.furmutage.entity.custom.LatexNetherMantaRayMaleEntity;
import net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;

import java.util.List;

public class LatexNetherMantaRayMaleModel<T extends LatexNetherMantaRayMaleEntity> extends AdvancedHumanoidModel<T> {
    private final HumanoidAnimator<T, LatexNetherMantaRayMaleModel<T>> animator;
    private final ModelPart RightLeg;
    private final ModelPart LeftLeg;
    private final ModelPart Head;
    private final ModelPart Torso;
    private final ModelPart RightArm;
    private final ModelPart LeftArm;

    public LatexNetherMantaRayMaleModel(ModelPart root) {
        super(root);
        this.RightLeg = root.getChild("RightLeg");
        this.LeftLeg = root.getChild("LeftLeg");
        this.Head = root.getChild("Head");
        this.Torso = root.getChild("Torso");
        this.RightArm = root.getChild("RightArm");
        this.LeftArm = root.getChild("LeftArm");

        var tail = this.Torso.getChild("Tail");
        var tailPrimary = tail.getChild("TailPrimary");
        var tailSecondary = tailPrimary.getChild("TailSecondary");
        var tailTertiary = tailSecondary.getChild("TailTertiary");
        List<ModelPart> tailJoints = List.of(tailPrimary, tailSecondary, tailTertiary);

        var rightLowerLeg = this.RightLeg.getChild("RightLowerLeg");
        var rightFoot = rightLowerLeg.getChild("RightFoot");
        var leftLowerLeg = this.LeftLeg.getChild("LeftLowerLeg");
        var leftFoot = leftLowerLeg.getChild("LeftFoot");

        animator = HumanoidAnimator.of(this).hipOffset(-1.5f)
                .addPreset(AnimatorPresets.wolfLike(
                        this.Head, this.Head.getChild("LeftEar"), this.Head.getChild("RightEar"),
                        this.Torso, this.LeftArm, this.RightArm,
                        tail, tailJoints,
                        this.LeftLeg, leftLowerLeg, leftFoot, leftFoot.getChild("LeftPad"),
                        this.RightLeg, rightLowerLeg, rightFoot, rightFoot.getChild("RightPad")));
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-2.5F, 10.5F, 0.0F));
        RightLeg.addOrReplaceChild("RightThigh_r1", CubeListBuilder.create().texOffs(32, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));
        PartDefinition RightLowerLeg = RightLeg.addOrReplaceChild("RightLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));
        RightLowerLeg.addOrReplaceChild("RightCalf_r1", CubeListBuilder.create().texOffs(40, 22).addBox(-1.99F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));
        PartDefinition RightFoot = RightLowerLeg.addOrReplaceChild("RightFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));
        RightFoot.addOrReplaceChild("RightArch_r1", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));
        RightFoot.addOrReplaceChild("RightPad", CubeListBuilder.create().texOffs(29, 43).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

        PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(2.5F, 10.5F, 0.0F));
        LeftLeg.addOrReplaceChild("LeftThigh_r1", CubeListBuilder.create().texOffs(16, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));
        PartDefinition LeftLowerLeg = LeftLeg.addOrReplaceChild("LeftLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));
        LeftLowerLeg.addOrReplaceChild("LeftCalf_r1", CubeListBuilder.create().texOffs(40, 12).addBox(-2.01F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));
        PartDefinition LeftFoot = LeftLowerLeg.addOrReplaceChild("LeftFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));
        LeftFoot.addOrReplaceChild("LeftArch_r1", CubeListBuilder.create().texOffs(47, 40).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));
        LeftFoot.addOrReplaceChild("LeftPad", CubeListBuilder.create().texOffs(11, 43).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(58, 0).addBox(-1.5F, -3.0F, -7.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.01F))
                .texOffs(32, 12).addBox(-1.5F, -1.0F, -6.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));
        Head.addOrReplaceChild("LeftEar", CubeListBuilder.create(), PartPose.offset(2.5F, -8.0F, 0.0F));
        Head.addOrReplaceChild("RightEar", CubeListBuilder.create(), PartPose.offset(-2.5F, -8.0F, 0.0F));
        PartDefinition Fins = Head.addOrReplaceChild("Fins", CubeListBuilder.create(), PartPose.offset(0.0F, -8.5F, 0.0F));
        Fins.addOrReplaceChild("leftear_r1", CubeListBuilder.create().texOffs(32, 50).addBox(-6.15F, -28.9F, -23.8F, 2.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 33.5F, 0.0F, -0.7418F, 0.2182F, 0.2618F));
        Fins.addOrReplaceChild("leftear_r2", CubeListBuilder.create().texOffs(55, 7).addBox(-0.873F, -5.1926F, -1.08F, 2.0F, 6.0F, 3.0F, new CubeDeformation(0.025F)), PartPose.offsetAndRotation(3.1494F, 4.5304F, 0.0273F, -1.1916F, 0.7221F, 0.3993F));
        Fins.addOrReplaceChild("rightear_r1", CubeListBuilder.create().texOffs(56, 16).addBox(-1.127F, -5.1926F, -1.08F, 2.0F, 6.0F, 3.0F, new CubeDeformation(0.025F)), PartPose.offsetAndRotation(-3.1494F, 4.5304F, 0.0273F, -1.1916F, -0.7221F, -0.3993F));
        Fins.addOrReplaceChild("rightear_r2", CubeListBuilder.create().texOffs(52, 49).addBox(4.15F, -28.9F, -23.8F, 2.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 33.5F, 0.0F, -0.7418F, -0.2182F, -0.2618F));
        Fins.addOrReplaceChild("Head_r1", CubeListBuilder.create().texOffs(24, 43).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 3.0F, 0.0873F, 0.0F, 0.0F));
        Fins.addOrReplaceChild("Head_r2", CubeListBuilder.create().texOffs(20, 16).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 3.0F, 0.2618F, 0.0F, 0.0F));
        Fins.addOrReplaceChild("Head_r3", CubeListBuilder.create().texOffs(56, 25).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, -1.0F, 0.6545F, 0.0F, 0.0F));
        Fins.addOrReplaceChild("Head_r4", CubeListBuilder.create().texOffs(59, 46).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 6.5F, -3.5F, 1.0472F, 0.0F, -2.0071F));
        Fins.addOrReplaceChild("Head_r5", CubeListBuilder.create().texOffs(49, 59).addBox(0.0F, 0.0F, -1.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 6.5F, -3.5F, 1.0472F, 0.0F, 2.0071F));
        Fins.addOrReplaceChild("Head_r6", CubeListBuilder.create().texOffs(0, 57).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, -3.5F, 1.0472F, 0.0F, 0.0F));

        PartDefinition Torso = partdefinition.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));
        PartDefinition RightMembrane = Torso.addOrReplaceChild("RightMembrane", CubeListBuilder.create(), PartPose.offset(-1.5F, 5.5F, 0.75F));
        PartDefinition RightJoint = RightMembrane.addOrReplaceChild("RightJoint", CubeListBuilder.create(), PartPose.offset(-2.5F, 0.0F, 0.0F));
        RightJoint.addOrReplaceChild("RightArm_r1", CubeListBuilder.create().texOffs(22, 50).addBox(-4.0F, 1.0F, 0.0F, 4.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, 0.0F, 0.0F, -0.3927F));
        PartDefinition LeftMembrane = Torso.addOrReplaceChild("LeftMembrane", CubeListBuilder.create(), PartPose.offset(1.5F, 5.5F, 0.75F));
        PartDefinition LeftJoint = LeftMembrane.addOrReplaceChild("LeftJoint", CubeListBuilder.create(), PartPose.offset(2.5F, 0.0F, 0.0F));
        LeftJoint.addOrReplaceChild("LeftArm_r1", CubeListBuilder.create().texOffs(48, 0).addBox(0.0F, 1.0F, 0.0F, 4.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, 0.0F, 0.0F, 0.3927F));
        PartDefinition Tail = Torso.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0F, 10.5F, 0.0F));
        PartDefinition TailPrimary = Tail.addOrReplaceChild("TailPrimary", CubeListBuilder.create(), PartPose.offset(0.0F, -0.5F, 0.75F));
        TailPrimary.addOrReplaceChild("Base_r1", CubeListBuilder.create().texOffs(24, 0).addBox(-2.0F, -1.075F, 0.375F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.875F, 0.85F, 1.9199F, 0.0F, 0.0F));
        TailPrimary.addOrReplaceChild("Base_r2", CubeListBuilder.create().texOffs(32, 0).addBox(-2.0F, 0.75F, -0.8F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -1.0F, 1.1781F, 0.0F, 0.0F));
        PartDefinition TailSecondary = TailPrimary.addOrReplaceChild("TailSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 3.25F, 7.25F));
        TailSecondary.addOrReplaceChild("Base_r3", CubeListBuilder.create().texOffs(48, 32).addBox(-1.5F, -1.3563F, -0.6088F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.25F, 1.0F, 1.309F, 0.0F, 0.0F));
        PartDefinition TailTertiary = TailSecondary.addOrReplaceChild("TailTertiary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.5F, 4.5F));
        TailTertiary.addOrReplaceChild("Base_r4", CubeListBuilder.create().texOffs(57, 59).addBox(-0.4617F, -6.7695F, 0.1805F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(44, 49).addBox(-0.4617F, -8.7695F, -2.8195F, 1.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.425F, 4.25F, -0.4684F, -0.008F, -1.5605F));
        TailTertiary.addOrReplaceChild("Base_r5", CubeListBuilder.create().texOffs(22, 60).addBox(-0.5117F, -6.7695F, 0.1805F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(14, 50).addBox(-0.5117F, -8.7695F, -2.8195F, 1.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.425F, 4.25F, -0.4684F, -0.008F, 1.5811F));
        TailTertiary.addOrReplaceChild("Base_r6", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -0.3449F, -0.7203F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.25F, 0.25F, 1.4835F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(24, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 1.5F, 0.0F));
        partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(0, 32).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 1.5F, 0.0F));

        return LayerDefinition.create(meshdefinition, 96, 96);
    }

    @Override
    public ModelPart getArm(HumanoidArm arm) { return arm == HumanoidArm.LEFT ? LeftArm : RightArm; }
    @Override
    public ModelPart getLeg(HumanoidArm arm) { return arm == HumanoidArm.LEFT ? LeftLeg : RightLeg; }
    @Override
    public ModelPart getHead() { return Head; }
    @Override
    public ModelPart getTorso() { return Torso; }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        RightLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        LeftLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        Head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        Torso.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        RightArm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        LeftArm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public HumanoidAnimator<T, LatexNetherMantaRayMaleModel<T>> getAnimator(T entity) { return animator; }
}
