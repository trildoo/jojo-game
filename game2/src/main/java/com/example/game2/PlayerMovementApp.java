package com.example.game2;
import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javafx.animation.PauseTransition;

public class PlayerMovementApp extends Application {
    public static final int SCENE_WIDTH = 1920;
    public static final int SCENE_HEIGHT = 1000;
    public static final double PLAYER_RADIUS = 60;//change value if needed(this will affect both him,enemy,their icons)
    Random random = new Random();
    int randomNumber = random.nextInt(8);


    int HP = 300;
    int ATK = 10;
    int PLAYER_LOCATION_X = 500;
    int PLAYER_LOCATION_Y = 500;
    int charge=0;
    int ENEMY_HP = 50;
    int ENEMY_ATK = 5;
    int BOSS_HP=400;
    int counter=0;
    int glow_level=10;
    double target_y;
    double target_x;
    double movementSpeed = 10.0;
    int kill_counter;
    boolean Joseph_spoke = false;
    boolean regen_happened=false;
    String musicFilePath = "Y2Mate.is - JoJo's Bizarre Adventure - Opening 3 4K 60FPS Creditless -7a9fFpNMSpo-48k-1659809909489.wav";//background
    String soundFilePath = "emeraldsplash (mp3cut.net).wav";//kakyoin
    String soundFilePath1 ="Y2Mate.is - Star Platinum Summon Sound Effect-cZLUBF0_uOo-48k-1658058451351 (mp3cut.net).wav";//jotaro charge(e)
    String soundFilePath2 = "Y2Mate.is - ZA WARUDO-7ePWNmLP0Z0-48k-1657990994260 (mp3cut.net)(1).wav";//za warudo
    String soundFilePath3 = "Y2Mate.is - ORA ORA ORA - Sound effect-bz5Z1yxs8cY-48k-1659817090874 (mp3cut.net)(1).wav";
    String soundFilePath4 = "Y2Mate.is - [ZA WARUDO OVER HEAVEN] Sound effect-D4H9DO5MwCk-48k-1659878323419 (mp3cut.net)(1).wav";
    public Stage controlsStage;
    public Circle player;
    public ImageView icon;//jotaro
    public ImageView icon_enemy;//bat
    public ImageView icon_BOSS;//dio
    public ImageView icon_BOSS_knife;//knife,part of dio's ultimate
    public ImageView icon_kakyoin;//kakyoin
    public ImageView icon_kakyoin_stand;//his stand
    public Circle enemy;
    public Label label_charge;
    public Label label_ultimate;
    public Label label_HP_left;
    public Label label_ENEMY_HP_left;
    public Label label_BOSS_HP_left;
    public Label label_stats;
    public Label label_BOSS_attack;
    public Label Controls;
    public Label kakyoin_speak;
    public Rectangle fillRect;//enemy
    public Rectangle fillRect1;//boss
    public Rectangle fillRect2;//player
    public Circle BOSS;
    public Circle za_warudo_aura_in ;
    public Circle holy_aura;
    public Circle player_aura;
    public Rectangle BOSS_knife;
    public Circle kakyoin;
    public Circle kakyoin_stand;
    public Rectangle nextArea;

    Glow glow = new Glow();

    //public void setRandomNumber(int randomNumber) {//made to test the unlikely scenario
   //     this.randomNumber = randomNumber;
   // }

    @Override
    public void start(Stage primaryStage) {

        Pane root = new Pane();
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        primaryStage.setTitle("My Game");
        String relativeImagePath = "set_game.png";
        Image backgroundImage = new Image(new File(relativeImagePath).toURI().toString());
        ImageView backgroundImageView = new ImageView(backgroundImage);
        root.getChildren().add(backgroundImageView);
Script script_object = new Script();
        script_object.showScript();
        initializePlayer();
        initializeEnemy(root);
        initializeBOSS();
        initializeSupport();
        initializeSupport_stand();
        playBackgroundMusic(musicFilePath);

        root.getChildren().addAll(player, icon, label_charge,
                fillRect,fillRect1, Controls, label_ENEMY_HP_left,
                label_stats,BOSS,icon_BOSS,label_BOSS_HP_left,
                label_BOSS_attack,za_warudo_aura_in,player_aura,BOSS_knife,
                icon_BOSS_knife,kakyoin,icon_kakyoin,kakyoin_speak,kakyoin_stand
                ,icon_kakyoin_stand, label_HP_left,fillRect2,label_ultimate,nextArea,holy_aura);

        scene.setOnKeyPressed(event -> handleKeyPress(event.getCode()));
        scene.setOnKeyReleased(event -> handleKeyRelease(event.getCode()));

        primaryStage.setScene(scene);
        primaryStage.show();}
    public void initializePlayer() {
        player = new Circle(PLAYER_RADIUS, Color.BLUE);
        player_aura = new Circle(PLAYER_RADIUS,Color.NAVY);
        holy_aura = new Circle(PLAYER_RADIUS,Color.LIME);

        String relativeImagePath2 = "jotaro_original-removebg-preview.png";
        icon = new ImageView(new Image(new File(relativeImagePath2).toURI().toString()));
        icon.setOnMouseClicked(event -> showLabel_STATS(HP,ATK));

        za_warudo_aura_in = new Circle(PLAYER_RADIUS,Color.GOLD);
        za_warudo_aura_in.setVisible(false);

        String relativeImagePath1 = "pixil-frame-0(7).png";//knife
        icon_BOSS_knife = new ImageView(new Image(new File(relativeImagePath1).toURI().toString()));

        BOSS_knife = new Rectangle(32,32,Color.RED);
        BOSS_knife.setVisible(false);
        BOSS_knife.setY(650);
        BOSS_knife.setX(1600);
        icon_BOSS_knife.setTranslateX(BOSS_knife.getX());
        icon_BOSS_knife.setTranslateY(BOSS_knife.getY());
        icon_BOSS_knife.setVisible(false);

        player.setOpacity(0.0);
        player.setTranslateX(PLAYER_LOCATION_X);
        player.setTranslateY(PLAYER_LOCATION_Y);

        Controls = new Label();

        label_charge = new Label();
        label_ultimate = new Label();
        label_stats = new Label();
        label_ENEMY_HP_left = new Label();
        label_BOSS_HP_left = new Label();
        label_BOSS_attack = new Label();

        double iconSize = PLAYER_RADIUS * 2;  // Use the circle's diameter as the icon size
        icon.setFitWidth(iconSize);
        icon.setFitHeight(iconSize);
        icon.setTranslateX(player.getTranslateX() - PLAYER_RADIUS);
        icon.setTranslateY(player.getTranslateY() - PLAYER_RADIUS);

        fillRect = new Rectangle();
        fillRect.setWidth(0);
        fillRect.setHeight(0);
        fillRect.setTranslateX(PLAYER_LOCATION_X - 250);
        fillRect.setTranslateY(230);

        fillRect1 = new Rectangle();
        fillRect1.setWidth(0);
        fillRect1.setHeight(0);
        fillRect1.setTranslateX(1600);
        fillRect1.setTranslateY(600);

        player_aura.setOpacity(0.5);
        player_aura.setTranslateY(player.getTranslateY());
        player_aura.setTranslateX(player.getTranslateY());
        player_aura.setVisible(false);


        holy_aura.setOpacity(0.5);
        holy_aura.setTranslateY(player.getTranslateY());
        holy_aura.setTranslateX(player.getTranslateY());
        holy_aura.setVisible(false);

        kakyoin_speak = new Label();

        label_HP_left = new Label();
        label_HP_left.setTranslateY(900);
        label_HP_left.setTranslateX(50);

        fillRect2 = new Rectangle();
        fillRect2.setWidth(600);
        fillRect2.setHeight(40);
        fillRect2.setTranslateX(10);
        fillRect2.setTranslateY(950);
        showLabel_HP_left(HP,300);

        nextArea = new Rectangle();

    }
    public void initializeEnemy(Pane pane) {
        enemy = new Circle(PLAYER_RADIUS + 10, Color.RED);
        enemy.setOpacity(0.0);
        enemy.setTranslateX(300);
        enemy.setTranslateY(300);
        String relativeImagePath3 = "bat_enemy_50_50-removebg-preview.png";
        icon_enemy = new ImageView(new Image(new File(relativeImagePath3).toURI().toString()));
        icon_enemy.setOnMouseClicked(event -> {
            ENEMY_HP -= ATK;
            showLabel_ENEMY_HP_left(ENEMY_HP,50);
            if (ENEMY_HP <= 0) {
                kill_counter++;
                pane.getChildren().removeAll(enemy, icon_enemy, label_ENEMY_HP_left, fillRect);
                pane.getChildren().addAll(enemy, icon_enemy, label_ENEMY_HP_left, fillRect);
                ENEMY_HP = 50;
            }
            EnemyAttack(ENEMY_ATK);
            ATK=10;
            removeLabel_Ultimate();
            player_aura.setVisible(false);
        });
        double iconSize = PLAYER_RADIUS * 2;
        icon_enemy.setFitWidth(iconSize);
        icon_enemy.setFitHeight(iconSize);
        icon_enemy.setTranslateX(enemy.getTranslateX() - PLAYER_RADIUS);
        icon_enemy.setTranslateY(enemy.getTranslateY() - PLAYER_RADIUS);
        pane.getChildren().addAll(enemy, icon_enemy);
    }
    public void initializeBOSS(){
        BOSS = new Circle(PLAYER_RADIUS, Color.GOLD);
        BOSS.setOpacity(0.0);
        BOSS.setTranslateX(1700);
        BOSS.setTranslateY(700);
        String relativeImagePath4 = "dio_original-removebg-preview.png";
        icon_BOSS = new ImageView(new Image(new File(relativeImagePath4).toURI().toString()));
        double iconSize = PLAYER_RADIUS * 2;
        icon_BOSS.setFitWidth(iconSize);
        icon_BOSS.setFitHeight(iconSize);
        icon_BOSS.setTranslateX(BOSS.getTranslateX() - PLAYER_RADIUS);
        icon_BOSS.setTranslateY(BOSS.getTranslateY() - PLAYER_RADIUS);
        icon_BOSS.setOnMouseClicked(event -> {
            icon.getTranslateX();
            icon.getTranslateY();
            if((icon.getTranslateX()>1300&&icon.getTranslateX()<1800) &&icon.getTranslateY()>400&&icon.getTranslateY()<900){
                BOSS_HP -= ATK;
                showLabel_BOSS_HP_left(BOSS_HP,400);
                counter++;
                if(BOSS_HP<=0&& !regen_happened){
                    JOptionPane.showMessageDialog(null,"DIO:my regeneration abilities saved me(i doubt ill be able to use them again)");
                    BOSS_HP=400;
                    showLabel_BOSS_HP_left(400,400);
                    regen_happened=true;
                }
                if(BOSS_HP<=0&& regen_happened){
                    icon_BOSS.setVisible(false);
                    label_BOSS_HP_left.setVisible(false);
                    JOptionPane.showMessageDialog(null,"Congratulations,Boss defeated");
                    showEnding();

                }
                if(counter==4){
                    try {
                        BossUltimate();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    counter=0;
                }
            }
            else{
                JOptionPane.showMessageDialog(null,"JOTARO:I CANNOT BEAT THE SHIT OUT OF YOU WITHOUT GETTING CLOSER");
                JOptionPane.showMessageDialog(null,"DIO:OH HO!THEN COME AS CLOSE AS YOU LIKE");

            }

        });
    }
    public void initializeSupport(){
        kakyoin = new Circle(PLAYER_RADIUS, Color.LIMEGREEN);
        kakyoin.setOpacity(0.0);
        kakyoin.setTranslateX(30);
        kakyoin.setTranslateY(300);
        String relativeImagePath5 = "kakyoin-removebg-preview.png";
        icon_kakyoin = new ImageView(new Image(new File(relativeImagePath5).toURI().toString()));
        double iconSize = PLAYER_RADIUS * 2;
        icon_kakyoin.setFitWidth(iconSize);
        icon_kakyoin.setFitHeight(iconSize);
        icon_kakyoin.setTranslateX(30);
        icon_kakyoin.setTranslateY(300);
        icon_kakyoin.setOnMouseClicked((event -> {
            KakyoinSpeak(kill_counter);

        }));
    }
    public void initializeSupport_stand(){
        kakyoin_stand = new Circle(PLAYER_RADIUS, Color.LIMEGREEN);
        kakyoin_stand.setOpacity(0.0);
        kakyoin_stand.setTranslateX(30);
        kakyoin_stand.setTranslateY(300);
        String relativeImagePath6 = "kakyoin_stand_80-100_new-removebg-preview.png";
        icon_kakyoin_stand = new ImageView(new Image(new File(relativeImagePath6).toURI().toString()));
        icon_kakyoin_stand.setVisible(false);
        double iconSize = PLAYER_RADIUS * 2;
        icon_kakyoin_stand.setFitWidth(iconSize);
        icon_kakyoin_stand.setFitHeight(iconSize);
        icon_kakyoin_stand.setTranslateX(30);
        icon_kakyoin_stand.setTranslateY(300);
        icon_kakyoin_stand.setOnMouseClicked((event -> {
            KakyoinSpeak(kill_counter);
            showLabel_BOSS_HP_left(BOSS_HP-50,400);
        }));
    }
    public void handleKeyPress(KeyCode keyCode) {
        if (keyCode == KeyCode.W || keyCode == KeyCode.UP) {
            player.setTranslateY(player.getTranslateY() - movementSpeed);
            player_aura.setTranslateY(player_aura.getTranslateY() - movementSpeed);
        } else if (keyCode == KeyCode.A || keyCode == KeyCode.LEFT) {
            player.setTranslateX(player.getTranslateX() - movementSpeed);
            player_aura.setTranslateX(player.getTranslateX() - movementSpeed);
        } else if (keyCode == KeyCode.S || keyCode == KeyCode.DOWN) {
            player.setTranslateY(player.getTranslateY() + movementSpeed);
            player_aura.setTranslateY(player_aura.getTranslateY() + movementSpeed);
        } else if (keyCode == KeyCode.D || keyCode == KeyCode.RIGHT) {
            player.setTranslateX(player.getTranslateX() + movementSpeed);
            player_aura.setTranslateX(player_aura.getTranslateX() + movementSpeed);
        } else if (keyCode == KeyCode.E) {
            showChargingLabel();
            charge++;
        }
        else if(keyCode == KeyCode.TAB){
            showControls();
        }
        else if (keyCode == KeyCode.Q) {
            if(player_aura.isVisible()){
                label_ultimate.setText("Ultimate is already active");
            }
            else{
                showLabel_Ultimate();
                Ultimate();
                charge = 0;
            }

        }
        else if (keyCode == KeyCode.H) {
            if (HP < 300) {
                JOptionPane.showMessageDialog(null, "HOLY:Your mother is always there for you Jotaro");
                JOptionPane.showMessageDialog(null, "HOLY:I'll stay with you till you don't need me anymore");
                holy_aura.setTranslateX(player.getTranslateX());
                holy_aura.setTranslateY(player.getTranslateY());
                holy_aura.setVisible(true);
                HP = HP + 10;
                showLabel_HP_left(HP, 300);
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                    holy_aura.setVisible(false);
                }));
                timeline.setCycleCount(1);
                timeline.play();
            } else {
                JOptionPane.showMessageDialog(null, "SYSTEM:Cannot heal");
                holy_aura.setVisible(false);
            }
        }
         target_x = player.getTranslateX()- PLAYER_RADIUS;
         target_y = player.getTranslateY()- PLAYER_RADIUS;
        icon.setTranslateX(player.getTranslateX() - PLAYER_RADIUS);
        icon.setTranslateY(player.getTranslateY() - PLAYER_RADIUS);
    }
    public void handleKeyRelease(KeyCode keyCode) {
        if (keyCode == KeyCode.E) {
            removeChargingLabel();

        }

    }
    public void showChargingLabel() {
        label_charge.setTranslateX(900 - 30);
        label_charge.setTranslateY(20);
        if(charge>=3){
            label_charge.setText("SYSTEM:Already fully charged");
        }else{
            playSound(soundFilePath1);
            label_charge.setText("JOTARO:Star Platinum Charge");
        }
        Font font = Font.font("Arial", FontWeight.EXTRA_BOLD, 30);
        label_charge.setBackground(Background.fill(Color.BLUE));
        label_charge.setStyle("-fx-border-color: black");
        label_charge.setFont(font);
        Pane root = (Pane) player.getParent();
        if (!root.getChildren().contains(label_charge)) {
            root.getChildren().add(label_charge);
        }
    }
    public void removeChargingLabel() {
        Pane root = (Pane) player.getParent();
        root.getChildren().remove(label_charge);
    }
    public void Ultimate() {
        glow.setLevel(glow_level);
        player_aura.setEffect(glow);
        playSound(soundFilePath3);
        ATK = ATK + 50;
        player_aura.setTranslateY(player.getTranslateY());
        player_aura.setTranslateX(player.getTranslateX());
        player_aura.setVisible(true);
    }

    public void showLabel_Ultimate(){
        label_ultimate.setText("JOTARO:ORA ORA ORA ORA ORA ORA ORA ORA ORA ORA ORA ");
        label_ultimate.setVisible(false);
        label_ultimate.setTranslateX(900 - 30);
        label_ultimate.setTranslateY(200);
        label_ultimate.setVisible(true);
        Font font = Font.font("Arial", FontWeight.EXTRA_BOLD, 30);
        label_ultimate.setBackground(Background.fill(Color.BLUE));
        label_ultimate.setStyle("-fx-border-color: black");
        label_ultimate.setFont(font);
        Pane root = (Pane) player.getParent();
        if (!root.getChildren().contains(label_ultimate)) {
            root.getChildren().add(label_ultimate);
        }
    }
    public void removeLabel_Ultimate() {
        Pane root = (Pane) player.getParent();
        root.getChildren().remove(label_ultimate);
    }
    public void KakyoinSpeak(int kill_counter) {
        kakyoin_speak.setTranslateX(300);
        kakyoin_speak.setTranslateY(10);
        kakyoin_speak.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        kakyoin_speak.setBackground(Background.fill(Color.GREEN));
        kakyoin_speak.setStyle("-fx-border-color: black");
        if (!za_warudo_aura_in.isVisible()) {
            if (kakyoin_speak.isVisible()) {
                kakyoin_speak.setVisible(false);
            } else {
                kakyoin_speak.setVisible(true);
                if (kill_counter < 4) {
                    kakyoin_speak.setText("KAKYOIN: You need to kill more enemies so that I can get my energy back ");
                } else {
                    kill_counter=0;
                    kakyoin_speak.setText("KAKYOIN:EMERALD SPLASH");
                    playSound(soundFilePath);
                    icon_kakyoin.setVisible(false);
                    Pane root = (Pane) player.getParent();
                    root.getChildren().remove(icon_kakyoin);
                    icon_kakyoin_stand.setVisible(true);
                    double targetX = 1700; // Boss location
                    double targetY = 700;
                    double startX = icon_kakyoin_stand.getTranslateX();
                    double startY = icon_kakyoin_stand.getTranslateY();
                    int numRectangles = 10;
                    AtomicInteger rectanglesFinished = new AtomicInteger();
                    final int finalKillCounter = kill_counter; // Create a final copy of the kill_counter
                    double radius = 100; // Distance of rectangles from Kakyoin
                    double angleIncrement = 360.0 / numRectangles; // Angle increment between rectangles
                    Duration delay = Duration.seconds(0.1); // Delay between each rectangle's animation
                    List<Rectangle> rectangles = new ArrayList<>();
                    for (int i = 0; i < numRectangles; i++) {
                        double angle = Math.toRadians(angleIncrement * i); // Calculate angle in radians
                        double offsetX = radius * Math.cos(angle); // Calculate X offset
                        double offsetY = radius * Math.sin(angle); // Calculate Y offset
                        Rectangle rectangle = new Rectangle(20, 20);
                        rectangle.setTranslateX(startX + offsetX); // Set X position based on offset
                        rectangle.setTranslateY(startY + offsetY); // Set Y position based on offset
                        // Create a gradient fill for the rectangle
                        Stop[] stops = new Stop[]{new Stop(0, Color.DARKGREEN), new Stop(1, Color.LIGHTGREEN)};
                        LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
                        rectangle.setFill(gradient);
                        // Apply a glow effect to make it look like a shiny emerald
                        Glow glow = new Glow();
                        glow.setLevel(100);
                        rectangle.setEffect(glow);
                        root.getChildren().add(rectangle);
                        rectangles.add(rectangle);
                        double endX = targetX;
                        double endY = targetY;
                        Timeline timeline = new Timeline();
                        timeline.getKeyFrames().add(
                                new KeyFrame(Duration.ZERO, new KeyValue(rectangle.translateXProperty(), startX + offsetX),
                                        new KeyValue(rectangle.translateYProperty(), startY + offsetY))
                        );
                        timeline.getKeyFrames().add(
                                new KeyFrame(Duration.seconds(2), new KeyValue(rectangle.translateXProperty(), endX),
                                        new KeyValue(rectangle.translateYProperty(), endY))
                        );
                        timeline.setOnFinished(event -> {
                            rectanglesFinished.getAndIncrement();
                            root.getChildren().remove(rectangle); // Remove the rectangle after it reaches the target
                            if (rectanglesFinished.get() == numRectangles) {
                                kakyoin_speak.setText("");
                                kakyoin_speak.setBackground(null);
                                kakyoin_speak.setStyle("-fx-border-color: null");
                                icon_kakyoin_stand.setOnMouseClicked(e2 -> KakyoinSpeak(finalKillCounter));
                            }
                        });
                        timeline.setDelay(delay.multiply(i)); // Apply delay based on the index of the rectangle
                        timeline.play();
                    }
                    // Check if za_warudo_aura_in becomes visible while rectangles are traveling
                    za_warudo_aura_in.visibleProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            for (Rectangle rectangle : rectangles) {
                                rectangle.setTranslateX(rectangle.getTranslateX()); // Stop the movement of the rectangle
                                rectangle.setTranslateY(rectangle.getTranslateY());
                            }
                            rectangles.clear(); // Remove all rectangles at their current location
                        }
                    });
                    icon_kakyoin_stand.setDisable(true); // Disable clicking during the cooldown
                    // Add a delay between clicks to prevent spamming

                    PauseTransition clickCooldown = new PauseTransition(Duration.seconds(2)); // 2 seconds cool down
                    clickCooldown.setOnFinished(event -> {
                        icon_kakyoin_stand.setDisable(false); // Enable clicking after the cooldown
                        icon_kakyoin_stand.setOnMouseClicked(e2 -> KakyoinSpeak(finalKillCounter)); // Re-enable the mouse click event
                        BOSS_HP =BOSS_HP-30;
                        showLabel_BOSS_HP_left(BOSS_HP,400);

                        if(BOSS_HP<=0){
                            if(icon_BOSS.isVisible()){
                                icon_BOSS.setVisible(false);
                                label_BOSS_HP_left.setVisible(false);
                                JOptionPane.showMessageDialog(null,"Congratulations,Boss defeated");
                                 showEnding();

                            }

                        }
                    });
                    clickCooldown.play();
                }
            }
        }
    }

    public void showEnding() {
        while (true) {

            try {
                int rating = Integer.parseInt(JOptionPane.showInputDialog("Rate this game from 0-10:"));

                if (rating < 5&&rating>=0) {
                    JOptionPane.showMessageDialog(null, "That's a shame. Hope you like it more next time \uD83D\uDE14");
                    JOptionPane.showMessageDialog(null, "Check the console for a surprise that might make up for it");
                    Script ending_script = new Script();
                    ending_script.showEndingScript();

                    break;
                } else if (rating == 5) {
                    JOptionPane.showMessageDialog(null, "So, it's about decent. I'll take that \uD83D\uDE10");
                    JOptionPane.showMessageDialog(null, "Check the console for a surprise that might make up for it");
                    Script ending_script = new Script();
                    ending_script.showEndingScript();
                    break;
                } else if(rating>5&&rating <=10){
                    JOptionPane.showMessageDialog(null, "Glad you enjoyed it \uD83D\uDE0A");
                    JOptionPane.showMessageDialog(null, "Check the console for a surprise!!");
                    Script ending_script = new Script();
                    ending_script.showEndingScript();
                    break;

                }
                else{
                    JOptionPane.showMessageDialog(null, "Oops,thats not an acceptable value");
                    continue;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid integer rating!");
                continue;
            }
        }
    }


    public void showLabel_ENEMY_HP_left(int ENEMY_HP,int MAX_HP) {
        label_ENEMY_HP_left.setTranslateX(PLAYER_LOCATION_X - 250);
        label_ENEMY_HP_left.setTranslateY(200);
        label_ENEMY_HP_left.setText("Enemy HP: " + ENEMY_HP+" ");
        label_ENEMY_HP_left.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        label_ENEMY_HP_left.setBackground(Background.fill(Color.WHITE));
        label_ENEMY_HP_left.setStyle("-fx-border-color: black");
        double percentage = (double) ENEMY_HP / MAX_HP;  // Calculate the percentage based on the maximum HP
        double fillWidth = 300 * percentage;
        fillRect.setWidth(fillWidth);
        if(percentage==1){
            fillRect.setVisible(false);
        }
        else if (percentage > 0.7) {
            fillRect.setWidth(300);
            fillRect.setHeight(20);
            fillRect.setVisible(true);
            fillRect.setFill(Color.GREEN);
        } else if (percentage > 0.4) {
            fillRect.setWidth(fillWidth);
            fillRect.setVisible(true);
            fillRect.setFill(Color.ORANGE);
        } else {
            fillRect.setWidth(fillWidth);
            fillRect.setVisible(true);
            fillRect.setFill(Color.RED);
        }
        Pane root = (Pane) player.getParent();
        if (!root.getChildren().contains(fillRect)) {
            root.getChildren().add(fillRect);
        }
    }
    public void EnemyAttack(int ENEMY_ATK){
        HP=HP-ENEMY_ATK;
        showLabel_HP_left(HP,300);
        if(HP<=0){
            if(!Joseph_spoke){
                JOptionPane.showMessageDialog(null,"Joseph:Jotaro you cannot give up, avenge me");
                HP=100;
                Joseph_spoke=true;
            }
            else if(Joseph_spoke){
                JOptionPane.showMessageDialog(null,"YOU LOSE JOTARO");
                icon.setVisible(false);
                icon_kakyoin_stand.setVisible(false);
                icon_kakyoin.setVisible(false);
                label_HP_left.setVisible(false);
            }
        }
    }
    public void BossUltimate() throws InterruptedException {
//setRandomNumber(2);
        if (randomNumber == 2) {
            label_BOSS_attack.setEffect(glow);
            label_BOSS_attack.setText("ZA WARUDO OVER HEAVEN");
            ATK=0;
            label_BOSS_attack.setTranslateX(800);
            label_BOSS_attack.setTranslateY(300);
            label_BOSS_attack.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 30));
            label_BOSS_attack.setBackground(Background.fill(Color.GOLD));
            label_BOSS_attack.setStyle("-fx-border-color: black");
            label_BOSS_attack.setVisible(true);
            playSound(soundFilePath4);
            icon.setVisible(false);
            player_aura.setVisible(false);
            icon_kakyoin_stand.setVisible(false);
            icon_kakyoin.setVisible(false);
            label_HP_left.setVisible(false);
            Joseph_spoke=true;
            HP=0;




        } else {
            glow.setLevel(glow_level);
            za_warudo_aura_in.setVisible(true);
            za_warudo_aura_in.setOpacity(0.5);
            za_warudo_aura_in.setTranslateX(1700);
            za_warudo_aura_in.setTranslateY(700);
            za_warudo_aura_in.setVisible(false);
            za_warudo_aura_in.setEffect(glow);

            playSound(soundFilePath2);
            label_BOSS_attack.setTranslateX(1600);
            label_BOSS_attack.setTranslateY(300);
            label_BOSS_attack.setText("ZA WARUDO");
            label_BOSS_attack.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            label_BOSS_attack.setBackground(Background.fill(Color.GOLD));
            label_BOSS_attack.setStyle("-fx-border-color: black");
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, e -> {
                        za_warudo_aura_in.setVisible(true);
                        icon_BOSS_knife.setVisible(true);
                        movementSpeed = 0.01;
                    }),
                    new KeyFrame(Duration.seconds(5), e -> {
                        // End of the animation, hide aura
                        za_warudo_aura_in.setVisible(false);
                        label_BOSS_attack.setText("");
                        label_BOSS_attack.setBackground(null);
                        label_BOSS_attack.setStyle("-fx-border-color: null");
                        KnifeMove(target_x, target_y);
                        movementSpeed = 10.00;
                        HP = HP - 30;
                        showLabel_HP_left(HP, 300);
                        ATK = 10;
                        removeLabel_Ultimate();
                        if (HP <= 0) {
                            if (!Joseph_spoke) {
                                JOptionPane.showMessageDialog(null, "Joseph:Jotaro you cannot give up,please avenge me");
                                HP = 100;
                                Joseph_spoke = true;
                            } else if (Joseph_spoke) {

                                JOptionPane.showMessageDialog(null, "YOU LOSE JOTARO");
                                icon.setVisible(false);
                                icon_kakyoin_stand.setVisible(false);
                                icon_kakyoin.setVisible(false);
                                label_HP_left.setVisible(false);
                            }
                        }

                    })
            );
            timeline.play();
        }
    }
    public void KnifeMove(double target_x, double target_y) {
        Pane root = (Pane) player.getParent();
        // Remove and add BOSS_knife
        root.getChildren().remove(BOSS_knife);
        root.getChildren().add(BOSS_knife);
        // Remove and add icon_BOSS_knife
        root.getChildren().remove(icon_BOSS_knife);
        root.getChildren().add(icon_BOSS_knife);
        // Set BOSS_knife coordinates
        BOSS_knife.setWidth(2);
        BOSS_knife.setY(target_y + 40);
        BOSS_knife.setX(target_x + 70);
        BOSS_knife.setVisible(true);
        // Set icon_BOSS_knife translation
        icon_BOSS_knife.setTranslateX(target_x + 70);
        icon_BOSS_knife.setTranslateY(target_y + 40);
        icon_BOSS_knife.setVisible(true);
        player_aura.setVisible(false);
        ATK=10;
    }
    public void showLabel_HP_left(int current_HP, int MAX_HP) {
        double percentage = (double) current_HP / MAX_HP;
        double fillWidth = 300 * percentage; // Assuming the initial width is 300

        fillRect2.setWidth(fillWidth);
        fillRect2.setHeight(30);
        fillRect2.setVisible(true);

        if (percentage == 1) {
            fillRect2.setFill(Color.GREEN);
        } else if (percentage > 0.7) {
            fillRect2.setFill(Color.GREEN);
        } else if (percentage > 0.5) {
            fillRect2.setFill(Color.ORANGE);
        } else {
            fillRect2.setFill(Color.RED);
        }
    }

    public void showLabel_BOSS_HP_left(int BOSS_HP,int MAX_HP) {
        label_BOSS_HP_left.setTranslateX(1600);
        label_BOSS_HP_left.setTranslateY(550);
        label_BOSS_HP_left.setText("BOSS HP: " + BOSS_HP+" ");
        label_BOSS_HP_left.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        label_BOSS_HP_left.setBackground(Background.fill(Color.WHITE));
        label_BOSS_HP_left.setStyle("-fx-border-color: black");
        double percentage = (double) BOSS_HP / MAX_HP;  // Calculate the percentage based on the maximum HP
        double fillWidth = 300 * percentage;

        fillRect1.setWidth(fillWidth);
        if(percentage==1){
            fillRect1.setVisible(false);
        }
        else if (percentage > 0.7) {
            fillRect1.setWidth(300);
            fillRect1.setHeight(20);
            fillRect1.setVisible(true);
            fillRect1.setFill(Color.GREEN);
        } else if (percentage > 0.5) {
            fillRect1.setWidth(fillWidth);
            fillRect1.setVisible(true);
            fillRect1.setFill(Color.ORANGE);
        } else {
            fillRect1.setWidth(fillWidth);
            fillRect1.setVisible(true);
            fillRect1.setFill(Color.RED);
        }
        Pane root = (Pane) player.getParent();
        if (!root.getChildren().contains(fillRect1)) {
            root.getChildren().add(fillRect1);
        }
    }
    public void showLabel_STATS(int HP, int ATK) {
        label_stats.setTranslateX(900 - 30);
        label_stats.setTranslateY(20);
        if (label_stats.isVisible()) {
            label_stats.setVisible(false);
        } else {
            label_stats.setText("Player Stats:\nHP: " + HP + "\nATK: " + ATK+"\nkill count:"+kill_counter);
            label_stats.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            label_stats.setBackground(Background.fill(Color.WHITE));
            label_stats.setStyle("-fx-border-color: black");
            label_stats.setVisible(true);
            Pane root = (Pane) player.getParent();
            if (!root.getChildren().contains(label_stats)) {
                root.getChildren().add(label_stats);
            }
        }
    }
    public void showControls() {
        Controls.setTranslateX(500);
        Controls.setTranslateY(500);
        controlsStage = new Stage();
        VBox root = new VBox();
        Scene scene = new Scene(root, 500, 300);
        controlsStage.setScene(scene);
        controlsStage.setResizable(false);
        controlsStage.setTitle("Controls");

        Label controlLabel1 = new Label("PRESS W,A,S,D,ARROW KEYS : MOVEMENT");
        controlLabel1.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Label controlLabel2 = new Label("CLICK(ENEMY) : ATTACK");
        controlLabel2.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Label controlLabel3 = new Label("CLICK(SELF) : SHOW STATS");
        controlLabel3.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Label controlLabel4 = new Label("PRESS E : CHARGE");
        controlLabel4.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Label controlLabel5 = new Label("PRESS Q : ULTIMATE");
        controlLabel5.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Label controlLabel6 = new Label("CLICK(ALLY) : SUPPORT");
        controlLabel6.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Label controlLabel8 = new Label("PRESS H : HEAL");
        controlLabel8.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        root.getChildren().addAll(controlLabel1, controlLabel2,controlLabel3,controlLabel4,controlLabel5,controlLabel6,controlLabel8);
        controlsStage.show();
    }


    public void playBackgroundMusic(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void playSound(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
