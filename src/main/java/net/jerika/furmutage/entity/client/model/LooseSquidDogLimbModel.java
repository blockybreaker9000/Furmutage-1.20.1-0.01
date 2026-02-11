package net.jerika.furmutage.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jerika.furmutage.entity.animations.LooseSquidDogLimbAnimation;
import net.jerika.furmutage.entity.custom.LooseSquidDogLimbEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Loose Squid Dog Limb model – geometry and animations from Blockbench.
 */
@OnlyIn(Dist.CLIENT)
public class LooseSquidDogLimbModel extends HierarchicalModel<LooseSquidDogLimbEntity> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation(furmutage.MOD_ID, "loose_squid_dog_limb"), "main");

    private final ModelPart root;

    public LooseSquidDogLimbModel(ModelPart root) {
        this.root = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bodypart1 = partdefinition.addOrReplaceChild("bodypart1", CubeListBuilder.create(), PartPose.offset(0.0F, 25.6F, 0.0F));
        bodypart1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(18, 0).addBox(-2.4697F, -1.1161F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.05F, -5.0F, 0.0F, 0.0F, 0.0F, -0.8727F));

        PartDefinition bodypart2 = partdefinition.addOrReplaceChild("bodypart2", CubeListBuilder.create(), PartPose.offset(7.4F, 25.6F, 0.0F));
        bodypart2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(12, 7).addBox(-3.4059F, -1.0201F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.05F, -3.35F, 0.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition bodypart3 = partdefinition.addOrReplaceChild("bodypart3", CubeListBuilder.create(), PartPose.offset(7.4F, 25.6F, 0.0F));
        bodypart3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 11).addBox(-3.4559F, -0.9791F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5F, -2.6F, 0.0F, 0.0F, 0.0F, 0.0436F));

        PartDefinition bodypart4 = partdefinition.addOrReplaceChild("bodypart4", CubeListBuilder.create(), PartPose.offset(0.0F, 25.6F, 0.0F));
        bodypart4.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 15).addBox(-3.5283F, -0.8671F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -2.85F, 0.0F, 0.0F, 0.0F, -0.0436F));

        PartDefinition bodypart5 = partdefinition.addOrReplaceChild("bodypart5", CubeListBuilder.create(), PartPose.offset(0.0F, 25.6F, 0.0F));
        bodypart5.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 7).addBox(-3.5283F, -0.8671F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.6F, -2.65F, 0.0F, 0.0F, 0.0F, 0.0524F));

        PartDefinition bodypart6 = partdefinition.addOrReplaceChild("bodypart6", CubeListBuilder.create().texOffs(12, 11).addBox(-12.9F, -3.7F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 25.6F, 0.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(8.0F, 19.6F, 0.0F, 0.0F, 0.0F, -0.4363F));
        head.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 0).addBox(-0.667F, -1.0674F, -3.0F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.3F, -0.05F, 0.5F, 0.0F, 0.0F, -0.48F));
        head.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(12, 15).addBox(-0.1081F, -1.0583F, -2.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.7F, -1.6F, 0.5F, 0.0F, 0.0F, -0.48F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(LooseSquidDogLimbEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        if (entity.isHanging()) {
            this.animate(entity.ceilinghangAnimationState, LooseSquidDogLimbAnimation.ceilinghang, ageInTicks, 1.0F);
        } else {
            this.animate(entity.idleAnimationState, LooseSquidDogLimbAnimation.idle, ageInTicks, 1.0F);
            this.animate(entity.walkAnimationState, LooseSquidDogLimbAnimation.walk, ageInTicks, 1.0F);
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
