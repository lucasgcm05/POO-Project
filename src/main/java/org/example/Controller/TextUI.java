package org.example.Controller;
/*********************************************************************************/
/** DISCLAIMER: Este código foi criado e alterado durante as aulas práticas      */
/** de POO. Representa uma solução em construção, com base na matéria leccionada */ 
/** até ao momento da sua elaboração, e resulta da discussão e experimentação    */
/** durante as aulas. Como tal, não deverá ser visto como uma solução canónica,  */
/** ou mesmo acabada. É disponibilizado para auxiliar o processo de estudo.      */
/** Os alunos são encorajados a testar adequadamente o código fornecido e a      */
/** procurar soluções alternativas, à medida que forem adquirindo mais           */
/** conhecimentos de POO.                                                        */
/*********************************************************************************/

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

import org.example.Exceptions.*;
import org.example.Model.Musica.*;
import org.example.Model.Playlist.PlayList;
import org.example.Model.Playlist.UserCreatedPlaylist;
import org.example.Model.Queries.*;
import org.example.Model.SpotifUM;
import org.example.Model.User.*;
import org.example.Parser.DataBundle;
import org.example.Parser.DataLoader;
import org.example.Parser.DataSaver;
import org.example.View.Menu;
import org.example.Controller.AdminMenu;

/*
* FALTA SIMULAÇAO DOS TEMPOS
* IMPLEMNTAR A LOGICA DA PLAYLIST
* SERIALIZAÇAO PELO ADMINISTRADOR
* */

public class TextUI {
  private SpotifUM model;
  private Scanner sc;
  private User currentUser;

  public TextUI(SpotifUM model) {
    this.model = model;
    this.sc = new Scanner(System.in);
    this.currentUser = null;
  }

  public void run() {
    Menu menu = new Menu(new String[]{
            "Entrar como utilizador",
            "Registar novo utilizador",
            "Entrar como administrador"
    });

    menu.setHandler(1, this::loginUser);
    menu.setHandler(2, this::registerUser);
    menu.setHandler(3, this::loginAdmin);

    menu.run();
  }

  private void loginUser() {
    System.out.print("Nome de utilizador: ");
    String username = sc.nextLine();

    try {
      currentUser = model.getUser(username);
      System.out.println("Bem-vindo, " + currentUser.getName() + "!");
      UserMenu userMenu = new UserMenu(model, sc, currentUser);
      userMenu.runUserMenu();
    } catch (UserDoesntExistException e) {
      System.out.println("Utilizador não encontrado: " + e.getMessage());
    }
  }

  private void registerUser() {
    System.out.print("Escolha um nome de utilizador: ");
    String username = sc.nextLine();

    System.out.print("Email: ");
    String email = sc.nextLine();

    System.out.print("Morada: ");
    String address = sc.nextLine();

    System.out.println("Escolha o plano:");
    System.out.println("1 - FreePlan");
    System.out.println("2 - PremiumBasePlan");
    System.out.println("3 - PremiumTopPlan");

    int opcaoPlano = -1;
    while (opcaoPlano < 1 || opcaoPlano > 3) {
      System.out.print("Plano (1 a 3): ");
      try {
        opcaoPlano = Integer.parseInt(sc.nextLine());
      } catch (NumberFormatException e) {
        System.out.println("Introduza um número válido.");
      }
    }

    Plan plano;
    switch (opcaoPlano) {
      case 1 -> plano = new FreePlan();
      case 2 -> plano = new PremiumBasePlan();
      case 3 -> plano = new PremiumTopPlan();
      default -> throw new IllegalStateException("Plano inválido.");
    }

    User novo = new User(username, email, address, 0.0, plano, new ArrayList<>());

    try {
      model.addUser(novo);
      currentUser = novo;
      System.out.println("Utilizador registado com sucesso!");
      UserMenu userMenu = new UserMenu(model, sc, currentUser);
      userMenu.runUserMenu();
    } catch (UserAlreadyExistsException e) {
      System.out.println("Já existe um utilizador com esse nome: " + e.getMessage());
    }
  }

  private void loginAdmin() {
    System.out.println("Login Admin");
    String adminName = sc.nextLine();

    System.out.println("Bem Vindo ao Sistema, Admin: " + adminName + "!");

    AdminMenu adminMenu = new AdminMenu(model, sc);
    adminMenu.runAdminMenu();
  }
}
