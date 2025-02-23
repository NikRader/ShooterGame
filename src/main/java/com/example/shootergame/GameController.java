package com.example.shootergame;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class GameController {

    @FXML
    private Pane nar;
    @FXML
    private Circle circleA;
    @FXML
    private Circle circleB;
    @FXML
    private Line narrow;
    @FXML
    private Polygon polygon;
    @FXML
    private Label score_number;
    @FXML
    private Label score_num;
    @FXML
    private Label shots_number;
    @FXML
    private Label plus_one;
    @FXML
    private Label plus_two;
    @FXML
    private Button start_button;
    @FXML
    private Button stop_button;
    @FXML
    private Button shot_button;
    @FXML
    Thread t;
    boolean run = true, shot = false;
    int shot_count = 0;
    int score = 0;

    @FXML
    void initialize() {
        start_button.setDisable(false);
        stop_button.setDisable(true);
        shot_button.setDisable(true);
        shots_number.setText("" + shot_count);
        score_num.setText("" + score);
        plus_one.setVisible(false);
        plus_two.setVisible(false);
    }

    //------------------------------------------Targets---------------------------------
    private void RunCircleA() {
        double rad1 = circleA.getRadius();
        double y1 = circleA.getLayoutY();
        y1 += 9;
        if ((y1 + rad1) >= 462) y1 = 10;
        circleA.setLayoutY(y1);
    }

    private void RunCircleB() {
        double rad2 = circleB.getRadius();
        double y2 = circleB.getLayoutY();
        y2 += 12;
        if ((y2 + rad2) >= 462) y2 = 10;
        circleB.setLayoutY(y2);
    }

    // ----------------------------------------Narrow------------------------------------
    private void RunNarrow() {
        double y = nar.getLayoutY();
        double x = nar.getLayoutX();
        x += 32;
        nar.setLayoutX(x);
    }

    private void Narrow_back() {
        nar.setLayoutX(77);
        nar.setLayoutY(233);
    }

    private boolean narrow_outside() {
        double x = nar.getLayoutX() + 70;
        if (x > 670) return true;
        return false;
    }

    //--------------------------------Check of hit----------------------------------

    // Check of hit in Target A
    private boolean ShotA() {
        double x_end_nar = nar.getLayoutX() + nar.getPrefWidth();
        double y_end_nar = nar.getLayoutY() + nar.getPrefHeight() / 2;

        double xA = circleA.getLayoutX();
        double yA = circleA.getLayoutY();
        double rA = circleA.getRadius();
        if (x_end_nar >= xA - rA && x_end_nar <= xA + rA && y_end_nar >= yA - rA && y_end_nar <= yA + rA) {
            return true;
        }
        return false;
    }

    // Check of hit in Target B
    private boolean ShotB() {
        double x_end_nar = nar.getLayoutX() + nar.getPrefWidth();
        double y_end_nar = nar.getLayoutY() + nar.getPrefHeight() / 2;
        double xB = circleB.getLayoutX();
        double yB = circleB.getLayoutY();
        double rB = circleB.getRadius();
        if (x_end_nar >= xB - rB && x_end_nar <= xB + rB && y_end_nar >= yB - rB && y_end_nar <= yB + rB) {
            return true;
        }
        return false;
    }

    // --------------------Buttons------------------------

    // Start Button
    @FXML
    protected void StartGame() {
        stop_button.setDisable(false);
        shot_button.setDisable(false);
        if (t != null) return; // Protection from creating of several Threads
        t = new Thread(() -> {
            run = true;
            while (run) {
                RunCircleA();
                RunCircleB();
                if (shot) {
                    RunNarrow();
                    plus_one.setVisible(false);
                    plus_two.setVisible(false);
                }
                if (ShotA()) {
                    Platform.runLater(() -> {
                        score += 1;
                        score_num.setText("" + score);
                        plus_one.setVisible(true);
                    });
                    System.out.println("Попал в А");
                    Narrow_back();
                    shot = false;
                }
                if (ShotB()) {
                    Platform.runLater(() -> {
                        score += 2;
                        score_num.setText("" + score);
                        plus_two.setVisible(true);
                    });
                    System.out.println("Попал в B");
                    Narrow_back();
                    shot = false;
                }
                if (narrow_outside()) {
                    Narrow_back();
                    shot = false;
                }
                try {
                    t.sleep(100);
                } catch (InterruptedException e) {
                    run = false;
                }
            }
        });
        t.start();
    }

    // Shot Button
    @FXML
    protected void Shots() {
        if (t == null) return;
        shot = true;
        System.out.println("Выстрелил!");
        shot_count++;
        shots_number.setText("" + shot_count);
    }

    // Stop Button
    @FXML
    protected void StopGame() {
        if (t == null) return;
        t.interrupt();
        shot_count = 0;
        score = 0;
        shots_number.setText("" + shot_count);
        score_num.setText("" + score);
        circleA.setLayoutY(247);
        circleB.setLayoutY(247);
        t = null;
    }
}

