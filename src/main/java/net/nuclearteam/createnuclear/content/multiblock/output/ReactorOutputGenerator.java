package net.nuclearteam.createnuclear.content.multiblock.output;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class ReactorOutputGenerator extends SpecialBlockStateGen {

    @Override
    protected Property<?>[] getIgnoredProperties() {
        return new Property<?>[] {ReactorOutput.DIR};
    }

    @Override
    protected int getXRotation(BlockState state) {
        return state.getValue(ReactorOutput.FACING) == Direction.DOWN ? 180 : 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        return state.getValue(ReactorOutput.FACING).getAxis().isVertical()
                ? 0
                : horizontalAngle(state.getValue(ReactorOutput.FACING));
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        return prov
                .models()
                .getExistingFile(prov
                        .modLoc("block/reactor/output/output" + (state.getValue(ReactorOutput.FACING).getAxis().isVertical()
                                ? "_vertical"
                                : ""
                        )));
    }
}