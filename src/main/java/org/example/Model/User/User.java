package org.example.Model.User;

import org.example.Model.Musica.Music;
import org.example.Model.User.History.History;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String email;
    private String address;
    private double points = 0;
    private Plan plan;
    //private final int idUser;
    private ArrayList<History> history;

    //private static int incrementaId = 0;

    public String getName() {
        return this.name;
    }

    /*public int getIdUser() {
        return idUser;
    }*/

    public ArrayList<History> getHistory() {
        return history;
    }

    public String getEmail() {
        return this.email;
    }

    public double getPoints() {
        return points;
    }

    public Plan getPlan() {
        return plan;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public void setHistory(ArrayList<History> history) {
        this.history = history;
    }

    public void setPlan(Plan newPlan) {
        if (newPlan instanceof PremiumTopPlan && !(this.plan instanceof PremiumTopPlan)) this.points += 100;
        this.plan = newPlan;
    }

    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) return false;
        User other = (User) o;
        return Objects.equals(other.getName(), this.name);
    }

    public User () {
        this.name = "";
        this.email = "";
        this.address = "";
        this.points = 0;
        this.plan = new FreePlan();
        //this.idUser = ++incrementaId;
        this.history = new ArrayList<>();
    }

    public User(String name, String email, String address, double points, Plan plan, ArrayList<History> history) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.points = points;
        this.plan = plan;
        //this.idUser = ++incrementaId;
        this.history = history;
    }

    public User(User o) {
        this.name = o.getName();
        this.email = o.getEmail();
        this.address = o.getAddress();
        this.points = o.getPoints();
        this.plan = o.getPlan();
        //this.idUser = o.getIdUser();
        this.history = o.getHistory();
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", points=" + points +
                ", plan=" + plan +
                //", idUser=" + idUser +
                ", history=" + history +
                '}';
    }

    public User clone() { return new User(this);}

    public void playMusic(Music music) {
        // calcula os pontos do user
        double earned = plan.calculatePoints(points);
        points += earned;

        // adiciona ao historico a música
        History his = new History();
        his.setMusic(music);
        his.setTimestamp(LocalDateTime.now());
        this.history.add(his);
    }
}