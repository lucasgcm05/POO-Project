package org.example.Parser;

import com.google.gson.*;
import org.example.Model.Musica.MultimediaMusic;
import org.example.Model.Musica.Music;

import java.lang.reflect.Type;

public class MusicAdapter implements JsonDeserializer<Music>, JsonSerializer<Music> {

    @Override
    public Music deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        String name = obj.get("name").getAsString();
        String author = obj.get("author").getAsString();
        String publisher = obj.get("publisherName").getAsString();
        String lyrics = obj.get("lyrics").getAsString();
        String music = obj.get("music").getAsString();
        String genre = obj.get("genre").getAsString();
        int duration = obj.get("duration").getAsInt();
        boolean isPlaying = obj.has("isPlaying") && obj.get("isPlaying").getAsBoolean();
        int views = obj.has("views") ? obj.get("views").getAsInt() : 0;

        if (obj.has("videoLink") && !obj.get("videoLink").isJsonNull()) {
            String videoLink = obj.get("videoLink").getAsString();
            return new MultimediaMusic(name, author, publisher, lyrics, music, genre, duration, isPlaying, views, videoLink);
        } else {
            return new Music(name, author, publisher, lyrics, music, genre, duration, isPlaying, views);
        }
    }

    @Override
    public JsonElement serialize(Music src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.addProperty("name", src.getName());
        obj.addProperty("author", src.getAuthor());
        obj.addProperty("publisherName", src.getPublisherName());
        obj.addProperty("lyrics", src.getLyrics());
        obj.addProperty("music", src.getMusic());
        obj.addProperty("genre", src.getGenre());
        obj.addProperty("duration", src.getDuration());
        obj.addProperty("isPlaying", src.isPlaying());
        obj.addProperty("views", src.getViews());

        if (src instanceof MultimediaMusic) {
            obj.addProperty("videoLink", ((MultimediaMusic) src).getVideoLink());
        } else {
            obj.add("videoLink", JsonNull.INSTANCE);
        }

        return obj;
    }
}

