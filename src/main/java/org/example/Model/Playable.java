package org.example.Model;

import org.example.Model.Musica.Music;
import org.example.Model.User.User;

import java.util.List;

public interface Playable {
    public void shuffle (User user);

    public Music next(User user);

    /**
     * Retrocede para a música anterior (apenas em modo sequencial).
     */
    public Music previous(User user);
    public Music play(User user);
}
