package view;

import client.Client;
import client.ClientApp;
import client.ClientRMI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

//TODO far partire il gioco da CLI, gestire la reconnect
public class BoardViewGUI extends Application{
    private float mainMenuWidth = 870;
    private float mainMenuHeight = 650;

    @Override
    public void start(Stage primaryStage) { //can't change name
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

        Button CLI = setButtons("CLI", mainMenuWidth/2-30,mainMenuHeight/2+100, true);
        Button GUI = setButtons("GUI", mainMenuWidth/2+30, mainMenuHeight/2+100, true);
        Button RMI = setButtons("RMI", mainMenuWidth/2-30, mainMenuHeight/2+100, false);
        Button Socket = setButtons("Socket", mainMenuWidth/2+30, mainMenuHeight/2+100, false);
        Button confirm = setButtons("Confirm", 0, 0, false);
        Button back = setButtons("Return", mainMenuWidth / 2 + 65, mainMenuHeight / 2 + 138, false);

        GridPane Name = askName();
        TextField inputName = new TextField();
        ProgressIndicator progress = createBar();
        progress.setVisible(false);
        Label wait = new Label("Waiting for the game to begin");
        wait.setStyle("-fx-font-weight: bold");
        wait.setLayoutX(mainMenuWidth/2-60);
        wait.setLayoutY(mainMenuHeight/2+80);
        wait.setVisible(false);
        //Button thread = new Button();

        Name.add(confirm, 1, 2);
        Name.add(back, 2, 2);
        Name.add(inputName, 1, 1);
        root.getChildren().addAll(RMI, Socket, CLI, GUI, Name, back, progress, wait);

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
                Name.getChildren().get(1).setVisible(false);
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
                Name.getChildren().get(1).setVisible(false);
            }
        });

        confirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String name = inputName.getText();


                    if(isRMI[0] = true){
                        try {
                            ClientApp client = new ClientApp();
                            ClientRMI clientRMI = new ClientRMI(client);
                            clientRMI.connect("127.0.0.1");

                            if(clientRMI.register(name) /*|| clientRMI.reconnect(name, )*/){
                                Name.setVisible(false);
                                confirm.setVisible(false);
                                back.setVisible(false);
                                Name.getChildren().get(1).setVisible(false);
                                progress.setVisible(true);
                                wait.setVisible(true);
                                //startCountDown(clientRMI);
                            }
                            else Name.getChildren().get(1).setVisible(true);

                        }
                        catch (RemoteException e) {
                            e.fillInStackTrace();
                        }
                    }
                    //else parte socket
                }
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

   /* private void setOnAction(Button thread, ClientRMI clientRMI) {
        thread.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                while(true) {
                        if(clientRMI.hasStarted())
                            Platform.exit();

                    }
                }
        });
    }*/

    private BackgroundImage menuBackground() {
        try {
            Image image = new Image(new FileInputStream("src/resources/images/mainmenu/background.png"));
            return new BackgroundImage(image,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));

        } catch (FileNotFoundException e) {
            e.fillInStackTrace();
        }
        return null;
    }

    private Button setButtons(String string, float setX, float setY, boolean visible){
        Button RMI = new Button();
        RMI.setText(string);//modifica nome del pulsante
        RMI.setLayoutX(setX);
        RMI.setLayoutY(setY);
        RMI.setVisible(visible);
        return RMI;
    }



    private GridPane askName(){
        GridPane name = new GridPane();
        name.setAlignment(Pos.CENTER);
        name.setHgap(10);
        name.setVgap(10);
        name.setPadding(new Insets(0, 0, 0, 0));
        name.setVisible(false);

        name.add(new Label("Name:"), 0, 1);//altezza e poi lunghezza//permette d'inserire il testo
        Label fail = new Label("Error: name not valid");
        fail.setVisible(false);
        name.add(fail, 1, 0);
        name.setLayoutX(mainMenuWidth/2-80);
        name.setLayoutY(mainMenuHeight / 2 + 75);
        return name;
    }

    private ProgressBar createBar(){
        ProgressBar progress = new ProgressBar(0);
        progress.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        progress.setPrefWidth(200);
        progress.setPrefHeight(20);
        progress.setLayoutX(mainMenuWidth/2-80);
        progress.setLayoutY(mainMenuHeight/2+100);
        return progress;

    }

    /*private void gameStart(Client client) {
        Task task = new Task<Void>() {
            @Override
            protected Void call() {
                while(true)
                    if (client.hasStarted(client))
                        return null;
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
        Platform.exit();
    }*/

    private void startCountDown(Client client) {
        Thread thread = new Thread(){
            public void run() {

                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {

                    boolean verify;

                    public void run() {
                        if (verify = false) {
                            Platform.runLater(() -> verify = client.hasStarted());

                        } else {
                            timer.cancel();
                            Platform.exit();
                        }
                    }
                }, 1000, 1000); //Every 1 second
            }
    };
        thread.setDaemon(true);
        thread.start();
    }

}
