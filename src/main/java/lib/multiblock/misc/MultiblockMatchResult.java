package lib.multiblock.misc;

import net.minecraft.world.level.block.state.pattern.BlockInWorld;

import java.util.List;

public record MultiblockMatchResult(List<BlockInWorld> blocks) {
}
