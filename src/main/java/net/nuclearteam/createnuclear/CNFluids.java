package net.nuclearteam.createnuclear;

import com.simibubi.create.AllFluids;
import com.tterrag.registrate.builders.FluidBuilder.FluidTypeFactory;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.createmod.catnip.theme.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry.InteractionInformation;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.nuclearteam.createnuclear.content.decoration.palettes.CNPaletteStoneTypes;
import org.joml.Vector3f;
import net.nuclearteam.createnuclear.CNTags.CNFluidTags;

import java.util.List;
import java.util.function.Supplier;

public class CNFluids {
    public static final FluidEntry<BaseFlowingFluid.Flowing> URANIUM =
        CreateNuclear.REGISTRATE.standardFluid("uranium", SolidRenderedPlaceableFluidity.create(0x38FF08, () -> 1f / 32f))
            .lang("Liquid Uranium")
            .tag(CNFluidTags.URANIUM.tag)
            .properties(p -> p.viscosity(2500)
                .density(1600)
                .canSwim(false)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
                .canDrown(false)
            )
            .fluidProperties(f -> f.levelDecreasePerBlock(2)
                .tickRate(15)
                .slopeFindDistance(6)
                .explosionResistance(100f)
            )
            .source(BaseFlowingFluid.Source::new)
            .bucket()
            .tag(CNTags.forgeItemTag("buckets/uranium"))
            .lang("Uranium Bucket")
            .build()
            .register();

    public static void register() {}

    public static void handleFluidEffect(LivingEvent.LivingVisibilityEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.isAlive() && !(entity.isSpectator())) {
            if (entity.tickCount % 20 == 0) return;
            if (entity.isInFluidType(URANIUM.getType())) {
                entity.addEffect(new MobEffectInstance(CNEffects.RADIATION.getDelegate(), 100, 0));
            }
        }

    }

    public static void registerFluidInteractions() {
        // Supplier for the common BlockState to return (Autunite)
        Supplier<BlockState> autuniteState = () -> CNPaletteStoneTypes.AUTUNITE.getBaseBlock().get().defaultBlockState();

        // The FluidType that all interactions will target (uranium)
        FluidType uraniumType = URANIUM.get().getFluidType();

        // List of source FluidTypes we want to register (lava and water)
        List<FluidType> sourceFluids = List.of(
            NeoForgeMod.LAVA_TYPE.value(),
            NeoForgeMod.WATER_TYPE.value()
        );

        // Loop over each source fluid and register the interaction
        for (FluidType source : sourceFluids) {
            FluidInteractionRegistry.addInteraction(source, new InteractionInformation(uraniumType, fs -> autuniteState.get()));
        }
    }

    private static class SolidRenderedPlaceableFluidity extends AllFluids.TintedFluidType {

        private Vector3f fogColor;
        private Supplier<Float> fogDistance;

        public static FluidTypeFactory create(int fogColor, Supplier<Float> fogDistance) {
            return (p, s, f) -> {
                SolidRenderedPlaceableFluidity fluidtype = new SolidRenderedPlaceableFluidity(p,s,f);
                fluidtype.fogColor = new Color(fogColor, false).asVectorF();
                fluidtype.fogDistance = fogDistance;
                return fluidtype;
            };
        }

        private SolidRenderedPlaceableFluidity(Properties properties, ResourceLocation stillTecture, ResourceLocation flowingTexture) {
            super(properties, stillTecture, flowingTexture);
        }

        @Override
        protected int getTintColor(FluidStack stack) {
            return NO_TINT;
        }

        @Override
        protected int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
            return 0x38FF08;
        }

        @Override
        protected Vector3f getCustomFogColor() {
            return fogColor;
        }

        @Override
        protected float getFogDistanceModifier() {
            return fogDistance.get();
        }
    }
}
