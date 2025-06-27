package net.nuclearteam.createnuclear.content.contraptions.irradiated.cat;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;

@SuppressWarnings({"unused"})
public class IrradiatedCatRelaxOnOwnerGoal extends Goal {
    private final IrradiatedCat irradiatedCat;
    @Nullable
    private Player ownerPlayer;
    @Nullable
    private BlockPos goalPos;
    private int onBedTicks;

    public IrradiatedCatRelaxOnOwnerGoal(IrradiatedCat cat) {
        this.irradiatedCat = cat;
    }

    public boolean canUse() {
        if (!this.irradiatedCat.isTame()) {
            return false;
        } else if (this.irradiatedCat.isOrderedToSit()) {
            return false;
        } else {
            LivingEntity livingentity = this.irradiatedCat.getOwner();
            if (livingentity instanceof Player) {
                this.ownerPlayer = (Player)livingentity;
                if (!livingentity.isSleeping()) {
                    return false;
                }

                if (this.irradiatedCat.distanceToSqr(this.ownerPlayer) > (double)100.0F) {
                    return false;
                }

                BlockPos blockpos = this.ownerPlayer.blockPosition();
                BlockState blockstate = this.irradiatedCat.level().getBlockState(blockpos);
            }

            return false;
        }
    }

    private boolean spaceIsOccupied() {
        BlockPos pos = this.goalPos;
        if (pos == null) {

            return false;
        }

        AABB searchArea = new AABB(pos).inflate(2.0);
        for (IrradiatedCat nearbyCat : this.irradiatedCat.level().getEntitiesOfClass(IrradiatedCat.class, searchArea)) {
            if (nearbyCat != this.irradiatedCat && (nearbyCat.isLying() || nearbyCat.isRelaxStateOne())) {
                return true;
            }
        }

        return false;
    }

    public boolean canContinueToUse() {
        return this.irradiatedCat.isTame() && !this.irradiatedCat.isOrderedToSit() && this.ownerPlayer != null && this.ownerPlayer.isSleeping() && this.goalPos != null && !this.spaceIsOccupied();
    }

    public void start() {
        if (this.goalPos != null) {
            this.irradiatedCat.setInSittingPose(false);
            this.irradiatedCat.getNavigation().moveTo(this.goalPos.getX(), this.goalPos.getY(), this.goalPos.getZ(), 1.1F);
        }

    }

    public void stop() {
        this.irradiatedCat.setLying(false);
        float f = this.irradiatedCat.level().getTimeOfDay(1.0F);
        if (this.ownerPlayer.getSleepTimer() >= 100 && (double)f > 0.77 && (double)f < 0.8 && (double)this.irradiatedCat.level().getRandom().nextFloat() < 0.7) {
            this.giveMorningGift();
        }

        this.onBedTicks = 0;
        this.irradiatedCat.setRelaxStateOne(false);
        this.irradiatedCat.getNavigation().stop();
    }

    private void giveMorningGift() {
        RandomSource randomsource = this.irradiatedCat.getRandom();
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        blockpos$mutableblockpos.set(this.irradiatedCat.isLeashed() ? this.irradiatedCat.getLeashHolder().blockPosition() : this.irradiatedCat.blockPosition());
        this.irradiatedCat.randomTeleport(blockpos$mutableblockpos.getX() + randomsource.nextInt(11) - 5, blockpos$mutableblockpos.getY() + randomsource.nextInt(5) - 2, blockpos$mutableblockpos.getZ() + randomsource.nextInt(11) - 5, false);
        blockpos$mutableblockpos.set(this.irradiatedCat.blockPosition());
        LootTable loottable = this.irradiatedCat.level().getServer().reloadableRegistries().getLootTable(BuiltInLootTables.CAT_MORNING_GIFT);
        LootParams lootparams = (new LootParams.Builder((ServerLevel)this.irradiatedCat.level())).withParameter(LootContextParams.ORIGIN, this.irradiatedCat.position()).withParameter(LootContextParams.THIS_ENTITY, this.irradiatedCat).create(LootContextParamSets.GIFT);

        for (ItemStack itemstack : loottable.getRandomItems(lootparams)) {
            this.irradiatedCat.level().addFreshEntity(new ItemEntity(this.irradiatedCat.level(), (double) blockpos$mutableblockpos.getX() - (double) Mth.sin(this.irradiatedCat.yBodyRot * ((float) Math.PI / 180F)), blockpos$mutableblockpos.getY(), (double) blockpos$mutableblockpos.getZ() + (double) Mth.cos(this.irradiatedCat.yBodyRot * ((float) Math.PI / 180F)), itemstack));
        }

    }

    public void tick() {
        if (this.ownerPlayer != null && this.goalPos != null) {
            this.irradiatedCat.setInSittingPose(false);
            this.irradiatedCat.getNavigation().moveTo(this.goalPos.getX(), this.goalPos.getY(), this.goalPos.getZ(), 1.1F);
            if (this.irradiatedCat.distanceToSqr(this.ownerPlayer) < (double)2.5F) {
                ++this.onBedTicks;
                if (this.onBedTicks > this.adjustedTickDelay(16)) {
                    this.irradiatedCat.setLying(true);
                    this.irradiatedCat.setRelaxStateOne(false);
                } else {
                    this.irradiatedCat.lookAt(this.ownerPlayer, 45.0F, 45.0F);
                    this.irradiatedCat.setRelaxStateOne(true);
                }
            } else {
                this.irradiatedCat.setLying(false);
            }
        }

    }
}
