package org.example.Parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.Model.*;
import org.example.Model.Musica.Album;
import org.example.Model.Musica.Music;
import org.example.Model.Playlist.PlayList;
import org.example.Model.User.Plan;
import org.example.Model.User.User;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataSaver {

    public static void saveDataToJson(SpotifUM spotifUM, String filePath) {
        List<User> users = new ArrayList<>(spotifUM.getAllUsers().values());
        List<Album> albums = new ArrayList<>(spotifUM.getAllAlbums().values());
        List<PlayList> playlists = new ArrayList<>(spotifUM.getAllPlaylists().values());

        DataBundle bundle = new DataBundle(users, albums, playlists);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Plan.class, new PlanAdapter()) // Registra o adaptador para Plan
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()) // Adaptador para LocalDateTime
                .registerTypeAdapter(PlayList.class, new PlayListAdapter())
                .registerTypeAdapter(Music.class, new MusicAdapter())
                .create();

        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(bundle, writer);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao escrever ficheiro JSON: " + e.getMessage(), e);
        }
    }
}