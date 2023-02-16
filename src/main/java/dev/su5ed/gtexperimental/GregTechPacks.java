package dev.su5ed.gtexperimental;

import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.datagen.pack.ClassicIC2RecipesPackGen;
import dev.su5ed.gtexperimental.datagen.pack.ExperimentalIC2RecipesPackGen;
import dev.su5ed.gtexperimental.datagen.pack.FTBICRecipesPackGen;
import dev.su5ed.gtexperimental.util.ProfileManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.PathPackResources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static cpw.mods.modlauncher.api.LamdbaExceptionUtils.rethrowConsumer;

public final class GregTechPacks implements RepositorySource {
    public static final RepositorySource INSTANCE = new GregTechPacks();
    private static final Map<String, BooleanSupplier> CONDITIONAL_PACKS = Map.of(
        ClassicIC2RecipesPackGen.NAME, () -> ModHandler.ic2Loaded && ProfileManager.INSTANCE.isClassic(),
        ExperimentalIC2RecipesPackGen.NAME, () -> ModHandler.ic2Loaded && !ProfileManager.INSTANCE.isClassic(),
        FTBICRecipesPackGen.NAME, () -> ModHandler.ftbicLoaded
    );

    private GregTechPacks() {}

    @Override
    public void loadPacks(Consumer<Pack> packConsumer, Pack.PackConstructor packFactory) {
        IModFile modFile = ModList.get().getModFileById(Reference.MODID).getFile();

        Path packsPath = modFile.findResource("packs");
        if (Files.exists(packsPath) && Files.isDirectory(packsPath)) {
            try (Stream<Path> paths = Files.list(packsPath)) {
                paths
                    .filter(p -> {
                        BooleanSupplier condition = CONDITIONAL_PACKS.get(p.getFileName().toString());
                        return condition == null || condition.getAsBoolean();
                    })
                    .forEach(rethrowConsumer(path -> {
                        String packName = "mod:" + Reference.MODID + ":" + path.getFileName();
                        PackResources packResources = new PathPackResources(packName, path);
                        var metadata = packResources.getMetadataSection(PackMetadataSection.SERIALIZER);
                        if (metadata != null) {
                            Pack pack = packFactory.create(packName, Component.literal(Reference.NAME + " " + path.getFileName()), false, () -> packResources, metadata, Pack.Position.TOP, PackSource.DEFAULT, false);
                            if (pack != null) {
                                packConsumer.accept(pack);
                            }
                        }
                    }));
            } catch (IOException e) {
                GregTechMod.LOGGER.error("Error loading additional GTE datapacks", e);
            }
        }
    }
}
