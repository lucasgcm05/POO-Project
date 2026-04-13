package org.example.Controller;

import org.example.Exceptions.MusicAlreadyExistsException;
import org.example.Exceptions.MusicDoesntExistException;
import org.example.Exceptions.PlaylistAlreadyExistsException;
import org.example.Exceptions.PlaylistDoesntExistException;
import org.example.Model.Musica.Album;
import org.example.Model.Musica.MultimediaMusic;
import org.example.Model.Musica.Music;
import org.example.Model.Playlist.*;
import org.example.Model.SpotifUM;
import org.example.Model.User.*;
import org.example.View.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UserMenu {
    private SpotifUM model;
    private Scanner sc;
    private User currentUser;
    private Album currentAlbum;
    private PlayList currentPlaylist;

    public UserMenu(SpotifUM model, Scanner sc, User currentUser) {
        this.model = model;
        this.sc = sc;
        this.currentUser = currentUser;
        this.currentAlbum = null; // Inicialmente, nenhum álbum está selecionado
        this.currentPlaylist = null;
    }

    public void runUserMenu() {
        Menu menu = new Menu(new String[]{
                "Selecionar álbum a ouvir",
                "Ouvir playlist, aleatória",
                "Escolher playlist",
                "Adiconar playlist",
                "Editar playlist",
                "Remover playlist",
                "Ouvir playlist de favoritos",
                "Ouvir playlist de um género com tempo limitado",
                "Trocar de plano"
        });

        menu.setHandler(1, this::ouvirAlbum);
        menu.setHandler(2, this::ouvirPlaylistAleatoria);
        menu.setHandler(3, this::ouvirPlaylist);
        menu.setHandler(4, this::addPlaylist);
        menu.setHandler(5, this::editPlaylist);
        menu.setHandler(6, this::removePlaylist);
        menu.setHandler(7, this::listenToFavoritesList);
        menu.setHandler(8, this::listenToGenderTimeLimitedPL);
        menu.setHandler(9, this::changePlan);

        if (!currentUser.getPlan().canCreatePlaylists()) {
            menu.setPreCondition(1, () -> false);
            menu.setPreCondition(3, () -> false);
            menu.setPreCondition(4, () -> false);
            menu.setPreCondition(5, () -> false);
            menu.setPreCondition(6, () -> false);
        }

        if (!currentUser.getPlan().canGenerateFavorites()) {
            menu.setPreCondition(7, () -> false);
            menu.setPreCondition(8, () -> false);
        }
        menu.run();
    }

    private void ouvirAlbum() {
        Map<String, Album> albums = model.getAllAlbums();
        List<Album> albumList = new ArrayList<>(albums.values());

        if (albumList.isEmpty()) {
            System.out.println("Não existem álbuns disponíveis.");
            return;
        }

        System.out.println("Álbuns disponíveis:");
        for (Album album : albumList) {
            System.out.printf("- %s (%s)\n", album.getName(), album.getAuthor());
        }

        Album selectedAlbum = null;
        while (selectedAlbum == null) {
            System.out.print("Escolha o nome do álbum: ");
            String albumName = sc.nextLine();
            selectedAlbum = albums.get(albumName);

            if (selectedAlbum == null) {
                System.out.println("Álbum não encontrado. Tente novamente.");
            }
        }
        currentAlbum = selectedAlbum;

        System.out.println("A tocar o álbum: " + currentAlbum.getName() + " por " + currentAlbum.getAuthor());
        playCurrent();

        albumPlaybackMenu();
    }

    private void albumPlaybackMenu() {
        Menu menu = new Menu(new String[]{
                "Tocar música atual",
                "Próxima música",
                "Música anterior",
                "Ativar shuffle",
        });

        menu.setHandler(1, this::playCurrent);
        menu.setHandler(2, this::nextMusic);
        menu.setHandler(3, this::previousMusic);
        menu.setHandler(4, this::shuffleAlbum);

        // Desativa shuffle se plano for base
        if (!currentUser.getPlan().canCreatePlaylists()) {
            menu.setPreCondition(3, () -> false);
            menu.setPreCondition(4, () -> false);
        }

        menu.run();
    }


    private void playCurrent() {
        Music m = currentAlbum.play(currentUser);
        System.out.println("A tocar: " + m.getName());
        System.out.println(m.getLyrics());

        if (m instanceof MultimediaMusic multimediaMusic) {
            System.out.println("Se quiser aceder ao vídeo: " + multimediaMusic.getVideoLink());
        }
    }

    private void nextMusic() {
        Music m = currentAlbum.next(currentUser);
        System.out.println("A tocar: " + m.getName());
        System.out.println(m.getLyrics());

        if (m instanceof MultimediaMusic multimediaMusic) {
            System.out.println("Se quiser aceder ao vídeo: " + multimediaMusic.getVideoLink());
        }
    }

    private void previousMusic() {
        Music m = currentAlbum.previous(currentUser);
        System.out.println("A tocar: " + m.getName());
        System.out.println(m.getLyrics());

        if (m instanceof MultimediaMusic multimediaMusic) {
            System.out.println("Se quiser aceder ao vídeo: " + multimediaMusic.getVideoLink());
        }
    }

    private void shuffleAlbum() {
        currentAlbum.shuffle(currentUser);
        if(currentAlbum.isShuffle()) {
            System.out.println("Shuffle ativado.");
        }
        else {
            System.out.println("Shuffle desativado.");
        }
    }

    private void ouvirPlaylistAleatoria() {
        try {
            // Obter uma playlist aleatória usando a classe RandomPlaylist
            PlayList randomPlaylist = RandomPlaylist.GenerateRandomPlaylist(model);

            if (randomPlaylist == null) {
                System.out.println("Não existem playlists disponíveis no sistema.");
                return;
            }
            currentPlaylist = randomPlaylist;
            currentPlaylist.resetState(); // Reinicializar o estado da playlist

            System.out.println("A tocar a playlist aleatória: " + currentPlaylist.getName() + " criada por " + currentPlaylist.getOwner().getName());

            playCurrentPL();

            playlistPlaybackMenu(); // Exibir o menu de reprodução da playlist

        } catch (IllegalStateException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void ouvirPlaylist() {
        try {
            // Obter todas as playlists
            Map<String, PlayList> allPlaylists = model.getAllPlaylists();

            // Filtrar playlists públicas e privadas do usuário atual
            List<PlayList> accessiblePlaylists = allPlaylists.values().stream()
                    .filter(playlist -> !playlist.isPrivate() || playlist.getOwner().equals(currentUser))
                    .toList();

            if (accessiblePlaylists.isEmpty()) {
                System.out.println("Não existem playlists disponíveis para você.");
                return;
            }

            System.out.println("Playlists disponíveis:");
            accessiblePlaylists.forEach(playlist ->
                    System.out.println("- " + playlist.getName() + " (Criada por: " + playlist.getOwner().getName() + ")")
            );

            PlayList selectedPlaylist = null;
            while (selectedPlaylist == null) {
                System.out.print("Escolha o nome da playlist: ");
                String playlistName = sc.nextLine();

                // Procurar a playlist pelo nome
                selectedPlaylist = accessiblePlaylists.stream()
                        .filter(playlist -> playlist.getName().equalsIgnoreCase(playlistName))
                        .findFirst()
                        .orElse(null);

                if (selectedPlaylist == null) {
                    System.out.println("Playlist não encontrada. Tente novamente.");
                }
            }

            currentPlaylist = selectedPlaylist;
            currentPlaylist.resetState(); // Reinicializa o estado da playlist

            // Se plano for base → shuffle + next
            if (!currentUser.getPlan().canCreatePlaylists()) {
                currentPlaylist.shuffle(currentUser);     // ativa shuffle
                Music m = currentPlaylist.next(currentUser); // força shuffle a escolher 1ª música
                System.out.println("A tocar a playlist: " + currentPlaylist.getName() + " criada por " + currentPlaylist.getOwner().getName());
                System.out.println("A tocar: " + m.getName());
                System.out.println(m.getLyrics());
            } else {
                System.out.println("A tocar a playlist: " + currentPlaylist.getName() + " criada por " + currentPlaylist.getOwner().getName());
                playCurrentPL();
            }

            playlistPlaybackMenu();
        } catch (IllegalStateException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void playlistPlaybackMenu() {
        Menu menu = new Menu(new String[]{
                "Tocar música atual",
                "Próxima música",
                "Música anterior",
                "Ativar shuffle",
        });

        menu.setHandler(1, this::playCurrentPL);
        menu.setHandler(2, this::nextMusicPL);
        menu.setHandler(3, this::previousMusicPL);
        menu.setHandler(4, this::shufflePL);

        // Desativa shuffle se plano for base
        if (!currentUser.getPlan().canCreatePlaylists()) {
            menu.setPreCondition(3, () -> false);
            menu.setPreCondition(4, () -> false);
        }

        menu.run();
    }

    private void playCurrentPL() {
        if (currentPlaylist == null) {
            System.out.println("Nenhuma playlist selecionada.");
            return;
        }

        Music m = currentPlaylist.play(currentUser);
        System.out.println("A tocar: " + m.getName());
        System.out.println(m.getLyrics());

        if (m instanceof MultimediaMusic multimediaMusic) {
            System.out.println("Se quiser aceder ao vídeo: " + multimediaMusic.getVideoLink());
        }
    }

    private void nextMusicPL() {
        if (currentPlaylist == null) {
            System.out.println("Nenhuma playlist selecionada.");
            return;
        }

        Music m = currentPlaylist.next(currentUser);
        System.out.println("A tocar: " + m.getName());
        System.out.println(m.getLyrics());

        if (m instanceof MultimediaMusic multimediaMusic) {
            System.out.println("Se quiser aceder ao vídeo: " + multimediaMusic.getVideoLink());
        }
    }

    private void previousMusicPL() {
        if (currentPlaylist == null) {
            System.out.println("Nenhuma playlist selecionada.");
            return;
        }

        Music m = currentPlaylist.previous(currentUser);
        System.out.println("A tocar: " + m.getName());
        System.out.println(m.getLyrics());

        if (m instanceof MultimediaMusic multimediaMusic) {
            System.out.println("Se quiser aceder ao vídeo: " + multimediaMusic.getVideoLink());
        }
    }

    private void shufflePL() {
        if (currentPlaylist == null) {
            System.out.println("Nenhuma playlist selecionada.");
            return;
        }

        currentPlaylist.shuffle(currentUser);
        if (currentPlaylist.isShuffle()) {
            System.out.println("Shuffle ativado.");
        } else {
            System.out.println("Shuffle desativado.");
        }
    }

    private void addPlaylist() {
        // Solicitar o nome da playlist
        System.out.print("Insira o nome da playlist: ");
        String playlistName = sc.nextLine();

        // Verificar se o nome já existe
        if (model.playlistExists(playlistName)) {
            System.out.println("Já existe uma playlist com esse nome. Escolha outro nome.");
            return;
        }

        // Perguntar se a playlist será privada
        System.out.print("A playlist será privada? (s/n): ");
        boolean isPrivate = sc.nextLine().equalsIgnoreCase("s");

        // Exibir todas as músicas disponíveis
        Map<String, Music> allMusics = model.getAllMusics();
        if (allMusics.isEmpty()) {
            System.out.println("Não há músicas disponíveis no sistema para adicionar à playlist.");
            return;
        }

        System.out.println("Músicas disponíveis:");
        allMusics.values().forEach(music ->
                System.out.println("- " + music.getName() + " por " + music.getAuthor())
        );

        // Solicitar músicas para adicionar à playlist
        List<Music> selectedMusics = new ArrayList<>();
        System.out.println("Digite o nome das músicas que deseja adicionar à playlist. Digite 'sair' para terminar.");

        while (true) {
            System.out.print("Nome da música (ou 'sair' para terminar): ");
            String musicName = sc.nextLine();
            if (musicName.equalsIgnoreCase("sair")) break;

            Music music = model.getMusic(musicName);
            if (music != null) {
                selectedMusics.add(music);
                System.out.println("Música adicionada: " + music.getName());
            } else {
                System.out.println("Música não encontrada. Tente novamente.");
            }
        }

        // Criar a playlist usando o método `create` da classe UserCreatedPlaylist
        try {
            UserCreatedPlaylist playlist = UserCreatedPlaylist.create(currentUser, playlistName, selectedMusics, isPrivate);
            model.addPlaylist(playlist);
            System.out.println("Playlist criada com sucesso!");
        } catch (UnsupportedOperationException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (PlaylistAlreadyExistsException e) {
            System.out.println("Já existe uma playlist com esse nome: " + e.getMessage());
        }
    }

    private void editPlaylist() {
        System.out.print("Insira o nome da playlist que deseja editar: ");
        String playlistName = sc.nextLine();

        try {
            // Obter a playlist pelo nome
            PlayList playlist = model.getPlaylist(playlistName);

            // Verificar se o usuário atual é o dono da playlist
            if (!playlist.getOwner().equals(currentUser)) {
                System.out.println("Erro: Você não tem permissão para editar esta playlist, pois não é o dono.");
                return;
            }

            // Verificar se a playlist é do tipo UserCreatedPlaylist
            if (!(playlist instanceof UserCreatedPlaylist)) {
                System.out.println("Erro: Apenas playlists criadas por usuários podem ser editadas.");
                return;
            }

            UserCreatedPlaylist userPlaylist = (UserCreatedPlaylist) playlist;

            // Exibir menu de edição
            Menu menu = new Menu(new String[]{
                    "Adicionar música à playlist",
                    "Remover música da playlist"
            });

            menu.setHandler(1, () -> addMusicToPlaylist(userPlaylist));
            menu.setHandler(2, () -> removeMusicFromPlaylist(userPlaylist));

            menu.run();

        } catch (PlaylistDoesntExistException e) {
            System.out.println("Erro: Essa playlist não existe no SpotifUM!");
        }
    }

    private void addMusicToPlaylist(UserCreatedPlaylist playlist) {
        System.out.print("Insira o nome da música que deseja adicionar: ");
        String musicName = sc.nextLine();

        Music music = model.getMusic(musicName);
        if (music == null) {
            System.out.println("Erro: Música não encontrada no sistema.");
            return;
        }

        try {
            playlist.addMusic(music);
            System.out.println("Música adicionada com sucesso à playlist!");
        } catch (MusicAlreadyExistsException e) {
            System.out.println("Erro: A música já existe na playlist.");
        }
    }

    private void removeMusicFromPlaylist(UserCreatedPlaylist playlist) {
        System.out.print("Insira o nome da música que deseja remover: ");
        String musicName = sc.nextLine();

        try {
            playlist.removeMusic(musicName);
            System.out.println("Música removida com sucesso da playlist!");
        } catch (MusicDoesntExistException e) {
            System.out.println("Erro: A música não existe na playlist.");
        }
    }

    private void removePlaylist() {
        System.out.println("Insira o nome da playlist que pretende remover:");
        String playlistName = sc.nextLine();

        try {
            // Obter a playlist pelo nome
            PlayList playlist = model.getPlaylist(playlistName);

            // Verificar se o usuário atual é o dono da playlist
            if (!playlist.getOwner().equals(currentUser)) {
                System.out.println("Erro: Você não tem permissão para remover esta playlist, pois não é o dono.");
                return;
            }

            // Verificar se a playlist é do tipo UserCreatedPlaylist
            if (!(playlist instanceof UserCreatedPlaylist)) {
                System.out.println("Erro: Apenas playlists criadas por usuários podem ser removidas.");
                return;
            }

            // Remover a playlist
            model.removePlaylist(playlistName);
            System.out.println("Playlist removida com sucesso!");
        } catch (PlaylistDoesntExistException e) {
            System.out.println("Erro: Essa playlist não existe no SpotifUM!");
        }
    }

    private void listenToFavoritesList() {
        try {
            // Verificar se o plano do usuário permite criar playlists de favoritos
            if (!currentUser.getPlan().canGenerateFavorites()) {
                System.out.println("Erro: Apenas utilizadores PremiumTop podem criar e ouvir playlists de favoritos.");
                return;
            }

            // Gerar a playlist de favoritos usando o método da classe FavoritesPlaylist
            FavoritesPlaylist favoritesPlaylist = FavoritesPlaylist.GenerateFavPlayList(model, currentUser);


            if (favoritesPlaylist == null || favoritesPlaylist.getMusics().isEmpty()) {
                System.out.println("Não há músicas suficientes no histórico para gerar uma playlist de favoritos.");
                return;
            }

            currentPlaylist = favoritesPlaylist;
            currentPlaylist.resetState(); // Reinicializar o estado da playlist

            System.out.println("A tocar a playlist de favoritos: " + currentPlaylist.getName());

            // Tocar a primeira música da playlist
            playCurrentPL();

            // Exibir o menu de reprodução da playlist
            playlistPlaybackMenu();
        } catch (UnsupportedOperationException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void listenToGenderTimeLimitedPL() {
        try {
            // Verificar se o plano do usuário permite criar playlists baseadas em gênero e tempo
            if (!currentUser.getPlan().canGenerateGenreTimeLimited()) {
                System.out.println("Erro: Apenas utilizadores PremiumTop podem criar e ouvir playlists baseadas em gênero e tempo.");
                return;
            }

            // Solicitar o gênero ao usuário
            System.out.print("Insira o gênero desejado: ");
            String genre = sc.nextLine();

            // Solicitar a duração máxima ao usuário
            System.out.print("Insira a duração máxima da playlist (em minutos): ");
            int maxDurationMinutes;
            try {
                maxDurationMinutes = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Erro: A duração deve ser um número inteiro.");
                return;
            }

            // Gerar a playlist baseada no gênero e na duração máxima
            GenreTimeLimitedPlaylist genreTimeLimitedPlaylist = GenreTimeLimitedPlaylist.create(
                    currentUser,
                    new ArrayList<>(), // Lista inicialmente vazia (será preenchida no método)
                    genre,
                    true, // Define como privada ou não, conforme o teu caso
                    maxDurationMinutes * 60, // transforma minutos em segundos
                    currentUser.getName() + "_genre_" + genre,
                    model.getAllAlbums()
            );


            if (genreTimeLimitedPlaylist == null || genreTimeLimitedPlaylist.getMusics().isEmpty()) {
                System.out.println("Não há músicas suficientes no sistema para criar uma playlist com o gênero e duração especificados.");
                return;
            }

            currentPlaylist = genreTimeLimitedPlaylist;
            currentPlaylist.resetState(); // Reinicializar o estado da playlist

            System.out.println("A tocar a playlist baseada em gênero e tempo: " + currentPlaylist.getName());

            // Tocar a primeira música da playlist
            playCurrentPL();

            // Exibir o menu de reprodução da playlist
            playlistPlaybackMenu();
        } catch (UnsupportedOperationException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void changePlan() {
        System.out.println("Plano atual: " + currentUser.getPlan().getClass().getSimpleName());
        System.out.println("Escolha o novo plano:");

        // Exibir opções de planos disponíveis
        System.out.println("1 - Base");
        System.out.println("2 - Premium Base");
        System.out.println("3 - Premium Top");

        int option = -1;
        while (option < 1 || option > 3) {
            System.out.print("Digite o número do plano desejado: ");
            try {
                option = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Erro: Por favor, insira um número válido.");
            }
        }

        // Determinar o novo plano com base na escolha do usuário
        Plan newPlan;
        switch (option) {
            case 1 -> newPlan = new FreePlan();
            case 2 -> newPlan = new PremiumBasePlan();
            case 3 -> newPlan = new PremiumTopPlan();
            default -> throw new IllegalStateException("Opção inválida.");
        }

        // Verificar se o novo plano é diferente do plano atual
        if (currentUser.getPlan().getClass().equals(newPlan.getClass())) {
            System.out.println("Erro: Você já está no plano selecionado.");
            return;
        }

        // Alterar o plano do usuário
        currentUser.setPlan(newPlan);
        System.out.println("Plano alterado com sucesso para: " + newPlan.getClass().getSimpleName());

        // Recarregar o menu do usuário para atualizar as preconditions
        System.out.println("Atualizando o menu para refletir as permissões do novo plano...");
        runUserMenu();
    }

}
