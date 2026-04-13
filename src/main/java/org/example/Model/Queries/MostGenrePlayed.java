package org.example.Model.Queries;

import org.example.Exceptions.MusicDoesntExistException;
import org.example.Model.Musica.Music;
import org.example.Model.SpotifUM;

import java.util.Map;
import java.util.stream.Collectors;

public class MostGenrePlayed {
    public static String MostGenrePlayed (SpotifUM spotifUM) throws  MusicDoesntExistException{
        Map<String, Music> allMusics = spotifUM.getAllMusics();
        return allMusics.values().stream().collect(Collectors.groupingBy(Music::getGenre, Collectors.summingInt(Music::getViews))
        ).entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey)
                .orElseThrow(()->new MusicDoesntExistException("There is no musics registered"));

    }
}
