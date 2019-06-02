package view;

import board.Board;
import board.billboard.Billboard;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

//TODO finire le playerboard in alto, ridimensionare carte ai lati, aggiungere i nomi, aggiungere i bottoni
public class BoardViewGameGUI extends Application {
    private int stageHeight = 700;
    private int stageWidth = 920;
    /*
    private Board board;

    BoardViewGameGUI(Board board){
        this.board = board;
    }*/

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
        AnchorPane pane = createGame(1);
        pane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setCenterShape(true);
        Scene scene = new Scene(pane);
        primaryStage.setTitle("Adrenaline");
        primaryStage.setScene(scene);
        primaryStage.setHeight(stageHeight);
        primaryStage.setWidth(stageWidth);
        primaryStage.show();
    }

    /**
     * This function creates GUI design and functions
     * @param number of the map
     * @return map GUI
     * @throws FileNotFoundException if files are not found
     */
    private AnchorPane createGame(int number) throws FileNotFoundException {
        AnchorPane anchor = new AnchorPane();
        anchor.getChildren().add(createMap(number));
        anchor.getChildren().get(0).setLayoutX(150);
        anchor.getChildren().get(0).setLayoutY(110);

        anchor.getChildren().add(createBoards());
        anchor.getChildren().get(1).setLayoutX(150);
        anchor.getChildren().get(1).setLayoutY(540);

        anchor.getChildren().add(createBoardRight());
        anchor.getChildren().get(2).setLayoutX(650);
        anchor.getChildren().get(2).setLayoutY(300);

        anchor.getChildren().add(createBoardLeft());
        anchor.getChildren().get(3).setLayoutX(-25);
        anchor.getChildren().get(3).setLayoutY(300);

        anchor.getChildren().add(createBoardMultiHigh());
        anchor.getChildren().get(4).setLayoutX(150);
        anchor.getChildren().get(4).setLayoutY(60);
        anchor.getChildren().get(4).setVisible(false);

        anchor.getChildren().add(createBoardMultiHigh());
        anchor.getChildren().get(5).setLayoutX(510);
        anchor.getChildren().get(5).setLayoutY(60);
        anchor.getChildren().get(5).setVisible(false);

        anchor.getChildren().add(createBoardHigh());
        anchor.getChildren().get(6).setLayoutX(300);
        anchor.getChildren().get(6).setLayoutY(35);
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
    private Pane createMap(int number) throws FileNotFoundException {//TODO mancano teschi, pedine, bottoni e settare le giuste immagini
        Pane map = new StackPane();
        ImageView mapImage = new ImageView(new Image(new FileInputStream("src/resources/images/gametable/map" + number + ".png")));
        mapImage.setFitHeight(430);
        mapImage.setFitWidth(600);
        map.getChildren().add(mapImage);
        //first line
        map.getChildren().add(ammoCards(ammoPath(1), -162, -52));
        map.getChildren().add(ammoCards(ammoPath(1), -70, -100));
        map.getChildren().add(ammoCards(ammoPath(1), 170, -52));
        //second line

        map.getChildren().add(ammoCards(ammoPath(1), -80, 20));
        map.getChildren().add(ammoCards(ammoPath(1), 35, 48));
        map.getChildren().add(ammoCards(ammoPath(1), 125, 48));
        //third line
        map.getChildren().add(ammoCards(ammoPath(1), -162, 128));
        map.getChildren().add(ammoCards(ammoPath(1), -80, 128));
        map.getChildren().add(ammoCards(ammoPath(1), 40, 128));

        //red weapon
        map.getChildren().add(tableWeaponCards(weaponPath(1), -255, -30, 90));
        map.getChildren().add(tableWeaponCards(weaponPath(1), -255, 35,90 ));
        map.getChildren().add(tableWeaponCards(weaponPath(1), -255, 100,90 ));
        //blue weapon
        map.getChildren().add(tableWeaponCards(weaponPath(1), 45, -170, 180));
        map.getChildren().add(tableWeaponCards(weaponPath(1), 110, -170, 180));
        map.getChildren().add(tableWeaponCards(weaponPath(1), 175, -170, 180));
        //yellow weapon
        map.getChildren().add(tableWeaponCards(weaponPath(1), 255, 50,270));
        map.getChildren().add(tableWeaponCards(weaponPath(1), 255, 115, 270));
        map.getChildren().add(tableWeaponCards(weaponPath(1), 255, 180, 270));
        //deck
        map.getChildren().add(tableWeaponCards(weaponPath(1), 250, -55,0));
        map.getChildren().add(tableWeaponCards(powerPath(0), 255, -160,0));

        //Card you want to see
        map.getChildren().add(tableWeaponCards(weaponPath(1), 0, 115,0));
        changeSizeImage((ImageView)map.getChildren().get(21),200, 150);
        //cell buttons
        for(Button buttons: cellButtonSet())
            map.getChildren().add(buttons);
        for(Button buttons: cardMapButtonSet())
            map.getChildren().add(buttons);
        for(Button buttons: cardViewButtonSet())
            map.getChildren().add(buttons);

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

        playerBoard.getChildren().add(tableWeaponCards(weaponPath(1), 680, 0, 0));
        playerBoard.getChildren().add(tableWeaponCards(weaponPath(1), 640, 0, 0));
        playerBoard.getChildren().add(tableWeaponCards(weaponPath(1), 600, 0,0));

        playerBoard.getChildren().add(tableWeaponCards(powerPath(0), -160, 0,0));
        playerBoard.getChildren().add(tableWeaponCards(powerPath(0), -120, 0,0));
        playerBoard.getChildren().add(tableWeaponCards(powerPath(0), -80, 0,0));
        for(int i = 1; i < 7; i++)
            changeSizeImage((ImageView)playerBoard.getChildren().get(i),110, 80);
        for(Button buttons: cardBoardButtonSet(600, 0,110, 80, 40, 0))
            playerBoard.getChildren().add(buttons);
        for(Button buttons: actionButtons())
            playerBoard.getChildren().add(buttons);
        return playerBoard;

    }

    /**
     * This function creates playerboard used by the right player
     * @return playerboard of the player
     * @throws FileNotFoundException if files are not found
     */
    private Pane createBoardRight() throws FileNotFoundException {
        Pane playerBoard = new Pane();
        playerBoard.getChildren().add(playerBoard(playerBoardPath(), 75, 280, 270));
        playerBoard.getChildren().add(tableWeaponCards(weaponPath(1), 190, -115,270));
        playerBoard.getChildren().add(tableWeaponCards(weaponPath(1), 190, -80, 270));
        playerBoard.getChildren().add(tableWeaponCards(weaponPath(1), 190, -45, 270));
        playerBoard.getChildren().add(tableWeaponCards(powerPath(0), 190, 115,270));
        playerBoard.getChildren().add(tableWeaponCards(powerPath(0), 190, 80,270));
        playerBoard.getChildren().add(tableWeaponCards(powerPath(0), 190, 45,270));

        for(int i = 1; i < 7; i++)
            changeSizeImage((ImageView)playerBoard.getChildren().get(i),75, 50);
        for(Button buttons: cardBoardButtonSet(280,75, 75, 50, 35, 270))
            playerBoard.getChildren().add(buttons);
        playerBoard.getChildren().add(createButton("board2",75,280,0, 0));
        playerBoard.getChildren().get(13).rotateProperty().setValue(270);
        return playerBoard;
    }

    /**
     * This function creates playerboard used by the left player
     * @return playerboard of the player
     * @throws FileNotFoundException if files are not found
     */
    private Pane createBoardLeft() throws FileNotFoundException {
        Pane playerBoard = new Pane();
        playerBoard.getChildren().add(playerBoard(playerBoardPath(), 75, 280, 90));
        playerBoard.getChildren().add(tableWeaponCards(weaponPath(1), 40, 115,90));
        playerBoard.getChildren().add(tableWeaponCards(weaponPath(1), 40, 80, 90));
        playerBoard.getChildren().add(tableWeaponCards(weaponPath(1), 40, 45, 90));
        playerBoard.getChildren().add(tableWeaponCards(powerPath(0), 40, -115,90));
        playerBoard.getChildren().add(tableWeaponCards(powerPath(0), 40, -80,90));
        playerBoard.getChildren().add(tableWeaponCards(powerPath(0), 40, -45,90));
        for(int i = 1; i < 7; i++)
            changeSizeImage((ImageView)playerBoard.getChildren().get(i),75, 50);
        for(Button buttons: cardBoardButtonSet(280,75, 75, 50, 35, 90))
            playerBoard.getChildren().add(buttons);
        playerBoard.getChildren().add(createButton("board3",75,280,0, 0));
        playerBoard.getChildren().get(13).rotateProperty().setValue(90);
        return playerBoard;
    }

    /**
     * This function creates playerboard in the high board of the GUI when there are 5 players playing
     * @return playerboard of the player
     * @throws FileNotFoundException if files are not found
     */
    private Pane createBoardMultiHigh() throws FileNotFoundException {
        Pane playerBoard = new Pane();
        playerBoard.getChildren().add(playerBoard(playerBoardPath(), 50, 240, 180));
        playerBoard.getChildren().add(tableWeaponCards(weaponPath(1), 70, -90, 180));
        playerBoard.getChildren().add(tableWeaponCards(weaponPath(1), 35, -90, 180));
        playerBoard.getChildren().add(tableWeaponCards(weaponPath(1), 0, -90,180));

        playerBoard.getChildren().add(tableWeaponCards(powerPath(0), 180, -90,180));
        playerBoard.getChildren().add(tableWeaponCards(powerPath(0), 145, -90,180));
        playerBoard.getChildren().add(tableWeaponCards(powerPath(0), 110, -90,180));
        for(int i = 1; i < 7; i++)
            changeSizeImage((ImageView)playerBoard.getChildren().get(i),75, 50);
        for(Button buttons: cardBoardButtonSet(280, 0,75, 50, 35, 180))
            playerBoard.getChildren().add(buttons);
        playerBoard.getChildren().add(createButton("board4/5",50,240,0, 0));
        return playerBoard;
    }

    /**
     * This function creates playerboard used by the player in the higher corner if there are 4 players
     * @return playerboard of the player
     * @throws FileNotFoundException if files are not found
     */
    private Pane createBoardHigh() throws FileNotFoundException {
        Pane playerBoard = new Pane();
        playerBoard.getChildren().add(playerBoard(playerBoardPath(), 75, 280, 180));

        playerBoard.getChildren().add(tableWeaponCards(weaponPath(1), -120, 0, 180));
        playerBoard.getChildren().add(tableWeaponCards(weaponPath(1), -85, 0, 180));
        playerBoard.getChildren().add(tableWeaponCards(weaponPath(1), -50, 0,180));
        playerBoard.getChildren().add(tableWeaponCards(powerPath(0), 350, 0,180));
        playerBoard.getChildren().add(tableWeaponCards(powerPath(0), 315, 0,180));
        playerBoard.getChildren().add(tableWeaponCards(powerPath(0), 280, 0,180));
        for(int i = 1; i < 7; i++)
            changeSizeImage((ImageView)playerBoard.getChildren().get(i),75, 50);
        for(Button buttons: cardBoardButtonSet(280, 0,75, 50, 35, 180))
            playerBoard.getChildren().add(buttons);
        playerBoard.getChildren().add(createButton("board4/5",75,280,0, 0));
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
     * @param number
     * @return the path
     */
    private String ammoPath(int number){
        return "src/resources/images/gametable/ammo" + number + ".png";
    }

    /**
     * This function returns the path of a weapon card image
     * @param number
     * @return the path
     */
    private String weaponPath(int number){
        return "src/resources/images/gametable/weapon" + number + ".png";
    }

    /**
     * This function returns the path of a power up card image
     * @param number
     * @return the path
     */
    private String powerPath(int number){
        return "src/resources/images/gametable/power" + number + ".png";
    }

    /**
     * This function returns the path of a playerboard image
     * @return the path
     */
    private String playerBoardPath(){
        return "src/resources/images/gametable/robotfrenzyboard.png";
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
     * This function creates buttons for actions in cells of the map
     * @return ArrayList of buttons
     */
    private ArrayList<Button> cellButtonSet(){
        ArrayList<Button> buttons = new ArrayList<>();
        for(int column = 0; column < 4; column++)
            for(int line = 0; line < 3; line++)
                buttons.add(createButton("c" + line + column, 90, 105, -150+ column * 105, -80 + line*95));

        return buttons;
    }

    /**
     * This function creates buttons for cards in the map
     * @return ArrayList of buttons
     */
    private ArrayList<Button> cardMapButtonSet(){
        ArrayList<Button> buttons = new ArrayList<>();
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++){
                buttons.add(createButton("card" + i*j, 90, 60, 0,0));
            }

        //red cards
        for(int i = 0; i < 3; i++) {
            buttons.get(i).setTranslateX(-255);
            buttons.get(i).setTranslateY(-30 + 65*i);
            buttons.get(i).rotateProperty().setValue(90);
        }
        //blue cards
        for(int i = 3; i < 6; i++){
            buttons.get(i).setTranslateX(45 + 65*(i-3));
            buttons.get(i).setTranslateY(-170);
            buttons.get(i).rotateProperty().setValue(180);
            }
        //yellow cards
        for(int i = 6; i < 9; i++){
            buttons.get(i).setTranslateX(255);
            buttons.get(i).setTranslateY(50 + 65*(i-6));
            buttons.get(i).rotateProperty().setValue(270);
        }
        return buttons;
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
        button.setOpacity(0);
        button.setVisible(false);
        return button;
    }

    /**
     * This function creates buttons for cards players have
     * @param boardWidth
     * @param boardHeight
     * @param height
     * @param width
     * @param distance
     * @param grades
     * @return
     */
    private ArrayList<Button> cardBoardButtonSet(int boardWidth, int boardHeight, int height, int width, int distance, int grades){
        ArrayList<Button> buttons = new ArrayList<>();
        if(grades == 0 || grades == 180){
        for(int i = 0; i < 3; i++){
            buttons.add(createButton("powerup" + i, height, width, -width -(2-i)*distance, boardHeight));
        }
        for(int i = 0; i < 3; i++){
            buttons.add(createButton("attack" + i, height, width, boardWidth+ (2-i)*distance, boardHeight));
        }}

        else if(grades == 270){
        for(int i = 0; i < 3; i++){
            buttons.add(createButton("powerup" + i, height, width, 190, distance + 10 + (2-i)*distance));
        }
        for(int i = 0; i < 3; i++){
            buttons.add(createButton("attack" + i, height, width, 190, -distance - 10 - (2-i)*distance));
        }}

        else
        {
            for(int i = 0; i < 3; i++){
                buttons.add(createButton("powerup" + i, height, width, width-10, -distance - 10 - (2-i)*distance));
            }
            for(int i = 0; i < 3; i++){
                buttons.add(createButton("attack" + i, height, width, width-10, distance + 10 + (2-i)*distance));
            }}

        for(Button button: buttons) {
            button.rotateProperty().setValue(grades);
        }
        return buttons;
    }

    private ArrayList<Button> actionButtons(){
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(createButton("move", 8, 0, 30,15));
        buttons.add(createButton("move", 8, 0, 30,28));
        buttons.add(createButton("move", 8, 0, 30,40));
        buttons.add(createButton("move", 8, 0, 30,70));
        buttons.add(createButton("move", 8, 0, 30,85));
        return buttons;
    }

}
