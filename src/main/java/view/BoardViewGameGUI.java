package view;

import attack.Attack;
import board.*;
import board.billboard.BillboardGenerator;
import client.Client;

import constants.Constants;
import controller.CommandObj;
import controller.EnumCommand;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import player.Player;
import powerup.PowerCard;
import powerup.PowerUp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.*;

import static constants.Color.convertToColor;
import static controller.EnumCommand.*;
import static deck.Bullet.*;
import static powerup.PowerUp.*;
import static powerup.PowerUp.KINETICRAY;
import static powerup.PowerUp.TELEPORTER;
import static powerup.PowerUp.VENOMGRENADE;

public class BoardViewGameGUI extends Application implements View {
    private int stageHeight = 700;
    private int stageWidth = 980;
    private Board board;
    private Client client;
    private List<Player> players = new ArrayList<>();
    private Player player = new Player("Marco");
    private EnumCommand command = CHOOSE_ACTION;
    private int numBoard = 2; //TODO settarlo a -1 fino a quando board non viene settata
    private int index = 0;
    private Object object;
    private Pane root;
    private int maxTargets;
    private Object targets;
    private Object chosenTargets;

    public BoardViewGameGUI(){

    }

    //TODO manca board
    public BoardViewGameGUI(Client client) throws RemoteException {
        this.client = client;
        this.player = client.getPlayer();
        this.players = client.getListOfPlayers();
    }

    private void initialize(/*, ArrayList<Client> client*/) {
        this.board = new Board(8, BillboardGenerator.createBillboard(numBoard));
        player.setPawnCell(board.getBillboard().getCellFromPosition(new Position(1, 1)));
        players.add(player);
        players.add(new Player("numBoard"));
        players.add(new Player("prova"));
        players.add(new Player("Marco"));
        players.add(new Player("tizio"));
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This function creates GUI for main game and shows it
     *
     * @param primaryStage stage
     * @throws FileNotFoundException if files are not found
     */
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException{
        initialize();
        root = createGame(numBoard);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        root.setCenterShape(true);
        Scene scene = new Scene(root);
        primaryStage.setTitle("Adrenaline");
        primaryStage.setScene(scene);
        primaryStage.setHeight(stageHeight);
        primaryStage.setWidth(stageWidth);
        primaryStage.show();
    }

    public Scene createScene(Client client) throws FileNotFoundException, InterruptedException {
        while(numBoard == -1) {
            wait();
        }
        initialize();
        root = createGame(numBoard);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        root.setCenterShape(true);
        return new Scene(root);
    }

    /**
     * This function returns the height of the scene
     * @return scene's height
     */
    public int getHeight(){
        return this.stageHeight;
    }

    /**
     * This function returns the width of the scene
     * @return scene's width
     */
    public int getWidth(){
        return this.stageWidth;
    }

    /**
     * This function returns the path of a weapon in a Regeneration cell
     * @param x coordinate
     * @param y coordinate
     * @param i number of weapon
     * @return string
     */
    private String getString(int x, int y, int i) {
        return board.getBillboard().getCellFromPosition(new Position(x, y)).getCard(i).stringGUI();
    }

    /**
     * This function creates GUI design and functions
     *
     * @param number of the map
     * @return map GUI
     * @throws FileNotFoundException if files are not found
     */
    private Pane createGame(int number) throws FileNotFoundException {
        Pane anchor = new Pane();

        Pane board1 = createMainPlayerboard();
        anchor.getChildren().add(board1);
        anchor.getChildren().get(0).setLayoutX(0);
        anchor.getChildren().get(0).setLayoutY(430);

        for(int i = 1; i < players.size(); i++) {
            anchor.getChildren().add(createOtherBoards(i));
            anchor.getChildren().get(i).setLayoutX(610);
            anchor.getChildren().get(i).setLayoutY(165*(i-1));
        }

        anchor.getChildren().add(askPane());
        anchor.getChildren().get(players.size()).setLayoutX(220);
        anchor.getChildren().get(players.size()).setLayoutY(230);
        anchor.getChildren().get(players.size()).toFront();
        anchor.getChildren().get(players.size()).setVisible(false);

        anchor.getChildren().add(createMap(number, board1));
        anchor.getChildren().get(players.size() + 1).setLayoutX(0);
        anchor.getChildren().get(players.size() + 1).setLayoutY(0);
        anchor.getChildren().get(players.size() + 1).toBack();

        anchor.getChildren().add(timer());
        anchor.getChildren().get(players.size() + 2).setLayoutY(380);
        anchor.setCenterShape(true);

        anchor.getChildren().add(points());
        anchor.getChildren().get(players.size() + 3).setLayoutY(630);
        anchor.getChildren().get(players.size() + 3).setVisible(true);
        anchor.setCenterShape(true);

        return anchor;
    }

    /**
     * This function creates a pane for showing points
     * @return a pane
     */
    private Pane points() {

        GridPane points = new GridPane();
        points.add(new Label("Points:"), 0, 0);
        ((Label)(points.getChildren().get(0))).setTextFill(Color.WHITE);
        for(int i = 0; i < players.size(); i++){
            points.add(new Label(getPlayerboardPlayer(i).getName() + ": " + getPlayerboardPlayer(i).getPoints() + "  "), i, 1);
            if(i == 0)
                ((Label)(points.getChildren().get(1+i))).setTextFill(Color.YELLOW);
            if(i == 1)
                ((Label)(points.getChildren().get(1+i))).setTextFill(Color.GRAY);
            if(i == 2)
                ((Label)(points.getChildren().get(1+i))).setTextFill(Color.PURPLE);
            if(i == 3)
                ((Label)(points.getChildren().get(1+i))).setTextFill(Color.LIGHTBLUE);
            if(i == 4)
                ((Label)(points.getChildren().get(1+i))).setTextFill(Color.GREEN);
        }
        return points;
    }

    /**
     * This function creates the map design
     *
     * @param number of the map to be shown //TODO rimuoverlo
     * @return map
     * @throws FileNotFoundException if files are not found
     */
    private Pane createMap(int number, Pane playerboard) throws FileNotFoundException {
        StackPane map = new StackPane();

        //Card you want to see 0
        map.getChildren().add(tableWeaponCards(weaponPath("weaponCard"), 0, 115));
        changeSizeImage((ImageView) map.getChildren().get(0), 200, 150);
        map.getChildren().get(0).setVisible(false);
        map.getChildren().get(0).toFront();

        //red weapon 1-3
        for (int i = 0; i < 3; i++) {
            if (i < ((RegenerationCell) board.getBillboard().getCellFromPosition(new Position(1, 0))).getCards().size())
                map.getChildren().add(generateCard(weaponPath(getString(1, 0, i)), 80, 55, -260, -30 + i * 60, 90));
            else {
                map.getChildren().add(generateCard(weaponPath("weaponCard"), 80, 55, -260, -30 + i * 60, 90));
                map.getChildren().get(i + 1).setVisible(false);
            }
            setMapCardActions((Button) map.getChildren().get(1 + i), (ImageView) map.getChildren().get(0), 1, 0, i, playerboard, map);
        }
        //blue weapon 4-6
        for (int i = 0; i < 3; i++) {
            if (i < ((RegenerationCell) board.getBillboard().getCellFromPosition(new Position(0, 2))).getCards().size())
                map.getChildren().add(generateCard(weaponPath(getString(0, 2, i)), 80, 55, 45 + i * 65, -175, 180));
            else {
                map.getChildren().add(generateCard(weaponPath("weaponCard"), 80, 55, 45 + i * 65, -175, 180));
                map.getChildren().get(i + 4).setVisible(false);
            }
            setMapCardActions((Button) map.getChildren().get(4 + i), (ImageView) map.getChildren().get(0), 0, 2, i, playerboard, map);
        }

        //yellow weapon 7-9
        for (int i = 0; i < 3; i++) {
            if (i < ((RegenerationCell) board.getBillboard().getCellFromPosition(new Position(2, 3))).getCards().size())
                map.getChildren().add(generateCard(weaponPath(getString(2, 3, i)), 80, 55, 255, 60 + i * 60, 270));
            else {
                map.getChildren().add(generateCard(weaponPath("weaponCard"), 80, 55, 255, 60 + i * 60, 270));
                map.getChildren().get(i + 7).setVisible(false);
            }
            setMapCardActions((Button) map.getChildren().get(7 + i), (ImageView) map.getChildren().get(0), 2, 3, i, playerboard, map);
        }
        //deck not to be modified 10-11
        map.getChildren().add(tableWeaponCards(weaponPath("weaponCard"), 250, -55));
        map.getChildren().add(tableWeaponCards(powerPath("powerCard"), 255, -160));

        ArrayList<Button> actionButton = actionButtons();
        generateBoard(map, number, actionButton);
        for (int i = 0; i < players.size(); i++) {
            map.getChildren().add(generateCard(pawnPath(giveColor(getPlayerboardPlayer(i))), 20, 20, -100, -150, 0));
            pawnAction((Button)map.getChildren().get(map.getChildren().size()-board.getSkulls()), getPlayerboardPlayer(i));
        }


        for (int i = 0; i < board.getSkulls(); i++)
            map.getChildren().add(addSkull(40, -245 + i * 25, -180));

        //Commands for player
        for(Button buttons : actionButton)
            playerboard.getChildren().add(buttons);
        for (int i = 0; i < 3; i = i + 2)
            setPowerUp((Button) playerboard.getChildren().get(2 + 2 * i), i);
        ArrayList<Button> shootButtons = cardViewButtonSet((ImageView) map.getChildren().get(0));
        for(Button buttons : shootButtons) {
            buttons.toFront();
            map.getChildren().add(buttons);
        }
        return map;
    }

    /**
     * This function creates playerboard for the client running this GUI
     *
     * @return playerboard of the player
     * @throws FileNotFoundException if files are not found
     */
    private Pane createMainPlayerboard() throws FileNotFoundException {
        Pane playerBoard = new Pane();
        playerBoard.getChildren().add(generateCard(playerBoardPath(giveColor(player)), 110, 600, 0, 0, 0));
        //Weapons and power ups
        for (int i = 0; i < 3; i++) {
            if (i + 1 > player.getWeapons().size()) {
                playerBoard.getChildren().add(generateCard(weaponPath("weaponCard"), 90, 60, i * 60, 110, 0));
                playerBoard.getChildren().get(2 * i + 1).setVisible(false);
            } else
                playerBoard.getChildren().add(generateCard(weaponPath(player.getWeapons().get(i).stringGUI()), 90, 60, i * 60, 110, 0));

            if (i < player.getPowerups().size())
                playerBoard.getChildren().add(generateCard(powerPath(player.getPowerups().get(i).stringGUI()), 90, 60, 420 + i * 60, 110, 0));
            else {
                playerBoard.getChildren().add(generateCard(powerPath("powerCard"), 90, 60, 420 + i * 60, 110, 0));
                playerBoard.getChildren().get(2 * i + 2).setVisible(false);
            }
        }

        //Damages
        for (int i = 0; i < 12; i++) {
            if(i < player.getPlayerBoard().getDamageTrack().size())
                playerBoard.getChildren().add(addDamage(player.getPlayerBoard().getDamageTrack().get(i), 30, 50 + i * 35, 40));//base x 50, distanza 35
            else{
                playerBoard.getChildren().add(addDamage(player, 30, 50 + i * 35, 40));//base x 50, distanza 35
                playerBoard.getChildren().get(i+7).setVisible(false);
            }
        }

        //Skulls
        for (int i = 0; i < 8; i++) {
            playerBoard.getChildren().add(addSkull(40, 120 + i * 32, 70));//base x 120, distanza 33
            if (i >= player.getPlayerBoard().getNumDeaths())
                playerBoard.getChildren().get(i+19).setVisible(false);
        }

        //Ammo
        int[] array = enumToIntArray(player.getBullets());
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++){
                playerBoard.getChildren().add(addAmmo(i, 30, 470 + j * 35, 5 + i * 35));//base x 120, distanza 33
                ammoAction((Button) playerBoard.getChildren().get(27+j+i*3), convertToColor(i));
                if(array[i] <= j)
                    playerBoard.getChildren().get(27+j+i*3).setVisible(false);
            }

        //Marks
        for (Player player : players)
            for (int i = 0; i < 3; i++) {
                playerBoard.getChildren().add(addDamage(player, 25, 280 + 10 * i + 30 * players.indexOf(player), 0));//base x 50, distanza 35
                if (i >= this.player.getMarks(player))
                    playerBoard.getChildren().get(3*players.indexOf(player)+36+i).setVisible(false);
            }

        return playerBoard;

    }

    /**
     * This function creates playerboard used by other player
     *
     * @param player number of the player
     * @return playerboard of the player
     * @throws FileNotFoundException if files are not found
     */
    private Pane createOtherBoards(int player) throws FileNotFoundException {
        Pane playerBoard = new Pane();
        playerBoard.getChildren().add(generateCard(playerBoardPath(giveColor(getPlayerboardPlayer(player))), 90, 350, 0, 0, 0));

        //Weapons and power ups
        for (int i = 0; i < 3; i++) {
            if (i + 1 > getPlayerboardPlayer(player).getWeapons().size()) {
                playerBoard.getChildren().add(generateCard(weaponPath("weaponCard"), 75, 50,  i * 50, 90, 0));
                playerBoard.getChildren().get(2 * i + 1).setVisible(false);
            } else
                playerBoard.getChildren().add(generateCard(weaponPath(getPlayerboardPlayer(player).getWeapons().get(i).stringGUI()), 75, 50, i * 50, 90, 0));

            if (i < getPlayerboardPlayer(player).getPowerups().size())
                playerBoard.getChildren().add(generateCard(powerPath(getPlayerboardPlayer(player).getPowerups().get(i).stringGUI()), 75, 50, 200 + i*50, 90, 0));
            else {
                playerBoard.getChildren().add(generateCard(powerPath("powerCard"), 75, 50, 200 + i*50, 90, 0));
                playerBoard.getChildren().get(2 * i + 2).setVisible(false);
            }
        }

        //Damages
        for (int i = 0; i < 12; i++) {
            if(i < getPlayerboardPlayer(player).getPlayerBoard().getDamageTrack().size())
                playerBoard.getChildren().add(addDamage(getPlayerboardPlayer(player).getPlayerBoard().getDamageTrack().get(i), 25, 25 + i*20, 35));//base x 50, distanza 35
            else{
                playerBoard.getChildren().add(addDamage(getPlayerboardPlayer(player), 25, 25 + i*20, 35));//base x 50, distanza 35
                playerBoard.getChildren().get(i+7).setVisible(false);
            }
        }

        //Skulls
        for (int i = 0; i < 8; i++) {
            playerBoard.getChildren().add(addSkull(25, 70+ i*18, 65));
            if (i >= getPlayerboardPlayer(player).getPlayerBoard().getNumDeaths())
               playerBoard.getChildren().get(i+19).setVisible(false);
        }

        //Ammo
        int[] array = enumToIntArray(getPlayerboardPlayer(player).getBullets());
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++){
                playerBoard.getChildren().add(addAmmo(i, 20, 270 + j * 25, 5 + i * 30));
                if(array[i] <= j)
                    playerBoard.getChildren().get(27+j+i*3).setVisible(false);
            }

        //Marks
        for (Player p : players)
            for (int i = 0; i < 3; i++) {
                playerBoard.getChildren().add(addDamage(p, 15, 165 + 5 * i + 20 * players.indexOf(p), 2));
                if (i >= getPlayerboardPlayer(player).getMarks(p))
                    playerBoard.getChildren().get(3*players.indexOf(p)+36+i).setVisible(false);
                }

        return playerBoard;
    }

    /**
     * This function creates a Pane for responding questions
     * @return pane
     */
    private Pane askPane() {
        Pane pane = new Pane();
        pane.getChildren().add(new Label("Do you want to use a Power up?"));
        pane.getChildren().add(new Button("Yes"));
        pane.getChildren().add(new Button("No"));
        pane.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        ((Button) pane.getChildren().get(1)).setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switch(command){
                    case ASKFORPOWERUP:
                        notifyServer(new CommandObj(CHECKPOWERUP, object, true));
                        pane.setVisible(false);
                    case PLACE_WEAPONCARD:
                        command = LOAD_WEAPONCARD;
                        pane.setVisible(false);
                        break;
                    case ASKTARGETS:
                        command = CHOOSE_OPPONENTS;
                        pane.setVisible(false);
                        break;
                }

            }
        });
        //TODO sistemare questa parte dei power
        pane.getChildren().get(1).setTranslateX(30);
        pane.getChildren().get(1).setTranslateY(40);
        pane.getChildren().get(2).setTranslateX(100);
        pane.getChildren().get(2).setTranslateY(40);

        ((Button) pane.getChildren().get(2)).setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switch(command){
                    case ASKFORPOWERUP:
                        notifyServer(new CommandObj(CHECKPOWERUP, object, false));
                        pane.setVisible(false);
                    case PLACE_WEAPONCARD:
                        notifyServer(new CommandObj(EnumCommand.LOAD_WEAPONCARD, -1));
                        object =  false;
                        pane.setVisible(false);
                        break;
                    case ASKTARGETS:
                        object = true;
                        pane.setVisible(false);
                        break;
                }


            }
        });
        return pane;
    }

    /**
     * This function generates the map based on the board
     * @param map to add children
     * @param number of board
     * @param buttons for player actions
     * @throws FileNotFoundException file not found
     */
    private void generateBoard(Pane map, int number, ArrayList<Button> buttons) throws FileNotFoundException {
        int i = 0;
        int test = 0;
        int ammo = 0;
        ImageView mapImage = new ImageView(new Image(new FileInputStream("src/resources/images/gametable/map/board" + number + ".png")));
        changeSizeImage(mapImage, 430, 600);
        map.getChildren().add(mapImage);
        map.getChildren().get(12).toBack();
        switch (number) {
            case 1:
                generateBoardLeft1(map);
                generateBoardRight1(map);//8
                generateBoardButtonsLeft1(map);
                generateBoardButtonsRight1(map);//11
                ammo = 21;

                for (int y = 0; y < 4; y++)
                    for (int x = 0; x < 3; x++) {
                        i = setCellOnAction(map, x, y, i, test, ammo, buttons);
                        if (x == 1 && y == 0) {
                            x++;
                            test++;
                        }
                    }
                break;
            case 2:
                generateBoardLeft1(map);
                generateBoardRight2(map);
                generateBoardButtonsLeft1(map);
                generateBoardButtonsRight2(map);
                ammo = 20;
                for (int y = 0; y < 4; y++)
                    for (int x = 0; x < 3; x++) {
                        if (x == 0 && y == 3) {
                            x = 1;
                            test++;
                        }
                        i = setCellOnAction(map, x, y, i, test, ammo, buttons);
                        if (x == 1 && y == 0) {
                            x = 3;
                            test++;
                        }
                    }
                break;
            case 3:
                generateBoardLeft2(map);
                generateBoardRight1(map);
                generateBoardButtonsLeft2(map);
                generateBoardButtonsRight1(map);
                ammo = 22;
                for (int y = 0; y < 4; y++)
                    for (int x = 0; x < 3; x++) {
                        i = setCellOnAction(map, x, y, i, test, ammo, buttons);
                    }
                break;
            case 4:
                generateBoardLeft2(map);
                generateBoardRight2(map);
                generateBoardButtonsLeft2(map);
                generateBoardButtonsRight2(map);
                ammo = 21;
                for (int y = 0; y < 4; y++)
                    for (int x = 0; x < 3; x++) {
                        if (x == 0 && y == 3) {
                            x = 1;
                            test++;
                        }
                        i = setCellOnAction(map, x, y, i, test, ammo, buttons);
                    }
                break;
        }

        setActionPlayer(buttons.get(0), MOVE, map, ammo, buttons);
        setActionPlayer(buttons.get(1), GRAB_MOVE, map, ammo, buttons);
        setActionPlayer(buttons.get(2), SHOOT_MOVE, map, ammo, buttons);

    }

    /**
     * The following functions creates ammo ImageView depending on the board
     * @param map where to add Imageviews
     * @throws FileNotFoundException file not found
     */
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

    /**
     * The following functions create Cell buttons
     * @param map where to add children
     */
    private void generateBoardButtonsLeft1(Pane map) {

        map.getChildren().add(createButton("c00", 90, 105, -150, -80));
        map.getChildren().add(createButton("c10", 100, 100, -150, 20));
        map.getChildren().add(createButton("c01", 90, 95, -45, -80));
        map.getChildren().add(createButton("c11", 100, 95, -50, 20));
        map.getChildren().add(createButton("c21", 90, 105, -60, 120));

    }

    private void generateBoardButtonsRight1(Pane map) {

        map.getChildren().add(createButton("c02", 90, 105, 55, -80));
        map.getChildren().add(createButton("c12", 100, 95, 55, 20));
        map.getChildren().add(createButton("c22", 105, 95, 55, 120));
        map.getChildren().add(createButton("c03", 90, 95, 155, -80));
        map.getChildren().add(createButton("c13", 100, 95, 155, 20));
        map.getChildren().add(createButton("c23", 105, 95, 155, 120));

    }

    private void generateBoardButtonsLeft2(Pane map) {

        map.getChildren().add(createButton("c00", 90, 95, -160, -80));
        map.getChildren().add(createButton("c10", 100, 90, -160, 20));
        map.getChildren().add(createButton("c20", 90, 105, -155, 125));
        map.getChildren().add(createButton("c01", 90, 105, -55, -80));
        map.getChildren().add(createButton("c11", 100, 105, -60, 20));
        map.getChildren().add(createButton("c21", 90, 105, -50, 125));

    }

    private void generateBoardButtonsRight2(Pane map) {

        map.getChildren().add(createButton("c02", 90, 105, 55, -80));
        map.getChildren().add(createButton("c12", 100, 115, 50, 20));
        map.getChildren().add(createButton("c22", 90, 100, 55, 125));
        map.getChildren().add(createButton("c13", 100, 95, 155, 20));
        map.getChildren().add(createButton("c23", 100, 95, 155, 120));
    }

    /**
     * This function returns the path of a pawn image
     *
     * @param string name
     * @return the path
     */
    private String pawnPath(String string) {
        return "src/resources/images/gametable/pawns/" + string + "Pawn.png";
    }

    /**
     * This function returns the path of an ammo card image
     *
     * @param string name
     * @return the path
     */
    private String ammoPath(String string) {
        return "src/resources/images/gametable/ammo/" + string + ".png";
    }

    /**
     * This function returns the path of a weapon card image
     *
     * @param string name
     * @return the path
     */
    private String weaponPath(String string) {
        return "src/resources/images/gametable/weapons/" + string + ".png";
    }

    /**
     * This function returns the path of a power up card image
     *
     * @param string name
     * @return the path
     */
    private String powerPath(String string) {
        return "src/resources/images/gametable/power/" + string + ".png";
    }

    /**
     * This function returns the path of a damage counter
     * @param path name
     * @return the path
     */
    private String damagePath(String path) {
        return "src/resources/images/gametable/damages/" + path + "Damage.png";
    }

    /**
     * This function returns the path of a playerboard image
     * @return the path
     */
    private String playerBoardPath(String path) {
        return "src/resources/images/gametable/playerboard/" + path + "PlayerBoard.png";
    }

    /**
     * This function changes the dimension of an image
     * @param obj    to change dimension
     * @param height new height
     * @param width  new width
     */
    private void changeSizeImage(ImageView obj, int height, int width) {
        obj.setFitHeight(height);
        obj.setFitWidth(width);
    }

    /**
     * This function changes the dimension of a button
     * @param obj    to change dimension
     * @param height new height
     * @param width  new width
     */
    private void changeSizeButton(Button obj, int height, int width) {
        obj.setPrefHeight(height);
        obj.setPrefWidth(width);
    }

    /**
     * This function creates buttons
     * @param string name
     * @param height height
     * @param width  width
     * @param transX x position
     * @param transY y position
     * @return a new button
     */
    private Button createButton(String string, int height, int width, int transX, int transY) {
        Button button = new Button();
        changeSizeButton(button, height, width);
        button.setTranslateX(transX);
        button.setTranslateY(transY);
        button.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, new BorderWidths(5))));
        button.setStyle("-fx-background-color: transparent;");
        button.setOpacity(0);

        return button;
    }


    /**
     * This function creates buttons for player actions
     * @return an ArrayList of Buttons
     */
    private ArrayList<Button> actionButtons() {
        ArrayList<Button> actionButtons = new ArrayList<>();
        actionButtons.add(activateButton(  12));//Move
        actionButtons.add(activateButton( 29));//Grab
        actionButtons.add(activateButton( 47));//Shoot
        return actionButtons;
    }

    /**
     * This function creates buttons to check or to use cards
     *
     * @return ArrayList of buttons
     */
    private ArrayList<Button> cardViewButtonSet(ImageView image) {
        ArrayList<Button> buttons = new ArrayList<>();
        //one attack
        buttons.add(createButton("monoattack", 120, 150, 0, 155));
        shootAction(buttons.get(0), image, 0, 1);
        //> 1 attack
        buttons.add(createButton("multiattack", 60, 150, 0, 125));
        shootAction(buttons.get(1), image, 0, 2);
        //2 attacks
        buttons.add(createButton("biAttack", 60, 150, 0, 185));
        shootAction(buttons.get(2), image, 1, 2);
        //3 attacks
        buttons.add(createButton("triAttack1", 65, 75, -37, 185));
        shootAction(buttons.get(3), image, 1, 3);
        buttons.add(createButton("triAttack2", 65, 75, 37, 185));
        shootAction(buttons.get(4), image, 2, 3);

        return buttons;
    }

    /**
     * This function creates button for player actions
     * @param transY y coordinate
     * @return button
     */
    private Button activateButton(int transY) {
        Button button = createButton(" ", 40, 10, 6, transY);
        button.setRotate(90);
        return button;
    }

    /**
     * This function sets all Cell buttons in action
     * @param map with children
     * @param x coordinate
     * @param y coordinate
     * @param i number of ammo till that moment
     * @param test number of blank cells
     * @param ammo image of ammo
     * @param buttons for player action
     * @return number of Children till ammo
     */
    private int setCellOnAction(Pane map, int x, int y, int i, int test, int ammo, ArrayList<Button> buttons) {
        if (!((x == 1 && y == 0) || (x == 0 && y == 2) || (x == 2 && y == 3))) {
            setCellAction((Button) map.getChildren().get(ammo + x + y * 3 - test), x, y, (ImageView) map.getChildren().get(13 + i), map, ammo, buttons);
            i++;
        } else
            setCellAction((Button) map.getChildren().get(ammo + x + y * 3 - test), x, y, null, map, ammo, buttons);
        return i;
    }

    /**
     * This function shows invisible buttons
     * @param cell to show
     * @param isVisible or not
     */
    private void illuminateCell(Button cell, boolean isVisible) {
        cell.setVisible(isVisible);
        if (isVisible)
            cell.setOpacity(1);
    }

    /**
     * This function change graphics of an ImageView picking image from a Button
     * @param button with image
     * @param image to change pic
     */
    private void changeGraphics(Button button, ImageView image) {
        Node node = button.getGraphic();
        ImageView imageView = (ImageView) node;
        image.setImage(imageView.getImage());
    }

    /**
     * This function changes Button image with the one an ImageView
     * @param button to change image
     * @param image where to pick image
     */
    private void changeImage(Button button, ImageView image) {
        button.setGraphic(image);
        button.setVisible(true);
    }

    /**
     * This function create an Image of an ammo card
     *
     * @param path   from which you pick the image of the ammo card
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
     *
     * @param path   from which you pick the image of the weapon card
     * @param transX x position to set card
     * @param transY y position to set card
     * @return an Image of a weapon
     * @throws FileNotFoundException if file is not found
     */
    private ImageView tableWeaponCards(String path, int transX, int transY) throws FileNotFoundException {
        ImageView weaponImage = new ImageView(new Image(new FileInputStream(path)));
        weaponImage.setFitHeight(90);
        weaponImage.setFitWidth(60);
        weaponImage.setTranslateX(transX);
        weaponImage.setTranslateY(transY);
        return weaponImage;
    }

    /**
     * This function creates cards and Playerboard
     * @param name path from which image is picked
     * @param height of button
     * @param width of button
     * @param transX x coordinate
     * @param transY y coordinate
     * @param grades rotation
     * @return a button
     * @throws FileNotFoundException if file not found
     */
    private Button generateCard(String name, int height, int width, int transX, int transY, int grades) throws FileNotFoundException {

        Button button = new Button();
        changeSizeButton(button, height, width);
        button.setRotate(grades);

        button.setOpacity(1);
        ImageView image = new ImageView(new Image(new FileInputStream(name)));
        changeSizeImage(image, height, width);
        button.setGraphic(image);

        button.setPadding(Insets.EMPTY);
        button.setTranslateX(transX);
        button.setTranslateY(transY);

        return button;
    }

    /**
     * This function sets action for card Button in the map
     * @param buttonCard to activate
     * @param image to change
     * @param x coordinate
     * @param y coordinate
     * @param pos position of the card
     * @param playerboard of player of this GUI
     * @param map map pane
     */
    //Carte relative alla mappa
    private void setMapCardActions(Button buttonCard, ImageView image, int x, int y, int pos, Pane playerboard, Pane map) {//carte della mappa posso guardarle o pescarle
        for (int i = 0; i < 3; i++) {
            weaponAction(playerboard, (Button) playerboard.getChildren().get(2 * i + 1), image, i);
        }
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
                    case GRAB_WEAPON:
                        if(board.getBillboard().getCellPosition(player.getCell()).equals(new Position(x, y))){
                            notifyServer(new CommandObj(GRAB_WEAPON, pos));
                            buttonCard.setVisible(false);
                        }
                        else buttonCard.disableProperty();
                        /*if (!getPlayer().canPay(toIntArray(getWeapon(x, y, pos).getGrabCost())))
                            buttonCard.disabledProperty();
                        else if (getPlayer().getWeapons().size() <= 3) {
                            getPlayer().addWeapon((WeaponCard) board.getBillboard().getCellFromPosition(new Position(x, y)).getCard(pos));
                            changeGraphicButtons((Button) playerboard.getChildren().get(3), buttonCard);
                            buttonCard.setVisible(false);
                            for (int i = 5; i > -1; i--)
                                map.getChildren().get(map.getChildren().size() - board.getSkulls()).setVisible(true);
                            command = CHOOSE_ACTION;
                        } else command = DISCARD_WEAPON;*/
                        break;
                    default:
                        buttonCard.disableProperty();

                }
            }
        });
    }

    /**
     * This function activates actions for player
     * @param action button to activate
     * @param order state to set
     * @param map board pane
     * @param ammoSize Size of map children till ammo cards
     * @param buttons list of action buttons
     */
    //azioni che giocatore può fare
    private void setActionPlayer(Button action, EnumCommand order, Pane map, int ammoSize, ArrayList<Button> buttons) {
        action.setOpacity(1);

        action.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (Button button : buttons)
                    button.setVisible(false);
                //verifica se è il proprio turno
                if (command == CHOOSE_ACTION) {
                    command = order;
                    for(int i = ammoSize; i < ammoSize + board.getBillboard().getCellMap().size(); i++)
                        illuminateCell((Button)map.getChildren().get(i), true);
                }

            }
        });
    }

    /**
     * This function sets action for Cell buttons
     * @param cell to activate
     * @param x coordinate
     * @param y coordinate
     * @param ammo image of Ammo card in cell if present
     * @param map board pane
     * @param ammoSize  Size of map children till ammo cards
     * @param buttons player action buttons
     */
    //Azioni che può fare cella
    private void setCellAction(Button cell, int x, int y, ImageView ammo, Pane map, int ammoSize, ArrayList<Button> buttons) {
        cell.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                for (int i = ammoSize; i < map.getChildren().size() - board.getSkulls() - players.size(); i++) {
                    illuminateCell((Button) map.getChildren().get(i), false);
                }
                movePawn((Button) map.getChildren().get(map.getChildren().size() - board.getSkulls() - players.size() ));

                for (Button button : buttons)
                    button.setVisible(true);

                switch (command) {
                    case MOVE:
                        notifyServer(new CommandObj(MOVE, new Position(x, y)));
                        break;
                    case GRAB_MOVE:
                        if (ammo != null)
                            ammo.setVisible(false);

                        notifyServer(new CommandObj(GRAB_MOVE, new Position(x, y)));
                        command = CHOOSE_ACTION;
                        break;
                    case SHOOT_MOVE:
                       notifyServer(new CommandObj(SHOOT_MOVE, new Position(x, y)));
                       break;
                    case MOVE_FRENZY:
                        notifyServer(new CommandObj(MOVE_FRENZY, new Position(x, y)));
                        break;
                    case GRAB_MOVE_FRENZYX1:
                        if (ammo != null)
                            ammo.setVisible(false);

                        notifyServer(new CommandObj(GRAB_MOVE_FRENZYX1, new Position(x, y)));
                        break;
                    case GRAB_MOVE_FRENZYX2:
                        if (ammo != null)
                            ammo.setVisible(false);
                        notifyServer(new CommandObj(GRAB_MOVE_FRENZYX2, new Position(x, y)));
                        break;
                    case SHOOT_MOVE_FRENZYX1:
                        notifyServer(new CommandObj(SHOOT_MOVE_FRENZYX1, new Position(x, y)));
                        break;
                    case SHOOT_MOVE_FRENZYX2:
                        notifyServer(new CommandObj(SHOOT_MOVE_FRENZYX2, new Position(x, y)));
                        break;
                    case REG_CELL:
                       notifyServer(new CommandObj(REG_CELL, new Position(x, y)));
                        command = NOT_YOUR_TURN;
                        break;
                    case TELEPORTER:
                        notifyServer(new CommandObj(EnumCommand.TELEPORTER, new Position(x, y)));
                        break;
                    case KINETICRAY:
                        notifyServer(new CommandObj(EnumCommand.KINETICRAY, (Player) object, new Position(x, y)));
                        break;
                    case CHOOSE_CELL:
                        if(((List<Position>)targets).contains(new Position(x, y))) {
                            chosenTargets = new Position(x, y);
                            object = true;
                        }
                            else cell.disableProperty();
                            break;
                    default:
                        cell.disableProperty();
                }
            }
        });
    }

    //TODO sistemare questa funzione con notify
    /**
     * This function sets action for Power ups
     * @param powerUp to activate
     * @param i position of AmmoCard
     */
    //azioni power up
    private void setPowerUp(Button powerUp, int i) {
        powerUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switch (command) {
                    case CHOOSE_ACTION:
                        if (player.getPowerups().get(i).getPowerUp() == TELEPORTER || player.getPowerups().get(i).getPowerUp() == KINETICRAY){
                            if(player.getPowerups().get(i).getPowerUp() == TELEPORTER)
                                notifyServer(new CommandObj(PAYPOWERUP, i));
                            if (player.getPowerups().get(i).getPowerUp() == KINETICRAY)
                                notifyServer(new CommandObj(PAYPOWERUP, i));
                        }
                        else powerUp.disableProperty();
                        break;
                    case SHOOT://after shooting
                        if (player.getPowerups().get(i).getPowerUp() != VENOMGRENADE) {
                            if (player.getPowerups().get(i).getPowerUp() == PowerUp.GUNSIGHT)
                                notifyServer(new CommandObj(PAYGUNSIGHT, i));
                            else notifyServer(new CommandObj(PAYPOWERUP, i));
                        }
                        else powerUp.disableProperty();
                        break;
                    case USE_VENOMGRENADE:// after getting hit
                        if (player.getPowerups().get(i).getPowerUp() == VENOMGRENADE)
                            notifyServer(new CommandObj(PAYPOWERUP, i));
                        else powerUp.disableProperty();
                        break;
                    case DISCARD_POWER: // Discard power up
                        notifyServer(new CommandObj(PAIDPOWERUP, player.getPowerups().get(0), true));
                        powerUp.setVisible(false);
                        break;
                    default:
                        powerUp.disableProperty();
                }
            }
        });
    }

    /**
     * This function sets action for Weapon cards
     * @param playerBoard
     * @param attack button to activate
     * @param weapon image of WeaponCard
     * @param i position of WeaponCard
     */
    private void weaponAction(Pane playerBoard, Button attack, ImageView weapon, int i) {
        attack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switch (command) {
                    case SHOOT://after shooting
                        if (player.getWeapons().get(i).isReady()) {
                            index = 1;
                            changeImage(attack, weapon);
                        } else attack.disableProperty();
                        break;
                    case DISCARD_WEAPON:// after getting hit
                        attack.setVisible(false);
                        break;
                    case LOAD_WEAPONCARD:
                        if(!player.getNotLoaded().contains(player.getWeapons().get(i)))
                            attack.disableProperty();
                        else{
                            notifyServer(new CommandObj(EnumCommand.LOAD_WEAPONCARD, player.getNotLoaded().indexOf(player.getWeapons().get(i))));
                            if(player.getNotLoaded().isEmpty())
                                object = true;
                            else {
                                root.getChildren().get(players.size()+2).setVisible(true);
                                command = PLACE_WEAPONCARD;
                            }
                        }
                    default:
                        attack.disableProperty();
                }
            }
        });
    }

    //TODO settare meglio gli attacchi
    /**
     * This function activates button for shooting actions
     * @param attack button to activate
     * @param weapon image of weapon
     * @param nattack number of attack
     * @param sizeAttacks size of attacks
     */
    private void shootAction(Button attack, ImageView weapon, int nattack, int sizeAttacks) {
       attack.setOpacity(1);
       attack.setVisible(false);
       attack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(((List<Integer>) chosenTargets).isEmpty() && nattack != 0 || nattack != 2)
                    attack.disableProperty();
                if (command == SHOOT && (player.getWeapons().get(index).getAttacks().size() == sizeAttacks || (player.getWeapons().get(0).getAttacks().size() == 2 && nattack == 0))) {
                    ((List<Integer>) chosenTargets).add(nattack);
                    if((Boolean) targets || ((List<Integer>) chosenTargets).size() == player.getWeapons().get(index).getAttacks().size()){
                        object = true;
                        weapon.setVisible(false);
                    }
                    else {
                        attack.setVisible(false);
                    }
                    //se posso usare l'attacco numero nattack allora l'attacco s'illumina
                    //poi cambia stato in base al tipo d'attacco
                } else attack.disableProperty();
            }

        });
    }

    /**
     * This function is used to create ImageViews for damages and marks
     * @param shooter player from which color of image is picked
     * @param dimension of the image
     * @param transX x coordinate
     * @param transY y coordinate
     * @return Image
     * @throws FileNotFoundException file not found
     */
    private ImageView addDamage(Player shooter, int dimension, int transX, int transY) throws FileNotFoundException {
        ImageView image = new ImageView(new Image(new FileInputStream(damagePath(giveColor(shooter)))));
        changeSizeImage(image, dimension, dimension);
        image.setTranslateX(transX);
        image.setTranslateY(transY);
        return image;
    }

    /**
     * This function gives a string with the color of player
     * @param player whose color is needed
     * @return a string
     */
    private String giveColor(Player player) {
        if (players.indexOf(player) == 0)
            return "yellow";
        if (players.indexOf(player) == 1)
            return "gray";
        if (players.indexOf(player) == 2)
            return "purple";
        if (players.indexOf(player) == 3)
            return "lightBlue";
        return "green";
    }

    /**
     * This function return a player from list of players
     * @param i from of player from the player of this GUI
     * @return player wanted
     */
    private Player getPlayerboardPlayer(int i) {
        if (players.indexOf(player) + i > players.size())
            return players.get(players.indexOf(player) + i - players.size());
        else return players.get(players.indexOf(player) + i);
    }

    /**
     * This function creates Imageviews for skulls
     * @param dimension of skulls
     * @param transX x coordinate
     * @param transY y coordinate
     * @return ImageView of skull
     * @throws FileNotFoundException file not found
     */
    private ImageView addSkull(int dimension, int transX, int transY) throws FileNotFoundException {
        ImageView image = new ImageView(new Image(new FileInputStream("src/resources/images/gametable/damages/skull.png")));
        changeSizeImage(image, dimension, dimension);
        image.setTranslateX(transX);
        image.setTranslateY(transY);
        return image;
    }

    /**
     * This function moves a pawn when needed
     * @param pawn to be moved
     */
    private void movePawn(Button pawn) {
        int distance = 100;
        if (board.getBillboard().getCellPosition(player.getCell()).getX() != 0)
            distance = 90;
        pawn.setTranslateX(-140 + board.getBillboard().getCellPosition(player.getCell()).getY() * 100 + 20 * (player.getCell().getPawns().size() % 3));
        pawn.setTranslateY(-110 + board.getBillboard().getCellPosition(player.getCell()).getX() * distance + 30 * (player.getCell().getPawns().size() % 2));
    }

    /**
     * This function creates an ammo Button
     * @param color of ammo
     * @param dimension of button
     * @param transX c voordinate
     * @param transY y coordinate
     * @return a button of ammo
     * @throws FileNotFoundException file not found
     */
    private Button addAmmo(int color, int dimension, int transX, int transY) throws FileNotFoundException {
        String ammoString = "";
        if(color == 0)
            ammoString = "RED";
        if(color == 1)
            ammoString = "YELLOW";
        if(color == 2)
            ammoString = "BLUE";
        Button ammo = generateCard(ammoPath(ammoString), dimension, dimension, transX, transY, 0);
        ammo.setPadding(Insets.EMPTY);
        return ammo;
    }

    /**
     * This function sets action for ammo buttons
     * @param ammo to activate
     */
    private void ammoAction(Button ammo, constants.Color color) {
        ammo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switch (command) {
                    case PAYGUNSIGHT:
                        ammo.setVisible(false);
                        notifyServer(new CommandObj(GUNSIGHTPAID, object, color));
                        break;
                    case PAYPOWERUP:
                        notifyServer(new CommandObj(PAIDPOWERUP, object, false));
                        break;
                    default:
                        ammo.disableProperty();
                }
            }

        });
    }

    /**
     * This function sets action for pawn buttons
     * @param pawn to activate
     * @param name player name associated with pawn
     */
    private void pawnAction(Button pawn, Player name) {
        pawn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switch (command) {
                    case USE_KINETICRAY:
                        object = name;
                        command = EnumCommand.KINETICRAY;
                        break;
                    case CHOOSE_OPPONENTS:
                        if(((List<Player>)targets).contains(name)){
                            ((List<Player>)chosenTargets).add(name);
                            maxTargets--;
                            if(maxTargets == 0)
                                object = true;
                            else{
                                root.getChildren().get(players.size()).setVisible(true);
                                changeMessage("Do you want to hit another taget?", (Pane) root.getChildren().get(players.size()));
                                ((Pane)root.getChildren().get(players.size())).getChildren().get(1).setVisible(true);
                                ((Pane)root.getChildren().get(players.size())).getChildren().get(2).setVisible(true);
                                command = ASKTARGETS;
                            }
                        }
                        else pawn.disableProperty();
                        break;
                    default:
                        pawn.disableProperty();
                }
            }

        });
    }

    /**
     * This function modifies playerboards if events happen
     * @param p to modify playerboard
     * @throws FileNotFoundException file not found
     */
    private void notifyChangesPlayerboard(Player p) throws FileNotFoundException {
        Pane playerBoard = (Pane) root.getChildren().get(returnPanePlayer(p));

        //Power ups and Weapons
        for (int i = 0; i < 3; i++) {
            if (i + 1 > p.getWeapons().size())
                playerBoard.getChildren().get(2*i + 1).setVisible(false);
            else {
                ((Button) playerBoard.getChildren().get(2*i +1)).setGraphic(new ImageView(new Image(new FileInputStream(weaponPath(player.getWeapons().get(i).stringGUI())))));
                playerBoard.getChildren().get(2*i + 1).setVisible(true);
            }
            if (i < player.getPowerups().size()){
                ((Button) playerBoard.getChildren().get(2*i + 2)).setGraphic(new ImageView(new Image(new FileInputStream(powerPath(player.getPowerups().get(i).stringGUI())))));
                playerBoard.getChildren().get(2*i + 2).setVisible(true);
            }
            else playerBoard.getChildren().get(2*i + 2).setVisible(true);
        }

        //Damages
        for (int i = 0; i < 12; i++) {
            if(i< p.getPlayerBoard().getDamageTrack().size()) {
                ((ImageView) playerBoard.getChildren().get(7+i)).setImage(new Image(new FileInputStream(damagePath(giveColor(p.getPlayerBoard().getDamageTrack().get(i))))));
                playerBoard.getChildren().get(7+i).setVisible(true);
            }
            else{
                playerBoard.getChildren().get(7+i).setVisible(false);
            }
        }

        //Skulls
        for (int i = 0; i < 8; i++) {
            if(i >= p.getPlayerBoard().getNumDeaths())
                playerBoard.getChildren().get(19+i).setVisible(true);
            else  playerBoard.getChildren().get(19+i).setVisible(false);
        }

        //Ammo
        int[] array = enumToIntArray(p.getBullets());
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++){
                if(array[i] < j)
                    playerBoard.getChildren().get(27+j+i*3).setVisible(true);
                else playerBoard.getChildren().get(27+j+i*3).setVisible(false);
            }


        //marks
        for (Player player : players)
            for (int i = 0; i < 3; i++) {
                if (i < player.getMarks(player)) {
                    ((ImageView) playerBoard.getChildren().get(35 + i)).setImage(new Image(new FileInputStream(damagePath(giveColor(p.getPlayerBoard().getDamageTrack().get(i))))));
                    playerBoard.getChildren().get(35 + i).setVisible(true);
                }
                else playerBoard.getChildren().get(35 + i).setVisible(false);
            }

        root.getChildren().set(returnPanePlayer(p),playerBoard);
    }

    /**
     * This function modifies map pane if board was modified
     * @param map to modify
     * @param board new Board
     * @throws FileNotFoundException file not found
     */
    private void notifyChangesMap(Pane map, Board board) throws FileNotFoundException {
        for (int i = 0; i < 3; i++) {
            if (i < ((RegenerationCell) board.getBillboard().getCellFromPosition(new Position(1, 0))).getCards().size()) {
                ((Button) map.getChildren().get(i + 1)).setGraphic(new ImageView(new Image(new FileInputStream(weaponPath(((RegenerationCell) board.getBillboard().getCellFromPosition(new Position(1, 0))).getCards().get(i).stringGUI())))));
                map.getChildren().get(i + 1).setVisible(true);
            } else
                map.getChildren().get(i + 1).setVisible(false);
        }
        //blue weapon 4-6
        for (int i = 0; i < 3; i++) {
            if (i < ((RegenerationCell) board.getBillboard().getCellFromPosition(new Position(0, 2))).getCards().size()) {
                ((Button) map.getChildren().get(i + 1)).setGraphic(new ImageView(new Image(new FileInputStream(weaponPath(((RegenerationCell) board.getBillboard().getCellFromPosition(new Position(0, 2))).getCards().get(i).stringGUI())))));
                map.getChildren().get(i + 1).setVisible(true);
            } else map.getChildren().get(i + 4).setVisible(false);
        }

        //yellow weapon 7-9
        for (int i = 0; i < 3; i++) {
        if (i < ((RegenerationCell) board.getBillboard().getCellFromPosition(new Position(2, 3))).getCards().size()) {
            ((Button) map.getChildren().get(i + 1)).setGraphic(new ImageView(new Image(new FileInputStream(weaponPath(((RegenerationCell) board.getBillboard().getCellFromPosition(new Position(2, 3))).getCards().get(i).stringGUI())))));
            map.getChildren().get(i + 1).setVisible(true);
        }
        else map.getChildren().get(i + 4).setVisible(false);
        }

        //Ammo card
        int j = 0;
        for(int x = 0; x < 3; x++)
            for(int y = 0; y < 3; y++)
                if(board.getBillboard().getCellFromPosition(new Position(x, y)).getClass() != null && board.getBillboard().getCellFromPosition(new Position(x, y)).getClass() == NormalCell.class){
                    ((ImageView) map.getChildren().get(12+j)).setImage(new Image(new FileInputStream(ammoPath(board.getBillboard().getCellFromPosition(new Position(x, y)).getCard(0).stringGUI()))));
                    j++;
                }
        //Cells
        //Pawns
        for(int i = 0; i < players.size(); i++)
            movePawn((Button) map.getChildren().get(map.getChildren().size()-board.getSkulls()-players.size()+i-5));

        //Skulls
        for(int i = 0; i < board.getSkulls(); i++)
            if(i > board.getSkulls())
                map.getChildren().remove(map.getChildren().size()-1-board.getSkulls()+i-5);
    }

    /**
     * This function creates a timer label for player
     * @return a pane
     */
    private GridPane timer(){
        GridPane pane = new GridPane();
        pane.add(new Label("Time left:"), 0, 0);
        pane.add(new Label(), 0, 1);
        pane.getChildren().get(0).setStyle("-fx-text-fill: white;");
        pane.getChildren().get(1).setStyle("-fx-text-fill: white;");
        return pane;
    }

    /**
     * This function activates timer for player actions
     * @param timerText to be set
     * @param counter seconds during which player can play
     * @param changeText text with info
     */
    private void startCountDown(Pane timerText, int counter, boolean changeText) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int count = counter;
            public void run() {
                if(count > 0) {
                    if(changeText)
                        Platform.runLater(() -> ((Label) timerText.getChildren().get(1)).setText(printTime(count)));
                    count--;

                }
                else {
                    timerText.setVisible(false);
                    timer.cancel();
                }
            }
        }, 1000, 1000); //Every 1 second
    }

    /**
     * This function returns index of player
     * @param player to know position in players List
     * @return index of player wanted
     */
    private int returnPanePlayer(Player player){
        if (players.indexOf(player) == 0)
            return 1;
        if (players.indexOf(player) == 1)
            return 2;
        if (players.indexOf(player) == 2)
            return 3;
        if (players.indexOf(player) == 3)
            return 4;
        return 5;
    }

    /**
     * This function prints time
     * @param count number to be printed
     * @return a string
     */
    private String printTime(int count){
        if(count%60>= 0 && count%60 <=9)
            return count/60 + ":0" + count%60;
        else return count/60 + ":" + count%60;
    }

    /**
     * This function changes message for Ask pane
     * @param string with text
     * @param ask pane
     */
    private void changeMessage(String string, Pane ask){
       ask.getChildren().get(0).setVisible(true);
        ((Label) ask.getChildren().get(0)).setText(string);
        startCountDown(ask, 5, false);
    }

    //TODO usarla quando si crea board
    /**
     * This function sets a number used for creating map
     */
    private void setNumBoard(){
        if (board.getBillboard().getCellMap().size() == 12)
            numBoard = 3;
        if (board.getBillboard().getCellMap().size() == 10)
            numBoard = 2;
        if (board.getBillboard().getCellMap().size() == 11 && board.getBillboard().getCellFromPosition(new Position(2, 0)) == null)
            numBoard = 1;
        if (board.getBillboard().getCellMap().size() == 11 && board.getBillboard().getCellFromPosition(new Position(0, 3)) == null)
            numBoard = 4;
    }

    @Override
    public void gameStart(Board board) {
        this.board = board;
        setNumBoard();
    }

    @Override
    public void giveMessage(String title, String mex){
        changeMessage(mex, (Pane) root.getChildren().get(players.size()));
    }

    @Override
    public void giveError(String error){
        changeMessage(error, (Pane) root.getChildren().get(players.size()));
    }

    @Override
    public boolean loadWeapon(List<String> notLoaded){
        root.getChildren().get(players.size()).setVisible(true);
        ((Label)((Pane)root.getChildren().get(players.size())).getChildren().get(0)).setText("Do you want to load a WeaponCard?");
        ((Pane)root.getChildren().get(players.size())).getChildren().get(1).setVisible(true);
        ((Pane)root.getChildren().get(players.size())).getChildren().get(2).setVisible(true);
        command = PLACE_WEAPONCARD;
        while(true) {
            if (object.getClass() == Boolean.class)
                return((Boolean) object);
        }
    }


    @Override
    public void myTurn(Constants modAction){
        this.command = CHOOSE_ACTION;
        startCountDown((Pane) root.getChildren().get(players.size() + 2), 120, true);
    }

    @Override
    public void notMyTurn(String nameOfWhoPlay){
        this.command = NOT_YOUR_TURN;
        for(Player p : players)
            if(p.getName().equals(nameOfWhoPlay))
                ((Button)root.getChildren().get(returnPanePlayer(p))).setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, new BorderWidths(5))));
            else (root.getChildren().get(returnPanePlayer(p))).setStyle("-fx-padding: 5; -fx-border-style: none;\n-fx-border-width: 0;-fx-border-insets: 0;");
    }

    @Override
    public void showBoard(){}

    @Override
    public void regeneratePlayer(){
        command = REG_CELL;
        for(int i = root.getChildren().size() - board.getSkulls()-players.size()-board.getBillboard().getCellMap().size(); i < root.getChildren().size() - board.getSkulls()-players.size() - 5; i++)
            illuminateCell((Button)root.getChildren().get(i), true);
    }

    @Override
    public void updatePlayer(Player player){
        try {
            notifyChangesPlayerboard(player);
            if(this.player.getName().equals(player.getName()))
                this.player = player;
            for(Player p : players)
                if(p.getName().equals(player.getName()))
                    players.set(players.indexOf(p), player);
        } catch (FileNotFoundException e) {
            e.fillInStackTrace();
        }
    }

    @Override
    public void updateBoard(Board board){
        try {
            notifyChangesMap((Pane)root.getChildren().get(players.size()), board);
            this.board = board;
        } catch (FileNotFoundException e) {
            e.fillInStackTrace();
        }
    }

    @Override
    public void grab(Cell cell){
        if(cell.getClass() == RegenerationCell.class)
            command = GRAB_WEAPON;
    }

    //usata per ottenere i target
    @Override
    public List<String> getTargetsName(List<Player> potentialTarget, int maxTarget){
        command = CHOOSE_OPPONENTS;
        maxTargets = maxTarget;
        targets = potentialTarget;
        while(true) {
            if (object.getClass() == Boolean.class)
                return((List<String>) chosenTargets);
        }
    }

    //usata per gli optional
    @Override
    public List<Integer> chooseIndexes(List<Attack> attacks, boolean canRandom){
        command = CHOOSE_CELL;
        targets = canRandom; //Boolean per optional
        while(true) {
            if (object.getClass() == Boolean.class)
                return((List<Integer>)chosenTargets);
        }
    }

    //usata per colpire i bersagli
    @Override
    public Position choosePositionToAttack(List<Position> potentialposition) {
        command = CHOOSE_CELL;
        targets = potentialposition;
        while(true) {
            if (object.getClass() == Boolean.class)
                return((Position) chosenTargets);
        }
    }

    @Override
    public void askPowerUp(PowerUp power) {
        root.getChildren().get(players.size()).setVisible(true);
        ((Label)((Pane)root.getChildren().get(players.size())).getChildren().get(0)).setText("Do you want to use a power up?");
        ((Pane)root.getChildren().get(players.size())).getChildren().get(1).setVisible(true);
        ((Pane)root.getChildren().get(players.size())).getChildren().get(2).setVisible(true);
        object = power;
        command = ASKFORPOWERUP;
    }

    @Override
    public void usePowerUp() {
        command = POWERUP;
    }

    @Override
    public void payGunsight(int[] bullets, int card) {
        root.getChildren().get(players.size()).setVisible(true);
        ((Label)((Pane)root.getChildren().get(players.size())).getChildren().get(0)).setText("Choose which cube do you want to use");
        ((Pane)root.getChildren().get(players.size())).getChildren().get(1).setVisible(false);
        ((Pane)root.getChildren().get(players.size())).getChildren().get(2).setVisible(false);
        command = PAYGUNSIGHT;
    }

    @Override
    public void payPowerUp(PowerCard card) {
        root.getChildren().get(players.size()).setVisible(true);
        ((Label)((Pane)root.getChildren().get(players.size())).getChildren().get(0)).setText("Click on th power up if you want to discard it, else the cubes if you want to pay it");
        ((Pane)root.getChildren().get(players.size())).getChildren().get(1).setVisible(false);
        ((Pane)root.getChildren().get(players.size())).getChildren().get(2).setVisible(false);
        object = card;
        command = PAYPOWERUP;
    }

    @Override
    public void useTeleport() {
        command = EnumCommand.TELEPORTER;
        for(int i = board.getBillboard().getCellMap().size()-1; i > -1; i++)
            illuminateCell(((Button)(returnPaneMap()).getChildren().get(returnPaneMap().getChildren().size()-board.getSkulls()-players.size()-i-5)), true);
    }

    @Override
    public void useKineticray(List<Player> player) {
        command = CHOOSE_OPPONENTS;
    }

    @Override
    public void giveRoundScore(String playerDead, Map<String, Integer> points) {
        changeMessage(playerDead, (Pane) root.getChildren().get(players.size()+3));
    }

    @Override
    public void chooseGunsightTarget(List<Player> targets) {

    }


    private void notifyServer(CommandObj obj) {
        try {
            client.sendCMD(obj);
        } catch (RemoteException e) {
            e.fillInStackTrace();
        }
    }

    private Pane returnPaneMap(){
        return (Pane) root.getChildren().get(players.size()+1);
    }

}
