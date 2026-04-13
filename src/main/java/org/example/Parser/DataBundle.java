package org.example.Parser;

import org.example.Model.Musica.Album;
import org.example.Model.Playlist.PlayList;
import org.example.Model.User.User;

import java.util.List;

public class DataBundle {
    private List<User> users;
    private List<Album> albums;
    private List<PlayList> playlists;

    public DataBundle(List<User> users, List<Album> albums, List<PlayList> playlists) {
        this.users = users;
        this.albums = albums;
        this.playlists = playlists;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public List<PlayList> getPlaylists() {
        return playlists;
    }
}

