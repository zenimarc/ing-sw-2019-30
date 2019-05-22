package controller;

import board.Cell;
import constants.Color;

/**
è un oggetto utile al fine di move e grab e shoot per il controller in particolare
nel metodo update() degli observer va passato un object quindi se abbiamo bisogno di più parametri, serve un oggetto
che li contenga tutti, ecco il motivo di questa classe
TODO: è giusto mettere questa classe in questo package o meglio di no? -Marco
 */
public class CommandObj {
    private PlayerCommand cmd;
    private int weaponSelector;
    private Color cellColor;
    private Cell cell;
    private Object object;

    public CommandObj(PlayerCommand cmd, Cell cell, int sel){
        this.cmd = cmd;
        this.weaponSelector = sel;
        this.cell = cell;
    }


    public CommandObj(PlayerCommand cmd, Color color){
        this.cmd = cmd;
        this.cellColor = color;
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

    public Color getCellColor() {
        return cellColor;
    }

    public Object getObject() {
        return object;
    }
}
