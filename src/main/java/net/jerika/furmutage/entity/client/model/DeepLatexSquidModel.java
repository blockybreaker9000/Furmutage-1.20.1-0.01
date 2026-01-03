package net.jerika.furmutage.entity.client.model;

import net.jerika.furmutage.entity.custom.DeepLatexSquidEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for DeepLatexSquidEntity, copied from Changed mod's LatexSquidDogMaleModel.
 * Simplified to work without Changed mod's animation system.
 */
public class DeepLatexSquidModel<T extends DeepLatexSquidEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(furmutage.MOD_ID, "deep_latex_squid"), "main");
    
    private final ModelPart root;
    private final ModelPart Head;
    private final ModelPart Torso;
    private final ModelPart LeftArm;
    private final ModelPart RightArm;
    private final ModelPart LeftArm2;
    private final ModelPart RightArm2;
    private final ModelPart LeftLeg;
    private final ModelPart RightLeg;
    private final ModelPart Tail;
    
    // Leg parts for wolf bipedal animations
    private final ModelPart leftLowerLeg;
    private final ModelPart leftFoot;
    private final ModelPart leftPad;
    private final ModelPart rightLowerLeg;
    private final ModelPart rightFoot;
    private final ModelPart rightPad;
    
    // Tail parts for tail animations
    private final ModelPart tailPrimary;
    private final ModelPart tailSecondary;
    private final ModelPart tailTertiary;
    private final List<ModelPart> tailJoints;
    
    // Ear parts for ear animations
    private final ModelPart leftEar;
    private final ModelPart rightEar;
    
    // Tentacle parts for tentacle animations
    private final List<ModelPart> upperLeftTentacle;
    private final List<ModelPart> upperRightTentacle;
    private final List<ModelPart> lowerLeftTentacle;
    private final List<ModelPart> lowerRightTentacle;
    
    // Constants for animations (from Changed mod)
    private static final float SWAY_RATE = 0.33333334F * 0.25F;
    private static final float SWAY_SCALE = 0.025F;
    private static final float DRAG_SCALE = 0.75F;
    private static final float LEG_SPACING = 0.0f; // From animator
    private static final float HIP_OFFSET = -1.5f; // From animator
    private static final float LEG_LENGTH = 13.0f; // From animator

    public DeepLatexSquidModel(ModelPart root) {
        this.root = root;
        this.RightLeg = root.getChild("RightLeg");
        this.LeftLeg = root.getChild("LeftLeg");
        this.Head = root.getChild("Head");
        this.Torso = root.getChild("Torso");
        this.RightArm = root.getChild("RightArm");
        this.RightArm2 = root.getChild("RightArm2");
        this.LeftArm = root.getChild("LeftArm");
        this.LeftArm2 = root.getChild("LeftArm2");
        this.Tail = Torso.getChild("Tail");
        
        // Get leg parts (from LatexSquidDogMaleModel constructor)
        this.leftLowerLeg = LeftLeg.getChild("LeftLowerLeg");
        this.leftFoot = leftLowerLeg.getChild("LeftFoot");
        this.leftPad = leftFoot.getChild("LeftPad");
        this.rightLowerLeg = RightLeg.getChild("RightLowerLeg");
        this.rightFoot = rightLowerLeg.getChild("RightFoot");
        this.rightPad = rightFoot.getChild("RightPad");
        
        // Get tail parts
        this.tailPrimary = Tail.getChild("TailPrimary");
        this.tailSecondary = tailPrimary.getChild("TailSecondary");
        this.tailTertiary = tailSecondary.getChild("TailTertiary");
        this.tailJoints = List.of(tailPrimary, tailSecondary, tailTertiary);
        
        // Get ear parts
        this.leftEar = Head.getChild("LeftEar");
        this.rightEar = Head.getChild("RightEar");
        
        // Build tentacle lists (from LatexSquidDogMaleModel constructor)
        var upperRightTentacleList = new ArrayList<ModelPart>();
        upperRightTentacleList.add(Torso.getChild("RightUpperTentacle"));
        upperRightTentacleList.add(last(upperRightTentacleList).getChild("TentacleSecondaryRU"));
        upperRightTentacleList.add(last(upperRightTentacleList).getChild("TentacleTertiaryRU"));
        upperRightTentacleList.add(last(upperRightTentacleList).getChild("TentacleQuaternaryRU"));
        upperRightTentacleList.add(last(upperRightTentacleList).getChild("TentaclePadRU"));
        
        var upperLeftTentacleList = new ArrayList<ModelPart>();
        upperLeftTentacleList.add(Torso.getChild("LeftUpperTentacle"));
        upperLeftTentacleList.add(last(upperLeftTentacleList).getChild("TentacleSecondaryLU"));
        upperLeftTentacleList.add(last(upperLeftTentacleList).getChild("TentacleTertiaryLU"));
        upperLeftTentacleList.add(last(upperLeftTentacleList).getChild("TentacleQuaternaryLU"));
        upperLeftTentacleList.add(last(upperLeftTentacleList).getChild("TentaclePadLU"));
        
        var lowerRightTentacleList = new ArrayList<ModelPart>();
        lowerRightTentacleList.add(Torso.getChild("RightLowerTentacle"));
        lowerRightTentacleList.add(last(lowerRightTentacleList).getChild("TentacleSecondaryRL"));
        lowerRightTentacleList.add(last(lowerRightTentacleList).getChild("TentacleTertiaryRL"));
        lowerRightTentacleList.add(last(lowerRightTentacleList).getChild("TentacleQuaternaryRL"));
        lowerRightTentacleList.add(last(lowerRightTentacleList).getChild("TentaclePadRL"));
        
        var lowerLeftTentacleList = new ArrayList<ModelPart>();
        lowerLeftTentacleList.add(Torso.getChild("LeftLowerTentacle"));
        lowerLeftTentacleList.add(last(lowerLeftTentacleList).getChild("TentacleSecondaryLL"));
        lowerLeftTentacleList.add(last(lowerLeftTentacleList).getChild("TentacleTertiaryLL"));
        lowerLeftTentacleList.add(last(lowerLeftTentacleList).getChild("TentacleQuaternaryLL"));
        lowerLeftTentacleList.add(last(lowerLeftTentacleList).getChild("TentaclePadLL"));
        
        this.upperLeftTentacle = upperLeftTentacleList;
        this.upperRightTentacle = upperRightTentacleList;
        this.lowerLeftTentacle = lowerLeftTentacleList;
        this.lowerRightTentacle = lowerRightTentacleList;
    }
    
    private static <T> T last(List<T> list) {
        return list.get(list.size() - 1);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-2.7F, 9.3F, 0.0F));

        PartDefinition RightThigh_r1 = RightLeg.addOrReplaceChild("RightThigh_r1", CubeListBuilder.create().texOffs(48, 51).addBox(-2.0F, -0.9F, -2.55F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, 1.2F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition RightLowerLeg = RightLeg.addOrReplaceChild("RightLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.75F, -4.1F));

        PartDefinition RightCalf_r1 = RightLowerLeg.addOrReplaceChild("RightCalf_r1", CubeListBuilder.create().texOffs(56, 12).addBox(-1.99F, -0.9F, -2.4F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, -1.3F, 2.6F, 0.8727F, 0.0F, 0.0F));

        PartDefinition RightFoot = RightLowerLeg.addOrReplaceChild("RightFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.9F, 8.2F));

        PartDefinition RightArch_r1 = RightFoot.addOrReplaceChild("RightArch_r1", CubeListBuilder.create().texOffs(0, 64).addBox(-2.0F, -8.95F, -0.825F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.255F)), PartPose.offsetAndRotation(0.0F, 7.8F, -5.35F, -0.3491F, 0.0F, 0.0F));

        PartDefinition RightPad = RightFoot.addOrReplaceChild("RightPad", CubeListBuilder.create().texOffs(52, 32).addBox(-2.0F, 0.25F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 4.55F, -4.8F));

        PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(2.7F, 9.3F, 0.0F));

        PartDefinition LeftThigh_r1 = LeftLeg.addOrReplaceChild("LeftThigh_r1", CubeListBuilder.create().texOffs(48, 40).addBox(-2.0F, -0.9F, -2.55F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, 1.2F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition LeftLowerLeg = LeftLeg.addOrReplaceChild("LeftLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.75F, -4.1F));

        PartDefinition LeftCalf_r1 = LeftLowerLeg.addOrReplaceChild("LeftCalf_r1", CubeListBuilder.create().texOffs(48, 22).addBox(-2.01F, -0.9F, -2.4F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, -1.3F, 2.6F, 0.8727F, 0.0F, 0.0F));

        PartDefinition LeftFoot = LeftLowerLeg.addOrReplaceChild("LeftFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.9F, 8.2F));

        PartDefinition LeftArch_r1 = LeftFoot.addOrReplaceChild("LeftArch_r1", CubeListBuilder.create().texOffs(61, 59).addBox(-2.0F, -8.95F, -0.825F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.255F)), PartPose.offsetAndRotation(0.0F, 7.8F, -5.35F, -0.3491F, 0.0F, 0.0F));

        PartDefinition LeftPad = LeftFoot.addOrReplaceChild("LeftPad", CubeListBuilder.create().texOffs(24, 0).addBox(-2.0F, 0.25F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 4.55F, -4.8F));

        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, CubeDeformation.NONE)
                .texOffs(37, 0).addBox(-2.0F, -3.0F, -6.0F, 4.0F, 2.0F, 2.0F, CubeDeformation.NONE)
                .texOffs(0, 5).addBox(-1.5F, -1.0F, -5.0F, 3.0F, 1.0F, 1.0F, CubeDeformation.NONE), PartPose.offset(0.0F, -2.2F, 0.0F));

        PartDefinition RightEar = Head.addOrReplaceChild("RightEar", CubeListBuilder.create(), PartPose.offset(-3.0F, -7.5F, 0.0F));

        PartDefinition RightEarPivot = RightEar.addOrReplaceChild("RightEarPivot", CubeListBuilder.create().texOffs(40, 22).addBox(-1.9F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(42, 4).addBox(-0.9F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(0, 21).addBox(-0.9F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(24, 0).addBox(0.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

        PartDefinition LeftEar = Head.addOrReplaceChild("LeftEar", CubeListBuilder.create(), PartPose.offset(3.0F, -7.5F, 0.0F));

        PartDefinition LeftEarPivot = LeftEar.addOrReplaceChild("LeftEarPivot", CubeListBuilder.create().texOffs(12, 32).addBox(-1.1F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(48, 12).addBox(-1.1F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(24, 22).addBox(-1.1F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(24, 2).addBox(-1.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

        PartDefinition Hair = Head.addOrReplaceChild("Hair", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F))
                .texOffs(24, 8).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Torso = partdefinition.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(28, 28).addBox(-4.0F, 0.1F, -2.2F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -2.2F, 0.0F));

        PartDefinition Tail = Torso.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0F, 11.0F, 0.0F));

        PartDefinition TailPrimary = Tail.addOrReplaceChild("TailPrimary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition Base_r1 = TailPrimary.addOrReplaceChild("Base_r1", CubeListBuilder.create().texOffs(16, 56).addBox(-2.0F, 1.15F, -1.4F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1781F, 0.0F, 0.0F));

        PartDefinition TailSecondary = TailPrimary.addOrReplaceChild("TailSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.25F, 5.3F));

        PartDefinition Base_r2 = TailSecondary.addOrReplaceChild("Base_r2", CubeListBuilder.create().texOffs(48, 0).addBox(-2.5F, 0.45F, -2.1F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(0.0F, 0.5F, -0.8F, 1.4835F, 0.0F, 0.0F));

        PartDefinition TailTertiary = TailSecondary.addOrReplaceChild("TailTertiary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.25F, 5.2F));

        PartDefinition Base_r3 = TailTertiary.addOrReplaceChild("Base_r3", CubeListBuilder.create().texOffs(32, 60).addBox(-2.0F, 0.1F, -2.35F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.15F)), PartPose.offsetAndRotation(0.0F, -0.5F, 0.5F, 1.8326F, 0.0F, 0.0F));

        PartDefinition RightUpperTentacle = Torso.addOrReplaceChild("RightUpperTentacle", CubeListBuilder.create(), PartPose.offset(-2.5F, 2.7F, 1.0F));

        PartDefinition TentaclePart_r1 = RightUpperTentacle.addOrReplaceChild("TentaclePart_r1", CubeListBuilder.create().texOffs(68, 68).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.192F, -0.4363F, -0.0524F));

        PartDefinition TentacleSecondaryRU = RightUpperTentacle.addOrReplaceChild("TentacleSecondaryRU", CubeListBuilder.create(), PartPose.offset(-1.5F, -0.5F, 3.3F));

        PartDefinition TentaclePart_r2 = TentacleSecondaryRU.addOrReplaceChild("TentaclePart_r2", CubeListBuilder.create().texOffs(32, 22).addBox(0.0F, -1.0F, 3.55F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(1.5F, 0.5F, -3.3F, 0.2269F, -0.6981F, -0.1134F));

        PartDefinition TentacleTertiaryRU = TentacleSecondaryRU.addOrReplaceChild("TentacleTertiaryRU", CubeListBuilder.create(), PartPose.offset(-2.4F, -0.7F, 2.7F));

        PartDefinition TentaclePart_r3 = TentacleTertiaryRU.addOrReplaceChild("TentaclePart_r3", CubeListBuilder.create().texOffs(56, 68).addBox(1.8F, -1.0F, 6.75F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(3.9F, 1.2F, -6.0F, 0.2967F, -0.9425F, -0.2094F));

        PartDefinition TentacleQuaternaryRU = TentacleTertiaryRU.addOrReplaceChild("TentacleQuaternaryRU", CubeListBuilder.create(), PartPose.offset(-3.0F, -0.5F, 2.2F));

        PartDefinition TentaclePart_r4 = TentacleQuaternaryRU.addOrReplaceChild("TentaclePart_r4", CubeListBuilder.create().texOffs(34, 68).addBox(4.45F, -1.0F, 9.375F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(6.9F, 1.7F, -8.2F, 0.4712F, -1.1868F, -0.4102F));

        PartDefinition TentaclePadRU = TentacleQuaternaryRU.addOrReplaceChild("TentaclePadRU", CubeListBuilder.create(), PartPose.offset(-3.6F, -0.4F, 1.3F));

        PartDefinition TentaclePart_r5 = TentaclePadRU.addOrReplaceChild("TentaclePart_r5", CubeListBuilder.create().texOffs(0, 16).addBox(6.075F, -1.5F, 16.4F, 2.0F, 3.0F, 2.0F, CubeDeformation.NONE)
                .texOffs(64, 22).addBox(6.075F, -2.5F, 12.4F, 2.0F, 5.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(10.5F, 2.1F, -9.5F, 0.6807F, -1.2915F, -0.6283F));

        PartDefinition RightLowerTentacle = Torso.addOrReplaceChild("RightLowerTentacle", CubeListBuilder.create(), PartPose.offset(-2.5F, 6.7F, 1.0F));

        PartDefinition TentaclePart_r6 = RightLowerTentacle.addOrReplaceChild("TentaclePart_r6", CubeListBuilder.create().texOffs(72, 44).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.192F, -0.4276F, 0.0524F));

        PartDefinition TentacleSecondaryRL = RightLowerTentacle.addOrReplaceChild("TentacleSecondaryRL", CubeListBuilder.create(), PartPose.offset(-1.5F, 0.5F, 3.3F));

        PartDefinition TentaclePart_r7 = TentacleSecondaryRL.addOrReplaceChild("TentaclePart_r7", CubeListBuilder.create().texOffs(65, 31).addBox(0.0F, -1.0F, 3.6F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(1.5F, -0.5F, -3.3F, -0.2182F, -0.6894F, 0.1134F));

        PartDefinition TentacleTertiaryRL = TentacleSecondaryRL.addOrReplaceChild("TentacleTertiaryRL", CubeListBuilder.create(), PartPose.offset(-2.4F, 0.7F, 2.9F));

        PartDefinition TentaclePart_r8 = TentacleTertiaryRL.addOrReplaceChild("TentaclePart_r8", CubeListBuilder.create().texOffs(72, 37).addBox(1.9F, -1.05F, 6.8F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(3.9F, -1.2F, -6.2F, -0.2967F, -0.9425F, 0.2094F));

        PartDefinition TentacleQuaternaryRL = TentacleTertiaryRL.addOrReplaceChild("TentacleQuaternaryRL", CubeListBuilder.create(), PartPose.offset(-2.9F, 0.4F, 2.0F));

        PartDefinition TentaclePart_r9 = TentacleQuaternaryRL.addOrReplaceChild("TentaclePart_r9", CubeListBuilder.create().texOffs(72, 18).addBox(4.525F, -1.0F, 9.3F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(6.8F, -1.6F, -8.2F, -0.4712F, -1.1868F, 0.4102F));

        PartDefinition TentaclePadRL = TentacleQuaternaryRL.addOrReplaceChild("TentaclePadRL", CubeListBuilder.create(), PartPose.offset(-3.7F, 0.3F, 1.3F));

        PartDefinition TentaclePart_r10 = TentaclePadRL.addOrReplaceChild("TentaclePart_r10", CubeListBuilder.create().texOffs(0, 73).addBox(6.15F, -1.5F, 16.4F, 2.0F, 3.0F, 2.0F, CubeDeformation.NONE)
                .texOffs(64, 48).addBox(6.15F, -2.5F, 12.4F, 2.0F, 5.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(10.5F, -1.9F, -9.5F, -0.6807F, -1.2915F, 0.6283F));

        PartDefinition LeftUpperTentacle = Torso.addOrReplaceChild("LeftUpperTentacle", CubeListBuilder.create(), PartPose.offset(2.5F, 2.7F, 1.0F));

        PartDefinition TentaclePart_r11 = LeftUpperTentacle.addOrReplaceChild("TentaclePart_r11", CubeListBuilder.create().texOffs(68, 6).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.192F, 0.4363F, 0.0524F));

        PartDefinition TentacleSecondaryLU = LeftUpperTentacle.addOrReplaceChild("TentacleSecondaryLU", CubeListBuilder.create(), PartPose.offset(1.5F, -0.5F, 3.3F));

        PartDefinition TentaclePart_r12 = TentacleSecondaryLU.addOrReplaceChild("TentaclePart_r12", CubeListBuilder.create().texOffs(16, 32).addBox(-2.0F, -1.0F, 3.55F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-1.5F, 0.5F, -3.3F, 0.2269F, 0.6981F, 0.1134F));

        PartDefinition TentacleTertiaryLU = TentacleSecondaryLU.addOrReplaceChild("TentacleTertiaryLU", CubeListBuilder.create(), PartPose.offset(2.4F, -0.7F, 2.7F));

        PartDefinition TentaclePart_r13 = TentacleTertiaryLU.addOrReplaceChild("TentaclePart_r13", CubeListBuilder.create().texOffs(68, 0).addBox(-3.8F, -1.0F, 6.75F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-3.9F, 1.2F, -6.0F, 0.2967F, 0.9425F, 0.2094F));

        PartDefinition TentacleQuaternaryLU = TentacleTertiaryLU.addOrReplaceChild("TentacleQuaternaryLU", CubeListBuilder.create(), PartPose.offset(3.0F, -0.5F, 2.2F));

        PartDefinition TentaclePart_r14 = TentacleQuaternaryLU.addOrReplaceChild("TentaclePart_r14", CubeListBuilder.create().texOffs(22, 67).addBox(-6.45F, -1.0F, 9.375F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-6.9F, 1.7F, -8.2F, 0.4712F, 1.1868F, 0.4102F));

        PartDefinition TentaclePadLU = TentacleQuaternaryLU.addOrReplaceChild("TentaclePadLU", CubeListBuilder.create(), PartPose.offset(3.6F, -0.4F, 1.3F));

        PartDefinition TentaclePart_r15 = TentaclePadLU.addOrReplaceChild("TentaclePart_r15", CubeListBuilder.create().texOffs(0, 0).addBox(-8.075F, -1.5F, 16.4F, 2.0F, 3.0F, 2.0F, CubeDeformation.NONE)
                .texOffs(48, 62).addBox(-8.075F, -2.5F, 12.4F, 2.0F, 5.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-10.5F, 2.1F, -9.5F, 0.6807F, 1.2915F, 0.6283F));

        PartDefinition LeftLowerTentacle = Torso.addOrReplaceChild("LeftLowerTentacle", CubeListBuilder.create(), PartPose.offset(2.5F, 6.7F, 1.0F));

        PartDefinition TentaclePart_r16 = LeftLowerTentacle.addOrReplaceChild("TentaclePart_r16", CubeListBuilder.create().texOffs(72, 12).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.192F, 0.4276F, -0.0524F));

        PartDefinition TentacleSecondaryLL = LeftLowerTentacle.addOrReplaceChild("TentacleSecondaryLL", CubeListBuilder.create(), PartPose.offset(1.5F, 0.5F, 3.3F));

        PartDefinition TentaclePart_r17 = TentacleSecondaryLL.addOrReplaceChild("TentaclePart_r17", CubeListBuilder.create().texOffs(14, 65).addBox(-2.0F, -1.0F, 3.6F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-1.5F, -0.5F, -3.3F, -0.2182F, 0.6894F, -0.1134F));

        PartDefinition TentacleTertiaryLL = TentacleSecondaryLL.addOrReplaceChild("TentacleTertiaryLL", CubeListBuilder.create(), PartPose.offset(2.4F, 0.7F, 2.9F));

        PartDefinition TentaclePart_r18 = TentacleTertiaryLL.addOrReplaceChild("TentaclePart_r18", CubeListBuilder.create().texOffs(42, 71).addBox(-3.9F, -1.05F, 6.8F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-3.9F, -1.2F, -6.2F, -0.2967F, 0.9425F, -0.2094F));

        PartDefinition TentacleQuaternaryLL = TentacleTertiaryLL.addOrReplaceChild("TentacleQuaternaryLL", CubeListBuilder.create(), PartPose.offset(2.9F, 0.4F, 2.0F));

        PartDefinition TentaclePart_r19 = TentacleQuaternaryLL.addOrReplaceChild("TentaclePart_r19", CubeListBuilder.create().texOffs(10, 71).addBox(-6.525F, -1.0F, 9.3F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-6.8F, -1.6F, -8.2F, -0.4712F, 1.1868F, -0.4102F));

        PartDefinition TentaclePadLL = TentacleQuaternaryLL.addOrReplaceChild("TentaclePadLL", CubeListBuilder.create(), PartPose.offset(3.7F, 0.3F, 1.3F));

        PartDefinition TentaclePart_r20 = TentaclePadLL.addOrReplaceChild("TentaclePart_r20", CubeListBuilder.create().texOffs(72, 57).addBox(-8.15F, -1.5F, 16.4F, 2.0F, 3.0F, 2.0F, CubeDeformation.NONE)
                .texOffs(64, 39).addBox(-8.15F, -2.5F, 12.4F, 2.0F, 5.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-10.5F, -1.9F, -9.5F, -0.6807F, 1.2915F, -0.6283F));

        PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(32, 44).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(-5.2F, 3.9F, -0.2F));

        PartDefinition RightArm2 = partdefinition.addOrReplaceChild("RightArm2", CubeListBuilder.create().texOffs(0, 48).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(-5.2F, -0.1F, -0.2F));

        PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(16, 40).addBox(-0.8F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(5.0F, 3.9F, -0.2F));

        PartDefinition LeftArm2 = partdefinition.addOrReplaceChild("LeftArm2", CubeListBuilder.create().texOffs(0, 32).addBox(-0.8F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(5.0F, -0.1F, -0.2F));

        return LayerDefinition.create(meshdefinition, 96, 96);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // Calculate swim amount (0.0 = not swimming, 1.0 = fully swimming)
        float swimAmount = entity.isSwimming() && entity.isInWater() ? 1.0f : 0.0f;
        
        // Head rotation (from WolfHeadInitAnimator)
        this.Head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.Head.xRot = headPitch * ((float)Math.PI / 180F);
        
        // Clamp head rotation
        this.Head.yRot = Mth.clamp(this.Head.yRot, -0.5f, 0.5f);
        this.Head.xRot = Mth.clamp(this.Head.xRot, -0.5f, 0.5f);
        
        // Wolf Bipedal Animations (from WolfBipedalInitAnimator and WolfBipedalSwimAnimator)
        boolean fallFlying = entity.getFallFlyingTicks() > 4;
        float swingSpeed = 1.0F;
        if (fallFlying) {
            swingSpeed = (float)entity.getDeltaMovement().lengthSqr();
            swingSpeed /= 0.2F;
            swingSpeed *= swingSpeed * swingSpeed;
        }
        if (swingSpeed < 1.0F) {
            swingSpeed = 1.0F;
        }
        
        if (swimAmount > 0.0f) {
            // Swimming leg animations (from WolfBipedalSwimAnimator)
            LeftLeg.xRot = Mth.lerp(swimAmount, LeftLeg.xRot, 0.3F * Mth.cos(limbSwing * 0.33333334F + (float)Math.PI) + 0.2181662F);
            RightLeg.xRot = Mth.lerp(swimAmount, RightLeg.xRot, 0.3F * Mth.cos(limbSwing * 0.33333334F) + 0.2181662F);
            
            rightLowerLeg.xRot = Mth.lerp(swimAmount, rightLowerLeg.xRot, -0.6544985F);
            leftLowerLeg.xRot = Mth.lerp(swimAmount, leftLowerLeg.xRot, -0.6544985F);
            rightFoot.xRot = Mth.lerp(swimAmount, rightFoot.xRot, 0.3926991F);
            leftFoot.xRot = Mth.lerp(swimAmount, leftFoot.xRot, 0.3926991F);
            rightPad.xRot = Mth.lerp(swimAmount, rightPad.xRot, 0.3490659F);
            leftPad.xRot = Mth.lerp(swimAmount, leftPad.xRot, 0.3490659F);
        } else {
            // Walking leg animations (from WolfBipedalInitAnimator)
            RightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / swingSpeed;
            LeftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount / swingSpeed;
            RightLeg.yRot = 0.0F;
            LeftLeg.yRot = 0.0F;
            
            RightLeg.x = -(2.2f + LEG_SPACING);
            LeftLeg.x = (2.2f + LEG_SPACING);
            RightLeg.zRot = Mth.PI * 0.015f;
            LeftLeg.zRot = Mth.PI * -0.015f;
            
            RightLeg.xRot += Mth.lerp(limbSwingAmount, Mth.PI * 0.03f, 0.0F);
            LeftLeg.xRot += Mth.lerp(limbSwingAmount, Mth.PI * -0.03f, 0.0F);
            
            rightLowerLeg.xRot = Mth.map(RightLeg.xRot, -1.1F, 1.1F, -0.08726646F, 0.08726646F);
            leftLowerLeg.xRot = Mth.map(LeftLeg.xRot, -1.1F, 1.1F, -0.08726646F, 0.08726646F);
            rightFoot.xRot = Mth.map(-RightLeg.xRot, -1.1F, 1.1F, -0.1745329F, 0.1745329F);
            leftFoot.xRot = Mth.map(-LeftLeg.xRot, -1.1F, 1.1F, -0.1745329F, 0.1745329F);
            
            rightPad.xRot = Mth.clamp(-RightLeg.xRot, -0.2617994f, 0.2617994f);
            rightPad.zRot = Mth.clamp(-RightLeg.zRot, -0.2617994f, 0.2617994f);
            leftPad.xRot = Mth.clamp(-LeftLeg.xRot, -0.2617994f, 0.2617994f);
            leftPad.zRot = Mth.clamp(-LeftLeg.zRot, -0.2617994f, 0.2617994f);
        }
        
        // Arm swim animations (from ArmSwimAnimator)
        if (swimAmount > 0.0f) {
            float armSwimRot = ageInTicks * 0.1f;
            RightArm2.xRot = Mth.lerp(swimAmount, RightArm2.xRot, Mth.cos(armSwimRot) * 0.5f);
            LeftArm2.xRot = Mth.lerp(swimAmount, LeftArm2.xRot, Mth.cos(armSwimRot + (float)Math.PI) * 0.5f);
        } else {
            // Basic arm animations
            RightArm2.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
            LeftArm2.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        }
        
        // Double arm bob animations (from DoubleArmBobAnimator)
        float armBob = Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        RightArm.xRot += armBob;
        LeftArm.xRot += armBob;
        
        // Tail animations (from TailSwimAnimator and basic tail movement)
        if (swimAmount > 0.0f) {
            Tail.xRot = Mth.lerp(swimAmount, Tail.xRot, -1.1f);
            float oldZ = Tail.zRot;
            Tail.zRot = Mth.lerp(swimAmount, 0.0F, Tail.yRot);
            Tail.yRot = Mth.lerp(swimAmount, oldZ, 0.0F);
        } else {
            // Basic tail sway
            Tail.yRot = Mth.cos(limbSwing * 0.6662F) * 0.2F * limbSwingAmount;
        }
        
        // Ear animations (from WolfEarsInitAnimator - simplified)
        float earRot = Mth.lerp(0.85f, -0.08726646F, 0.04363323F);
        rightEar.zRot = earRot;
        leftEar.zRot = -earRot;
        rightEar.yRot = Mth.clamp(Tail.yRot * 0.5f, -Mth.PI / 8.0f, Mth.PI / 4.0f);
        leftEar.yRot = Mth.clamp(Tail.yRot * 0.5f, -Mth.PI / 4.0f, Mth.PI / 8.0f);
        
        // Tentacle animations
        if (swimAmount > 0.0f) {
            // Swimming tentacles (from SquidDogTentaclesSwimAnimator)
            swimTentacle(upperLeftTentacle, ageInTicks, -20.0f * Mth.DEG_TO_RAD, 60.0f * Mth.DEG_TO_RAD, swimAmount);
            swimTentacle(upperRightTentacle, ageInTicks, -20.0f * Mth.DEG_TO_RAD, -60.0f * Mth.DEG_TO_RAD, swimAmount);
            swimTentacle(lowerLeftTentacle, ageInTicks, -22.5f * Mth.DEG_TO_RAD, 60.0f * Mth.DEG_TO_RAD, swimAmount);
            swimTentacle(lowerRightTentacle, ageInTicks, -22.5f * Mth.DEG_TO_RAD, -60.0f * Mth.DEG_TO_RAD, swimAmount);
        } else {
            // Idle tentacles (from SquidDogTentaclesInitAnimator - simplified)
            float tentacleSway = SWAY_SCALE * Mth.cos(ageInTicks * SWAY_RATE + (((float)Math.PI / 3.0F) * 0.75f));
            float tentacleBalance = Mth.cos(limbSwing * 0.6662F) * 0.125F * limbSwingAmount / swingSpeed;
            
            idleTentacle(upperLeftTentacle, limbSwingAmount, ageInTicks, tentacleSway, tentacleBalance, 0.0f);
            idleTentacle(upperRightTentacle, limbSwingAmount, ageInTicks, tentacleSway, tentacleBalance, 0.0f);
            idleTentacle(lowerLeftTentacle, limbSwingAmount, ageInTicks, tentacleSway, tentacleBalance, 0.0f);
            idleTentacle(lowerRightTentacle, limbSwingAmount, ageInTicks, tentacleSway, tentacleBalance, 0.0f);
            
            // Vertical bend for upper tentacles
            bendVerticalTentacle(upperLeftTentacle, -12.5f * Mth.DEG_TO_RAD);
            bendVerticalTentacle(upperRightTentacle, 12.5f * Mth.DEG_TO_RAD);
            
            // Bob animations (from SquidDogTentaclesBobAnimator)
            float bobScale = Mth.lerp(limbSwingAmount, 0.9f, 0.125f);
            bobTentacle(upperLeftTentacle, ageInTicks * 0.8f, bobScale);
            bobTentacle(upperRightTentacle, ageInTicks * 1.1f, -bobScale);
            bobTentacle(lowerLeftTentacle, ageInTicks * 1.2f, -bobScale);
            bobTentacle(lowerRightTentacle, ageInTicks * 0.9f, bobScale);
        }
    }
    
    /**
     * Animate tentacles during swimming (from AbstractTentaclesAnimator.swimTentacle)
     */
    private void swimTentacle(List<ModelPart> tentacle, float ageInTicks, float yAngle, float zAngle, float swimAmount) {
        if (tentacle.isEmpty()) return;
        
        var first = tentacle.get(0);
        first.yRot = Mth.lerp(swimAmount, first.yRot, yAngle);
        first.zRot = Mth.lerp(swimAmount, first.zRot, zAngle);
        
        float offset = 0.0F;
        for (ModelPart joint : tentacle) {
            joint.yRot = Mth.lerp(swimAmount, joint.yRot, SWAY_SCALE * Mth.cos(ageInTicks * SWAY_RATE -
                    (((float)Math.PI / 3.0F) * offset)));
            offset += 0.75F;
        }
    }
    
    /**
     * Idle tentacle animation (from AbstractTentaclesAnimator.idleTentacle)
     */
    private void idleTentacle(List<ModelPart> tentacle, float limbSwingAmount, float ageInTicks, float tentacleSway, float balance, float tentacleDrag) {
        float offset = 0.0F;
        for (ModelPart joint : tentacle) {
            joint.yRot = Mth.lerp(limbSwingAmount, SWAY_SCALE * Mth.cos(ageInTicks * SWAY_RATE -
                    (((float)Math.PI / 3.0F) * offset)), 0.0f) + tentacleDrag * DRAG_SCALE;
            offset += 0.75F;
        }
    }
    
    /**
     * Bob tentacle animation (from AbstractTentaclesAnimator.bobTentacle)
     */
    private void bobTentacle(List<ModelPart> tentacle, float ageInTicks, float scale) {
        for (ModelPart joint : tentacle) {
            joint.yRot += scale * (Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F);
            joint.xRot += scale * Mth.sin(ageInTicks * 0.067F) * 0.05F;
        }
    }
    
    /**
     * Bend tentacle vertically (from AbstractTentaclesAnimator.bendVerticalTentacle)
     */
    private void bendVerticalTentacle(List<ModelPart> tentacle, float scale) {
        for (ModelPart joint : tentacle) {
            joint.zRot += scale;
        }
    }
    
    /**
     * Reset tentacle to default pose (from AbstractTentaclesAnimator.resetTentacle)
     */
    private void resetTentacle(List<ModelPart> tentacle) {
        for (ModelPart joint : tentacle) {
            joint.xRot = 0.0f;
            joint.yRot = 0.0f;
            joint.zRot = 0.0f;
        }
    }
}

