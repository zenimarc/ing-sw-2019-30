package controller;

/**
 * This enumeration is needed to define the field of action of an attack against enemies:
 * VISIBLE: it's the set of cells you can see (see rules to know what "visible" means)
 * SAME_ROOM: it's the set of cells of your room (nb. your cell is included)
 * VISIBLE_ROOM: it's the set of cells of another room you can see, the cells in your room are not included.
 * SAME_CELL: it's only your cell
 * CARDINAL: it's the set of cells that are on the same axe (x or y) of your cell but
 *           if there's a wall between your cell and a target cell, the target cell won't be in this set
 *           if there's a door between your cell and a target cell, the target cell will be in this set
 *           (nb. your cell is included)
 * CARDINAL_WALL_BYPASS: it's the set of cells that are on the same axe (x or y) of your cell.
 *                      (nb. walls are not considered, your cell is included)
 */
public enum EnumTargetSet {

    VISIBLE,
    NOT_VISIBLE,
    SAME_ROOM,
    VISIBLE_ROOM,
    SAME_CELL,
    CARDINAL,
    CARDINAL_WALL_BYPASS;


}
