package view;

import board.Board;
import board.Cell;
import board.Position;
import board.billboard.Billboard;
import board.billboard.BillboardGenerator;
import client.Client;
import controller.CommandObj;
import controller.PlayerCommand;
import controller.PlayerController;
import deck.AmmoCard;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import player.Player;
import weapon.WeaponCard;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import static controller.PlayerCommand.*;
import static deck.Bullet.toIntArray;
import static powerup.PowerUp.*;

//TODO Finire altri due bottoni a livello di azioni, sistemare la posizione dei bottoni delle celle
public class BoardViewGameGUI extends Application {
    private int stageHeight = 700;
    private int stageWidth = 920;
    private Board board;
    private ArrayList<Client> client;
    private ArrayList<Player> players;
    private Player player = new Player("Marco");
    private PlayerCommand command = GRAB_WEAPON;
    private PlayerController controller;
    private int random;

    private void initialize(/*, ArrayList<Client> client*/){
        this.board = new Board(8, BillboardGenerator.createBillboard(1));
        player = new Player("Marco");
        this.random = new Random().nextInt(3) + 1;
        //this.players = players;
        //this.client = client;
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This function creates GUI for main game and shows it
     * @param primaryStage stage
     * @throws FileNotFoundException if files are not found
     */
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        initialize();
        Pane pane = createGame(4);
        pane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setCenterShape(true);
        Scene scene = new Scene(pane);
        primaryStage.setTitle("Adrenaline");
        primaryStage.setScene(scene);
        primaryStage.setHeight(stageHeight);
        primaryStage.setWidth(stageWidth);
        primaryStage.show();

    }

    private String getString(int x, int y, int i){
        return board.getBillboard().getCellFromPosition(new Position(x,y)).getCard(i).stringGUI();
    }

    /**
     * This function creates GUI design and functions
     * @param number of the map
     * @return map GUI
     * @throws FileNotFoundException if files are not found
     */
    private Pane createGame(int number) throws FileNotFoundException {
        Pane anchor = new Pane();

        Pane board1 = createBoards();
        anchor.getChildren().add(board1);
        anchor.getChildren().get(0).setLayoutX(150);
        anchor.getChildren().get(0).setLayoutY(540);

        anchor.getChildren().add(createBoardRight());
        anchor.getChildren().get(1).setLayoutX(700);
        anchor.getChildren().get(1).setLayoutY(300);

        anchor.getChildren().add(createBoardLeft());
        anchor.getChildren().get(2).setLayoutX(-50);
        anchor.getChildren().get(2).setLayoutY(300);

        anchor.getChildren().add(createMap(number, board1));
        anchor.getChildren().get(3).setLayoutX(150);
        anchor.getChildren().get(3).setLayoutY(110);

        anchor.getChildren().add(createBoardMultiHigh());
        anchor.getChildren().get(4).setLayoutX(150);
        anchor.getChildren().get(4).setLayoutY(60);
        anchor.getChildren().get(4).setVisible(false);

        anchor.getChildren().add(createBoardMultiHigh());
        anchor.getChildren().get(5).setLayoutX(510);
        anchor.getChildren().get(5).setLayoutY(60);
        anchor.getChildren().get(5).setVisible(false);

        anchor.getChildren().add(createBoardHigh());
        anchor.getChildren().get(6).setLayoutX(310);
        anchor.getChildren().get(6).setLayoutY(0);
       // anchor.getChildren().get(6).setVisible(false);
        anchor.setCenterShape(true);
        return anchor;
    }

    /**
     * This function creates the map design
     * @param number of the map to be shown
     * @return map
     * @throws FileNotFoundException if files are not found
     */
    private Pane createMap(int number, Pane playerboard) throws FileNotFoundException {//TODO mancano teschi, pedine, bottoni e settare le giuste immagini
        StackPane map = new StackPane();

        //Card you want to see 0
        map.getChildren().add(tableWeaponCards(weaponPath("weaponCard"), 0, 115, 0));
        changeSizeImage((ImageView) map.getChildren().get(0), 200, 150);
        map.getChildren().get(0).setVisible(false);

        //red weapon 1-3
        for(int i = 0; i< 3; i++) {
            map.getChildren().add(generateCard(weaponPath(getString(1, 0, i)), 80, 55, -255, -30 + i * 65, 90));
            setAction((Button)map.getChildren().get(1+i), (ImageView) map.getChildren().get(0), 1, 0, i, playerboard);
        }
        //blue weapon 4-6
        for(int i = 0; i< 3; i++) {
            map.getChildren().add(generateCard(weaponPath(getString(0, 2, i)), 80, 55, 45 + i * 65, -170, 180));
            setAction((Button)map.getChildren().get(4+i), (ImageView) map.getChildren().get(0), 0,2, i, playerboard);
        }

        //yellow weapon 7-9
        for(int i =0; i< 3; i++) {
            map.getChildren().add(generateCard(weaponPath(getString(2, 3, i)), 80, 55, 255, 50 + i * 65, 270));
            setAction((Button)map.getChildren().get(7+i), (ImageView) map.getChildren().get(0), 2, 3, i, playerboard);
        }
        //deck not to be modified 10-11
        map.getChildren().add(tableWeaponCards(weaponPath("weaponCard"), 250, -55, 0));
        map.getChildren().add(tableWeaponCards(powerPath("powerCard"), 255, -160, 0));

        generateBoard(map, number);


return map;
    }

    /**
     * This function creates playerboard for the client running this GUI
     * @return playerboard of the player
     * @throws FileNotFoundException if files are not found
     */
    private Pane createBoards() throws FileNotFoundException {
        Pane playerBoard = new Pane();
        playerBoard.getChildren().add(playerBoard(playerBoardPath(), 110, 600, 0));

        for(int i = 0; i < 3; i++) {
            playerBoard.getChildren().add(generateCard(weaponPath("weaponCard"), 110, 80,80 - i * 40, 0, 0));
            playerBoard.getChildren().get(2*i+1).setVisible(false);
            playerBoard.getChildren().add(generateCard(powerPath("powerCard"), 110, 80,680 - i * 40, 0, 0));
            playerBoard.getChildren().get(2*i+2).setVisible(false);
        }

        return playerBoard;

    }

    /**
     * This function creates playerboard used by the right player
     * @return playerboard of the player
     * @throws FileNotFoundException if files are not found
     */
    private Pane createBoardRight() throws FileNotFoundException {
        Pane playerBoard = new Pane();
        playerBoard.getChildren().add(generateCard(playerBoardPath(), 75, 280, 0, 0, 270));

        for(int i = 0; i < 3; i++) {
            playerBoard.getChildren().add(generateCard(weaponPath("Electroscythe"), 75, 50, 190, -115+i*35, 270));
            playerBoard.getChildren().add(generateCard(powerPath("VENOMGRENADER"), 75, 50, 190, 115-i*35, 270));
        }

        return playerBoard;
    }

    /**
     * This function creates playerboard used by the left player
     * @return playerboard of the player
     * @throws FileNotFoundException if files are not found
     */
    private Pane createBoardLeft() throws FileNotFoundException {
        Pane playerBoard = new Pane();
        playerBoard.getChildren().add(generateCard(playerBoardPath(), 75, 280, 0, 0, 270));

        for(int i = 0; i < 3; i++) {
            playerBoard.getChildren().add(generateCard(weaponPath("Electroscythe"), 75, 50, 40, 115-i*35, 90));
            playerBoard.getChildren().add(generateCard(powerPath("VENOMGRENADER"), 75, 50, 40, -115+i*35, 90));
        }

        return playerBoard;
    }

    /**
     * This function creates playerboard in the high board of the GUI when there are 5 players playing
     * @return playerboard of the player
     * @throws FileNotFoundException if files are not found
     */
    private Pane createBoardMultiHigh() throws FileNotFoundException {
        Pane playerBoard = new Pane();
        playerBoard.getChildren().add(generateCard(playerBoardPath(), 50, 240, 0, 0, 180));

        for(int i = 0; i < 3; i++) {
            playerBoard.getChildren().add(generateCard(weaponPath("Electroscythe"), 75, 50, 70-i*35, -90, 90));
            playerBoard.getChildren().add(generateCard(powerPath("VENOMGRENADER"), 75, 50, 180-i*35, -90, 90));
        }

        return playerBoard;
    }

    /**
     * This function creates playerboard used by the player in the higher corner if there are 4 players
     * @return playerboard of the player
     * @throws FileNotFoundException if files are not found
     */
    private Pane createBoardHigh() throws FileNotFoundException {

        Pane playerBoard = new Pane();
        playerBoard.getChildren().add(generateCard(playerBoardPath(), 75, 280, 0, 0, 180));

        for(int i = 0; i < 3; i++) {
            playerBoard.getChildren().add(generateCard(weaponPath("Electroscythe"), 75, 50, -120+i*35, 0, 180));
            playerBoard.getChildren().add(generateCard(powerPath("VENOMGRENADER"), 75, 50, 350-i*35, 0, 180));
        }

return playerBoard;
    }

    /**
     * This function create an Image of an ammo card
     * @param path from which you pick the image of the ammo card
     * @param transX x position to set card
     * @param transY y position to set card
     * @return an image of the ammo card
     * @throws FileNotFoundException if file is not found
     */
    private ImageView ammoCards(String path, int transX, int transY) throws FileNotFoundException {
        ImageView ammo = new ImageView(new Image(new FileInputStream(path)));
        ammo.setFitHeight(25);
        ammo.setFitWidth(25);
        ammo.setTranslateX(transX);
        ammo.setTranslateY(transY);
        return ammo;
    }

    /**
     * This function create an Image of an weapon card
     * @param path from which you pick the image of the weapon card
     * @param transX x position to set card
     * @param transY y position to set card
     * @param grades to rotate cards
     * @return an Image of a weapon
     * @throws FileNotFoundException if file is not found
     */
    private ImageView tableWeaponCards(String path, int transX, int transY, int grades) throws FileNotFoundException {
        ImageView weaponImage = new ImageView(new Image(new FileInputStream(path)));
        weaponImage.setFitHeight(90);
        weaponImage.setFitWidth(60);
        weaponImage.setTranslateX(transX);
        weaponImage.setTranslateY(transY);
        weaponImage.rotateProperty().setValue(grades);
        return weaponImage;
    }

    /**
     * This function draws the playerboard of a player
     * @param path from which the image of playerboard is picked
     * @param height dimension of playerboard
     * @param width dimension of playerboard
     * @param grades to rotate playerboard
     * @return the image of the playerboard
     * @throws FileNotFoundException if file is not found
     */
    private ImageView playerBoard(String path, int height, int width, int grades) throws FileNotFoundException {
        ImageView playerBoardImage = new ImageView(new Image(new FileInputStream(path)));
        playerBoardImage.setFitHeight(height);
        playerBoardImage.setFitWidth(width);
        playerBoardImage.rotateProperty().setValue(grades);
        return playerBoardImage;
    }

    /**
     * This function returns the path of an ammo card image
     * @param string name
     * @return the path
     */
    private String ammoPath(String string){
        return "src/resources/images/gametable/ammo/" + string + ".png";
    }

    /**
     * This function returns the path of a weapon card image
     * @param string name
     * @return the path
     */
    private String weaponPath(String string){
        return "src/resources/images/gametable/weapons/" + string + ".png";
    }

    /**
     * This function returns the path of a power up card image
     * @param string name
     * @return the path
     */
    private String powerPath(String string){
        return "src/resources/images/gametable/power/" + string + ".png";
    }

    /**
     * This function returns the path of a playerboard image
     * @return the path
     */
    private String playerBoardPath(){
        return "src/resources/images/gametable/playerboard/yellowPlayerBoard.png";
    }

    /**
     * This function changes the dimension of an image
     * @param obj to change dimension
     * @param height new height
     * @param width new width
     */
    private void changeSizeImage(ImageView obj, int height, int width){
        obj.setFitHeight(height);
        obj.setFitWidth(width);
    }

    /**
     * This function changes the dimension of a button
     * @param obj to change dimension
     * @param height new height
     * @param width new width
     */
    private void changeSizeButton(Button obj, int height, int width){
        obj.setPrefHeight(height);
        obj.setPrefWidth(width);
    }

    /**
     * This function creates buttons to check or to use cards
     * @return ArrayList of buttons
     */
    private ArrayList<Button> cardViewButtonSet(){
        ArrayList<Button> buttons = new ArrayList<>();
        //no attacks
        buttons.add(createButton("viewcard", 200, 150, 0, 115));
        //one attack
        buttons.add(createButton("monoattack", 130, 150, 0, 150));
        //> 1 attack
        buttons.add(createButton("multiattack", 55, 150, 0, 130));
        //2 attacks
        buttons.add(createButton("biAttack", 55, 150, 0, 185));
        //3 attacks
        buttons.add(createButton("triAttack1", 55, 75, -40, 185));
        buttons.add(createButton("triAttack2", 55, 75, 40, 185));

        return buttons;
    }

    /**
     * This function creates buttons
     * @param string name
     * @param height height
     * @param width width
     * @param transX x position
     * @param transY y position
     * @return a new button
     */
    private Button createButton(String string, int height, int width, int transX, int transY){
        Button button = new Button(string);
        changeSizeButton(button, height, width);
        button.setTranslateX(transX);
        button.setTranslateY(transY);
        button.setOpacity(0.5);

        return button;
    }

    private ArrayList<Button> actionButtons(){
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(createButton("move", 8, 40, 0,15));
        buttons.add(createButton("move", 8, 40, 0,28));
        buttons.add(createButton("move", 8, 40, 0,40));
        buttons.add(createButton("move", 8, 40, 0,70));
        buttons.add(createButton("move", 8, 40, 0,85));
        return buttons;
    }

    private Button generateCard(String name, int height, int width, int transX, int transY, int grades) throws FileNotFoundException {

        Button button = new Button();
        changeSizeButton(button, height, width);
        button.rotateProperty().setValue(grades);

        button.setOpacity(1);
        ImageView image = new ImageView(new Image(new FileInputStream(name)));
        changeSizeImage(image, height, width);
        button.setGraphic(image);

        button.setPadding(Insets.EMPTY);
        button.setTranslateX(transX);
        button.setTranslateY(transY);

        return button;
    }

    //prima la carta da vedere, secondo è posizione dove verrà messa
    private void setAction(Button buttonCard, ImageView image, int x, int y, int pos, Pane playerboard){//carte della mappa posso guardarle o pescarle
        buttonCard.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switch (command) {
                    case CHOOSE_ACTION: //guarda la carta
                        changeGraphics(buttonCard, image);
                        image.setVisible(true);
                        command = UNSELECT;
                        break;
                    case UNSELECT:
                        image.setVisible(false);
                        command = CHOOSE_ACTION;
                        break;
                    case GRAB_WEAPON://scegliere la carta da prendere, non serve il riferimento alle altre carte perchè lo prendo dal player
                        if(!getPlayer().canPay(toIntArray(getWeapon(x, y, pos).getGrabCost())))
                            buttonCard.disabledProperty();
                        else if(getPlayer().getWeapons().size() <= 3){
                            getPlayer().addWeapon((WeaponCard) board.getBillboard().getCellFromPosition(new Position(x, y)).getCard(pos));
                            System.out.print("funziona");
                            changeImage((Button)playerboard.getChildren().get(3), image);
                            image.setVisible(false);
                            buttonCard.setVisible(false);
                            //turno ++
                        }
                            else command = DISCARD_WEAPON;
                        break;
                    default: buttonCard.disabledProperty();

                }
            }
        });
    }

    private void playerboardCardAction(Button card, ImageView image){
        card.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switch (command) {
                    case MOVE: //guarda la carta

                        break;
                    case END_TURN://carta da usare
                        Node cardNode = card.getGraphic();
                        ImageView image = (ImageView) cardNode;
                        image.setImage(image.getImage());
                        //verifica se è weapon o ammo, se ammo la può usare subito

                        break;
                    default: card.setVisible(false);

                }
            }
        });
    }

    private void setMovePlayer(Button action, PlayerCommand order){
        action.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //verifica se è il proprio turno
                if(command == CHOOSE_ACTION){
                    //If per la frenzy
                    command = order;
                    action.setVisible(false);
                }
                else action.disabledProperty();
            }
        });
    }

    private void setCellAction(Button cell, int x, int y, Optional<ImageView> ammo){
        cell.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switch (command) {
                    case MOVE:
                        if(board.getBillboard().canMove(player.getCell(), board.getBillboard().getCellFromPosition(new Position(x, y)), 3)) {
                            cell.setVisible(true);
                            player.setPawnCell(board.getBillboard().getCellFromPosition(new Position(x, y)));
                            //cambio posizione pedina
                            //CHOOSE ACTION o END TURN

                        }

                    /*case FRENZY_MOVE:
                        if(board.getBillboard().canMove(player.getCell(), board.getBillboard().getCellFromPosition(new Position(x, y)), 4)) {
                            cell.setVisible(true);
                            player.setPawnCell(board.getBillboard().getCellFromPosition(new Position(x, y)));
                            //cambio posizione pedina
                            //CHOOSE ACTION o END TURN

                        }*/

                        break;
                    case GRAB_MOVE://scegliere la carta da prendere
                        if(board.getBillboard().canMove(player.getCell(), board.getBillboard().getCellFromPosition(new Position(x, y)), 2)) {
                            cell.setVisible(true);
                            player.setPawnCell(board.getBillboard().getCellFromPosition(new Position(x, y)));
                            if(ammo.isPresent()) {
                                ammo.get().setVisible(false);
                            }

                            //da ammo e power up
                            //CHOOSE ACTION o END TURN
                        }
                    case SHOOT_MOVE:
                        if(board.getBillboard().canMove(player.getCell(), board.getBillboard().getCellFromPosition(new Position(x, y)), 2)) {
                            cell.setVisible(true);
                            player.setPawnCell(board.getBillboard().getCellFromPosition(new Position(x, y)));
                            command = SHOOT;
                        }

                        break;
                    default: cell.setVisible(false);

                }

            }

        });
    }
    private void setPowerUp(Button attack, ArrayList<Button> Buttons){
        attack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switch (command){
                    case CHOOSE_ACTION:
                        if(getPlayer().getPowerups().get(1).getPowerUp() == TELEPORTER || getPlayer().getPowerups().get(1).getPowerUp() == KINETICRAY)

                            //else attack.disableProperty();
                        break;
                    case SHOOT://after shooting
                        if(getPlayer().getPowerups().get(1).getPowerUp() == GUNSIGHT && getPlayer().getBullets().size() > 0);

                        break;
                    case END_TURN:// after getting hit
                        if(getPlayer().getPowerups().get(1).getPowerUp() == VENOMGRENADE);
                        break;
                    default:
                        attack.disableProperty();
                }
            }
        });
    }


    private void changeGraphics(Button button, ImageView image){
        Node node = button.getGraphic();
        ImageView imageView = (ImageView) node;
        image.setImage(imageView.getImage());
    }

    private void changeImage(Button button, ImageView image){
        button.setGraphic(image);
        button.setVisible(true);
    }

    private Player getPlayer(){return this.player;}

    private WeaponCard getWeapon(int x, int y, int pos){
        return (WeaponCard) board.getBillboard().getCellFromPosition(new Position(x, y)).getCard(pos);
    }

    private void generateBoard(Pane map, int number) throws FileNotFoundException {
        ImageView mapImage = new ImageView(new Image(new FileInputStream("src/resources/images/gametable/map/board"+number+".png")));
        changeSizeImage(mapImage, 430, 600);
        map.getChildren().add(mapImage);
        map.getChildren().get(12).toBack();
        switch (number){
            case 1:
                generateBoardLeft1(map);
                generateBoardRight1(map);//8
                generateBoardButtonsLeft1(map);
                generateBoardButtonsRight1(map);//11
                for(int x = 0; x < 3; x++)
                    for(int y = 0; y < 4; y++) {
                        if((x == 0 && y == 1) || (x == 0 && y == 1) || (x == 0 && y == 1))
                        setCellAction((Button) map.getChildren().get(20), x, y, Optional.of((ImageView) map.getChildren().get(0)));
                    }
                break;
            case 2:
                generateBoardLeft1(map);
                generateBoardRight2(map);
                generateBoardButtonsLeft2(map);
                generateBoardButtonsRight1(map);
                for(int i = 13; i < 23; i++)
                    setCellAction((Button) map.getChildren().get(0), 1, 1, Optional.of((ImageView) map.getChildren().get(0)));
                break;
            case 3:
                generateBoardLeft2(map);
                generateBoardRight1(map);
                generateBoardButtonsLeft1(map);
                generateBoardButtonsRight2(map);
                for(int i = 13; i < 25; i++)
                    setCellAction((Button) map.getChildren().get(0), 1, 1, Optional.of((ImageView) map.getChildren().get(0)));
                break;
            case 4:
                generateBoardLeft2(map);
                generateBoardRight2(map);
                generateBoardButtonsLeft2(map);
                generateBoardButtonsRight2(map);
                for(int i = 13; i < 24; i++)
                    setCellAction((Button) map.getChildren().get(0), 1, 1, Optional.of((ImageView) map.getChildren().get(0)));
                break;
        }
    }

    private void generateBoardLeft1(Pane map) throws FileNotFoundException {
        //first line 13-16
        map.getChildren().add(ammoCards(ammoPath(getString(0, 0, 0)), -170, -100));

        map.getChildren().add(ammoCards(ammoPath(getString(0, 1, 0)), -75, -50));
        generateBoardCloned(map);

    }

    private void generateBoardRight1(Pane map) throws FileNotFoundException {
        //first line 17/18-20/21
        map.getChildren().add(ammoCards(ammoPath(getString(1, 2, 0)), 35, 48));
        map.getChildren().add(ammoCards(ammoPath(getString(2, 2, 0)), 40, 128));

        map.getChildren().add(ammoCards(ammoPath(getString(0, 3, 0)), 170, -52));
        map.getChildren().add(ammoCards(ammoPath(getString(1, 3, 0)), 125, 48));

    }

    private void generateBoardLeft2(Pane map) throws FileNotFoundException {
        //first line 13-17
        map.getChildren().add(ammoCards(ammoPath(getString(0, 0, 0)), -162, -52));
        map.getChildren().add(ammoCards(ammoPath(getString(2, 0, 0)), -162, 128));

        map.getChildren().add(ammoCards(ammoPath(getString(0, 1, 0)), -70, -100));
        generateBoardCloned(map);

    }

    private void generateBoardRight2(Pane map) throws FileNotFoundException {
        //17/18-20/21
        map.getChildren().add(ammoCards(ammoPath(getString(1, 2, 0)), 35, 30));
        map.getChildren().add(ammoCards(ammoPath(getString(2, 2, 0)), 25, 130));
        map.getChildren().add(ammoCards(ammoPath(getString(1, 3, 0)), 170, 30));

    }

    private void generateBoardCloned(Pane map) throws FileNotFoundException {
        map.getChildren().add(ammoCards(ammoPath(getString(1, 1, 0)), -80, 20));
        map.getChildren().add(ammoCards(ammoPath(getString(2, 1, 0)), -80, 128));
    }

    private void generateBoardButtonsLeft1(Pane map) throws FileNotFoundException {

        map.getChildren().add(createButton("c00",90, 105, -150, -80));
        map.getChildren().add(createButton("c01",100, 100, -150, 20));
        map.getChildren().add(createButton("c10",90, 95, -45, -80));
        map.getChildren().add(createButton("c11",100, 95, -50, 20));
        map.getChildren().add(createButton("c12",90, 105, -60, 120));

    }

    private void generateBoardButtonsRight1(Pane map) throws FileNotFoundException {

        map.getChildren().add(createButton("c02",90, 105, 55, -80));
        map.getChildren().add(createButton("c12",100, 95, 55, 20));
        map.getChildren().add(createButton("c22",105, 95, 55, 120));
        map.getChildren().add(createButton("c03",90, 95, 155, -80));
        map.getChildren().add(createButton("c13",100, 95, 155, 20));
        map.getChildren().add(createButton("c23",105, 95, 155, 120));

    }

    private void generateBoardButtonsLeft2(Pane map) throws FileNotFoundException {

        map.getChildren().add(createButton("c00",90, 95, -160, -80));
        map.getChildren().add(createButton("c01",100, 90, -160, 20));
        map.getChildren().add(createButton("c02",90, 105, -155, 125));
        map.getChildren().add(createButton("c10",90, 105, -55, -80));
        map.getChildren().add(createButton("c11",100, 105, -60, 20));
        map.getChildren().add(createButton("c12",90, 105, -50, 125));

    }

    private void generateBoardButtonsRight2(Pane map) throws FileNotFoundException {

        map.getChildren().add(createButton("c02",90, 105, 55, -80));
        map.getChildren().add(createButton("c12",100, 115, 50, 20));
        map.getChildren().add(createButton("c22",90, 100, 55, 125));
        map.getChildren().add(createButton("c13",100, 95, 155, 20));
        map.getChildren().add(createButton("c23",100, 95, 155, 120));
    }
}

