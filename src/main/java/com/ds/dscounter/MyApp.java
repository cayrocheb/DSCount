package com.ds.dscounter;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.security.Key;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyApp extends Application {
    private int nbMorts = 0; // variable pour stocker le nombre de morts
    private KeyCode toucheIncrement = KeyCode.SPACE; // variable pour stocker la touche d'incrémentation
    private KeyCode touchePedale = KeyCode.F1;

    private int remainingTimeMin;
    private int remainingTimeSec;

    private static Label tempsTotalLabel;
    private static Label tempsMonoLabel;
    private int totalTimeMin;
    private int totalTimeSec;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Création du label
        Label mortsLabel = new Label("Morts : " + nbMorts);
        mortsLabel.setStyle("-fx-font-size: 24px;"); // style pour agrandir le texte
        mortsLabel.setTextFill(Color.WHITE);

        // Création du label pour le temps
        tempsTotalLabel = new Label("Temps pédalé : " + totalTimeMin + ":" + totalTimeSec);
        tempsTotalLabel.setStyle("-fx-font-size: 24px;");
        tempsTotalLabel.setTextFill(Color.WHITE);

        // Création du label pour le temps
        tempsMonoLabel = new Label("Temps à pédaler : " + remainingTimeMin + ":" + remainingTimeSec);
        tempsMonoLabel.setStyle("-fx-font-size: 24px;");
        tempsMonoLabel.setTextFill(Color.WHITE);

        // Ajout des deux labels dans un VBox
        VBox vbox = new VBox(mortsLabel, tempsTotalLabel,tempsMonoLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);


        // Création de la première fenêtre
        StackPane root1 = new StackPane();
        root1.setStyle("-fx-background-color: green;");
        root1.getChildren().add(vbox);
        Scene scene1 = new Scene(root1, 400, 300);



        // Écouteur d'événements pour le nombre de morts
        scene1.setOnKeyPressed(event -> {
            if (event.getCode() == toucheIncrement) {
                nbMorts++;
                mortsLabel.setText("Morts : " + nbMorts);
            }
            else if(event.getCode() == touchePedale){
                    System.out.println("Entrée");
                    remainingTimeSec=0;
                    remainingTimeMin=2;
                    Thread t =new Thread(this::chrono);
                    t.start();
            }
        });


        primaryStage.setScene(scene1);
        primaryStage.show();

        // Création de la deuxième fenêtre
        Stage secondaryStage = new Stage();
        StackPane root2 = new StackPane();
        root2.setStyle("-fx-background-color: #ffffff;");
        Scene scene2 = new Scene(root2, 300, 200);

        // Création du ComboBox pour choisir la touche d'incrémentation
        ObservableList<KeyCode> options = FXCollections.observableArrayList(
                KeyCode.A, KeyCode.B, KeyCode.C, KeyCode.D, KeyCode.E, KeyCode.F, KeyCode.G,
                KeyCode.H, KeyCode.I, KeyCode.J, KeyCode.K, KeyCode.L, KeyCode.M, KeyCode.N,
                KeyCode.O, KeyCode.P, KeyCode.Q, KeyCode.R, KeyCode.S, KeyCode.T, KeyCode.U,
                KeyCode.V, KeyCode.W, KeyCode.X, KeyCode.Y, KeyCode.Z,
                KeyCode.F1, KeyCode.F2, KeyCode.F3, KeyCode.F4, KeyCode.F5, KeyCode.F6,
                KeyCode.F7, KeyCode.F8, KeyCode.F9, KeyCode.F10, KeyCode.F11, KeyCode.F12,
                KeyCode.DIGIT0, KeyCode.DIGIT1, KeyCode.DIGIT2, KeyCode.DIGIT3, KeyCode.DIGIT4,
                KeyCode.DIGIT5, KeyCode.DIGIT6, KeyCode.DIGIT7, KeyCode.DIGIT8, KeyCode.DIGIT9,
                KeyCode.NUMPAD0, KeyCode.NUMPAD1, KeyCode.NUMPAD2, KeyCode.NUMPAD3, KeyCode.NUMPAD4,
                KeyCode.NUMPAD5, KeyCode.NUMPAD6, KeyCode.NUMPAD7, KeyCode.NUMPAD8, KeyCode.NUMPAD9,
                KeyCode.ADD, KeyCode.SUBTRACT, KeyCode.MULTIPLY, KeyCode.DIVIDE, KeyCode.DECIMAL
        );
        ComboBox<KeyCode> comboBox = new ComboBox<>(options);
        ComboBox<KeyCode> comboBox2 = new ComboBox<>(options);
        comboBox2.setValue(touchePedale);
        comboBox.setValue(toucheIncrement); // sélectionne la valeur par défaut
        comboBox.setOnAction(event -> toucheIncrement = comboBox.getValue()); // met à jour la variable toucheIncrement
        comboBox2.setOnAction(event -> touchePedale = comboBox2.getValue());

        Label label = new Label("Touche mort:");
        label.setStyle("-fx-font-size: 24px;");
        label.setTextFill(Color.BLACK);

        Label label2 = new Label("Touche pédalage:");
        label2.setStyle("-fx-font-size: 24px;");
        label2.setTextFill(Color.BLACK);

        VBox vBox = new VBox(label, comboBox,label2, comboBox2);
        vBox.setAlignment(Pos.CENTER);

        root2.getChildren().add(vBox);

        secondaryStage.setScene(scene2);
        secondaryStage.show();
    }
    private void chrono(){
        boolean out = false;
        while(!out){
            try {
                if(remainingTimeSec==0){
                    if(remainingTimeMin==0){
                        out=true;
                    }
                    else{
                        remainingTimeMin--;
                        remainingTimeSec=59;
                    }
                }
                else{
                    remainingTimeSec--;
                }
                if(totalTimeSec+1 == 60){
                    totalTimeMin++;
                    totalTimeSec=0;
                }
                else{
                    totalTimeSec++;
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tempsTotalLabel.setText("Temps pédalé : " + totalTimeMin + ":" + totalTimeSec);
                        tempsMonoLabel.setText("Temps à pédaler : " + remainingTimeMin + ":" + remainingTimeSec);
                    }
                });
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}



