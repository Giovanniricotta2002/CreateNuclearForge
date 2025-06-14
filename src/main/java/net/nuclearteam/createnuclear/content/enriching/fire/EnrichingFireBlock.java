package net.nuclearteam.createnuclear.content.enriching.fire;

import com.mojang.serialization.MapCodec;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.nuclearteam.createnuclear.CNTags.CNBlockTags;
import net.nuclearteam.createnuclear.CreateNuclear;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings({"deprecation"})
public class EnrichingFireBlock extends BaseFireBlock {
    public static final MapCodec<EnrichingFireBlock> CODEC = simpleCodec(EnrichingFireBlock::new);


    public EnrichingFireBlock(Properties properties, float fireDamage) {
        super(properties, fireDamage);
    }

    public EnrichingFireBlock(Properties properties) {
        super(properties, 1f);
    }

    @Override
    protected MapCodec<? extends BaseFireBlock> codec() {
        return CODEC;
    }

    public BlockState getStateForPlacement() {
        return this.defaultBlockState();
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        return this.canSurvive(pState, pLevel, pCurrentPos)
                ? this.getStateForPlacement()
                : Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return EnrichingFireBlock.canSurviveOnBlock(worldIn.getBlockState(pos.below()));
    }

    @Override
    protected boolean canBurn(BlockState p_49284_) {
        return true;
    }

    public static boolean canSurviveOnBlock(BlockState pState) {
        CreateNuclear.LOGGER.warn("Can survive on {} : {}", pState.getBlock(), pState.is(CNBlockTags.ENRICHING_FIRE_BASE_BLOCKS.tag));
        return pState.is(CNBlockTags.ENRICHING_FIRE_BASE_BLOCKS.tag);
    }

    public static NonNullUnaryOperator<Properties> getLight() {
        return p -> p.lightLevel(a -> 15);
    }
}
