package controller;
import board.*;
import board.Cell;
import board.NormalCell;
import board.RegenerationCell;
import board.billboard.BillboardGenerator;
import constants.Color;
import deck.Deck;
import player.Player;
import powerup.PowerCard;
import server.GameServer;
import server.GameServerImpl;

import java.util.*;
import java.util.stream.Collectors;

import static constants.Constants.*;

/**
 * BoardController controls the table game.
 */
public class BoardController{

    private int playerTurn = 0;
    private int firstPlayerInFF = 2; //TODO metterlo a 0
    private boolean hasFirstPlayInFrenzy = false;
    private List<Player> listOfPlayers;
    private List<PlayerController> playerControllers;
    private Board board;
    private Player playerWhoPlay;
    private GameServerImpl gameServer;
    private TurnHandler turnHandler;

    /**
     * Default constructor
     */

    public BoardController(List<Player> players, Board board) {
        this(players, 8);
        this.board = board;
    }

    public BoardController(List<Player> players, Board board, List<PlayerController> controllers) {
        this(players, 8);
        this.board = board;
        this.playerControllers = controllers;
    }

    /**
     * This genetare a boardcontroller with a default board
     * @param players
     * @param numskulls
     */
    public BoardController(List<Player> players, int numskulls){
        this.listOfPlayers = players;
        this.playerControllers = new ArrayList<>();

        Random random = new Random();
        this.board = new Board(numskulls, BillboardGenerator.createBillboard(random.nextInt(3)+1));

        for (Player player : this.listOfPlayers){
            //Create player controller
            playerControllers.add(new PlayerController(player, this));
            //Draw 2 PowerCard
            player.addPowerCard((PowerCard) board.getPowerUpDeck().draw());
            player.addPowerCard((PowerCard) board.getPowerUpDeck().draw());
        }
    }

    public void setGameServer(GameServerImpl gameServer) {
        this.gameServer = gameServer;
    }

    public GameServer getGameServer() {
        return gameServer;
    }


    protected Player getPlayerWhoPlay() {
        return playerWhoPlay;
    }
    /**
     * This function returns the player who has to do a move
     * @return the player
     */
    public Player getPlayer(){return this.listOfPlayers.get(playerTurn);}

    /**
     * this function returns a Player from a nickname
     * @param nickname of the player you want to get
     * @return the player associated to the nickname
     */
    public Player getPlayer(String nickname){
        return listOfPlayers.stream().filter(x -> x.getName().equals(nickname)).findFirst().orElse(null);
    }

    /**
     * This function returns the board associated with this controller
     * @return the board associated with this controller
     */

    public Board getBoard(){return this.board;}

    /**
     * This function returns the number of the actual turn
     * @return the number of the turn
     */

    public int getNumTurns() { return this.playerTurn; }

    /**
     * This function changes the number to decide which turn is
     */
    public int changeTurn() {
        playerTurn++;
        if(playerTurn >= listOfPlayers.size())
            playerTurn = 0;
        while(!listOfPlayers.get(playerTurn).isActive()) {
            playerTurn++;
            if(playerTurn >= listOfPlayers.size())
                playerTurn = 0;
        }

        if(turnHandler != null) {
            turnHandler.interrupt();
            turnHandler = new TurnHandler(this);
            turnHandler.start();
            playerPlay(listOfPlayers.get(playerTurn));
        }

        return playerTurn;
    }

    /**
     * This function verifies if the final phase of the game can begin
     * @return true if the number of skulls is zero
     */
    public boolean isFinalFrenzy() {
        return (this.board.getSkulls() == 0);
    }

    /**
     * This function changes the value of firstPlayerInFF to decide the number of actions of a player during Final Frenzy
     */
    public void setFinalFrenzyTurns(){
        if(this.isFinalFrenzy()){
            this.firstPlayerInFF = this.playerTurn;
        }
    }

    public boolean isFrenzyTurnBeforeFirst(){
        return hasFirstPlayInFrenzy;
    }

    /**
     * This function verifies if the player can really do a move
     * @param player wanted to be verified
     * @return true if it's his turn, else false
     */
    public boolean verifyTurn(Player player) {
        return(player == listOfPlayers.get(playerTurn));
    }

    /** verify if the deck is empty, potential need to recreate with discarded cards
     * @param deck to verify the emptiness
     */
    public boolean verifyDeck(Deck deck) {
        return(deck.getSize() == 0);
    }

    /**
     * This function sets the board (game) of the BoardController
     * @param board to assign
     */
    public void setBoard(Board board) {this.board = board; }

    /**
     * This function return the PlayerController associated to a specific Player
     * @param player you want to know its PlayerController
     * @return player's PlayerController if present, else null.
     */
    public PlayerController getPlayerController(Player player){
        return this.playerControllers.stream().filter(x -> x.getPlayer().getName().equals(player.getName())).findFirst().orElse(null);
    }

    /**
     * this function return the PlayerController associated to a specific nickname of a Player
     * @param nickname of the player you want to know its PlayerController
     * @return player's PlayerController if present, else null.
     */
    public PlayerController getPlayerController(String nickname){
        return this.playerControllers.stream().filter(x -> x.getPlayer().getName().equals(nickname)).findFirst().orElse(null);
    }

    /**
     * This function returns the list of players associated to this board (this game)
     * @return the list of player associated to this board
     */
    public List<Player> getListOfPlayers(){
        return listOfPlayers;
    }

    /**
     * This function, given the shooter cell, returns a list of players who are possible targets compatible with the attack's target type.
     * @param shooterCell is the shooter's Cell
     * @param targetType is the attack's target type, examples: Visible, Same room... (see EnumTargetSet for complete enumeration)
     * @return a list of players the shooter che hit with the selected targetType
     */
    public List<Player> getPotentialTargets(Cell shooterCell, EnumTargetSet targetType) {

        //If there are not opponents return empty list
        if (this.listOfPlayers.size() == 1) return Collections.emptyList();

        List<Player> opponents = new ArrayList<>(this.listOfPlayers);
        opponents.remove(playerWhoPlay);

        switch (targetType) {
            case VISIBLE:
                return opponents.stream().filter(x -> board.getBillboard().visibleCells(shooterCell).contains(x.getCell())).collect(Collectors.toList());
            case NOT_VISIBLE:
                return opponents.stream().filter(x -> !(board.getBillboard().visibleCells(shooterCell).contains(x.getCell()))).collect(Collectors.toList());
            case VISIBLE_ROOM:
                return opponents.stream().filter(x -> board.getBillboard().canSeeThroughDoor(shooterCell, x.getCell())).collect(Collectors.toList());
            case VISIBLE_NOT_SAME:
                return opponents.stream().filter(x -> board.getBillboard().visibleCells(shooterCell).contains(x.getCell()) && x.getCell() != shooterCell).collect(Collectors.toList());
            case SAME_ROOM:
                return opponents.stream().filter(x -> board.getBillboard().hasSameColor(shooterCell, x.getCell())).collect(Collectors.toList());
            case SAME_CELL:
                return opponents.stream().filter(x -> x.getCell().equals(shooterCell)).collect(Collectors.toList());
            case CARDINAL_WALL_BYPASS:
                return opponents.stream().filter(x -> (board.getBillboard().getCellPosition(x.getCell()).getX()) == board.getBillboard().getCellPosition(shooterCell).getX() ||
                        board.getBillboard().getCellPosition(x.getCell()).getY() == board.getBillboard().getCellPosition(shooterCell).getY()).collect(Collectors.toList());
            case CARDINAL:
                /*
                it's the same of Cardinal_Wall_Bypass but it has to check if there are walls that obstacle the line.
                so do the same checks as upper case, and if there aren't walls between shooter_cell and target_cell,
                need to be true this sentence: the shooter could potentially move to the target_cell
                in a number of steps that are the distance between the two cells.
                please note that if the two cells are on the same axe (has to be true due the first check) the distance between
                the two cell is the minimum possible steps to reach the target cell.
                 */
                return opponents.stream().filter(x -> ((board.getBillboard().getCellPosition(x.getCell()).getX()) == board.getBillboard().getCellPosition(shooterCell).getX() ||
                        board.getBillboard().getCellPosition(x.getCell()).getY() == board.getBillboard().getCellPosition(shooterCell).getY())
                        && board.getBillboard().canMove(shooterCell, x.getCell(), board.getBillboard().cellDistance(shooterCell, x.getCell()))).collect(Collectors.toList());
            default:
                return Collections.emptyList();
        }
    }

    /**
     * This function, given the shooter cell, returns a list of players who are possible targets compatible with the attack's target type
     * who are in cell in range [minDistance;maxDistance]
     * @param shooterCell is the shooter's Cell
     * @param targetType is the attack's target type, examples: Visible, Same room... (see EnumTargetSet for complete enumeration)
     * @param minDistance min distance of opponents
     * @param maxDistance  max distance of opponents
     * @return a list of players the shooter che hit with the selected targetType
     */
    public List<Player> getPotentialTargets(Cell shooterCell, EnumTargetSet targetType, int minDistance, int maxDistance) {

        List<Player> opponents = getPotentialTargets(shooterCell, targetType);
        if(opponents.isEmpty()) return Collections.emptyList();
        if(minDistance==-1 && maxDistance!=-1) {
            return opponents.stream()
                    .filter(x -> board.getBillboard().cellDistance(shooterCell, x.getCell()) <= maxDistance)
                    .collect(Collectors.toList());
        }else if(minDistance!=-1 && maxDistance==-1){
            return opponents.stream()
                    .filter(x -> board.getBillboard().cellDistance(shooterCell, x.getCell()) >= minDistance)
                    .collect(Collectors.toList());
        }else {
            return opponents.stream()
                    .filter(x -> board.getBillboard().cellDistance(shooterCell, x.getCell()) >= minDistance &&
                            board.getBillboard().cellDistance(shooterCell, x.getCell()) <= maxDistance)
                    .collect(Collectors.toList());
        }
    }

        /**
         * This function returns a subset of Cell from billboard where the player can move in tot steps
         * @param shooterCell is the player's cell
         * @param steps are the maximum steps available
         * @return a list of Cell reachable from shooterCell in tot steps
         */
    public List<Cell> getPotentialDestinationCells(Cell shooterCell, int steps){
        return board.getBillboard().getCellMap().keySet().stream().filter(x -> board.getBillboard().canMove(shooterCell, x, steps)).collect(Collectors.toList());
    }

    public String getPotentialDestinationCellsString(Cell shooterCell, int steps){
        StringBuilder sb = new StringBuilder();

        sb.append("Potential destination cell: ");
        for(Cell c: getPotentialDestinationCells(shooterCell,steps)){
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * This function returns a subset of Positions of Cells from billboard where the player can put another player using
     * Kinetic ray power up
     * @param playerCell of other player
     * @return a list
     */
    protected List<Position> getCellsKineticRay(Cell playerCell){
        List<Cell> cells = board.getBillboard().getCellMap().keySet().stream().filter(x -> board.getBillboard().cellDistance(playerCell, x) <= 2 &&
                        ((board.getBillboard().getCellPosition(x).getX()) == board.getBillboard().getCellPosition(playerCell).getX() ||
                                board.getBillboard().getCellPosition(x).getY() == board.getBillboard().getCellPosition(playerCell).getY())
        ).collect(Collectors.toList());
        ArrayList<Position> positions = new ArrayList<>();
        for(Cell cell: cells)
            positions.add(board.getBillboard().getCellPosition(cell));
        return positions;
        }

    /**
     * Set RegenerationCell of a player which pawn was removed from billboard
     * @param player Player out of game
     * @param color Color of Regeneration Cell
     * @return true if it is valid, else false
     */
    public boolean setRegenerationCell(Player player, Color color){
        Cell regenerationCell = board.getBillboard().getRegenerationCell()
                .stream()
                .filter(x -> x.getColor() == color)
                .findFirst()
                .orElse(null);

        if(regenerationCell!=null){
            board.setPlayerCell(player, regenerationCell);
            return true;
        }
        return false;
    }

    public void firstPlay(){
        this.turnHandler = new TurnHandler(this);
        turnHandler.start();
        playerPlay(listOfPlayers.get(0));
    }

    public synchronized void updatePlayerMenuStatus(Player player){
        PlayerController pc = playerControllers.stream().filter(x-> x.getPlayer()==player).findFirst().orElse(null);
        if (pc!=null){
            if (player.equals(listOfPlayers.get(playerTurn)))
                pc.myTurn(ACTION_PER_TURN_NORMAL_MODE);
            else
                pc.notMyTurn(listOfPlayers.get(playerTurn).getName());
        }
    }

    /**
     * Say to playerController of player to start his turn
     * @param player Player how can play
     */
    public synchronized void playerPlay(Player player){
        PlayerController pc = playerControllers.stream().filter(x-> x.getPlayer()==player).findFirst().orElse(null);
        if(pc!=null) {
            playerWhoPlay = pc.getPlayer();
            playerControllers.stream().filter(x-> x.getPlayer()!=player).forEach(x -> x.notMyTurn(player.getName()));

            if(!isFinalFrenzy()){
                pc.myTurn(ACTION_PER_TURN_NORMAL_MODE);
            }else {

                if(playerTurn==0) hasFirstPlayInFrenzy = true;

                if(!hasFirstPlayInFrenzy){
                    pc.myTurn(ACTION_PER_TURN_FF_BEFORE_FIRST);
                }else {
                    pc.myTurn(ACTION_PER_TURN_FF_AFTER_FIRST);
                    if(playerTurn==firstPlayerInFF){
                        totalPoints();
                        //TODO devo chiamare qualcuno per dire che il gioco è finito!!!
                        return;
                    }
                }
            }
            restoreCell(pc.getModifyCell());
            //If someone died
            if(listOfPlayers.stream().anyMatch(Player::isDead)){
                deadManager();
            }
        }
        changeTurn();
    }

    private void deadManager(){
        List<Player> deadPlayers = listOfPlayers.stream().filter(Player::isDead).collect(Collectors.toList());
        for(Player dead : deadPlayers) {
            dead.setPawnCell(null);
            HashMap<String, Integer> points = givePoints(dead);
            notifyScore(dead.getName(), points);
            if (board.getSkulls() > 0) {
                board.decrementSkull();
                dead.getPlayerBoard().addSkull();
                if(board.getSkulls()==0) setFinalFrenzyTurns();
            }
            regeneration(dead);

        }
    }

    public List<Player> notNullCellPlayers(){
        return listOfPlayers.stream().filter(x-> board.getBillboard().getCellMap().containsKey(x.getCell()) && x != playerWhoPlay).collect(Collectors.toList());
    }

    /**
     * Restores NormalCells and/or RegenerationCells that player modified in his turn
     * @param cells modify cells
     */
    private void restoreCell(List<Cell> cells){
        for(Cell cell : cells){
            if(cell.getClass()== NormalCell.class){
                NormalCell nc = (NormalCell) cell;
                nc.setCard(board.getAmmoCardDeck().draw());
            }
            else if(cell.getClass() == RegenerationCell.class) {
                //If no weaponCard in deck return
                if (board.getWeaponCardDeck().getSize() == 0) return;
                //else
                RegenerationCell rc = (RegenerationCell) cell;
                rc.setCard(board.getWeaponCardDeck().draw());
            }
        }
    }

    private void regeneration(Player dead){
        dead.resetDamage();
        dead.getPlayerBoard().resurrect();

        PlayerController deadPC = playerControllers.stream().filter(x -> x.getPlayer().equals(dead)).findFirst().orElse(null);
        if(deadPC!= null){
            dead.addPowerCard((PowerCard) board.getPowerUpDeck().draw());
            deadPC.regCell();
        }
    }

    /**
     * This give points to players who hit deadPlayer
     * @param deadPlayer Player who died
     * @return Map to Player name -> Points 4 this dead
     */
    private HashMap<String, Integer> givePoints(Player deadPlayer){
            Map<Player, Integer> points = deadPlayer.getPlayerBoard().getPoints(isFinalFrenzy());
            HashMap<String, Integer> points4View = new HashMap<>();
            points.keySet().forEach(x-> {
                x.addPoints(points.get(x));
                points4View.put(x.getName(), points.get(x));
            });
            return points4View;
    }

    /**
     * Notify view to print new points
     * @param deadPlayer Player dead name
     * @param points points for each players
     */
    private void notifyScore(String deadPlayer, Map<String, Integer> points ){
        for(PlayerController pc : playerControllers){
            pc.cmdForView(new CommandObj(EnumCommand.PRINT_POINTS, deadPlayer, points ));
        }
    }

    private void notifyScore(Map<String, Integer> points ){
        for(PlayerController pc : playerControllers){
            pc.cmdForView(new CommandObj(EnumCommand.PRINT_POINTS, null ,points));
        }
    }

    /**
     * Send all Players actual score of all players
     */
    public void totalPoints(){
        HashMap<String, Integer> finalPoints = new HashMap<>();
        playerControllers
                .stream()
                .map(PlayerController::getPlayer)
                .forEach(x -> {
                    finalPoints.put(x.getName(), x.getPoints());
                });
        notifyScore(finalPoints);
    }

    public void kick(Player player){
        this.gameServer.removeClient(player);
    }

    public void kickPlayerWhoPlay(){
        System.out.println("disconnetto per inattività: "+listOfPlayers.get(playerTurn).getName());
        this.kick(listOfPlayers.get(playerTurn));
        getPlayerController(listOfPlayers.get(playerTurn)).receiveCmd(new CommandObj(EnumCommand.END_TURN));
    }

    public void suspend(Player playerToSuspend){
        Optional<Player> p = listOfPlayers.stream().filter(x -> x.equals(playerToSuspend)).findFirst();
        if (p.isPresent())
            p.get().suspend();
    }

    public void reactivate(Player playerToReactivate){
        playerToReactivate.reactivate();
    }

    public int getNumActivePlayers(){
        return this.listOfPlayers.stream().filter(x->x.isActive()).collect(Collectors.toList()).size();
    }
}