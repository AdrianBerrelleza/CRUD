package com.crud.GUi;

/**
 * Punto de entrada que NO extiende javafx.application.Application.
 * Evita el error "JavaFX runtime components are missing" que ocurre
 * al ejecutar un jar empaquetado cuya clase main sí extiende Application.
 */
public class Launcher {
    public static void main(String[] args) {
        MainApp.main(args);
    }
}
