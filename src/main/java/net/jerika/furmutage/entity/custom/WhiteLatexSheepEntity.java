package net.jerika.furmutage.entity.custom;

import net.jerika.furmutage.ai.latex_beast_ai.ChangedEntityImprovedPathfindingGoal;
import net.jerika.furmutage.ai.TaintedGrassFoliageEatGoal;
import net.jerika.furmutage.sound.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * White latex infected version of a sheep. Now hostile.
 */
public class WhiteLatexSheepEntity extends Sheep {
    public WhiteLatexSheepEntity(EntityType<? extends Sheep> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void registerGoals() {
        // Call super to initialize parent class fields (like eatBlockGoal)
        super.registerGoals();
        
        // Remove all goals using predicate (correct method for 1.20.1)
        this.goalSelector.removeAllGoals(goal -> true);
        this.targetSelector.removeAllGoals(goal -> true);
        
        // Register hostile goals
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new ChangedEntityImprovedPathfindingGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(4, new TaintedGrassFoliageEatGoal(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        
        // Hostile targeting

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Sheep.createAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 60.0D);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.WHITE_LATEX_SHEEP_AMBIENT.get();
    }

    @Override
    public void shear(SoundSource pSource) {
        // Call super to handle normal shearing behavior (drops 1 wool, sets sheared flag, plays sound, etc.)
        super.shear(pSource);
        
        // Drop an additional wool item (double wool) - drop same color as sheep
        if (!this.level().isClientSide) {
            net.minecraft.world.item.DyeColor dyeColor = this.getColor();
            ItemStack woolStack = getWoolItemForColor(dyeColor);
            this.spawnAtLocation(woolStack);
        }
    }
    
    /**
     * Maps a DyeColor to the corresponding wool ItemStack.
     */
    private ItemStack getWoolItemForColor(net.minecraft.world.item.DyeColor color) {
        if (color == null) {
            return new ItemStack(Items.WHITE_WOOL, 1);
        }
        
        return switch (color) {
            case WHITE -> new ItemStack(Items.WHITE_WOOL, 1);
            case ORANGE -> new ItemStack(Items.ORANGE_WOOL, 1);
            case MAGENTA -> new ItemStack(Items.MAGENTA_WOOL, 1);
            case LIGHT_BLUE -> new ItemStack(Items.LIGHT_BLUE_WOOL, 1);
            case YELLOW -> new ItemStack(Items.YELLOW_WOOL, 1);
            case LIME -> new ItemStack(Items.LIME_WOOL, 1);
            case PINK -> new ItemStack(Items.PINK_WOOL, 1);
            case GRAY -> new ItemStack(Items.GRAY_WOOL, 1);
            case LIGHT_GRAY -> new ItemStack(Items.LIGHT_GRAY_WOOL, 1);
            case CYAN -> new ItemStack(Items.CYAN_WOOL, 1);
            case PURPLE -> new ItemStack(Items.PURPLE_WOOL, 1);
            case BLUE -> new ItemStack(Items.BLUE_WOOL, 1);
            case BROWN -> new ItemStack(Items.BROWN_WOOL, 1);
            case GREEN -> new ItemStack(Items.GREEN_WOOL, 1);
            case RED -> new ItemStack(Items.RED_WOOL, 1);
            case BLACK -> new ItemStack(Items.BLACK_WOOL, 1);
        };
    }
}

