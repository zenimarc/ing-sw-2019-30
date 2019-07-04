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
    GRAB_MOVE_FRENZY_AFTER_FIRST("Grab move single action", ""),
    GRAB_MOVE_FRENZY_BEFORE_FIRST("Grab move double action", ""),
    SHOOT_MOVE_FRENZY_AFTER_FIRST("Shoot move single action", ""),
    SHOOT_MOVE_FRENZY_BEFORE_FIRST("Shoot move double action", ""),
    //
    REG_CELL("Regeneration Cell", "Choose Regeneration cell"),
    GET_DESTINATION_CELL("Get destination cell", "Get potential destination cell"),
    GRAB_WEAPON("Grab", "..."),
    GRAB_AMMO("Grab", "..."),
    DISCARD_WEAPON("Discard weapon","..."),
    GRAB_MOVE("Grab move", "Move than Grab"),
    SHOOT_MOVE("Shot move", "Move than Shoot"),
    SHOOT_CHECK("",""),
    PLACE_WEAPONCARD("Place WeaponCard","..."),
    CHOOSE_ATTACK("Choose attack","..."),
    LOAD_WEAPONCARD("Load weapon","..."),

    //Power ups
    ASK_FOR_POWER_UP("", ""),
    CHECKPOWERUP("check usable power ups", "..."),
    PAYPOWERUP("check payable power up", "..."),
    PAYGUNSIGHT("check if can pay Gunsight", "..."),
    GUNSIGHTPAID("", ""),
    PAIDPOWERUP("", ""),
    DISCARD_POWER ("Discard Power up", ""),
    USE_GUNSIGHT("Use Gunsight power Up", "..."),
    GUNSIGHT("Use Gunsight power Up", "..."),
    USE_TELEPORTER("Use Teleporter power Up", "..."),
    TELEPORTER("", ""),
    USE_VENOMGRENADE("Use Venom Grenade power Up", "..."),
    USE_KINETICRAY("Use Kinetic Ray power Up", "..."),
    KINETICRAY("Use Kinetic Ray power Up", "..."),

    CHECK_EVERY_TIME_POWER_UP("",""),
    USE_POWER_UP("Use power up","Use power up"),

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

    public static final Set<EnumCommand> PlayerAction = EnumSet.of(END_TURN, MOVE, GRAB, SHOOT, POWERUP);
    public static final Set<EnumCommand> PlayerActionFF_BEFORE = EnumSet.of(END_TURN, SHOOT_MOVE_FRENZY_BEFORE_FIRST, MOVE_FRENZY, GRAB_MOVE_FRENZY_BEFORE_FIRST);
    public static final Set<EnumCommand> PlayerActionFF_AFTER = EnumSet.of(END_TURN, SHOOT_MOVE_FRENZY_AFTER_FIRST, GRAB_MOVE_FRENZY_AFTER_FIRST);



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