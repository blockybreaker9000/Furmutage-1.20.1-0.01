package net.jerika.furmutage.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jerika.furmutage.entity.animations.BacteriaAnimations;
import net.jerika.furmutage.entity.custom.LatexBacteriaEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class BacteriaModel<T extends LatexBacteriaEntity> extends HierarchicalModel<T> {
	private final ModelPart root;
	private final ModelPart Body;
	private final ModelPart rightlegs;
	private final ModelPart rightlegs2;
	private final ModelPart LeftLegs;
	private final ModelPart LeftLegs2;

	public BacteriaModel(ModelPart root) {
		this.root = root;
		this.Body = root.getChild("Body");
		this.rightlegs = root.getChild("rightlegs");
		this.rightlegs2 = root.getChild("rightlegs2");
		this.LeftLegs = root.getChild("LeftLegs");
		this.LeftLegs2 = root.getChild("LeftLegs2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -14.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(-3, -2).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 8).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(-3, -2).addBox(-2.0F, -13.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(-6, -4).addBox(-3.0F, -12.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 0.0F));

		PartDefinition cube_r1 = Body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 12).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(-3, -2).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, 0.829F, 0.0F));

		PartDefinition cube_r2 = Body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(-6, -4).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, 0.0F, -0.7418F, 0.0F));

		PartDefinition cube_r3 = Body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition body_r1 = Body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(-3, -2).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition rightlegs = partdefinition.addOrReplaceChild("rightlegs", CubeListBuilder.create().texOffs(0, 8).addBox(-7.0F, -4.0F, 0.0F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 8).addBox(-2.0F, -7.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 8).addBox(-4.0F, -7.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 8).addBox(-6.0F, -6.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

		PartDefinition rightlegs2 = partdefinition.addOrReplaceChild("rightlegs2", CubeListBuilder.create().texOffs(0, 8).addBox(-7.0F, -4.0F, 0.0F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 8).addBox(-2.0F, -7.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 8).addBox(-4.0F, -7.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 8).addBox(-6.0F, -6.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.6545F, 0.0F));

		PartDefinition LeftLegs = partdefinition.addOrReplaceChild("LeftLegs", CubeListBuilder.create().texOffs(5, 8).addBox(5.0F, -4.0F, 0.0F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(5, 8).addBox(4.0F, -6.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(5, 8).addBox(2.0F, -7.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(5, 8).addBox(0.0F, -7.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, -0.48F, 0.0F));

		PartDefinition LeftLegs2 = partdefinition.addOrReplaceChild("LeftLegs2", CubeListBuilder.create().texOffs(11, 8).addBox(5.0F, -4.0F, 0.0F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(11, 8).addBox(4.0F, -6.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(11, 8).addBox(2.0F, -7.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(11, 8).addBox(0.0F, -7.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.829F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		// Animate based on entity state
		if (entity.isJumping()) {
			this.animate(entity.jumpAnimationState, BacteriaAnimations.jump, ageInTicks, 1.0f);
		} else {
			this.animateWalk(BacteriaAnimations.walk, limbSwing, limbSwingAmount, 2.0f, 2.5f);
		}
		this.animate(entity.idleAnimationState, BacteriaAnimations.idle, ageInTicks, 1.0f);
	}

	@Override
	public ModelPart root() {
		return root;
	}
}

