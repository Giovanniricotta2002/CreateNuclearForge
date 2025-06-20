package net.nuclearteam.createnuclear;


import com.mojang.logging.LogUtils;
import com.simibubi.create.AllDataComponents;
import com.simibubi.create.CreateBuildInfo;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.simibubi.create.infrastructure.data.CreateDatagen;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.nuclearteam.createnuclear.content.decoration.palettes.CNPaletteBlocks;
import net.nuclearteam.createnuclear.content.equipment.armor.CNArmorMaterials;
import net.nuclearteam.createnuclear.content.kinetics.fan.processing.CNFanProcessingTypes;
import net.nuclearteam.createnuclear.foundation.advancement.CNAdvancement;
import net.nuclearteam.createnuclear.foundation.advancement.CNTriggers;
import net.nuclearteam.createnuclear.infrastructure.config.CNConfigs;
import net.nuclearteam.createnuclear.infrastructure.data.CreateNuclearDatagen;
import org.slf4j.Logger;

import com.simibubi.create.api.registrate.CreateRegistrateRegistrationCallback;

@Mod(CreateNuclear.MOD_ID)
public class CreateNuclear {
    public static final String MOD_ID = "createnuclear";
    public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * <b>Other mods should not use this field!</b> If you are an addon developer, create your own instance of
     * {@link CreateRegistrate}.
     * </br
     * If you were using this instance to render a callback listener use {@link CreateRegistrateRegistrationCallback#register} instead.
     */
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID)
            .defaultCreativeTab((ResourceKey<CreativeModeTab>) null)
            .setTooltipModifierFactory(item ->
                    new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                            .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
            );

    public CreateNuclear(IEventBus eventBus, ModContainer modContainer) {
        onCtor(eventBus, modContainer);
    }

    public static void onCtor(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("{} {} initializing! Commit hash: {}", MOD_ID, CreateBuildInfo.VERSION, CreateBuildInfo.GIT_COMMIT);

        ModLoadingContext modLoadingContext = ModLoadingContext.get();

        IEventBus forgeEventBus = NeoForge.EVENT_BUS;

        REGISTRATE.registerEventListeners(modEventBus);


        CNTags.init();
        CNBlocks.register();
        CNBlockEntityTypes.register();
        CNItems.register();
        CNPackets.register();
        CNMenus.register();
        CNFluids.register();
        CNEntityType.register();
        CNPaletteBlocks.register();

        CNArmorMaterials.register(modEventBus);
        CNDataComponents.register(modEventBus);

        CNConfigs.register(modLoadingContext, modContainer);

        CNCreativeModeTabs.register(modEventBus);
        CNEffects.register(modEventBus);
        CNPotions.register(modEventBus);
        CNRecipeTypes.register(modEventBus);

        modEventBus.addListener(CreateNuclear::init);
        modEventBus.addListener(CreateNuclear::onRegister);
        modEventBus.addListener(EventPriority.LOWEST, CreateNuclearDatagen::gatherData);
        forgeEventBus.addListener(CNFluids::handleFluidEffect);

        modEventBus.addListener(EventPriority.HIGHEST, CreateNuclearDatagen::gatherDataHighPriority);

        //DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> CreateNuclearClient.onCtorClient(modEventBus, forgeEventBus));
    }

    public static void init(final FMLCommonSetupEvent event) {
        CNFluids.registerFluidInteractions();
    }

    public static void onRegister(final RegisterEvent event) {
        CNFanProcessingTypes.register();

        if (event.getRegistry() == BuiltInRegistries.TRIGGER_TYPES) {
            CNAdvancement.register();
            CNTriggers.register();
        }
    }


    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

}
