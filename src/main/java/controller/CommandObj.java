package controller;

import board.Cell;
import weapon.WeaponCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * CommandObj is a class used to send info between UpdateManagers
 */
public class CommandObj implements Serializable {
    private EnumCommand cmd;
    private int weaponSelector;
    private Cell cell;
    private Object object;
    private Object object2;
    private List<Object> objects;

    public CommandObj(EnumCommand cmd, Cell cell, int sel){
        this.cmd = cmd;
        this.weaponSelector = sel;
        this.cell = cell;
    }

    public CommandObj(EnumCommand cmd, WeaponCard weaponCard, int index){
        this.cmd = cmd;
        this.object = weaponCard;
        this.weaponSelector = index;
    }

    public CommandObj(EnumCommand cmd, Object o){
        this.cmd = cmd;
        this.object = o;
    }

    public CommandObj(EnumCommand cmd, Object o, Object obj2){
        this.cmd = cmd;
        this.object = o;
        this.object2 = obj2;
    }

    public CommandObj(EnumCommand cmd, ArrayList<Object> objs){
        this.cmd = cmd;
        this.objects = objs;
    }

    public CommandObj(EnumCommand cmd){
        this.cmd = cmd;
    }


    public int getWeaponSelector() {
        return weaponSelector;
    }

    public Cell getCell() {
        return cell;
    }

    public EnumCommand getCmd() {
        return cmd;
    }

    public Object getObject() {
        return object;
    }

    public Object getObject2() {
        return object2;
    }

    public List<Object> getList() {
        return objects;
    }
}
