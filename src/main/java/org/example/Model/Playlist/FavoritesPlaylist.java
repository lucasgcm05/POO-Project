package org.example.Model.Playlist;

import org.example.Model.Musica.Music;
import org.example.Model.Playable;
import org.example.Model.SpotifUM;
import org.example.Model.User.History.History;
import org.example.Model.User.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FavoritesPlaylist extends PlayList implements Serializable {
    private static int id = 0;

    public FavoritesPlaylist(User owner, List<Music> favorites, String name, boolean privacy) {
        super(favorites, name, owner, privacy);
    }

    public static int getId() {
        return id;
    }

    public FavoritesPlaylist create(User owner, List<Music> favorites, String name, boolean privacy) {
        if (!owner.getPlan().canGenerateFavorites()) {
            throw new UnsupportedOperationException(
                    "Somente utilizadores PremiumTop podem criar playlist de favoritos.");
        }
        return new FavoritesPlaylist(owner, favorites, name, privacy);
    }

    public static FavoritesPlaylist GenerateFavPlayList (SpotifUM spotifUM, User owner) {
        int total = owner.getHistory().size();
        List <Music> recentMusics = owner.getHistory().stream().map(History::getMusic)
                .toList();
        Map<Music, Long> playCounts = recentMusics.stream()
                .collect(Collectors.groupingBy(
                        m -> m,
                        Collectors.counting()
                ));
        List<Music> favMusics = playCounts.entrySet().stream()
                .sorted(Map.Entry.<Music, Long>comparingByValue(Comparator.reverseOrder())) // ordena de forma decrescente
                .map(Map.Entry::getKey)
                .limit(50)
                .toList();

        return new FavoritesPlaylist(owner, favMusics, owner.getName()+"Favorite Musics" + id++, true);
    }
}

/*
* USERS BASE -> OUVIR PLAYLIST ALEATORIA
* USER PREMIUM BASE -> OUVIR PLAYLIST ALEATORIA, CRIAR PLAYLIST, REMOVER, EDITAR, OUVIR PLAYLIST CRIADA,
* USER PREMIUM TOP -> TUDO DO DE CIMA + FAVORITES E GENRE TIME
* */