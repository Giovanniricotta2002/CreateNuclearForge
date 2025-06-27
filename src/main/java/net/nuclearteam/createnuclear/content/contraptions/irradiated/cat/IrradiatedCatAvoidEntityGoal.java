package net.nuclearteam.createnuclear.content.contraptions.irradiated.cat;

import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

public class IrradiatedCatAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
    private final IrradiatedCat irradiatedCat;

    public IrradiatedCatAvoidEntityGoal(IrradiatedCat cat, Class<T> entityClassToAvoid, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
        super(cat, entityClassToAvoid, maxDist, walkSpeedModifier, sprintSpeedModifier, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
        this.irradiatedCat = cat;
    }

    public boolean canUse() {
        return !this.irradiatedCat.isTame() && super.canUse();
    }

    public boolean canContinueToUse() {
        return !this.irradiatedCat.isTame() && super.canContinueToUse();
    }
}
