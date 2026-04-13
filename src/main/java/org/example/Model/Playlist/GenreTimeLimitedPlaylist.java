package org.example.Model.Playlist;

import org.example.Model.Musica.Album;
import org.example.Model.Musica.Music;
import org.example.Model.Playable;
import org.example.Model.User.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenreTimeLimitedPlaylist extends PlayList implements Serializable {
    private final String genre;
    private final int seconds;


    public String getGenre() {
        return genre;
    }

    public int getSeconds() {
        return seconds;
    }

    public GenreTimeLimitedPlaylist(User creator, List<Music> available, String genre, boolean privacy, int maxSeconds, String name) {
        super (available, name, creator, privacy);
        this.genre = genre;
        this.seconds = maxSeconds;

    }

    public static GenreTimeLimitedPlaylist create(User creator, List<Music> available, String genre,boolean privacy, int maxSeconds, String name, Map<String, Album> albums) {
        if (!creator.getPlan().canGenerateGenreTimeLimited()) {
            throw new UnsupportedOperationException(
                    "Somente utilizadores premium podem gerar playlists por género e tempo.");
        }
        GenreTimeLimitedPlaylist aux = new GenreTimeLimitedPlaylist(creator, available, genre, privacy,maxSeconds, name);
        final int[] totalDuration = {0};
        albums.values().stream()
                .flatMap(album -> album.getMusics().stream()) // Itera sobre a lista de músicas
                .filter(music -> music.getGenre().equalsIgnoreCase(genre)) // Filtra pelo gênero
                .forEach(music -> {
                    int dur = music.getDuration();
                    if (totalDuration[0] + dur <= maxSeconds) {
                        aux.getMusics().add(music.clone()); // Adiciona uma cópia da música para manter a composição
                        totalDuration[0] += dur;
                    }
                });
        return aux;
    }
}
