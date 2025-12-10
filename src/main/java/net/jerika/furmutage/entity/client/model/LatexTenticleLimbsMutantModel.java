package net.jerika.furmutage.entity.client.model;// Made with Blockbench 5.0.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jerika.furmutage.entity.animations.LatexTenticleLimbsMutantAnimations;
import net.jerika.furmutage.entity.animations.mutantfamilyAnimations;
import net.jerika.furmutage.entity.custom.LatexTenticleLimbsMutantEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class LatexTenticleLimbsMutantModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "latex_tenticle_limbs_mutant"), "main");
	private final ModelPart root;
	private final ModelPart RightLeg;
	private final ModelPart RightLowerLeg;
	private final ModelPart RightFoot;
	private final ModelPart RightPad;
	private final ModelPart LeftLeg;
	private final ModelPart LeftLowerLeg;
	private final ModelPart LeftFoot;
	private final ModelPart LeftPad;
	private final ModelPart Head;
	private final ModelPart RightEar;
	private final ModelPart RightEarPivot;
	private final ModelPart LeftEar;
	private final ModelPart LeftEarPivot;
	private final ModelPart RightEar2;
	private final ModelPart RightEarPivot2;
	private final ModelPart LeftEar2;
	private final ModelPart LeftEarPivot2;
	private final ModelPart RightEar3;
	private final ModelPart RightEarPivot3;
	private final ModelPart LeftEar3;
	private final ModelPart LeftEarPivot3;
	private final ModelPart Torso;
	private final ModelPart Tail;
	private final ModelPart TailPrimary;
	private final ModelPart TailSecondary;
	private final ModelPart TailTertiary;
	private final ModelPart tenticlesleft;
	private final ModelPart TentacleSecondaryLU7;
	private final ModelPart TentacleTertiaryLU7;
	private final ModelPart TentacleQuaternaryLU7;
	private final ModelPart TentaclePadLU7;
	private final ModelPart TentacleSecondaryLU6;
	private final ModelPart TentacleTertiaryLU6;
	private final ModelPart TentacleQuaternaryLU6;
	private final ModelPart TentaclePadLU6;
	private final ModelPart TentacleSecondaryLU5;
	private final ModelPart TentacleTertiaryLU5;
	private final ModelPart TentacleQuaternaryLU5;
	private final ModelPart TentaclePadLU5;
	private final ModelPart TentacleSecondaryLU2;
	private final ModelPart TentacleTertiaryLU2;
	private final ModelPart TentacleQuaternaryLU2;
	private final ModelPart TentaclePadLU2;
	private final ModelPart TentacleSecondaryLU3;
	private final ModelPart TentacleTertiaryLU3;
	private final ModelPart TentacleQuaternaryLU3;
	private final ModelPart TentaclePadLU3;
	private final ModelPart TentacleSecondaryLU4;
	private final ModelPart TentacleTertiaryLU4;
	private final ModelPart TentacleQuaternaryLU4;
	private final ModelPart TentaclePadLU4;
	private final ModelPart TentacleSecondaryLU;
	private final ModelPart TentacleTertiaryLU;
	private final ModelPart TentacleQuaternaryLU;
	private final ModelPart TentaclePadLU;
	private final ModelPart tenticlesright;
	private final ModelPart TentacleSecondaryRU;
	private final ModelPart TentacleTertiaryRU;
	private final ModelPart TentacleQuaternaryRU;
	private final ModelPart TentaclePadRU;
	private final ModelPart TentacleSecondaryRU2;
	private final ModelPart TentacleTertiaryRU2;
	private final ModelPart TentacleQuaternaryRU2;
	private final ModelPart TentaclePadRU2;
	private final ModelPart TentacleSecondaryRU3;
	private final ModelPart TentacleTertiaryRU3;
	private final ModelPart TentacleQuaternaryRU3;
	private final ModelPart TentaclePadRU3;
	private final ModelPart TentacleSecondaryRU4;
	private final ModelPart TentacleTertiaryRU4;
	private final ModelPart TentacleQuaternaryRU4;
	private final ModelPart TentaclePadRU4;
	private final ModelPart TentacleSecondaryRU5;
	private final ModelPart TentacleTertiaryRU5;
	private final ModelPart TentacleQuaternaryRU5;
	private final ModelPart TentaclePadRU5;
	private final ModelPart TentacleSecondaryRU6;
	private final ModelPart TentacleTertiaryRU6;
	private final ModelPart TentacleQuaternaryRU6;
	private final ModelPart TentaclePadRU6;
	private final ModelPart TentacleSecondaryRU7;
	private final ModelPart TentacleTertiaryRU7;
	private final ModelPart TentacleQuaternaryRU7;
	private final ModelPart TentaclePadRU7;

	public LatexTenticleLimbsMutantModel(ModelPart root) {
		this.root = root;
		this.RightLeg = root.getChild("RightLeg");
		this.RightLowerLeg = this.RightLeg.getChild("RightLowerLeg");
		this.RightFoot = this.RightLowerLeg.getChild("RightFoot");
		this.RightPad = this.RightFoot.getChild("RightPad");
		this.LeftLeg = root.getChild("LeftLeg");
		this.LeftLowerLeg = this.LeftLeg.getChild("LeftLowerLeg");
		this.LeftFoot = this.LeftLowerLeg.getChild("LeftFoot");
		this.LeftPad = this.LeftFoot.getChild("LeftPad");
		this.Head = root.getChild("Head");
		this.RightEar = this.Head.getChild("RightEar");
		this.RightEarPivot = this.RightEar.getChild("RightEarPivot");
		this.LeftEar = this.Head.getChild("LeftEar");
		this.LeftEarPivot = this.LeftEar.getChild("LeftEarPivot");
		this.RightEar2 = this.Head.getChild("RightEar2");
		this.RightEarPivot2 = this.RightEar2.getChild("RightEarPivot2");
		this.LeftEar2 = this.Head.getChild("LeftEar2");
		this.LeftEarPivot2 = this.LeftEar2.getChild("LeftEarPivot2");
		this.RightEar3 = this.Head.getChild("RightEar3");
		this.RightEarPivot3 = this.RightEar3.getChild("RightEarPivot3");
		this.LeftEar3 = this.Head.getChild("LeftEar3");
		this.LeftEarPivot3 = this.LeftEar3.getChild("LeftEarPivot3");
		this.Torso = root.getChild("Torso");
		this.Tail = this.Torso.getChild("Tail");
		this.TailPrimary = this.Tail.getChild("TailPrimary");
		this.TailSecondary = this.TailPrimary.getChild("TailSecondary");
		this.TailTertiary = this.TailSecondary.getChild("TailTertiary");
		this.tenticlesleft = root.getChild("tenticlesleft");
		this.TentacleSecondaryLU7 = this.tenticlesleft.getChild("TentacleSecondaryLU7");
		this.TentacleTertiaryLU7 = this.TentacleSecondaryLU7.getChild("TentacleTertiaryLU7");
		this.TentacleQuaternaryLU7 = this.TentacleTertiaryLU7.getChild("TentacleQuaternaryLU7");
		this.TentaclePadLU7 = this.TentacleQuaternaryLU7.getChild("TentaclePadLU7");
		this.TentacleSecondaryLU6 = this.tenticlesleft.getChild("TentacleSecondaryLU6");
		this.TentacleTertiaryLU6 = this.TentacleSecondaryLU6.getChild("TentacleTertiaryLU6");
		this.TentacleQuaternaryLU6 = this.TentacleTertiaryLU6.getChild("TentacleQuaternaryLU6");
		this.TentaclePadLU6 = this.TentacleQuaternaryLU6.getChild("TentaclePadLU6");
		this.TentacleSecondaryLU5 = this.tenticlesleft.getChild("TentacleSecondaryLU5");
		this.TentacleTertiaryLU5 = this.TentacleSecondaryLU5.getChild("TentacleTertiaryLU5");
		this.TentacleQuaternaryLU5 = this.TentacleTertiaryLU5.getChild("TentacleQuaternaryLU5");
		this.TentaclePadLU5 = this.TentacleQuaternaryLU5.getChild("TentaclePadLU5");
		this.TentacleSecondaryLU2 = this.tenticlesleft.getChild("TentacleSecondaryLU2");
		this.TentacleTertiaryLU2 = this.TentacleSecondaryLU2.getChild("TentacleTertiaryLU2");
		this.TentacleQuaternaryLU2 = this.TentacleTertiaryLU2.getChild("TentacleQuaternaryLU2");
		this.TentaclePadLU2 = this.TentacleQuaternaryLU2.getChild("TentaclePadLU2");
		this.TentacleSecondaryLU3 = this.tenticlesleft.getChild("TentacleSecondaryLU3");
		this.TentacleTertiaryLU3 = this.TentacleSecondaryLU3.getChild("TentacleTertiaryLU3");
		this.TentacleQuaternaryLU3 = this.TentacleTertiaryLU3.getChild("TentacleQuaternaryLU3");
		this.TentaclePadLU3 = this.TentacleQuaternaryLU3.getChild("TentaclePadLU3");
		this.TentacleSecondaryLU4 = this.tenticlesleft.getChild("TentacleSecondaryLU4");
		this.TentacleTertiaryLU4 = this.TentacleSecondaryLU4.getChild("TentacleTertiaryLU4");
		this.TentacleQuaternaryLU4 = this.TentacleTertiaryLU4.getChild("TentacleQuaternaryLU4");
		this.TentaclePadLU4 = this.TentacleQuaternaryLU4.getChild("TentaclePadLU4");
		this.TentacleSecondaryLU = this.tenticlesleft.getChild("TentacleSecondaryLU");
		this.TentacleTertiaryLU = this.TentacleSecondaryLU.getChild("TentacleTertiaryLU");
		this.TentacleQuaternaryLU = this.TentacleTertiaryLU.getChild("TentacleQuaternaryLU");
		this.TentaclePadLU = this.TentacleQuaternaryLU.getChild("TentaclePadLU");
		this.tenticlesright = root.getChild("tenticlesright");
		this.TentacleSecondaryRU = this.tenticlesright.getChild("TentacleSecondaryRU");
		this.TentacleTertiaryRU = this.TentacleSecondaryRU.getChild("TentacleTertiaryRU");
		this.TentacleQuaternaryRU = this.TentacleTertiaryRU.getChild("TentacleQuaternaryRU");
		this.TentaclePadRU = this.TentacleQuaternaryRU.getChild("TentaclePadRU");
		this.TentacleSecondaryRU2 = this.tenticlesright.getChild("TentacleSecondaryRU2");
		this.TentacleTertiaryRU2 = this.TentacleSecondaryRU2.getChild("TentacleTertiaryRU2");
		this.TentacleQuaternaryRU2 = this.TentacleTertiaryRU2.getChild("TentacleQuaternaryRU2");
		this.TentaclePadRU2 = this.TentacleQuaternaryRU2.getChild("TentaclePadRU2");
		this.TentacleSecondaryRU3 = this.tenticlesright.getChild("TentacleSecondaryRU3");
		this.TentacleTertiaryRU3 = this.TentacleSecondaryRU3.getChild("TentacleTertiaryRU3");
		this.TentacleQuaternaryRU3 = this.TentacleTertiaryRU3.getChild("TentacleQuaternaryRU3");
		this.TentaclePadRU3 = this.TentacleQuaternaryRU3.getChild("TentaclePadRU3");
		this.TentacleSecondaryRU4 = this.tenticlesright.getChild("TentacleSecondaryRU4");
		this.TentacleTertiaryRU4 = this.TentacleSecondaryRU4.getChild("TentacleTertiaryRU4");
		this.TentacleQuaternaryRU4 = this.TentacleTertiaryRU4.getChild("TentacleQuaternaryRU4");
		this.TentaclePadRU4 = this.TentacleQuaternaryRU4.getChild("TentaclePadRU4");
		this.TentacleSecondaryRU5 = this.tenticlesright.getChild("TentacleSecondaryRU5");
		this.TentacleTertiaryRU5 = this.TentacleSecondaryRU5.getChild("TentacleTertiaryRU5");
		this.TentacleQuaternaryRU5 = this.TentacleTertiaryRU5.getChild("TentacleQuaternaryRU5");
		this.TentaclePadRU5 = this.TentacleQuaternaryRU5.getChild("TentaclePadRU5");
		this.TentacleSecondaryRU6 = this.tenticlesright.getChild("TentacleSecondaryRU6");
		this.TentacleTertiaryRU6 = this.TentacleSecondaryRU6.getChild("TentacleTertiaryRU6");
		this.TentacleQuaternaryRU6 = this.TentacleTertiaryRU6.getChild("TentacleQuaternaryRU6");
		this.TentaclePadRU6 = this.TentacleQuaternaryRU6.getChild("TentaclePadRU6");
		this.TentacleSecondaryRU7 = this.tenticlesright.getChild("TentacleSecondaryRU7");
		this.TentacleTertiaryRU7 = this.TentacleSecondaryRU7.getChild("TentacleTertiaryRU7");
		this.TentacleQuaternaryRU7 = this.TentacleTertiaryRU7.getChild("TentacleQuaternaryRU7");
		this.TentaclePadRU7 = this.TentacleQuaternaryRU7.getChild("TentaclePadRU7");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-2.5F, 12.5F, 1.0F));

		PartDefinition RightThigh_r1 = RightLeg.addOrReplaceChild("RightThigh_r1", CubeListBuilder.create().texOffs(30, 11).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition RightLowerLeg = RightLeg.addOrReplaceChild("RightLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 5.375F, -3.2F));

		PartDefinition RightCalf_r1 = RightLowerLeg.addOrReplaceChild("RightCalf_r1", CubeListBuilder.create().texOffs(32, 31).addBox(-1.99F, 0.1215F, -0.0823F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.125F, -0.05F, 0.9774F, 0.0F, 0.0F));

		PartDefinition RightFoot = RightLowerLeg.addOrReplaceChild("RightFoot", CubeListBuilder.create(), PartPose.offset(0.0F, -0.2F, 7.175F));

		PartDefinition RightArch_r1 = RightFoot.addOrReplaceChild("RightArch_r1", CubeListBuilder.create().texOffs(16, 47).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

		PartDefinition RightPad = RightFoot.addOrReplaceChild("RightPad", CubeListBuilder.create().texOffs(16, 40).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

		PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(2.5F, 12.5F, 1.0F));

		PartDefinition LeftThigh_r1 = LeftLeg.addOrReplaceChild("LeftThigh_r1", CubeListBuilder.create().texOffs(32, 21).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition LeftLowerLeg = LeftLeg.addOrReplaceChild("LeftLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 5.375F, -3.2F));

		PartDefinition LeftCalf_r1 = LeftLowerLeg.addOrReplaceChild("LeftCalf_r1", CubeListBuilder.create().texOffs(0, 37).addBox(-2.01F, 0.1215F, -0.0823F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.125F, -0.05F, 0.9774F, 0.0F, 0.0F));

		PartDefinition LeftFoot = LeftLowerLeg.addOrReplaceChild("LeftFoot", CubeListBuilder.create(), PartPose.offset(0.0F, -0.2F, 7.175F));

		PartDefinition LeftArch_r1 = LeftFoot.addOrReplaceChild("LeftArch_r1", CubeListBuilder.create().texOffs(30, 47).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

		PartDefinition LeftPad = LeftFoot.addOrReplaceChild("LeftPad", CubeListBuilder.create().texOffs(34, 40).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 21).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(84, 18).addBox(-2.0F, -5.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(50, 9).addBox(-1.5F, 0.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));

		PartDefinition Snout_r1 = Head.addOrReplaceChild("Snout_r1", CubeListBuilder.create().texOffs(64, 43).addBox(-1.0F, -29.625F, -0.95F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 24.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

		PartDefinition RightEar = Head.addOrReplaceChild("RightEar", CubeListBuilder.create(), PartPose.offset(-3.0F, -7.5F, 0.0F));

		PartDefinition RightEarPivot = RightEar.addOrReplaceChild("RightEarPivot", CubeListBuilder.create().texOffs(16, 37).addBox(-4.6F, 0.8F, 1.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(8, 89).addBox(-5.6F, 0.8F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 83).addBox(-4.6F, 0.8F, -1.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2F, -0.9F, -0.6F, -0.2182F, 0.1745F, -0.4363F));

		PartDefinition LeftEar = Head.addOrReplaceChild("LeftEar", CubeListBuilder.create(), PartPose.offset(3.0F, -7.5F, 0.0F));

		PartDefinition LeftEarPivot = LeftEar.addOrReplaceChild("LeftEarPivot", CubeListBuilder.create().texOffs(48, 83).addBox(0.6F, 0.8F, -1.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(84, 32).addBox(0.6F, 0.8F, 1.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 89).addBox(4.6F, 0.8F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2F, -0.9F, -0.6F, -0.2182F, -0.1745F, 0.4363F));

		PartDefinition RightEar2 = Head.addOrReplaceChild("RightEar2", CubeListBuilder.create(), PartPose.offset(-3.0F, -8.2F, 2.0F));

		PartDefinition RightEarPivot2 = RightEar2.addOrReplaceChild("RightEarPivot2", CubeListBuilder.create().texOffs(44, 88).addBox(-1.9F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(60, 88).addBox(-0.9F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
		.texOffs(26, 37).addBox(-0.9F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(48, 37).addBox(0.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

		PartDefinition LeftEar2 = Head.addOrReplaceChild("LeftEar2", CubeListBuilder.create(), PartPose.offset(3.0F, -8.2F, 2.0F));

		PartDefinition LeftEarPivot2 = LeftEar2.addOrReplaceChild("LeftEarPivot2", CubeListBuilder.create().texOffs(88, 35).addBox(-1.1F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(20, 89).addBox(-1.1F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
		.texOffs(88, 39).addBox(-1.1F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(80, 85).addBox(-1.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

		PartDefinition RightEar3 = Head.addOrReplaceChild("RightEar3", CubeListBuilder.create(), PartPose.offset(-3.0F, -8.2F, -1.0F));

		PartDefinition RightEarPivot3 = RightEar3.addOrReplaceChild("RightEarPivot3", CubeListBuilder.create().texOffs(52, 88).addBox(-1.9F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(66, 88).addBox(-0.9F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
		.texOffs(86, 15).addBox(-0.9F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(58, 9).addBox(0.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

		PartDefinition LeftEar3 = Head.addOrReplaceChild("LeftEar3", CubeListBuilder.create(), PartPose.offset(3.0F, -8.2F, -1.0F));

		PartDefinition LeftEarPivot3 = LeftEar3.addOrReplaceChild("LeftEarPivot3", CubeListBuilder.create().texOffs(36, 88).addBox(-1.1F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(26, 89).addBox(-1.1F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
		.texOffs(80, 89).addBox(-1.1F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(80, 87).addBox(-1.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

		PartDefinition Torso = partdefinition.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 0.0F, -1.0F, 8.0F, 14.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(99, 13).addBox(-2.0F, -3.0F, 5.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(100, 1).addBox(-5.0F, 3.0F, 4.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(101, 26).addBox(1.0F, 1.0F, 3.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));

		PartDefinition Tail = Torso.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0F, 11.5F, 3.0F));

		PartDefinition TailPrimary = Tail.addOrReplaceChild("TailPrimary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition Base_r1 = TailPrimary.addOrReplaceChild("Base_r1", CubeListBuilder.create().texOffs(0, 46).addBox(-2.0F, 0.75F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(-0.4F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1781F, 0.0F, 0.0F));

		PartDefinition TailSecondary = TailPrimary.addOrReplaceChild("TailSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.25F, 4.0F));

		PartDefinition Base_r2 = TailSecondary.addOrReplaceChild("Base_r2", CubeListBuilder.create().texOffs(30, 0).addBox(-2.5F, -0.65F, -2.1F, 5.0F, 6.0F, 5.0F, new CubeDeformation(-0.35F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.5F, 1.4835F, 0.0F, 0.0F));

		PartDefinition TailTertiary = TailSecondary.addOrReplaceChild("TailTertiary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0195F, 4.5086F));

		PartDefinition Base_r3 = TailTertiary.addOrReplaceChild("Base_r3", CubeListBuilder.create().texOffs(46, 11).addBox(-2.0F, -1.7F, -1.75F, 4.0F, 4.0F, 4.0F, new CubeDeformation(-0.37F)), PartPose.offsetAndRotation(0.0F, -0.4F, 1.5F, 1.8326F, 0.0F, 0.0F));

		PartDefinition tenticlesleft = partdefinition.addOrReplaceChild("tenticlesleft", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition TentacleSecondaryLU7 = tenticlesleft.addOrReplaceChild("TentacleSecondaryLU7", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, -22.0F, 0.3F, 0.0F, 0.7854F, 0.0F));

		PartDefinition TentaclePart_r1 = TentacleSecondaryLU7.addOrReplaceChild("TentaclePart_r1", CubeListBuilder.create().texOffs(60, 70).addBox(-2.0F, -1.0F, 3.55F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 0.5F, -3.3F, 0.2269F, 0.6981F, 0.1134F));

		PartDefinition TentacleTertiaryLU7 = TentacleSecondaryLU7.addOrReplaceChild("TentacleTertiaryLU7", CubeListBuilder.create(), PartPose.offset(2.4F, -0.7F, 2.7F));

		PartDefinition TentaclePart_r2 = TentacleTertiaryLU7.addOrReplaceChild("TentaclePart_r2", CubeListBuilder.create().texOffs(12, 83).addBox(-3.8F, -1.0F, 6.75F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.9F, 1.2F, -6.0F, 0.2967F, 0.9425F, 0.2094F));

		PartDefinition TentacleQuaternaryLU7 = TentacleTertiaryLU7.addOrReplaceChild("TentacleQuaternaryLU7", CubeListBuilder.create(), PartPose.offset(3.0F, -0.5F, 2.2F));

		PartDefinition TentaclePart_r3 = TentacleQuaternaryLU7.addOrReplaceChild("TentaclePart_r3", CubeListBuilder.create().texOffs(24, 83).addBox(-6.45F, -1.0F, 9.375F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.9F, 1.7F, -8.2F, 0.4712F, 1.1868F, 0.4102F));

		PartDefinition TentaclePadLU7 = TentacleQuaternaryLU7.addOrReplaceChild("TentaclePadLU7", CubeListBuilder.create(), PartPose.offset(3.6F, -0.4F, 1.3F));

		PartDefinition TentaclePart_r4 = TentaclePadLU7.addOrReplaceChild("TentaclePart_r4", CubeListBuilder.create().texOffs(0, 88).addBox(-8.075F, -1.5F, 16.4F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(60, 55).addBox(-8.075F, -2.5F, 12.4F, 2.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5F, 2.1F, -9.5F, 0.6807F, 1.2915F, 0.6283F));

		PartDefinition TentacleSecondaryLU6 = tenticlesleft.addOrReplaceChild("TentacleSecondaryLU6", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, -12.0F, 1.3F, 0.0F, 1.6144F, 0.0F));

		PartDefinition TentaclePart_r5 = TentacleSecondaryLU6.addOrReplaceChild("TentaclePart_r5", CubeListBuilder.create().texOffs(68, 43).addBox(-2.0F, -1.0F, 3.55F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 0.5F, -3.3F, 0.2269F, 0.6981F, 0.1134F));

		PartDefinition TentacleTertiaryLU6 = TentacleSecondaryLU6.addOrReplaceChild("TentacleTertiaryLU6", CubeListBuilder.create(), PartPose.offset(2.4F, -0.7F, 2.7F));

		PartDefinition TentaclePart_r6 = TentacleTertiaryLU6.addOrReplaceChild("TentaclePart_r6", CubeListBuilder.create().texOffs(48, 77).addBox(-3.8F, -1.0F, 6.75F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.9F, 1.2F, -6.0F, 0.2967F, 0.9425F, 0.2094F));

		PartDefinition TentacleQuaternaryLU6 = TentacleTertiaryLU6.addOrReplaceChild("TentacleQuaternaryLU6", CubeListBuilder.create(), PartPose.offset(3.0F, -0.5F, 2.2F));

		PartDefinition TentaclePart_r7 = TentacleQuaternaryLU6.addOrReplaceChild("TentaclePart_r7", CubeListBuilder.create().texOffs(72, 79).addBox(-6.45F, -1.0F, 9.375F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.9F, 1.7F, -8.2F, 0.4712F, 1.1868F, 0.4102F));

		PartDefinition TentaclePadLU6 = TentacleQuaternaryLU6.addOrReplaceChild("TentaclePadLU6", CubeListBuilder.create(), PartPose.offset(3.6F, -0.4F, 1.3F));

		PartDefinition TentaclePart_r8 = TentaclePadLU6.addOrReplaceChild("TentaclePart_r8", CubeListBuilder.create().texOffs(86, 0).addBox(-8.075F, -1.5F, 16.4F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(48, 56).addBox(-8.075F, -2.5F, 12.4F, 2.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5F, 2.1F, -9.5F, 0.6807F, 1.2915F, 0.6283F));

		PartDefinition TentacleSecondaryLU5 = tenticlesleft.addOrReplaceChild("TentacleSecondaryLU5", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, -19.0F, 0.3F, 0.0F, 1.0472F, 0.0F));

		PartDefinition TentaclePart_r9 = TentacleSecondaryLU5.addOrReplaceChild("TentaclePart_r9", CubeListBuilder.create().texOffs(48, 65).addBox(-2.0F, -1.0F, 3.55F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 0.5F, -3.3F, 0.2269F, 0.6981F, 0.1134F));

		PartDefinition TentacleTertiaryLU5 = TentacleSecondaryLU5.addOrReplaceChild("TentacleTertiaryLU5", CubeListBuilder.create(), PartPose.offset(2.4F, -0.7F, 2.7F));

		PartDefinition TentaclePart_r10 = TentacleTertiaryLU5.addOrReplaceChild("TentaclePart_r10", CubeListBuilder.create().texOffs(24, 77).addBox(-3.8F, -1.0F, 6.75F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.9F, 1.2F, -6.0F, 0.2967F, 0.9425F, 0.2094F));

		PartDefinition TentacleQuaternaryLU5 = TentacleTertiaryLU5.addOrReplaceChild("TentacleQuaternaryLU5", CubeListBuilder.create(), PartPose.offset(3.0F, -0.5F, 2.2F));

		PartDefinition TentaclePart_r11 = TentacleQuaternaryLU5.addOrReplaceChild("TentaclePart_r11", CubeListBuilder.create().texOffs(36, 77).addBox(-6.45F, -1.0F, 9.375F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.9F, 1.7F, -8.2F, 0.4712F, 1.1868F, 0.4102F));

		PartDefinition TentaclePadLU5 = TentacleQuaternaryLU5.addOrReplaceChild("TentaclePadLU5", CubeListBuilder.create(), PartPose.offset(3.6F, -0.4F, 1.3F));

		PartDefinition TentaclePart_r12 = TentaclePadLU5.addOrReplaceChild("TentaclePart_r12", CubeListBuilder.create().texOffs(72, 85).addBox(-8.075F, -1.5F, 16.4F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(56, 46).addBox(-8.075F, -2.5F, 12.4F, 2.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5F, 2.1F, -9.5F, 0.6807F, 1.2915F, 0.6283F));

		PartDefinition TentacleSecondaryLU2 = tenticlesleft.addOrReplaceChild("TentacleSecondaryLU2", CubeListBuilder.create(), PartPose.offsetAndRotation(4.0F, -21.0F, 0.3F, 0.0F, 1.8762F, 0.0F));

		PartDefinition TentaclePart_r13 = TentacleSecondaryLU2.addOrReplaceChild("TentaclePart_r13", CubeListBuilder.create().texOffs(62, 6).addBox(-2.0F, -1.0F, 3.55F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 0.5F, -3.3F, 0.2269F, 0.6981F, 0.1134F));

		PartDefinition TentacleTertiaryLU2 = TentacleSecondaryLU2.addOrReplaceChild("TentacleTertiaryLU2", CubeListBuilder.create(), PartPose.offset(2.4F, -0.7F, 2.7F));

		PartDefinition TentaclePart_r14 = TentacleTertiaryLU2.addOrReplaceChild("TentaclePart_r14", CubeListBuilder.create().texOffs(36, 71).addBox(-3.8F, -1.0F, 6.75F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.9F, 1.2F, -6.0F, 0.2967F, 0.9425F, 0.2094F));

		PartDefinition TentacleQuaternaryLU2 = TentacleTertiaryLU2.addOrReplaceChild("TentacleQuaternaryLU2", CubeListBuilder.create(), PartPose.offset(3.0F, -0.5F, 2.2F));

		PartDefinition TentaclePart_r15 = TentacleQuaternaryLU2.addOrReplaceChild("TentaclePart_r15", CubeListBuilder.create().texOffs(48, 71).addBox(-6.45F, -1.0F, 9.375F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.9F, 1.7F, -8.2F, 0.4712F, 1.1868F, 0.4102F));

		PartDefinition TentaclePadLU2 = TentacleQuaternaryLU2.addOrReplaceChild("TentaclePadLU2", CubeListBuilder.create(), PartPose.offset(3.6F, -0.4F, 1.3F));

		PartDefinition TentaclePart_r16 = TentaclePadLU2.addOrReplaceChild("TentaclePart_r16", CubeListBuilder.create().texOffs(84, 27).addBox(-8.075F, -1.5F, 16.4F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(48, 19).addBox(-8.075F, -2.5F, 12.4F, 2.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5F, 2.1F, -9.5F, 0.6807F, 1.2915F, 0.6283F));

		PartDefinition TentacleSecondaryLU3 = tenticlesleft.addOrReplaceChild("TentacleSecondaryLU3", CubeListBuilder.create(), PartPose.offsetAndRotation(4.0F, -14.0F, 0.3F, 0.0F, 1.1345F, 0.0F));

		PartDefinition TentaclePart_r17 = TentacleSecondaryLU3.addOrReplaceChild("TentaclePart_r17", CubeListBuilder.create().texOffs(62, 12).addBox(-2.0F, -1.0F, 3.55F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 0.5F, -3.3F, 0.2269F, 0.6981F, 0.1134F));

		PartDefinition TentacleTertiaryLU3 = TentacleSecondaryLU3.addOrReplaceChild("TentacleTertiaryLU3", CubeListBuilder.create(), PartPose.offset(2.4F, -0.7F, 2.7F));

		PartDefinition TentaclePart_r18 = TentacleTertiaryLU3.addOrReplaceChild("TentaclePart_r18", CubeListBuilder.create().texOffs(72, 18).addBox(-3.8F, -1.0F, 6.75F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.9F, 1.2F, -6.0F, 0.2967F, 0.9425F, 0.2094F));

		PartDefinition TentacleQuaternaryLU3 = TentacleTertiaryLU3.addOrReplaceChild("TentacleQuaternaryLU3", CubeListBuilder.create(), PartPose.offset(3.0F, -0.5F, 2.2F));

		PartDefinition TentaclePart_r19 = TentacleQuaternaryLU3.addOrReplaceChild("TentaclePart_r19", CubeListBuilder.create().texOffs(72, 24).addBox(-6.45F, -1.0F, 9.375F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.9F, 1.7F, -8.2F, 0.4712F, 1.1868F, 0.4102F));

		PartDefinition TentaclePadLU3 = TentacleQuaternaryLU3.addOrReplaceChild("TentaclePadLU3", CubeListBuilder.create(), PartPose.offset(3.6F, -0.4F, 1.3F));

		PartDefinition TentaclePart_r20 = TentaclePadLU3.addOrReplaceChild("TentaclePart_r20", CubeListBuilder.create().texOffs(84, 54).addBox(-8.075F, -1.5F, 16.4F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(48, 28).addBox(-8.075F, -2.5F, 12.4F, 2.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5F, 2.1F, -9.5F, 0.6807F, 1.2915F, 0.6283F));

		PartDefinition TentacleSecondaryLU4 = tenticlesleft.addOrReplaceChild("TentacleSecondaryLU4", CubeListBuilder.create(), PartPose.offsetAndRotation(4.0F, -17.0F, 0.3F, 0.0F, 1.4399F, 0.0F));

		PartDefinition TentaclePart_r21 = TentacleSecondaryLU4.addOrReplaceChild("TentaclePart_r21", CubeListBuilder.create().texOffs(0, 64).addBox(-2.0F, -1.0F, 3.55F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 0.5F, -3.3F, 0.2269F, 0.6981F, 0.1134F));

		PartDefinition TentacleTertiaryLU4 = TentacleSecondaryLU4.addOrReplaceChild("TentacleTertiaryLU4", CubeListBuilder.create(), PartPose.offset(2.4F, -0.7F, 2.7F));

		PartDefinition TentaclePart_r22 = TentacleTertiaryLU4.addOrReplaceChild("TentaclePart_r22", CubeListBuilder.create().texOffs(72, 30).addBox(-3.8F, -1.0F, 6.75F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.9F, 1.2F, -6.0F, 0.2967F, 0.9425F, 0.2094F));

		PartDefinition TentacleQuaternaryLU4 = TentacleTertiaryLU4.addOrReplaceChild("TentacleQuaternaryLU4", CubeListBuilder.create(), PartPose.offset(3.0F, -0.5F, 2.2F));

		PartDefinition TentaclePart_r23 = TentacleQuaternaryLU4.addOrReplaceChild("TentaclePart_r23", CubeListBuilder.create().texOffs(72, 55).addBox(-6.45F, -1.0F, 9.375F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.9F, 1.7F, -8.2F, 0.4712F, 1.1868F, 0.4102F));

		PartDefinition TentaclePadLU4 = TentacleQuaternaryLU4.addOrReplaceChild("TentaclePadLU4", CubeListBuilder.create(), PartPose.offset(3.6F, -0.4F, 1.3F));

		PartDefinition TentaclePart_r24 = TentaclePadLU4.addOrReplaceChild("TentaclePart_r24", CubeListBuilder.create().texOffs(84, 59).addBox(-8.075F, -1.5F, 16.4F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(50, 0).addBox(-8.075F, -2.5F, 12.4F, 2.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5F, 2.1F, -9.5F, 0.6807F, 1.2915F, 0.6283F));

		PartDefinition TentacleSecondaryLU = tenticlesleft.addOrReplaceChild("TentacleSecondaryLU", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, -24.0F, 0.3F, 0.0F, 1.2654F, 0.0F));

		PartDefinition TentaclePart_r25 = TentacleSecondaryLU.addOrReplaceChild("TentaclePart_r25", CubeListBuilder.create().texOffs(62, 0).addBox(-2.0F, -1.0F, 3.55F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 0.5F, -3.3F, 0.2269F, 0.6981F, 0.1134F));

		PartDefinition TentacleTertiaryLU = TentacleSecondaryLU.addOrReplaceChild("TentacleTertiaryLU", CubeListBuilder.create(), PartPose.offset(2.4F, -0.7F, 2.7F));

		PartDefinition TentaclePart_r26 = TentacleTertiaryLU.addOrReplaceChild("TentaclePart_r26", CubeListBuilder.create().texOffs(12, 71).addBox(-3.8F, -1.0F, 6.75F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.9F, 1.2F, -6.0F, 0.2967F, 0.9425F, 0.2094F));

		PartDefinition TentacleQuaternaryLU = TentacleTertiaryLU.addOrReplaceChild("TentacleQuaternaryLU", CubeListBuilder.create(), PartPose.offset(3.0F, -0.5F, 2.2F));

		PartDefinition TentaclePart_r27 = TentacleQuaternaryLU.addOrReplaceChild("TentaclePart_r27", CubeListBuilder.create().texOffs(24, 71).addBox(-6.45F, -1.0F, 9.375F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.9F, 1.7F, -8.2F, 0.4712F, 1.1868F, 0.4102F));

		PartDefinition TentaclePadLU = TentacleQuaternaryLU.addOrReplaceChild("TentaclePadLU", CubeListBuilder.create(), PartPose.offset(3.6F, -0.4F, 1.3F));

		PartDefinition TentaclePart_r28 = TentaclePadLU.addOrReplaceChild("TentaclePart_r28", CubeListBuilder.create().texOffs(84, 22).addBox(-8.075F, -1.5F, 16.4F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(44, 47).addBox(-8.075F, -2.5F, 12.4F, 2.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5F, 2.1F, -9.5F, 0.6807F, 1.2915F, 0.6283F));

		PartDefinition tenticlesright = partdefinition.addOrReplaceChild("tenticlesright", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition TentacleSecondaryRU = tenticlesright.addOrReplaceChild("TentacleSecondaryRU", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.0F, -24.0F, 0.3F, 0.0F, -1.6144F, 0.0F));

		PartDefinition TentaclePart_r29 = TentacleSecondaryRU.addOrReplaceChild("TentaclePart_r29", CubeListBuilder.create().texOffs(64, 37).addBox(0.0F, -1.0F, 3.55F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 0.5F, -3.3F, 0.2269F, -0.6981F, -0.1134F));

		PartDefinition TentacleTertiaryRU = TentacleSecondaryRU.addOrReplaceChild("TentacleTertiaryRU", CubeListBuilder.create(), PartPose.offset(-2.4F, -0.7F, 2.7F));

		PartDefinition TentaclePart_r30 = TentacleTertiaryRU.addOrReplaceChild("TentaclePart_r30", CubeListBuilder.create().texOffs(72, 61).addBox(1.8F, -1.0F, 6.75F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.9F, 1.2F, -6.0F, 0.2967F, -0.9425F, -0.2094F));

		PartDefinition TentacleQuaternaryRU = TentacleTertiaryRU.addOrReplaceChild("TentacleQuaternaryRU", CubeListBuilder.create(), PartPose.offset(-3.0F, -0.5F, 2.2F));

		PartDefinition TentaclePart_r31 = TentacleQuaternaryRU.addOrReplaceChild("TentaclePart_r31", CubeListBuilder.create().texOffs(72, 67).addBox(4.45F, -1.0F, 9.375F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.9F, 1.7F, -8.2F, 0.4712F, -1.1868F, -0.4102F));

		PartDefinition TentaclePadRU = TentacleQuaternaryRU.addOrReplaceChild("TentaclePadRU", CubeListBuilder.create(), PartPose.offset(-3.6F, -0.4F, 1.3F));

		PartDefinition TentaclePart_r32 = TentaclePadRU.addOrReplaceChild("TentaclePart_r32", CubeListBuilder.create().texOffs(84, 64).addBox(6.075F, -1.5F, 16.4F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(52, 37).addBox(6.075F, -2.5F, 12.4F, 2.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.5F, 2.1F, -9.5F, 0.6807F, -1.2915F, -0.6283F));

		PartDefinition TentacleSecondaryRU2 = tenticlesright.addOrReplaceChild("TentacleSecondaryRU2", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.0F, -21.0F, 0.3F, 0.0F, -1.4399F, 0.0F));

		PartDefinition TentaclePart_r33 = TentacleSecondaryRU2.addOrReplaceChild("TentaclePart_r33", CubeListBuilder.create().texOffs(60, 64).addBox(0.0F, -1.0F, 3.55F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 0.5F, -3.3F, 0.2269F, -0.6981F, -0.1134F));

		PartDefinition TentacleTertiaryRU2 = TentacleSecondaryRU2.addOrReplaceChild("TentacleTertiaryRU2", CubeListBuilder.create(), PartPose.offset(-2.4F, -0.7F, 2.7F));

		PartDefinition TentaclePart_r34 = TentacleTertiaryRU2.addOrReplaceChild("TentaclePart_r34", CubeListBuilder.create().texOffs(72, 73).addBox(1.8F, -1.0F, 6.75F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.9F, 1.2F, -6.0F, 0.2967F, -0.9425F, -0.2094F));

		PartDefinition TentacleQuaternaryRU2 = TentacleTertiaryRU2.addOrReplaceChild("TentacleQuaternaryRU2", CubeListBuilder.create(), PartPose.offset(-3.0F, -0.5F, 2.2F));

		PartDefinition TentaclePart_r35 = TentacleQuaternaryRU2.addOrReplaceChild("TentaclePart_r35", CubeListBuilder.create().texOffs(74, 0).addBox(4.45F, -1.0F, 9.375F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.9F, 1.7F, -8.2F, 0.4712F, -1.1868F, -0.4102F));

		PartDefinition TentaclePadRU2 = TentacleQuaternaryRU2.addOrReplaceChild("TentaclePadRU2", CubeListBuilder.create(), PartPose.offset(-3.6F, -0.4F, 1.3F));

		PartDefinition TentaclePart_r36 = TentaclePadRU2.addOrReplaceChild("TentaclePart_r36", CubeListBuilder.create().texOffs(84, 69).addBox(6.075F, -1.5F, 16.4F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 55).addBox(6.075F, -2.5F, 12.4F, 2.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.5F, 2.1F, -9.5F, 0.6807F, -1.2915F, -0.6283F));

		PartDefinition TentacleSecondaryRU3 = tenticlesright.addOrReplaceChild("TentacleSecondaryRU3", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.0F, -17.0F, 0.3F, 0.0F, -1.5272F, 0.0F));

		PartDefinition TentaclePart_r37 = TentacleSecondaryRU3.addOrReplaceChild("TentaclePart_r37", CubeListBuilder.create().texOffs(12, 65).addBox(0.0F, -1.0F, 3.55F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 0.5F, -3.3F, 0.2269F, -0.6981F, -0.1134F));

		PartDefinition TentacleTertiaryRU3 = TentacleSecondaryRU3.addOrReplaceChild("TentacleTertiaryRU3", CubeListBuilder.create(), PartPose.offset(-2.4F, -0.7F, 2.7F));

		PartDefinition TentaclePart_r38 = TentacleTertiaryRU3.addOrReplaceChild("TentaclePart_r38", CubeListBuilder.create().texOffs(74, 6).addBox(1.8F, -1.0F, 6.75F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.9F, 1.2F, -6.0F, 0.2967F, -0.9425F, -0.2094F));

		PartDefinition TentacleQuaternaryRU3 = TentacleTertiaryRU3.addOrReplaceChild("TentacleQuaternaryRU3", CubeListBuilder.create(), PartPose.offset(-3.0F, -0.5F, 2.2F));

		PartDefinition TentaclePart_r39 = TentacleQuaternaryRU3.addOrReplaceChild("TentaclePart_r39", CubeListBuilder.create().texOffs(74, 12).addBox(4.45F, -1.0F, 9.375F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.9F, 1.7F, -8.2F, 0.4712F, -1.1868F, -0.4102F));

		PartDefinition TentaclePadRU3 = TentacleQuaternaryRU3.addOrReplaceChild("TentaclePadRU3", CubeListBuilder.create(), PartPose.offset(-3.6F, -0.4F, 1.3F));

		PartDefinition TentaclePart_r40 = TentaclePadRU3.addOrReplaceChild("TentaclePart_r40", CubeListBuilder.create().texOffs(84, 74).addBox(6.075F, -1.5F, 16.4F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(12, 56).addBox(6.075F, -2.5F, 12.4F, 2.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.5F, 2.1F, -9.5F, 0.6807F, -1.2915F, -0.6283F));

		PartDefinition TentacleSecondaryRU4 = tenticlesright.addOrReplaceChild("TentacleSecondaryRU4", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, -14.0F, 1.3F, 0.0F, -1.7017F, 0.0F));

		PartDefinition TentaclePart_r41 = TentacleSecondaryRU4.addOrReplaceChild("TentaclePart_r41", CubeListBuilder.create().texOffs(24, 65).addBox(0.0F, -1.0F, 3.55F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 0.5F, -3.3F, 0.2269F, -0.6981F, -0.1134F));

		PartDefinition TentacleTertiaryRU4 = TentacleSecondaryRU4.addOrReplaceChild("TentacleTertiaryRU4", CubeListBuilder.create(), PartPose.offset(-2.4F, -0.7F, 2.7F));

		PartDefinition TentaclePart_r42 = TentacleTertiaryRU4.addOrReplaceChild("TentaclePart_r42", CubeListBuilder.create().texOffs(0, 76).addBox(1.8F, -1.0F, 6.75F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.9F, 1.2F, -6.0F, 0.2967F, -0.9425F, -0.2094F));

		PartDefinition TentacleQuaternaryRU4 = TentacleTertiaryRU4.addOrReplaceChild("TentacleQuaternaryRU4", CubeListBuilder.create(), PartPose.offset(-3.0F, -0.5F, 2.2F));

		PartDefinition TentaclePart_r43 = TentacleQuaternaryRU4.addOrReplaceChild("TentaclePart_r43", CubeListBuilder.create().texOffs(76, 36).addBox(4.45F, -1.0F, 9.375F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.9F, 1.7F, -8.2F, 0.4712F, -1.1868F, -0.4102F));

		PartDefinition TentaclePadRU4 = TentacleQuaternaryRU4.addOrReplaceChild("TentaclePadRU4", CubeListBuilder.create(), PartPose.offset(-3.6F, -0.4F, 1.3F));

		PartDefinition TentaclePart_r44 = TentaclePadRU4.addOrReplaceChild("TentaclePart_r44", CubeListBuilder.create().texOffs(84, 79).addBox(6.075F, -1.5F, 16.4F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(24, 56).addBox(6.075F, -2.5F, 12.4F, 2.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.5F, 2.1F, -9.5F, 0.6807F, -1.2915F, -0.6283F));

		PartDefinition TentacleSecondaryRU5 = tenticlesright.addOrReplaceChild("TentacleSecondaryRU5", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.0F, -19.0F, 0.3F, 0.0F, -1.2654F, 0.0F));

		PartDefinition TentaclePart_r45 = TentacleSecondaryRU5.addOrReplaceChild("TentaclePart_r45", CubeListBuilder.create().texOffs(36, 65).addBox(0.0F, -1.0F, 3.55F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 0.5F, -3.3F, 0.2269F, -0.6981F, -0.1134F));

		PartDefinition TentacleTertiaryRU5 = TentacleSecondaryRU5.addOrReplaceChild("TentacleTertiaryRU5", CubeListBuilder.create(), PartPose.offset(-2.4F, -0.7F, 2.7F));

		PartDefinition TentaclePart_r46 = TentacleTertiaryRU5.addOrReplaceChild("TentaclePart_r46", CubeListBuilder.create().texOffs(60, 76).addBox(1.8F, -1.0F, 6.75F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.9F, 1.2F, -6.0F, 0.2967F, -0.9425F, -0.2094F));

		PartDefinition TentacleQuaternaryRU5 = TentacleTertiaryRU5.addOrReplaceChild("TentacleQuaternaryRU5", CubeListBuilder.create(), PartPose.offset(-3.0F, -0.5F, 2.2F));

		PartDefinition TentaclePart_r47 = TentacleQuaternaryRU5.addOrReplaceChild("TentaclePart_r47", CubeListBuilder.create().texOffs(12, 77).addBox(4.45F, -1.0F, 9.375F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.9F, 1.7F, -8.2F, 0.4712F, -1.1868F, -0.4102F));

		PartDefinition TentaclePadRU5 = TentacleQuaternaryRU5.addOrReplaceChild("TentaclePadRU5", CubeListBuilder.create(), PartPose.offset(-3.6F, -0.4F, 1.3F));

		PartDefinition TentaclePart_r48 = TentaclePadRU5.addOrReplaceChild("TentaclePart_r48", CubeListBuilder.create().texOffs(84, 84).addBox(6.075F, -1.5F, 16.4F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 56).addBox(6.075F, -2.5F, 12.4F, 2.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.5F, 2.1F, -9.5F, 0.6807F, -1.2915F, -0.6283F));

		PartDefinition TentacleSecondaryRU6 = tenticlesright.addOrReplaceChild("TentacleSecondaryRU6", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.0F, -12.0F, 0.3F, 0.0F, -1.2654F, 0.0F));

		PartDefinition TentaclePart_r49 = TentacleSecondaryRU6.addOrReplaceChild("TentaclePart_r49", CubeListBuilder.create().texOffs(68, 49).addBox(0.0F, -1.0F, 3.55F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 0.5F, -3.3F, 0.2269F, -0.6981F, -0.1134F));

		PartDefinition TentacleTertiaryRU6 = TentacleSecondaryRU6.addOrReplaceChild("TentacleTertiaryRU6", CubeListBuilder.create(), PartPose.offset(-2.4F, -0.7F, 2.7F));

		PartDefinition TentaclePart_r50 = TentacleTertiaryRU6.addOrReplaceChild("TentaclePart_r50", CubeListBuilder.create().texOffs(80, 42).addBox(1.8F, -1.0F, 6.75F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.9F, 1.2F, -6.0F, 0.2967F, -0.9425F, -0.2094F));

		PartDefinition TentacleQuaternaryRU6 = TentacleTertiaryRU6.addOrReplaceChild("TentacleQuaternaryRU6", CubeListBuilder.create(), PartPose.offset(-3.0F, -0.5F, 2.2F));

		PartDefinition TentaclePart_r51 = TentacleQuaternaryRU6.addOrReplaceChild("TentaclePart_r51", CubeListBuilder.create().texOffs(80, 48).addBox(4.45F, -1.0F, 9.375F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.9F, 1.7F, -8.2F, 0.4712F, -1.1868F, -0.4102F));

		PartDefinition TentaclePadRU6 = TentacleQuaternaryRU6.addOrReplaceChild("TentaclePadRU6", CubeListBuilder.create(), PartPose.offset(-3.6F, -0.4F, 1.3F));

		PartDefinition TentaclePart_r52 = TentaclePadRU6.addOrReplaceChild("TentaclePart_r52", CubeListBuilder.create().texOffs(86, 5).addBox(6.075F, -1.5F, 16.4F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(60, 19).addBox(6.075F, -2.5F, 12.4F, 2.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.5F, 2.1F, -9.5F, 0.6807F, -1.2915F, -0.6283F));

		PartDefinition TentacleSecondaryRU7 = tenticlesright.addOrReplaceChild("TentacleSecondaryRU7", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.0F, -22.0F, 0.3F, 0.0F, -1.2654F, 0.0F));

		PartDefinition TentaclePart_r53 = TentacleSecondaryRU7.addOrReplaceChild("TentaclePart_r53", CubeListBuilder.create().texOffs(0, 70).addBox(0.0F, -1.0F, 3.55F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 0.5F, -3.3F, 0.2269F, -0.6981F, -0.1134F));

		PartDefinition TentacleTertiaryRU7 = TentacleSecondaryRU7.addOrReplaceChild("TentacleTertiaryRU7", CubeListBuilder.create(), PartPose.offset(-2.4F, -0.7F, 2.7F));

		PartDefinition TentaclePart_r54 = TentacleTertiaryRU7.addOrReplaceChild("TentaclePart_r54", CubeListBuilder.create().texOffs(0, 82).addBox(1.8F, -1.0F, 6.75F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.9F, 1.2F, -6.0F, 0.2967F, -0.9425F, -0.2094F));

		PartDefinition TentacleQuaternaryRU7 = TentacleTertiaryRU7.addOrReplaceChild("TentacleQuaternaryRU7", CubeListBuilder.create(), PartPose.offset(-3.0F, -0.5F, 2.2F));

		PartDefinition TentaclePart_r55 = TentacleQuaternaryRU7.addOrReplaceChild("TentaclePart_r55", CubeListBuilder.create().texOffs(60, 82).addBox(4.45F, -1.0F, 9.375F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.9F, 1.7F, -8.2F, 0.4712F, -1.1868F, -0.4102F));

		PartDefinition TentaclePadRU7 = TentacleQuaternaryRU7.addOrReplaceChild("TentaclePadRU7", CubeListBuilder.create(), PartPose.offset(-3.6F, -0.4F, 1.3F));

		PartDefinition TentaclePart_r56 = TentaclePadRU7.addOrReplaceChild("TentaclePart_r56", CubeListBuilder.create().texOffs(86, 10).addBox(6.075F, -1.5F, 16.4F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(60, 28).addBox(6.075F, -2.5F, 12.4F, 2.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.5F, 2.1F, -9.5F, 0.6807F, -1.2915F, -0.6283F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

		this.animateWalk(LatexTenticleLimbsMutantAnimations.LATEX_TENTICLE_LIMBS_MUTANT_WALK, limbSwing, limbSwingAmount, 2f, 2f);
		if (entity instanceof LatexTenticleLimbsMutantEntity tentacleEntity) {
			this.animate(tentacleEntity.idleAnimationState, LatexTenticleLimbsMutantAnimations.LATEX_TENTICLE_LIMBS_MUTANT_IDLE, ageInTicks, 1.5f);
			this.animate(tentacleEntity.attackAnimationState, LatexTenticleLimbsMutantAnimations.LATEX_TENTICLE_LIMBS_MUTANT_ATTACK, ageInTicks, 2f);
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
		RightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		LeftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Torso.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		tenticlesleft.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		tenticlesright.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

    @Override
    public ModelPart root() {
        return root;
    }
}