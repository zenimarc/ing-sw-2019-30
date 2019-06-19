package controller;

import java.util.EnumSet;
import java.util.Set;

/**
 * 
 */
public enum PlayerCommand {
    MOVE("Move", "Move your pawn"),
    GRAB("Grab", "..."),
    SHOOT("Shoot", "..."),
    POWERUP("PowerUp", "..."),
    END_TURN("End Turn", "End your turn"),

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

    //Controller to View
    PRINT_ERROR("Print error",""),
    CHOOSE_OPTIONAL_ATTACK("Choose optional attack",""),

    //Commands for GUI
    CHOOSE_ACTION("Action", "..."),
    UNSELECT("prova", "...");

    ;

    public static final Set<PlayerCommand> PlayerAction = EnumSet.of(MOVE, GRAB, SHOOT, POWERUP, END_TURN);

    private String name;
    private String description;

    PlayerCommand(String name, String desc){
        this.name = name;
        this.description = desc;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static PlayerCommand getPlayerActionFromIndex(int i){
        for(PlayerCommand command : PlayerCommand.PlayerAction){
            if(i==command.ordinal()) return command;
        }
        return null;
    }
}