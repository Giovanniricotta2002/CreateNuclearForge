package net.nuclearteam.createnuclear.infrastructure.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.data.recipe.CreateMechanicalCraftingRecipeGen;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.tterrag.registrate.providers.ProviderType;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.foundation.advancement.CNAdvancement;
import net.nuclearteam.createnuclear.foundation.data.recipe.CNMechanicalCraftingRecipeGen;
import net.nuclearteam.createnuclear.foundation.data.recipe.CNRecipeProvider;
import net.nuclearteam.createnuclear.foundation.data.recipe.CNShapelessRecipeGen;
import net.nuclearteam.createnuclear.foundation.data.recipe.CNStandardRecipeGen;
import net.nuclearteam.createnuclear.foundation.ponder.CreateNuclearPonderPlugin;

import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class CreateNuclearDatagen {
    public static void gatherDataHighPriority(GatherDataEvent event) {
        if (event.getMods().contains(CreateNuclear.MOD_ID))
            addExtraRegistrateData();
    }

    public static void gatherData(GatherDataEvent event) {
        if (!event.getMods().contains(CreateNuclear.MOD_ID))
            return;


        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();


        GeneratedEntriesProvider generatedEntriesProvider = new GeneratedEntriesProvider(output, lookupProvider);
        lookupProvider = generatedEntriesProvider.getRegistryProvider();
        generator.addProvider(event.includeServer(), generatedEntriesProvider);

        generator.addProvider(event.includeClient(), new CNStandardRecipeGen(output, lookupProvider));
        generator.addProvider(event.includeServer(), new CNMechanicalCraftingRecipeGen(output, lookupProvider));
//        generator.addProvider(event.includeServer(), new CNShapelessRecipeGen(output, lookupProvider));


        generator.addProvider(event.includeClient(), new CNAdvancement(output, lookupProvider));

        if (event.includeServer()) {
            CNRecipeProvider.registerAllProcessing(generator, output, lookupProvider);
        }
    }

    private static void addExtraRegistrateData() {
        CreateNuclearRegistrateTags.addGenerators();

        CreateNuclear.REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
            BiConsumer<String, String> langConsumer = provider::add;

            provideDefaultLang("interface", langConsumer);
            provideDefaultLang("potion", langConsumer);
            provideDefaultLang("tooltips", langConsumer);
            provideDefaultLang("reactor", langConsumer);
            CNAdvancement.provideLang(langConsumer);
            providePonderLang(langConsumer);
        });
    }

    private static void provideDefaultLang(String fileName, BiConsumer<String, String> consumer) {
        String path = "assets/createnuclear/lang/default/" + fileName + ".json";
        JsonElement jsonElement = FilesHelper.loadJsonResource(path);
        if (jsonElement == null) {
            throw new IllegalStateException(String.format("Could not find default lang file: %s", path));
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().getAsString();
            consumer.accept(key, value);
        }
    }

    private static void providePonderLang(BiConsumer<String, String> consumer) {
        PonderIndex.addPlugin(new CreateNuclearPonderPlugin());
        PonderIndex.getLangAccess().provideLang(CreateNuclear.MOD_ID, consumer);

    }
}
