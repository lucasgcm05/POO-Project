package org.example.Model.Queries;

import org.example.Exceptions.MusicDoesntExistException;
import org.example.Model.Musica.Album;
import org.example.Model.Musica.Music;
import org.example.Model.SpotifUM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MostArtistPlayed {
    public static String MostArtistPlayed (SpotifUM spotifUM) throws MusicDoesntExistException {
        Map<String, Music> musics= spotifUM.getAllMusics();
        // attaches all the musics in a stream
        return musics.values().stream()
                // creates a Map<String,Integer>, where Artist is the key and the total number of views the value
                .collect(Collectors.groupingBy(Music::getAuthor,Collectors.summingInt(Music::getViews)))
                // calculates the max value
                .entrySet().stream().max(Map.Entry.comparingByValue())
                //gets the key, which is the name of the artist
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new MusicDoesntExistException("There is no musics registered"));
    }
}
