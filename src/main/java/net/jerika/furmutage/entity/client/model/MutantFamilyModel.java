package net.jerika.furmutage.entity.client.model;// Made with Blockbench 5.0.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jerika.furmutage.entity.animations.mutantfamilyAnimations;
import net.jerika.furmutage.entity.custom.LatexMutantFamilyEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class MutantFamilyModel<T extends LatexMutantFamilyEntity> extends HierarchicalModel<T> {
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart heads;
	private final ModelPart Head11;
	private final ModelPart RightEar11;
	private final ModelPart RightEarPivot11;
	private final ModelPart LeftEar11;
	private final ModelPart LeftEarPivot11;
	private final ModelPart Hair11;
	private final ModelPart Head10;
	private final ModelPart RightEar10;
	private final ModelPart RightEarPivot10;
	private final ModelPart LeftEar10;
	private final ModelPart LeftEarPivot10;
	private final ModelPart Hair10;
	private final ModelPart Head9;
	private final ModelPart RightEar9;
	private final ModelPart RightEarPivot9;
	private final ModelPart LeftEar9;
	private final ModelPart LeftEarPivot9;
	private final ModelPart Hair9;
	private final ModelPart Head8;
	private final ModelPart RightEar8;
	private final ModelPart RightEarPivot8;
	private final ModelPart LeftEar8;
	private final ModelPart LeftEarPivot8;
	private final ModelPart Hair8;
	private final ModelPart Head7;
	private final ModelPart RightEar7;
	private final ModelPart RightEarPivot7;
	private final ModelPart LeftEar7;
	private final ModelPart LeftEarPivot7;
	private final ModelPart Hair7;
	private final ModelPart Head6;
	private final ModelPart RightEar6;
	private final ModelPart RightEarPivot6;
	private final ModelPart LeftEar6;
	private final ModelPart LeftEarPivot6;
	private final ModelPart Hair6;
	private final ModelPart Head5;
	private final ModelPart RightEar5;
	private final ModelPart RightEarPivot5;
	private final ModelPart LeftEar5;
	private final ModelPart LeftEarPivot5;
	private final ModelPart Hair5;
	private final ModelPart Head4;
	private final ModelPart RightEar4;
	private final ModelPart RightEarPivot4;
	private final ModelPart LeftEar4;
	private final ModelPart LeftEarPivot4;
	private final ModelPart Hair4;
	private final ModelPart Head;
	private final ModelPart RightEar;
	private final ModelPart RightEarPivot;
	private final ModelPart LeftEar;
	private final ModelPart LeftEarPivot;
	private final ModelPart Hair;
	private final ModelPart Head2;
	private final ModelPart RightEar2;
	private final ModelPart RightEarPivot2;
	private final ModelPart LeftEar2;
	private final ModelPart LeftEarPivot2;
	private final ModelPart Hair2;
	private final ModelPart Head3;
	private final ModelPart RightEar3;
	private final ModelPart RightEarPivot3;
	private final ModelPart LeftEar3;
	private final ModelPart LeftEarPivot3;
	private final ModelPart Hair3;
	private final ModelPart headbody;
	private final ModelPart Torso;
	private final ModelPart LeftArm;
	private final ModelPart RightArm2;
	private final ModelPart RightArm;
	private final ModelPart Tail;
	private final ModelPart TailPrimary;
	private final ModelPart TailSecondary;
	private final ModelPart TailTertiary;
	private final ModelPart Tail2;
	private final ModelPart TailPrimary2;
	private final ModelPart TailSecondary2;
	private final ModelPart TailTertiary2;
	private final ModelPart Tail3;
	private final ModelPart TailPrimary3;
	private final ModelPart TailSecondary3;
	private final ModelPart TailTertiary3;
	private final ModelPart legs;
	private final ModelPart rightleg3;
	private final ModelPart RightLeg;
	private final ModelPart bpne;
	private final ModelPart RightFoot;
	private final ModelPart RightPad;
	private final ModelPart RightLeg2;
	private final ModelPart RightLowerLeg2;
	private final ModelPart RightFoot2;
	private final ModelPart RightPad2;
	private final ModelPart leftlegss;
	private final ModelPart LeftLeg;
	private final ModelPart LeftLowerLeg;
	private final ModelPart LeftFoot;
	private final ModelPart LeftPad;
	private final ModelPart LeftLeg2;
	private final ModelPart LeftLowerLeg2;
	private final ModelPart LeftFoot2;
	private final ModelPart LeftPad2;

	public MutantFamilyModel(ModelPart root) {
		this.root = root;
		this.body = root.getChild("body");
		this.heads = this.body.getChild("heads");
		this.Head11 = this.heads.getChild("Head11");
		this.RightEar11 = this.Head11.getChild("RightEar11");
		this.RightEarPivot11 = this.RightEar11.getChild("RightEarPivot11");
		this.LeftEar11 = this.Head11.getChild("LeftEar11");
		this.LeftEarPivot11 = this.LeftEar11.getChild("LeftEarPivot11");
		this.Hair11 = this.Head11.getChild("Hair11");
		this.Head10 = this.heads.getChild("Head10");
		this.RightEar10 = this.Head10.getChild("RightEar10");
		this.RightEarPivot10 = this.RightEar10.getChild("RightEarPivot10");
		this.LeftEar10 = this.Head10.getChild("LeftEar10");
		this.LeftEarPivot10 = this.LeftEar10.getChild("LeftEarPivot10");
		this.Hair10 = this.Head10.getChild("Hair10");
		this.Head9 = this.heads.getChild("Head9");
		this.RightEar9 = this.Head9.getChild("RightEar9");
		this.RightEarPivot9 = this.RightEar9.getChild("RightEarPivot9");
		this.LeftEar9 = this.Head9.getChild("LeftEar9");
		this.LeftEarPivot9 = this.LeftEar9.getChild("LeftEarPivot9");
		this.Hair9 = this.Head9.getChild("Hair9");
		this.Head8 = this.heads.getChild("Head8");
		this.RightEar8 = this.Head8.getChild("RightEar8");
		this.RightEarPivot8 = this.RightEar8.getChild("RightEarPivot8");
		this.LeftEar8 = this.Head8.getChild("LeftEar8");
		this.LeftEarPivot8 = this.LeftEar8.getChild("LeftEarPivot8");
		this.Hair8 = this.Head8.getChild("Hair8");
		this.Head7 = this.heads.getChild("Head7");
		this.RightEar7 = this.Head7.getChild("RightEar7");
		this.RightEarPivot7 = this.RightEar7.getChild("RightEarPivot7");
		this.LeftEar7 = this.Head7.getChild("LeftEar7");
		this.LeftEarPivot7 = this.LeftEar7.getChild("LeftEarPivot7");
		this.Hair7 = this.Head7.getChild("Hair7");
		this.Head6 = this.heads.getChild("Head6");
		this.RightEar6 = this.Head6.getChild("RightEar6");
		this.RightEarPivot6 = this.RightEar6.getChild("RightEarPivot6");
		this.LeftEar6 = this.Head6.getChild("LeftEar6");
		this.LeftEarPivot6 = this.LeftEar6.getChild("LeftEarPivot6");
		this.Hair6 = this.Head6.getChild("Hair6");
		this.Head5 = this.heads.getChild("Head5");
		this.RightEar5 = this.Head5.getChild("RightEar5");
		this.RightEarPivot5 = this.RightEar5.getChild("RightEarPivot5");
		this.LeftEar5 = this.Head5.getChild("LeftEar5");
		this.LeftEarPivot5 = this.LeftEar5.getChild("LeftEarPivot5");
		this.Hair5 = this.Head5.getChild("Hair5");
		this.Head4 = this.heads.getChild("Head4");
		this.RightEar4 = this.Head4.getChild("RightEar4");
		this.RightEarPivot4 = this.RightEar4.getChild("RightEarPivot4");
		this.LeftEar4 = this.Head4.getChild("LeftEar4");
		this.LeftEarPivot4 = this.LeftEar4.getChild("LeftEarPivot4");
		this.Hair4 = this.Head4.getChild("Hair4");
		this.Head = this.heads.getChild("Head");
		this.RightEar = this.Head.getChild("RightEar");
		this.RightEarPivot = this.RightEar.getChild("RightEarPivot");
		this.LeftEar = this.Head.getChild("LeftEar");
		this.LeftEarPivot = this.LeftEar.getChild("LeftEarPivot");
		this.Hair = this.Head.getChild("Hair");
		this.Head2 = this.heads.getChild("Head2");
		this.RightEar2 = this.Head2.getChild("RightEar2");
		this.RightEarPivot2 = this.RightEar2.getChild("RightEarPivot2");
		this.LeftEar2 = this.Head2.getChild("LeftEar2");
		this.LeftEarPivot2 = this.LeftEar2.getChild("LeftEarPivot2");
		this.Hair2 = this.Head2.getChild("Hair2");
		this.Head3 = this.heads.getChild("Head3");
		this.RightEar3 = this.Head3.getChild("RightEar3");
		this.RightEarPivot3 = this.RightEar3.getChild("RightEarPivot3");
		this.LeftEar3 = this.Head3.getChild("LeftEar3");
		this.LeftEarPivot3 = this.LeftEar3.getChild("LeftEarPivot3");
		this.Hair3 = this.Head3.getChild("Hair3");
		this.headbody = this.heads.getChild("headbody");
		this.Torso = this.body.getChild("Torso");
		this.LeftArm = this.Torso.getChild("LeftArm");
		this.RightArm2 = this.Torso.getChild("RightArm2");
		this.RightArm = this.Torso.getChild("RightArm");
		this.Tail = this.Torso.getChild("Tail");
		this.TailPrimary = this.Tail.getChild("TailPrimary");
		this.TailSecondary = this.TailPrimary.getChild("TailSecondary");
		this.TailTertiary = this.TailSecondary.getChild("TailTertiary");
		this.Tail2 = this.Tail.getChild("Tail2");
		this.TailPrimary2 = this.Tail2.getChild("TailPrimary2");
		this.TailSecondary2 = this.TailPrimary2.getChild("TailSecondary2");
		this.TailTertiary2 = this.TailSecondary2.getChild("TailTertiary2");
		this.Tail3 = this.Tail.getChild("Tail3");
		this.TailPrimary3 = this.Tail3.getChild("TailPrimary3");
		this.TailSecondary3 = this.TailPrimary3.getChild("TailSecondary3");
		this.TailTertiary3 = this.TailSecondary3.getChild("TailTertiary3");
		this.legs = root.getChild("legs");
		this.rightleg3 = this.legs.getChild("rightleg3");
		this.RightLeg = this.rightleg3.getChild("RightLeg");
		this.bpne = this.RightLeg.getChild("bpne");
		this.RightFoot = this.bpne.getChild("RightFoot");
		this.RightPad = this.RightFoot.getChild("RightPad");
		this.RightLeg2 = this.rightleg3.getChild("RightLeg2");
		this.RightLowerLeg2 = this.RightLeg2.getChild("RightLowerLeg2");
		this.RightFoot2 = this.RightLowerLeg2.getChild("RightFoot2");
		this.RightPad2 = this.RightFoot2.getChild("RightPad2");
		this.leftlegss = this.legs.getChild("leftlegss");
		this.LeftLeg = this.leftlegss.getChild("LeftLeg");
		this.LeftLowerLeg = this.LeftLeg.getChild("LeftLowerLeg");
		this.LeftFoot = this.LeftLowerLeg.getChild("LeftFoot");
		this.LeftPad = this.LeftFoot.getChild("LeftPad");
		this.LeftLeg2 = this.leftlegss.getChild("LeftLeg2");
		this.LeftLowerLeg2 = this.LeftLeg2.getChild("LeftLowerLeg2");
		this.LeftFoot2 = this.LeftLowerLeg2.getChild("LeftFoot2");
		this.LeftPad2 = this.LeftFoot2.getChild("LeftPad2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 18.0F, 0.0F));

        PartDefinition heads = body.addOrReplaceChild("heads", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition Head11 = heads.addOrReplaceChild("Head11", CubeListBuilder.create().texOffs(54, 30).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(16, 133).addBox(-2.0F, -3.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(62, 28).addBox(-1.5F, -1.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -35.5F, -5.0F, 0.0F, 1.2217F, 0.0F));

        PartDefinition RightEar11 = Head11.addOrReplaceChild("RightEar11", CubeListBuilder.create(), PartPose.offset(-3.0F, -7.5F, 0.0F));

        PartDefinition RightEarPivot11 = RightEar11.addOrReplaceChild("RightEarPivot11", CubeListBuilder.create().texOffs(86, 48).addBox(-1.9F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(158, 8).addBox(-0.9F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(158, 159).addBox(-0.9F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(50, 55).addBox(0.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

        PartDefinition LeftEar11 = Head11.addOrReplaceChild("LeftEar11", CubeListBuilder.create(), PartPose.offset(3.0F, -7.5F, 0.0F));

        PartDefinition LeftEarPivot11 = LeftEar11.addOrReplaceChild("LeftEarPivot11", CubeListBuilder.create().texOffs(86, 52).addBox(-1.1F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(32, 159).addBox(-1.1F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(16, 160).addBox(-1.1F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(50, 57).addBox(-1.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

        PartDefinition Hair11 = Head11.addOrReplaceChild("Hair11", CubeListBuilder.create().texOffs(54, 46).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F))
                .texOffs(96, 112).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Head10 = heads.addOrReplaceChild("Head10", CubeListBuilder.create().texOffs(0, 55).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(116, 140).addBox(-2.0F, -3.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(70, 28).addBox(-1.5F, -1.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, -36.5F, 4.0F, 2.7925F, -0.829F, 3.1416F));

        PartDefinition RightEar10 = Head10.addOrReplaceChild("RightEar10", CubeListBuilder.create(), PartPose.offset(-3.0F, -7.5F, 0.0F));

        PartDefinition RightEarPivot10 = RightEar10.addOrReplaceChild("RightEarPivot10", CubeListBuilder.create().texOffs(86, 56).addBox(-1.9F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(38, 159).addBox(-0.9F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(22, 160).addBox(-0.9F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(50, 59).addBox(0.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

        PartDefinition LeftEar10 = Head10.addOrReplaceChild("LeftEar10", CubeListBuilder.create(), PartPose.offset(3.0F, -7.5F, 0.0F));

        PartDefinition LeftEarPivot10 = LeftEar10.addOrReplaceChild("LeftEarPivot10", CubeListBuilder.create().texOffs(118, 44).addBox(-1.1F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(44, 159).addBox(-1.1F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(160, 43).addBox(-1.1F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(82, 16).addBox(-1.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

        PartDefinition Hair10 = Head10.addOrReplaceChild("Hair10", CubeListBuilder.create().texOffs(62, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F))
                .texOffs(118, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Head9 = heads.addOrReplaceChild("Head9", CubeListBuilder.create().texOffs(32, 62).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(116, 144).addBox(-2.0F, -3.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(78, 28).addBox(-1.5F, -1.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, -35.5F, -8.0F, 0.0F, -1.0908F, 0.0F));

        PartDefinition RightEar9 = Head9.addOrReplaceChild("RightEar9", CubeListBuilder.create(), PartPose.offset(-3.0F, -7.5F, 0.0F));

        PartDefinition RightEarPivot9 = RightEar9.addOrReplaceChild("RightEarPivot9", CubeListBuilder.create().texOffs(140, 151).addBox(-1.9F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(50, 159).addBox(-0.9F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(160, 45).addBox(-0.9F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(82, 18).addBox(0.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

        PartDefinition LeftEar9 = Head9.addOrReplaceChild("LeftEar9", CubeListBuilder.create(), PartPose.offset(3.0F, -7.5F, 0.0F));

        PartDefinition LeftEarPivot9 = LeftEar9.addOrReplaceChild("LeftEarPivot9", CubeListBuilder.create().texOffs(28, 152).addBox(-1.1F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(56, 159).addBox(-1.1F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(160, 47).addBox(-1.1F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(82, 20).addBox(-1.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

        PartDefinition Hair9 = Head9.addOrReplaceChild("Hair9", CubeListBuilder.create().texOffs(64, 62).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F))
                .texOffs(118, 30).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Head8 = heads.addOrReplaceChild("Head8", CubeListBuilder.create().texOffs(0, 71).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(150, 39).addBox(-2.0F, -4.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(86, 60).addBox(-1.5F, -2.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -35.5F, 6.0F, 2.6616F, 0.829F, 3.1416F));

        PartDefinition RightEar8 = Head8.addOrReplaceChild("RightEar8", CubeListBuilder.create(), PartPose.offset(-3.0F, -7.5F, 0.0F));

        PartDefinition RightEarPivot8 = RightEar8.addOrReplaceChild("RightEarPivot8", CubeListBuilder.create().texOffs(36, 155).addBox(-1.9F, -2.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(62, 159).addBox(-0.9F, -2.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(160, 49).addBox(-0.9F, -3.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(82, 22).addBox(0.1F, -4.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

        PartDefinition LeftEar8 = Head8.addOrReplaceChild("LeftEar8", CubeListBuilder.create(), PartPose.offset(3.0F, -7.5F, 0.0F));

        PartDefinition LeftEarPivot8 = LeftEar8.addOrReplaceChild("LeftEarPivot8", CubeListBuilder.create().texOffs(44, 155).addBox(-1.1F, -2.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(68, 159).addBox(-1.1F, -2.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(160, 51).addBox(-1.1F, -3.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(82, 24).addBox(-1.1F, -4.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

        PartDefinition Hair8 = Head8.addOrReplaceChild("Hair8", CubeListBuilder.create().texOffs(32, 78).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F))
                .texOffs(0, 119).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Head7 = heads.addOrReplaceChild("Head7", CubeListBuilder.create().texOffs(64, 78).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(36, 151).addBox(-2.0F, -4.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(126, 14).addBox(-1.5F, -2.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -24.5F, 4.0F, -2.8362F, 0.9599F, 3.1416F));

        PartDefinition RightEar7 = Head7.addOrReplaceChild("RightEar7", CubeListBuilder.create(), PartPose.offset(-3.0F, -7.5F, 0.0F));

        PartDefinition RightEarPivot7 = RightEar7.addOrReplaceChild("RightEarPivot7", CubeListBuilder.create().texOffs(52, 155).addBox(-1.9F, -2.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(74, 159).addBox(-0.9F, -2.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(160, 53).addBox(-0.9F, -3.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(82, 26).addBox(0.1F, -4.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

        PartDefinition LeftEar7 = Head7.addOrReplaceChild("LeftEar7", CubeListBuilder.create(), PartPose.offset(3.0F, -7.5F, 0.0F));

        PartDefinition LeftEarPivot7 = LeftEar7.addOrReplaceChild("LeftEarPivot7", CubeListBuilder.create().texOffs(60, 155).addBox(-1.1F, -2.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(80, 159).addBox(-1.1F, -2.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(160, 55).addBox(-1.1F, -3.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(28, 133).addBox(-1.1F, -4.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

        PartDefinition Hair7 = Head7.addOrReplaceChild("Hair7", CubeListBuilder.create().texOffs(86, 16).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F))
                .texOffs(126, 0).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Head6 = heads.addOrReplaceChild("Head6", CubeListBuilder.create().texOffs(86, 32).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(48, 151).addBox(-2.0F, -4.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(134, 14).addBox(-1.5F, -2.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, -23.5F, 4.0F, -2.8362F, -1.2217F, 3.1416F));

        PartDefinition RightEar6 = Head6.addOrReplaceChild("RightEar6", CubeListBuilder.create(), PartPose.offset(-3.0F, -7.5F, 0.0F));

        PartDefinition RightEarPivot6 = RightEar6.addOrReplaceChild("RightEarPivot6", CubeListBuilder.create().texOffs(68, 155).addBox(-1.9F, -2.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(86, 159).addBox(-0.9F, -2.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(160, 57).addBox(-0.9F, -3.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(28, 135).addBox(0.1F, -4.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

        PartDefinition LeftEar6 = Head6.addOrReplaceChild("LeftEar6", CubeListBuilder.create(), PartPose.offset(3.0F, -7.5F, 0.0F));

        PartDefinition LeftEarPivot6 = LeftEar6.addOrReplaceChild("LeftEarPivot6", CubeListBuilder.create().texOffs(76, 155).addBox(-1.1F, -2.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(92, 159).addBox(-1.1F, -2.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(160, 59).addBox(-1.1F, -3.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(144, 136).addBox(-1.1F, -4.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

        PartDefinition Hair6 = Head6.addOrReplaceChild("Hair6", CubeListBuilder.create().texOffs(0, 87).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F))
                .texOffs(32, 126).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Head5 = heads.addOrReplaceChild("Head5", CubeListBuilder.create().texOffs(94, 0).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(60, 151).addBox(-2.0F, -4.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(16, 137).addBox(-1.5F, -2.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -24.5F, 7.0F, -2.8798F, -0.0436F, 3.1416F));

        PartDefinition RightEar5 = Head5.addOrReplaceChild("RightEar5", CubeListBuilder.create(), PartPose.offset(-3.0F, -7.5F, 0.0F));

        PartDefinition RightEarPivot5 = RightEar5.addOrReplaceChild("RightEarPivot5", CubeListBuilder.create().texOffs(84, 155).addBox(-1.9F, -2.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(98, 159).addBox(-0.9F, -2.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(160, 61).addBox(-0.9F, -3.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(124, 148).addBox(0.1F, -4.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

        PartDefinition LeftEar5 = Head5.addOrReplaceChild("LeftEar5", CubeListBuilder.create(), PartPose.offset(3.0F, -7.5F, 0.0F));

        PartDefinition LeftEarPivot5 = LeftEar5.addOrReplaceChild("LeftEarPivot5", CubeListBuilder.create().texOffs(92, 155).addBox(-1.1F, -2.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(104, 159).addBox(-1.1F, -2.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(160, 63).addBox(-1.1F, -3.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(96, 151).addBox(-1.1F, -4.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

        PartDefinition Hair5 = Head5.addOrReplaceChild("Hair5", CubeListBuilder.create().texOffs(32, 94).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F))
                .texOffs(64, 126).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Head4 = heads.addOrReplaceChild("Head4", CubeListBuilder.create().texOffs(64, 94).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(72, 151).addBox(-2.0F, -4.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(24, 137).addBox(-1.5F, -2.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -22.5F, -11.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition RightEar4 = Head4.addOrReplaceChild("RightEar4", CubeListBuilder.create(), PartPose.offset(-3.0F, -7.5F, 0.0F));

        PartDefinition RightEarPivot4 = RightEar4.addOrReplaceChild("RightEarPivot4", CubeListBuilder.create().texOffs(128, 155).addBox(-1.9F, -2.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(110, 159).addBox(-0.9F, -2.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(160, 65).addBox(-0.9F, -3.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(96, 153).addBox(0.1F, -4.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

        PartDefinition LeftEar4 = Head4.addOrReplaceChild("LeftEar4", CubeListBuilder.create(), PartPose.offset(3.0F, -7.5F, 0.0F));

        PartDefinition LeftEarPivot4 = LeftEar4.addOrReplaceChild("LeftEarPivot4", CubeListBuilder.create().texOffs(136, 155).addBox(-1.1F, -2.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(116, 159).addBox(-1.1F, -2.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(160, 67).addBox(-1.1F, -3.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(144, 155).addBox(-1.1F, -4.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

        PartDefinition Hair4 = Head4.addOrReplaceChild("Hair4", CubeListBuilder.create().texOffs(96, 48).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F))
                .texOffs(96, 126).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Head = heads.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(96, 64).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(84, 151).addBox(-2.0F, -4.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(142, 14).addBox(-1.5F, -2.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -23.5F, -9.0F, 0.2618F, 1.0908F, 0.0F));

        PartDefinition RightEar = Head.addOrReplaceChild("RightEar", CubeListBuilder.create(), PartPose.offset(-3.0F, -7.5F, 0.0F));

        PartDefinition RightEarPivot = RightEar.addOrReplaceChild("RightEarPivot", CubeListBuilder.create().texOffs(16, 156).addBox(-1.9F, -2.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(122, 159).addBox(-0.9F, -2.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(160, 69).addBox(-0.9F, -3.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(32, 156).addBox(0.1F, -4.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

        PartDefinition LeftEar = Head.addOrReplaceChild("LeftEar", CubeListBuilder.create(), PartPose.offset(3.0F, -7.5F, 0.0F));

        PartDefinition LeftEarPivot = LeftEar.addOrReplaceChild("LeftEarPivot", CubeListBuilder.create().texOffs(24, 156).addBox(-1.1F, -2.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(128, 159).addBox(-1.1F, -2.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(160, 71).addBox(-1.1F, -3.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(144, 157).addBox(-1.1F, -4.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

        PartDefinition Hair = Head.addOrReplaceChild("Hair", CubeListBuilder.create().texOffs(96, 80).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F))
                .texOffs(128, 44).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Head2 = heads.addOrReplaceChild("Head2", CubeListBuilder.create().texOffs(96, 96).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(128, 151).addBox(-2.0F, -4.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(116, 148).addBox(-1.5F, -2.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -36.5F, -8.0F));

        PartDefinition RightEar2 = Head2.addOrReplaceChild("RightEar2", CubeListBuilder.create(), PartPose.offset(-3.0F, -7.5F, 0.0F));

        PartDefinition RightEarPivot2 = RightEar2.addOrReplaceChild("RightEarPivot2", CubeListBuilder.create().texOffs(0, 157).addBox(-1.9F, -2.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(134, 159).addBox(-0.9F, -2.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(160, 73).addBox(-0.9F, -3.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(28, 160).addBox(0.1F, -4.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

        PartDefinition LeftEar2 = Head2.addOrReplaceChild("LeftEar2", CubeListBuilder.create(), PartPose.offset(3.0F, -7.5F, 0.0F));

        PartDefinition LeftEarPivot2 = LeftEar2.addOrReplaceChild("LeftEarPivot2", CubeListBuilder.create().texOffs(8, 157).addBox(-1.1F, -2.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(140, 159).addBox(-1.1F, -2.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(160, 75).addBox(-1.1F, -3.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(160, 81).addBox(-1.1F, -4.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

        PartDefinition Hair2 = Head2.addOrReplaceChild("Hair2", CubeListBuilder.create().texOffs(0, 103).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Head3 = heads.addOrReplaceChild("Head3", CubeListBuilder.create().texOffs(32, 110).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(16, 152).addBox(-2.0F, -4.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(158, 12).addBox(-1.5F, -2.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -24.5F, -8.0F, 0.4363F, -0.7854F, 0.0F));

        PartDefinition RightEar3 = Head3.addOrReplaceChild("RightEar3", CubeListBuilder.create(), PartPose.offset(-3.0F, -7.5F, 0.0F));

        PartDefinition RightEarPivot3 = RightEar3.addOrReplaceChild("RightEarPivot3", CubeListBuilder.create().texOffs(158, 0).addBox(-1.9F, -2.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(146, 159).addBox(-0.9F, -2.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(160, 77).addBox(-0.9F, -3.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(160, 83).addBox(0.1F, -4.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

        PartDefinition LeftEar3 = Head3.addOrReplaceChild("LeftEar3", CubeListBuilder.create(), PartPose.offset(3.0F, -7.5F, 0.0F));

        PartDefinition LeftEarPivot3 = LeftEar3.addOrReplaceChild("LeftEarPivot3", CubeListBuilder.create().texOffs(158, 4).addBox(-1.1F, -2.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(152, 159).addBox(-1.1F, -2.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(160, 79).addBox(-1.1F, -3.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(160, 85).addBox(-1.1F, -4.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

        PartDefinition Hair3 = Head3.addOrReplaceChild("Hair3", CubeListBuilder.create().texOffs(64, 110).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F))
                .texOffs(128, 58).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition headbody = heads.addOrReplaceChild("headbody", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -9.0F, 16.0F, 15.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.5F, 0.0F));

        PartDefinition Torso = body.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(0, 30).addBox(-7.0F, 0.0F, -8.0F, 14.0F, 12.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.5F, 0.0F));

        PartDefinition LeftArm = Torso.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(128, 72).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 15.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 1.0F, 0.0F, 0.0F, 0.0F, -0.829F));

        PartDefinition RightArm2 = Torso.addOrReplaceChild("RightArm2", CubeListBuilder.create().texOffs(128, 91).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -24.0F, 0.0F));

        PartDefinition RightArm2_r1 = RightArm2.addOrReplaceChild("RightArm2_r1", CubeListBuilder.create().texOffs(128, 107).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -6.0F, 0.0F, 0.0F, 0.0F, 0.7418F));

        PartDefinition RightArm = Torso.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(128, 123).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -8.0F, -2.0F, 0.0F, 0.0F, 1.0472F));

        PartDefinition RightArm_r1 = RightArm.addOrReplaceChild("RightArm_r1", CubeListBuilder.create().texOffs(0, 133).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 0.0F, 0.0F, 0.7418F));

        PartDefinition Tail = Torso.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0F, 9.5F, 3.0F));

        PartDefinition TailPrimary = Tail.addOrReplaceChild("TailPrimary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition TailPrimary_r1 = TailPrimary.addOrReplaceChild("TailPrimary_r1", CubeListBuilder.create().texOffs(144, 102).addBox(-2.0F, 0.75F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1781F, 0.0F, 0.0F));

        PartDefinition TailSecondary = TailPrimary.addOrReplaceChild("TailSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.25F, 4.5F));

        PartDefinition TailSecondary_r1 = TailSecondary.addOrReplaceChild("TailSecondary_r1", CubeListBuilder.create().texOffs(62, 16).addBox(-2.5F, -0.45F, -2.1F, 5.0F, 7.0F, 5.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 1.4835F, 0.0F, 0.0F));

        PartDefinition TailTertiary = TailSecondary.addOrReplaceChild("TailTertiary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.75F, 2.5F));

        PartDefinition TailTertiary_r1 = TailTertiary.addOrReplaceChild("TailTertiary_r1", CubeListBuilder.create().texOffs(0, 149).addBox(-2.0F, -1.2F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.0F, 1.8326F, 0.0F, 0.0F));

        PartDefinition Tail2 = Tail.addOrReplaceChild("Tail2", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, 0.0F, 0.6981F, 0.0F));

        PartDefinition TailPrimary2 = Tail2.addOrReplaceChild("TailPrimary2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition TailPrimary2_r1 = TailPrimary2.addOrReplaceChild("TailPrimary2_r1", CubeListBuilder.create().texOffs(144, 111).addBox(-2.0F, 0.75F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1781F, 0.0F, 0.0F));

        PartDefinition TailSecondary2 = TailPrimary2.addOrReplaceChild("TailSecondary2", CubeListBuilder.create(), PartPose.offset(0.0F, 1.25F, 4.5F));

        PartDefinition TailSecondary2_r1 = TailSecondary2.addOrReplaceChild("TailSecondary2_r1", CubeListBuilder.create().texOffs(128, 139).addBox(-2.5F, -0.45F, -2.1F, 5.0F, 7.0F, 5.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 1.4835F, 0.0F, 0.0F));

        PartDefinition TailTertiary2 = TailSecondary2.addOrReplaceChild("TailTertiary2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.75F, 2.5F));

        PartDefinition TailTertiary2_r1 = TailTertiary2.addOrReplaceChild("TailTertiary2_r1", CubeListBuilder.create().texOffs(150, 14).addBox(-2.0F, -1.2F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.0F, 1.8326F, 0.0F, 0.0F));

        PartDefinition Tail3 = Tail.addOrReplaceChild("Tail3", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 0.0F, -0.6109F, 0.0F));

        PartDefinition TailPrimary3 = Tail3.addOrReplaceChild("TailPrimary3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition TailPrimary3_r1 = TailPrimary3.addOrReplaceChild("TailPrimary3_r1", CubeListBuilder.create().texOffs(144, 120).addBox(-2.0F, 0.75F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1781F, 0.0F, 0.0F));

        PartDefinition TailSecondary3 = TailPrimary3.addOrReplaceChild("TailSecondary3", CubeListBuilder.create(), PartPose.offset(0.0F, 1.25F, 4.5F));

        PartDefinition TailSecondary3_r1 = TailSecondary3.addOrReplaceChild("TailSecondary3_r1", CubeListBuilder.create().texOffs(16, 140).addBox(-2.5F, -0.45F, -2.1F, 5.0F, 7.0F, 5.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 1.4835F, 0.0F, 0.0F));

        PartDefinition TailTertiary3 = TailSecondary3.addOrReplaceChild("TailTertiary3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.75F, 2.5F));

        PartDefinition TailTertiary3_r1 = TailTertiary3.addOrReplaceChild("TailTertiary3_r1", CubeListBuilder.create().texOffs(150, 22).addBox(-2.0F, -1.2F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.0F, 1.8326F, 0.0F, 0.0F));

        PartDefinition legs = partdefinition.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition rightleg3 = legs.addOrReplaceChild("rightleg3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 12.0F));

        PartDefinition RightLeg = rightleg3.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.5F, -13.5F, -18.0F, 0.0F, 0.9163F, 0.0F));

        PartDefinition RightLeg_r1 = RightLeg.addOrReplaceChild("RightLeg_r1", CubeListBuilder.create().texOffs(36, 140).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition bpne = RightLeg.addOrReplaceChild("bpne", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

        PartDefinition bpne_r1 = bpne.addOrReplaceChild("bpne_r1", CubeListBuilder.create().texOffs(100, 140).addBox(-1.99F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition RightFoot = bpne.addOrReplaceChild("RightFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

        PartDefinition RightFoot_r1 = RightFoot.addOrReplaceChild("RightFoot_r1", CubeListBuilder.create().texOffs(150, 30).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

        PartDefinition RightPad = RightFoot.addOrReplaceChild("RightPad", CubeListBuilder.create().texOffs(32, 55).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

        PartDefinition RightLeg2 = rightleg3.addOrReplaceChild("RightLeg2", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.5F, -13.5F, -7.0F, 0.0F, 0.9163F, 0.0F));

        PartDefinition RightLeg2_r1 = RightLeg2.addOrReplaceChild("RightLeg2_r1", CubeListBuilder.create().texOffs(52, 140).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition RightLowerLeg2 = RightLeg2.addOrReplaceChild("RightLowerLeg2", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

        PartDefinition RightLowerLeg2_r1 = RightLowerLeg2.addOrReplaceChild("RightLowerLeg2_r1", CubeListBuilder.create().texOffs(144, 72).addBox(-1.99F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition RightFoot2 = RightLowerLeg2.addOrReplaceChild("RightFoot2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

        PartDefinition RightFoot2_r1 = RightFoot2.addOrReplaceChild("RightFoot2_r1", CubeListBuilder.create().texOffs(100, 150).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

        PartDefinition RightPad2 = RightFoot2.addOrReplaceChild("RightPad2", CubeListBuilder.create().texOffs(144, 129).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

        PartDefinition leftlegss = legs.addOrReplaceChild("leftlegss", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition LeftLeg = leftlegss.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offsetAndRotation(5.5F, -13.5F, -6.0F, 0.0F, -0.6981F, 0.0F));

        PartDefinition LeftLeg_r1 = LeftLeg.addOrReplaceChild("LeftLeg_r1", CubeListBuilder.create().texOffs(68, 140).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition LeftLowerLeg = LeftLeg.addOrReplaceChild("LeftLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

        PartDefinition LeftLowerLeg_r1 = LeftLowerLeg.addOrReplaceChild("LeftLowerLeg_r1", CubeListBuilder.create().texOffs(144, 82).addBox(-2.01F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition LeftFoot = LeftLowerLeg.addOrReplaceChild("LeftFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

        PartDefinition LeftFoot_r1 = LeftFoot.addOrReplaceChild("LeftFoot_r1", CubeListBuilder.create().texOffs(114, 150).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

        PartDefinition LeftPad = LeftFoot.addOrReplaceChild("LeftPad", CubeListBuilder.create().texOffs(148, 136).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

        PartDefinition LeftLeg2 = leftlegss.addOrReplaceChild("LeftLeg2", CubeListBuilder.create(), PartPose.offsetAndRotation(5.5F, -13.5F, 5.0F, 0.0F, -0.6981F, 0.0F));

        PartDefinition LeftLeg2_r1 = LeftLeg2.addOrReplaceChild("LeftLeg2_r1", CubeListBuilder.create().texOffs(84, 140).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition LeftLowerLeg2 = LeftLeg2.addOrReplaceChild("LeftLowerLeg2", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

        PartDefinition LeftLowerLeg2_r1 = LeftLowerLeg2.addOrReplaceChild("LeftLowerLeg2_r1", CubeListBuilder.create().texOffs(144, 92).addBox(-2.01F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition LeftFoot2 = LeftLowerLeg2.addOrReplaceChild("LeftFoot2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

        PartDefinition LeftFoot2_r1 = LeftFoot2.addOrReplaceChild("LeftFoot2_r1", CubeListBuilder.create().texOffs(148, 150).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

        PartDefinition LeftPad2 = LeftFoot2.addOrReplaceChild("LeftPad2", CubeListBuilder.create().texOffs(148, 143).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw , headPitch, ageInTicks);

        this.animateWalk(mutantfamilyAnimations.MUTANT_FAMILY_WALK, limbSwing, limbSwingAmount, 2f, 2f);
        this.animate(((LatexMutantFamilyEntity) entity).idleAnimationState, mutantfamilyAnimations.MUTANT_FAMILY_IDLE, ageInTicks, 3f);
        this.animate(entity.attackAnimationState, mutantfamilyAnimations.MUTANT_FAMILY_ATTACK, ageInTicks, 2f);
    }
    private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
        pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
        pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

        this.heads.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
        this.heads.xRot = pHeadPitch * ((float)Math.PI / 180F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		legs.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

    @Override
    public ModelPart root() {
        return root;
    }
}