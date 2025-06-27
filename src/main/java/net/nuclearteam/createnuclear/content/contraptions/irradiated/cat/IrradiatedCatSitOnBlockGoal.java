package net.nuclearteam.createnuclear.content.contraptions.irradiated.cat;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public class IrradiatedCatSitOnBlockGoal extends MoveToBlockGoal {
    private final IrradiatedCat irradiatedCat;

    public IrradiatedCatSitOnBlockGoal(IrradiatedCat cat, double speedModifier) {
        super(cat, speedModifier, 8);
        this.irradiatedCat = cat;
    }

    public boolean canUse() {
        return this.irradiatedCat.isTame() && !this.irradiatedCat.isOrderedToSit() && super.canUse();
    }

    public void start() {
        super.start();
        this.irradiatedCat.setInSittingPose(false);
    }

    public void stop() {
        super.stop();
        this.irradiatedCat.setInSittingPose(false);
    }

    public void tick() {
        super.tick();
        this.irradiatedCat.setInSittingPose(this.isReachedTarget());
    }

    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        if (!level.isEmptyBlock(pos.above())) {
            return false;
        } else {
            BlockState blockstate = level.getBlockState(pos);
            if (blockstate.is(Blocks.CHEST)) {
                return ChestBlockEntity.getOpenCount(level, pos) < 1;
            } else {
                return blockstate.is(Blocks.FURNACE) && (Boolean) blockstate.getValue(FurnaceBlock.LIT) || blockstate.is(BlockTags.BEDS, (p_25156_) -> (Boolean) p_25156_.getOptionalValue(BedBlock.PART).map((p_148084_) -> p_148084_ != BedPart.HEAD).orElse(true));
            }
        }
    }
}