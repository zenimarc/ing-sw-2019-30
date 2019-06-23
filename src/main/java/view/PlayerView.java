package view;
import attack.Attack;
import board.Cell;
import board.NormalCell;
import board.Position;
import board.RegenerationCell;
import board.billboard.Billboard;
import constants.Constants;
import controller.CommandObj;
import controller.EnumCommand;
import player.Player;
import powerup.PowerCard;
import powerup.PowerUp;
import weapon.WeaponCard;

import java.util.*;

import static constants.Color.convertToColor;
import static controller.EnumCommand.*;

/**
 * 
 */
public class PlayerView extends Observable{
    private Player player;
    private Scanner reader = new Scanner(System.in);

    /**
     * Default constructor
     */
    public PlayerView(Player player, Observer clientManager) {
        this.player = player;
        this.addObserver(clientManager);
    }

    protected void myTurn() {
        boolean toServerAction = false;
        EnumCommand command = choosePlayerAction();
        switch (command) {
            case MOVE:
                toServerAction = move(EnumCommand.MOVE);
                break;
            case GRAB:
                toServerAction = move(EnumCommand.GRAB_MOVE);
                break;
            case SHOOT:
                toServerAction = shoot();
                break;
            case POWERUP:
                notifyServer(new CommandObj(CHECKPOWERUP, POWERUP));
                toServerAction = true;
                break;
            case END_TURN:
                notifyServer(new CommandObj(EnumCommand.END_TURN));
                toServerAction = true;
                break;
            default:
                break;
        }

        if(!toServerAction) printError("Action not performed");

    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * This ask new position and notify MOVE action
     * @param enumCommand type of movement
     * @return movement success
     */
    public boolean move(EnumCommand enumCommand) {
        String positionString = "";
        while (!positionString.matches("[0-2],[0-3]")) {
            print("Where do you want to move?");
            positionString = reader.next();
            }

        Position newPosition = new Position(
                Integer.valueOf(positionString.split(",")[0]),
                Integer.valueOf(positionString.split(",")[1]));

        notifyServer(new CommandObj(enumCommand, newPosition));
        return true;
    }

    /**
     * This invoke grabAmmo or grabWeapon
     * @return
     */
    public boolean grab() {
        if(player.getCell().getClass() == NormalCell.class) return grabAmmo();
        else if (player.getCell().getClass()== RegenerationCell.class) return grabWeapon();
        return false;
    }

    /**
     * This ask which weapon want and notify that action
     * @return
     */
    private boolean grabWeapon(){
        int drawIndex = chooseWeaponCard();
        notifyServer(new CommandObj(EnumCommand.GRAB_WEAPON, drawIndex));
        return true;
    }

    /**
     * This notify to grab ammo
     * @return
     */
    private boolean grabAmmo(){
        notifyServer(new CommandObj(EnumCommand.GRAB_AMMO));
        return false;
    }

    private boolean shoot() {
        if (player.getWeapons().isEmpty()) {
            printError("You have not loaded weapon, so you can't shoot");
            return false;
        }

        int index = chooseWeaponToPlace();
        //Not want to place weapon
        if (index == -1) return false;
        //else want shoot
        WeaponCard weaponCard = player.getWeapons().get(index);

        index = chooseTypeAttack(weaponCard);

        notifyServer(new CommandObj(EnumCommand.SHOOT, weaponCard, index));

        return true;
    }

    /**
     * Ask player if he want load some weapon and notify that
     * @return
     */
    public boolean loadWeapon(){

        List<WeaponCard> notLoaded = player.getNotLoaded();
        if(notLoaded.isEmpty()) return true;

        if(wantLoad()) {
            int index = chooseWeaponToLoad();
            if(index==-1) return true;

           notifyServer(new CommandObj(EnumCommand.LOAD_WEAPONCARD, player.getNotLoaded().get(index)));

            loadWeapon();
        }
        return  true;
    }

    /**
     * This ask player if want load some weapon
     * @return
     */
    private boolean wantLoad(){
        String format = "[0-1]";
        String read = "";
        while(!read.matches(format)) {
            print("Do you want to load your weapon? [1: Yes, 0: No] ");
            read = reader.next();
        }
        return read.equals("0");
    }

    /**
     * Ask player which weapon want to load and return index
     * @return
     */
    private int chooseWeaponToLoad(){
        String format = "[0-" + player.getNotLoaded().size() + "]";
        String read = "";
        while (!read.matches(format)) {
            print("Which weapon do you want to load?\n");
            print(stringWeaponFromList(player.getNotLoaded(), true));
            read = reader.next();
        }

        return Integer.valueOf(read)-1;
    }

    /**
     * This ask player which opponents want to hit from a list
     * @param possibleTarget possible target list
     * @return player who can hit
     */
    public Player chooseTarget(List<Player> possibleTarget){
        if(possibleTarget.isEmpty()){
            printError("There are no possible targets");
            return null;
        }
        String format = "[0-"+possibleTarget.size()+"]";
        String question = stringForChooseTarget(possibleTarget);
        String read;
        int index;

        while(true){
            print(question);
            read = reader.next();
            if(read.matches(format)){
                index = Integer.valueOf(read)-1;
                break;
            }
        }
        return index==-1 ? null : possibleTarget.get(index);
    }

    /**
     * This ask player which opponents want hit from possible target
     * @param numTarget max target to hit
     * @param checkedList all possible target
     * @return list of opponents to hit
     */
    public List<Player> chooseTargets(int numTarget, List<Player> checkedList){
        ArrayList<Player> possibleTargets = (ArrayList<Player>) ((ArrayList<Player>)checkedList).clone();
        List<Player> targets = new ArrayList<>();
        Player p;

        if(possibleTargets.isEmpty()) return Collections.emptyList();

        for(int i=0; i<numTarget;i++){
            p = chooseTarget(possibleTargets);
            if(p!=null){
                targets.add(p);
                possibleTargets.remove(p);
            }else{
                break;
            }
        }
        return targets;
    }

    public boolean regeneratesPlayer(){
        int index = choosePowerUp4Regeneration();
        notifyServer(new CommandObj(EnumCommand.REG_CELL, player.getPowerups().get(index)));
        return true;
    }

    public void drawCLI(BoardViewCLI map) {
        map.drawCLI();
    }

    public void drawGUI() {
        // TODO implement here
    }

    private String stringForChooseTarget(List<Player> possibleTarget){
        StringBuilder sb = new StringBuilder();

        sb.append("Possible target are:\n");
        sb.append("0) No one\t");
        for(Player p : possibleTarget){
            sb.append(possibleTarget.indexOf(p)+1);
            sb.append(") ");
            sb.append(p.getName());
            sb.append("\t");
        }
        sb.append("\nWhich opponent do you want to shoot?");
        return sb.toString();
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
            sb.append("Set your RegenerationCell. Your PowerUpCards are:\n");
            for (PowerCard pc : powerUps) {
                sb.append(powerUps.indexOf(pc));
                sb.append(") ");
                sb.append(pc);
                sb.append('\n');
            }
            sb.append("In which Regeneration cell do you want to go? (Enter number)[0] ");

            print(sb.toString());

            slt = reader.nextInt();
            if (slt < powerUps.size()) return slt;
        }
    }

    /**
     * Generate string for choose player action
     * @return String for choose player action
     */
    private String stringForPlayerAction(){
        StringBuilder sb = new StringBuilder();
        sb.append("Possible Actions: \n");
        for(EnumCommand action : EnumCommand.PlayerAction){
            sb.append(action.ordinal());
            sb.append(") ");
            sb.append(action.getName());
            sb.append('\n');
        }
        return sb.toString();
    }

    /**
     * Ask Player wich action want to do
     * @return Player command
     */
    private EnumCommand choosePlayerAction(){
        int slt;
        String read;
        String formatString = "[0-"+ EnumCommand.PlayerAction.size()+"]";

        while(true) {
            print(stringForPlayerAction());
            print("What do you want to do?");
            read =reader.next();
            slt = read.matches(formatString) ? Integer.valueOf(read) : EnumCommand.PlayerAction.size();
            if(slt< EnumCommand.PlayerAction.size()){
                return EnumCommand.values()[slt];
            }
        }
    }

    /**
     * Ask player wich card want play (only loaded)
     * @param mex Introducion mex
     * @param query Query mex
     * @return string for choose weapon from hand
     */
    private String stringForChooseWeaponFromHand(String mex, String query){
        StringBuilder sb = new StringBuilder();
        sb.append(mex);

        sb.append(stringWeaponFromList(player.getWeapons(), true));
        sb.append(query);
        return sb.toString();
    }

    /**
         * This ask player what WeaponCard want to discard
         * @return index of WeaponCard to Discard, -1 if don't want to discard
         */
    private int chooseWeaponFromHand(String mex){
        String read;
        String formatString = "[0-"+ Constants.MAX_WEAPON_HAND_SIZE.getValue()+"]";

        while (true){
            print(mex);
            read = reader.next();
            if(read.matches(formatString)){
                break;
            }
        }

        return Integer.valueOf(read)-1;
    }

    /**
     * String for choose weapon to discard
     * @return String for choose weapon to discard
     */
    private String stringForChooseWeaponToDiscard() {
        return stringForChooseWeaponFromHand("You have just three weapon in your hand. You have:\n",
                "\nWhich do you want to discard? [0 = I don't want grab a new Weapon] ");
    }

    /**
     * Execute chooseWeaponFromHand using message for discard weapon
     * @return index of card to discard
     */
    public int chooseWeaponToDiscard(){
        return chooseWeaponFromHand(stringForChooseWeaponToDiscard());
    }

    private String stringForChooseWeaponToPlace(){
        return stringForChooseWeaponFromHand("You have this Weapon: ", "What weapon to shoot?");
    }

    /**
     * Execute chooseWeaponFromHand using message for place weaponcard
     * @return index of card to place
     */
    private int chooseWeaponToPlace(){
        return  chooseWeaponFromHand(stringForChooseWeaponToPlace());
    }

    private String stringForChooseWeaponCard(){
        RegenerationCell cell = (RegenerationCell) player.getCell();
        StringBuilder sb = new StringBuilder();
        sb.append("Weapon card in this cell are:");
        sb.append(stringWeaponFromList(cell.getCards(), false));
        sb.append("\nWhich do you want? ");
        return sb.toString();
    }

    private String stringWeaponFromList(List<WeaponCard> weaponCards, boolean nullAnswer){
        StringBuilder sb = new StringBuilder();
        int i = 0;
        if(nullAnswer){
            sb.append(i);
            sb.append(") No weapon\t");
            i++;
        }

        for(WeaponCard wp : weaponCards){
            sb.append(i);
            sb.append(") ");
            sb.append(wp);
            sb.append("\t");
            i++;
        }

        sb.append("\n");
        return sb.toString();

    }

    /**
     * This ask player which WeaponCard want to draw in a RegenerationCell
     * @return index of chosen WeaponCard
     */
    private int chooseWeaponCard(){
        String read;
        String formatString = "[0-"+((((RegenerationCell) player.getCell()).getCards().size())-1) +"]";

        while (true) {
            print(stringForChooseWeaponCard());
            read = reader.next();
            if (read.matches(formatString)) {
                break;
            } else {
                printError();
            }
        }
        return Integer.valueOf(read);
    }

    private String stringOnlyBaseAttack(WeaponCard wc){
        StringBuilder sb = new StringBuilder();
        sb.append("\t1)");
        sb.append(wc.getBaseAttack());
        sb.append("\n");
        return sb.toString();
    }

    private String stringOptionalAttack(WeaponCard wc){
        StringBuilder sb = new StringBuilder();

        sb.append("\t1)If you want use only base attack:\n");
        sb.append("\t\t");
        sb.append(wc.getBaseAttack());
        sb.append("\n");
        sb.append("\t2)If you want use one (or more) optional attack(s):");
        for(Attack attack : wc.getAttacks()){
            sb.append("\n\t\t");
            sb.append(attack);
        }
        sb.append('\n');

        return sb.toString();
    }

    private String stringForChooseAttackInList(List<Attack> attacks){
        StringBuilder sb = new StringBuilder();

        sb.append("What optional attack do you want to use?\n");
        sb.append("\t0)No one\n");
        for(Attack attack : attacks){
            sb.append('\t');
            sb.append(attacks.indexOf(attack)+1);
            sb.append(")");
            sb.append(attack);
            sb.append('\n');
        }
        return sb.toString();
    }

    /**
     * This ask user what optional attack want use
     * @param wc selected weapon card
     * @param canRandom user can use optional attack whitout order
     * @return list of index of choosen optional attack
     */
    public List<Integer> chooseOptionalAttack(WeaponCard wc, boolean canRandom){
        ArrayList<Integer> indexes = new ArrayList<>();
        String format;
        String read;

        List<Attack> attacks = new ArrayList<>(wc.getAttacks());
        if(attacks.isEmpty()) return Collections.emptyList();

        if(canRandom) {
            while (true) {
                format = "[0-"+attacks.size()+"]";
                print(stringForChooseAttackInList(attacks));
                read = reader.next();
                if(read.matches(format)) {
                    if (!read.equals("0")){
                        indexes.add(wc.getAttacks().indexOf(attacks.get(Integer.valueOf(read)-1)));
                        attacks.remove(Integer.valueOf(read)-1);
                    }
                    else break;
                }

            }
        }
        return indexes;
    }

    private String stringAlternativeAttack(WeaponCard wc){
        StringBuilder sb = new StringBuilder();

        sb.append("\tBase attack is:\n\t");
        sb.append(stringOnlyBaseAttack(wc));
        sb.append("\tor else you can choose alterative attack:\n");
        sb.append("\t\t2)");
        sb.append(wc.getAlternativeAttack());
        return sb.toString();
    }

    /**
     * Create string for choose which attack want to use
     * @param wc WeaponCard
     * @return String contains possible attack
     */
    private String stringForChooseAttack(WeaponCard wc) {
        StringBuilder sb = new StringBuilder();

        sb.append("Attacks for ");
        sb.append(wc.getName());
        sb.append(":\n");

        if(wc.getAttacks().isEmpty() && wc.getAlternativeAttack()==null){
            sb.append(stringOnlyBaseAttack(wc));
        }else if(!wc.getAttacks().isEmpty()){
            sb.append(stringOptionalAttack(wc));
        }else if(wc.getAlternativeAttack()!=null){
            sb.append(stringAlternativeAttack(wc));
        }

        sb.append("\nWhich do you prefer? [0 not attack]");
        return sb.toString();
    }

    /**
     * This ask player which attack want to use: BaseAttack or use OptionalAttack or use AlternativeAttack
     * @param wc WeaponCard
     * @return index of attack to use
     */
    private int chooseTypeAttack(WeaponCard wc){
        String read;
        int max = (wc.getAlternativeAttack()==null && wc.getAttacks().isEmpty()) ? 1 : 2;

        String format = "[0-"+max+"]";
        String question = stringForChooseAttack(wc);

        while(true){
            print(question);
            read = reader.next();
            if(read.matches(format)){
                break;
            }
        }
        return Integer.valueOf(read)-1 ;
    }

    private String stringForChooseCell(String mex, String query, Billboard billboard, List<Cell> cells){
        StringBuilder sb = new StringBuilder();

        sb.append(mex);
        sb.append("\nPossible position:\n");
        for(Cell cell : cells){
            sb.append('\t');
            sb.append(cells.indexOf(cell)+1);
            sb.append(")");
            sb.append(billboard.getCellPosition(cell));
            sb.append('\n');
        }
        sb.append(query);
        return  sb.toString();
    }

    public Cell chooseCellToAttack(Billboard billboard, List<Cell> cells){
        String mex = stringForChooseCell("This is an AreaAttack.", "Where do you want shoot? ", billboard, cells);
        String format = "[1-"+cells.size()+"]";
        String read;
        while (true) {
            print(mex);
            read = reader.next();
            if (read.matches(format)) {
                return cells.get(Integer.valueOf(read)-1);
            }
        }
    }

    private boolean discardOrPayPowerUp(PowerCard powerUp){
        if (player.canPayPowerUp(powerUp)) {
            System.out.print("Do you want to pay or to discard your power up?");
            return true;
        }
        else return false;
    }

    private String choosePowerUp(List<PowerCard> powerups){
        StringBuilder sb = new StringBuilder();

        sb.append("Usable Power ups are:\n");
        sb.append("0) No one\t");
        for(PowerCard power : powerups){
                sb.append(powerups.indexOf(power) + 1);
                sb.append(") ");
                sb.append(power.toString());
                sb.append("\t");
        }
        sb.append("\nWhich Power up do you want to use?");
        return sb.toString();
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
        if(mex.equals("")){
            sb.append("Illegal Action");
        }
        else{
            sb.append(mex);
        }
        sb.append(" -----");
        print(sb.toString());
    }

    public void print(String string){
        System.out.println(string);
    }

    public String stringForTurnOf(String name){
        StringBuilder sb = new StringBuilder();
        String nameOf = "Turn of: ";

        int totStar = 120;
        int i;

        StringBuilder sbStar = new StringBuilder();

        sbStar.append('\n');
        for(i=0;i<totStar;i++){sbStar.append('*');}
        sbStar.append('\n');
        int numOfStar = (totStar - name.length() - nameOf.length() -4)/2;
        for(i=0;i<numOfStar;i++){sbStar.append('*');}
        sbStar.append("  ");

        sb.append(sbStar.toString());
        sb.append(nameOf);
        sb.append(name.toUpperCase());
        sb.append(sbStar.reverse().toString());

        return sb.toString();
    }

    private void notifyServer(CommandObj cmd){
        setChanged();
        notifyObservers(cmd);
    }

    public boolean askForPowerUp(ArrayList<PowerCard> powerCards){
        String format = "[0-1]";
        String read = "";
        while (!read.matches(format)) {
            print("Do you want to use a Power up? [1: Yes, 0: No] ");
            print(choosePowerUp(powerCards));
            read = reader.next();
        }
        return Boolean.valueOf(read);
    }

    public boolean usePowerUp(ArrayList<PowerCard> powerCards) {
        if (powerCards.isEmpty()) {
            printError("You have no power ups, so you can't use one");
            return false;
        }
        choosePowerUp(powerCards);
        String format = "[0-" + player.getPowerups().size() + "]";
        String read = "";
        while (!read.matches(format)) {
            print("Which power up do you want to use?\n");
            print(choosePowerUp(powerCards));
            read = reader.next();
        }
        if(powerCards.get(0).getPowerUp() == PowerUp.GUNSIGHT)
            notifyServer(new CommandObj(PAYGUNSIGHT, player.getPowerups().get(Integer.valueOf(read)-1)));
        else notifyServer(new CommandObj(PAYPOWERUP, player.getPowerups().get(Integer.valueOf(read)-1)));
        return true;
    }

    public void askPayGunsight(int[] payCubeGunsight, PowerCard power) {
        String format = "[0-"+ payCubeGunsight.length+"]";
        String read = "";

        while (!read.matches(format)) {
            print("Which cube do you want to use to pay? [0: Red, 1: Yellow, 2: Blue]");
            read = reader.next();
        }
        notifyServer(new CommandObj(GUNSIGHTPAID, power, convertToColor(Integer.valueOf(read))));
    }

    public void askToPay(PowerCard power) {
        String format = "[0-1]";
        String read = "";

        while (!read.matches(format)) {
            print("Do you want to pay or to discard your power up? [1: Discard, 0: Pay]");
            read = reader.next();
        }
        notifyServer(new CommandObj(PAIDPOWERUP, power, Boolean.valueOf(read)));
    }

    public boolean moveTeleporter() {
        return move(TELEPORTER);
    }

    public boolean moveKineticray(Player player, List<Position> cells) {
        String positionString = "";
        while (!positionString.matches("[0-2],[0-3]")) {
            print("Where do you want to move?");
            positionString = reader.next();
            Position newPosition = new Position(
                    Integer.valueOf(positionString.split(",")[0]),
                    Integer.valueOf(positionString.split(",")[1]));
            if (cells.contains(newPosition)) {
                notifyServer(new CommandObj(KINETICRAY, player, newPosition));
                return true;
            }
        }
        return false;
    }

}