package com.legobmw99.allomancy.modules.materials.world;

import com.legobmw99.allomancy.modules.materials.MaterialsConfig;
import com.legobmw99.allomancy.modules.materials.MaterialsSetup;
import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.ArrayList;

public class OreGenerator {

    private static class OreData {
        public int max_height;
        public int min_height;
        public int vein_size;
        public int ores_per_chunk;
        public Block ore_block;
        public boolean config_enabled;

        /**
         * Construct an OreData with the given parameters
         *
         * @param max_height     the maximum height it can generate at
         * @param min_height     the minumum height it can generate at
         * @param vein_size      the vien size
         * @param ores_per_chunk number of times it can generate per chunk
         * @param ore_block      the block to generate
         * @param config_enabled whether or not it is enabled in the configuration file
         */
        protected OreData(int max_height, int min_height, int vein_size, int ores_per_chunk, Block ore_block,
                          boolean config_enabled) {
            this.max_height = max_height;
            this.min_height = min_height;
            this.vein_size = vein_size;
            this.ores_per_chunk = ores_per_chunk;
            this.ore_block = ore_block;
            this.config_enabled = config_enabled;
        }
    }

    private static ArrayList<OreData> ores = new ArrayList<>();

    static {
        ores.add(new OreData(MaterialsConfig.copper_max_y.get(), MaterialsConfig.copper_min_y.get(), 9, MaterialsConfig.copper_density.get(),
                MaterialsSetup.COPPER_ORE.get(), MaterialsConfig.generate_copper.get()));
        ores.add(new OreData(MaterialsConfig.tin_max_y.get(), MaterialsConfig.tin_min_y.get(), 9, MaterialsConfig.tin_density.get(),
                MaterialsSetup.TIN_ORE.get(), MaterialsConfig.generate_tin.get()));
        ores.add(new OreData(MaterialsConfig.lead_max_y.get(), MaterialsConfig.lead_min_y.get(), 9, MaterialsConfig.lead_density.get(),
                MaterialsSetup.LEAD_ORE.get(), MaterialsConfig.generate_lead.get()));
        ores.add(new OreData(MaterialsConfig.zinc_max_y.get(), MaterialsConfig.zinc_min_y.get(), 9, MaterialsConfig.zinc_density.get(),
                MaterialsSetup.ZINC_ORE.get(), MaterialsConfig.generate_zinc.get()));
    }


    public static void generationSetup() {
        for (Biome biome : Biome.BIOMES) {
            // We only want overworld generation
            if (biome.getRegistryName().toString().matches(".*end.*|.*nether.*")) {
                continue;
            }
            for (OreData ore : ores) {
                if (ore.config_enabled) {
                    ConfiguredFeature<OreFeatureConfig, Feature<OreFeatureConfig>> feature = new ConfiguredFeature<>(Feature.ORE,
                            new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ore.ore_block.getDefaultState(), ore.vein_size));
                    feature.func_227228_a_(new ConfiguredPlacement<>(Placement.COUNT_RANGE, new CountRangeConfig(ore.ores_per_chunk, ore.max_height, 1, ore.max_height)));
                    biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, feature);

                }
            }
        }
    }
}