package net.jerika.furmutage.ai;

import net.jerika.furmutage.block.custom.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.EnumSet;

/**
 * Custom goal that allows the entity to eat only tainted white grass foliage blocks,
 * not normal grass blocks.
 */
public class TaintedGrassFoliageEatGoal extends Goal {
    private final Mob mob;
    private final Level level;
    private int eatAnimationTick;

    public TaintedGrassFoliageEatGoal(Mob mob) {
        this.mob = mob;
        this.level = mob.level();
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (this.mob.getRandom().nextInt(this.mob.isBaby() ? 50 : 1000) != 0) {
            return false;
        } else {
            BlockPos blockpos = this.mob.blockPosition();
            // Check if standing on tainted grass foliage
            if (this.level.getBlockState(blockpos).is(ModBlocks.TAINTED_WHITE_GRASS_FOLIAGE.get())) {
                return true;
            } else {
                // Check if standing on tainted grass block with foliage above
                BlockPos belowPos = blockpos.below();
                BlockState belowState = this.level.getBlockState(belowPos);
                if (belowState.is(ModBlocks.TAINTED_WHITE_GRASS.get())) {
                    BlockState aboveState = this.level.getBlockState(blockpos);
                    return aboveState.is(ModBlocks.TAINTED_WHITE_GRASS_FOLIAGE.get());
                }
                return false;
            }
        }
    }

    @Override
    public void start() {
        this.eatAnimationTick = 40;
        this.level.broadcastEntityEvent(this.mob, (byte)10);
        this.mob.getNavigation().stop();
    }

    @Override
    public void stop() {
        this.eatAnimationTick = 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.eatAnimationTick > 0;
    }

    @Override
    public void tick() {
        this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
        if (this.eatAnimationTick == 4) {
            BlockPos blockpos = this.mob.blockPosition();
            BlockState blockstate = this.level.getBlockState(blockpos);
            
            // Check if standing on tainted grass foliage
            if (blockstate.is(ModBlocks.TAINTED_WHITE_GRASS_FOLIAGE.get())) {
                if (this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                    this.level.destroyBlock(blockpos, false);
                }
                this.mob.ate();
                this.mob.gameEvent(GameEvent.EAT);
            } else {
                // Check if there's foliage above the block we're standing on
                BlockPos belowPos = blockpos.below();
                BlockState belowState = this.level.getBlockState(belowPos);
                if (belowState.is(ModBlocks.TAINTED_WHITE_GRASS.get())) {
                    BlockState foliageState = this.level.getBlockState(blockpos);
                    if (foliageState.is(ModBlocks.TAINTED_WHITE_GRASS_FOLIAGE.get())) {
                        if (this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                            this.level.destroyBlock(blockpos, false);
                        }
                        this.mob.ate();
                        this.mob.gameEvent(GameEvent.EAT);
                    }
                }
            }
        }
    }
}
