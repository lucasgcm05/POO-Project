package org.example.Model.User;

import java.io.Serializable;

public interface Plan extends Serializable {
    double calculatePoints(double currentPoints);
    /** Pode criar playlists personalizadas? */
    boolean canCreatePlaylists();

    /** Pode gerar listas de favoritos com base nas preferências? */
    boolean canGenerateFavorites();

    /** Pode gerar playlists por género e duração? */
    boolean canGenerateGenreTimeLimited();
}
