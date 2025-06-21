package net.nuclearteam.createnuclear.content.multiblock.bluePrintItem;

import com.simibubi.create.AllDataComponents;
import com.simibubi.create.foundation.item.ItemHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.nuclearteam.createnuclear.CNDamageTypes;
import net.nuclearteam.createnuclear.CNDataComponents;
import net.nuclearteam.createnuclear.CNItems;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ReactorBluePrintItem extends Item implements MenuProvider {

    public ReactorBluePrintItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("reactor.item.gui.name");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        ItemStack heldItem = player.getMainHandItem();
        return ReactorBluePrintMenu.create(id, inv, heldItem);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() == null) return InteractionResult.PASS;
        return use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);

        if (!player.isShiftKeyDown() && hand == InteractionHand.MAIN_HAND) {
            if (!world.isClientSide && player instanceof ServerPlayer)
                player.openMenu(this, buf -> {
                    ItemStack.STREAM_CODEC.encode(buf, heldItem);
                });
            return InteractionResultHolder.success(heldItem);
        }
        else if (player.isShiftKeyDown() && hand == InteractionHand.MAIN_HAND) {
            if (!world.isClientSide && player instanceof ServerPlayer) {
                player.openMenu(this, buf -> {
                    ItemStack.STREAM_CODEC.encode(buf, heldItem);
                });
            }
            return InteractionResultHolder.success(heldItem);
        }
        return InteractionResultHolder.pass(heldItem);
    }

    public static ItemStackHandler getItemStorage(ItemStack stack) {
        final int slotCount = 57;
        ItemStackHandler inventory = new ItemStackHandler(slotCount);

        if (stack.getItem() != CNItems.REACTOR_BLUEPRINT.get()) {
            throw new IllegalArgumentException("Cannot get configured items from non-blueprint item: " + stack);
        }

        ReactorBluePrintData data = stack.get(CNDataComponents.REACTOR_BLUE_PRINT_DATA);
        if (data == null || data.pattern().length != slotCount) {
            return inventory;
        }

        PatternData[] pattern = data.pattern();
        for (int i = 0; i < slotCount; i++) {
            inventory.setStackInSlot(i, pattern[i].stack());
        }

        return inventory;
    }
}