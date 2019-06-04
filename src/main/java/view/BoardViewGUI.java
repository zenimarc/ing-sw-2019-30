package view;

import client.Client;
import client.ClientRMI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;

public class BoardViewGUI extends Application {
    private float mainMenuWidth = 870;
    private float mainMenuHeight = 650;


    @Override
    public void start(Stage primaryStage) { //can't change name
        primaryStage.setTitle("Adrenaline"); //nome dell'app
        primaryStage.setResizable(false);
        Pane root = mainMenu(primaryStage);
        root.setBackground(new Background(menuBackground()));

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
     * This function will generate the main menu, where player can choose between CLI or GUI,
     * Socket or RMI, then insert name
     * @return a Pane
     */
    private Pane mainMenu(Stage gameStage) {
        final boolean[] isRMI = new boolean[1];
        Pane root = new Pane();
        Button RMI = setRMIButton();
        Button Socket = setSocketButton();
        Button CLI = setCLIButton();
        Button GUI = setGUIButton();
        Button confirm = confirmButton();
        Button back= backButton();
        GridPane Name = askName();
        Name.add(confirm, 1, 1);
        Name.add(back, 2, 1);
        GridPane wait = waitCheck("Verifying name...");
        GridPane fail = waitCheck("Error: name not valid");

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Warning");
        alert.setHeaderText("Required Fields Empty");

        root.getChildren().add(RMI);
        root.getChildren().add(Socket);
        root.getChildren().add(CLI);
        root.getChildren().add(GUI);
        root.getChildren().add(Name);
        root.getChildren().add(back);
        root.getChildren().add(wait);
        root.getChildren().add(fail);


        /*
        Image imageDecline = new Image(getClass().getResourceAsStream("not.png"));
Button button5 = new Button();
button5.setGraphic(new ImageView(imageDecline)); for setting button image
         */
        CLI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //farà andare il sistema
                Platform.exit();
            }
        });

        GUI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CLI.setVisible(false);
                GUI.setVisible(false);
                RMI.setVisible(true);
                Socket.setVisible(true);
                isRMI[0] = false;
            }
        });

        RMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                RMI.setVisible(false);
                Socket.setVisible(false);
                Name.setVisible(true);
                confirm.setVisible(true);
                back.setVisible(true);
                isRMI[0] = true;
            }
        });

        Socket.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                RMI.setVisible(false);
                Socket.setVisible(false);
                Name.setVisible(true);
                confirm.setVisible(true);
                back.setVisible(true);
            }
        });

        confirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String name = Name.getChildren().get(0).getAccessibleText();
                    Name.setVisible(false);
                    confirm.setVisible(false);
                    wait.setVisible(true);

                    if(isRMI[0] = true){
                        try {
                            ClientRMI clientRMI = new ClientRMI();
                            clientRMI.connect("127.0.0.1");
                            System.out.println(name);
                            clientRMI.register(name);

                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                /*}
                else {
                    //confirm.disableProperty().bind(Name.getAccessibleText().isEmpty());

                }*/

                }

                //se il controllo non va bene lancia errore e return, poi risetta name, confirm true, resto false

                //farà controllo nomi

        });

        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                RMI.setVisible(true);
                Socket.setVisible(true);
                Name.setVisible(false);
                confirm.setVisible(false);
                back.setVisible(false);
            }
        });

        return root;
    }

    private BackgroundImage menuBackground() {
        try {
            Image image = new Image(new FileInputStream("src/resources/images/mainmenu/background.png"));
            return new BackgroundImage(image,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Button setRMIButton(){
        Button RMI = new Button();
        RMI.setText("RMI");//modifica nome del pulsante
        RMI.setLayoutX(mainMenuWidth/2-30);
        RMI.setLayoutY(mainMenuHeight/2+100);
        RMI.setVisible(false);
        return RMI;
    }

    private Button setSocketButton(){
        Button Socket = new Button();
        Socket.setText("Socket");//modifica nome del pulsante
        Socket.setLayoutX(mainMenuWidth/2+30);
        Socket.setLayoutY(mainMenuHeight/2+100);
        Socket.setVisible(false);
        return Socket;
    }

    private Button setCLIButton(){
        Button CLI = new Button();
        CLI.setText("CLI");//modifica nome del pulsante
        CLI.setLayoutX(mainMenuWidth/2-30);
        CLI.setLayoutY(mainMenuHeight/2+100);
        return CLI;
    }

    private Button setGUIButton() {
        Button GUI = new Button();
        GUI.setText("GUI");//modifica nome del pulsante
        GUI.setLayoutX(mainMenuWidth / 2 + 30);
        GUI.setLayoutY(mainMenuHeight / 2 + 100);
        return GUI;
    }

    private Button confirmButton(){
        Button confirm = new Button();
        confirm.setText("Confirm");//modifica nome del pulsante
        confirm.setLayoutX(mainMenuWidth / 2 - 35);
        confirm.setLayoutY(mainMenuHeight / 2 + 130);
        confirm.setVisible(false);
        return confirm;
    }

    private Button backButton(){
        Button back = new Button();
        back.setText("Return");//modifica nome del pulsante
        back.setLayoutX(mainMenuWidth / 2 + 65);
        back.setLayoutY(mainMenuHeight / 2 + 135);
        back.setVisible(false);
        return back;
    }

    private GridPane askName(){
        GridPane name = new GridPane();
        name.setAlignment(Pos.CENTER);
        name.setHgap(10);
        name.setVgap(10);
        name.setPadding(new Insets(0, 0, 0, 0));
        name.setVisible(false);

        name.add(new Label("Name:"), 0, 0);//altezza e poi lunghezza//permette d'inserire il testo
        name.add(new TextField(), 1, 0);
        name.setLayoutX(mainMenuWidth/2-80);
        name.setLayoutY(mainMenuHeight / 2 + 100);
name.getChildren().get(1).setAccessibleText("name");
        return name;
    }

    private GridPane waitCheck(String string){
        GridPane waitCheck = new GridPane();
        waitCheck.setAlignment(Pos.CENTER);
        waitCheck.setHgap(10);
        waitCheck.setVgap(10);
        waitCheck.setPadding(new Insets(0, 0, 0, 0));
        waitCheck.setVisible(false);
        Label userName = new Label(string);//inserisce nome
        waitCheck.add(userName, 0, 0);//altezza e poi lunghezza
        waitCheck.setLayoutX(mainMenuWidth/2-20);
        waitCheck.setLayoutY(mainMenuHeight / 2 + 100);
        return waitCheck;
    }
}
