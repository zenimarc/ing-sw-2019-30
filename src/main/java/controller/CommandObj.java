package controller;

import board.Cell;
import weapon.WeaponCard;

import java.io.Serializable;
import java.util.List;

/**
è un oggetto utile al fine di move e grab e shoot per il controller in particolare
nel metodo update() degli observer va passato un object quindi se abbiamo bisogno di più parametri, serve un oggetto
che li contenga tutti, ecco il motivo di questa classe
TODO: è giusto mettere questa classe in questo package o meglio di no? -Marco
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

    public CommandObj(EnumCommand cmd, List<Object> objs){
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
}
