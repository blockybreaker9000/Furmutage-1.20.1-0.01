package net.jerika.furmutage.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jerika.furmutage.entity.custom.TwoHeadedWhiteLatexMutantUnifiedEntity;
import net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;

import java.util.List;

public class TwoHeadedWhiteLatexMutantUnifiedModel<T extends TwoHeadedWhiteLatexMutantUnifiedEntity> extends AdvancedHumanoidModel<T> {
    private final HumanoidAnimator<T, TwoHeadedWhiteLatexMutantUnifiedModel<T>> animator;
    private final ModelPart RightLeg;
    private final ModelPart LeftLeg;
    private final ModelPart Head;
    private final ModelPart Head2;
    private final ModelPart Torso;
    private final ModelPart RightArm;
    private final ModelPart LeftArm;

    public TwoHeadedWhiteLatexMutantUnifiedModel(ModelPart root) {
        super(root);
        this.RightLeg = root.getChild("RightLeg");
        this.LeftLeg = root.getChild("LeftLeg");
        this.Head = root.getChild("Head");
        this.Head2 = root.getChild("Head2");
        this.Torso = root.getChild("Torso");
        this.RightArm = root.getChild("RightArm");
        this.LeftArm = root.getChild("LeftArm");

        var tail = this.Torso.getChild("Tail");
        var tailPrimary = tail.getChild("TailPrimary");
        var tailSecondary = tailPrimary.getChild("TailSecondary");
        var tailTertiary = tailSecondary.getChild("TailTertiary");
        var tailQuaternary = tailTertiary.getChild("TailQuaternary");
        List<ModelPart> tailJoints = List.of(tailPrimary, tailSecondary, tailTertiary, tailQuaternary);

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
        RightLeg.addOrReplaceChild("RightThigh_r1", CubeListBuilder.create().texOffs(32, 44).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));
        PartDefinition RightLowerLeg = RightLeg.addOrReplaceChild("RightLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));
        RightLowerLeg.addOrReplaceChild("RightCalf_r1", CubeListBuilder.create().texOffs(48, 47).addBox(-1.99F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));
        PartDefinition RightFoot = RightLowerLeg.addOrReplaceChild("RightFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));
        RightFoot.addOrReplaceChild("RightArch_r1", CubeListBuilder.create().texOffs(0, 59).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));
        RightFoot.addOrReplaceChild("RightPad", CubeListBuilder.create().texOffs(48, 9).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

        PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(2.5F, 10.5F, 0.0F));
        LeftLeg.addOrReplaceChild("LeftThigh_r1", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));
        PartDefinition LeftLowerLeg = LeftLeg.addOrReplaceChild("LeftLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));
        LeftLowerLeg.addOrReplaceChild("LeftCalf_r1", CubeListBuilder.create().texOffs(48, 22).addBox(-2.01F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));
        PartDefinition LeftFoot = LeftLowerLeg.addOrReplaceChild("LeftFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));
        LeftFoot.addOrReplaceChild("LeftArch_r1", CubeListBuilder.create().texOffs(14, 61).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));
        LeftFoot.addOrReplaceChild("LeftPad", CubeListBuilder.create().texOffs(24, 0).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 1.5F, 0.0F, 0.0F, 0.0F, -0.3491F));
        Head.addOrReplaceChild("Base_r1", CubeListBuilder.create().texOffs(56, 65).addBox(-1.9F, 0.15F, -0.3F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, -5.0F, 0.0F, -1.2217F, 0.4712F, 0.6981F));
        Head.addOrReplaceChild("Base_r2", CubeListBuilder.create().texOffs(0, 16).addBox(-1.4F, 0.25F, 2.3F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, -5.0F, 0.0F, -1.4312F, 0.6458F, 0.5847F));
        Head.addOrReplaceChild("Base_r3", CubeListBuilder.create().texOffs(64, 65).addBox(-0.1F, 0.15F, -0.3F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5F, -5.0F, 0.0F, -1.2217F, -0.4712F, -0.6981F));
        Head.addOrReplaceChild("Base_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6F, 0.25F, 2.3F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5F, -5.0F, 0.0F, -1.4312F, -0.6458F, -0.5847F));
        PartDefinition TopHorn = Head.addOrReplaceChild("TopHorn", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -4.0F, 2.3F, 0.6109F, 0.0F, 0.0F));
        TopHorn.addOrReplaceChild("TopHorn_r1", CubeListBuilder.create().texOffs(48, 62).addBox(-1.0F, -1.0F, -2.2F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.15F)), PartPose.offsetAndRotation(0.0F, -4.5F, 0.5F, 0.0F, 0.0F, 0.7854F));
        PartDefinition RightEar = Head.addOrReplaceChild("RightEar", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.5F, -8.0F, 0.0F, 0.0F, 0.0F, -0.2618F));
        RightEar.addOrReplaceChild("Base_r5", CubeListBuilder.create().texOffs(24, 0).addBox(-0.6F, -1.5F, -3.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, -0.2094F, -0.056F, 0.0059F));
        RightEar.addOrReplaceChild("Base_r6", CubeListBuilder.create().texOffs(38, 62).addBox(-2.0F, -4.75F, -1.0F, 2.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, -0.2182F, 0.0785F));
        PartDefinition LeftEar = Head.addOrReplaceChild("LeftEar", CubeListBuilder.create(), PartPose.offsetAndRotation(2.5F, -8.0F, 0.0F, 0.0F, 0.0F, 0.2618F));
        LeftEar.addOrReplaceChild("Base_r7", CubeListBuilder.create().texOffs(0, 32).addBox(-0.4F, -1.5F, -3.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.0F, 0.0F, -0.2094F, 0.056F, -0.0059F));
        LeftEar.addOrReplaceChild("Base_r8", CubeListBuilder.create().texOffs(28, 61).addBox(0.0F, -4.75F, -1.0F, 2.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.2182F, -0.0785F));

        PartDefinition Head2 = partdefinition.addOrReplaceChild("Head2", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.4363F));
        Head2.addOrReplaceChild("Base_r9", CubeListBuilder.create().texOffs(56, 65).addBox(-1.9F, 0.15F, -0.3F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, -5.0F, 0.0F, -1.2217F, 0.4712F, 0.6981F));
        Head2.addOrReplaceChild("Base_r10", CubeListBuilder.create().texOffs(0, 16).addBox(-1.4F, 0.25F, 2.3F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, -5.0F, 0.0F, -1.4312F, 0.6458F, 0.5847F));
        Head2.addOrReplaceChild("Base_r11", CubeListBuilder.create().texOffs(64, 65).addBox(-0.1F, 0.15F, -0.3F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5F, -5.0F, 0.0F, -1.2217F, -0.4712F, -0.6981F));
        Head2.addOrReplaceChild("Base_r12", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6F, 0.25F, 2.3F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5F, -5.0F, 0.0F, -1.4312F, -0.6458F, -0.5847F));
        PartDefinition TopHorn2 = Head2.addOrReplaceChild("TopHorn2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -4.0F, 2.3F, 0.6109F, 0.0F, 0.0F));
        TopHorn2.addOrReplaceChild("TopHorn_r2", CubeListBuilder.create().texOffs(48, 62).addBox(-1.0F, -1.0F, -2.2F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.15F)), PartPose.offsetAndRotation(0.0F, -4.5F, 0.5F, 0.0F, 0.0F, 0.7854F));
        PartDefinition RightEar2 = Head2.addOrReplaceChild("RightEar2", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.5F, -8.0F, 0.0F, 0.0F, 0.0F, -0.2618F));
        RightEar2.addOrReplaceChild("Base_r13", CubeListBuilder.create().texOffs(24, 0).addBox(-0.6F, -1.5F, -3.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, -0.2094F, -0.056F, 0.0059F));
        RightEar2.addOrReplaceChild("Base_r14", CubeListBuilder.create().texOffs(38, 62).addBox(-2.0F, -4.75F, -1.0F, 2.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, -0.2182F, 0.0785F));
        PartDefinition LeftEar2 = Head2.addOrReplaceChild("LeftEar2", CubeListBuilder.create(), PartPose.offsetAndRotation(2.5F, -8.0F, 0.0F, 0.0F, 0.0F, 0.2618F));
        LeftEar2.addOrReplaceChild("Base_r15", CubeListBuilder.create().texOffs(0, 32).addBox(-0.4F, -1.5F, -3.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.0F, 0.0F, -0.2094F, 0.056F, -0.0059F));
        LeftEar2.addOrReplaceChild("Base_r16", CubeListBuilder.create().texOffs(28, 61).addBox(0.0F, -4.75F, -1.0F, 2.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.2182F, -0.0785F));

        PartDefinition Torso = partdefinition.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(28, 28).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));
        PartDefinition Tail = Torso.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0F, 11.0F, 0.0F));
        PartDefinition TailPrimary = Tail.addOrReplaceChild("TailPrimary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        TailPrimary.addOrReplaceChild("Base_r17", CubeListBuilder.create().texOffs(56, 57).addBox(-2.0F, -2.9F, 0.4F, 4.0F, 4.0F, 4.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5236F, 0.0F, 0.0F));
        PartDefinition TailSecondary = TailPrimary.addOrReplaceChild("TailSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 3.5F));
        TailSecondary.addOrReplaceChild("Base_r18", CubeListBuilder.create().texOffs(48, 0).addBox(-1.5F, -1.4F, -2.7F, 3.0F, 3.0F, 6.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, 1.0F, 2.5F, -0.3927F, 0.0F, 0.0F));
        PartDefinition TailTertiary = TailSecondary.addOrReplaceChild("TailTertiary", CubeListBuilder.create(), PartPose.offset(0.0F, 2.5F, 5.0F));
        TailTertiary.addOrReplaceChild("Base_r19", CubeListBuilder.create().texOffs(46, 38).addBox(-1.5F, -13.225F, 6.6F, 3.0F, 3.0F, 6.0F, new CubeDeformation(-0.32F)), PartPose.offsetAndRotation(0.0F, 10.5F, -8.5F, -0.1309F, 0.0F, 0.0F));
        PartDefinition TailQuaternary = TailTertiary.addOrReplaceChild("TailQuaternary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.5F, 4.5F));
        TailQuaternary.addOrReplaceChild("Base_r20", CubeListBuilder.create().texOffs(16, 32).addBox(-1.0F, -10.45F, 13.5F, 2.0F, 2.0F, 4.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(0.0F, 10.0F, -13.0F, 0.0436F, 0.0F, 0.0F));

        PartDefinition LeftWing = Torso.addOrReplaceChild("LeftWing", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, 5.0F, 2.0F, 0.0F, -0.48F, 0.0F));
        PartDefinition leftWingRoot = LeftWing.addOrReplaceChild("leftWingRoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        leftWingRoot.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(37, 0).addBox(18.975F, -4.475F, 1.65F, 7.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 20.0F, -2.0F, 0.0F, 0.0F, -1.2654F));
        leftWingRoot.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(60, 47).addBox(19.075F, -12.7F, 1.2F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 20.0F, -2.0F, 0.0F, 0.0F, -0.7854F));
        leftWingRoot.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(64, 43).addBox(7.775F, -19.75F, 1.2F, 5.0F, 2.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-2.0F, 20.0F, -2.0F, 0.0F, 0.0F, -0.3491F));
        PartDefinition leftSecondaries = leftWingRoot.addOrReplaceChild("leftSecondaries", CubeListBuilder.create().texOffs(52, 67).addBox(-0.8F, -0.475F, -0.3F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(7.3F, -7.0F, -0.5F, 0.0F, 0.0F, -0.5236F));
        leftSecondaries.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(12, 70).addBox(-2.025F, -22.55F, 1.2F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.3F, 27.0F, -1.5F, 0.0F, 0.0F, 0.48F));
        leftSecondaries.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(60, 0).addBox(15.525F, -13.85F, 1.648F, 9.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.3F, 27.0F, -1.5F, 0.0F, 0.0F, -0.7418F));
        leftSecondaries.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(36, 57).addBox(13.4F, 10.625F, 1.651F, 9.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.3F, 27.0F, -1.5F, 0.0F, 0.0F, -1.8326F));
        PartDefinition leftTertiaries = leftSecondaries.addOrReplaceChild("leftTertiaries", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.3F, 0.0F, 0.0F, 0.0F, 0.0F, -0.9599F));
        leftTertiaries.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(48, 67).addBox(-3.3F, -22.5F, 1.2F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, 27.0F, -1.5F, 0.0F, 0.0F, 0.5236F));
        leftTertiaries.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(56, 16).addBox(16.125F, -10.525F, 1.64F, 9.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, 27.0F, -1.5F, 0.0F, 0.0F, -0.8727F));
        leftTertiaries.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(8, 68).addBox(9.15F, -26.2F, 1.2F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(-9.0F, 27.0F, -1.5F, 0.0F, 0.0F, -0.0436F));

        PartDefinition RightWing = Torso.addOrReplaceChild("RightWing", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, 5.0F, 2.0F, 0.0F, 0.48F, 0.0F));
        PartDefinition rightWingRoot = RightWing.addOrReplaceChild("rightWingRoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        rightWingRoot.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(60, 21).addBox(-25.975F, -4.475F, 1.65F, 7.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 20.0F, -2.0F, 0.0F, 0.0F, 1.2654F));
        rightWingRoot.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(32, 24).addBox(-25.075F, -12.7F, 1.2F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 20.0F, -2.0F, 0.0F, 0.0F, 0.7854F));
        rightWingRoot.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(64, 50).addBox(-12.775F, -19.75F, 1.2F, 5.0F, 2.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(2.0F, 20.0F, -2.0F, 0.0F, 0.0F, 0.3491F));
        PartDefinition rightSecondaries = rightWingRoot.addOrReplaceChild("rightSecondaries", CubeListBuilder.create().texOffs(0, 68).addBox(-0.2F, -0.475F, -0.3F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-7.3F, -7.0F, -0.5F, 0.0F, 0.0F, 0.5236F));
        rightSecondaries.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(20, 70).addBox(1.025F, -22.55F, 1.2F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.3F, 27.0F, -1.5F, 0.0F, 0.0F, -0.48F));
        rightSecondaries.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(17, 56).addBox(-22.4F, 10.625F, 1.651F, 9.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.3F, 27.0F, -1.5F, 0.0F, 0.0F, 1.8326F));
        rightSecondaries.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(58, 37).addBox(-24.525F, -13.85F, 1.648F, 9.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.3F, 27.0F, -1.5F, 0.0F, 0.0F, 0.7418F));
        PartDefinition rightTertiaries = rightSecondaries.addOrReplaceChild("rightTertiaries", CubeListBuilder.create(), PartPose.offsetAndRotation(0.3F, 0.0F, 0.0F, 0.0F, 0.0F, 0.9599F));
        rightTertiaries.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(4, 68).addBox(2.3F, -22.5F, 1.2F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, 27.0F, -1.5F, 0.0F, 0.0F, -0.5236F));
        rightTertiaries.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(16, 70).addBox(-10.15F, -26.2F, 1.2F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(9.0F, 27.0F, -1.5F, 0.0F, 0.0F, 0.0436F));
        rightTertiaries.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(52, 32).addBox(-25.125F, -10.525F, 1.64F, 9.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, 27.0F, -1.5F, 0.0F, 0.0F, 0.8727F));

        partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(0, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 1.5F, 0.0F));
        partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(16, 40).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 1.5F, 0.0F));

        return LayerDefinition.create(meshdefinition, 96, 96);
    }

    @Override
    public ModelPart getArm(HumanoidArm arm) {
        return arm == HumanoidArm.LEFT ? LeftArm : RightArm;
    }

    @Override
    public ModelPart getLeg(HumanoidArm arm) {
        return arm == HumanoidArm.LEFT ? LeftLeg : RightLeg;
    }

    @Override
    public ModelPart getHead() {
        return Head;
    }

    @Override
    public ModelPart getTorso() {
        return Torso;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        RightLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        LeftLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        Head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        Head2.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        Torso.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        RightArm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        LeftArm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public HumanoidAnimator<T, TwoHeadedWhiteLatexMutantUnifiedModel<T>> getAnimator(T entity) {
        return animator;
    }
}
