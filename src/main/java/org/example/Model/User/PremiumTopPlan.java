package org.example.Model.User;

import java.io.Serializable;

public class PremiumTopPlan implements Plan, Serializable {
    private static final long serialVersionUID = 1L;
    @Override
    public double calculatePoints(double currentPoints) {
        return currentPoints*0.025;
    }
    @Override public boolean canCreatePlaylists() { return true; }
    @Override public boolean canGenerateFavorites() { return true; }
    @Override public boolean canGenerateGenreTimeLimited() { return true; }

}
 