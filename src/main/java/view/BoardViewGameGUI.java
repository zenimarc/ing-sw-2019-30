package view;

import attack.Attack;
import board.Board;
import board.Position;
import board.RegenerationCell;
import board.billboard.BillboardGenerator;
import client.Client;

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
import weapon.WeaponCard;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.*;

import static controller.EnumCommand.*;
import static powerup.PowerUp.KINETICRAY;
import static powerup.PowerUp.TELEPORTER;
import static powerup.PowerUp.VENOMGRENADE;

//TODO sistemare Playerboard high multi
public class BoardViewGameGUI extends Application implements View {
    private int stageHeight = 700;
    private int stageWidth = 930;
    private Board board;
    private Client client;
    private List<Player> players = new ArrayList<>();
    private Player player = new Player("Marco");
    private EnumCommand command = CHOOSE_ACTION;
    private int test = 3;
    private Pane root;

    public BoardViewGameGUI() throws RemoteException {
    }

    /*
    public BoardViewGameGUI(Board board, ArrayList<Client> clients, Player player, EnumCommand command) throws RemoteException {
        this.board = board;
        this.clients = clients;
        this.player = player;
        this command = command;
    }
     */

    private Player getPlayer() {
        return this.player;
    }

    private WeaponCard getWeapon(int x, int y, int pos) {
        return (WeaponCard) board.getBillboard().getCellFromPosition(new Position(x, y)).getCard(pos);
    }

    private void initialize(/*, ArrayList<Client> client*/) {
        this.board = new Board(8, BillboardGenerator.createBillboard(test));
        player.setPawnCell(board.getBillboard().getCellFromPosition(new Position(1, 1)));
        players.add(player);
        players.add(new Player("test"));
        players.add(new Player("prova"));
        players.add(new Player("Marco"));
        //players.add(new Player("tizio"));
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
    public void start(Stage primaryStage) throws FileNotFoundException, RemoteException, InterruptedException {
        initialize();
        //gameStart()
        root = createGame(test);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        root.setCenterShape(true);
        Scene scene = new Scene(root);
        primaryStage.setTitle("Adrenaline");
        primaryStage.setScene(scene);
        primaryStage.setHeight(stageHeight);
        primaryStage.setWidth(stageWidth);
        primaryStage.show();
    }

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

        Pane board1 = createBoards();
        anchor.getChildren().add(board1);
        anchor.getChildren().get(0).setLayoutX(150);
        anchor.getChildren().get(0).setLayoutY(540);

        anchor.getChildren().add(createBoardRight());
        anchor.getChildren().get(1).setLayoutX(650);
        anchor.getChildren().get(1).setLayoutY(300);

        anchor.getChildren().add(createBoardLeft());
        anchor.getChildren().get(2).setLayoutX(-30);
        anchor.getChildren().get(2).setLayoutY(300);

        if (players.size() == 5) {
            anchor.getChildren().add(createBoardMultiHigh(3));
            anchor.getChildren().get(3).setLayoutX(150);
            anchor.getChildren().get(3).setLayoutY(60);

            anchor.getChildren().add(createBoardMultiHigh(4));
            anchor.getChildren().get(4).setLayoutX(510);
            anchor.getChildren().get(4).setLayoutY(60);
        } else if (players.size() == 4) {
            anchor.getChildren().add(createBoardHigh());
            anchor.getChildren().get(3).setLayoutX(310);
            anchor.getChildren().get(3).setLayoutY(33);
        }

        anchor.getChildren().add(askPane(board1));
        anchor.getChildren().get(players.size()).setLayoutX(370);
        anchor.getChildren().get(players.size()).setLayoutY(300);
        anchor.getChildren().get(players.size()).toFront();

        anchor.getChildren().add(createMap(number, board1));
        anchor.getChildren().get(players.size() + 1).setLayoutX(150);
        anchor.getChildren().get(players.size() + 1).setLayoutY(110);
        anchor.getChildren().get(players.size() + 1).toBack();

        anchor.getChildren().add(timer());
        startCountDown((Pane) anchor.getChildren().get(players.size() + 2), 15);
        anchor.getChildren().get(players.size() + 2).setLayoutX(800);
        anchor.setCenterShape(true);


        return anchor;
    }

    /**
     *
     * @param playerboard
     * @return
     */
    private Pane askPane(Pane playerboard) {
        GridPane pane = new GridPane();
        pane.getChildren().add(new Label("Do you want to use a Power up?"));
        pane.getChildren().add(new Button("Yes"));
        pane.getChildren().add(new Button("No"));
        pane.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        ((Button) pane.getChildren().get(1)).setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (int i = 0; i < 3; i = i + 2)
                    playerboard.getChildren().get(2 + 2 * i).setVisible(true);
                command = POWERUP;
                pane.setVisible(false);
            }
        });
        pane.getChildren().get(1).setTranslateX(30);
        pane.getChildren().get(1).setTranslateY(40);
        pane.getChildren().get(2).setTranslateX(100);
        pane.getChildren().get(2).setTranslateY(40);

        ((Button) pane.getChildren().get(2)).setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pane.setVisible(false);
                command = CHOOSE_ACTION;
            }
        });
        return pane;
    }

    /**
     * This function creates the map design
     *
     * @param number of the map to be shown
     * @return map
     * @throws FileNotFoundException if files are not found
     */
    private Pane createMap(int number, Pane playerboard) throws FileNotFoundException {//TODO mancano teschi, pedine, bottoni e settare le giuste immagini
        StackPane map = new StackPane();

        //Card you want to see 0
        map.getChildren().add(tableWeaponCards(weaponPath("weaponCard"), 0, 115));
        changeSizeImage((ImageView) map.getChildren().get(0), 200, 150);
        map.getChildren().get(0).setVisible(false);
        map.getChildren().get(0).toFront();

        //red weapon 1-3
        for (int i = 0; i < 3; i++) {
            if (i < ((RegenerationCell) board.getBillboard().getCellFromPosition(new Position(1, 0))).getCards().size())
                map.getChildren().add(generateCard(weaponPath(getString(1, 0, i)), 80, 55, -250, -30 + i * 65, 90));
            else {
                map.getChildren().add(generateCard(weaponPath("weaponCard"), 80, 55, -250, -30 + i * 65, 90));
                map.getChildren().get(i + 1).setVisible(false);
            }
            setMapCardActions((Button) map.getChildren().get(1 + i), (ImageView) map.getChildren().get(0), 1, 0, i, playerboard, map);
        }
        //blue weapon 4-6
        for (int i = 0; i < 3; i++) {
            if (i < ((RegenerationCell) board.getBillboard().getCellFromPosition(new Position(1, 0))).getCards().size())
                map.getChildren().add(generateCard(weaponPath(getString(0, 2, i)), 80, 55, 35 + i * 65, -170, 180));
            else {
                map.getChildren().add(generateCard(weaponPath("weaponCard"), 80, 55, 35 + i * 65, -170, 180));
                map.getChildren().get(i + 4).setVisible(false);
            }
            setMapCardActions((Button) map.getChildren().get(4 + i), (ImageView) map.getChildren().get(0), 0, 2, i, playerboard, map);
        }

        //yellow weapon 7-9
        for (int i = 0; i < 3; i++) {
            if (i < ((RegenerationCell) board.getBillboard().getCellFromPosition(new Position(1, 0))).getCards().size())
                map.getChildren().add(generateCard(weaponPath(getString(2, 3, i)), 80, 55, 255, 50 + i * 65, 270));
            else {
                map.getChildren().add(generateCard(weaponPath("weaponCard"), 80, 55, 255, 50 + i * 65, 270));
                map.getChildren().get(i + 7).setVisible(false);
            }
            setMapCardActions((Button) map.getChildren().get(7 + i), (ImageView) map.getChildren().get(0), 2, 3, i, playerboard, map);
        }
        //deck not to be modified 10-11
        map.getChildren().add(tableWeaponCards(weaponPath("weaponCard"), 250, -55));
        map.getChildren().add(tableWeaponCards(powerPath("powerCard"), 255, -160));

        generateBoard(map, number, actionButtons(map, map.getChildren().size()));
        for (int i = 0; i < players.size(); i++) {
            map.getChildren().add(createButton(pawnPath(giveColor(getPlayerboardPlayer(i))), 20, 20, -40, -10));

            map.getChildren().get(map.getChildren().size() - 1).setOpacity(1);
            map.getChildren().get(map.getChildren().size() - 1).setStyle("-fx-background-color: rgba(0, 100, 100, 1); -fx-background-radius: 50; -fx-padding: 5;\n" +
                    " -fx-border-width: 0;");
            ((Button) map.getChildren().get(map.getChildren().size() - 1)).borderProperty().unbind();
        }


        for (int i = 0; i < board.getSkulls(); i++)
            map.getChildren().add(addSkull(40, -245 + i * 25, -180, 0));
        for (int i = 0; i < 3; i = i + 2)
            setPowerUp((Button) playerboard.getChildren().get(2 + 2 * i), map, i);
        return map;
    }

    /**
     * This function creates playerboard for the client running this GUI
     *
     * @return playerboard of the player
     * @throws FileNotFoundException if files are not found
     */
    private Pane createBoards() throws FileNotFoundException {
        Pane playerBoard = new Pane();
        playerBoard.getChildren().add(generateCard(playerBoardPath(giveColor(player)), 110, 600, 0, 0, 0));

        //Weapons and power ups
        for (int i = 0; i < 3; i++) {
            if (i + 1 > player.getWeapons().size()) {
                playerBoard.getChildren().add(generateCard(weaponPath("weaponCard"), 110, 80, -120 + i * 40, 0, 0));

            } else
                playerBoard.getChildren().add(generateCard(weaponPath(player.getWeapons().get(i).stringGUI()), 110, 80, -120 + i * 40, 0, 0));

            if (i < player.getPowerups().size())
                playerBoard.getChildren().add(generateCard(powerPath(player.getPowerups().get(i).stringGUI()), 110, 80, 600 + i * 40, 0, 0));
            else {
                playerBoard.getChildren().add(generateCard(powerPath("powerCard"), 110, 80, 600 + i * 40, 0, 0));
                playerBoard.getChildren().get(2 * i + 2).setVisible(false);
            }
        }

        //Damages
        for (int i = 0; i < 12; i++) {
            if(i< player.getPlayerBoard().getDamageTrack().size())
                playerBoard.getChildren().add(addDamage(player.getPlayerBoard().getDamageTrack().get(i), 30, 50 + i * 35, 40, 0));//base x 50, distanza 35
            else{
                playerBoard.getChildren().add(addDamage(player.getPlayerBoard().getDamageTrack().get(i), 30, 50 + i * 35, 40, 0));//base x 50, distanza 35
                playerBoard.getChildren().get(i+7).setVisible(false);
            }
        }

        //Skulls
        for (int i = 0; i < 8; i++) {
            playerBoard.getChildren().add(addSkull(40, 120 + i * 33, 70, 0));//base x 120, distanza 33
            if (i < player.getPlayerBoard().getNumDeaths())
                playerBoard.getChildren().get(i+19).setVisible(false);
        }

        //Ammo
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                playerBoard.getChildren().add(addAmmo(playerBoard, 25, 500 + j * 25, 30 + i * 20, 0));//base x 120, distanza 33
                //da fare
            }

        //Marks
        for (Player player : players)
            for (int i = 0; i < 3; i++) {
                if (i < player.getMarks(player))
                    playerBoard.getChildren().add(addDamage(player, 30, 280 + 15 * i + 20 * players.indexOf(player), -5, 0));//base x 280, distanza 15 se identici, 20 altrimenti
                else{
                    playerBoard.getChildren().add(addDamage(player.getPlayerBoard().getDamageTrack().get(i), 30, 50 + i * 35, 40, 0));//base x 50, distanza 35
                    playerBoard.getChildren().get(i*players.indexOf(player)+27).setVisible(false);
                }
            }


        return playerBoard;

    }

    /**
     * This function creates playerboard used by the right player
     *
     * @return playerboard of the player
     * @throws FileNotFoundException if files are not found
     */
    private Pane createBoardRight() throws FileNotFoundException {
        Pane playerBoard = new Pane();
        playerBoard.getChildren().add(generateCard(playerBoardPath(giveColor(getPlayerboardPlayer(1))), 75, 280, 0, 0, 270));

        //Weapons and power ups
        for (int i = 0; i < 3; i++) {
            if (i + 1 > getPlayerboardPlayer(1).getWeapons().size()) {
                playerBoard.getChildren().add(generateCard(weaponPath("weaponCard"), 75, 50, 190, -115 + i * 35, 270));
                playerBoard.getChildren().get(2 * i + 1).setVisible(false);
            } else
                playerBoard.getChildren().add(generateCard(weaponPath(getPlayerboardPlayer(1).getWeapons().get(i).stringGUI()), 75, 50, 190, -115 + i * 35, 270));

            if (i < getPlayerboardPlayer(1).getPowerups().size())
                playerBoard.getChildren().add(generateCard(powerPath(getPlayerboardPlayer(1).getPowerups().get(i).stringGUI()), 75, 50, 190, 115 - i * 35, 270));
            else {
                playerBoard.getChildren().add(generateCard(powerPath("powerCard"), 75, 50, 190, 115 - i * 35, 270));
                playerBoard.getChildren().get(2 * i + 2).setVisible(false);
            }
        }

        //Damages
        for (int i = 0; i < 12; i++) {
            if(i< player.getPlayerBoard().getDamageTrack().size())
                playerBoard.getChildren().add(addDamage(getPlayerboardPlayer(1).getPlayerBoard().getDamageTrack().get(i), 20, 130, 135-i*16, 270));//base x 50, distanza 35
            else{
                playerBoard.getChildren().add(addDamage(getPlayerboardPlayer(1).getPlayerBoard().getDamageTrack().get(i), 20, 130, 135-i*16, 270));//base x 50, distanza 35
                playerBoard.getChildren().get(i+7).setVisible(false);
            }
        }

        //Skulls
        for (int i = 0; i < 8; i++) {
            playerBoard.getChildren().add(addSkull(20, 155, 100-i*16, 270));//base x 120, distanza 33
            if (i < player.getPlayerBoard().getNumDeaths())
                playerBoard.getChildren().get(i+19).setVisible(false);
        }

        //Ammo
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                playerBoard.getChildren().add(addAmmo(playerBoard, 15, 110 + j * 20, -60 - i * 20, 270));//base x 120, distanza 33
                //TODO da fare
            }

        //Marks
        for (Player player : players)
            for (int i = 0; i < 3; i++) {
                if (i < player.getMarks(player))
                    playerBoard.getChildren().add(addDamage(player, 15, 105 + 15 * i + 20 * players.indexOf(player), -5, 0));//base x 280, distanza 15 se identici, 20 altrimenti
                else{
                    playerBoard.getChildren().add(addDamage(player.getPlayerBoard().getDamageTrack().get(i), 15, 105, 30 - 5 * i - 20 * players.indexOf(player), 270));//base x 50, distanza 35
                    playerBoard.getChildren().get(i*players.indexOf(player)+27).setVisible(false);
                }
            }

        return playerBoard;
    }

    /**
     * This function creates playerboard used by the left player
     *
     * @return playerboard of the player
     * @throws FileNotFoundException if files are not found
     */
    private Pane createBoardLeft() throws FileNotFoundException {
        Pane playerBoard = new Pane();
        playerBoard.getChildren().add(generateCard(playerBoardPath(giveColor(getPlayerboardPlayer(2))), 75, 280, 0, 0, 90));

        //Weapons and power ups
        for (int i = 0; i < 3; i++) {
            if (i + 1 > getPlayerboardPlayer(2).getWeapons().size()) {
                playerBoard.getChildren().add(generateCard(weaponPath("weaponCard"), 75, 50, 40, -115 - i * 35, 90));
                playerBoard.getChildren().get(2 * i + 1).setVisible(false);
            } else
                playerBoard.getChildren().add(generateCard(weaponPath(getPlayerboardPlayer(2).getWeapons().get(i).stringGUI()), 75, 50, 40, -115 - i * 35, 90));
            if (i < getPlayerboardPlayer(1).getPowerups().size())
                playerBoard.getChildren().add(generateCard(powerPath(getPlayerboardPlayer(2).getPowerups().get(i).stringGUI()), 75, 50, 40, 115 - i * 35, 90));
            else {
                playerBoard.getChildren().add(generateCard(powerPath("powerCard"), 75, 50, 40, 115 - i * 35, 90));
                playerBoard.getChildren().get(2 * i + 2).setVisible(false);
            }
        }


        //Damage
        for (int i = 0; i < 12; i++) {
            if(i< player.getPlayerBoard().getDamageTrack().size())
                playerBoard.getChildren().add(addDamage(getPlayerboardPlayer(1).getPlayerBoard().getDamageTrack().get(i), 20, 130, -80 + i * 16, 90));//base x 50, distanza 35
            else{
                playerBoard.getChildren().add(addDamage(getPlayerboardPlayer(1).getPlayerBoard().getDamageTrack().get(i), 20, 130, -80 + i * 16, 90));//base x 50, distanza 35
                playerBoard.getChildren().get(i+7).setVisible(false);
            }
        }
        //Skulls
        for (int i = 0; i < 8; i++) {
            playerBoard.getChildren().add(addSkull(20, 105, -45 + i * 15, 90));//base x 120, distanza 33
            if (i < player.getPlayerBoard().getNumDeaths())
                playerBoard.getChildren().get(i+19).setVisible(false);
        }
        //Ammo
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                playerBoard.getChildren().add(addAmmo(playerBoard, 15, 110 + j * 20, 160 - i * 20, 90));//base x 120, distanza 33
                //TODO da fare
            }
        //Marks
        for (Player player : players)
            for (int i = 0; i < 3; i++) {
                if (i < player.getMarks(player))
                    playerBoard.getChildren().add(addDamage(player, 15, 160, 30 + 5 * i + 20 * players.indexOf(player), 90));//base x 280, distanza 15 se identici, 20 altrimenti
                else{
                    playerBoard.getChildren().add(addDamage(player.getPlayerBoard().getDamageTrack().get(i), 15, 160, 30 + 5 * i + 20 * players.indexOf(player), 90));//base x 50, distanza 35
                    playerBoard.getChildren().get(i*players.indexOf(player)+27).setVisible(false);
                }
            }

        return playerBoard;
    }

    /**
     * This function creates playerboard in the high board of the GUI when there are 5 players playing
     *
     * @return playerboard of the player
     * @throws FileNotFoundException if files are not found
     */
    private Pane createBoardMultiHigh(int i) throws FileNotFoundException {
        Pane playerBoard = new Pane();
        playerBoard.getChildren().add(generateCard(playerBoardPath(giveColor(getPlayerboardPlayer(i))), 50, 240, 0, 0, 180));

        for (int j = 0; j < 3; j++) {
            if (i + 1 > getPlayerboardPlayer(i).getWeapons().size()) {
                playerBoard.getChildren().add(generateCard(weaponPath("weaponCard"), 75, 50, 80 - j * 35, -65, 180));

            } else
                playerBoard.getChildren().add(generateCard(weaponPath(getPlayerboardPlayer(i).getWeapons().get(j).stringGUI()), 75, 50, 60 - j * 35, -70, 180));

            if (i < getPlayerboardPlayer(i).getPowerups().size())
                playerBoard.getChildren().add(generateCard(powerPath(getPlayerboardPlayer(i).getPowerups().get(j).stringGUI()), 75, 50, 180 - j * 35, -70, 180));
            else {
                playerBoard.getChildren().add(generateCard(powerPath("powerCard"), 75, 50, 180 - j * 35, -90, 180));

            }
        }

        //Damage
        for (int j = 0; j < 12; j++) {
            if(i< player.getPlayerBoard().getDamageTrack().size())
                playerBoard.getChildren().add(addDamage(getPlayerboardPlayer(i).getPlayerBoard().getDamageTrack().get(j), 15, 205 - j * 13, 20, 180));//base x 50, distanza 35
            else{
                playerBoard.getChildren().add(addDamage(getPlayerboardPlayer(i).getPlayerBoard().getDamageTrack().get(j), 15, 205 - j * 13, 20, 180));//base x 50, distanza 35
                playerBoard.getChildren().get(j+7).setVisible(false);
            }
        }
        //Skulls
        for (int j = 0; j < 8; j++) {
            playerBoard.getChildren().add(addSkull(15, 175 - j * 10, 0, 180));//base x 120, distanza 33
            if (i < player.getPlayerBoard().getNumDeaths())
                playerBoard.getChildren().get(i+19).setVisible(false);
        }
        //Ammo
        for (int x = 0; x < 3; x++)
            for (int j = 0; j < 3; j++) {
                playerBoard.getChildren().add(addAmmo(playerBoard, 10, 5 + j * 15, x * 15, 180));//base x 120, distanza 33
                //TODO da fare
            }
        //Marks
        for (Player player : players)
            for (int j = 0; j < 3; j++) {
                if (i < player.getMarks(player))
                    playerBoard.getChildren().add(addDamage(player, 10, 110 - 5 * j - 15 * players.indexOf(player), 40, 180));//base x 280, distanza 15 se identici, 20 altrimenti
                else{
                    playerBoard.getChildren().add(addDamage(player.getPlayerBoard().getDamageTrack().get(i), 10, 110 - 5 * j - 15 * players.indexOf(player), 40, 180));//base x 50, distanza 35
                    playerBoard.getChildren().get(i*players.indexOf(player)+27).setVisible(false);
                }
            }

        return playerBoard;
    }

    /**
     * This function creates playerboard used by the player in the higher corner if there are 4 players
     *
     * @return playerboard of the player
     * @throws FileNotFoundException if files are not found
     */
    private Pane createBoardHigh() throws FileNotFoundException {

        Pane playerBoard = new Pane();
        playerBoard.getChildren().add(generateCard(playerBoardPath(giveColor(getPlayerboardPlayer(3))), 75, 280, 0, 0, 180));

        for (int i = 0; i < 3; i++) {
            if (i + 1 > getPlayerboardPlayer(3).getWeapons().size()) {
                playerBoard.getChildren().add(generateCard(weaponPath("weaponCard"), 75, 50, -120 + i * 35, 0, 180));
                playerBoard.getChildren().get(2 * i + 1).setVisible(false);
            } else
                playerBoard.getChildren().add(generateCard(weaponPath(getPlayerboardPlayer(3).getWeapons().get(i).stringGUI()), 75, 50, -120 + i * 35, 0, 180));
            if (i < getPlayerboardPlayer(3).getPowerups().size())
                playerBoard.getChildren().add(generateCard(powerPath(getPlayerboardPlayer(3).getPowerups().get(i).stringGUI()), 75, 50, 350 + i * 35, 0, 180));
            else {
                playerBoard.getChildren().add(generateCard(powerPath("powerCard"), 75, 50, 350 + i * 35, 0, 180));
                playerBoard.getChildren().get(2 * i + 2).setVisible(false);
            }
        }

        //Damage
        for (int i = 0; i < 12; i++) {
            if(i< player.getPlayerBoard().getDamageTrack().size())
                playerBoard.getChildren().add(addDamage(getPlayerboardPlayer(3).getPlayerBoard().getDamageTrack().get(i), 20, 240 - i * 16, 30, 180));//base x 50, distanza 35
            else{
                playerBoard.getChildren().add(addDamage(getPlayerboardPlayer(3).getPlayerBoard().getDamageTrack().get(i), 20, 240 - i * 16, 30, 180));//base x 50, distanza 35
                playerBoard.getChildren().get(i+7).setVisible(false);
            }
        }
        //Skulls
        for (int i = 0; i < 8; i++) {
            playerBoard.getChildren().add(addSkull(20, 205 - i * 15, 5, 180));//base x 120, distanza 33
            if (i < player.getPlayerBoard().getNumDeaths())
                playerBoard.getChildren().get(i+19).setVisible(false);
        }
        //Ammo
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                playerBoard.getChildren().add(addAmmo(playerBoard, 25, j * 20, 5 + i * 20, 180));//base x 120, distanza 33
                //TODO da fare
            }
        //Marks
        for (Player player : players)
            for (int i = 0; i < 3; i++) {
                if (i < player.getMarks(player))
                    playerBoard.getChildren().add(addDamage(player, 15, 130 - 5 * i - 20 * players.indexOf(player), 60, 180));//base x 280, distanza 15 se identici, 20 altrimenti
                else{
                    playerBoard.getChildren().add(addDamage(player.getPlayerBoard().getDamageTrack().get(i), 15, 130 - 5 * i - 20 * players.indexOf(player), 60, 180));//base x 50, distanza 35
                    playerBoard.getChildren().get(i*players.indexOf(player)+27).setVisible(false);
                }
            }
        return playerBoard;
    }

    /**
     *
     * @param map
     * @param number
     * @param buttons
     * @throws FileNotFoundException
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
        map.getChildren().add(buttons.get(0));
        setActionPlayer(buttons.get(1), GRAB_MOVE, map, ammo, buttons);
        map.getChildren().add(buttons.get(1));
        setActionPlayer(buttons.get(2), SHOOT_MOVE, map, ammo, buttons);
        map.getChildren().add(buttons.get(2));

    }

    /**
     *
     * @param map
     * @throws FileNotFoundException
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
     *
     * @param map
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
        return "src/resources/images/gametable/pawns/" + string + ".png";
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
        obj.minHeight(0);
        obj.minWidth(0);
        obj.setMaxHeight(height);
        obj.setMaxWidth(width);
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
     *
     * @param map
     * @param size
     * @return
     */
    private ArrayList<Button> actionButtons(Pane map, int size) {
        ArrayList<Button> actionButtons = new ArrayList<>();
        actionButtons.add(activateButton(" ", MOVE, -282, 245, map, size));//"moveFrenzy2"
        actionButtons.add(activateButton(" ", GRAB_MOVE, -282, 262, map, size));//"grabFrenzy2",
        actionButtons.add(activateButton(" ", SHOOT_MOVE, -282, 280, map, size));//"shootFrenzy2",
        actionButtons.add(activateButton(" ", GRAB, -255, 70, map, size));//"grabFrenzy1",
        actionButtons.add(activateButton(" ", SHOOT, -255, 85, map, size));//"shootFrenzy1",
        return actionButtons;
    }

    /**
     * This function creates buttons to check or to use cards
     *
     * @return ArrayList of buttons
     */
    private ArrayList<Button> cardViewButtonSet() {
        ArrayList<Button> buttons = new ArrayList<>();
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
     *
     * @param string
     * @param command
     * @param transX
     * @param transY
     * @param map
     * @param size
     * @return
     */
    private Button activateButton(String string, EnumCommand command, int transX, int transY, Pane map, int size) {
        Button button = createButton(" ", 40, 10, transX, transY);
        button.rotateProperty().setValue(90);
        return button;
    }

    /**
     *
     * @param map
     * @param x
     * @param y
     * @param i
     * @param test
     * @param ammo
     * @param buttons
     * @return
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
     *
     * @param map
     * @param order
     * @param x
     * @param y
     * @param i
     * @param ammo
     * @param steps
     */
    private void illuminateCells(Pane map, EnumCommand order, int x, int y, int i, int ammo, int steps) {
        if (board.getBillboard().canMove(player.getCell(), board.getBillboard().getCellFromPosition(new Position(x, y)), steps)) {
            illuminateCell((Button) map.getChildren().get(ammo + x + y * 3 - i), true);
        } else illuminateCell((Button) map.getChildren().get(ammo + x + y * 3 - i), false);
    }

    /**
     *
     * @param cell
     * @param canReach
     */
    private void illuminateCell(Button cell, boolean canReach) {
        cell.setVisible(canReach);
        if (canReach)
            cell.setOpacity(1);
    }

    /**
     *
     * @param button
     * @param image
     */
    private void changeGraphics(Button button, ImageView image) {
        Node node = button.getGraphic();
        ImageView imageView = (ImageView) node;
        image.setImage(imageView.getImage());
    }

    /**
     *
     * @param button
     * @param image
     */
    private void changeGraphicButtons(Button button, Button image) {
        Node node = image.getGraphic();
        ImageView imageView = (ImageView) node;
        changeSizeImage(imageView, 110, 80);
        button.setGraphic(imageView);
        button.setVisible(true);

    }

    /**
     *
     * @param button
     * @param image
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
     *
     * @param name
     * @param height
     * @param width
     * @param transX
     * @param transY
     * @param grades
     * @return
     * @throws FileNotFoundException
     */
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

    /**
     *
     * @param buttonCard
     * @param image
     * @param x
     * @param y
     * @param pos
     * @param playerboard
     * @param map
     */
    //Carte relative alla mappa
    private void setMapCardActions(Button buttonCard, ImageView image, int x, int y, int pos, Pane playerboard, Pane map) {//carte della mappa posso guardarle o pescarle
        for (int i = 0; i < 3; i++) {
            weaponAction((Button) playerboard.getChildren().get(2 * i + 1), image, i);
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
                        notifyServer(new CommandObj(GRAB_WEAPON, pos));
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
     *
     * @param action
     * @param order
     * @param map
     * @param ammoSize
     * @param buttons
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
     *
     * @param cell
     * @param x
     * @param y
     * @param ammo
     * @param map
     * @param ammoSize
     * @param buttons
     */
    //Azioni che può fare cella
    private void setCellAction(Button cell, int x, int y, ImageView ammo, Pane map, int ammoSize, ArrayList<Button> buttons) {
        cell.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                switch (command) {
                    case MOVE:
                        for (int i = ammoSize; i < map.getChildren().size() - 5 - 7 - players.size() + 1; i++) {
                            illuminateCell((Button) map.getChildren().get(i), false);
                        }
                        movePawn((Button) map.getChildren().get(map.getChildren().size() - 7 - players.size() + 1));
                        notifyServer(new CommandObj(MOVE, new Position(x, y)));
                        command = CHOOSE_ACTION;
                        for (Button button : buttons)
                            button.setVisible(true);

                        break;
                    case GRAB_MOVE:
                        notifyServer(new CommandObj(MOVE, new Position(x, y)));
                        if (ammo != null)
                            ammo.setVisible(false);

                        for (int i = ammoSize; i < map.getChildren().size() - 5 - 7 - players.size() + 1; i++) {
                            illuminateCell((Button) map.getChildren().get(i), false);
                        }
                        movePawn((Button) map.getChildren().get(map.getChildren().size() - 7 - players.size() + 1));
                        notifyServer(new CommandObj(GRAB_MOVE, new Position(x, y)));
                        command = CHOOSE_ACTION;
                        for (Button button : buttons)
                            button.setVisible(true);

                        break;

                    case SHOOT_MOVE:
                        for (int i = ammoSize; i < map.getChildren().size() - 5 - 7 - players.size() + 1; i++) {
                            illuminateCell((Button) map.getChildren().get(i), false);
                        }
                        movePawn((Button) map.getChildren().get(map.getChildren().size() - 7 - players.size() + 1));
                        notifyServer(new CommandObj(SHOOT_MOVE, new Position(x, y)));
                        command = CHOOSE_ACTION;
                        for (Button button : buttons)
                            button.setVisible(true);
                        break;
                    case REG_CELL:
                        for (int i = ammoSize; i < map.getChildren().size() - 5 - 7 - players.size() + 1; i++) {
                            illuminateCell((Button) map.getChildren().get(i), false);
                        }
                        movePawn((Button) map.getChildren().get(map.getChildren().size() - 7 - players.size() + 1));
                        notifyServer(new CommandObj(REG_CELL, new Position(x, y)));
                        command = CHOOSE_ACTION;
                        for (Button button : buttons)
                            button.setVisible(true);
                        command = END_TURN;
                        break;
                    default:
                        cell.disableProperty();
                }
            }
        });
    }

    /**
     *
     * @param powerUp
     * @param map
     * @param i
     */
    //azioni power up
    private void setPowerUp(Button powerUp, Pane map, int i) {
        powerUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switch (command) {
                    case CHOOSE_ACTION:
                        if (getPlayer().getPowerups().get(i).getPowerUp() == TELEPORTER || getPlayer().getPowerups().get(i).getPowerUp() == KINETICRAY)
                            command = DISCARD_POWER;
                        else powerUp.disableProperty();
                        break;
                    case SHOOT://after shooting
                        if (getPlayer().getPowerups().get(i).getPowerUp() != VENOMGRENADE)
                            command = DISCARD_POWER;
                        else powerUp.disableProperty();
                        break;
                    case VENOMGRENADE:// after getting hit
                        if (getPlayer().getPowerups().get(i).getPowerUp() == VENOMGRENADE)
                            command = DISCARD_POWER;
                        else powerUp.disableProperty();
                        break;
                    case DISCARD_POWER: // Discard power up
                        //Chiede la carta al server e verifica se è quella
                        powerUp.setVisible(false);
                        break;
                    default:
                        powerUp.disableProperty();
                }
            }
        });
    }

    /**
     *
     * @param attack
     * @param weapon
     * @param i
     */
    private void weaponAction(Button attack, ImageView weapon, int i) {
        attack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switch (command) {
                    case SHOOT://after shooting
                        if (getPlayer().getWeapons().get(i).isReady()) {
                            changeImage(attack, weapon);
                            command = CHOOSE_ACTION;
                        } else attack.disableProperty();
                        break;
                    case DISCARD_WEAPON:// after getting hit
                        attack.setVisible(false);
                        command = CHOOSE_ACTION;
                        break;
                    default:
                        attack.disableProperty();
                }
            }
        });
    }

    /**
     *
     * @param attack
     * @param weapon
     * @param nattack
     * @param numAttacks
     * @param card
     */
    private void shootAction(Button attack, ImageView weapon, int nattack, int numAttacks, WeaponCard card) {
        attack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (player.getWeapons().get(0).getAttacks().size() == numAttacks) {
                    attack.setVisible(true);
                    //se posso usare l'attacco numero nattack allora l'attacco s'illumina
                    //poi cambia stato in base al tipo d'attacco
                } else attack.disableProperty();
            }

        });
    }

    /**
     *
     * @param playerboard
     */
    private void playerboardAction(Button playerboard) {
        playerboard.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switch (command) {
                    case LOAD_WEAPONCARD:
                        break;

                }
            }

        });
    }

    /**
     *
     * @param shooter
     * @param dimension
     * @param transX
     * @param transY
     * @param grades
     * @return
     * @throws FileNotFoundException
     */
    private ImageView addDamage(Player shooter, int dimension, int transX, int transY, int grades) throws FileNotFoundException {
        ImageView image = new ImageView(new Image(new FileInputStream(damagePath(giveColor(shooter)))));
        changeSizeImage(image, dimension, dimension);
        image.setTranslateX(transX);
        image.setTranslateY(transY);
        image.rotateProperty().setValue(grades);
        return image;
    }

    /**
     *
     * @param player
     * @return
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
     *
     * @param i
     * @return
     */
    private Player getPlayerboardPlayer(int i) {
        if (players.indexOf(player) + i > players.size())
            return players.get(players.indexOf(player) + i - players.size());
        else return players.get(players.indexOf(player) + i);
    }

    /**
     *
     * @param dimension
     * @param transX
     * @param transY
     * @param grades
     * @return
     * @throws FileNotFoundException
     */
    private ImageView addSkull(int dimension, int transX, int transY, int grades) throws FileNotFoundException {
        ImageView image = new ImageView(new Image(new FileInputStream("src/resources/images/gametable/damages/skull.png")));
        changeSizeImage(image, dimension, dimension);
        image.setTranslateX(transX);
        image.setTranslateY(transY);
        image.rotateProperty().setValue(grades);
        return image;
    }

    /**
     *
     * @param pawn
     */
    private void movePawn(Button pawn) {
        int distance = 100;
        if (board.getBillboard().getCellPosition(player.getCell()).getX() != 0)
            distance = 90;
        pawn.setTranslateX(-140 + board.getBillboard().getCellPosition(player.getCell()).getY() * 100 + 20 * (player.getCell().getPawns().size() % 3));
        pawn.setTranslateY(-110 + board.getBillboard().getCellPosition(player.getCell()).getX() * distance + 30 * (player.getCell().getPawns().size() % 2));
    }

    /**
     *
     * @param billboard
     * @param dimension
     * @param transX
     * @param transY
     * @param grades
     * @return
     * @throws FileNotFoundException
     */
    private Button addAmmo(Pane billboard, int dimension, int transX, int transY, int grades) throws FileNotFoundException {
        Button ammo = generateCard(ammoPath("201n"), dimension, dimension, transX, transY, grades);
        ammo.setVisible(true);
        return ammo;
    }

    /**
     *
     * @param ammo
     * @param playerboard
     * @param color
     */
    private void ammoAction(Button ammo, Pane playerboard, Color color) {
        ammo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switch (command) {
                    case PAYGUNSIGHT:
                        ammo.setVisible(false);
                        command = DISCARD_POWER;
                        break;
                    case DISCARD_POWER:
                        ammo.setVisible(false);
                        command = POWERUP;
                        break;
                    default:
                        ammo.disableProperty();
                }
            }

        });
    }

    /**
     *
     * @param p
     * @throws FileNotFoundException
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
            if(i < p.getPlayerBoard().getNumDeaths())
                playerBoard.getChildren().get(19+i).setVisible(true);
            else  playerBoard.getChildren().get(19+i).setVisible(false);
        }

        //Ammo
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                playerBoard.getChildren().add(addAmmo(playerBoard, 25, 500 + j * 25, 30 + i * 20, 0));//base x 120, distanza 33
                //da fare
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
     *
     * @param map
     * @param board
     * @throws FileNotFoundException
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
        } else map.getChildren().get(i + 4).setVisible(false);
    }

    //12
    //for() ammo
            for(int i = 0; i < players.size(); i++) {
               movePawn((Button) map.getChildren().get(map.getChildren().size()-board.getSkulls()-players.size()+i));
            }

            for(int i = 0; i < board.getSkulls(); i++)
                if(i> board.getSkulls())
                    map.getChildren().remove(map.getChildren().size()-1-board.getSkulls()+i);

    }

    /**
     *
     * @return
     */
    public GridPane timer(){
        GridPane pane = new GridPane();
        pane.add(new Label("Time left:"), 0, 0);
        pane.add(new Label(), 0, 1);
        pane.getChildren().get(0).setStyle("-fx-text-fill: white;");
        pane.getChildren().get(1).setStyle("-fx-text-fill: white;");
        return pane;
    }

    private void startCountDown(Pane timerText, int counter) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            int count = counter;
            public void run() {
                if(count > 0) {
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
     *
     * @param player
     * @return
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
     *
     * @param count
     * @return
     */
    private String printTime(int count){
        if(count%60>= 0 && count%60 <=9)
            return count/60 + ":0" + count%60;
        else return count/60 + ":" + count%60;
    }

    /**
     *
     * @param board
     * @param client
     * @param command
     * @throws RemoteException
     */
    public void gameStart(Board board, Client client, EnumCommand command) throws RemoteException {
        this.board = board;
        this.client = client;
        this.player = client.getPlayer();
        this.players = client.getListOfPlayers();
        this.command = command;
    }

    private void changeMessage(String string, Pane ask){
       // ask.getChildren()
    }

    @Override
    public void gameStart() {

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
        return true;
    }

    @Override
    public void myTurn(){
        this.command = CHOOSE_ACTION;
    }

    @Override
    public void notMyTurn(String nameOfWhoPlay){
        this.command = END_TURN;
    }

    @Override
    public void showBoard(){}

    @Override
    public void regeneratePlayer(){
        command = REG_CELL;
        for(int i = root.getChildren().size() - board.getSkulls()-players.size()-board.getBillboard().getCellMap().size(); i < root.getChildren().size() - board.getSkulls()-players.size(); i++)
            illuminateCell((Button)root.getChildren().get(i), true);
    }

    @Override
    public void updatePlayer(Player player){
        try {
            notifyChangesPlayerboard(player);
            if(this.player.getName().equals(player.getName()))
                this.player = player;
            players.set(players.indexOf(player), player);
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
    public void grab(){
        if(player.getCell().getClass() == RegenerationCell.class)
            command = GRAB_WEAPON;
        //TODO illumina cell
    }

    @Override
    public List<String> getTargetsName(List<Player> potentialTarget, int maxTarget){return null;}

    @Override
    public List<Integer> chooseIndexes(List<Attack> attacks, boolean canRandom){return null;}

    @Override
    public Position choosePositionToAttack(List<Position> potentialposition) {
        return null;
    }

    private void notifyServer(CommandObj obj) {
        try {
            client.sendCMD(obj);
        } catch (RemoteException e) {
            e.fillInStackTrace();
        }
    }
}

/*
TODO gestire bene segnalini mappa
caso prima riga
       for(int i = 0; i < 4; i++) {
            map.getChildren().add(addSkull(30, -140+i*90, -110, 0));
            map.getChildren().add(addSkull(30, -120+i*90, -110, 0));
            map.getChildren().add(addSkull(30, -140+i*90, -80, 0));
            map.getChildren().add(addSkull(30, -120+i*90, -80, 0));
            map.getChildren().add(addSkull(30, -140+i*90, -50, 0));
        }
        caso seconda riga
        for(int i = 0; i < 4; i++) {
            map.getChildren().add(addSkull(30, -140+i*100, -10, 0));
            map.getChildren().add(addSkull(30, -120+i*100, -10, 0));
            map.getChildren().add(addSkull(30, -140+i*100, 20, 0));
            map.getChildren().add(addSkull(30, -120+i*100, 20, 0));
            map.getChildren().add(addSkull(30, -140+i*100, 50, 0));
        }
caso terza riga
        for(int i = 0; i < 4; i++) {
            map.getChildren().add(addSkull(30, -140+i*100, 90, 0));
            map.getChildren().add(addSkull(30, -120+i*100, 90, 0));
            map.getChildren().add(addSkull(30, -140+i*100, 120, 0));
            map.getChildren().add(addSkull(30, -120+i*100, 120, 0));
            map.getChildren().add(addSkull(30, -140+i*100, 150, 0));
        }
*/
