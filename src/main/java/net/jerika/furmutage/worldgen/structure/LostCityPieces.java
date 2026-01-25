package net.jerika.furmutage.worldgen.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class LostCityPieces {
    // Create a simple structure piece type using a lambda
    private static final StructurePieceType LOST_CITY_PIECE_TYPE = 
            (context, tag) -> new LostCityPiece(tag);
    
    public static void addPieces(StructureTemplateManager templateManager, BlockPos pos, RandomSource random, StructurePieceAccessor pieces) {
        pieces.addPiece(new LostCityPiece(pos, random));
    }
    
    public static class LostCityPiece extends net.minecraft.world.level.levelgen.structure.StructurePiece {
        private final RandomSource random;
        
        public LostCityPiece(BlockPos pos, RandomSource random) {
            super(LOST_CITY_PIECE_TYPE, 0, createBoundingBox(pos));
            this.random = random;
        }
        
        public LostCityPiece(CompoundTag tag) {
            super(LOST_CITY_PIECE_TYPE, tag);
            this.random = net.minecraft.util.RandomSource.create();
        }
        
        private static BoundingBox createBoundingBox(BlockPos pos) {
            // Create a bounding box for a city (roughly 64x64 blocks)
            int size = 64;
            return new BoundingBox(
                    pos.getX() - size / 2,
                    pos.getY() - 10,
                    pos.getZ() - size / 2,
                    pos.getX() + size / 2,
                    pos.getY() + 30,
                    pos.getZ() + size / 2
            );
        }
        
        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
            // Save any additional data if needed
        }
        
        @Override
        public void postProcess(net.minecraft.world.level.WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
            // Generate lost city buildings
            generateCity(level, boundingBox, this.random);
        }
        
        private void generateCity(net.minecraft.world.level.WorldGenLevel level, BoundingBox boundingBox, RandomSource random) {
            int centerX = (boundingBox.minX() + boundingBox.maxX()) / 2;
            int centerZ = (boundingBox.minZ() + boundingBox.maxZ()) / 2;
            
            // Find ground level
            int groundY = findGroundLevel(level, centerX, centerZ, boundingBox);
            
            // Generate multiple buildings
            int buildingCount = 5 + random.nextInt(8); // 5-12 buildings
            
            for (int i = 0; i < buildingCount; i++) {
                int offsetX = (random.nextInt(60) - 30);
                int offsetZ = (random.nextInt(60) - 30);
                int buildingX = centerX + offsetX;
                int buildingZ = centerZ + offsetZ;
                int buildingY = findGroundLevel(level, buildingX, buildingZ, boundingBox);
                
                generateBuilding(level, boundingBox, random, buildingX, buildingY, buildingZ);
            }
            
            // Generate roads between buildings
            generateRoads(level, boundingBox, random, centerX, groundY, centerZ);
        }
        
        private int findGroundLevel(net.minecraft.world.level.WorldGenLevel level, int x, int z, BoundingBox boundingBox) {
            for (int y = boundingBox.maxY(); y >= boundingBox.minY(); y--) {
                BlockPos pos = new BlockPos(x, y, z);
                if (level.getBlockState(pos).isSolid() && level.getBlockState(pos.above()).isAir()) {
                    return y + 1;
                }
            }
            return boundingBox.minY() + 10;
        }
        
        private void generateBuilding(net.minecraft.world.level.WorldGenLevel level, BoundingBox boundingBox, RandomSource random, int x, int y, int z) {
            // Generate a ruined building
            int width = 4 + random.nextInt(6); // 4-9 blocks wide
            int length = 4 + random.nextInt(6); // 4-9 blocks long
            int height = 3 + random.nextInt(8); // 3-10 blocks tall
            
            BlockState wallBlock = Blocks.COBBLESTONE.defaultBlockState();
            BlockState floorBlock = Blocks.STONE_BRICKS.defaultBlockState();
            BlockState roofBlock = Blocks.STONE_BRICK_SLAB.defaultBlockState();
            
            // Build walls (with some missing blocks for ruins effect)
            for (int dx = 0; dx < width; dx++) {
                for (int dz = 0; dz < length; dz++) {
                    for (int dy = 0; dy < height; dy++) {
                        int worldX = x + dx;
                        int worldY = y + dy;
                        int worldZ = z + dz;
                        
                        if (!boundingBox.isInside(new BlockPos(worldX, worldY, worldZ))) {
                            continue;
                        }
                        
                        // Floor
                        if (dy == 0) {
                            if (random.nextDouble() > 0.1) { // 90% chance for floor
                                placeBlock(level, floorBlock, worldX, worldY, worldZ, boundingBox);
                            }
                        }
                        // Walls (only on edges)
                        else if (dx == 0 || dx == width - 1 || dz == 0 || dz == length - 1) {
                            if (random.nextDouble() > 0.3) { // 70% chance for wall blocks (ruins)
                                placeBlock(level, wallBlock, worldX, worldY, worldZ, boundingBox);
                            }
                        }
                        // Roof
                        else if (dy == height - 1) {
                            if (random.nextDouble() > 0.4) { // 60% chance for roof (ruins)
                                placeBlock(level, roofBlock, worldX, worldY, worldZ, boundingBox);
                            }
                        }
                    }
                }
            }
            
            // Add some interior details (chests, etc.)
            if (random.nextDouble() < 0.3) { // 30% chance for chest
                int chestX = x + width / 2;
                int chestZ = z + length / 2;
                if (boundingBox.isInside(new BlockPos(chestX, y + 1, chestZ))) {
                    placeBlock(level, Blocks.CHEST.defaultBlockState(), chestX, y + 1, chestZ, boundingBox);
                }
            }
        }
        
        private void generateRoads(net.minecraft.world.level.WorldGenLevel level, BoundingBox boundingBox, RandomSource random, int centerX, int y, int centerZ) {
            // Generate simple roads (cobblestone paths)
            BlockState roadBlock = Blocks.COBBLESTONE.defaultBlockState();
            
            // Main road (north-south)
            for (int z = boundingBox.minZ(); z <= boundingBox.maxZ(); z++) {
                for (int dx = -2; dx <= 2; dx++) {
                    int x = centerX + dx;
                    if (boundingBox.isInside(new BlockPos(x, y, z))) {
                        placeBlock(level, roadBlock, x, y, z, boundingBox);
                    }
                }
            }
            
            // Cross road (east-west)
            for (int x = boundingBox.minX(); x <= boundingBox.maxX(); x++) {
                for (int dz = -2; dz <= 2; dz++) {
                    int z = centerZ + dz;
                    if (boundingBox.isInside(new BlockPos(x, y, z))) {
                        placeBlock(level, roadBlock, x, y, z, boundingBox);
                    }
                }
            }
        }
        
        protected void placeBlock(net.minecraft.world.level.WorldGenLevel level, BlockState state, int x, int y, int z, BoundingBox boundingBox) {
            BlockPos pos = new BlockPos(x, y, z);
            if (boundingBox.isInside(pos) && level.getBlockState(pos).isAir()) {
                level.setBlock(pos, state, 2);
            }
        }
    }
}
