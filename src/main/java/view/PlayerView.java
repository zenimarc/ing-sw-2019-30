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
import deck.Card;
import player.Player;
import powerup.PowerCard;
import powerup.PowerUp;
import weapon.WeaponCard;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import static constants.Color.convertToColor;
import static controller.EnumCommand.*;

/**
 * PlayerView is used to ask a player what he wants to do
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

    /**
     *Play turn in normal mode
     */
    private boolean normalModeMyTurn(){
        boolean toServerAction = false;
        EnumCommand command = choosePlayerAction(PlayerAction);
        switch (command) {
            case MOVE:
                toServerAction = move(MOVE);
                break;
            case GRAB:
                toServerAction = move(GRAB_MOVE);
                break;
            case SHOOT:
                notifyServer(new CommandObj(SHOOT_CHECK));
                toServerAction = true;
                break;
            case POWERUP:
                notifyServer(new CommandObj(CHECK_EVERY_TIME_POWER_UP, true));
                toServerAction = true;
                break;
            case END_TURN:
                notifyServer(new CommandObj(EnumCommand.END_TURN));
                toServerAction = true;
                break;
            default:
                break;
        }
        return toServerAction;
    }

    /**
     * Asks player action to do in FFrenzy if player is before first player
     * @return good action
     */
    private boolean finalFrenzyBeforeFirst(){
        boolean toServerAction = false;
        EnumCommand command = choosePlayerAction(PlayerActionFF_BEFORE);
        switch (command){
            case SHOOT_MOVE_FRENZY_BEFORE_FIRST:
                toServerAction = (move(SHOOT_MOVE_FRENZY_BEFORE_FIRST));
                break;
            case MOVE_FRENZY:
                toServerAction = move(command);
                break;
            case GRAB_MOVE_FRENZY_BEFORE_FIRST:
                toServerAction = move(GRAB_MOVE_FRENZY_BEFORE_FIRST);
                break;
            case END_TURN:
                notifyServer(new CommandObj(EnumCommand.END_TURN));
                toServerAction = true;
                break;
                default:
                    break;

        }
        return toServerAction;
    }

    /**
     * Asks player action to do in FFrenzy if player is after first player or is first player
     * @return good action
     */
    private boolean finalFrenzyAfterFirst(){
        boolean toServerAction = false;
        EnumCommand command = choosePlayerAction(PlayerActionFF_AFTER);
        switch (command){
            case SHOOT_MOVE_FRENZY_AFTER_FIRST:
                toServerAction = move(SHOOT_MOVE_FRENZY_AFTER_FIRST);
                break;
            case GRAB_MOVE_FRENZY_AFTER_FIRST:
                toServerAction = move(GRAB_MOVE_FRENZY_AFTER_FIRST);
                break;
            case END_TURN:
                notifyServer(new CommandObj(EnumCommand.END_TURN));
                toServerAction = true;
                break;
            default:
                break;

        }
        return toServerAction;
    }

    /**
     * Select correct option-menu to ask player which action he wants to do
     * @param modAction normal turn, ff after first, ff before first
     */
    protected void myTurn(Constants modAction) {
        boolean toServerAction = false;

        switch (modAction) {
            case ACTION_PER_TURN_NORMAL_MODE:
                toServerAction = normalModeMyTurn();
                break;
            case ACTION_PER_TURN_FF_BEFORE_FIRST:
                toServerAction = finalFrenzyBeforeFirst();
                break;
            case ACTION_PER_TURN_FF_AFTER_FIRST:
                toServerAction = finalFrenzyAfterFirst();
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
     * This asks new position and notify MOVE action
     * @param enumCommand type of movement
     * @return true if movement was successful
     */
    public boolean move(EnumCommand enumCommand) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String positionString = "";

        while (!positionString.matches("[0-2],[0-3]")) {
            print("Where do you want to move?");
            try {
                positionString = in.readLine();
            }catch (IOException ioe){
                ioe.fillInStackTrace();
            }
        }

        Position newPosition = new Position(
                Integer.valueOf(positionString.split(",")[0]),
                Integer.valueOf(positionString.split(",")[1]));

        notifyServer(new CommandObj(enumCommand, newPosition));
        return true;
    }

    /**
     * This function invokes grabAmmo or grabWeapon
     * @return the result of grab
     */
    public boolean grab(Cell cell) {
        if(cell.getClass() == NormalCell.class) return grabAmmo();
        else if (cell.getClass()== RegenerationCell.class) return grabWeapon((RegenerationCell) cell);
        return false;
    }

    /**
     * This function asks which weapon the player wants and notifies that action
     * @return true
     */
    private boolean grabWeapon(RegenerationCell cell){
        int drawIndex = chooseWeaponCard(cell);
        notifyServer(new CommandObj(EnumCommand.GRAB_WEAPON, drawIndex));
        return true;
    }

    /**
     * This notifies to grab ammo
     * @return false
     */
    private boolean grabAmmo(){
        notifyServer(new CommandObj(EnumCommand.GRAB_AMMO));
        return false;
    }

    /**
     * This function is used to ask information about shooting action
     * @return true
     */
    public boolean shoot() {
        if (player.getWeapons().isEmpty()) {
            printError("You have not loaded weapon, so you can't shoot");
            return false;
        }

        int index = chooseWeaponToPlace(player.getWeapons().stream()
                .map(WeaponCard::getName)
                .collect(Collectors.toCollection(ArrayList::new)));
        //Not want to place weapon
        if (index == -1) {
            notifyServer(new CommandObj(EnumCommand.SHOOT, index));
            return false;
        }
        //else want shoot
        WeaponCard weaponCard = player.getWeapons().get(index);

        index = chooseTypeAttack(weaponCard);

        notifyServer(new CommandObj(EnumCommand.SHOOT, weaponCard, index));

        return true;
    }

    /**
     * Asks player if he wants to load some weapons, then notifies server of his choice
     * @return false if no weapons need to be loaded
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
     * This function asks player if he wants to load some weapons
     * @return true if he wants, else false
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
     * This function asks player which weapon wants to load and returns index
     * @return index of weapon wanted to load
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
     * This function asks player which opponents wants to hit from a list
     * @param possibleTarget possible target list
     * @return players who can hit
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
     * This function asks player which opponents wants hit from possible target
     * @param numTarget max target which can be hit
     * @param checkedList all possible targets
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

    /**
     * This function asks in which RegenerationCell the player wants to regenerate in
     * @return true
     */
    public boolean regeneratesPlayer(){
        int index = choosePowerUp4Regeneration();
        notifyServer(new CommandObj(EnumCommand.REG_CELL, player.getPowerups().get(index)));
        return true;
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
     * This function asks player which powerUpCard wants to discard to decide in which RegenerationCell he wants to be regenerated
     * @return number of Power up to be discarded
     */
    private int choosePowerUp4Regeneration(){
        int size = player.getPowerups().size()-1;
        String formatString = "[0-"+ size +"]";
        String slt = "";

        List<PowerCard> powerUps = player.getPowerups();
        StringBuilder sb = new StringBuilder();
        sb.append("Set your RegenerationCell. Your PowerUpCards are:\n");
        for (PowerCard pc : powerUps) {
            sb.append(powerUps.indexOf(pc));
            sb.append(") ");
            sb.append(pc);
            sb.append('\n');
        }
        print(sb.toString());

        while (!slt.matches(formatString)) {
            print("In which Regeneration cell do you want to go? (Enter number)[0] ");
            slt = reader.next();
        }
        return Integer.valueOf(slt);
    }

    /**
     * Generates string for choose player action
     * @return String for choose player action
     */
    private String stringForPlayerAction(List<EnumCommand> commands){
        StringBuilder sb = new StringBuilder();
        sb.append("Possible Actions: \n");
        for(EnumCommand action : commands){
            sb.append(commands.indexOf(action));
            sb.append(") ");
            sb.append(action.getName());
            sb.append('\n');
        }
        return sb.toString();
    }

    /**
     * Asks Player wich action he wants to do
     * @return Player command
     */
    private EnumCommand choosePlayerAction(Set<EnumCommand> playerCommands){
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int slt = playerCommands.size();
        String read;
        String formatString = "[0-"+ playerCommands.size()+"]";
        String question = stringForPlayerAction(new ArrayList<>(playerCommands));

        while(true) {
            print(question);
            print("What do you want to do?");
            try {
                read = in.readLine();
                slt = read.matches(formatString) ? Integer.valueOf(read) : playerCommands.size();
            }catch (IOException ioe){
                ioe.fillInStackTrace();
            }

            if(slt< playerCommands.size()){
                return (EnumCommand) (new ArrayList(playerCommands)).get(slt);
            }
        }
    }

    /**
     * Asks player which WeaponCard wants to use (only loaded)
     * @param mex Introducion mex
     * @param query Query mex
     * @return string for choose weapon from hand
     */
    private String stringForChooseWeaponFromHand(String mex, String query, List<String> weapons){
        StringBuilder sb = new StringBuilder();
        sb.append(mex);
        sb.append(stringWeaponFromList(weapons, true));
        sb.append(query);
        return sb.toString();
    }

    /**
     * This function asks player which WeaponCard wants to discard
     * @return index of WeaponCard to Discard, -1 if don't want to discard
     */
    private int chooseWeaponFromHand(String mex){
        String read;
        String formatString = "[0-"+ Constants.MAX_WEAPON_HAND_SIZE.getValue()+"]";

        while (true){
            print(mex);
            read = reader.next();
            if(read.matches(formatString)){
                return Integer.valueOf(read)-1;
            }
        }
    }

    /**
     * String for choose weapon to discard
     * @return String for choose weapon to discard
     */
    private String stringForChooseWeaponToDiscard(List<String> weapons) {
        return stringForChooseWeaponFromHand("You have just three weapon in your hand. You have:\n",
                "\nWhich do you want to discard? [0 = I don't want grab a new Weapon] ", weapons);
    }

    /**
     * Executes chooseWeaponFromHand using message for discard weapon
     */
    public void chooseWeaponToDiscard(List<String> weapons, int grabIndex ){
        int discardIndex = chooseWeaponFromHand(stringForChooseWeaponToDiscard(weapons));
        notifyServer(new CommandObj(DISCARD_WEAPON, discardIndex, grabIndex));
    }

    /**
     * String for choosing  weapon to use
     * @param weapons which can be used
     * @return string
     */
    private String stringForChooseWeaponToPlace(ArrayList<String> weapons){
        return stringForChooseWeaponFromHand("You have this Weapons: ", "What weapon do you want to use?", weapons);
    }

    /**
     * Executes chooseWeaponFromHand using message for place WeaponCard
     * @return index of card to place
     */
    private int chooseWeaponToPlace(ArrayList<String> weapons){
        return  chooseWeaponFromHand(stringForChooseWeaponToPlace(weapons));
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
     * This asks player which WeaponCard wants to draw in a RegenerationCell
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
     * This asks user which optional attack wants use
     * @param canRandom user can use optional attack without order
     * @return list of index of chosen optional attack
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
        sb.append("\tor else you can choose alternative attack:\n");
        sb.append("\t\t2)");
        sb.append(wc.getAlternativeAttack());
        return sb.toString();
    }

    /**
     * Creates string for choosing which attack wants to use
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
     * This asks player which attack wants to use: BaseAttack, OptionalAttack or AlternativeAttack
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
        String mex = stringForChooseCell("Uoooo! Area effect!", "Where do you want shoot? ", positions);
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

    public void chooseCellToKineticrai(List<Position> positions, String opponent){
        Position p = chooseCellToAttack(positions);
        notifyServer(new CommandObj(USE_KINETICRAY, p, opponent));
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
        sbStar.append(starLine);
        sbStar.append('\n');

        return sbStar.toString();
    }

    /**
     * This is to notify player action of other players
     * @param opponent Nickname of player who play
     * @param action action that do
     */
    public void opponentsAction(String opponent, String action){
        StringBuilder sb = new StringBuilder();

        sb.append(opponent);
        sb.append(": ");
        sb.append(action);
        sb.append("\n");

        print(sb.toString());
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

    public void giveScore(String nameDead, Map<String, Integer> points) {
        StringBuilder sb = new StringBuilder();
        String starLine = starLine();

        sb.append("\n\n");
        sb.append(starLine);
        sb.append("\n");
        if(nameDead==null) sb.append(wordInStar("END GAME"));
        else sb.append(wordInStar(nameDead.toUpperCase()+" is dead"));
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

    private String stringForChoosePowerUp(List<PowerCard> powers){
        StringBuilder sb = new StringBuilder();
        sb.append("You can use: ");
        sb.append("\t0)No one");
        for(int i = 0 ; i< powers.size(); i++){
            sb.append('\t');
            sb.append(i+1);
            sb.append(")");
            sb.append(powers.get(i).getPowerUp());
            sb.append(" (");
            sb.append(powers.get(i).getColor());
            sb.append(")");
        }
        sb.append('\n');
        sb.append("Which do you want?");

        return sb.toString();
    }

    /**
     * Ask which power up in powers want to use
     * @param powers possible PowerUp
     */
    public void askForPowerUp(List<PowerCard> powers){
        String format = "[0-"+powers.size()+"]";
        String read = "";
        String question = stringForChoosePowerUp(powers);

        while (!read.matches(format)) {
            print(question);
            read = reader.next();
        }

        notifyServer(new CommandObj(USE_POWER_UP, powers.get(Integer.valueOf(read)-1)));
    }

    public boolean usePowerUp(PowerUp powerUpType, List<Player> opponents) {

        switch (powerUpType) {
            case TELEPORTER:
                move(TELEPORTER);
                break;
            case KINETICRAY:
                Player p = chooseTarget(opponents);
                if(p!=null) {
                    notifyServer(new CommandObj(KINETICRAY_TARGET, p.getName()));
                }
                break;
            case GUNSIGHT:
            case VENOMGRENADE:
                break;
            default:
                break;
        }

        return true;
    }

    public void askPayGunsight(int[] payCubeGunsight, int power) {
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

    protected void chooseGunsightTarget(List<Player> players){
        String target = "";
        while (!target.matches("[0-"+ players.size() +"]") || Integer.valueOf(target) == 0) {
            print("Which player do you want to hit?");
            print("Possible target are:\n");
            print(stringForChooseTarget(players));
            target = reader.next();
        }
        notifyServer(new CommandObj(GUNSIGHT, Integer.valueOf(target)-1));
    }

    public void discardPowerUp(Card power) {
        String target = "";
        while (!target.matches("[0-3]")) {
            print("Which Power Up do you want to discard?");
            print("Possible Power ups are:\n");
            print(stringForDiscardPowerUp(power));
            target = reader.next();
        }
        if(Integer.valueOf(target) == 3)
            notifyServer(new CommandObj(DISCARD_POWER, power));
        else notifyServer(new CommandObj(DISCARD_POWER, player.getPowerups().get(Integer.valueOf(target))));
    }

    private String stringForDiscardPowerUp(Card power) {
        StringBuilder sb = new StringBuilder();

        for(Card card : player.getPowerups()){
            sb.append(player.getPowerups().indexOf(card));
            sb.append(") ");
            sb.append(card.toString());
            sb.append("\t");
        }
        sb.append("3) ");
        sb.append(power);
        sb.append("\n");
        return sb.toString();
    }

    public int askPriorityForAttacks() {
        String optional = "";
        while (!optional.matches("[0-3]")) {
            print("Do you want to use your priority attack first? [1: Yes, 0: No]");
            optional = reader.next();
        }
        return Integer.valueOf(optional);
    }
}