package org.example.Controller;

import org.example.Model.Musica.Music;
import org.example.Model.Queries.*;
import org.example.Exceptions.NoPlayListException;
import org.example.Model.SpotifUM;
import org.example.Model.User.User;
import org.example.View.Menu;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class QueriesMenu {
    private SpotifUM model;
    private Scanner sc;


    public QueriesMenu(SpotifUM model, Scanner sc) {
        this.model = model;
        this.sc = sc;
    }

    public void runQueriesMenu() {
        Menu menu = new Menu(new String[] {
                "Música mais reproduzida",
                "Intérprete mais escutado",
                "Utilizador mais ativo num intervalo de tempo",
                "Utilizador mais ativo",
                "Utilizador com mais pontos",
                "Tipo de música mais reproduzida",
                "Número de playlists públicas",
                "Utilizador com mais playlists da sua autoria"
        });

        menu.setHandler(1, this::mostListenedMusic);
        menu.setHandler(2, this::mostListenedArtist);
        menu.setHandler(3, this::mostActiveUserBeetweenDates);
        menu.setHandler(4, this::mostActiveUser);
        menu.setHandler(5, this::highestPointsUser);
        menu.setHandler(6, this::mostListenedGenre);
        menu.setHandler(7, this::howManyPublicPL);
        menu.setHandler(8, this::mostPLCreatedByUser);

        menu.run();
    }

    private void mostListenedMusic() {
        MostStreamedMusic query = new MostStreamedMusic();
        Music mostStreamed = query.mostStreamedMusic(model);

        if (mostStreamed != null) {
            System.out.println("A música mais reproduzida é: " + mostStreamed.getName());
            System.out.println("Autor: " + mostStreamed.getAuthor());
            System.out.println("Visualizações: " + mostStreamed.getViews());
        } else {
            System.out.println("Não há músicas disponíveis no sistema.");
        }
    }

    private void mostListenedArtist() {
        MostArtistPlayed query = new MostArtistPlayed();
        String artist = query.MostArtistPlayed(model);

        if (artist != null) {
            System.out.println("O artista mais escutado é: " + artist);
        } else {
            System.out.println("Não há músicas disponíveis no sistema.");
        }
    }

    private void mostActiveUserBeetweenDates() {
        System.out.print("Data início (yyyy-MM-ddTHH:mm): ");
        String startInput = sc.nextLine();
        System.out.print("Data fim (yyyy-MM-ddTHH:mm): ");
        String endInput = sc.nextLine();

        try {
            LocalDateTime start = LocalDateTime.parse(startInput);
            LocalDateTime end = LocalDateTime.parse(endInput);

            User mostActive = MostMusicsPlayedByUser.MostMusicsPlayedByUserWithTime(start, end, model);
            if (mostActive != null) {
                System.out.println("Utilizador mais ativo entre as datas:");
                System.out.println("Nome: " + mostActive.getName());
            } else {
                System.out.println("Nenhum utilizador ativo nesse intervalo.");
            }
        } catch (DateTimeParseException e) {
            System.out.println("Formato de data inválido. Use o formato yyyy-MM-ddTHH:mm");
        }
    }

    private void mostActiveUser() {
        User user = MostMusicsPlayedByUser.usuarioMaisAtivo(model);
        if (user != null) {
            System.out.println("Utilizador mais ativo: " + user.getName());
            System.out.println("Total de músicas ouvidas: " + user.getHistory().size());
        } else {
            System.out.println("Nenhum utilizador com histórico.");
        }
    }

    private void highestPointsUser() {
        MostPointedUser query = new MostPointedUser();
        User user = query.MostPointedUser(model);

        if (user != null) {
            System.out.println("Utilizador com mais pontos: " + user.getName());
            System.out.printf("Pontos: %.2f\n", user.getPoints());
        } else {
            System.out.println("Nenhum utilizador registado.");
        }
    }

    private void mostListenedGenre() {
        String genre = MostGenrePlayed.MostGenrePlayed(model);
        if (genre != null) {
            System.out.println("Género mais reproduzido: " + genre);
        } else {
            System.out.println("Nenhuma música reproduzida.");
        }
    }

    public void howManyPublicPL() {
        int publicPlaylistsCount = HowManyPublicPlaylists.HowManyPublicPlaylists(model);
        System.out.println("Número de playlists públicas: " + publicPlaylistsCount);
    }

    public void mostPLCreatedByUser() {
        try {
        User user = MostPlaylistsCreatedByUser.MostPlaylistsCreatedByUser(model);
        System.out.println("Utilizador com mais playlists criadas: " + user.getName());
        } catch (NoPlayListException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }


}
