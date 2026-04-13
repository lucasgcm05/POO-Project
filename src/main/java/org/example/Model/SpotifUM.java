package org.example.Model;

import org.example.Exceptions.*;
import org.example.Model.Musica.Album;
import org.example.Model.Musica.Music;
import org.example.Model.Playlist.PlayList;
import org.example.Model.User.User;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SpotifUM implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Album> albuns;
    private Map<String, User> users;
    private Map<String , PlayList> playlists;

    public static SpotifUM readFromFile(String fileName) throws IOException, ClassNotFoundException {
        File file = new File(fileName);

        if (!file.exists()) {
            throw new FileNotFoundException("O ficheiro " + fileName + " não foi encontrado.");
        }

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (SpotifUM) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IOException("Erro ao ler o ficheiro: " + e.getMessage(), e);
        }
    }

    public static void writeToFile(SpotifUM spotifUM, String fileName) throws IOException {
        File file = new File(fileName);

        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(spotifUM);
            oos.flush();
        } catch (IOException e) {
            throw new IOException("Erro ao escrever no ficheiro: " + e.getMessage(), e);
        }
    }

    public SpotifUM() {
        albuns = new HashMap<>();
        users = new HashMap<>();
        playlists = new HashMap<>();
    }

    public void addAlbum(Album album) throws AlbumAlreadyExistsException {

        if (!albuns.containsValue(album)) {
            albuns.put(album.getName(), album.clone());
        }
        else throw new AlbumAlreadyExistsException(album.getName());
    }

    public Album getAlbum(String albumName) {
        Album a = albuns.get(albumName);
        if (a == null) {
            throw new AlbumDoesntExistException(albumName);
        }
        return a;
    }

    public void removeAlbum(String albumName) throws AlbumDoesntExistException {
        if(!albuns.containsKey(albumName)) {
            throw new AlbumDoesntExistException(albumName);
        }
        albuns.remove(albumName);
    }

    public boolean albumExists(String albumName) {
        return albuns.containsKey(albumName);
    }

    public Map<String, Music> getAllMusics() {
        return this.albuns.values().stream()
                .flatMap(album -> album.getMusics().stream()) // Itera sobre a lista de músicas
                .collect(Collectors.toMap(Music::getName, music -> music, (existing, replacement) -> existing));
    }

    public Music getMusic(String musicName) {
        return getAllMusics().get(musicName);
    }

    public Map<String, Music> getMusicsFromAlbum(String albumName) {
        Album album = getAlbum(albumName); // Obtém o álbum pelo nome
        return album.getMusics().stream() // Itera sobre a lista de músicas
                .collect(Collectors.toMap(Music::getName, music -> music, (existing, replacement) -> existing));
    }

    public Map<String, Album> getAlbumByName(String name) {
        return this.albuns.values().stream().filter(album -> album.getName().equals(name))
                .collect(Collectors.toMap(Album::getName, album -> album.clone()));
    }

    public Map<String, Album> getAlbumByAuthor(String author) {
        return this.albuns.values().stream().filter(album -> album.getAuthor().equals(author))
                .collect(Collectors.toMap(Album::getName, album -> album.clone()));
    }

    public Map<String, Album> getAllAlbums() {
        return new HashMap<>(this.albuns); // Acho que tem de ser assim se nao o estado nao é atualizado, REVER!!
        /*Map<String, Album> copy = new HashMap<>();
        for (Map.Entry<String, Album> entry : this.albuns.entrySet()) {
            copy.put(entry.getValue().getName(), entry.getValue().clone()); // ver se isto ta certo
        }
        return copy;*/
    }

    public void addUser(User user) throws UserAlreadyExistsException {
        if (!users.containsValue(user)) {
            users.put(user.getName(), user.clone());
        }
        else throw new UserAlreadyExistsException(user.getName());
    }
    public User getUser(String username) throws UserDoesntExistException {
        User u = users.get(username);
        if (u != null) {
            return u;
        }
        else throw new UserDoesntExistException(username);
    }

    public void removeUser(String username) throws UserDoesntExistException {
        if (!users.containsKey(username))
            throw new UserDoesntExistException(username);
        users.remove(username);
    }


    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    public Map<String, User> getAllUsers () {
        Map<String, User> users1 = new HashMap<>();
        for (Map.Entry<String, User> entry : this.users.entrySet()) {
            users1.put(entry.getKey(), entry.getValue().clone());
        }
        return users1;
    }

    public void addPlaylist(PlayList playlist) throws PlaylistAlreadyExistsException {
        if (!playlists.containsValue(playlist)) {
            playlists.put(playlist.getName(), playlist);
        }
        else throw new PlaylistAlreadyExistsException(playlist.getName());
    }

    public PlayList getPlaylist(String playlistName) throws PlaylistDoesntExistException {
        if(!playlists.containsKey(playlistName)) {
            throw new PlaylistDoesntExistException(playlistName);
        }

        return playlists.get(playlistName);
    }

    public void removePlaylist(String playlistName) throws PlaylistDoesntExistException {
        if(!playlists.containsKey(playlistName)) {
            throw new PlaylistDoesntExistException(playlistName);
        }

        playlists.remove(playlistName);
    }
    public boolean playlistExists(String playlistName) {
        return playlists.containsKey(playlistName);
    }

    public Map<String, PlayList> getAllPlaylists() {
        return new HashMap<>(this.playlists);
    }
}
