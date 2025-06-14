package net.nuclearteam.createnuclear.content.multiblock.controller;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import com.simibubi.create.foundation.utility.IInteractionChecker;
import lib.multiblock.SimpleMultiBlockAislePatternBuilder;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.nuclearteam.createnuclear.*;
import net.nuclearteam.createnuclear.content.multiblock.IHeat;
import net.nuclearteam.createnuclear.content.multiblock.input.ReactorInputEntity;
import net.nuclearteam.createnuclear.content.multiblock.output.ReactorOutput;
import net.nuclearteam.createnuclear.content.multiblock.output.ReactorOutputEntity;

import java.util.List;

import static net.nuclearteam.createnuclear.content.multiblock.CNMultiblock.*;
import static net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerBlock.ASSEMBLED;

@SuppressWarnings({"unused"})
public class ReactorControllerBlockEntity extends SmartBlockEntity implements IInteractionChecker, IHaveGoggleInformation {
    public boolean destroyed = false;
    public boolean created = false;
    public boolean test = true;
    public int speed = 16; // This is the result speed of the reactor, change this to change the total capacity

    public boolean sendUpdate;

    public ReactorControllerBlock controller;

    public ReactorControllerInventory inventory;

    //public LinkedHashSet<LazyOptional<IItemHandler>> attachedInventory;

    //private boolean powered;
    public State powered = State.OFF;
    public float reactorPower;
    public float lastReactorPower;
    int overFlowHeatTimer = 0;
    int overFlowLimiter = 30;
    double overHeat = 0;
    public int baseUraniumHeat = 25;
    public int baseGraphiteHeat = -10;
    public int proximityUraniumHeat = 5;
    public int proximityGraphiteHeat = -5;
    public int maxUraniumPerGraphite = 3;
    public int graphiteTimer = 3600;
    public int uraniumTimer = 3600;
    public int countUraniumRod;
    public int countGraphiteRod;
    public int heat;
    public double total;
    public CompoundTag screen_pattern = new CompoundTag();
    public ItemStack configuredPattern;

    private ItemStack fuelItem;
    private ItemStack coolerItem;

    private HolderLookup.Provider registries;

    private final int[][] formattedPattern = new int[][]{
            {99,99,99,0,1,2,99,99,99},
            {99,99,3,4,5,6,7,99,99},
            {99,8,9,10,11,12,13,14,99},
            {15,16,17,18,19,20,21,22,23},
            {24,25,26,27,28,29,30,31,32},
            {33,34,35,36,37,38,39,40,41},
            {99,42,43,44,45,46,47,48,99},
            {99,99,49,50,51,52,53,99,99},
            {99,99,99,54,55,56,99,99,99}
    };
    private final int[][] offsets = { {1, 0}, {-1, 0}, {0, 1}, {0, -1} };



    public ReactorControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inventory = new ReactorControllerInventory(this);
        configuredPattern = ItemStack.EMPTY;
        //attachedInventory = new LinkedHashSet<>();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    public boolean getAssembled() { // permet de savoir si le réacteur est formé ou pas.
        BlockState state = getBlockState();
        return state.getValue(ASSEMBLED);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if(!configuredPattern.isEmpty()) {
            CreateLang.translate("gui.gauge.info_header")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
            IHeat.HeatLevel.getName("reactor_controller").style(ChatFormatting.GRAY).forGoggles(tooltip);

            IHeat.HeatLevel.getFormattedHeatText(configuredPattern.get(CNDataComponents.HEAT).intValue()).forGoggles(tooltip);

            if (fuelItem.isEmpty()) {
                // if rod empty we initialize it at 1 (and display it as 0) to avoid having air item displayed instead of the rod
                IHeat.HeatLevel.getFormattedItemText(new ItemStack(CNItems.URANIUM_ROD.asItem(), 1), true).forGoggles(tooltip);
            } else {
                IHeat.HeatLevel.getFormattedItemText(fuelItem, false).forGoggles(tooltip);
            }

            if (fuelItem.isEmpty()) {
                // if rod empty we initialize it at 1 (and display it as 0) to avoid having air item displayed instead of the rod
                IHeat.HeatLevel.getFormattedItemText(new ItemStack(CNItems.GRAPHITE_ROD.asItem(), 1), true).forGoggles(tooltip);
            } else {
                IHeat.HeatLevel.getFormattedItemText(coolerItem, false).forGoggles(tooltip);
            }
        }

        return true;
    }

    //(Si les methode read et write ne sont pas implémenté alors lorsque l'on relance le monde minecraft les items dans le composant auront disparu !)
    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) { //Permet de stocker les item 1/2
        if (!clientPacket) {
            inventory.deserializeNBT(registries, tag.getCompound("pattern"));
        }
        configuredPattern = ItemStack.parseOptional(registries, tag.getCompound("items"));
        if (ItemStack.parseOptional(registries, tag.getCompound("cooler")) != null || ItemStack.parseOptional(registries, tag.getCompound("fuel")) != null) {
            coolerItem = ItemStack.parseOptional(registries, tag.getCompound("cooler"));
            fuelItem = ItemStack.parseOptional(registries, tag.getCompound("fuel"));

        }

        this.registries = registries;
        /*
        countGraphiteRod = compound.getInt("countGraphiteRod");
        countUraniumRod = compound.getInt("countUraniumRod");
        graphiteTimer = compound.getInt("graphiteTimer");
        uraniumTimer = compound.getInt("uraniumTimer");
        heat = compound.getInt("heat");
*/
        total = tag.getDouble("total");
        super.read(tag, registries, clientPacket);
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) { //Permet de stocker les item 2/2
        if (!clientPacket) {
            compound.put("pattern", inventory.serializeNBT(registries));
            //compound.putBoolean("powered", isPowered());
        }
        /*compound.put("items", configuredPattern.finalize(registries));

        if (coolerItem != null || fuelItem != null) {
            compound.put("cooler", coolerItem.get(registries));
            compound.put("fuel", fuelItem.finalize(registries));
        }*/
        /*compound.putInt("countGraphiteRod", countGraphiteRod);
        compound.putInt("countUraniumRod", countUraniumRod);
        compound.putInt("graphiteTimer", graphiteTimer);
        compound.putInt("uraniumTimer", uraniumTimer);
        compound.putInt("heat", heat);
        compound.putString("state", powered.name());
        compound.put("screen_pattern", screen_pattern);
*/
        compound.putDouble("total", calculateProgress());
        super.write(compound, registries, clientPacket);
    }

    public enum State {
        ON, OFF
    }

    private void explodeReactorCore(Level level, BlockPos pos) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos currentPos = pos.offset(x, y, z);
                    //le problème viens de la il ne rentre pas dans le if
                    if (level.getBlockState(currentPos).is(CNBlocks.REACTOR_CORE.get())) {
                        // Create and execute the explosion
                        Explosion explosion = new Explosion(level, null, currentPos.getX(), currentPos.getY(), currentPos.getZ(), 4.0F, false, Explosion.BlockInteraction.DESTROY);
                        explosion.explode();
                        explosion.finalizeExplosion(true);
                    }
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide)
            return;

        if (isEmptyConfiguredPattern()) {

            BlockEntity blockEntity = level.getBlockEntity(getBlockPosForReactor('I'));



            if (blockEntity instanceof ReactorInputEntity be) {
                CompoundTag tag = be.getPersistentData();
                ListTag inventoryTag = tag.getCompound("Inventory").getList("Items", Tag.TAG_COMPOUND);
                fuelItem = ItemStack.parse(this.registries, inventoryTag.getCompound(0)).get();
                coolerItem = ItemStack.parse(this.registries, inventoryTag.getCompound(1)).get();
                if (fuelItem.getCount() > 0 && coolerItem.getCount() > 0) {
                    configuredPattern.set(CNDataComponents.HEAT, (float) calculateHeat(tag));
                    if (updateTimers()) {
                        be.inventory.extractItem(0, 1, false);
                        be.inventory.extractItem(1, 1, false);
                        total = calculateProgress();
                        int heat = configuredPattern.getOrDefault(CNDataComponents.HEAT, 0).intValue();

                        if (IHeat.HeatLevel.of(heat) == IHeat.HeatLevel.SAFETY || IHeat.HeatLevel.of(heat) == IHeat.HeatLevel.CAUTION || IHeat.HeatLevel.of(heat) == IHeat.HeatLevel.WARNING) {
                            this.rotate(getBlockState(), new BlockPos(getBlockPos().getX(), getBlockPos().getY() + FindController('O').getY(), getBlockPos().getZ()), getLevel(), heat/4);
                        } else {
                            // Send a packet to all clients around this block within 16 blocks
                            EventTriggerPacket packet = new EventTriggerPacket(600); // display for 100 ticks
                            CreateNuclear.LOGGER.warn("hum EventTriggerBlock ? {}", packet);
                            CatnipServices.NETWORK.sendToClientsAround((ServerLevel) level, getBlockPos(), 32, packet);

                            this.rotate(getBlockState(), new BlockPos(getBlockPos().getX(), getBlockPos().getY() + FindController('O').getY(), getBlockPos().getZ()), getLevel(), 0);
                        }
                        return;
                    }
                } else {
                    this.rotate(getBlockState(), new BlockPos(getBlockPos().getX(), getBlockPos().getY() + FindController('O').getY(), getBlockPos().getZ()), getLevel(), 0);
                }

                /*if (fuelItem.getCount() > 0 && coolerItem.getCount() > 0) {
                    configuredPattern.getOrCreateTag().putDouble("heat", calculateHeat(tag));
                    if (updateTimers()) {
                        TransferUtil.extract(be.inventory, ItemVariant.of(fuelItem), 1);
                        TransferUtil.extract(be.inventory, ItemVariant.of(coolerItem), 1);
                        total = calculateProgress();
                        int heat = (int) configuredPattern.getOrCreateTag().getDouble("heat");

                        if (IHeat.HeatLevel.of(heat) == IHeat.HeatLevel.SAFETY || IHeat.HeatLevel.of(heat) == IHeat.HeatLevel.CAUTION || IHeat.HeatLevel.of(heat) == IHeat.HeatLevel.WARNING) {
                            //j'ai divisé la chaleur par 4, car maintenant on a mis la chaleur sur 1000 et non plus sur 200 en ayant rajouté 1/5 de bonus
                            this.rotate(getBlockState(), new BlockPos(getBlockPos().getX(), getBlockPos().getY() + FindController('O').getY(), getBlockPos().getZ()), getLevel(), heat/4);
                        } else {
                            this.rotate(getBlockState(), new BlockPos(getBlockPos().getX(), getBlockPos().getY() + FindController('O').getY(), getBlockPos().getZ()), getLevel(), 0);
                        }
                        return;
                    }
                } else {
                    this.rotate(getBlockState(), new BlockPos(getBlockPos().getX(), getBlockPos().getY() + FindController('O').getY(), getBlockPos().getZ()), getLevel(), 0);
                }
                */

                this.notifyUpdate();
            }
        }
    }

    private boolean isEmptyConfiguredPattern() {
        return !configuredPattern.isEmpty();// || !configuredPattern.getOrCreateTag().isEmpty();
    }

    private boolean updateTimers() {

        total -= 1;
        return total <= 0;//(total/constTotal) <= 0;
    }

    private double calculateProgress() {
        countGraphiteRod = configuredPattern.getOrDefault(CNDataComponents.COUNT_GRAPHITE_ROD, 0);
        countUraniumRod = configuredPattern.getOrDefault(CNDataComponents.COUNT_URANIUM_ROD, 0);
        // graphiteTimer = configuredPattern.getOrCreateTag().getInt("graphiteTime");
        // uraniumTimer = configuredPattern.getOrCreateTag().getInt("uraniumTime");

        double totalGraphiteRodLife = (double) graphiteTimer / countGraphiteRod;
        double totalUraniumRodLife = (double) uraniumTimer / countUraniumRod;

        return totalGraphiteRodLife + totalUraniumRodLife;
    }

    private double calculateHeat(CompoundTag tag) {
        countGraphiteRod = configuredPattern.getOrDefault(CNDataComponents.COUNT_GRAPHITE_ROD, 0);
        countUraniumRod = configuredPattern.getOrDefault(CNDataComponents.COUNT_URANIUM_ROD, 0);
        heat = 0;

        // if more than maxUraniumPerGraphite of the rods are uranium, the reactor will overheat
        if (countUraniumRod > countGraphiteRod * maxUraniumPerGraphite) {
            overFlowHeatTimer++;
            if (overFlowHeatTimer >= overFlowLimiter) {
                overHeat+=1;
                overFlowHeatTimer= 0;
                if (overFlowLimiter > 2) {
                    overFlowLimiter -= 1;
                }
            }
        } else {
            overFlowHeatTimer = 0;
            overFlowLimiter = 30;
            if (overHeat > 0) {
                overHeat -= 2;
            } else {
                overHeat = 0;
            }
        }
        // the offsets for the four directions (down, up, right, left) is int[][] offsets = { {1, 0}, {-1, 0}, {0, 1}, {0, -1} }; (defined at the top of the class)
        String currentRod = "";
        ListTag list = inventory.getStackInSlot(0).getOrDefault(CNDataComponents.PATTERN, new CompoundTag()).getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            if (list.getCompound(i).getString("id").equals("createnuclear:uranium_rod")) {
                heat += baseUraniumHeat;
                currentRod = "u";
            } else if (list.getCompound(i).getString("id").equals("createnuclear:graphite_rod")) {
                heat += baseGraphiteHeat;
                currentRod = "g";
            }
            for (int j = 0; j < formattedPattern.length; j++) {
                for (int k = 0; k < formattedPattern[j].length; k++) {
                    // Skip if the current pattern value is 99
                    if (formattedPattern[j][k] == 99) continue;

                    // Check if the current slot matches the pattern
                    if (list.getCompound(i).getInt("Slot") != formattedPattern[j][k]) continue;

                    // For each neighbor (up, down, right, left)
                    for (int[] offset : offsets) {
                        int nj = j + offset[0];
                        int nk = k + offset[1];

                        // Check if the indices are within the array boundaries
                        if (nj < 0 || nj >= formattedPattern.length || nk < 0 || nk >= formattedPattern[j].length)
                            continue;

                        int neighborSlot = formattedPattern[nj][nk];

                        // Loop through the list to find the neighbor slot
                        for (int l = 0; l < list.size(); l++) {
                            if (list.getCompound(l).getInt("Slot") == neighborSlot) {
                                // If the currentRod equals "u", apply the corresponding heat
                                if (currentRod.equals("u")) {
                                    String id = list.getCompound(l).getString("id");
                                    if (id.equals("createnuclear:uranium_rod")) {
                                        heat += proximityUraniumHeat;
                                    } else if (id.equals("createnuclear:graphite_rod")) {
                                        heat += proximityGraphiteHeat;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return heat + overHeat;
    }

    private BlockPos getBlockPosForReactor(char character) {
        BlockPos pos = FindController(character);
        BlockPos posController = getBlockPos();
        BlockPos posInput = new BlockPos(posController.getX(), posController.getY(), posController.getZ());

        int[][] directions = {
                {0,0, pos.getX()}, // NORTH
                {0,0, -pos.getX()}, // SOUTH
                {-pos.getX(),0,0}, // EAST
                {pos.getX(),0,0} // WEST
        };


        for (int[] direction : directions) {
            BlockPos newPos = posController.offset(direction[0], direction[1], direction[2]);
            if (level.getBlockState(newPos).is(CNBlocks.REACTOR_INPUT.get())) {
                posInput = newPos;
                break;
            }
        }

        return posInput;
    }

    private CompoundTag convertePattern(CompoundTag compoundTag) {
        ListTag pattern = compoundTag.getList("Items", Tag.TAG_COMPOUND);

        int[][] list = new int[][]{
                {99,99,99,0,1,2,99,99,99},
                {99,99,3,4,5,6,7,99,99},
                {99,8,9,10,11,12,13,14,99},
                {15,16,17,18,19,20,21,22,23},
                {24,25,26,27,28,29,30,31,32},
                {33,34,35,36,37,38,39,40,41},
                {99,42,43,44,45,46,47,48,99},
                {99,99,49,50,51,52,53,99,99},
                {99,99,99,54,55,56,99,99,99}
        };


        return null;
    }

    private static BlockPos FindController(char character) {
        return SimpleMultiBlockAislePatternBuilder.start()
                .aisle(AAAAA, AAAAA, AAAAA, AAAAA, AAAAA)
                .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                .aisle(AAIAA, ADADA, BACAB, ADADA, AAAA)
                .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                .aisle(AAAAA, AAAAA, AAAAA, AAAAA, AAOAA)
                .where('A', a -> a.getState().is(CNBlocks.REACTOR_CASING.get()))
                .where('B', a -> a.getState().is(CNBlocks.REACTOR_FRAME.get()))
                .where('C', a -> a.getState().is(CNBlocks.REACTOR_CORE.get()))
                .where('D', a -> a.getState().is(CNBlocks.REACTOR_COOLER.get()))
                .where('*', a -> a.getState().is(CNBlocks.REACTOR_CONTROLLER.get()))
                .where('O', a -> a.getState().is(CNBlocks.REACTOR_OUTPUT.get()))
                .where('I', a -> a.getState().is(CNBlocks.REACTOR_INPUT.get()))
                .getDistanceController(character);
    }

    public void rotate(BlockState state, BlockPos pos, Level level, int rotation) {
        if (level.getBlockState(pos).is(CNBlocks.REACTOR_OUTPUT.get()) && rotation > 0) {
            if (level.getBlockState(pos).getBlock() instanceof ReactorOutput block) {
                ReactorOutputEntity entity = block.getBlockEntityType().getBlockEntity(level, pos);
                if (state.getValue(ASSEMBLED)) { // Starting the energy
                    entity.speed = rotation;
                    entity.heat = rotation;
                } else { // stopping the energy
                    entity.speed = 0;
                    entity.heat = 0;
                }
                entity.updateSpeed = true;
                entity.updateGeneratedRotation();
                entity.setSpeed(rotation);

            }
        }
        else {
            if (level.getBlockState(pos).getBlock() instanceof ReactorOutput block) {
                ReactorOutputEntity entity = block.getBlockEntityType().getBlockEntity(level, pos);
                entity.setSpeed(0);
                entity.heat = 0;
                entity.updateSpeed = true;
                entity.updateGeneratedRotation();
            }
        }
    }

    public InteractionResult onClick(Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.is(CNItems.REACTOR_BLUEPRINT.get()) && !heldItem.isEmpty()) {
            if (configuredPattern.isEmpty()) {
                inventory.setStackInSlot(0, heldItem);
                configuredPattern = heldItem;
                //player.setItemInHand(hand, ItemStack.EMPTY);
            }
            notifyUpdate();
            return InteractionResult.SUCCESS;
        }
        else if (heldItem.isEmpty()) {
            if (configuredPattern.isEmpty()) {
                if (!level.isClientSide) {
                    if (player.addItem(configuredPattern)){
                        configuredPattern = ItemStack.EMPTY;
                    }
                    else {
                        player.setItemInHand(hand, configuredPattern);
                        inventory.setStackInSlot(0, ItemStack.EMPTY);
                    }
                    notifyUpdate();
                    return InteractionResult.CONSUME;
                    //return InteractionResult.FAIL;
                }
                else return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}