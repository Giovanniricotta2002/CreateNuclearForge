package net.nuclearteam.createnuclear.content.multiblock.controller;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.ItemHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.nuclearteam.createnuclear.*;
import net.nuclearteam.createnuclear.content.multiblock.CNMultiblock;
import net.nuclearteam.createnuclear.content.multiblock.output.ReactorOutput;
import net.nuclearteam.createnuclear.content.multiblock.output.ReactorOutputEntity;
import net.nuclearteam.createnuclear.foundation.block.HorizontalDirectionalReactorBlock;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class ReactorControllerBlock extends HorizontalDirectionalReactorBlock implements IWrenchable, IBE<ReactorControllerBlockEntity> {
    public static final BooleanProperty ASSEMBLED = BooleanProperty.create("assembled");

    public ReactorControllerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(ASSEMBLED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(ASSEMBLED, false);
    }
    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
                                boolean isMoving) {
        withBlockEntityDo(worldIn, pos, be -> be.created = false);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide)
            return ItemInteractionResult.SUCCESS;

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof ReactorControllerBlockEntity controllerBlockEntity)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        ItemStack heldItem = player.getItemInHand(hand);
        CreateNuclear.LOGGER.warn("helm: {}", heldItem.get(CNDataComponents.REACTOR_BLUE_PRINT_DATA));

        if (!state.getValue(ASSEMBLED)) {
            player.sendSystemMessage(Component.translatable("reactor.info.assembled.none").withStyle(ChatFormatting.RED));
        }
        else {
            if (heldItem.is(CNItems.REACTOR_BLUEPRINT.get()) && controllerBlockEntity.inventory.getItem(0).isEmpty()){
                withBlockEntityDo(level, pos, be -> {
                    be.inventory.setStackInSlot(0, heldItem);
                    be.configuredPattern = heldItem;

                    player.setItemInHand(hand, ItemStack.EMPTY);
                });
                return ItemInteractionResult.SUCCESS;

            }
            else if (heldItem.isEmpty() && !controllerBlockEntity.inventory.getItem(0).isEmpty()) {
                withBlockEntityDo(level, pos, be -> {
                    player.setItemInHand(hand, be.inventory.getItem(0));
                    be.inventory.setStackInSlot(0, ItemStack.EMPTY);
                    be.configuredPattern = ItemStack.EMPTY;
                    be.total = 0.0;
                    be.rotate(be.getBlockState(), new BlockPos(be.getBlockPos().getX(), be.getBlockPos().getY() + (-3), be.getBlockPos().getZ()), be.getLevel(), 0);
                    be.notifyUpdate();
                });
                state.setValue(ASSEMBLED, false);
                return ItemInteractionResult.SUCCESS;

            }
            else if (!heldItem.isEmpty() && !controllerBlockEntity.inventory.getItem(0).isEmpty()) {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
        }
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.hasBlockEntity() || state.getBlock() == newState.getBlock())
            return;

        withBlockEntityDo(worldIn, pos, be -> ItemHelper.dropContents(worldIn, pos, be.inventory));
        worldIn.removeBlockEntity(pos);

        ReactorControllerBlock controller = (ReactorControllerBlock) state.getBlock();

        controller.Rotate(state, pos.below(3), worldIn, 0);
        List<? extends Player> players = worldIn.players();
        for (Player p : players) {
            p.sendSystemMessage(Component.translatable("reactor.info.assembled.destroyer"));
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (state.getValue(ASSEMBLED))
            return;
        List<? extends Player> players = level.players();
        ReactorControllerBlock controller = (ReactorControllerBlock) state.getBlock();
        controller.Verify(state, pos, level, players, true);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        ReactorControllerBlock controller = (ReactorControllerBlock) state.getBlock();
        ReactorControllerBlockEntity entity = controller.getBlockEntity(level, pos);
        if (!entity.created)
            return;
        controller.Rotate(state, pos.below(3), level, 0);
        List<? extends Player> players = level.players();
        for (Player p : players) {
            p.sendSystemMessage(Component.translatable("reactor.info.assembled.creator"));
        }
    }

    // this is the Function that verifies if the pattern is correct (as a test, we added the energy output)
    public void Verify(BlockState state, BlockPos pos, Level level, List<? extends Player> players, boolean create){
        ReactorControllerBlock controller = (ReactorControllerBlock) level.getBlockState(pos).getBlock();
        ReactorControllerBlockEntity entity = controller.getBlockEntity(level, pos);
        var result = CNMultiblock.REGISTRATE_MULTIBLOCK.findStructure(level, pos); // control the pattern
        if (result != null) { // the pattern is correct

            for (Player player : players) {
                if (create && !entity.created) {
                    player.sendSystemMessage(Component.translatable("reactor.info.assembled.creator"));
                    level.setBlockAndUpdate(pos, state.setValue(ASSEMBLED, true));
                    entity.created = true;
                    entity.destroyed = false;
                }
            }
            return;
        }

        // the pattern is incorrect
        for (Player player : players) {
            if (!create && !entity.destroyed)
            {
                player.sendSystemMessage(Component.translatable("reactor.info.assembled.destroyer"));
                level.setBlockAndUpdate(pos, state.setValue(ASSEMBLED, false));
                entity.created = false;
                entity.destroyed = true;
                Rotate(state, pos.below(3), level, 0);
            }
        }
    }
    public void Rotate(BlockState state, BlockPos pos, Level level, int rotation) {
        if (level.getBlockState(pos).is(CNBlocks.REACTOR_OUTPUT.get())) {
            ReactorOutput block = (ReactorOutput) level.getBlockState(pos).getBlock();
            ReactorOutputEntity entity = block.getBlockEntityType().getBlockEntity(level, pos);

            if (state.getValue(ASSEMBLED) && rotation != 0) { // Starting the energy
                entity.speed = rotation;
                entity.setSpeed(Math.abs(entity.speed));
            } else { // stopping the energy

                entity.setSpeed(0);
                entity.speed = 0;
            }
            entity.updateSpeed = true;
            entity.updateGeneratedRotation();

            entity.setSpeed(rotation);
        }
    }

    @Override
    public Class<ReactorControllerBlockEntity> getBlockEntityClass() {
        return ReactorControllerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ReactorControllerBlockEntity> getBlockEntityType() {
        return CNBlockEntityTypes.REACTOR_CONTROLLER.get();
    }

}
