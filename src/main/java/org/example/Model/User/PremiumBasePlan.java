package org.example.Model.User;

import java.io.Serializable;

public class PremiumBasePlan implements Plan, Serializable {
    @Override
    public double calculatePoints(double currentPoints) {
        return 10;
    }
    @Override public boolean canCreatePlaylists() { return true; }

    @Override public boolean canGenerateFavorites() { return false; }
    @Override public boolean canGenerateGenreTimeLimited() { return false; }
}
