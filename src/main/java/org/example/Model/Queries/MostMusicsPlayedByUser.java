package org.example.Model.Queries;

import org.example.Exceptions.UserDoesntExistException;
import org.example.Model.SpotifUM;
import org.example.Model.User.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MostMusicsPlayedByUser {
    public static User usuarioMaisAtivo(SpotifUM spotifUM) throws UserDoesntExistException{
        Map<String, User> allUsers = spotifUM.getAllUsers();
        return allUsers.entrySet().stream()
                .max(Comparator.comparingInt(e-> e.getValue().getHistory().size()))
                .map(Map.Entry::getValue)
                .orElseThrow(() -> new UserDoesntExistException("There is no Users registered"));
    }

    public static User MostMusicsPlayedByUserWithTime(LocalDateTime begin, LocalDateTime end, SpotifUM spotifUM) {
        Map<String, User> allUsers = spotifUM.getAllUsers();
        return allUsers.entrySet().stream()
                .max(Comparator.comparingLong(e -> e.getValue().getHistory().stream()
                        .filter(p -> !p.getTimestamp().isBefore(begin) && !p.getTimestamp().isAfter(end))
                        .count()
                ))
                .map(Map.Entry::getValue)
                .orElseThrow(()-> new UserDoesntExistException("There is no Users registered"));

    }
}
