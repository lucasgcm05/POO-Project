package org.example.Model.Playlist;

import org.example.Exceptions.MusicAlreadyExistsException;
import org.example.Exceptions.MusicDoesntExistException;
import org.example.Exceptions.OutOfBoundsPlaylistException;
import org.example.Model.Musica.Music;
import org.example.Model.User.User;

import java.io.Serializable;
import java.util.List;

public class UserCreatedPlaylist extends PlayList implements Serializable {

    public UserCreatedPlaylist(User creator, String name, List<Music> musics, boolean privacy) {
        super(musics, name, creator, privacy);
    }

    public static UserCreatedPlaylist create(User creator, String name, List<Music> musics, boolean privacy) {
        if (!creator.getPlan().canCreatePlaylists()) {
            throw new UnsupportedOperationException(
                    "Somente utilizadores premium podem criar playlists.");
        } else return new UserCreatedPlaylist(creator, name, musics, privacy);
    }

    public void addMusic(Music music) throws MusicAlreadyExistsException {
        if (musicExists(music.getName())) {
            throw new MusicAlreadyExistsException(music.getName());
        }

        this.getMusics().add(music.clone());
    }

    public void removeMusic(String name) throws MusicDoesntExistException {
        if (!musicExists(name)) {
            throw new MusicDoesntExistException(name);
        }

        this.getMusics().removeIf(m -> m.getName().equalsIgnoreCase(name));
    }


}