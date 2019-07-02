package controller;

import java.util.EnumSet;
import java.util.Set;

/**
 * 
 */
public enum EnumCommand {
    MOVE("Move", "Move your pawn"),
    GRAB("Grab", "..."),
    SHOOT("Shoot", "..."),
    POWERUP("PowerUp", "..."),
    END_TURN("End Turn", "End your turn"),
    //Final frenzy
    MOVE_FRENZY("Move Frenzy", ""),
    GRAB_MOVE_FRENZYX1("Grab move single action", ""),
    GRAB_MOVE_FRENZYX2("Grab move double action", ""),
    SHOOT_MOVE_FRENZYX1("Shoot move single action", ""),
    SHOOT_MOVE_FRENZYX2("Shoot move double action", ""),
    //
    REG_CELL("Regeneration Cell", "Choose Regeneration cell"),
    GET_DESTINATION_CELL("Get destination cell", "Get potential destination cell"),
    GRAB_WEAPON("Grab", "..."),
    GRAB_AMMO("Grab", "..."),
    DISCARD_WEAPON("Discard weapon","..."),
    GRAB_MOVE("Grab move", "Move than Grab"),
    SHOOT_MOVE("Shot move", "Move than Shoot"),
    PLACE_WEAPONCARD("Place WeaponCard","..."),
    CHOOSE_ATTACK("Choose attack","..."),
    LOAD_WEAPONCARD("Load weapon","..."),

    //Power ups
    ASKFORPOWERUP("", ""),
    CHECKPOWERUP("check usable power ups", "..."),
    PAYPOWERUP("check payable power up", "..."),
    PAYGUNSIGHT("check if can pay Gunsight", "..."),
    GUNSIGHTPAID("", ""),
    PAIDPOWERUP("", ""),
    DISCARD_POWER ("Discard Power up", ""),
    USE_GUNSIGHT("Use Gunsight power Up", "..."),
    USE_TELEPORTER("Use Teleporter power Up", "..."),
    TELEPORTER("", ""),
    USE_VENOMGRENADE("Use Venom Grenade power Up", "..."),
    VENOMGRENADE("Use Venom Grenade power Up", "..."),
    USE_KINETICRAY("Use Kinetic Ray power Up", "..."),
    KINETICRAY("Use Kinetic Ray power Up", "..."),

    //Controller to View
    PRINT_ERROR("Print error",""),
    CHOOSE_OPPONENTS("Choose opponents","Choose opponents to shoot."),
    CHOOSE_OPTIONAL_ATTACK("Choose optional attack",""),
    YOUR_TURN("Play!","It's your turn"),
    YOUR_FRENY_TURN("Play!","It's your frenzy turn"),
    NOT_YOUR_TURN("Not your turn","Wait a moment..."),
    SHOW_BOARD("Show board",""),
    UPDATE_PLAYER("Update Player","Modify local Player"),
    UPDATE_BOARD("Update Board","Modify local Board"),
    PRINT_POINTS("Print points","Print points of a round"),

    //Commands for GUI
    CHOOSE_ACTION("Action", "..."),
    UNSELECT("prova", "..."),
    ASKTARGETS("", ""),
    CHOOSE_CELL("", "")
    ;

    public static final Set<EnumCommand> PlayerAction = EnumSet.of(MOVE, GRAB, SHOOT, POWERUP, END_TURN);
    public static final Set<EnumCommand> PlayerActionFF_1 = EnumSet.of(SHOOT_MOVE_FRENZYX1, MOVE_FRENZY, GRAB_MOVE_FRENZYX1);
    public static final Set<EnumCommand> PlayerActionFF_2 = EnumSet.of(SHOOT_MOVE_FRENZYX2, GRAB_MOVE_FRENZYX2);



    private String name;
    private String description;

    /**
     * Constructor
     */
    EnumCommand(String name, String desc){
        this.name = name;
        this.description = desc;
    }

    /**
     * This function return the name of a Command
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * This function return the description of a Command
     * @return description
     */
    public String getDescription() {
        return description;
    }

    public static EnumCommand getPlayerActionFromIndex(int i){
        for(EnumCommand command : EnumCommand.PlayerAction){
            if(i==command.ordinal())
                return command;
        }
        return null;
    }
}