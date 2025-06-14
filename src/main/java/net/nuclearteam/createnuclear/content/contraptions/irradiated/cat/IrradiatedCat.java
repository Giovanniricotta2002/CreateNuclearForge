package net.nuclearteam.createnuclear.content.contraptions.irradiated.cat;


import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.EventHooks;
import net.nuclearteam.createnuclear.CNEntityType;
import net.nuclearteam.createnuclear.CNTags;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.function.Predicate;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings({"unused", "deprecation"})
public class IrradiatedCat extends TamableAnimal {
    public static final double TEMPT_SPEED_MOD = 0.6;
    public static final double WALK_SPEED_MOD = 0.8;
    public static final double SPRINT_SPEED_MOD = 1.33;
    private static final EntityDataAccessor<Boolean> IS_LYING;
    private static final EntityDataAccessor<Boolean> RELAX_STATE_ONE;
    private static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR;
    @Nullable
    private CatAvoidEntityGoal<Player> avoidPlayersGoal;
    @Nullable
    private TemptGoal temptGoal;
    private float lieDownAmount;
    private float lieDownAmountO;
    private float lieDownAmountTail;
    private float lieDownAmountOTail;
    private float relaxStateOneAmount;
    private float relaxStateOneAmountO;

    public IrradiatedCat(EntityType<? extends IrradiatedCat> entityType, Level level) {
        super(entityType, level);
        this.reassessTameGoals();
    }

    protected void registerGoals() {
        this.temptGoal = new CatTemptGoal(this, 0.6, (p_335255_) -> p_335255_.is(CNTags.CNItemTags.FUEL.tag), true);
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new CatRelaxOnOwnerGoal(this));
        this.goalSelector.addGoal(4, this.temptGoal);
        this.goalSelector.addGoal(5, new CatLieOnBedGoal(this, 1.1, 8));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, (double)1.0F, 10.0F, 5.0F));
        this.goalSelector.addGoal(7, new CatSitOnBlockGoal(this, 0.8));
        this.goalSelector.addGoal(8, new LeapAtTargetGoal(this, 0.3F));
        this.goalSelector.addGoal(9, new OcelotAttackGoal(this));
        this.goalSelector.addGoal(10, new BreedGoal(this, 0.8));
        this.goalSelector.addGoal(11, new WaterAvoidingRandomStrollGoal(this, 0.8, 1.0000001E-5F));
        this.goalSelector.addGoal(12, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>(this, Rabbit.class, false, (Predicate)null));
        this.targetSelector.addGoal(1, new NonTameRandomTargetGoal(this, Turtle.class, false, Turtle.BABY_ON_LAND_SELECTOR));
    }

    public void setLying(boolean lying) {
        this.entityData.set(IS_LYING, lying);
    }

    public boolean isLying() {
        return (Boolean)this.entityData.get(IS_LYING);
    }

    void setRelaxStateOne(boolean relaxStateOne) {
        this.entityData.set(RELAX_STATE_ONE, relaxStateOne);
    }

    boolean isRelaxStateOne() {
        return (Boolean)this.entityData.get(RELAX_STATE_ONE);
    }



    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_LYING, false);
        builder.define(RELAX_STATE_ONE, false);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
    }

    public void customServerAiStep() {
        if (this.getMoveControl().hasWanted()) {
            double d0 = this.getMoveControl().getSpeedModifier();
            if (d0 == 0.6) {
                this.setPose(Pose.CROUCHING);
                this.setSprinting(false);
            } else if (d0 == 1.33) {
                this.setPose(Pose.STANDING);
                this.setSprinting(true);
            } else {
                this.setPose(Pose.STANDING);
                this.setSprinting(false);
            }
        } else {
            this.setPose(Pose.STANDING);
            this.setSprinting(false);
        }

    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        if (this.isTame()) {
            if (this.isInLove()) {
                return SoundEvents.CAT_PURR;
            } else {
                return this.random.nextInt(4) == 0 ? SoundEvents.CAT_PURREOW : SoundEvents.CAT_AMBIENT;
            }
        } else {
            return SoundEvents.CAT_STRAY_AMBIENT;
        }
    }

    public int getAmbientSoundInterval() {
        return 120;
    }

    public void hiss() {
        this.makeSound(SoundEvents.CAT_HISS);
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.CAT_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.CAT_DEATH;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, (double)10.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.3F)
                .add(Attributes.ATTACK_DAMAGE, (double)3.0F);
    }

    protected void usePlayerItem(Player player, InteractionHand hand, ItemStack stack) {
        if (this.isFood(stack)) {
            this.playSound(SoundEvents.CAT_EAT, 1.0F, 1.0F);
        }

        super.usePlayerItem(player, hand, stack);
    }

    public void tick() {
        super.tick();
        if (this.temptGoal != null && this.temptGoal.isRunning() && !this.isTame() && this.tickCount % 100 == 0) {
            this.playSound(SoundEvents.CAT_BEG_FOR_FOOD, 1.0F, 1.0F);
        }

        this.handleLieDown();
    }

    private void handleLieDown() {
        if ((this.isLying() || this.isRelaxStateOne()) && this.tickCount % 5 == 0) {
            this.playSound(SoundEvents.CAT_PURR, 0.6F + 0.4F * (this.random.nextFloat() - this.random.nextFloat()), 1.0F);
        }

        this.updateLieDownAmount();
        this.updateRelaxStateOneAmount();
    }

    private void updateLieDownAmount() {
        this.lieDownAmountO = this.lieDownAmount;
        this.lieDownAmountOTail = this.lieDownAmountTail;
        if (this.isLying()) {
            this.lieDownAmount = Math.min(1.0F, this.lieDownAmount + 0.15F);
            this.lieDownAmountTail = Math.min(1.0F, this.lieDownAmountTail + 0.08F);
        } else {
            this.lieDownAmount = Math.max(0.0F, this.lieDownAmount - 0.22F);
            this.lieDownAmountTail = Math.max(0.0F, this.lieDownAmountTail - 0.13F);
        }

    }

    private void updateRelaxStateOneAmount() {
        this.relaxStateOneAmountO = this.relaxStateOneAmount;
        if (this.isRelaxStateOne()) {
            this.relaxStateOneAmount = Math.min(1.0F, this.relaxStateOneAmount + 0.1F);
        } else {
            this.relaxStateOneAmount = Math.max(0.0F, this.relaxStateOneAmount - 0.13F);
        }

    }

    public float getLieDownAmount(float partialTicks) {
        return Mth.lerp(partialTicks, this.lieDownAmountO, this.lieDownAmount);
    }

    public float getLieDownAmountTail(float partialTicks) {
        return Mth.lerp(partialTicks, this.lieDownAmountOTail, this.lieDownAmountTail);
    }

    public float getRelaxStateOneAmount(float partialTicks) {
        return Mth.lerp(partialTicks, this.relaxStateOneAmountO, this.relaxStateOneAmount);
    }

    @Nullable
    public IrradiatedCat getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
       return CNEntityType.IRRADIATED_CAT.create(level);
    }

    public boolean canMate(Animal otherAnimal) {
        if (!this.isTame()) {
            return false;
        } else {
            boolean var10000;
            if (otherAnimal instanceof IrradiatedCat) {
                IrradiatedCat cat = (IrradiatedCat) otherAnimal;
                var10000 = cat.isTame() && super.canMate(otherAnimal);
            } else {
                var10000 = false;
            }

            return var10000;
        }
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        spawnGroupData = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        boolean flag = level.getMoonBrightness() > 0.9F;
        ServerLevel serverlevel = level.getLevel();


        return spawnGroupData;
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();
        if (this.isTame()) {
            if (this.isOwnedBy(player)) {
                if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
                    if (!this.level().isClientSide()) {
                        FoodProperties foodproperties = itemstack.getFoodProperties(this);
                        this.heal(foodproperties != null ? (float)foodproperties.nutrition() : 1.0F);
                        this.usePlayerItem(player, hand, itemstack);
                    }

                    return InteractionResult.sidedSuccess(this.level().isClientSide());
                }

                InteractionResult interactionresult = super.mobInteract(player, hand);
                if (!interactionresult.consumesAction()) {
                    this.setOrderedToSit(!this.isOrderedToSit());
                    return InteractionResult.sidedSuccess(this.level().isClientSide());
                }

                return interactionresult;
            }
        } else if (this.isFood(itemstack)) {
            if (!this.level().isClientSide()) {
                this.usePlayerItem(player, hand, itemstack);
                this.tryToTame(player);
                this.setPersistenceRequired();
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }

        InteractionResult interactionresult1 = super.mobInteract(player, hand);
        if (interactionresult1.consumesAction()) {
            this.setPersistenceRequired();
        }

        return interactionresult1;
    }

    public boolean isFood(ItemStack stack) {
        return stack.is(CNTags.CNItemTags.FUEL.tag);
    }

    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isTame() && this.tickCount > 2400;
    }

    public void setTame(boolean tame, boolean applyTamingSideEffects) {
        super.setTame(tame, applyTamingSideEffects);
        this.reassessTameGoals();
    }

    protected void reassessTameGoals() {
        if (this.avoidPlayersGoal == null) {
            this.avoidPlayersGoal = new CatAvoidEntityGoal<Player>(this, Player.class, 16.0F, 0.8, 1.33);
        }

        this.goalSelector.removeGoal(this.avoidPlayersGoal);
        if (!this.isTame()) {
            this.goalSelector.addGoal(4, this.avoidPlayersGoal);
        }

    }

    private void tryToTame(Player player) {
        if (this.random.nextInt(3) == 0 && !EventHooks.onAnimalTame(this, player)) {
            this.tame(player);
            this.setOrderedToSit(true);
            this.level().broadcastEntityEvent(this, (byte)7);
        } else {
            this.level().broadcastEntityEvent(this, (byte)6);
        }

    }

    public boolean isSteppingCarefully() {
        return this.isCrouching() || super.isSteppingCarefully();
    }

    static {
        IS_LYING = SynchedEntityData.defineId(IrradiatedCat.class, EntityDataSerializers.BOOLEAN);
        RELAX_STATE_ONE = SynchedEntityData.defineId(IrradiatedCat.class, EntityDataSerializers.BOOLEAN);
        DATA_COLLAR_COLOR = SynchedEntityData.defineId(IrradiatedCat.class, EntityDataSerializers.INT);
    }

    static class CatAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
        private final IrradiatedCat cat;

        public CatAvoidEntityGoal(IrradiatedCat cat, Class<T> entityClassToAvoid, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
            super(cat, entityClassToAvoid, maxDist, walkSpeedModifier, sprintSpeedModifier, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
            this.cat = cat;
        }

        public boolean canUse() {
            return !this.cat.isTame() && super.canUse();
        }

        public boolean canContinueToUse() {
            return !this.cat.isTame() && super.canContinueToUse();
        }
    }

    static class CatRelaxOnOwnerGoal extends Goal {
        private final IrradiatedCat cat;
        @Nullable
        private Player ownerPlayer;
        @Nullable
        private BlockPos goalPos;
        private int onBedTicks;

        public CatRelaxOnOwnerGoal(IrradiatedCat cat) {
            this.cat = cat;
        }

        public boolean canUse() {
            if (!this.cat.isTame()) {
                return false;
            } else if (this.cat.isOrderedToSit()) {
                return false;
            } else {
                LivingEntity livingentity = this.cat.getOwner();
                if (livingentity instanceof Player) {
                    this.ownerPlayer = (Player)livingentity;
                    if (!livingentity.isSleeping()) {
                        return false;
                    }

                    if (this.cat.distanceToSqr(this.ownerPlayer) > (double)100.0F) {
                        return false;
                    }

                    BlockPos blockpos = this.ownerPlayer.blockPosition();
                    BlockState blockstate = this.cat.level().getBlockState(blockpos);
                }

                return false;
            }
        }

        private boolean spaceIsOccupied() {
            for(IrradiatedCat cat : this.cat.level().getEntitiesOfClass(IrradiatedCat.class, (new AABB(this.goalPos)).inflate((double)2.0F))) {
                if (cat != this.cat && (cat.isLying() || cat.isRelaxStateOne())) {
                    return true;
                }
            }

            return false;
        }

        public boolean canContinueToUse() {
            return this.cat.isTame() && !this.cat.isOrderedToSit() && this.ownerPlayer != null && this.ownerPlayer.isSleeping() && this.goalPos != null && !this.spaceIsOccupied();
        }

        public void start() {
            if (this.goalPos != null) {
                this.cat.setInSittingPose(false);
                this.cat.getNavigation().moveTo((double)this.goalPos.getX(), (double)this.goalPos.getY(), (double)this.goalPos.getZ(), (double)1.1F);
            }

        }

        public void stop() {
            this.cat.setLying(false);
            float f = this.cat.level().getTimeOfDay(1.0F);
            if (this.ownerPlayer.getSleepTimer() >= 100 && (double)f > 0.77 && (double)f < 0.8 && (double)this.cat.level().getRandom().nextFloat() < 0.7) {
                this.giveMorningGift();
            }

            this.onBedTicks = 0;
            this.cat.setRelaxStateOne(false);
            this.cat.getNavigation().stop();
        }

        private void giveMorningGift() {
            RandomSource randomsource = this.cat.getRandom();
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            blockpos$mutableblockpos.set(this.cat.isLeashed() ? this.cat.getLeashHolder().blockPosition() : this.cat.blockPosition());
            this.cat.randomTeleport((double)(blockpos$mutableblockpos.getX() + randomsource.nextInt(11) - 5), (double)(blockpos$mutableblockpos.getY() + randomsource.nextInt(5) - 2), (double)(blockpos$mutableblockpos.getZ() + randomsource.nextInt(11) - 5), false);
            blockpos$mutableblockpos.set(this.cat.blockPosition());
            LootTable loottable = this.cat.level().getServer().reloadableRegistries().getLootTable(BuiltInLootTables.CAT_MORNING_GIFT);
            LootParams lootparams = (new LootParams.Builder((ServerLevel)this.cat.level())).withParameter(LootContextParams.ORIGIN, this.cat.position()).withParameter(LootContextParams.THIS_ENTITY, this.cat).create(LootContextParamSets.GIFT);
            ObjectListIterator var5 = loottable.getRandomItems(lootparams).iterator();

            while(var5.hasNext()) {
                ItemStack itemstack = (ItemStack)var5.next();
                this.cat.level().addFreshEntity(new ItemEntity(this.cat.level(), (double)blockpos$mutableblockpos.getX() - (double)Mth.sin(this.cat.yBodyRot * ((float)Math.PI / 180F)), (double)blockpos$mutableblockpos.getY(), (double)blockpos$mutableblockpos.getZ() + (double)Mth.cos(this.cat.yBodyRot * ((float)Math.PI / 180F)), itemstack));
            }

        }

        public void tick() {
            if (this.ownerPlayer != null && this.goalPos != null) {
                this.cat.setInSittingPose(false);
                this.cat.getNavigation().moveTo((double)this.goalPos.getX(), (double)this.goalPos.getY(), (double)this.goalPos.getZ(), (double)1.1F);
                if (this.cat.distanceToSqr(this.ownerPlayer) < (double)2.5F) {
                    ++this.onBedTicks;
                    if (this.onBedTicks > this.adjustedTickDelay(16)) {
                        this.cat.setLying(true);
                        this.cat.setRelaxStateOne(false);
                    } else {
                        this.cat.lookAt(this.ownerPlayer, 45.0F, 45.0F);
                        this.cat.setRelaxStateOne(true);
                    }
                } else {
                    this.cat.setLying(false);
                }
            }

        }
    }

    static class CatTemptGoal extends TemptGoal {
        @Nullable
        private Player selectedPlayer;
        private final IrradiatedCat cat;

        public CatTemptGoal(IrradiatedCat cat, double speedModifier, Predicate<ItemStack> items, boolean canScare) {
            super(cat, speedModifier, items, canScare);
            this.cat = cat;
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
            return this.selectedPlayer != null && this.selectedPlayer.equals(this.player) ? false : super.canScare();
        }

        public boolean canUse() {
            return super.canUse() && !this.cat.isTame();
        }
    }
}
