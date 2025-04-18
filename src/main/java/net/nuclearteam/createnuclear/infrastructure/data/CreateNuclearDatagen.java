package net.nuclearteam.createnuclear.infrastructure.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.tterrag.registrate.providers.ProviderType;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.foundation.advancement.CNAdvancement;
import net.nuclearteam.createnuclear.foundation.data.recipe.CNProcessingRecipeGen;
import net.nuclearteam.createnuclear.foundation.data.recipe.CNStandardRecipeGen;
import net.nuclearteam.createnuclear.foundation.ponder.CreateNuclearPonderPlugin;

import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class CreateNuclearDatagen {
    public static void gatherData(GatherDataEvent event) {
        addExtraRegistrateData();

        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();


        GeneratedEntriesProvider generatedEntriesProvider = new GeneratedEntriesProvider(output, lookupProvider);
        lookupProvider = generatedEntriesProvider.getRegistryProvider();
        generator.addProvider(event.includeClient(), generatedEntriesProvider);
        generator.addProvider(event.includeClient(), new CNStandardRecipeGen(output));
        generator.addProvider(event.includeClient(), new CNAdvancement(output));

        /*if (event.includeClient()) {

        }*/

        if (event.includeServer()) {
            CNProcessingRecipeGen.registerAll(generator, output);
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
