package net.nuclearteam.createnuclear.content.kinetics.fan.processing;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.theme.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.nuclearteam.createnuclear.*;
import net.nuclearteam.createnuclear.content.enriching.campfire.EnrichingCampfireBlock;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("unused")
public class CNFanProcessingTypes {
    public static final EnrichedType ENRICHED = register("enriched", new EnrichedType());

    private static final Map<String, FanProcessingType> LEGACY_NAME_MAP;

    static {
        Object2ReferenceOpenHashMap<String, FanProcessingType> map = new Object2ReferenceOpenHashMap<>();
        map.put("ENRICHED", ENRICHED);
        map.trim();
        LEGACY_NAME_MAP = map;
    }

    private static <T extends FanProcessingType> T register(String id, T type) {
        return Registry.register(CreateBuiltInRegistries.FAN_PROCESSING_TYPE, CreateNuclear.asResource(id), type);
    }

    @Nullable
    public static FanProcessingType ofLegacyName(String name) {
        return LEGACY_NAME_MAP.get(name);
    }

    public static void register() {}

    public static FanProcessingType parseLegacy(String str) {
        FanProcessingType type = ofLegacyName(str);
        if (type != null) {
            return type;
        }
        return FanProcessingType.parse(str);
    }

    public static class EnrichedType implements FanProcessingType {

        @Override
        public boolean isValidAt(Level level, BlockPos pos) {
            BlockState state = level.getBlockState(pos);
            if (CNTags.CNBlockTags.FAN_PROCESSING_CATALYSTS_ENRICHED.matches(state)) {
                return !state.is(CNBlocks.ENRICHING_CAMPFIRE.get()) || !state.hasProperty(EnrichingCampfireBlock.LIT) || state.getValue(EnrichingCampfireBlock.LIT);
            }
            return false;
        }

        @Override
        public int getPriority() {
            return 301;
        }

        @Override
        public boolean canProcess(ItemStack stack, Level level) {
            Optional<RecipeHolder<Recipe<SingleRecipeInput>>> recipe = CNRecipeTypes.ENRICHED.find(new SingleRecipeInput(stack), level);
            return recipe.isPresent();
        }

        @Nullable
        @Override
        public List<ItemStack> process(ItemStack stack, Level level) {
            Optional<RecipeHolder<Recipe<SingleRecipeInput>>> recipe = CNRecipeTypes.ENRICHED.find(new SingleRecipeInput(stack), level);
            if (recipe.isPresent())
                return RecipeApplier.applyRecipeOn(level, stack, recipe.get());
            return null;
        }

        @Override
        public void spawnProcessingParticles(Level level, Vec3 pos) {
            if (level.random.nextInt(8) != 0) return;
            pos = pos.add(VecHelper.offsetRandomly(Vec3.ZERO, level.random, 1)
                    .multiply(1, 0.5f, 1)
                    .normalize()
                    .scale(0.15f)
            );
            level.addParticle(ParticleTypes.ANGRY_VILLAGER, pos.x, pos.y + .45f, pos.z, 0.0, 0.0, 0.0);
            if (level.random.nextInt(2) != 0) level.addParticle(ParticleTypes.FIREWORK, pos.x, pos.y + .25f, pos.z, 0.0, 0.0, 0.0);
        }

        @Override
        public void morphAirFlow(AirFlowParticleAccess particleAccess, RandomSource random) {
            particleAccess.setColor(Color.mixColors(0x0, 0x126568, random.nextFloat()));
            particleAccess.setAlpha(1f);
            if (random.nextFloat() < 1 / 128f) particleAccess.spawnExtraParticle(ParticleTypes.ASH, .125f);
            if (random.nextFloat() < 1 / 32f) particleAccess.spawnExtraParticle(ParticleTypes.DOLPHIN, .125f);
        }

        @Override
        public void affectEntity(Entity entity, Level level) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.addEffect(new MobEffectInstance(CNEffects.RADIATION.getDelegate(), 10, 0, true, true));
            }
        }
    }
}
