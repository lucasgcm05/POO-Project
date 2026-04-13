package org.example.Model.Musica;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class MultimediaMusic extends Music {
    private static final long serialVersionUID = 1L;

    private String videoLink;

    public MultimediaMusic() {
        super();
        this.videoLink = "";
    }

    public MultimediaMusic(String name, String author, String publisherName, String lyrics,
                           String music, String genre, int duration, boolean isPlaying, int views, String videoLink) {
        super(name, author, publisherName, lyrics, music, genre, duration, isPlaying, views);
        this.videoLink = videoLink;
    }

    public MultimediaMusic(MultimediaMusic anotherMultimediaMusic) {
        super(anotherMultimediaMusic);
        this.videoLink = anotherMultimediaMusic.getVideoLink();
    }

    public String getVideoLink() {return videoLink;}

    public void setVideoLink(String videoLink) {this.videoLink = videoLink;}

    public boolean equals(Object o) {
        if (this == o)
            return true;

        if ((o == null) || (this.getClass() != o.getClass()))
            return false;

        MultimediaMusic music1 = (MultimediaMusic) o;
        return super.equals(music1) && Objects.equals(this.videoLink, music1.getVideoLink());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), videoLink);
    }

    @Override
    public MultimediaMusic clone() {
        return new MultimediaMusic(this);
    }

    public String toString() {
        return "MultimediaMusic{" +
                "name='" + this.getName() + '\'' +
                ", author='" + this.getAuthor() + '\'' +
                ", publisherName='" + this.getPublisherName() + '\'' +
                ", lyrics='" + this.getLyrics() + '\'' +
                ", music=" + this.getMusic() +
                ", genre='" + this.getGenre() + '\'' +
                ", duration=" + this.getDuration() +
                ", videoLink='" + this.getVideoLink() + '\'' +
                '}';
    }
}
