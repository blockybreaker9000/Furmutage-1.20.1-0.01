package net.jerika.furmutage.client.renderer.model.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.tfanimations.HelperModel;
import net.ltxprogrammer.changed.client.animations.Limb;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModelSet;
import net.ltxprogrammer.changed.client.renderer.model.armor.LatexHumanoidArmorModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ThunderiumArmorModel<T extends ChangedEntity> extends LatexHumanoidArmorModel<T, ThunderiumArmorModel<T>> {
    public static final ArmorModelSet<ChangedEntity, ThunderiumArmorModel<ChangedEntity>> MODEL_SET =
            ArmorModelSet.of(new ResourceLocation("furmutage", "armor_thunderium"), ThunderiumArmorModel::createArmorLayer, ThunderiumArmorModel::new);

    private final ModelPart Head;
    private final ModelPart Hat;
    private final ModelPart Torso;
    private final ModelPart LeftLeg;
    private final ModelPart RightLeg;
    private final ModelPart LeftArm;
    private final ModelPart RightArm;
    private final HumanoidAnimator<T, ThunderiumArmorModel<T>> animator;

    public ThunderiumArmorModel(ModelPart modelPart, ArmorModel model) {
        super(modelPart, model);
        this.Head = modelPart.getChild("head");
        this.Hat = modelPart.hasChild("hat") ? modelPart.getChild("hat") : Head;
        this.Torso = modelPart.getChild("body");
        this.LeftLeg = modelPart.getChild("left_leg");
        this.RightLeg = modelPart.getChild("right_leg");
        this.LeftArm = modelPart.getChild("left_arm");
        this.RightArm = modelPart.getChild("right_arm");

        this.animator = HumanoidAnimator.of(this).hipOffset(-1.5f).legLength(10.5f)
                .addPreset(AnimatorPresets.humanLike(Head, Torso, LeftArm, RightArm, LeftLeg, RightLeg));
    }

    public static LayerDefinition createArmorLayer(ArmorModel layer) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        // Head armor
        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, layer.dualDeformation)
                // Helmet visor
                .texOffs(40, 16).addBox(-1.0F, -4.5F, -5.4F, 2.0F, 3.0F, 2.0F, layer.deformation.extend(0.1F))
                // Helmet side pieces
                .texOffs(41, 22).addBox(-4.15F, -4.1F, -5.7F, 3.0F, 3.0F, 1.7F, layer.deformation.extend(0.1F))
                .texOffs(41, 22).mirror().addBox(1.15F, -4.1F, -5.7F, 3.0F, 3.0F, 1.7F, layer.deformation.extend(0.1F))
                // Helmet main piece
                .texOffs(38, 35).addBox(-4.2F, -4.2F, -4.4F, 8.4F, 4.0F, 4.4F, layer.deformation.extend(0.1F))
                // Helmet top
                .texOffs(40, 27).addBox(-4.4F, -5.2F, -4.6F, 8.8F, 4.0F, 2.4F, layer.deformation.extend(0.1F))
                // Helmet crown
                .texOffs(28, 4).addBox(-4.6F, -2.15F, -4.6F, 9.2F, 3.0F, 9.2F, layer.deformation.extend(0.1F))
                // Helmet front extension
                .texOffs(34, 12).addBox(-4.6F, -1.75F, -7.6F, 9.2F, 1.0F, 3.2F, layer.deformation.extend(0.1F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        // Hat layer (same as head for now)
        PartDefinition hat = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create()
                .texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, layer.dualDeformation.extend(0.2F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        // Body/Torso armor
        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, layer.dualDeformation)
                // Chest plate
                .texOffs(0, 54).addBox(-4.4F, 1.0F, -2.4F, 8.8F, 6.0F, 4.8F, layer.deformation.extend(0.1F))
                // Torso armor
                .texOffs(4, 12).addBox(-4.1F, -0.1F, -2.1F, 8.2F, 12.2F, 4.2F, layer.deformation.extend(0.1F))
                // Waist armor
                .texOffs(0, 45).addBox(-3.2F, 7.0F, -2.4F, 6.4F, 5.0F, 4.8F, layer.deformation.extend(0.1F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        // Left Arm armor
        PartDefinition leftArm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create()
                .texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, layer.dualDeformation).mirror(false)
                // Shoulder pad
                .texOffs(50, 18).mirror().addBox(-5.6F, -1.6F, -2.7F, 2.4F, 4.0F, 5.3F, layer.deformation.extend(0.1F))
                // Upper arm armor
                .texOffs(0, 16).mirror().addBox(-4.1F, -0.1F, -2.1F, 4.1F, 8.2F, 4.2F, layer.deformation.extend(0.1F))
                // Forearm armor
                .texOffs(16, 0).mirror().addBox(-4.4F, 6.1F, -2.4F, 3.6F, 4.0F, 4.8F, layer.deformation.extend(0.1F)),
                PartPose.offset(5.0F, 2.0F, 0.0F));

        // Right Arm armor
        PartDefinition rightArm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
                .texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, layer.dualDeformation)
                // Shoulder pad
                .texOffs(50, 18).addBox(3.2F, -1.6F, -2.7F, 2.4F, 4.0F, 5.3F, layer.deformation.extend(0.1F))
                // Upper arm armor
                .texOffs(0, 16).addBox(0.0F, -0.1F, -2.1F, 4.1F, 8.2F, 4.2F, layer.deformation.extend(0.1F))
                // Forearm armor
                .texOffs(16, 0).addBox(0.8F, 6.1F, -2.4F, 3.6F, 4.0F, 4.8F, layer.deformation.extend(0.1F)),
                PartPose.offset(-5.0F, 2.0F, 0.0F));

        // Left Leg armor
        PartDefinition leftLeg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create()
                .texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, layer.dualDeformation).mirror(false)
                // Thigh armor
                .texOffs(0, 16).mirror().addBox(-4.1F, 2.0F, -2.1F, 4.1F, 8.2F, 4.2F, layer.deformation.extend(0.1F))
                // Shin armor
                .texOffs(16, 0).mirror().addBox(-4.4F, 8.1F, -2.4F, 3.6F, 4.0F, 4.8F, layer.deformation.extend(0.1F))
                // Boot
                .texOffs(24, 59).mirror().addBox(-4.6F, 10.6F, -2.5F, 5.2F, 3.0F, 2.0F, layer.deformation.extend(0.1F))
                .texOffs(19, 48).mirror().addBox(-4.5F, 8.6F, -2.5F, 5.0F, 5.0F, 5.0F, layer.deformation.extend(0.1F)),
                PartPose.offset(2.0F, 12.0F, 0.0F));

        // Right Leg armor
        PartDefinition rightLeg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create()
                .texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, layer.dualDeformation)
                // Thigh armor
                .texOffs(0, 16).addBox(0.0F, 2.0F, -2.1F, 4.1F, 8.2F, 4.2F, layer.deformation.extend(0.1F))
                // Shin armor
                .texOffs(16, 0).addBox(0.8F, 8.1F, -2.4F, 3.6F, 4.0F, 4.8F, layer.deformation.extend(0.1F))
                // Boot
                .texOffs(24, 59).addBox(-0.6F, 10.6F, -2.5F, 5.2F, 3.0F, 2.0F, layer.deformation.extend(0.1F))
                .texOffs(19, 48).addBox(-0.5F, 8.6F, -2.5F, 5.0F, 5.0F, 5.0F, layer.deformation.extend(0.1F)),
                PartPose.offset(-2.0F, 12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderForSlot(T entity, RenderLayerParent<? super T, ?> parent, ItemStack stack, EquipmentSlot slot, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        this.scaleForSlot(parent, slot, poseStack);

        switch (slot) {
            case HEAD -> {
                Head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                if (Hat != Head) {
                    Hat.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                }
            }
            case CHEST -> {
                Torso.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                LeftArm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                RightArm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            }
            case LEGS -> {
                Torso.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                LeftLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                RightLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            }
            case FEET -> {
                LeftLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                RightLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            }
        }

        poseStack.popPose();
    }

    @Override
    public HumanoidAnimator<T, ThunderiumArmorModel<T>> getAnimator(T entity) {
        return animator;
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        if (Hat != Head) {
            Hat.copyFrom(Head);
        }
    }

    public ModelPart getArm(HumanoidArm arm) {
        return arm == HumanoidArm.LEFT ? this.LeftArm : this.RightArm;
    }

    public ModelPart getLeg(HumanoidArm leg) {
        return leg == HumanoidArm.LEFT ? this.LeftLeg : this.RightLeg;
    }

    public ModelPart getHead() {
        return this.Head;
    }

    public ModelPart getTorso() {
        return Torso;
    }

    @Nullable
    @Override
    public HelperModel getTransfurHelperModel(Limb limb) {
        return null;
    }
}
