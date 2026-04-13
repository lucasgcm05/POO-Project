package org.example.Model.Playlist;

import org.example.Model.Musica.Album;
import org.example.Model.Musica.Music;
import org.example.Model.Playable;
import org.example.Model.User.User;

import java.io.Serializable;
import java.util.*;

public abstract class PlayList implements Playable, Serializable {
    private List<Music> musics;
    private String name;
    private User owner;
    private boolean isPaused = true;
    private boolean isPrivate = false;
    private int currentIndex = 0;
    private Music currentMusic;
    private boolean isShuffle = false;


    public List<Music> getMusics() {
        return musics;
    }

    public void setMusics(List<Music> musics) {
        this.musics = musics;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public boolean isShuffle() {
        return isShuffle;
    }

    public void setShuffle(boolean shuffleMode) {
        this.isShuffle = shuffleMode;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public PlayList () {
        this.musics =  new ArrayList<>();
        this.name = "";
        this.owner = null;
        this.currentIndex = 0;
        this.currentMusic = null;
        this.isShuffle = false;

    }

    public PlayList(List<Music> musics, String name, User owner, boolean privacy) {
        this.musics = musics;
        this.name = name;
        this.owner = owner;
        this.isPrivate = privacy;
        this.currentIndex = 0;
        if (!this.musics.isEmpty()) {
            this.currentMusic = this.musics.get(currentIndex);
        } else {
            this.currentMusic = null;
        }
        this.isShuffle = false;
    }

    public PlayList (PlayList a) {
        this.musics = a.getMusics();
        this.name = a.getName();
        this.owner = a.getOwner();
        this.isPrivate = a.isPrivate();
        this.currentIndex = a.getCurrentIndex();
        if (this.musics.isEmpty()) {
            this.currentMusic = null;
        } else {
            this.currentMusic = this.musics.get(currentIndex);
        }
        this.isShuffle = a.isShuffle();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if ((o == null) || (this.getClass() != o.getClass()))
            return false;

        PlayList playlist = (PlayList) o;
        return this.name.equals(playlist.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, owner, musics);
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
            this.currentMusic = this.musics.get(currentIndex);
        } else {
            this.currentMusic = null;
        }
        this.isShuffle = false;
    }

    public boolean musicExists(String name) {
        return this.musics.stream()
                .anyMatch(m -> m.getName().equalsIgnoreCase(name));
    }
}
