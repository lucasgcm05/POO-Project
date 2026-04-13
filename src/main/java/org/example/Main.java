package org.example;


import org.example.Controller.TextUI;
import org.example.Model.SpotifUM;
import org.example.Parser.*;



import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        new TextUI(new SpotifUM()).run();
    }
}