package view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BoardViewGUI extends Application {
    private int mainMenuWidth = 800;
    private int mainMenuHeight = 598;

    @Override
    public void start(Stage primaryStage) { //can't change name
        Pane root = chooseConnection();
        root.setBackground(menuBackground());

        Scene scene = new Scene(root, mainMenuWidth, mainMenuHeight);//changes window dimension prima lunghezza, poi altezza

        primaryStage.setTitle("Adrenaline"); //nome dell'app
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This function will generate the main menu, consisting
     *
     * @return
     */
    public Pane chooseConnection() {
        Button btn1 = new Button();
        Button btn2 = new Button();
        btn1.setText("RMI");//modifica nome del pulsante
        btn2.setText("Socket");//modifica nome del pulsante
        btn1.setLayoutX(mainMenuWidth/2-30);
        btn1.setLayoutY(mainMenuHeight/2+100);
        btn2.setLayoutX(mainMenuWidth/2+30);
        btn2.setLayoutY(mainMenuHeight/2+100);
        Pane root = new Pane();
        root.getChildren().add(btn1);
        root.getChildren().add(btn2);

        /*
        Image imageDecline = new Image(getClass().getResourceAsStream("not.png"));
Button button5 = new Button();
button5.setGraphic(new ImageView(imageDecline)); for setting button image
         */

        btn1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                }
        });
        btn2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Pane root2 = chooseCLIorGUI();
            }
        });
        return root;
    }

    public Pane chooseCLIorGUI() {
        Button btn1 = new Button();
        Button btn2 = new Button();
        btn1.setText("CLI");//modifica nome del pulsante
        btn2.setText("GUI");//modifica nome del pulsante
        btn1.setLayoutX(mainMenuWidth/2-30);
        btn1.setLayoutY(mainMenuHeight/2+100);
        btn2.setLayoutX(mainMenuWidth/2+30);
        btn2.setLayoutY(mainMenuHeight/2+100);
        Pane root = new Pane();
        root.getChildren().add(btn1);
        root.getChildren().add(btn2);

        btn1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) { //print message
                System.out.println("GAGGIOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            }
        });
        return root;
    }

    public Background menuBackground() {
        try {
            Image image = new Image(new FileInputStream("src/resources/images/mainmenu/background.png"));
            BackgroundImage bgImg = new BackgroundImage(image,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));
            Background background = new Background(bgImg);
            return background;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
return null;
    }
}
