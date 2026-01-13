package net.jerika.furmutage.event;

import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RoselightShearsAreaShearingEvents {
    
    private static final int SHEAR_RADIUS = 8; // Radius in blocks to search for sheep
    private static final double EXTRA_WOOL_MULTIPLIER = 1.5; // 50% more wool
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        if (hand == null) {
            return;
        }
        
        ItemStack heldItem = player.getItemInHand(hand);
        
        // Check if player is holding the roselight shears
        if (heldItem.getItem() != ModItems.ROSELIGHT_SHEARS.get()) {
            return;
        }
        
        // Only process if interacting with a sheep
        if (!(event.getTarget() instanceof Sheep targetSheep)) {
            return;
        }
        
        Level level = event.getLevel();
        BlockPos centerPos = targetSheep.blockPosition();
        
        // Find all sheep in the area
        AABB searchArea = new AABB(centerPos).inflate(SHEAR_RADIUS);
        List<Sheep> nearbySheep = level.getEntitiesOfClass(Sheep.class, searchArea);
        
        int sheepSheared = 0;
        
        // Shear all nearby unsheared sheep
        for (Sheep sheep : nearbySheep) {
            if (!sheep.isSheared() && sheep.isAlive()) {
                // Get the wool color and convert to wool item
                DyeColor woolColor = sheep.getColor();
                ItemStack woolStack = getWoolStackForColor(woolColor);
                
                // Calculate extra wool (round up)
                int woolAmount = (int) Math.ceil(woolStack.getCount() * EXTRA_WOOL_MULTIPLIER);
                woolStack.setCount(woolAmount);
                
                // Drop wool at sheep location
                sheep.spawnAtLocation(woolStack);
                
                // Set sheep as sheared
                sheep.setSheared(true);
                
                // Play shear sound
                level.playSound(null, sheep, SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1.0F, 1.0F);
                
                sheepSheared++;
            }
        }
        
        // Damage the shears (only once, not per sheep)
        if (sheepSheared > 0 && !player.getAbilities().instabuild) {
            heldItem.hurtAndBreak(1, player, (entity) -> {
                entity.broadcastBreakEvent(hand == InteractionHand.MAIN_HAND 
                    ? EquipmentSlot.MAINHAND 
                    : EquipmentSlot.OFFHAND);
            });
        }
        
        // Cancel the event to prevent normal shearing
        if (sheepSheared > 0) {
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }
    
    private static ItemStack getWoolStackForColor(DyeColor color) {
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
