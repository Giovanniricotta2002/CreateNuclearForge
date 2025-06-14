package net.nuclearteam.createnuclear.content.multiblock.bluePrintItem;

import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.nuclearteam.createnuclear.CNDataComponents;
import net.nuclearteam.createnuclear.CNPackets;
import net.nuclearteam.createnuclear.foundation.gui.CNGuiTextures;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.simibubi.create.foundation.gui.AllGuiTextures.PLAYER_INVENTORY;

@ParametersAreNonnullByDefault
@SuppressWarnings({"unused"})
public class ReactorBluePrintItemScreen extends AbstractSimiContainerScreen<ReactorBluePrintMenu> {
    protected static final CNGuiTextures BG = CNGuiTextures.CONFIGURED_PATTERN_GUI;

    public ReactorBluePrintItemScreen(ReactorBluePrintMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void init() {
        setWindowSize(BG.width, BG.height + PLAYER_INVENTORY.getHeight());
        setWindowOffset(0, 0);
        super.init();
        clearWidgets();
    }


    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = leftPos;
        int y = topPos+38;

        BG.render(guiGraphics, x+23, y-19);
        renderPlayerInventory(guiGraphics, x+23, y+175);

        guiGraphics.drawString(font, title, x+26, y-12, 0x592424, false); //ici pour le titre

    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (!ItemStack.matches(menu.player.getMainHandItem(), menu.contentHolder)) {
            menu.player.closeContainer();
        }

        float coef = 0.1F;

        CompoundTag tag = menu.contentHolder.getOrDefault(CNDataComponents.PATTERN, new CompoundTag());

        sendValueUpdate(tag, coef,
                tag.getInt("graphiteTime"),
                tag.getInt("uraniumTime"),
                tag.getInt("countUraniumRod"),
                tag.getInt("countGraphiteRod")+3
        );


    }


    private static void sendValueUpdate(CompoundTag tag, float heat, int graphiteTime, int uraniumTime, int countGraphiteRod, int countUraniumRod) {
        CatnipServices.NETWORK.sendToServer(new ReactorBluePrintItemPacket(tag, heat, graphiteTime, uraniumTime, countGraphiteRod, countGraphiteRod));
    }
}