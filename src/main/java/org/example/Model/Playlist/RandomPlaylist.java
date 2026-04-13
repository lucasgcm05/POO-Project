package org.example.Model.Playlist;

import org.example.Model.Musica.Music;
import org.example.Model.SpotifUM;
import org.example.Model.User.User;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class RandomPlaylist extends PlayList implements Serializable {

    public RandomPlaylist() {
        super ();
    }

    public RandomPlaylist(List<Music> musics, String name, User owner, boolean privacy) {
        super(musics, name, owner, privacy);
    }

    public RandomPlaylist(PlayList a) {
        super(a);
    }

    public static RandomPlaylist GenerateRandomPlaylist (SpotifUM spotifUM) {
        User defaultUser = new User(
                "SpotifUM",        // name
                "",                // email irrelevante
                "",                // address irrelevante
                0.0,               // pontos inicial
                null,              // plano padrão (nenhum)
                new ArrayList<>()  // histórico vazio
        );
        List<Music> musics = spotifUM.getAllMusics().values().stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            Collections.shuffle(list);
                            return list.stream();
                        }
                ))
                .limit(50)
                .toList();
        return new RandomPlaylist(musics, "Randomly Generated", defaultUser, false);
    }
}
