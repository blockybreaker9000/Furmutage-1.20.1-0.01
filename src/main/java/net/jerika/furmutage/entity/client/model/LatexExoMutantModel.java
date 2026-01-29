package net.jerika.furmutage.entity.client.model;

// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Updated to use Changed mod's HumanoidAnimator system

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jerika.furmutage.entity.custom.LatexExoMutantEntity;
import net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LatexExoMutantModel<T extends LatexExoMutantEntity> extends AdvancedHumanoidModel<T> {
	private final HumanoidAnimator<T, LatexExoMutantModel<T>> animator;
	private final ModelPart LeftArm;
	private final ModelPart LeftBraceLeft;
	private final ModelPart LeftBraceRight;
	private final ModelPart LeftArm2;
	private final ModelPart RightArm;
	private final ModelPart RightBraceLeft;
	private final ModelPart RightBraceRight;
	private final ModelPart RightArm2;
	private final ModelPart Torso;
	private final ModelPart Back;
	private final ModelPart Left;
	private final ModelPart Right;
	private final ModelPart HeadSupport;
	private final ModelPart Torso2;
	private final ModelPart Tail;
	private final ModelPart TailPrimary;
	private final ModelPart TailSecondary;
	private final ModelPart TailTertiary;
	private final ModelPart Head;
	private final ModelPart Visor;
	private final ModelPart RightEar;
	private final ModelPart RightEarPivot;
	private final ModelPart LeftEar;
	private final ModelPart LeftEarPivot;
	private final ModelPart Head2;
	private final ModelPart RightEar2;
	private final ModelPart RightEarPivot2;
	private final ModelPart LeftEar2;
	private final ModelPart LeftEarPivot2;
	private final ModelPart LeftLeg;
	private final ModelPart LeftLowerLeg;
	private final ModelPart LeftFoot;
	private final ModelPart LeftPad;
	private final ModelPart LeftLeg2;
	private final ModelPart LeftLowerLeg2;
	private final ModelPart LeftFoot2;
	private final ModelPart LeftPad2;
	private final ModelPart LeftLegReplacement;
	private final ModelPart LeftPants;
	private final ModelPart LeftLegReplacementLower;
	private final ModelPart RightLeg;
	private final ModelPart RightLowerLeg;
	private final ModelPart RightFoot;
	private final ModelPart RightPad;
	private final ModelPart RightLeg2;
	private final ModelPart RightLowerLeg2;
	private final ModelPart RightFoot2;
	private final ModelPart RightPad2;
	private final ModelPart RightLegReplacement;
	private final ModelPart RightPants;
	private final ModelPart RightLegReplacementLower2;

	public LatexExoMutantModel(ModelPart root) {
		super(root);
		this.LeftArm = root.getChild("LeftArm");
		this.LeftBraceLeft = this.LeftArm.getChild("LeftBraceLeft");
		this.LeftBraceRight = this.LeftArm.getChild("LeftBraceRight");
		this.LeftArm2 = this.LeftArm.getChild("LeftArm2");
		this.RightArm = root.getChild("RightArm");
		this.RightBraceLeft = this.RightArm.getChild("RightBraceLeft");
		this.RightBraceRight = this.RightArm.getChild("RightBraceRight");
		this.RightArm2 = this.RightArm.getChild("RightArm2");
		this.Torso = root.getChild("Torso");
		this.Back = this.Torso.getChild("Back");
		this.Left = this.Back.getChild("Left");
		this.Right = this.Back.getChild("Right");
		this.HeadSupport = this.Torso.getChild("HeadSupport");
		this.Torso2 = this.Torso.getChild("Torso2");
		this.Tail = this.Torso2.getChild("Tail");
		this.TailPrimary = this.Tail.getChild("TailPrimary");
		this.TailSecondary = this.TailPrimary.getChild("TailSecondary");
		this.TailTertiary = this.TailSecondary.getChild("TailTertiary");
		this.Head = root.getChild("Head");
		this.Visor = this.Head.getChild("Visor");
		this.RightEar = this.Head.getChild("RightEar");
		this.RightEarPivot = this.RightEar.getChild("RightEarPivot");
		this.LeftEar = this.Head.getChild("LeftEar");
		this.LeftEarPivot = this.LeftEar.getChild("LeftEarPivot");
		this.Head2 = this.Head.getChild("Head2");
		this.RightEar2 = this.Head2.getChild("RightEar2");
		this.RightEarPivot2 = this.RightEar2.getChild("RightEarPivot2");
		this.LeftEar2 = this.Head2.getChild("LeftEar2");
		this.LeftEarPivot2 = this.LeftEar2.getChild("LeftEarPivot2");
		this.LeftLeg = root.getChild("LeftLeg");
		this.LeftLowerLeg = this.LeftLeg.getChild("LeftLowerLeg");
		this.LeftFoot = this.LeftLowerLeg.getChild("LeftFoot");
		this.LeftPad = this.LeftFoot.getChild("LeftPad");
		this.LeftLeg2 = this.LeftLeg.getChild("LeftLeg2");
		this.LeftLowerLeg2 = this.LeftLeg2.getChild("LeftLowerLeg2");
		this.LeftFoot2 = this.LeftLowerLeg2.getChild("LeftFoot2");
		this.LeftPad2 = this.LeftFoot2.getChild("LeftPad2");
		this.LeftLegReplacement = this.LeftLeg.getChild("LeftLegReplacement");
		this.LeftPants = this.LeftLegReplacement.getChild("LeftPants");
		this.LeftLegReplacementLower = this.LeftLegReplacement.getChild("LeftLegReplacementLower");
		this.RightLeg = root.getChild("RightLeg");
		this.RightLowerLeg = this.RightLeg.getChild("RightLowerLeg");
		this.RightFoot = this.RightLowerLeg.getChild("RightFoot");
		this.RightPad = this.RightFoot.getChild("RightPad");
		this.RightLeg2 = this.RightLeg.getChild("RightLeg2");
		this.RightLowerLeg2 = this.RightLeg2.getChild("RightLowerLeg2");
		this.RightFoot2 = this.RightLowerLeg2.getChild("RightFoot2");
		this.RightPad2 = this.RightFoot2.getChild("RightPad2");
		this.RightLegReplacement = this.RightLeg.getChild("RightLegReplacement");
		this.RightPants = this.RightLegReplacement.getChild("RightPants");
		this.RightLegReplacementLower2 = this.RightLegReplacement.getChild("RightLegReplacementLower2");

		// Setup HumanoidAnimator with wolfLike preset
		var tailPrimary = this.Tail.getChild("TailPrimary");
		var tailSecondary = tailPrimary.getChild("TailSecondary");
		var tailTertiary = tailSecondary.getChild("TailTertiary");
		List<ModelPart> tailJoints = List.of(tailPrimary, tailSecondary, tailTertiary);

		var leftLowerLeg = this.LeftLeg.getChild("LeftLowerLeg");
		var leftFoot = leftLowerLeg.getChild("LeftFoot");
		var rightLowerLeg = this.RightLeg.getChild("RightLowerLeg");
		var rightFoot = rightLowerLeg.getChild("RightFoot");

		animator = HumanoidAnimator.of(this).hipOffset(-1.5f)
				.addPreset(AnimatorPresets.wolfLike(
						this.Head, this.Head.getChild("LeftEar"), this.Head.getChild("RightEar"),
						this.Torso, this.LeftArm, this.RightArm,
						this.Tail, tailJoints,
						this.LeftLeg, leftLowerLeg, leftFoot, leftFoot.getChild("LeftPad"),
						this.RightLeg, rightLowerLeg, rightFoot, rightFoot.getChild("RightPad")));
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(0, 59).addBox(-1.0F, 6.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.3F))
		.texOffs(40, 27).addBox(-2.0F, 2.0F, 2.0F, 6.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(82, 80).addBox(-1.0F, -1.0F, 3.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 0.5F, 0.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition LeftBraceLeft = LeftArm.addOrReplaceChild("LeftBraceLeft", CubeListBuilder.create().texOffs(80, 49).addBox(-2.5F, -2.0F, -5.5F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 73).addBox(-0.5F, -2.0F, -4.5F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 4.0F, 2.5F));

		PartDefinition LeftBraceRight = LeftArm.addOrReplaceChild("LeftBraceRight", CubeListBuilder.create().texOffs(52, 80).addBox(-0.5F, -2.0F, -5.5F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(74, 8).addBox(-0.5F, -2.0F, -4.5F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 4.0F, 2.5F));

		PartDefinition LeftArm2 = LeftArm.addOrReplaceChild("LeftArm2", CubeListBuilder.create().texOffs(24, 16).addBox(-1.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition GooStrand_r1 = LeftArm2.addOrReplaceChild("GooStrand_r1", CubeListBuilder.create().texOffs(24, 69).addBox(-3.75F, 1.4F, -1.25F, 6.0F, 2.0F, 2.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(-1.0F, -0.5F, 0.0F, 0.0F, 0.2618F, 0.3927F));

		PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(16, 60).addBox(-3.0F, 6.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.3F))
		.texOffs(70, 38).addBox(-4.0F, 2.0F, 2.0F, 6.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 83).addBox(-1.0F, -1.0F, 3.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition RightBraceLeft = RightArm.addOrReplaceChild("RightBraceLeft", CubeListBuilder.create().texOffs(80, 54).addBox(-2.5F, -2.0F, -5.5F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 75).addBox(-0.5F, -2.0F, -4.5F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 4.0F, 2.5F));

		PartDefinition RightBraceRight = RightArm.addOrReplaceChild("RightBraceRight", CubeListBuilder.create().texOffs(74, 80).addBox(-0.5F, -2.0F, -5.5F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(10, 76).addBox(-0.5F, -2.0F, -4.5F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 4.0F, 2.5F));

		PartDefinition RightArm2 = RightArm.addOrReplaceChild("RightArm2", CubeListBuilder.create().texOffs(0, 32).addBox(-3.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition GooStrand_r2 = RightArm2.addOrReplaceChild("GooStrand_r2", CubeListBuilder.create().texOffs(70, 34).addBox(-2.25F, 1.4F, -1.25F, 6.0F, 2.0F, 2.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(1.0F, -0.5F, 0.0F, 0.0F, -0.2618F, -0.3927F));

		PartDefinition Torso = partdefinition.addOrReplaceChild("Torso", CubeListBuilder.create(), PartPose.offset(0.0F, -1.5F, 0.0F));

		PartDefinition Back = Torso.addOrReplaceChild("Back", CubeListBuilder.create().texOffs(16, 32).addBox(-7.0F, -22.0F, 2.0F, 10.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(80, 6).addBox(-4.0F, -17.5F, 3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(30, 83).addBox(-4.0F, -19.5F, 3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(6, 84).addBox(-4.0F, -21.5F, 3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(34, 73).addBox(-3.0F, -25.0F, 4.0F, 2.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(72, 31).addBox(-1.0F, -25.0F, 4.0F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(52, 77).addBox(-8.0F, -25.0F, 4.0F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(86, 34).addBox(-7.0F, -15.0F, 1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(64, 52).addBox(-7.0F, -16.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(56, 19).addBox(2.0F, -15.5F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(86, 59).addBox(2.0F, -15.0F, 1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(20, 76).addBox(2.0F, -16.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(84, 30).addBox(-7.0F, -15.5F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 26.0F, 0.0F));

		PartDefinition Left = Back.addOrReplaceChild("Left", CubeListBuilder.create().texOffs(40, 70).addBox(-0.5F, -2.5F, -5.5F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(62, 24).addBox(-4.5F, -2.5F, -5.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(52, 70).addBox(-0.5F, 1.5F, -5.5F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(78, 21).addBox(-4.5F, 1.5F, -5.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, -19.5F, 2.5F));

		PartDefinition Right = Back.addOrReplaceChild("Right", CubeListBuilder.create().texOffs(64, 70).addBox(-0.5F, -2.5F, -5.5F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(72, 24).addBox(-0.5F, 1.5F, -5.5F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(20, 81).addBox(0.5F, 1.5F, -5.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(40, 81).addBox(0.5F, -2.5F, -5.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.5F, -19.5F, 2.5F));

		PartDefinition HeadSupport = Torso.addOrReplaceChild("HeadSupport", CubeListBuilder.create().texOffs(60, 80).addBox(0.0F, -6.25F, 0.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 1.0F, 4.25F));

		PartDefinition Torso2 = Torso.addOrReplaceChild("Torso2", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition Tail = Torso2.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0F, 10.5F, 0.0F));

		PartDefinition TailPrimary = Tail.addOrReplaceChild("TailPrimary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition Base_r1 = TailPrimary.addOrReplaceChild("Base_r1", CubeListBuilder.create().texOffs(54, 34).addBox(-2.0F, 0.75F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1781F, 0.0F, 0.0F));

		PartDefinition TailSecondary = TailPrimary.addOrReplaceChild("TailSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.25F, 4.5F));

		PartDefinition Base_r2 = TailSecondary.addOrReplaceChild("Base_r2", CubeListBuilder.create().texOffs(32, 0).addBox(-2.5F, -0.45F, -2.1F, 5.0F, 7.0F, 5.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 1.4835F, 0.0F, 0.0F));

		PartDefinition TailTertiary = TailSecondary.addOrReplaceChild("TailTertiary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.75F, 2.5F));

		PartDefinition Base_r3 = TailTertiary.addOrReplaceChild("Base_r3", CubeListBuilder.create().texOffs(32, 61).addBox(-2.0F, -1.2F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.0F, 1.8326F, 0.0F, 0.0F));

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(76, 76).addBox(-2.0F, -3.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -1.5F, 0.0F));

		PartDefinition Visor = Head.addOrReplaceChild("Visor", CubeListBuilder.create().texOffs(40, 23).addBox(-5.0F, -6.0F, -4.0F, 10.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 84).addBox(4.0F, -6.0F, -3.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(22, 84).addBox(-5.0F, -6.0F, -3.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(76, 60).addBox(-5.0F, -6.5F, -1.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(84, 38).addBox(-5.0F, -8.5F, 0.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(40, 84).addBox(4.0F, -8.5F, 0.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(84, 24).addBox(-4.0F, -8.5F, 0.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(84, 27).addBox(2.0F, -8.5F, 0.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(80, 0).addBox(-5.5F, -6.0F, -0.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(80, 43).addBox(4.5F, -6.0F, -0.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(76, 68).addBox(4.0F, -6.5F, -1.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(56, 17).addBox(-5.0F, -5.0F, 5.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(86, 62).addBox(-5.0F, -5.0F, 3.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(64, 86).addBox(4.0F, -5.0F, 3.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -1.0F));

		PartDefinition RightEar = Head.addOrReplaceChild("RightEar", CubeListBuilder.create(), PartPose.offset(-3.0F, -6.75F, 0.0F));

		PartDefinition RightEarPivot = RightEar.addOrReplaceChild("RightEarPivot", CubeListBuilder.create().texOffs(84, 8).addBox(-1.9F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(46, 84).addBox(-0.9F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
		.texOffs(86, 65).addBox(-0.9F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(20, 78).addBox(0.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

		PartDefinition LeftEar = Head.addOrReplaceChild("LeftEar", CubeListBuilder.create(), PartPose.offset(3.0F, -6.75F, 0.0F));

		PartDefinition LeftEarPivot = LeftEar.addOrReplaceChild("LeftEarPivot", CubeListBuilder.create().texOffs(84, 12).addBox(-1.1F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(28, 85).addBox(-1.1F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
		.texOffs(86, 67).addBox(-1.1F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(30, 81).addBox(-1.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

		PartDefinition Head2 = Head.addOrReplaceChild("Head2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(64, 48).addBox(-3.5F, -6.25F, -4.7F, 7.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(40, 77).addBox(-2.0F, -4.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(6, 86).addBox(-1.5F, -2.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition RightEar2 = Head2.addOrReplaceChild("RightEar2", CubeListBuilder.create(), PartPose.offset(-3.0F, -7.5F, 0.0F));

		PartDefinition RightEarPivot2 = RightEar2.addOrReplaceChild("RightEarPivot2", CubeListBuilder.create().texOffs(64, 82).addBox(-1.9F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(32, 39).addBox(-0.9F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
		.texOffs(68, 8).addBox(-0.9F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(52, 10).addBox(0.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

		PartDefinition LeftEar2 = Head2.addOrReplaceChild("LeftEar2", CubeListBuilder.create(), PartPose.offset(3.0F, -7.5F, 0.0F));

		PartDefinition LeftEarPivot2 = LeftEar2.addOrReplaceChild("LeftEarPivot2", CubeListBuilder.create().texOffs(32, 12).addBox(-1.1F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(72, 85).addBox(-1.1F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
		.texOffs(86, 69).addBox(-1.1F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(56, 85).addBox(-1.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

		PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(34, 85).addBox(1.5F, 1.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 9.5F, 0.0F));

		PartDefinition LeftThigh_r1 = LeftLeg.addOrReplaceChild("LeftThigh_r1", CubeListBuilder.create().texOffs(62, 19).addBox(-2.0F, 6.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition LeftLowerLeg = LeftLeg.addOrReplaceChild("LeftLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

		PartDefinition LeftCalf_r1 = LeftLowerLeg.addOrReplaceChild("LeftCalf_r1", CubeListBuilder.create().texOffs(0, 67).addBox(-2.01F, -0.125F, -2.9F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

		PartDefinition LeftFoot = LeftLowerLeg.addOrReplaceChild("LeftFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

		PartDefinition LeftArch_r1 = LeftFoot.addOrReplaceChild("LeftArch_r1", CubeListBuilder.create().texOffs(64, 77).addBox(-2.0F, -6.725F, 0.8F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.13F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.7418F, 0.0F, 0.0F));

		PartDefinition LeftArch_r2 = LeftFoot.addOrReplaceChild("LeftArch_r2", CubeListBuilder.create().texOffs(68, 0).addBox(-2.0F, -7.45F, -0.725F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.13F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

		PartDefinition LeftPad = LeftFoot.addOrReplaceChild("LeftPad", CubeListBuilder.create().texOffs(54, 27).addBox(-2.0F, 1.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.15F)), PartPose.offset(0.0F, 4.325F, -4.425F));

		PartDefinition LeftLeg2 = LeftLeg.addOrReplaceChild("LeftLeg2", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition LeftThigh_r2 = LeftLeg2.addOrReplaceChild("LeftThigh_r2", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition LeftLowerLeg2 = LeftLeg2.addOrReplaceChild("LeftLowerLeg2", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

		PartDefinition LeftCalf_r2 = LeftLowerLeg2.addOrReplaceChild("LeftCalf_r2", CubeListBuilder.create().texOffs(16, 50).addBox(-2.01F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

		PartDefinition LeftFoot2 = LeftLowerLeg2.addOrReplaceChild("LeftFoot2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

		PartDefinition LeftArch_r3 = LeftFoot2.addOrReplaceChild("LeftArch_r3", CubeListBuilder.create().texOffs(48, 61).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

		PartDefinition LeftPad2 = LeftFoot2.addOrReplaceChild("LeftPad2", CubeListBuilder.create().texOffs(50, 54).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

		PartDefinition LeftLegReplacement = LeftLeg.addOrReplaceChild("LeftLegReplacement", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LeftThigh_r3 = LeftLegReplacement.addOrReplaceChild("LeftThigh_r3", CubeListBuilder.create().texOffs(38, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition LeftPants = LeftLegReplacement.addOrReplaceChild("LeftPants", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LeftThighLayer_r1 = LeftPants.addOrReplaceChild("LeftThighLayer_r1", CubeListBuilder.create().texOffs(16, 39).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition LeftLegReplacementLower = LeftLegReplacement.addOrReplaceChild("LeftLegReplacementLower", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(52, 85).addBox(-2.5F, 1.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 9.5F, 0.0F));

		PartDefinition RightThigh_r1 = RightLeg.addOrReplaceChild("RightThigh_r1", CubeListBuilder.create().texOffs(64, 43).addBox(-2.0F, 6.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition RightLowerLeg = RightLeg.addOrReplaceChild("RightLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

		PartDefinition RightCalf_r1 = RightLowerLeg.addOrReplaceChild("RightCalf_r1", CubeListBuilder.create().texOffs(12, 68).addBox(-2.99F, -0.125F, -2.9F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

		PartDefinition RightFoot = RightLowerLeg.addOrReplaceChild("RightFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

		PartDefinition RightArch_r1 = RightFoot.addOrReplaceChild("RightArch_r1", CubeListBuilder.create().texOffs(78, 16).addBox(-7.0F, -6.725F, 0.8F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.13F)), PartPose.offsetAndRotation(5.0F, 7.075F, -4.975F, -0.7418F, 0.0F, 0.0F));

		PartDefinition RightArch_r2 = RightFoot.addOrReplaceChild("RightArch_r2", CubeListBuilder.create().texOffs(68, 52).addBox(-3.0F, -7.45F, -0.725F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.13F)), PartPose.offsetAndRotation(1.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

		PartDefinition RightPad = RightFoot.addOrReplaceChild("RightPad", CubeListBuilder.create().texOffs(32, 54).addBox(-2.0F, 1.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.15F)), PartPose.offset(0.0F, 4.325F, -4.425F));

		PartDefinition RightLeg2 = RightLeg.addOrReplaceChild("RightLeg2", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition RightThigh_r2 = RightLeg2.addOrReplaceChild("RightThigh_r2", CubeListBuilder.create().texOffs(48, 43).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition RightLowerLeg2 = RightLeg2.addOrReplaceChild("RightLowerLeg2", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

		PartDefinition RightCalf_r2 = RightLowerLeg2.addOrReplaceChild("RightCalf_r2", CubeListBuilder.create().texOffs(52, 0).addBox(-1.99F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

		PartDefinition RightFoot2 = RightLowerLeg2.addOrReplaceChild("RightFoot2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

		PartDefinition RightArch_r3 = RightFoot2.addOrReplaceChild("RightArch_r3", CubeListBuilder.create().texOffs(62, 61).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

		PartDefinition RightPad2 = RightFoot2.addOrReplaceChild("RightPad2", CubeListBuilder.create().texOffs(56, 10).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

		PartDefinition RightLegReplacement = RightLeg.addOrReplaceChild("RightLegReplacement", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition RightThigh_r3 = RightLegReplacement.addOrReplaceChild("RightThigh_r3", CubeListBuilder.create().texOffs(40, 12).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition RightPants = RightLegReplacement.addOrReplaceChild("RightPants", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition RightThighLayer_r1 = RightPants.addOrReplaceChild("RightThighLayer_r1", CubeListBuilder.create().texOffs(32, 43).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition RightLegReplacementLower2 = RightLegReplacement.addOrReplaceChild("RightLegReplacementLower2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	public void setupHand(T entity) {
		animator.setupHand();
	}

	@Override
	public ModelPart getArm(HumanoidArm p_102852_) {
		return p_102852_ == HumanoidArm.LEFT ? this.LeftArm : this.RightArm;
	}

	@Override
	public ModelPart getLeg(HumanoidArm p_102852_) {
		return p_102852_ == HumanoidArm.LEFT ? this.LeftLeg : this.RightLeg;
	}

	@Override
	public ModelPart getHead() {
		return this.Head;
	}

	@Override
	public ModelPart getTorso() {
		return this.Torso;
	}

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
	public HumanoidAnimator<T, LatexExoMutantModel<T>> getAnimator(T entity) {
		return animator;
	}
}