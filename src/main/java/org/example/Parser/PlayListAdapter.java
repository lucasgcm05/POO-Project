package org.example.Parser;

import com.google.gson.*;
import org.example.Model.Playlist.*;

import java.lang.reflect.Type;

public class PlayListAdapter implements JsonSerializer<PlayList>, JsonDeserializer<PlayList> {
    @Override
    public JsonElement serialize(PlayList src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = context.serialize(src).getAsJsonObject();
        obj.addProperty("type", src.getClass().getSimpleName()); // para identificar a subclasse
        return obj;
    }

    @Override
    public PlayList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        String type = obj.get("type").getAsString();

        Class<? extends PlayList> clazz;
        switch (type) {
            case "FavoritesPlaylist":
                clazz = FavoritesPlaylist.class;
                break;
            case "GenreTimeLimitedPlaylist":
                clazz = GenreTimeLimitedPlaylist.class;
                break;
            case "UserCreatedPlaylist":
                clazz = UserCreatedPlaylist.class;
                break;
            case "RandomPlaylist":
                clazz = RandomPlaylist.class;
                break;
            default:
                throw new JsonParseException("Tipo de playlist desconhecido: " + type);
        }

        return context.deserialize(json, clazz);
    }
}

