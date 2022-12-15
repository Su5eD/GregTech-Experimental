package dev.su5ed.gregtechmod.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

import java.util.BitSet;
import java.util.function.Function;

public class SingleOreFeature extends OreFeature {
    private final int maxCount;
    
    public SingleOreFeature() {
        super(OreConfiguration.CODEC);

        this.maxCount = 1;
    }

    @Override
    protected boolean doPlace(WorldGenLevel pLevel, RandomSource pRandom, OreConfiguration pConfig, double pMinX, double pMaxX, double pMinZ, double pMaxZ, double pMinY, double pMaxY, int pX, int pY, int pZ, int pWidth, int pHeight) {
        int i = 0;
        BitSet bitset = new BitSet(pWidth * pHeight * pWidth);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        int j = pConfig.size;
        double[] adouble = new double[j * 4];

        for (int k = 0; k < j; ++k) {
            float f = (float) k / (float) j;
            double d0 = Mth.lerp(f, pMinX, pMaxX);
            double d1 = Mth.lerp(f, pMinY, pMaxY);
            double d2 = Mth.lerp(f, pMinZ, pMaxZ);
            double d3 = pRandom.nextDouble() * (double) j / 16.0D;
            double d4 = ((double) (Mth.sin((float) Math.PI * f) + 1.0F) * d3 + 1.0D) / 2.0D;
            adouble[k * 4] = d0;
            adouble[k * 4 + 1] = d1;
            adouble[k * 4 + 2] = d2;
            adouble[k * 4 + 3] = d4;
        }

        for (int l3 = 0; l3 < j - 1; ++l3) {
            if (!(adouble[l3 * 4 + 3] <= 0.0D)) {
                for (int i4 = l3 + 1; i4 < j; ++i4) {
                    if (!(adouble[i4 * 4 + 3] <= 0.0D)) {
                        double d8 = adouble[l3 * 4] - adouble[i4 * 4];
                        double d10 = adouble[l3 * 4 + 1] - adouble[i4 * 4 + 1];
                        double d12 = adouble[l3 * 4 + 2] - adouble[i4 * 4 + 2];
                        double d14 = adouble[l3 * 4 + 3] - adouble[i4 * 4 + 3];
                        if (d14 * d14 > d8 * d8 + d10 * d10 + d12 * d12) {
                            if (d14 > 0.0D) {
                                adouble[i4 * 4 + 3] = -1.0D;
                            }
                            else {
                                adouble[l3 * 4 + 3] = -1.0D;
                            }
                        }
                    }
                }
            }
        }

        BulkSectionAccess bulksectionaccess = new BulkSectionAccess(pLevel);

        try {
            outer:
            for (int j4 = 0; j4 < j; ++j4) {
                double d9 = adouble[j4 * 4 + 3];
                if (!(d9 < 0.0D)) {
                    double d11 = adouble[j4 * 4];
                    double d13 = adouble[j4 * 4 + 1];
                    double d15 = adouble[j4 * 4 + 2];
                    int k4 = Math.max(Mth.floor(d11 - d9), pX);
                    int l = Math.max(Mth.floor(d13 - d9), pY);
                    int i1 = Math.max(Mth.floor(d15 - d9), pZ);
                    int j1 = Math.max(Mth.floor(d11 + d9), k4);
                    int k1 = Math.max(Mth.floor(d13 + d9), l);
                    int l1 = Math.max(Mth.floor(d15 + d9), i1);

                    for (int i2 = k4; i2 <= j1; ++i2) {
                        double d5 = ((double) i2 + 0.5D - d11) / d9;
                        if (d5 * d5 < 1.0D) {
                            for (int j2 = l; j2 <= k1; ++j2) {
                                double d6 = ((double) j2 + 0.5D - d13) / d9;
                                if (d5 * d5 + d6 * d6 < 1.0D) {
                                    for (int k2 = i1; k2 <= l1; ++k2) {
                                        double d7 = ((double) k2 + 0.5D - d15) / d9;
                                        if (d5 * d5 + d6 * d6 + d7 * d7 < 1.0D && !pLevel.isOutsideBuildHeight(j2)) {
                                            int l2 = i2 - pX + (j2 - pY) * pWidth + (k2 - pZ) * pWidth * pHeight;
                                            if (!bitset.get(l2)) {
                                                bitset.set(l2);
                                                blockpos$mutableblockpos.set(i2, j2, k2);
                                                if (pLevel.ensureCanWrite(blockpos$mutableblockpos)) {
                                                    LevelChunkSection levelchunksection = bulksectionaccess.getSection(blockpos$mutableblockpos);
                                                    if (levelchunksection != null) {
                                                        int i3 = SectionPos.sectionRelative(i2);
                                                        int j3 = SectionPos.sectionRelative(j2);
                                                        int k3 = SectionPos.sectionRelative(k2);
                                                        BlockState blockstate = levelchunksection.getBlockState(i3, j3, k3);

                                                        for (OreConfiguration.TargetBlockState oreconfiguration$targetblockstate : pConfig.targetStates) {
                                                            if (canPlaceOreAt(blockstate, bulksectionaccess::getBlockState, pRandom, pConfig, oreconfiguration$targetblockstate, blockpos$mutableblockpos)) {
                                                                levelchunksection.setBlockState(i3, j3, k3, oreconfiguration$targetblockstate.state, false);
                                                                ++i;
                                                                
                                                                if (i >= this.maxCount) {
                                                                    break outer;
                                                                }
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable throwable1) {
            try {
                bulksectionaccess.close();
            } catch (Throwable throwable) {
                throwable1.addSuppressed(throwable);
            }

            throw throwable1;
        }

        bulksectionaccess.close();
        return i > 0;
    }

    protected boolean canPlaceOreAt(BlockState state, Function<BlockPos, BlockState> adjacentStateAccessor, RandomSource random, OreConfiguration config, OreConfiguration.TargetBlockState targetState, BlockPos.MutableBlockPos mutablePos) {
        return OreFeature.canPlaceOre(state, adjacentStateAccessor, random, config, targetState, mutablePos);
    }
}
