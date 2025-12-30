package net.jerika.furmutage.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jerika.furmutage.entity.animations.LatexBomberMutantAnimation;
import net.jerika.furmutage.entity.custom.LatexMutantBomberEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
public class LatexBomberMutantModel<T extends LatexMutantBomberEntity> extends HierarchicalModel<T> {
	private final ModelPart LeftArm;
	private final ModelPart RightArm;
	private final ModelPart Torso;
	private final ModelPart Tail;
	private final ModelPart TailPrimary;
	private final ModelPart TailSecondary;
	private final ModelPart TailTertiary;
	private final ModelPart tumors;
	private final ModelPart Head;
	private final ModelPart RightEar;
	private final ModelPart RightEarPivot;
	private final ModelPart Hair;
	private final ModelPart tumors2;
	private final ModelPart LeftLeg;
	private final ModelPart LeftLowerLeg;
	private final ModelPart LeftFoot;
	private final ModelPart LeftPad;
	private final ModelPart tumors3;
	private final ModelPart RightLeg;
	private final ModelPart RightLowerLeg;
	private final ModelPart RightFoot;
	private final ModelPart RightPad;
	private final ModelPart rootPart;

	public LatexBomberMutantModel(ModelPart root) {
		this.rootPart = root;
		this.LeftArm = root.getChild("LeftArm");
		this.RightArm = root.getChild("RightArm");
		this.Torso = root.getChild("Torso");
		this.Tail = this.Torso.getChild("Tail");
		this.TailPrimary = this.Tail.getChild("TailPrimary");
		this.TailSecondary = this.TailPrimary.getChild("TailSecondary");
		this.TailTertiary = this.TailSecondary.getChild("TailTertiary");
		this.tumors = this.Torso.getChild("tumors");
		this.Head = root.getChild("Head");
		this.RightEar = this.Head.getChild("RightEar");
		this.RightEarPivot = this.RightEar.getChild("RightEarPivot");
		this.Hair = this.Head.getChild("Hair");
		this.tumors2 = this.Head.getChild("tumors2");
		this.LeftLeg = root.getChild("LeftLeg");
		this.LeftLowerLeg = this.LeftLeg.getChild("LeftLowerLeg");
		this.LeftFoot = this.LeftLowerLeg.getChild("LeftFoot");
		this.LeftPad = this.LeftFoot.getChild("LeftPad");
		this.tumors3 = this.LeftLeg.getChild("tumors3");
		this.RightLeg = root.getChild("RightLeg");
		this.RightLowerLeg = this.RightLeg.getChild("RightLowerLeg");
		this.RightFoot = this.RightLowerLeg.getChild("RightFoot");
		this.RightPad = this.RightFoot.getChild("RightPad");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(64, 67).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 1.5F, 0.0F));

		PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(68, 49).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 1.5F, 0.0F));

		PartDefinition Torso = partdefinition.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(30, 31).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));

		PartDefinition Tail = Torso.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0F, 12.5F, 0.0F));

		PartDefinition TailPrimary = Tail.addOrReplaceChild("TailPrimary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition Base_r1 = TailPrimary.addOrReplaceChild("Base_r1", CubeListBuilder.create().texOffs(60, 10).addBox(-2.0F, 0.75F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(-0.4F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1781F, 0.0F, 0.0F));

		PartDefinition TailSecondary = TailPrimary.addOrReplaceChild("TailSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.25F, 4.0F));

		PartDefinition Base_r2 = TailSecondary.addOrReplaceChild("Base_r2", CubeListBuilder.create().texOffs(48, 49).addBox(-2.5F, -0.65F, -2.1F, 5.0F, 6.0F, 5.0F, new CubeDeformation(-0.35F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.5F, 1.4835F, 0.0F, 0.0F));

		PartDefinition TailTertiary = TailSecondary.addOrReplaceChild("TailTertiary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0195F, 4.5086F));

		PartDefinition Base_r3 = TailTertiary.addOrReplaceChild("Base_r3", CubeListBuilder.create().texOffs(0, 66).addBox(-2.0F, -1.7F, -1.75F, 4.0F, 4.0F, 4.0F, new CubeDeformation(-0.37F)), PartPose.offsetAndRotation(0.0F, -0.4F, 1.5F, 1.8326F, 0.0F, 0.0F));

		PartDefinition tumors = Torso.addOrReplaceChild("tumors", CubeListBuilder.create().texOffs(68, 57).addBox(2.0F, 7.0F, -3.0F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(16, 59).addBox(1.0F, 5.0F, 0.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(32, 0).addBox(-7.0F, 4.0F, -5.0F, 7.0F, 7.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 46).addBox(-6.0F, 5.0F, -1.0F, 7.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(24, 49).addBox(-3.0F, 0.0F, -1.0F, 7.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(30, 75).addBox(-2.4F, 6.0F, -5.7F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(40, 75).addBox(-8.0F, 6.0F, -4.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(64, 75).addBox(-6.0F, 7.0F, -6.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(74, 75).addBox(-8.0F, 7.0F, 0.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(74, 38).addBox(-5.0F, 3.0F, -4.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(12, 76).addBox(-1.0F, 2.0F, 3.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(76, 15).addBox(-5.0F, 6.0F, 3.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 74).addBox(-2.0F, -3.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(42, 13).addBox(-1.5F, -1.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));

		PartDefinition Snout_r1 = Head.addOrReplaceChild("Snout_r1", CubeListBuilder.create().texOffs(26, 32).addBox(-1.0F, -29.625F, -0.95F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 26.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

		PartDefinition RightEar = Head.addOrReplaceChild("RightEar", CubeListBuilder.create(), PartPose.offset(-3.0F, -7.5F, 0.0F));

		PartDefinition RightEarPivot = RightEar.addOrReplaceChild("RightEarPivot", CubeListBuilder.create().texOffs(32, 13).addBox(-4.6F, 0.8F, 1.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(42, 69).addBox(-5.6F, 0.8F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(72, 42).addBox(-4.6F, 0.8F, -1.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2F, -0.9F, -0.6F, -0.2182F, 0.1745F, -0.4363F));

		PartDefinition Hair = Head.addOrReplaceChild("Hair", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F))
		.texOffs(24, 8).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tumors2 = Head.addOrReplaceChild("tumors2", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -11.0F, -5.0F, 7.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 32).addBox(1.0F, -6.0F, -2.6F, 7.0F, 8.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(31, 16).addBox(-3.0F, -10.0F, -3.0F, 6.0F, 7.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(54, 31).addBox(-7.0F, -5.0F, -6.0F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(74, 33).addBox(-8.0F, -3.0F, -5.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(76, 10).addBox(-6.0F, -4.0F, -7.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(74, 0).addBox(5.0F, -9.0F, -1.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(74, 5).addBox(1.0F, -9.0F, -6.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(74, 28).addBox(1.0F, -12.0F, -1.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(76, 20).addBox(-2.0F, -7.0F, 4.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(50, 76).addBox(6.0F, -3.0F, -1.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(24, 46).addBox(5.0F, -2.0F, -4.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(2.5F, 12.5F, 0.0F));

		PartDefinition LeftThigh_r1 = LeftLeg.addOrReplaceChild("LeftThigh_r1", CubeListBuilder.create().texOffs(0, 56).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition LeftLowerLeg = LeftLeg.addOrReplaceChild("LeftLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 5.375F, -3.2F));

		PartDefinition LeftCalf_r1 = LeftLowerLeg.addOrReplaceChild("LeftCalf_r1", CubeListBuilder.create().texOffs(60, 19).addBox(-2.01F, 0.1215F, -0.0823F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.125F, -0.05F, 0.9774F, 0.0F, 0.0F));

		PartDefinition LeftFoot = LeftLowerLeg.addOrReplaceChild("LeftFoot", CubeListBuilder.create(), PartPose.offset(0.0F, -0.2F, 7.175F));

		PartDefinition LeftArch_r1 = LeftFoot.addOrReplaceChild("LeftArch_r1", CubeListBuilder.create().texOffs(16, 67).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

		PartDefinition LeftPad = LeftFoot.addOrReplaceChild("LeftPad", CubeListBuilder.create().texOffs(54, 42).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

		PartDefinition tumors3 = LeftLeg.addOrReplaceChild("tumors3", CubeListBuilder.create().texOffs(30, 69).addBox(0.0F, 0.0F, -3.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(26, 34).addBox(-2.0F, 2.0F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(26, 36).addBox(3.0F, 1.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-2.5F, 12.5F, 0.0F));

		PartDefinition RightThigh_r1 = RightLeg.addOrReplaceChild("RightThigh_r1", CubeListBuilder.create().texOffs(58, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition RightLowerLeg = RightLeg.addOrReplaceChild("RightLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 5.375F, -3.2F));

		PartDefinition RightCalf_r1 = RightLowerLeg.addOrReplaceChild("RightCalf_r1", CubeListBuilder.create().texOffs(34, 60).addBox(-1.99F, 0.1215F, -0.0823F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.125F, -0.05F, 0.9774F, 0.0F, 0.0F));

		PartDefinition RightFoot = RightLowerLeg.addOrReplaceChild("RightFoot", CubeListBuilder.create(), PartPose.offset(0.0F, -0.2F, 7.175F));

		PartDefinition RightArch_r1 = RightFoot.addOrReplaceChild("RightArch_r1", CubeListBuilder.create().texOffs(50, 67).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

		PartDefinition RightPad = RightFoot.addOrReplaceChild("RightPad", CubeListBuilder.create().texOffs(50, 60).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public ModelPart root() {
		return rootPart;
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

		// Animate walking/running legs
		this.animateWalk(LatexBomberMutantAnimation.LATEX_BOMBER_MUTANT_WALK, limbSwing, limbSwingAmount, 2.0f, 2.5f);
		if (entity != null) {
			this.animate(entity.idleAnimationState, LatexBomberMutantAnimation.LATEX_BOMBER_MUTANT_IDLE, ageInTicks, 1.0f);
		}
	}

	private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
		pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
		pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

		this.Head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
		this.Head.xRot = pHeadPitch * ((float)Math.PI / 180F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		LeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		RightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Torso.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		LeftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		RightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}