package board.billboard;

import board.Cell;
import board.NormalCell;
import board.RegenerationCell;
import board.Door;
import board.Position;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import constants.Color;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;


public class BillboardGenerator extends Billboard {

    public static Billboard createBillboard(int number){
        if(number == 1)
            return generateBillboard();
        if(number == 2)
            return generateBillboard2();
        if(number == 3)
            return generateBillboard3();
        return generateBillboard4();
    }

    /**
     * This function creates the board with a blank cell on the left
     * @return a billboard
     */

    public static Billboard generateBillboard(){
        HashMap<Cell, Position> map = new HashMap<>();
        ArrayList<Door> doors = new ArrayList<>();

        createLeftMap1(map);
        createRightMap1(map);

        createDoor1(map, doors);
        createDoor2(map, doors);

        return new Billboard(map, doors);

    }

    /**
     * This function creates the board with a blank cell on the left and a blank cell on the right
     * @return a billboard
     */
    public static Billboard generateBillboard2() {
        HashMap<Cell, Position> map = new HashMap<>();
        ArrayList<Door> doors = new ArrayList<>();
        createLeftMap1(map);
        createRightMap2(map);

        createDoor1(map, doors);
        createDoor3(map, doors);

        return new Billboard(map, doors);
    }


    /**
     * This function creates the board with no blank cells
     * @return a billboard
     */
    public static Billboard generateBillboard3() {
        HashMap<Cell, Position> map = new HashMap<>();
        ArrayList<Door> doors = new ArrayList<>();

createLeftMap2(map);
createRightMap1(map);

createDoor4(map, doors);
createDoor5(map, doors);


        return new Billboard(map, doors);
    }

    /**
     * This function creates the board with a blank cell on the right
     * @return a billboard
     */
    public static Billboard generateBillboard4() {
        HashMap<Cell, Position> map = new HashMap<>();
        ArrayList<Door> doors = new ArrayList<>();

        createLeftMap2(map);
        createRightMap2(map);

        createDoor4(map, doors);
        createDoor6(map, doors);

        return new Billboard(map, doors);
    }

    /**
     * This function draws left part of map and has a blank cell on the left
     * @param map to assign cells
     */
    private static void createLeftMap1(HashMap<Cell, Position> map){
        Cell c00 = new NormalCell(Color.BLUE);
        Cell c10 = new RegenerationCell(Color.RED);

        Cell c01 = new NormalCell(Color.BLUE);
        Cell c11 = new NormalCell(Color.RED);
        Cell c21 = new NormalCell(Color.WHITE);

        map.put(c00, new Position(0, 0));
        map.put(c10, new Position(1, 0));

        setCells1(map, c01, c11, c21);
    }

    /**
     * This function draws right part of map with no blank cells
     * @param map to assign cells
     */
    private static void createRightMap1(HashMap<Cell, Position> map) {

        Cell c02 = new RegenerationCell(Color.BLUE);
        Cell c12 = new NormalCell(Color.YELLOW);
        Cell c22 = new NormalCell(Color.YELLOW);

        Cell c03 = new NormalCell(Color.GREEN);
        Cell c13 = new NormalCell(Color.YELLOW);
        Cell c23 = new RegenerationCell(Color.YELLOW);

        setCells2(map, c02, c12, c22);

        map.put(c03, new Position(0, 3));
        map.put(c13, new Position(1, 3));
        map.put(c23, new Position(2, 3));

    }

    /**
     * This function draws right part of map with no blank cells
     * @param map to assign cells
     */
        private static void createLeftMap2(HashMap<Cell, Position> map) {
            Cell c00 = new NormalCell(Color.RED);
            Cell c10 = new RegenerationCell(Color.RED);
            Cell c20 = new NormalCell(Color.WHITE);

            Cell c01 = new NormalCell(Color.BLUE);
            Cell c11 = new NormalCell(Color.PURPLE);
            Cell c21 = new NormalCell(Color.WHITE);

            map.put(c00, new Position(0, 0));
            map.put(c10, new Position(1, 0));
            map.put(c20, new Position(2, 0));

            setCells1(map, c01, c11, c21);
        }

    /**
     * This function draws right part of map with no blank cells
     * @param map to assign cells
     */

            private static void createRightMap2(HashMap<Cell, Position> map) {
                Cell c02 = new RegenerationCell(Color.BLUE);
                Cell c12 = new NormalCell(Color.PURPLE);
                Cell c22 = new NormalCell(Color.WHITE);

                Cell c13 = new NormalCell(Color.YELLOW);
                Cell c23 = new RegenerationCell(Color.YELLOW);

                setCells2(map, c02, c12, c22);

                map.put(c13, new Position(1, 3));
                map.put(c23, new Position(2, 3));
            }

    /**
     * The following two functions assign cells to a position and avoid repetition of code
     * @param map where to assign cells
     * @param c1 to assign
     * @param c2 to assign
     * @param c3 to assign
     */
    private static void setCells1(HashMap<Cell, Position> map, Cell c1, Cell c2, Cell c3){

        map.put(c1, new Position(0, 1));
        map.put(c2, new Position(1, 1));
        map.put(c3, new Position(2, 1));

    }

    private static void setCells2(HashMap<Cell, Position> map, Cell c1, Cell c2, Cell c3){

        map.put(c1, new Position(0, 2));
        map.put(c2, new Position(1, 2));
        map.put(c3, new Position(2, 2));

    }

    /**
     * CreateDoors n are used to create specific doors between cells
     * @param map map
     * @param doors stores doors creted
     */

    private static void createDoor1(HashMap<Cell, Position> map, ArrayList<Door> doors){


        doors.add(new Door(getCellsFromPosition(map, new Position(0, 0)), getCellsFromPosition(map, new Position(1, 0))));
        doors.add(new Door(getCellsFromPosition(map, new Position(0, 2)), getCellsFromPosition(map, new Position(1, 2))));
        doors.add(new Door(getCellsFromPosition(map, new Position(1, 1)), getCellsFromPosition(map, new Position(2, 1))));
    }

    private static void createDoor2(HashMap<Cell, Position> map, ArrayList<Door> doors){

        doors.add(new Door(getCellsFromPosition(map, new Position(0, 3)), getCellsFromPosition(map, new Position(1, 3))));
        createDoor7(map, doors);
    }

    private static void createDoor3(HashMap<Cell, Position> map, ArrayList<Door> doors){

        doors.add(new Door(getCellsFromPosition(map, new Position(1, 1)), getCellsFromPosition(map, new Position(1, 2))));
        createDoor6(map, doors);

    }

    private static void createDoor4(HashMap<Cell, Position> map, ArrayList<Door> doors){

        doors.add(new Door(getCellsFromPosition(map, new Position(0, 0)), getCellsFromPosition(map, new Position(0, 1))));
        doors.add(new Door(getCellsFromPosition(map, new Position(1, 0)), getCellsFromPosition(map, new Position(2, 0))));
        doors.add(new Door(getCellsFromPosition(map, new Position(1, 1)), getCellsFromPosition(map, new Position(2, 1))));
        doors.add(new Door(getCellsFromPosition(map, new Position(0, 2)), getCellsFromPosition(map, new Position(1, 2))));

    }

    private static void createDoor5(HashMap<Cell, Position> map, ArrayList<Door> doors){
        doors.add(new Door(getCellsFromPosition(map, new Position(0, 1)), getCellsFromPosition(map, new Position(1, 1))));
        doors.add(new Door(getCellsFromPosition(map, new Position(0, 2)), getCellsFromPosition(map, new Position(0, 3))));
        createDoor7(map, doors);
    }

    private static void createDoor6(HashMap<Cell, Position> map, ArrayList<Door> doors){
        doors.add(new Door(getCellsFromPosition(map, new Position(1, 2)), getCellsFromPosition(map, new Position(1, 3))));
        doors.add(new Door(getCellsFromPosition(map, new Position(2, 2)), getCellsFromPosition(map, new Position(2, 3))));

    }

    private static void createDoor7(HashMap<Cell, Position> map, ArrayList<Door> doors){
        doors.add(new Door(getCellsFromPosition(map, new Position(0, 3)), getCellsFromPosition(map, new Position(1, 3))));
        doors.add(new Door(getCellsFromPosition(map, new Position(2, 1)), getCellsFromPosition(map, new Position(2, 2))));

    }

    public static void main(String[] args) {

        Billboard myBillboard = new BillboardGenerator().generateBillboard();

        //Gson gson = new Gson();
        //String billboardjson = gson.toJson(myBillboard);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String billboardjson = gson.toJson(myBillboard);

        try {
            Files.write(Paths.get("./src/resources/billboard/billboard.json"), billboardjson.getBytes());
            System.out.println(billboardjson);
        } catch (IOException ioe) {
            ioe.fillInStackTrace();
        }

    try {
        //convert the json string back to object
        Billboard billboard = gson.fromJson(new BufferedReader(new FileReader("src/resources/billboard/billboard.json")), Billboard.class);
    }
    catch (FileNotFoundException fnfe){
        fnfe.fillInStackTrace();

    }

}
}
