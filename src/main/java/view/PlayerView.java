package view;
import board.Cell.Cell;
import board.Cell.NormalCell;
import board.Position;
import board.Cell.RegenerationCell;
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
            if(positionString.equals("canc"));
            return false;
        }

        Position newPosition = new Position(
                Integer.valueOf(positionString.split(",")[0]),
                Integer.valueOf(positionString.split(",")[1]));

        setChanged();
        notifyObservers(new CommandObj(playerCommand, newPosition));

        return true;
    }

    private boolean grab() {
        move(PlayerCommand.OPTIONAL_MOVE);

        if(player.getCell().getClass() == NormalCell.class) return grabAmmo();
        else if (player.getCell().getClass()== RegenerationCell.class) return grabWeapon();
        return false;
    }

    private boolean grabWeapon(){
        String askString = stringForChooseWeaponCard();
        String answerRegex = "[0-"+((((RegenerationCell) player.getCell()).getCards().size())-1) +"]";
        String answer;
        int index;

        System.out.println(askString);
        while (true){
            answer = reader.next();
            if(answer.matches(answerRegex)) {
                index = Integer.valueOf(answer);
                break;
            }
            printError();
        }

        setChanged();
        notifyObservers(new CommandObj(PlayerCommand.GRAB_WEAPON, player.getCell() ,index));

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

    public void printError(){
        System.out.println("----- !! Error: Illegal Action !! -----");
    }

}