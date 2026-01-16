package net.jerika.furmutage.entity.custom;

import net.jerika.furmutage.ai.ChangedEntityImprovedPathfindingGoal;
import net.jerika.furmutage.ai.ChangedStyleLeapAtTargetGoal;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbilityInstance;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.entity.AttributePresets;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;

public class DeepCaveHypnoCat extends ChangedEntity implements GenderedEntity {
    protected final SimpleAbilityInstance hypnosis;

    public DeepCaveHypnoCat(EntityType<? extends DeepCaveHypnoCat> type, Level level) {
        super(type, level);
        hypnosis = registerAbility(ability -> this.wantToHypno(), new SimpleAbilityInstance(ChangedAbilities.HYPNOSIS.get(), IAbstractChangedEntity.forEntity(this)));
        this.setMaxUpStep(2.0F); // Increased step height for climbing
    }

    @Override
    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);
        AttributePresets.catLike(attributes);
        // Make hyper aggressive - increase stats
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(64.0D); // Increased follow range
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(8.0D); // Increased attack damage
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(0.4D); // Faster movement
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        // Changed mod style AI goals - matching priority order with other Furmutage entities
        // Priority 1: Melee attack goal
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false));
        
        // Priority 2: Improved pathfinding for jumping onto blocks
        this.goalSelector.addGoal(2, new ChangedEntityImprovedPathfindingGoal(this));
        
        // Priority 3: Random stroll
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.3, 120, false));
        
        // Priority 4: Leap at target (only when target is above)
        this.goalSelector.addGoal(4, new ChangedStyleLeapAtTargetGoal(this, 0.4f));
        
        // Priority 5: Open doors (if has ground navigation)
        if (GoalUtils.hasGroundPathNavigation(this))
            this.goalSelector.addGoal(5, new OpenDoorGoal(this, true));
        
        // Priority 7: Look at player
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 7.0F));
        
        // Priority 8: Random look around
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        
        // Priority 9: Look at villager
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Villager.class, 7.0F, 0.2F));
        
        // Target priorities - Changed mod style
        // Priority 1: Hurt by target
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        
        // Priority 2: Target players
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public int getTicksRequiredToFreeze() { return 200; }

    @Override
    public Gender getGender() {
        return Gender.MALE;
    }

    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.REPLICATION;
    }

    public boolean wantToHypno() {
        return getTarget() != null;
    }

    public Color3 getTransfurColor(TransfurCause cause) {
        return Color3.getColor("#333333");
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ChangedEntity.createLatexAttributes();
    }

    // Custom spawn rule for deepslate caves
    public static boolean checkDeepCaveSpawnRules(EntityType<DeepCaveHypnoCat> entityType, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
        // Allow spawning via spawn eggs and other non-natural methods
        if (reason != MobSpawnType.NATURAL) {
            return true;
        }

        // Must be between Y level 0 and -60
        int y = pos.getY();
        if (y > 0 || y < -60) {
            return false;
        }

        // Must be dark enough
        if (world.getBrightness(LightLayer.SKY, pos) > random.nextInt(50)) {
            return false;
        }
        if (world.getBrightness(LightLayer.BLOCK, pos) > 0) {
            return false;
        }
        if (ChangedEntity.getLevelBrightnessAt(world.getLevel(), pos) > random.nextInt(10)) {
            return false;
        }

        // Random chance
        if (random.nextFloat() > 0.5f) {
            return false;
        }

        return net.minecraft.world.entity.monster.Monster.checkMonsterSpawnRules(entityType, world, reason, pos, random);
    }
}
