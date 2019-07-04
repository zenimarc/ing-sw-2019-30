package client;

import board.Board;
import board.Cell;
import constants.Constants;
import controller.CommandObj;
import deck.Card;
import player.Player;
import powerup.PowerCard;
import powerup.PowerUp;
import view.View;
import weapon.WeaponCard;

import java.util.*;

import static controller.EnumCommand.DISCARD_WEAPON;
import static controller.EnumCommand.UPDATE_PLAYER;

public class ClientUpdateManager extends Observable {
    private Player player;
    private Board board;
    private View view;

    protected ClientUpdateManager() {}

    public void setView(View view) {
        this.view = view;
    }

    public void setPlayer(Player player) {
        this.player = player;
        setChanged();
        notifyObservers(player);
    }

    public void setBoard(Board board) {
        this.board = board;
        setChanged();
        notifyObservers(board);
    }

    public Player getPlayer() {
        return player;
    }

    public Board getBoard() {
        return board;
    }

    public void executeCommand(CommandObj cmd){
        switch (cmd.getCmd()) {
            case GRAB:
                view.grab((Cell) cmd.getObject());
                break;
            case SHOW_BOARD:
                view.showBoard();
                break;
            case REG_CELL:
                view.regeneratePlayer();
                break;
            case YOUR_TURN:
                view.myTurn((Constants) cmd.getObject());
                break;
            case LOAD_WEAPONCARD:
                if(cmd.getObject().getClass().equals(ArrayList.class) ) {
                   try {
                       view.loadWeapon((ArrayList<String>) cmd.getObject());
                   }catch (ClassCastException cce){
                       view.giveError("In LOAD_WEAPONCARD arrived bad ArrayList, required ArrayList<String> arrived "+
                               ((ArrayList) cmd.getObject()).get(0));
                   }
                }
                break;
            case SHOOT:
                view.shoot();
                break;
            case ASKFORPOWERUP:
                view.askPowerUp((PowerUp) cmd.getObject());
                break;
            case CHECKPOWERUP:
                view.usePowerUp();
                break;
            case PAYGUNSIGHT:
                view.payGunsight((int[])cmd.getObject(), (int) cmd.getObject2());
                break;
            case PAYPOWERUP:
                view.payPowerUp((PowerCard) cmd.getObject());
                break;
            case USE_TELEPORTER:
                view.useTeleport();
                break;
            case USE_KINETICRAY:
                view.useKineticray((List<Player>)cmd.getObject());
                break;
            case USE_GUNSIGHT:
                view.chooseGunsightTarget((List<Player>)cmd.getObject());
                break;
            case DISCARD_POWER:
                view.discardPowerUp((Card) cmd.getObject());
            case UPDATE_PLAYER:
                updatePlayer(cmd.getObject());
                break;
            case UPDATE_BOARD:
                updateBoard(cmd.getObject());
                break;
            case NOT_YOUR_TURN:
                view.notMyTurn((String) cmd.getObject());
                break;
                case DISCARD_WEAPON:
                    view.discardWeapon((WeaponCard)  cmd.getObject());
                    break;
            case PRINT_POINTS:
                if(cmd.getObject().getClass().equals(String.class) && cmd.getObject2().getClass().equals(HashMap.class)) {
                    view.giveRoundScore((String) cmd.getObject(), (Map<String, Integer>) cmd.getObject2());
                }
                break;
            default:
                break;
        }
    }

    /**
     * Update local Player
     * @param obj object received from server
     */
    private void updatePlayer(Object obj){
        if(obj.getClass().equals(Player.class)) {
            this.player = (Player) obj;
            view.updatePlayer(player);
        }
    }

    /**
     * Update local Board
     * @param obj object received from server
     */
    private void updateBoard(Object obj){
        if(obj.getClass().equals(Board.class)) {
            this.board = (Board) obj;
            view.updateBoard(board);
        }
    }

}
