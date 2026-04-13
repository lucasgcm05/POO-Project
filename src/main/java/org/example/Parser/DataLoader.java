package org.example.Parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.Model.Musica.Music;
import org.example.Model.Playlist.PlayList;
import org.example.Model.User.Plan;


import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;

public class DataLoader {

    public static DataBundle loadData(String jsonFilePath) {
        try (FileReader reader = new FileReader(jsonFilePath)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Plan.class, new PlanAdapter())
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .registerTypeAdapter(PlayList.class, new PlayListAdapter())
                    .registerTypeAdapter(Music.class, new MusicAdapter())
                    .create(); // Removido o MusicMapDeserializer

            return gson.fromJson(reader, DataBundle.class);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler ficheiro de dados: " + e.getMessage(), e);
        }
    }
}