package org.example.Model.Musica;

import org.example.Exceptions.MusicAlreadyExistsException;
import org.example.Exceptions.MusicDoesntExistException;
import org.example.Model.Playable;
import org.example.Model.User.User;

import java.io.Serializable;
import java.util.*;

public class Album implements Serializable, Playable {
    private static final long serialVersionUID = 1L;

    //private final int id;
    private String name;
    private String author;
    private int year;
    private List<Music> musics;
    private int currentIndex = 0;
    private Music currentMusic;
    private boolean isShuffle = false;

    //private static int albumCount = 0;

    public Album() {
        //this.id = ++albumCount;
        this.name = "";
        this.author = "";
        this.year = 0;
        this.musics = new ArrayList<>();
        this.currentIndex = 0;
        this.currentMusic = null;
        this.isShuffle = false;
    }

    public Album(String name, String author, int year, List<Music> musics) {
        this.name = name;
        this.author = author;
        this.year = year;
        this.musics = new ArrayList<>();

        for (Music music : musics) {
            this.musics.add(music.clone()); // Clona cada música para manter a composição
        }

        this.currentIndex = 0;
        if (!this.musics.isEmpty()) {
            this.currentMusic = this.musics.get(currentIndex); // usar a lista clonada que acabaste de construir
        } else {
            this.currentMusic = null;
        }
        this.isShuffle = false;
    }

    public Album(Album anotherAlbum) {
        //this.id = anotherAlbum.getId();
        this.name = anotherAlbum.getName();
        this.author = anotherAlbum.getAuthor();
        this.year = anotherAlbum.getYear();
        this.musics = new ArrayList<>();
        for (Music music : anotherAlbum.getMusics()) {
            this.musics.add(music.clone()); // Clona cada música para manter a composição
        }
        this.currentIndex = anotherAlbum.getCurrentIndex();
        if (this.musics.isEmpty()) {
            this.currentMusic = null;
        } else {
            this.currentMusic = this.musics.get(currentIndex);
        }
        this.isShuffle = anotherAlbum.isShuffle();
    }

    //public int getId() {return id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Music> getMusics() {
        List<Music> copy = new ArrayList<>();
        for (Music music : this.musics) {
            copy.add(music.clone());
        }
        return copy;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public Music getCurrentMusic() {
        return currentMusic != null ? currentMusic.clone() : null;
    }

    public void setCurrentMusic(Music currentMusic) {
        this.currentMusic = currentMusic != null ? currentMusic.clone() : null;
    }

    public boolean isShuffle() {
        return isShuffle;
    }

    public void setShuffle(boolean shuffle) {
        isShuffle = shuffle;
    }

    public Music getMusic(String name) {
        for (Music music : this.musics) {
            if (music.getName().equals(name)) {
                return music.clone(); // Retorna uma cópia para manter a composição
            }
        }
        return null; // Retorna null se nenhuma música com o nome especificado for encontrada
    }

    public Album clone() {
        return new Album(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if ((o == null) || (this.getClass() != o.getClass()))
            return false;

        Album album = (Album) o;
        return this.name.equals(album.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, author, year, musics);
    }

    @Override
    public String toString() {
        return "Album{" +
                //"id=" + id +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", year=" + year +
                ", musics=" + musics +
                '}';
    }

    public void addMusic(Music music) throws MusicAlreadyExistsException {
        if (musicExists(music.getName())) {
            throw new MusicAlreadyExistsException(music.getName());
        }

        this.musics.add(music.clone());
    }

    public void removeMusic(String name) throws MusicDoesntExistException {
        if (!musicExists(name)) {
            throw new MusicDoesntExistException(name);
        }

        this.musics.removeIf(m -> m.getName().equalsIgnoreCase(name));
    }

    public boolean musicExists(String name) {
        return this.musics.stream()
                .anyMatch(m -> m.getName().equalsIgnoreCase(name));
    }


    @Override
    public void shuffle(User user) {
        if (!user.getPlan().canCreatePlaylists()) {
            setShuffle(true);
        }
        else {
            setShuffle(!isShuffle);
        }
    }

    @Override
    public Music next(User user) {
        if (!isShuffle) {
            if (this.getCurrentIndex() < this.musics.size() - 1) {
                setCurrentIndex(this.getCurrentIndex() + 1);
            }
            else {
                setCurrentIndex(0); // restart
            }
        }
        else {
            Random rand = new Random();
            int newIndex;
            do {
                newIndex = rand.nextInt(this.musics.size()); // Gera um índice aleatório dentro dos limites
            } while (newIndex == this.getCurrentIndex()); // Garante que o índice seja diferente do atual
            setCurrentIndex(newIndex);
        }

        Music musicPlaying = this.musics.get(this.getCurrentIndex());
        musicPlaying.play();
        user.playMusic(musicPlaying);
        return musicPlaying.clone();
    }

    @Override
    public Music previous(User user) {
        if (!isShuffle) {
            if (this.getCurrentIndex() > 0) {
                this.setCurrentIndex(this.getCurrentIndex() - 1);
            }
            else {
                this.setCurrentIndex(this.musics.size() - 1);
            }
        }

        Music musicPlaying = this.musics.get(this.getCurrentIndex());
        musicPlaying.play();
        user.playMusic(musicPlaying);
        return musicPlaying.clone();
    }

    @Override
    public Music play(User user) {
        Music musicPlaying = this.musics.get(this.getCurrentIndex());
        user.playMusic(musicPlaying);
        musicPlaying.play();
        return musicPlaying.clone();
    }

    public void resetState() {
        this.currentIndex = 0;
        if (!this.musics.isEmpty()) {
            this.currentMusic = this.musics.get(currentIndex); // usar a lista clonada que acabaste de construir
        } else {
            this.currentMusic = null;
        }
        this.isShuffle = false; // Reinicia o shuffle também, se necessário
    }
}
