package view;

import client.ClientRMI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.util.TimerTask;

//TODO far partire il gioco da CLI, gestire la reconnect
public class BoardViewGUI extends Application {
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

        TextField prova = new TextField();
        GridPane Name = askName();

        ProgressIndicator test = createBar();
        test.setVisible(false);

        Name.add(confirm, 1, 2);
        Name.add(back, 2, 2);


        root.getChildren().add(RMI);
        root.getChildren().add(Socket);
        root.getChildren().add(CLI);
        root.getChildren().add(GUI);
        root.getChildren().add(Name);
        root.getChildren().add(back);
        root.getChildren().add(test);



        Name.add(prova, 1, 1);

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
                String name = prova.getText();


                    if(isRMI[0] = true){
                        try {
                            ClientRMI clientRMI = new ClientRMI();
                            clientRMI.connect("127.0.0.1");

                            if(clientRMI.register(name) /*|| clientRMI.reconnect(name, )*/){
                                Name.setVisible(false);
                                confirm.setVisible(false);
                                Name.getChildren().get(1).setVisible(false);
                                test.setVisible(true);

                                Platform.exit();
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
for(int i = 0; i < 100; i++) {
    progress.setProgress(i++);
    System.out.print(i);
}

        /*final Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(50);
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                progress.setProgress(new_val.doubleValue()/50);

            }
        });*/

        progress.setLayoutX(mainMenuWidth/2-30);
        progress.setLayoutY(mainMenuHeight/2+100);
        return progress;

    }

}
