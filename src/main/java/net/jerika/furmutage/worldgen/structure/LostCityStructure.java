package net.jerika.furmutage.worldgen.structure;

import com.mojang.serialization.Codec;
import net.jerika.furmutage.furmutage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;

public class LostCityStructure extends Structure {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = 
            DeferredRegister.create(Registries.STRUCTURE_TYPE, furmutage.MOD_ID);
    
    public static final Codec<LostCityStructure> CODEC = simpleCodec(LostCityStructure::new);
    
    public static final RegistryObject<StructureType<LostCityStructure>> LOST_CITY_TYPE = 
            STRUCTURE_TYPES.register("lost_city", () -> () -> CODEC);
    
    public LostCityStructure(StructureSettings settings) {
        super(settings);
    }
    
    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, (builder) -> {
            generatePieces(builder, context);
        });
    }
    
    private void generatePieces(StructurePiecesBuilder builder, GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        BlockPos blockPos = new BlockPos(
                chunkPos.getMinBlockX(),
                context.chunkGenerator().getBaseHeight(
                        chunkPos.getMinBlockX(),
                        chunkPos.getMinBlockZ(),
                        Heightmap.Types.WORLD_SURFACE_WG,
                        context.heightAccessor(),
                        context.randomState()
                ),
                chunkPos.getMinBlockZ()
        );
        
        // Add the lost city piece
        LostCityPieces.addPieces(context.structureTemplateManager(), blockPos, context.random(), builder);
    }
    
    @Override
    public StructureType<?> type() {
        return LOST_CITY_TYPE.get();
    }
}
