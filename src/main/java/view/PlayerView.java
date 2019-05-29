package view;
import board.Cell;
import board.NormalCell;
import board.Position;
import board.RegenerationCell;
import constants.Constants;
import controller.CommandObj;
import controller.PlayerCommand;
import player.Player;
import powerup.PowerCard;
import weapon.WeaponCard;

import java.util.*;

/**
 * 
 */
public class PlayerView extends Observable{
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

    public void myTurn() {

        int index = choosePlayerAction();

        switch (index) {
            case 0:
                move(PlayerCommand.MOVE);
                break;
            case 1:
                grab();
                break;
            case 4:
                setChanged();
                notifyObservers(new CommandObj(PlayerCommand.END_TURN));
                break;
            default:
                break;
        }
    }


    public boolean move(PlayerCommand playerCommand) {
        String positionString;
        while (true) {
            System.out.println("In che cella vuoi andare? ");
            positionString = reader.next();

            if(positionString.matches("[0-2]+,+[0-3]")){
                break;
            }
        }

        Position newPosition = new Position(
                Integer.valueOf(positionString.split(",")[0]),
                Integer.valueOf(positionString.split(",")[1]));

        setChanged();
        notifyObservers(new CommandObj(playerCommand, newPosition));
        return true;
    }

    private boolean grab() {
        move(PlayerCommand.GRAB_MOVE);

        if(player.getCell().getClass() == NormalCell.class) return grabAmmo();
        else if (player.getCell().getClass()== RegenerationCell.class) return grabWeapon();
        return false;
    }

    private boolean grabWeapon(){
        int drawIndex = chooseWeaponCard();

        setChanged();
        notifyObservers(new CommandObj(PlayerCommand.GRAB_WEAPON, drawIndex));
        return true;
    }

    private boolean grabAmmo(){
        setChanged();
        notifyObservers(new CommandObj(PlayerCommand.GRAB_AMMO, player.getCell(),0));
        return false;
    }

    public boolean regPawn(){
        int index = choosePowerUp4Regeneration();
        setChanged();
        notifyObservers(new CommandObj(PlayerCommand.REG_CELL, player.getPowerups().get(index)));
        return true;
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

    private String stringForPlayerAction(){
        StringBuilder sb = new StringBuilder();
        sb.append("Possible Action: \n");
        for(PlayerCommand action : PlayerCommand.PlayerAction){
            sb.append(action.ordinal());
            sb.append(") ");
            sb.append(action.getName());
            sb.append('\n');
        }
        return sb.toString();
    }

    private int choosePlayerAction(){
        int slt;
        String read;
        String formatString = "[0-"+PlayerCommand.PlayerAction.size()+"]";

        while(true) {
            System.out.println(stringForPlayerAction());
            System.out.println("What do you want?");
            read =reader.next();
            slt = read.matches(formatString) ? Integer.valueOf(read) : PlayerCommand.PlayerAction.size();
            if(slt<PlayerCommand.PlayerAction.size()){ return slt;}
        }
    }

    private String stringForChooseWeaponToDiscard(){
        StringBuilder sb = new StringBuilder();
        sb.append("You have just three weapon in your hand. You have:\n");
        for(WeaponCard weaponCard : player.getWeapons()){
            sb.append((player.getWeapons().indexOf(weaponCard))+1);
            sb.append(") ");
            sb.append(weaponCard);
            sb.append("\t");
        }
        sb.append("\nWhich do you want to discard? [0 = I don't want grab a new Weapon] ");

        return sb.toString();
    }

    /**
     * This ask player what WeaponCard want to discard
     * @return index of WeaponCard to Discard, -1 if don't want to discard
     */
    public int chooseWeaponToDiscard(){
        String read;
        String formatString = "[0-"+ Constants.MAX_WEAPON_HAND_SIZE.getValue()+"]";

        while (true){
            System.out.println(stringForChooseWeaponToDiscard());
            read = reader.next();

            if(read.matches(formatString)){
                return Integer.valueOf(read)-1;
            }else {
                printError();
            }
        }
    }

    private String stringForChooseWeaponCard(){
        RegenerationCell cell = (RegenerationCell) player.getCell();
        StringBuilder sb = new StringBuilder();
        sb.append("Weapon card in this cell are:");
        for(WeaponCard weaponCard : cell.getCards()){
            sb.append('\t');
            sb.append(cell.getCards().indexOf(weaponCard));
            sb.append(") ");
            sb.append(weaponCard);
        }
        sb.append("\nWhich do you want? ");
        return sb.toString();
    }

    /**
     * This ask player which WeaponCard want to draw in a RegenerationCell
     * @return index of chosen WeaponCard
     */
    private int chooseWeaponCard(){
        String read;
        String formatString = "[0-"+((((RegenerationCell) player.getCell()).getCards().size())-1) +"]";

        System.out.println(stringForChooseWeaponCard());
        while (true){
            read = reader.next();
            if(read.matches(formatString)) {
                return Integer.valueOf(read);
            }
            printError();
        }
    }

    /**
     * Print default error
     */
    public void printError(){
        printError("");
    }
    /**
     * Print error in error format
     * @param mex error message
     */
    public void printError(String mex){
        StringBuilder sb = new StringBuilder();
        sb.append("----- ERR: ");
        if(mex==""){
            sb.append("Illegal Action");
        }
        else{
            sb.append(mex);
        }
        sb.append(" -----");
        System.out.println(sb.toString());
    }



}