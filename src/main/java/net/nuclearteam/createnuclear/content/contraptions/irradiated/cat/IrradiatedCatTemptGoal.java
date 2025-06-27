package net.nuclearteam.createnuclear.content.contraptions.irradiated.cat;

import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class IrradiatedCatTemptGoal extends TemptGoal {
    @Nullable
    private Player selectedPlayer;
    private final IrradiatedCat irradiatedCat;

    public IrradiatedCatTemptGoal(IrradiatedCat cat, double speedModifier, Predicate<ItemStack> items, boolean canScare) {
        super(cat, speedModifier, items, canScare);
        this.irradiatedCat = cat;
    }

    public void tick() {
        super.tick();
        if (this.selectedPlayer == null && this.mob.getRandom().nextInt(this.adjustedTickDelay(600)) == 0) {
            this.selectedPlayer = this.player;
        } else if (this.mob.getRandom().nextInt(this.adjustedTickDelay(500)) == 0) {
            this.selectedPlayer = null;
        }

    }

    protected boolean canScare() {
        return (this.selectedPlayer == null || !this.selectedPlayer.equals(this.player)) && super.canScare();
    }

    public boolean canUse() {
        return super.canUse() && !this.irradiatedCat.isTame();
    }
}
