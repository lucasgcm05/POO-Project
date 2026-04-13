package org.example.Model.User.History;

import org.example.Model.Musica.Music;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class History implements Serializable {
    private static final long serialVersionUID = 1L;

    private Music music;
    private LocalDateTime timestamp;

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public History(Music music, LocalDateTime timestamp) {
        this.music = music;
        this.timestamp = timestamp;
    }

    public History () {
        this.music = new Music();
        this.timestamp = LocalDateTime.MIN;
    }

    public History (History a) {
        this.music=a.getMusic();
        this.timestamp=a.getTimestamp();
    }

}
