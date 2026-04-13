package org.example.Controller;

import org.example.Exceptions.*;
import org.example.Model.Musica.Album;
import org.example.Model.Musica.Music;
import org.example.Model.Playlist.PlayList;
import org.example.Model.Queries.*;
import org.example.Model.SpotifUM;
import org.example.Model.User.*;
import org.example.Parser.DataBundle;
import org.example.Parser.DataLoader;
import org.example.Parser.DataSaver;
import org.example.View.Menu;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AdminMenu {
    private SpotifUM model;
    private Scanner sc;

    public AdminMenu(SpotifUM model, Scanner sc) {
        this.model = model;
        this.sc = sc;
    }

    public void runAdminMenu() {
        Menu menu = new Menu(new String[]{
                "Descarregar conteúdos de um ficheiro",
                "Adicionar álbum",
                "Remover álbum",
                "Editar álbum",
                "Adicionar user",
                "Remover user",
                "Aceder a resultados das queries",
                "Guardar estado atual em ficheiro JSON",
                "Carregar sistema de ficheiro .ser",
                "Guardar sistema de ficheiro .ser"
        });

        menu.setHandler(1, this::contentsFromFile);
        menu.setHandler(2, this::addAlbum);
        menu.setHandler(3, this::removeAlbum);
        menu.setHandler(4, this::editAlbum);
        menu.setHandler(5, this::addUser);
        menu.setHandler(6, this::removeUser);
        menu.setHandler(7, this::accessQueries);
        menu.setHandler(8, this::saveCurrentStateJSON);
        menu.setHandler(9, this::deserializeFromFile);
        menu.setHandler(10, this::serializeToFile);

        menu.run();
    }

    private void editAlbum() {


        System.out.print("Insira o nome do álbum que deseja editar: ");
        String albumName = sc.nextLine();

        try {
            Album album = model.getAlbum(albumName);

            Menu menu = new Menu(new String[]{
                    "Adicionar música ao álbum",
                    "Remover música do álbum"
            });

            menu.setHandler(1, () -> addMusicToAlbum(album));
            menu.setHandler(2, () -> removeMusicFromAlbum(album));

            menu.run();

        } catch (AlbumDoesntExistException e) {
            System.out.println("Álbum não encontrado: " + e.getMessage());
        }
    }

    private void addMusicToAlbum(Album album) {
        System.out.print("Nome da música: ");
        String musicName = sc.nextLine();

        System.out.print("Autor da música: ");
        String musicAuthor = sc.nextLine();

        System.out.print("Editora da música: ");
        String publisherName = sc.nextLine();

        System.out.print("Letra da música: ");
        String lyrics = sc.nextLine();

        System.out.print("Instrumentos usados na música: ");
        String music = sc.nextLine();

        System.out.print("Género da música: ");
        String genre = sc.nextLine();

        System.out.print("Duração da música (em segundos): ");
        int duration = -1;
        while (duration < 0) {
            try {
                duration = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Duração inválida. Por favor, insira um número válido.");
            }
        }

        Music newMusic = new Music(musicName, musicAuthor, publisherName, lyrics, music, genre, duration, false, 0);
        album.addMusic(newMusic);
        System.out.println("Música adicionada com sucesso ao álbum!");
    }

    private void removeMusicFromAlbum(Album album) {
        System.out.print("Insira o nome da música que deseja remover: ");
        String musicName = sc.nextLine();

        album.removeMusic(musicName);
        System.out.println("Música removida com sucesso do álbum!");
    }

    private void contentsFromFile() {
        System.out.print("Insira o caminho do ficheiro JSON: ");
        String filePath = sc.nextLine();

        try {
            // Carrega os dados do JSON
            DataBundle data = DataLoader.loadData(filePath);

            // Adiciona os usuários ao sistema
            data.getUsers().forEach(user -> {
                try {
                    model.addUser(user);
                } catch (UserAlreadyExistsException e) {
                    System.out.println("Utilizador já existente: " + user.getName());
                }
            });

            // Adiciona os álbuns ao sistema
            data.getAlbums().forEach(album -> {
                try {
                    model.addAlbum(album);
                } catch (AlbumAlreadyExistsException e) {
                    System.out.println("Álbum já existente: " + album.getName());
                }
            });

            model.getAllAlbums().values().forEach(Album::resetState);

            data.getPlaylists().forEach(playlist -> {
                try {
                    model.addPlaylist(playlist);
                } catch (PlaylistAlreadyExistsException e) {
                    System.out.println("Playlist já existente: " + playlist.getName());
                }
            });

            model.getAllPlaylists().values().forEach(PlayList::resetState);

            System.out.println("Dados carregados com sucesso a partir do ficheiro JSON.");
        } catch (RuntimeException e) {
            System.out.println("Erro ao carregar os dados: " + e.getMessage());
        }
    }

    private void addAlbum() {
        System.out.print("Insira o nome do álbum: ");
        String albumName = sc.nextLine();

        System.out.print("Insira o autor do álbum: ");
        String albumAuthor = sc.nextLine();

        System.out.print("Insira o ano de lançamento do álbum: ");
        int albumYear = -1;
        while (albumYear < 0) {
            try {
                albumYear = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ano inválido. Por favor, insira um número válido.");
            }
        }

        List<Music> musics = new ArrayList<>();
        System.out.println("Agora, insira as músicas do álbum. Digite 'sair' para terminar.");

        while (true) {
            System.out.print("Nome da música (ou 'sair' para terminar): ");
            String musicName = sc.nextLine();
            if (musicName.equalsIgnoreCase("sair")) break;

            System.out.print("Autor da música: ");
            String musicAuthor = sc.nextLine();

            System.out.print("Editora da música: ");
            String publisherName = sc.nextLine();

            System.out.print("Letra da música: ");
            String lyrics = sc.nextLine();

            System.out.print("Instrumentos usados na música: ");
            String music = sc.nextLine();

            System.out.print("Género da música: ");
            String genre = sc.nextLine();

            System.out.print("Duração da música (em segundos): ");
            int duration = -1;
            while (duration < 0) {
                try {
                    duration = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Duração inválida. Por favor, insira um número válido.");
                }
            }

            musics.add(new Music(musicName, musicAuthor, publisherName, lyrics, music, genre, duration, false, 0));
            System.out.println("Música adicionada ao álbum.");
        }

        try {
            Album album = new Album(albumName, albumAuthor, albumYear, musics);
            model.addAlbum(album);
            System.out.println("Álbum adicionado com sucesso!");
        } catch (AlbumAlreadyExistsException e) {
            System.out.println("O álbum já existe: " + e.getMessage());
        }
    }

    private void removeAlbum() {
        System.out.println("Insira o nome do álbum que pretende remover:");
        String albumName = sc.nextLine();

        try {
            model.removeAlbum(albumName);
            System.out.println("Álbum removido com sucesso!");
        } catch (AlbumDoesntExistException e) {
            System.out.println("Esse álbum não existe no SpotifUM!");
        }
    }

    private void addUser() {
        System.out.print("Insira o nome do usuário: ");
        String username = sc.nextLine();

        System.out.print("Insira o email do usuário: ");
        String email = sc.nextLine();

        System.out.print("Insira o endereço do usuário: ");
        String address = sc.nextLine();

        System.out.println("Escolha o plano do usuário:");
        System.out.println("1 - FreePlan");
        System.out.println("2 - PremiumBasePlan");
        System.out.println("3 - PremiumTopPlan");

        int opcaoPlano = -1;
        while (opcaoPlano < 1 || opcaoPlano > 3) {
            System.out.print("Plano (1 a 3): ");
            try {
                opcaoPlano = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida. Por favor, insira um número válido.");
            }
        }

        Plan plano;
        switch (opcaoPlano) {
            case 1 -> plano = new FreePlan();
            case 2 -> plano = new PremiumBasePlan();
            case 3 -> plano = new PremiumTopPlan();
            default -> throw new IllegalStateException("Plano inválido.");
        }

        User novoUser = new User(username, email, address, 0.0, plano, new ArrayList<>());

        try {
            model.addUser(novoUser);
            System.out.println("Usuário adicionado com sucesso!");
        } catch (UserAlreadyExistsException e) {
            System.out.println("Já existe um usuário com esse nome: " + e.getMessage());
        }
    }

    private void removeUser() {
        System.out.println("Insira o nome do usuário que pretende remover:");
        String username = sc.nextLine();

        try {
            model.removeUser(username);
            System.out.println("Usuário removido com sucesso!");
        } catch (UserDoesntExistException e) {
            System.out.println("Esse usuário não possui conta no SpotifUM!");
        }
    }




    private void accessQueries() {
        QueriesMenu queriesMenu = new QueriesMenu(model, sc);
        queriesMenu.runQueriesMenu();
    }

    private void saveCurrentStateJSON() {
        System.out.print("Caminho do ficheiro JSON a guardar: ");
        String path = sc.nextLine();

        try {
            DataSaver.saveDataToJson(model, path);
            System.out.println("Estado guardado com sucesso em JSON.");
        } catch (Exception e) {
            System.out.println("Erro ao guardar: " + e.getMessage());
        }
    }

    private void deserializeFromFile() {
        System.out.print("Caminho do ficheiro .ser a carregar: ");
        String filePath = sc.nextLine();

        try {
            this.model = SpotifUM.readFromFile(filePath);
            model.getAllAlbums().values().forEach(Album::resetState);
            System.out.println("Estado do sistema carregado com sucesso de .ser.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar: " + e.getMessage());
        }
    }

    private void serializeToFile() {
        System.out.print("Caminho do ficheiro .ser a guardar: ");
        String filePath = sc.nextLine();

        try {
            SpotifUM.writeToFile(model, filePath);
            System.out.println("Estado do sistema guardado com sucesso em .ser.");
        } catch (IOException e) {
            System.out.println("Erro ao guardar: " + e.getMessage());
        }
    }
}
