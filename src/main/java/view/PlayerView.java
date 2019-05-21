package view;
import board.Cell;
import constants.Constants;
import controller.CommandObj;
import controller.PlayerCommand;
import player.Player;
import powerup.PowerCard;
import weapon.WeaponCard;

import java.util.*;

import static constants.Constants.ACTION_PER_TURN_NORMAL_MODE;

/**
 * 
 */
public class PlayerView extends Observable {
    private Player player;
    private PlayerBoardView myPlayerBoard;
    private Scanner reader = new Scanner(System.in);

    /**
     * Default constructor
     */
    public PlayerView(Player player, Observer playerController) {
        this.player = player;
        this.addObserver(playerController);
    }

    public void myTurn(){
        int index;
        Cell cell;
        int cmdSel;

        //Set cell if pawn not in billboard
        if(player.getCell()==null){
            index = choosePowerUp4Regeneration();
            setChanged();
            notifyObservers(new CommandObj(PlayerCommand.REG_CELL, player.getPowerups().get(index).getColor()));
        }

        int numAction = 0;
        while(numAction< ACTION_PER_TURN_NORMAL_MODE.getValue()){
            index = choosePlayerAction();

            switch (index){
                case 0:
                    move();
                    break;
                case 4:
                    numAction = ACTION_PER_TURN_NORMAL_MODE.getValue();
                    break;
                    default:
                        break;
            }

        //    setChanged();
        //    notifyObservers(new CommandObj(PlayerCommand.getPlayerActionFromIndex(index), ---, ---)); //TODO scelta cella e slt
        }
    }


    public boolean move() {
        System.out.println("In che cella vuoi andare? ");


        return false;
    }

    public boolean grab(Cell cell) {
        /**if(player.Playerboard.damageTrack[2)!= null)
         *  player.move(cell);
         * getCard(int i);
         * player.turns--;
         */
        return false;
    }

    public boolean shoot(Cell cell, WeaponCard weapon) {
        // TODO implement here
        return false;
    }

    public void chooseTarget(){
        //TODO implement here
    }

    public void drawCLI(BoardViewCLI map) {
        map.drawCLI();
    }


    public void drawGUI() {
        // TODO implement here
    }

    /**
     * This ask player what powerUpCard wants discard to be regenerated
     * @return
     */
    private int choosePowerUp4Regeneration(){
        int slt;
        List<PowerCard> powerUps = player.getPowerups();

        while (true) {
            StringBuilder sb = new StringBuilder();
          //  sb.append(getPlayerToPrint());
            sb.append("Set your RegenerationCell. Your PowerUpCard are:\n");
            for (PowerCard pc : powerUps) {
                sb.append(powerUps.indexOf(pc));
                sb.append(") ");
                sb.append(pc);
                sb.append('\n');
            }
            sb.append("What RegenerationCell color you want? (Enter number)[0] ");

            System.out.print(sb.toString());

            slt = reader.nextInt();
            if (slt < powerUps.size()) return slt;
        }
    }

    private String getStringPlayerAction(){
        StringBuilder sb = new StringBuilder();
        sb.append("Possible Action: ");
        for(PlayerCommand action : PlayerCommand.PlayerAction){
            sb.append(action.ordinal());
            sb.append(") ");
            sb.append(action.getName());
            sb.append('\n');
        }
        return sb.toString();
    }

    private int choosePlayerAction(){
        System.out.println(getStringPlayerAction());
        System.out.println("What do you want?");
        return reader.nextInt();
    }


}