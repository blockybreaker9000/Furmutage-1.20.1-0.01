package net.jerika.furmutage.entity.client.model;// Made with Blockbench 5.0.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class TSCDroneModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "tscdronemodel"), "main");
	private final ModelPart Body;
	private final ModelPart Propeller1;
	private final ModelPart Propeller2;
	private final ModelPart Propeller3;
	private final ModelPart Propeller4;

	public TSCDroneModel(ModelPart root) {
		this.Body = root.getChild("Body");
		this.Propeller1 = root.getChild("Propeller1");
		this.Propeller2 = root.getChild("Propeller2");
		this.Propeller3 = root.getChild("Propeller3");
		this.Propeller4 = root.getChild("Propeller4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 18).addBox(-3.0F, 2.0F, 1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(8, 18).addBox(-3.0F, 2.0F, -3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(8, 22).addBox(-1.0F, 2.0F, -4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 18).addBox(1.0F, 2.0F, 1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 22).addBox(1.0F, 2.0F, -3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 0).addBox(-1.0F, 4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

		PartDefinition Propeller1 = partdefinition.addOrReplaceChild("Propeller1", CubeListBuilder.create().texOffs(0, 8).addBox(-2.0F, 2.0F, -2.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 2.0F, 5.0F));

		PartDefinition Propeller2 = partdefinition.addOrReplaceChild("Propeller2", CubeListBuilder.create().texOffs(12, 8).addBox(-2.0F, 1.0F, -2.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 3.0F, 5.0F));

		PartDefinition Propeller3 = partdefinition.addOrReplaceChild("Propeller3", CubeListBuilder.create().texOffs(12, 13).addBox(-2.0F, 1.0F, -2.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 3.0F, -4.0F));

		PartDefinition Propeller4 = partdefinition.addOrReplaceChild("Propeller4", CubeListBuilder.create().texOffs(0, 13).addBox(-1.0F, 1.0F, -2.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 3.0F, -4.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Propeller1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Propeller2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Propeller3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Propeller4.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}