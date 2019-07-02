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
import java.util.stream.Collectors;

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
                toServerAction = move(MOVE);
                break;
            case GRAB:
                toServerAction = move(GRAB_MOVE);
                break;
            case SHOOT:
                toServerAction = shoot();
                break;
            case POWERUP:
                notifyServer(new CommandObj(CHECKPOWERUP, POWERUP, true));
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
    public boolean grab(Cell cell) {
        if(cell.getClass() == NormalCell.class) return grabAmmo();
        else if (cell.getClass()== RegenerationCell.class) return grabWeapon((RegenerationCell) cell);
        return false;
    }

    /**
     * This ask which weapon want and notify that action
     * @return
     */
    private boolean grabWeapon(RegenerationCell cell){
        int drawIndex = chooseWeaponCard(cell);
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
     * Asks player if he wants to load some weapons, then notifies server of his choice
     * @return
     */
    public boolean loadWeapon(List<String> notLoaded){

        if(notLoaded.isEmpty()) return false;

        if(wantLoad()) {
            int index = chooseWeaponToLoad(notLoaded);
            if (index == -1) return false;

            notifyServer(new CommandObj(EnumCommand.LOAD_WEAPONCARD, index));

            notLoaded.remove(index);
            loadWeapon(notLoaded);

        }
        return true;
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
        return read.equals("1");
    }

    /**
     * Ask player which weapon want to load and return index
     * @return
     */
    private int chooseWeaponToLoad(List<String> notLoaded){
        String format = "[0-" + notLoaded.size() + "]";
        String read = "";
        while (!read.matches(format)) {
            print("Which weapon do you want to load?\n");
            print(stringWeaponFromList(notLoaded, true));
            read = reader.next();
        }
        return Integer.valueOf(read)-1;
    }

    /**
     * This ask player which opponents want to hit from a list
     * @param possibleTarget possible target list
     * @return player who can hit
     */
    private Player chooseTarget(List<Player> possibleTarget){
        String format = "[0-"+possibleTarget.size()+"]";
        print("Possible target are:\n");
        print("0) No one\t");

        String question = stringForChooseTarget(possibleTarget);
        print("\nWhich opponent do you want to shoot?");
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

        if(checkedList.isEmpty()){
            printError("There are no possible targets");
            return Collections.emptyList();
        }

        ArrayList<Player> possibleTargets = new ArrayList<>();
        possibleTargets.addAll(checkedList);

        List<Player> targets = new ArrayList<>();
        Player p;

        for(int i=0; i<numTarget;i++){
            p = chooseTarget(possibleTargets);
            if(p!=null){
                targets.add(p);
                possibleTargets.remove(p);
                if(possibleTargets.isEmpty()) return targets;
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


        for(Player p : possibleTarget){
            sb.append(possibleTarget.indexOf(p)+1);
            sb.append(") ");
            sb.append(p.getName());
            sb.append("\t");
        }

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

        sb.append(stringWeaponFromList(player.getWeapons().stream().map(WeaponCard::getName).collect(Collectors.toList()),
                true));
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

    private String stringForChooseWeaponCard(List<WeaponCard> weaponCards){
        StringBuilder sb = new StringBuilder();
        sb.append("Weapon card in this cell are:");
        sb.append(stringWeaponFromList(weaponCards.stream()
                .map(x -> {
                    if(x!=null) return x.getName();
                    else return "No weapon";
                })
                .collect(Collectors.toList()), false));
        sb.append("\nWhich do you want? ");
        return sb.toString();
    }

    private String stringWeaponFromList(List<String> weaponCardsName, boolean nullAnswer){
        StringBuilder sb = new StringBuilder();
        int i = 0;
        if(nullAnswer){
            sb.append(i);
            sb.append(") No weapon\t");
            i++;
        }
        for(String wp : weaponCardsName){
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
    private int chooseWeaponCard(RegenerationCell cell){
        String read;
        String formatString = "[0-"+((cell.getCards().size())-1) +"]";

        while (true) {
            print(stringForChooseWeaponCard(cell.getCards()));
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

        sb.append("\t1)If you want to use only base attack:\n");
        sb.append("\t\t");
        sb.append(wc.getBaseAttack());
        sb.append("\n");
        sb.append("\t2)If you want to use one (or more) optional attack(s):");
        for(Attack attack : wc.getAttacks()){
            sb.append("\n\t\t");
            sb.append(attack);
        }
        sb.append('\n');

        return sb.toString();
    }

    private String stringForChooseAttackInList(List<Attack> attacks){
        StringBuilder sb = new StringBuilder();

        sb.append("Which optional attack do you want to use?\n");
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
     * @param canRandom user can use optional attack whitout order
     * @return list of index of choosen optional attack
     */
    public List<Integer> chooseOptionalAttack(List<Attack> attacks, boolean canRandom){
        ArrayList<Integer> indexes = new ArrayList<>();
        String format;
        String read;

        if(attacks.isEmpty()) return Collections.emptyList();

        List<Attack> attackList = new ArrayList<>();
        attackList.addAll(attacks);

        if(canRandom) {
            while (true) {
                format = "[0-"+attackList.size()+"]";
                print(stringForChooseAttackInList(attackList));
                read = reader.next();
                if(read.matches(format)) {
                    if (!read.equals("0")){
                        indexes.add(attacks.indexOf(attackList.get(Integer.valueOf(read)-1)));
                        attackList.remove(Integer.valueOf(read)-1);
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
        String read = "";
        int max = (wc.getAlternativeAttack()==null && wc.getAttacks().isEmpty()) ? 1 : 2;

        String format = "[0-"+max+"]";
        String question = stringForChooseAttack(wc);

        while(!read.matches(format)){
            print(question);
            read = reader.next();
        }
        return Integer.valueOf(read)-1 ;
    }

    private String stringForChooseCell(String mex, String query, List<Position> positions){
        StringBuilder sb = new StringBuilder();

        sb.append(mex);
        sb.append("\nPossible position:\n");
        for(Position position : positions){
            sb.append('\t');
            sb.append(positions.indexOf(position)+1);
            sb.append(")");
            sb.append(position);
            sb.append('\n');
        }
        sb.append(query);
        return  sb.toString();
    }

    public Position chooseCellToAttack(List<Position> positions){
        String mex = stringForChooseCell("This is an AreaAttack.", "Where do you want shoot? ", positions);
        String format = "[1-"+positions.size()+"]";
        String read;
        while (true) {
            print(mex);
            read = reader.next();
            if (read.matches(format)) {
                return positions.get(Integer.valueOf(read)-1);
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
        String starLine = starLine();

        StringBuilder sbStar = new StringBuilder();

        sbStar.append('\n');
        sbStar.append(starLine);
        sbStar.append('\n');
        sbStar.append(wordInStar("Turn of: "+ name.toUpperCase()));
        sbStar.append('\n');
        sbStar.append(starLine);
        sbStar.append('\n');

        return sbStar.toString();
    }

    private String starLine(){
        return starLine(Constants.STAR_PER_LINE.getValue());
    }

    private String starLine(int lenght){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<lenght; i++){sb.append('*');}
        return sb.toString();
    }

    private String wordInStar(String string){
        int numOfStar = (Constants.STAR_PER_LINE.getValue() - string.length() - 4)/2;

        StringBuilder sb = new StringBuilder();

        sb.append(starLine(numOfStar));
        sb.append("  ");
        sb.append(string);
        sb.append("  ");
        sb.append(starLine(numOfStar));
        sb.append('\n');

        return sb.toString();
    }


    public void giveRoundScore(String nameDead, Map<String, Integer> points) {
        StringBuilder sb = new StringBuilder();
        String starLine = starLine();

        sb.append("\n\n");
        sb.append(starLine);
        sb.append("\n");
        sb.append(wordInStar(nameDead.toUpperCase()+" is dead"));
        sb.append(starLine);
        sb.append("\n");
        sb.append(wordInStar("Points are:"));
        for(String playerName : points.keySet()){
            sb.append(wordInStar(playerName+'\t'+points.get(playerName)));
        }
        sb.append(starLine);
        sb.append("\n\n");
        print(sb.toString());
    }

    private void notifyServer(CommandObj cmd){
        setChanged();
        notifyObservers(cmd);
    }

    public void askForPowerUp(ArrayList<PowerCard> powerCards, PowerUp power){
        String format = "[0-1]";
        String read = "";
        while (!read.matches(format)) {
            print("Do you want to use a Power up? [1: Yes, 0: No] ");
            print(choosePowerUp(powerCards));
            read = reader.next();
        }
        notifyServer(new CommandObj(CHECKPOWERUP, power, (Boolean.valueOf(read))));
    }

    public boolean usePowerUp() {
        if (player.getPowerups().isEmpty()) {
            printError("You have no power ups, so you can't use one");
            return false;
        }
        choosePowerUp(player.getPowerups());
        String format = "[0-" + player.getPowerups().size() + "]";
        String read = "";
        while (!read.matches(format)) {
            print(choosePowerUp(player.getPowerups()));
            read = reader.next();
        }
        if(Integer.valueOf(read) != 0) {
            if (player.getPowerups().get(Integer.valueOf(read) - 1).getPowerUp() == PowerUp.GUNSIGHT)
                notifyServer(new CommandObj(PAYGUNSIGHT, Integer.valueOf(read) - 1));
            else notifyServer(new CommandObj(PAYPOWERUP, Integer.valueOf(read) - 1));
        }
        else notifyServer(new CommandObj(PAYPOWERUP, Integer.valueOf(read) - 1));
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
        boolean bool = false;

        while (!read.matches(format)) {
            print("Do you want to pay or to discard your power up? [0: Pay, 1: Discard]");
            read = reader.next();
        }
        if(Integer.valueOf(read) == 1)
            bool = true;
        notifyServer(new CommandObj(PAIDPOWERUP, power, bool));
    }

    protected void moveTeleporter() {
        move(TELEPORTER);
    }

    protected void chooseTargets(List<Player> players){
        String target = "";
        while (!target.matches("[0-"+ players.size() +"]") || Integer.valueOf(target) == 0) {
            print("Which player do you want to move?");
            print("Possible target are:\n");
            print(stringForChooseTarget(players));
            target = reader.next();
        }
        moveKineticray(Integer.valueOf(target)-1);
    }

    private void moveKineticray(int player) {
        String positionString = "";
        while (!positionString.matches("[0-2],[0-3]")) {
            print("Where do you want to move?");
            positionString = reader.next();
            Position newPosition = new Position(
                    Integer.valueOf(positionString.split(",")[0]),
                    Integer.valueOf(positionString.split(",")[1]));
            notifyServer(new CommandObj(KINETICRAY, player, newPosition));
        }

    }

}