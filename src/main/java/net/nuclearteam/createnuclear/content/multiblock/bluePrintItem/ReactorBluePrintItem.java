package net.nuclearteam.createnuclear.content.multiblock.bluePrintItem;

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
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import net.nuclearteam.createnuclear.CNItems;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

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
                NetworkHooks.openScreen((ServerPlayer) player, this, buf -> buf.writeItem(heldItem));
            return InteractionResultHolder.success(heldItem);
        }
        else if (player.isShiftKeyDown() && hand == InteractionHand.MAIN_HAND) {
            if (!world.isClientSide && player instanceof ServerPlayer) {
                NetworkHooks.openScreen((ServerPlayer) player, this, buf -> buf.writeItem(heldItem));
            }
            return InteractionResultHolder.success(heldItem);
        }
        return InteractionResultHolder.pass(heldItem);
    }

    public static ItemStackHandler getItemStorage(ItemStack stack) {
        ItemStackHandler newInv = new ItemStackHandler(57);
        if (CNItems.REACTOR_BLUEPRINT.get() != stack.getItem()) throw new IllegalArgumentException("Cannot get configured items from non item: " + stack);
        if (!stack.hasTag()) return newInv;
        CompoundTag invNBT = stack.getOrCreateTagElement("pattern");
        if (!invNBT.isEmpty())
            newInv.deserializeNBT(invNBT);
        return newInv;
    }
}