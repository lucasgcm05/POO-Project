package org.example.Model.User;

import java.io.Serializable;

public class FreePlan implements Plan, Serializable {
    @Override
    public double calculatePoints(double currentPoints) {
        return 5;
    }
    @Override public boolean canCreatePlaylists() { return false; }

    @Override public boolean canGenerateFavorites() { return false; }
    @Override public boolean canGenerateGenreTimeLimited() { return false; }
}
