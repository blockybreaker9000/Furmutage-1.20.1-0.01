package net.jerika.furmutage.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jerika.furmutage.entity.custom.LatexHumanFleshEntity;
import net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;

import java.util.List;

public class LatexHumanFleshModel<T extends LatexHumanFleshEntity> extends AdvancedHumanoidModel<T> {
    private final HumanoidAnimator<T, LatexHumanFleshModel<T>> animator;
    private final ModelPart RightLeg;
    private final ModelPart LeftLeg;
    private final ModelPart Head;
    private final ModelPart Torso;
    private final ModelPart RightArm;
    private final ModelPart LeftArm;

    public LatexHumanFleshModel(ModelPart root) {
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
        RightLeg.addOrReplaceChild("RightThigh_r1", CubeListBuilder.create().texOffs(40, 47).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));
        PartDefinition RightLowerLeg = RightLeg.addOrReplaceChild("RightLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));
        RightLowerLeg.addOrReplaceChild("RightCalf_r1", CubeListBuilder.create().texOffs(52, 8).addBox(-1.99F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));
        PartDefinition RightFoot = RightLowerLeg.addOrReplaceChild("RightFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));
        RightFoot.addOrReplaceChild("RightArch_r1", CubeListBuilder.create().texOffs(54, 35).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));
        RightFoot.addOrReplaceChild("RightPad", CubeListBuilder.create().texOffs(54, 0).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

        PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(2.5F, 10.5F, 0.0F));
        LeftLeg.addOrReplaceChild("LeftThigh_r1", CubeListBuilder.create().texOffs(0, 51).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));
        PartDefinition LeftLowerLeg = LeftLeg.addOrReplaceChild("LeftLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));
        LeftLowerLeg.addOrReplaceChild("LeftCalf_r1", CubeListBuilder.create().texOffs(52, 18).addBox(-2.01F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));
        PartDefinition LeftFoot = LeftLowerLeg.addOrReplaceChild("LeftFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));
        LeftFoot.addOrReplaceChild("LeftArch_r1", CubeListBuilder.create().texOffs(56, 44).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));
        LeftFoot.addOrReplaceChild("LeftPad", CubeListBuilder.create().texOffs(54, 28).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -2.0F, 8.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(24, 23).addBox(-4.0F, -4.07F, -6.55F, 8.0F, 3.0F, 5.0F, new CubeDeformation(-0.005F)), PartPose.offset(0.0F, -0.5F, 0.0F));
        Head.addOrReplaceChild("Head_r1", CubeListBuilder.create().texOffs(24, 14).addBox(-4.0F, 0.01F, 0.0F, 8.0F, 3.0F, 6.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, -4.075F, -6.55F, 0.7121F, 0.0F, 0.0F));
        Head.addOrReplaceChild("Head_r2", CubeListBuilder.create().texOffs(28, 0).addBox(-4.0F, -3.005F, 0.055F, 8.0F, 3.0F, 5.0F, new CubeDeformation(-0.002F)), PartPose.offsetAndRotation(0.0F, -1.075F, -6.6F, -0.2155F, 0.0F, 0.0F));
        Head.addOrReplaceChild("RightEar", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.7F, -3.5F, -1.0F, -0.5672F, 0.0F, -0.2618F)).addOrReplaceChild("RightEarPivot", CubeListBuilder.create().texOffs(56, 53).addBox(-2.9F, -8.0F, -1.0F, 4.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(28, 8).addBox(-1.9F, -10.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.8F, 0.0F, 0.0F, 0.0F, 1.2217F, -0.0873F));
        Head.addOrReplaceChild("LeftEar", CubeListBuilder.create(), PartPose.offsetAndRotation(2.7F, -3.5F, -1.0F, -0.5672F, 0.0F, 0.2618F)).addOrReplaceChild("LeftEarPivot", CubeListBuilder.create().texOffs(16, 57).addBox(-1.1F, -8.0F, -1.0F, 4.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(38, 8).addBox(-1.1F, -10.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.8F, 0.0F, 0.0F, 0.0F, -1.2217F, 0.0873F));

        PartDefinition Torso = partdefinition.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(0, 14).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));
        PartDefinition Tail = Torso.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0F, 11.0F, 0.0F));
        PartDefinition TailPrimary = Tail.addOrReplaceChild("TailPrimary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        TailPrimary.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 30).addBox(-2.0F, -0.1F, -0.025F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.4F)), PartPose.offsetAndRotation(0.0F, -2.8F, 1.7F, -0.4363F, 0.0F, 0.0F));
        PartDefinition TailSecondary = TailPrimary.addOrReplaceChild("TailSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0F, 6.5F));
        TailSecondary.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(20, 47).addBox(-2.0F, -2.175F, -4.525F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 1.075F, 4.825F, -0.2182F, 0.0F, 0.0F));
        PartDefinition TailTertiary = TailSecondary.addOrReplaceChild("TailTertiary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.5F, 5.5F));
        TailTertiary.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 41).addBox(-2.0F, -4.375F, -6.3F, 4.0F, 4.0F, 6.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, 1.875F, 6.5F, 0.0436F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(38, 31).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 1.5F, 0.0F));
        partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(22, 31).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 1.5F, 0.0F));

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
    public HumanoidAnimator<T, LatexHumanFleshModel<T>> getAnimator(T entity) { return animator; }
}
