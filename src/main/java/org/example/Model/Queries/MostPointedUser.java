package org.example.Model.Queries;

import org.example.Exceptions.UserDoesntExistException;
import org.example.Model.Musica.Music;
import org.example.Model.SpotifUM;
import org.example.Model.User.User;

import java.util.Map;

public class MostPointedUser {
    public static User MostPointedUser (SpotifUM spotifUM) throws UserDoesntExistException {
        Map<String, User> allUsers = spotifUM.getAllUsers();
        return allUsers.values().stream().max((a, b)-> Double.compare(a.getPoints(), b.getPoints()))
                .orElseThrow(() -> new UserDoesntExistException ("There is no users registered"));
    }
}
