package net.nuclearteam.createnuclear.content.multiblock.input;


import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.nuclearteam.createnuclear.CNItems;
import org.jetbrains.annotations.NotNull;

public class ReactorInputInventory extends ItemStackHandler {
    private final ReactorInputEntity be;

    public ReactorInputInventory(ReactorInputEntity be) {
        super(2);
        this.be = be;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        be.setChanged();
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return switch (slot) {
            case 0 -> CNItems.URANIUM_ROD.isIn(stack);
            case 1 -> CNItems.GRAPHITE_ROD.isIn(stack);
            default -> !super.isItemValid(slot, stack);
        };
    }
}