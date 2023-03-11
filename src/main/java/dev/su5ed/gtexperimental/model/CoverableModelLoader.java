package dev.su5ed.gtexperimental.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.ExtendedBlockModelDeserializer;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

public class CoverableModelLoader implements IGeometryLoader<CoverableModelGeometry> {
    public static final ResourceLocation NAME = location("coverable");

    @Override
    public CoverableModelGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
        jsonObject.remove("loader"); // Remove before parsing BlockModel to prevent SO
        BlockModel blockModel = ExtendedBlockModelDeserializer.INSTANCE.getAdapter(BlockModel.class).fromJsonTree(jsonObject);
        return new CoverableModelGeometry(blockModel);
    }
}
