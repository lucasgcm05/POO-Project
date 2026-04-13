package org.example.Model.Queries;

import org.example.Exceptions.MusicDoesntExistException;
import org.example.Model.Musica.Music;
import org.example.Model.SpotifUM;

import java.util.Map;
import java.util.TreeMap;

public class MostStreamedMusic {
    public static Music mostStreamedMusic(SpotifUM spotifUM) throws MusicDoesntExistException{
        Map<String,Music> allMusics = spotifUM.getAllMusics();
        return allMusics.values().stream()
                .max((m1,m2) -> Integer.compare(m1.getViews(), m2.getViews()))
                .orElseThrow(()-> new MusicDoesntExistException("There is no Musics registered"));
    }
}
