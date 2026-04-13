package org.example.Model.Queries;

import org.example.Exceptions.NoPlayListException;
import org.example.Model.SpotifUM;

public class HowManyPublicPlaylists {
    public static int HowManyPublicPlaylists (SpotifUM spotifUM) {
        return (int)spotifUM.getAllPlaylists().values().stream().filter(a->!a.isPrivate()).count();
    }
}


