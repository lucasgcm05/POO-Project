package org.example.Model.Musica;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Music implements Serializable {
    private static final long serialVersionUID = 1L;

    //private final int id;
    private String name;
    private String author;
    private String publisherName;
    private String lyrics;
    private String music;
    private String genre;
    private int views;
    private int duration; // in seconds
    private boolean isPlaying;

    //private static int countMusic = 0;

    // Empty constructor
    public Music() {
        //this.id = ++countMusic;
        this.name = "";
        this.author = "";
        this.publisherName = "";
        this.lyrics = "";
        this.music = "";
        this.genre = "";
        this.duration = 0;
        this.isPlaying = false;
        this.views = 0;
    }

    // Parameterized constructor
    public Music(String name, String author, String publisherName, String lyrics,
                 String music, String genre, int duration, boolean isPlaying, int views) {
        //this.id = ++countMusic;
        this.name = name;
        this.author = author;
        this.publisherName = publisherName;
        this.lyrics = lyrics;
        this.music = music;
        this.genre = genre;
        this.duration = duration;
        this.isPlaying = isPlaying;
        this.views = views;
    }

    // what constructor ?
    public Music(Music anotherMusic) {
        //this.id = anotherMusic.getId();
        this.name = anotherMusic.getName();
        this.author = anotherMusic.getAuthor();
        this.publisherName = anotherMusic.getPublisherName();
        this.lyrics = anotherMusic.getLyrics();
        this.music = anotherMusic.getMusic();
        this.genre = anotherMusic.getGenre();
        this.duration = anotherMusic.getDuration();
        this.isPlaying = anotherMusic.isPlaying();
        this.views = anotherMusic.getViews();
    }

    //public int getId() {return id;}
    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisherName() {
        return this.publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getLyrics() {
        return this.lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getMusic() {
        return this.music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Music clone() {return new Music(this);}

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if ((o == null) || (this.getClass() != o.getClass()))
            return false;

        Music music1 = (Music) o;
        return Objects.equals(this.name, music1.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, author, publisherName, lyrics, music, genre, duration);
    }

    @Override
    public String toString() {
        return "Music{" +
                //"id=" + id +
                "name='" + this.getName() + '\'' +
                ", author='" + this.getAuthor() + '\'' +
                ", publisherName='" + this.getPublisherName() + '\'' +
                ", lyrics='" + this.getLyrics() + '\'' +
                ", music=" + this.getMusic() +
                ", genre='" + this.getGenre() + '\'' +
                ", duration=" + this.getDuration() +
                ", views=" + this.getViews() +
                '}';
    }

    public void pause() {
        this.isPlaying = false;
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public void play() {
        this.isPlaying = true;
        this.views++;
    }

}   