package net.nuclearteam.createnuclear.content.multiblock.bluePrintItem;

import com.simibubi.create.foundation.gui.menu.GhostItemMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.nuclearteam.createnuclear.*;
import net.nuclearteam.createnuclear.CNTags.CNItemTags;
import net.nuclearteam.createnuclear.infrastructure.config.CNConfigs;

import static net.nuclearteam.createnuclear.content.multiblock.bluePrintItem.ReactorBluePrintItem.getItemStorage;

public class ReactorBluePrintMenu extends GhostItemMenu<ItemStack> {

    public float heat = 0F;
    public int graphiteTime = 5000;
    public int uraniumTime = 3600;
    public int countGraphiteRod = 0;
    public int countUraniumRod = 0;
    public double progress = 0;

    public boolean sendUpdate = false;

    private ReactorBluePrintData reactorBluePrintData;

    public ReactorBluePrintMenu(MenuType<?> type, int id, Inventory inv, RegistryFriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public ReactorBluePrintMenu(MenuType<?> type, int id, Inventory inv, ItemStack contentHolder) {
        super(type, id, inv, contentHolder);
    }

    public static ReactorBluePrintMenu create(int id, Inventory inv, ItemStack stack) {
        return new ReactorBluePrintMenu(CNMenus.REACTOR_BLUEPRINT_MENU.get(), id, inv, stack);
    }

    @Override
    protected boolean allowRepeats() {
        return false;
    }

    @Override
    protected void initAndReadInventory(ItemStack contentHolder) {
        super.initAndReadInventory(contentHolder);
        int[][] positions = {
                {3, 0}, {4, 0}, {5, 0},
                {2, 1}, {3, 1}, {4, 1}, {5, 1}, {6, 1},
                {1, 2}, {2, 2}, {3, 2}, {4, 2}, {5, 2}, {6, 2}, {7, 2},
                {0, 3}, {1, 3}, {2, 3}, {3, 3}, {4, 3}, {5, 3}, {6, 3}, {7, 3}, {8, 3},
                {0, 4}, {1, 4}, {2, 4}, {3, 4}, {4, 4}, {5, 4}, {6, 4}, {7, 4}, {8, 4},
                {0, 5}, {1, 5}, {2, 5}, {3, 5}, {4, 5}, {5, 5}, {6, 5}, {7, 5}, {8, 5},
                {1, 6}, {2, 6}, {3, 6}, {4, 6}, {5, 6}, {6, 6}, {7, 6},
                {2, 7}, {3, 7}, {4, 7}, {5, 7}, {6, 7},
                {3, 8}, {4, 8}, {5, 8}
        };


        PatternData[] patternData = new PatternData[positions.length];
        ItemStack defaultStack = new ItemStack(Items.GLASS_PANE);

        for (int i = 0; i < positions.length; i++) {
            patternData[i] = new PatternData(i, defaultStack);
        }


        ReactorBluePrintData defaultPattern = new ReactorBluePrintData(0, 0, CNConfigs.common().rods.graphiteRodLifetime.get(), CNConfigs.common().rods.uraniumRodLifetime.get(), patternData, patternData);
        reactorBluePrintData = contentHolder.getOrDefault(CNDataComponents.REACTOR_BLUE_PRINT_DATA, defaultPattern);

        for (int i = 0; i < positions.length; i++) {
            ItemStack stack = reactorBluePrintData.pattern()[i].stack().is(CNItemTags.COOLER.tag) || reactorBluePrintData.pattern()[i].stack().is(CNItemTags.FUEL.tag)
                    ? reactorBluePrintData.pattern()[i].stack()
                    : ItemStack.EMPTY;
            ghostInventory.setStackInSlot(i, stack);
        }
    }

    @Override
    protected ItemStackHandler createGhostInventory() {
        return getItemStorage(contentHolder);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected ItemStack createOnClient(RegistryFriendlyByteBuf extraData) {
        return ItemStack.STREAM_CODEC.decode(extraData);
    }

    @Override
    protected void addSlots() {
        addPlayerSlots(getPlayerInventoryXOffset(), getPlayerInventoryYOffset());
        addPatternSlots();
    }

    private void addPatternSlots() {
        int startWidth = 8+23;
        int startHeight = 45;
        int incr = 18;
        int i = 0;
        int[][] positions = {
                {3, 0}, {4, 0}, {5, 0},
                {2, 1}, {3, 1}, {4, 1}, {5, 1}, {6, 1},
                {1, 2}, {2, 2}, {3, 2}, {4, 2}, {5, 2}, {6, 2}, {7, 2},
                {0, 3}, {1, 3}, {2, 3}, {3, 3}, {4, 3}, {5, 3}, {6, 3}, {7, 3}, {8, 3},
                {0, 4}, {1, 4}, {2, 4}, {3, 4}, {4, 4}, {5, 4}, {6, 4}, {7, 4}, {8, 4},
                {0, 5}, {1, 5}, {2, 5}, {3, 5}, {4, 5}, {5, 5}, {6, 5}, {7, 5}, {8, 5},
                {1, 6}, {2, 6}, {3, 6}, {4, 6}, {5, 6}, {6, 6}, {7, 6},
                {2, 7}, {3, 7}, {4, 7}, {5, 7}, {6, 7},
                {3, 8}, {4, 8}, {5, 8}
        };

        for (int[] pos : positions) {// up and down not middle
            this.addSlot(new SlotItemHandler(ghostInventory,i, startWidth + incr * pos[0], startHeight + incr * pos[1]));
            i++;
        }
    }

    @Override
    protected void saveData(ItemStack contentHolder) {
        int[][] positions = {
                {3, 0}, {4, 0}, {5, 0},
                {2, 1}, {3, 1}, {4, 1}, {5, 1}, {6, 1},
                {1, 2}, {2, 2}, {3, 2}, {4, 2}, {5, 2}, {6, 2}, {7, 2},
                {0, 3}, {1, 3}, {2, 3}, {3, 3}, {4, 3}, {5, 3}, {6, 3}, {7, 3}, {8, 3},
                {0, 4}, {1, 4}, {2, 4}, {3, 4}, {4, 4}, {5, 4}, {6, 4}, {7, 4}, {8, 4},
                {0, 5}, {1, 5}, {2, 5}, {3, 5}, {4, 5}, {5, 5}, {6, 5}, {7, 5}, {8, 5},
                {1, 6}, {2, 6}, {3, 6}, {4, 6}, {5, 6}, {6, 6}, {7, 6},
                {2, 7}, {3, 7}, {4, 7}, {5, 7}, {6, 7},
                {3, 8}, {4, 8}, {5, 8}
        };

        PatternData[] patternData = new PatternData[positions.length];
        int countGraphiteRod = 0;
        int countUraniumRod = 0;

        for (int i = 0; i < positions.length; i++) {
            ItemStack stack = ghostInventory.getStackInSlot(i);

            if (!stack.isEmpty() && stack.getCount() >= 1 && stack.getCount() <= 99) {
                if (stack.is(CNItemTags.COOLER.tag)) {
                    countGraphiteRod++;
                }
                if (stack.is(CNItemTags.FUEL.tag)) {
                    countUraniumRod++;
                }
                patternData[i] = new PatternData(i, stack);
            } else {
                patternData[i] = new PatternData(i, new ItemStack(Items.GLASS_PANE));
            }
        }

        /*CreateNuclear.LOGGER.warn("PatternData slots:\n{}\n\n Count: {}",
                Arrays.stream(patternData)
                        .map(pd -> "Slot " + pd.slot() + " => " + pd.stack())
                        .collect(Collectors.joining("\n")), countGraphiteRod
        );*/

        ReactorBluePrintData reactorBluePrintData = new ReactorBluePrintData(countGraphiteRod, countUraniumRod, CNConfigs.common().rods.graphiteRodLifetime.get(), CNConfigs.common().rods.uraniumRodLifetime.get(), patternData, patternData);
        contentHolder.set(CNDataComponents.REACTOR_BLUE_PRINT_DATA, reactorBluePrintData);

        ReactorBluePrintData newData = new ReactorBluePrintData(countGraphiteRod, countUraniumRod, CNConfigs.common().rods.graphiteRodLifetime.get(), CNConfigs.common().rods.uraniumRodLifetime.get(), patternData, patternData);

        if (!newData.equals(reactorBluePrintData)) {
            contentHolder.set(CNDataComponents.REACTOR_BLUE_PRINT_DATA, newData);
        }

    }

    protected int getPlayerInventoryXOffset() {
        return 31;
    }

    protected int getPlayerInventoryYOffset() {
        return 231;
    }

    @Override
    public boolean stillValid(Player player) {
        return playerInventory.getSelected() == contentHolder;
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        if (clickTypeIn == ClickType.THROW) {
            if ( slotId >= 0 && slotId < 9) {
                clickTypeIn = ClickType.PICKUP;
                super.clicked(slotId, dragType, clickTypeIn, player);
            }
            return;
        }
        super.clicked(slotId, dragType, clickTypeIn, player);
    }
}