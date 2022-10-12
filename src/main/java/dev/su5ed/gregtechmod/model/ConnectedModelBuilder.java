package dev.su5ed.gregtechmod.model;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ConnectedModelBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {
    private String name;
    
    public ConnectedModelBuilder(T parent, ExistingFileHelper existingFileHelper) {
        super(ConnectedModelLoader.NAME, parent, existingFileHelper);
    }

    public ConnectedModelBuilder<T> setTextureRoot(String name) {
        this.name = name;
        return this;
    }

    @Override
    public JsonObject toJson(JsonObject json) {
        json = super.toJson(json);

        Preconditions.checkNotNull(this.name, "name must not be null");
        json.addProperty("name", this.name);

        return json;
    }
}
