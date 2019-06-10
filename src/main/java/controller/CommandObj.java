package controller;

import board.Cell;
import constants.Color;
import weapon.WeaponCard;

/**
è un oggetto utile al fine di move e grab e shoot per il controller in particolare
nel metodo update() degli observer va passato un object quindi se abbiamo bisogno di più parametri, serve un oggetto
che li contenga tutti, ecco il motivo di questa classe
TODO: è giusto mettere questa classe in questo package o meglio di no? -Marco
 */
public class CommandObj {
    private PlayerCommand cmd;
    private int weaponSelector;
    private Cell cell;
    private Object object;

    public CommandObj(PlayerCommand cmd, Cell cell, int sel){
        this.cmd = cmd;
        this.weaponSelector = sel;
        this.cell = cell;
    }

    public CommandObj(PlayerCommand cmd, WeaponCard weaponCard, int index){
        this.cmd = cmd;
        this.object = weaponCard;
        this.weaponSelector = index;
    }

    public CommandObj(PlayerCommand cmd, Object o){
        this.cmd = cmd;
        this.object = o;
    }

    public CommandObj(PlayerCommand cmd){
        this.cmd = cmd;
    }


    public int getWeaponSelector() {
        return weaponSelector;
    }

    public Cell getCell() {
        return cell;
    }

    public PlayerCommand getCmd() {
        return cmd;
    }

    public Object getObject() {
        return object;
    }
}
