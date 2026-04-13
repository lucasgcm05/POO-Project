package org.example.Model.Queries;

import org.example.Exceptions.NoPlayListException;
import org.example.Model.Playlist.PlayList;
import org.example.Model.SpotifUM;
import org.example.Model.User.User;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class MostPlaylistsCreatedByUser {
    public static User MostPlaylistsCreatedByUser (SpotifUM spotifUM) throws NoPlayListException{
        return spotifUM.getAllPlaylists().values().stream().collect(Collectors.groupingBy(PlayList::getOwner, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new NoPlayListException("There is no PlayLists available"));
    }
}
