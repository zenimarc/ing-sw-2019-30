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
import javafx.stage.Window;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

//TODO far partire il gioco da CLI, gestire la reconnect
public class BoardViewGUI extends Application{
    private float mainMenuWidth = 870;
    private float mainMenuHeight = 650;
    private ClientApp app;
    private Client client;
    private String info;
    private Pane root;
    private boolean hasStarted;

    @Override
    public void start(Stage primaryStage) { //can't change name
        primaryStage.setResizable(false);
        root = mainMenu(primaryStage);
        root.setBackground(new Background(menuBackground()));

        Scene scene = new Scene(root, mainMenuWidth, mainMenuHeight);//changes window dimension prima lunghezza, poi altezza

        primaryStage.setTitle("Adrenaline"); //nome dell'app
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public float getHeight(){
        return this.mainMenuHeight;
    }

    public float getWidth(){
        return this.mainMenuWidth;
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This function will generate the main menu, where player can choose between CLI or GUI,
     * Socket or RMI, then insert name
     * @return a Pane
     */
    private Pane mainMenu(Stage stage) {
        final boolean[] isRMI = new boolean[1];
        Pane root = new Pane();
        Button CLI = setButtons("CLI", mainMenuWidth/2-30,mainMenuHeight/2+100, true);
        Button GUI = setButtons("GUI", mainMenuWidth/2+30, mainMenuHeight/2+100, true);
        Button RMI = setButtons("RMI", mainMenuWidth/2-30, mainMenuHeight/2+100, false);
        Button Socket = setButtons("Socket", mainMenuWidth/2+30, mainMenuHeight/2+100, false);
        Button confirm = setButtons("Confirm", 0, 0, false);
        Button back = setButtons("Return", mainMenuWidth / 2 + 40, mainMenuHeight / 2 + 138, false);

        Button connect = setButtons("Connect", mainMenuWidth/2-50, mainMenuHeight/2+100, false);
        Button reconnect = setButtons("Reconnect", mainMenuWidth/2+50, mainMenuHeight/2+100, false);


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
        root.getChildren().addAll(RMI, Socket, CLI, GUI, Name, back, progress, wait, connect, reconnect);

        CLI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
                app = new ClientApp();
                app.beginApp();

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
                isRMI[0] = false;

            }
        });

        confirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                    if(isRMI[0] = true){
                        try {
                            app = new ClientApp();
                            client = new ClientRMI(app);

                            if (((ClientRMI) client).connect(inputName.getText(), 1099)) {
                                setName(Name);
                                inputName.setText("");
                                back.setLayoutX(mainMenuWidth / 2 + 65);
                                confirm.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        info = inputName.getText();
                                        connect.setVisible(true);
                                        reconnect.setVisible(true);
                                        Name.setVisible(false);
                                        back.setVisible(false);
                                    }
                                });
                            }
                            else Name.getChildren().get(1).setVisible(true);
                        }
                        catch (RemoteException e) {
                            Name.getChildren().get(1).setVisible(true);
                            e.fillInStackTrace();
                        }
                    }
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

        connect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                connect.setVisible(false);
                reconnect.setVisible(false);
                if(((ClientRMI) client).register(info)) {
                    progress.setVisible(true);
                    wait.setVisible(true);
                    hasStarted = true;
                }
                else{
                     Name.setVisible(true);
                     Name.getChildren().get(1).setVisible(true);
                }

            }
        });

        reconnect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if(((ClientRMI) client).reconnect(info, new UUID(2, 2))){
                        connect.setVisible(false);
                        reconnect.setVisible(false);
                        progress.setVisible(true);
                        wait.setVisible(true);
                        hasStarted = true;
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        return root;
    }

    private EventHandler<ActionEvent> setNameAction(TextField text, Pane askName, Client clientRMI, Button connect, Button reconnect) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                info = text.getText();
                connect.setVisible(true);
                reconnect.setVisible(true);
                askName.setVisible(false);
            }
        };
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

        name.add(new Label("IP:"), 0, 1);//altezza e poi lunghezza//permette d'inserire il testo
        Label fail = new Label("Error: Ip not valid");
        fail.setVisible(false);
        name.add(fail, 1, 0);
        name.setLayoutX(mainMenuWidth/2-80);
        name.setLayoutY(mainMenuHeight / 2 + 75);
        return name;
    }

    private void setName(Pane pane){
        ((Label)pane.getChildren().get(0)).setText("Name:");
        ((Label)pane.getChildren().get(1)).setText("Error: Name not valid");
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

    private void startCountDown(Stage stage) {
        Thread thread = new Thread(){
            public void run() {

                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {

                    boolean verify;

                    public void run() {
                        while(hasStarted = false){

                        }
                            timer.cancel();
                            try {
                                BoardViewGameGUI view = new BoardViewGameGUI();
                                stage.setScene(view.createScene(client));
                                stage.setWidth(view.getWidth());
                                stage.setHeight(view.getHeight());
                            } catch (FileNotFoundException | InterruptedException e) {
                                e.printStackTrace();
                        }
                    }
                }, 1000, 1000); //Every 1 second
            }
    };
        thread.setDaemon(true);
        thread.start();
    }

}
