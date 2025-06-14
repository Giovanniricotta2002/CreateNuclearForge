package net.nuclearteam.createnuclear.content.contraptions.irradiated.wolf;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class BegGoal extends Goal {
    private final IrradiatedWolf irradiatedWolf;
    @Nullable
    private Player player;
    private final Level level;
    private final float lookDistance;
    private int lookTime;
    private final TargetingConditions begTargeting;

    public BegGoal(IrradiatedWolf wolf, float lookDistance) {
        this.irradiatedWolf = wolf;
        this.level = wolf.level();
        this.lookDistance = lookDistance;
        this.begTargeting = TargetingConditions.forNonCombat().range((double)lookDistance);
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        this.player = this.level.getNearestPlayer(this.begTargeting, this.irradiatedWolf);
        return this.player == null ? false : this.playerHoldingInteresting(this.player);
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.player.isAlive()) {
            return false;
        } else {
            return !(this.irradiatedWolf.distanceToSqr(this.player) > (double) (this.lookDistance * this.lookDistance)) && this.lookTime > 0 && this.playerHoldingInteresting(this.player);
        }
    }

    @Override
    public void start() {
        this.irradiatedWolf.setIsInterested(true);
        this.lookTime = this.adjustedTickDelay(40 + this.irradiatedWolf.getRandom().nextInt(40));
    }

    @Override
    public void stop() {
        this.irradiatedWolf.setIsInterested(false);
        this.player = null;
    }

    @Override
    public void tick() {
        this.irradiatedWolf.getLookControl().setLookAt(this.player.getX(), this.player.getEyeY(), this.player.getZ(), 10.0F, (float)this.irradiatedWolf.getMaxHeadXRot());
        this.lookTime--;
    }

    /**
     * Gets if the Player has the Bone in the hand.
     */
    private boolean playerHoldingInteresting(Player player) {
        for (InteractionHand interactionhand : InteractionHand.values()) {
            ItemStack itemstack = player.getItemInHand(interactionhand);
            if (itemstack.is(Items.BONE) || this.irradiatedWolf.isFood(itemstack)) {
                return true;
            }
        }

        return false;
    }
}
