package net.jerika.furmutage.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jerika.furmutage.entity.animations.DarkLatexChargerMutantAnimations;
import net.jerika.furmutage.entity.custom.DarkLatexChargerMutantEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class DarkLatexChargerMutantModel extends HierarchicalModel<DarkLatexChargerMutantEntity> {
	private final ModelPart rootModel;
	private final ModelPart leftleg;
	private final ModelPart rightleg;
	private final ModelPart leftarm;
	private final ModelPart upperarm;
	private final ModelPart lowerarm;
	private final ModelPart hand;
	private final ModelPart thumb;
	private final ModelPart kidnamedfinger1;
	private final ModelPart finger1;
	private final ModelPart finger2;
	private final ModelPart rightarm;
	private final ModelPart torso;
	private final ModelPart tail;
	private final ModelPart heads;
	private final ModelPart head6;
	private final ModelPart RightEar6;
	private final ModelPart RightEarPivot6;
	private final ModelPart LeftEar6;
	private final ModelPart LeftEarPivot6;
	private final ModelPart Mask6;
	private final ModelPart head3;
	private final ModelPart RightEar3;
	private final ModelPart RightEarPivot3;
	private final ModelPart LeftEar3;
	private final ModelPart LeftEarPivot3;
	private final ModelPart Mask3;
	private final ModelPart head4;
	private final ModelPart RightEar4;
	private final ModelPart RightEarPivot4;
	private final ModelPart LeftEar4;
	private final ModelPart LeftEarPivot4;
	private final ModelPart Mask4;
	private final ModelPart head2;
	private final ModelPart RightEar2;
	private final ModelPart RightEarPivot2;
	private final ModelPart LeftEar2;
	private final ModelPart LeftEarPivot2;
	private final ModelPart Mask2;
	private final ModelPart head5;
	private final ModelPart RightEar5;
	private final ModelPart RightEarPivot5;
	private final ModelPart LeftEar5;
	private final ModelPart LeftEarPivot5;
	private final ModelPart Mask5;
	private final ModelPart head1;
	private final ModelPart RightEar;
	private final ModelPart RightEarPivot;
	private final ModelPart LeftEar;
	private final ModelPart LeftEarPivot;
	private final ModelPart Mask;

	public DarkLatexChargerMutantModel(ModelPart root) {
		this.rootModel = root;
		this.leftleg = root.getChild("leftleg");
		this.rightleg = root.getChild("rightleg");
		this.leftarm = root.getChild("leftarm");
		this.upperarm = this.leftarm.getChild("upperarm");
		this.lowerarm = this.upperarm.getChild("lowerarm");
		this.hand = this.lowerarm.getChild("hand");
		this.thumb = this.hand.getChild("thumb");
		this.kidnamedfinger1 = this.hand.getChild("kidnamedfinger1");
		this.finger1 = this.kidnamedfinger1.getChild("finger1");
		this.finger2 = this.kidnamedfinger1.getChild("finger2");
		this.rightarm = root.getChild("rightarm");
		this.torso = root.getChild("torso");
		this.tail = this.torso.getChild("tail");
		this.heads = this.torso.getChild("heads");
		this.head6 = this.heads.getChild("head6");
		this.RightEar6 = this.head6.getChild("RightEar6");
		this.RightEarPivot6 = this.RightEar6.getChild("RightEarPivot6");
		this.LeftEar6 = this.head6.getChild("LeftEar6");
		this.LeftEarPivot6 = this.LeftEar6.getChild("LeftEarPivot6");
		this.Mask6 = this.head6.getChild("Mask6");
		this.head3 = this.heads.getChild("head3");
		this.RightEar3 = this.head3.getChild("RightEar3");
		this.RightEarPivot3 = this.RightEar3.getChild("RightEarPivot3");
		this.LeftEar3 = this.head3.getChild("LeftEar3");
		this.LeftEarPivot3 = this.LeftEar3.getChild("LeftEarPivot3");
		this.Mask3 = this.head3.getChild("Mask3");
		this.head4 = this.heads.getChild("head4");
		this.RightEar4 = this.head4.getChild("RightEar4");
		this.RightEarPivot4 = this.RightEar4.getChild("RightEarPivot4");
		this.LeftEar4 = this.head4.getChild("LeftEar4");
		this.LeftEarPivot4 = this.LeftEar4.getChild("LeftEarPivot4");
		this.Mask4 = this.head4.getChild("Mask4");
		this.head2 = this.heads.getChild("head2");
		this.RightEar2 = this.head2.getChild("RightEar2");
		this.RightEarPivot2 = this.RightEar2.getChild("RightEarPivot2");
		this.LeftEar2 = this.head2.getChild("LeftEar2");
		this.LeftEarPivot2 = this.LeftEar2.getChild("LeftEarPivot2");
		this.Mask2 = this.head2.getChild("Mask2");
		this.head5 = this.heads.getChild("head5");
		this.RightEar5 = this.head5.getChild("RightEar5");
		this.RightEarPivot5 = this.RightEar5.getChild("RightEarPivot5");
		this.LeftEar5 = this.head5.getChild("LeftEar5");
		this.LeftEarPivot5 = this.LeftEar5.getChild("LeftEarPivot5");
		this.Mask5 = this.head5.getChild("Mask5");
		this.head1 = this.heads.getChild("head1");
		this.RightEar = this.head1.getChild("RightEar");
		this.RightEarPivot = this.RightEar.getChild("RightEarPivot");
		this.LeftEar = this.head1.getChild("LeftEar");
		this.LeftEarPivot = this.LeftEar.getChild("LeftEarPivot");
		this.Mask = this.head1.getChild("Mask");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition leftleg = partdefinition.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(144, 16).addBox(-4.1782F, 17.0F, -8.7897F, 6.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.76F, 3.0F, 1.32F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r1 = leftleg.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(98, 30).addBox(-2.16F, -4.08F, -3.84F, 6.0F, 6.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0182F, 6.6F, -3.2218F, 1.0036F, 0.0F, 0.0F));

		PartDefinition cube_r2 = leftleg.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(108, 130).addBox(0.84F, -5.08F, 0.08F, 0.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0182F, 17.16F, -5.1098F, 1.1781F, 0.0F, 0.0F));

		PartDefinition cube_r3 = leftleg.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(122, 111).addBox(-2.16F, -4.08F, -1.92F, 6.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0182F, 17.16F, -1.1098F, 1.1781F, 0.0F, 0.0F));

		PartDefinition cube_r4 = leftleg.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 130).addBox(-2.16F, -4.08F, -1.92F, 6.0F, 6.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0182F, 9.48F, 0.8103F, 2.5307F, 0.0F, 0.0F));

		PartDefinition rightleg = partdefinition.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(130, 79).addBox(-4.8902F, 17.0F, -8.7897F, 6.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.76F, 3.0F, -0.68F, 0.0F, 0.7854F, 0.0F));

		PartDefinition cube_r5 = rightleg.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(122, 93).addBox(-4.08F, -4.08F, -1.92F, 6.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.8102F, 17.16F, -1.1098F, 1.1781F, 0.0F, 0.0F));

		PartDefinition cube_r6 = rightleg.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(122, 129).addBox(-4.08F, -11.4524F, -7.0822F, 6.0F, 6.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.8102F, 0.48F, 0.8103F, 2.5307F, 0.0F, 0.0F));

		PartDefinition cube_r7 = rightleg.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(40, 154).addBox(-1.08F, -3.08F, 1.16F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.8102F, 6.6F, -10.5218F, 1.0036F, 0.0F, 0.0F));

		PartDefinition cube_r8 = rightleg.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 99).addBox(-4.08F, -4.08F, -3.84F, 6.0F, 6.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.8102F, 6.6F, -3.5217F, 1.0036F, 0.0F, 0.0F));

		PartDefinition leftarm = partdefinition.addOrReplaceChild("leftarm", CubeListBuilder.create(), PartPose.offsetAndRotation(10.76F, -20.0F, -5.24F, 0.0F, 0.829F, 0.0F));

		PartDefinition upperarm = leftarm.addOrReplaceChild("upperarm", CubeListBuilder.create(), PartPose.offset(1.952F, -0.16F, 3.7451F));

		PartDefinition cube_r9 = upperarm.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(140, 145).addBox(-1.16F, 3.2F, -4.76F, 0.0F, 18.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -8.0F, 0.0F, 1.4993F, -0.1108F, 0.2936F));

		PartDefinition cube_r10 = upperarm.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(94, 63).addBox(-5.16F, 1.2F, -4.76F, 8.0F, 20.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(94, 93).addBox(-4.08F, -4.04F, -3.84F, 6.0F, 29.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.4993F, -0.1108F, 0.2936F));

		PartDefinition lowerarm = upperarm.addOrReplaceChild("lowerarm", CubeListBuilder.create(), PartPose.offset(-1.92F, 2.184F, 20.2F));

		PartDefinition cube_r11 = lowerarm.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(28, 154).addBox(-2.08F, 28.16F, -1.84F, 2.0F, 16.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(58, 57).addBox(-5.16F, -2.2F, -4.76F, 8.0F, 31.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(70, 0).addBox(-4.08F, -7.84F, -3.84F, 6.0F, 41.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 146).addBox(-4.08F, 36.16F, -3.84F, 6.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, -1.0F, -0.4971F, -0.1719F, -0.3053F));

		PartDefinition cube_r12 = lowerarm.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(76, 98).addBox(-5.16F, -2.2F, 0.24F, 8.0F, 31.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 1.0F, 0.0F, -0.4971F, -0.1719F, -0.3053F));

		PartDefinition hand = lowerarm.addOrReplaceChild("hand", CubeListBuilder.create(), PartPose.offset(14.36F, 37.976F, -19.96F));

		PartDefinition cube_r13 = hand.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(94, 49).addBox(-2.2803F, -2.0F, -5.2097F, 16.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 2.0F, 0.0F, 0.0F, -0.1309F, 0.0F));

		PartDefinition cube_r14 = hand.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 118).addBox(-4.1326F, -6.0F, -3.9572F, 12.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 4.0F, -1.0F, 0.0F, -0.1309F, 0.0F));

		PartDefinition thumb = hand.addOrReplaceChild("thumb", CubeListBuilder.create(), PartPose.offset(1.0F, 2.0F, -5.0F));

		PartDefinition cube_r15 = thumb.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(158, 101).addBox(-0.7853F, -2.0F, -2.9144F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6F, 0.0F, -4.1F, 0.0F, -0.1309F, 0.0F));

		PartDefinition cube_r16 = thumb.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(64, 132).addBox(0.3453F, -4.0F, -3.923F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.1F, 2.0F, -8.5F, 0.0F, -0.1309F, 0.0F));

		PartDefinition cube_r17 = thumb.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(154, 137).addBox(-2.9158F, -2.0F, -3.9059F, 6.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.1309F, 0.0F));

		PartDefinition kidnamedfinger1 = hand.addOrReplaceChild("kidnamedfinger1", CubeListBuilder.create(), PartPose.offset(10.0F, 4.0F, 1.2F));

		PartDefinition cube_r18 = kidnamedfinger1.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(146, 54).addBox(-1.0278F, -4.0F, -1.3574F, 9.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, 2.8F, 0.0F, -0.1309F, 0.0F));

		PartDefinition cube_r19 = kidnamedfinger1.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(146, 46).addBox(-1.1583F, -2.0F, -2.3488F, 9.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -2.0F, -2.2F, 0.0F, -0.1309F, 0.0F));

		PartDefinition finger1 = kidnamedfinger1.addOrReplaceChild("finger1", CubeListBuilder.create(), PartPose.offset(11.0F, -2.0F, -1.5F));

		PartDefinition cube_r20 = finger1.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(154, 133).addBox(0.52F, -4.0F, 2.0F, 12.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, 2.0F, -1.0F, 0.0F, -0.1309F, 0.0F));

		PartDefinition cube_r21 = finger1.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(62, 49).addBox(-0.863F, -3.0F, -0.8438F, 12.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.0F, -0.2F, 0.0F, -0.1309F, 0.0F));

		PartDefinition finger2 = kidnamedfinger1.addOrReplaceChild("finger2", CubeListBuilder.create(), PartPose.offset(11.0F, -2.0F, 5.5F));

		PartDefinition cube_r22 = finger2.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(28, 148).addBox(-0.863F, -2.0F, -0.8438F, 12.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.2F, 0.0F, -0.1309F, 0.0F));

		PartDefinition cube_r23 = finger2.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(154, 129).addBox(0.52F, -4.0F, 2.0F, 12.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, 2.0F, -1.5F, 0.0F, -0.1309F, 0.0F));

		PartDefinition rightarm = partdefinition.addOrReplaceChild("rightarm", CubeListBuilder.create(), PartPose.offsetAndRotation(-9.92F, -21.0F, -3.08F, 0.0F, -0.4363F, 0.0F));

		PartDefinition cube_r24 = rightarm.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(62, 33).addBox(-0.16F, -3.56F, 1.16F, 4.0F, 12.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(84, 146).addBox(3.84F, -3.56F, -1.84F, 4.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.7964F, 13.104F, 6.9106F, -0.5236F, 0.0F, 0.0F));

		PartDefinition cube_r25 = rightarm.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(38, 99).addBox(-4.24F, -2.64F, -0.84F, 5.0F, 18.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.9964F, -4.16F, 1.1506F, 0.5236F, 0.0F, 0.0F));

		PartDefinition cube_r26 = rightarm.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(64, 146).addBox(-4.24F, -3.64F, -0.84F, 5.0F, 19.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.9964F, 0.84F, 1.1506F, 0.5236F, 0.0F, 0.0F));

		PartDefinition torso = partdefinition.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 33).addBox(-9.4F, 2.8F, -5.76F, 19.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-11.48F, -13.88F, -9.6F, 23.0F, 21.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(50, 98).addBox(-0.08F, -19.88F, 0.4F, 0.0F, 21.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(98, 15).addBox(-11.48F, -28.88F, -1.6F, 23.0F, 15.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 0.0F));

		PartDefinition tail = torso.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 9.0F, 11.0F, 0.1745F, 0.0F, 0.0F));

		PartDefinition cube_r27 = tail.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(0, 78).addBox(-6.0F, -3.8428F, -1.9364F, 12.0F, 8.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 11.0F, 0.8727F, 0.0F, 0.0F));

		PartDefinition cube_r28 = tail.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(0, 57).addBox(-8.0F, -3.8428F, -1.9364F, 16.0F, 8.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r29 = tail.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(98, 0).addBox(-7.0F, -3.8428F, -1.9364F, 14.0F, 6.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -5.0F, -0.1745F, 0.0F, 0.0F));

		PartDefinition heads = torso.addOrReplaceChild("heads", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, -8.0F));

		PartDefinition head6 = heads.addOrReplaceChild("head6", CubeListBuilder.create().texOffs(108, 145).addBox(-13.0F, -51.5F, -14.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(162, 65).addBox(-10.5F, -44.5F, -16.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 57.0F, 8.0F));

		PartDefinition RightEar6 = head6.addOrReplaceChild("RightEar6", CubeListBuilder.create(), PartPose.offset(-12.0F, -51.0F, -10.0F));

		PartDefinition RightEarPivot6 = RightEar6.addOrReplaceChild("RightEarPivot6", CubeListBuilder.create().texOffs(50, 82).addBox(-1.9F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(90, 164).addBox(-0.9F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
		.texOffs(132, 167).addBox(-0.9F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(168, 44).addBox(0.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

		PartDefinition LeftEar6 = head6.addOrReplaceChild("LeftEar6", CubeListBuilder.create(), PartPose.offset(-6.0F, -51.0F, -10.0F));

		PartDefinition LeftEarPivot6 = LeftEar6.addOrReplaceChild("LeftEarPivot6", CubeListBuilder.create().texOffs(50, 86).addBox(-1.1F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(96, 164).addBox(-1.1F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
		.texOffs(168, 34).addBox(-1.1F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(60, 168).addBox(-1.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

		PartDefinition Mask6 = head6.addOrReplaceChild("Mask6", CubeListBuilder.create().texOffs(102, 165).addBox(-1.0F, -4.0F, -5.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(160, 157).addBox(-3.0F, -5.0F, -5.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(160, 159).addBox(-3.0F, -2.0F, -5.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(158, 99).addBox(-4.0F, -1.0F, -5.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(162, 86).addBox(-2.0F, -6.0F, -5.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(160, 167).addBox(-1.0F, -7.0F, -5.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(104, 158).addBox(3.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(136, 161).addBox(-4.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(104, 161).addBox(-2.0F, -2.0F, -7.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0F, -44.5F, -10.0F));

		PartDefinition head3 = heads.addOrReplaceChild("head3", CubeListBuilder.create().texOffs(32, 132).addBox(-4.0F, -5.5F, -7.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(126, 161).addBox(-1.5F, 1.5F, -9.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 1.0F, 1.0F, 0.3054F, -0.1309F, 0.0F));

		PartDefinition RightEar3 = head3.addOrReplaceChild("RightEar3", CubeListBuilder.create(), PartPose.offset(-3.0F, -5.0F, -3.0F));

		PartDefinition RightEarPivot3 = RightEar3.addOrReplaceChild("RightEarPivot3", CubeListBuilder.create().texOffs(56, 156).addBox(-1.9F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(0, 163).addBox(-0.9F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
		.texOffs(6, 167).addBox(-0.9F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(24, 167).addBox(0.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

		PartDefinition LeftEar3 = head3.addOrReplaceChild("LeftEar3", CubeListBuilder.create(), PartPose.offset(3.0F, -5.0F, -3.0F));

		PartDefinition LeftEarPivot3 = LeftEar3.addOrReplaceChild("LeftEarPivot3", CubeListBuilder.create().texOffs(56, 160).addBox(-1.1F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(6, 163).addBox(-1.1F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
		.texOffs(12, 167).addBox(-1.1F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(52, 167).addBox(-1.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

		PartDefinition Mask3 = head3.addOrReplaceChild("Mask3", CubeListBuilder.create().texOffs(128, 164).addBox(-1.0F, -4.0F, -5.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(158, 117).addBox(-3.0F, -5.0F, -5.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(158, 119).addBox(-3.0F, -2.0F, -5.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(158, 93).addBox(-4.0F, -1.0F, -5.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(162, 72).addBox(-2.0F, -6.0F, -5.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(18, 167).addBox(-1.0F, -7.0F, -5.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(118, 136).addBox(3.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(118, 139).addBox(-4.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(158, 121).addBox(-2.0F, -2.0F, -7.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, -3.0F));

		PartDefinition head4 = heads.addOrReplaceChild("head4", CubeListBuilder.create().texOffs(136, 30).addBox(-3.0F, -5.5F, -6.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(160, 161).addBox(-0.5F, 1.5F, -8.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, 11.0F, 0.0F, -0.2497F, -0.6038F, 0.0681F));

		PartDefinition RightEar4 = head4.addOrReplaceChild("RightEar4", CubeListBuilder.create(), PartPose.offset(-2.0F, -5.0F, -2.0F));

		PartDefinition RightEarPivot4 = RightEar4.addOrReplaceChild("RightEarPivot4", CubeListBuilder.create().texOffs(62, 45).addBox(-1.9F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(12, 163).addBox(-0.9F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
		.texOffs(40, 167).addBox(-0.9F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(168, 36).addBox(0.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

		PartDefinition LeftEar4 = head4.addOrReplaceChild("LeftEar4", CubeListBuilder.create(), PartPose.offset(4.0F, -5.0F, -2.0F));

		PartDefinition LeftEarPivot4 = LeftEar4.addOrReplaceChild("LeftEarPivot4", CubeListBuilder.create().texOffs(50, 78).addBox(-1.1F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(18, 163).addBox(-1.1F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
		.texOffs(46, 167).addBox(-1.1F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(168, 38).addBox(-1.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

		PartDefinition Mask4 = head4.addOrReplaceChild("Mask4", CubeListBuilder.create().texOffs(134, 164).addBox(-1.0F, -4.0F, -5.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(158, 125).addBox(-3.0F, -5.0F, -5.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(158, 127).addBox(-3.0F, -2.0F, -5.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(158, 95).addBox(-4.0F, -1.0F, -5.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(162, 74).addBox(-2.0F, -6.0F, -5.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(108, 167).addBox(-1.0F, -7.0F, -5.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(104, 146).addBox(3.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(104, 149).addBox(-4.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(160, 145).addBox(-2.0F, -2.0F, -7.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 1.5F, -2.0F));

		PartDefinition head2 = heads.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(76, 130).addBox(-3.0F, -7.5F, -5.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(116, 161).addBox(-0.5F, -0.5F, -7.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -7.0F, -1.0F, -0.2618F, -0.5236F, 0.0F));

		PartDefinition RightEar2 = head2.addOrReplaceChild("RightEar2", CubeListBuilder.create(), PartPose.offset(-2.0F, -7.0F, -1.0F));

		PartDefinition RightEarPivot2 = RightEar2.addOrReplaceChild("RightEarPivot2", CubeListBuilder.create().texOffs(56, 148).addBox(-1.9F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(44, 125).addBox(-0.9F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
		.texOffs(168, 32).addBox(-0.9F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(24, 163).addBox(0.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

		PartDefinition LeftEar2 = head2.addOrReplaceChild("LeftEar2", CubeListBuilder.create(), PartPose.offset(4.0F, -7.0F, -1.0F));

		PartDefinition LeftEarPivot2 = LeftEar2.addOrReplaceChild("LeftEarPivot2", CubeListBuilder.create().texOffs(56, 152).addBox(-1.1F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(162, 88).addBox(-1.1F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
		.texOffs(166, 166).addBox(-1.1F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(24, 165).addBox(-1.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

		PartDefinition Mask2 = head2.addOrReplaceChild("Mask2", CubeListBuilder.create().texOffs(122, 164).addBox(-1.0F, -4.0F, -5.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(158, 109).addBox(-3.0F, -5.0F, -5.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(158, 111).addBox(-3.0F, -2.0F, -5.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(32, 130).addBox(-4.0F, -1.0F, -5.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(162, 70).addBox(-2.0F, -6.0F, -5.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 167).addBox(-1.0F, -7.0F, -5.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(118, 130).addBox(3.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(118, 133).addBox(-4.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(158, 113).addBox(-2.0F, -2.0F, -7.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -0.5F, -1.0F));

		PartDefinition head5 = heads.addOrReplaceChild("head5", CubeListBuilder.create().texOffs(144, 0).addBox(-5.0F, -3.5F, -3.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(162, 62).addBox(-2.5F, 3.5F, -5.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 0.0F, -3.0F, -0.2618F, 0.6981F, 0.0F));

		PartDefinition RightEar5 = head5.addOrReplaceChild("RightEar5", CubeListBuilder.create(), PartPose.offset(-4.0F, -3.0F, 1.0F));

		PartDefinition RightEarPivot5 = RightEar5.addOrReplaceChild("RightEarPivot5", CubeListBuilder.create().texOffs(162, 76).addBox(-1.9F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(56, 164).addBox(-0.9F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
		.texOffs(114, 167).addBox(-0.9F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(168, 40).addBox(0.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

		PartDefinition LeftEar5 = head5.addOrReplaceChild("LeftEar5", CubeListBuilder.create(), PartPose.offset(2.0F, -3.0F, 1.0F));

		PartDefinition LeftEarPivot5 = LeftEar5.addOrReplaceChild("LeftEarPivot5", CubeListBuilder.create().texOffs(162, 80).addBox(-1.1F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(84, 164).addBox(-1.1F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
		.texOffs(120, 167).addBox(-1.1F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(168, 42).addBox(-1.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

		PartDefinition Mask5 = head5.addOrReplaceChild("Mask5", CubeListBuilder.create().texOffs(160, 164).addBox(-1.0F, -4.0F, -5.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(160, 149).addBox(-3.0F, -5.0F, -5.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(160, 151).addBox(-3.0F, -2.0F, -5.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(158, 97).addBox(-4.0F, -1.0F, -5.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(162, 84).addBox(-2.0F, -6.0F, -5.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(126, 167).addBox(-1.0F, -7.0F, -5.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(104, 152).addBox(3.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(104, 155).addBox(-4.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(160, 153).addBox(-2.0F, -2.0F, -7.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 3.5F, 1.0F));

		PartDefinition head1 = heads.addOrReplaceChild("head1", CubeListBuilder.create().texOffs(130, 63).addBox(-3.0F, -3.5F, -3.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(136, 46).addBox(-0.5F, 3.5F, -5.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -12.0F, -3.0F, -0.3054F, 0.4363F, 0.0F));

		PartDefinition RightEar = head1.addOrReplaceChild("RightEar", CubeListBuilder.create(), PartPose.offset(-2.0F, -3.0F, 1.0F));

		PartDefinition RightEarPivot = RightEar.addOrReplaceChild("RightEarPivot", CubeListBuilder.create().texOffs(50, 90).addBox(-1.9F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(44, 117).addBox(-0.9F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
		.texOffs(108, 165).addBox(-0.9F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(28, 146).addBox(0.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

		PartDefinition LeftEar = head1.addOrReplaceChild("LeftEar", CubeListBuilder.create(), PartPose.offset(4.0F, -3.0F, 1.0F));

		PartDefinition LeftEarPivot = LeftEar.addOrReplaceChild("LeftEarPivot", CubeListBuilder.create().texOffs(50, 94).addBox(-1.1F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(44, 121).addBox(-1.1F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
		.texOffs(168, 30).addBox(-1.1F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(56, 168).addBox(-1.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

		PartDefinition Mask = head1.addOrReplaceChild("Mask", CubeListBuilder.create().texOffs(116, 164).addBox(-1.0F, -4.0F, -5.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(80, 55).addBox(-3.0F, -5.0F, -5.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(108, 142).addBox(-3.0F, -2.0F, -5.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(62, 55).addBox(-4.0F, -1.0F, -5.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(162, 68).addBox(-2.0F, -6.0F, -5.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(166, 164).addBox(-1.0F, -7.0F, -5.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(90, 49).addBox(3.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(90, 52).addBox(-4.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(64, 142).addBox(-2.0F, -2.0F, -7.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 3.5F, 1.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public ModelPart root() {
		return rootModel;
	}

	@Override
	public void setupAnim(DarkLatexChargerMutantEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		boolean isMoving = limbSwingAmount > 0.00001f;
		boolean isBarelyMoving = limbSwingAmount > 0.00001f && limbSwingAmount < 0.04f;
		boolean isAttacking = entity.attackAnimationState.isStarted();

		// Attack takes priority - full attack animation when melee attacking
		if (isAttacking) {
			this.animate(entity.attackAnimationState, DarkLatexChargerMutantAnimations.ATTACK, ageInTicks, 1.2f);
		} else if (isBarelyMoving) {
			this.animateWalk(DarkLatexChargerMutantAnimations.WALK, limbSwing, limbSwingAmount, 2f, 2f);
		} else if (isMoving) {
			this.animateWalk(DarkLatexChargerMutantAnimations.SPRINT, limbSwing, limbSwingAmount, 2f, 2f);
		} else {
			this.animate(entity.idleAnimationState, DarkLatexChargerMutantAnimations.IDLE, ageInTicks, 1f);
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		leftleg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rightleg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leftarm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rightarm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		torso.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}