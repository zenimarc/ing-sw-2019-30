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
//TODO eliminare codice ripetuto - Gio'
//one blank cell left
    public static Billboard generateBillboard(){
        HashMap<Cell, Position> mappaProva = new HashMap<>();
        ArrayList<Door> doors = new ArrayList<>();

        Cell c00 = new NormalCell(Color.BLUE);
        Cell c10 = new RegenerationCell(Color.RED);

        Cell c01 = new NormalCell(Color.BLUE);
        Cell c11 = new NormalCell(Color.RED);
        Cell c21 = new NormalCell(Color.WHITE);

        Cell c02 = new RegenerationCell(Color.BLUE);
        Cell c12 = new NormalCell(Color.YELLOW);
        Cell c22 = new NormalCell(Color.YELLOW);

        Cell c03 = new NormalCell(Color.GREEN);
        Cell c13 = new NormalCell(Color.YELLOW);
        Cell c23 = new RegenerationCell(Color.YELLOW);

        mappaProva.put(c00, new Position(0, 0));
        mappaProva.put(c10, new Position(1, 0));

        mappaProva.put(c01, new Position(0, 1));
        mappaProva.put(c11, new Position(1, 1));
        mappaProva.put(c21, new Position(2, 1));


        mappaProva.put(c02, new Position(0, 2));
        mappaProva.put(c12, new Position(1, 2));
        mappaProva.put(c22, new Position(2, 2));

        mappaProva.put(c03, new Position(0, 3));
        mappaProva.put(c13, new Position(1, 3));
        mappaProva.put(c23, new Position(2, 3));


        doors.add(new Door(c00, c10));
        doors.add(new Door(c02, c12));
        doors.add(new Door(c03, c13));
        doors.add(new Door(c11, c21));
        doors.add(new Door(c02, c03));
        doors.add(new Door(c21, c22));

        return new Billboard(mappaProva, doors);

    }

    //2 blank cells
    public static Billboard generateBillboard2() {
        HashMap<Cell, Position> mappaProva = new HashMap<>();
        ArrayList<Door> doors = new ArrayList<>();

        Cell c00 = new NormalCell(Color.BLUE);
        Cell c10 = new RegenerationCell(Color.RED);

        Cell c01 = new NormalCell(Color.BLUE);
        Cell c11 = new NormalCell(Color.RED);
        Cell c21 = new NormalCell(Color.WHITE);

        Cell c02 = new RegenerationCell(Color.BLUE);
        Cell c12 = new NormalCell(Color.PURPLE);
        Cell c22 = new NormalCell(Color.WHITE);

        Cell c13 = new NormalCell(Color.YELLOW);
        Cell c23 = new RegenerationCell(Color.YELLOW);

            mappaProva.put(c00, new Position(0, 0));
            mappaProva.put(c10, new Position(1, 0));

            mappaProva.put(c01, new Position(0, 1));
            mappaProva.put(c11, new Position(1, 1));
            mappaProva.put(c21, new Position(2, 1));

            mappaProva.put(c02, new Position(0, 2));
            mappaProva.put(c12, new Position(1, 2));
            mappaProva.put(c22, new Position(2, 2));

            mappaProva.put(c13, new Position(1, 3));
            mappaProva.put(c23, new Position(2, 3));

            doors.add(new Door(c00, c10));
            doors.add(new Door(c02, c12));
            doors.add(new Door(c11, c21));
            doors.add(new Door(c11, c12));
            doors.add(new Door(c12, c13));
            doors.add(new Door(c22, c23));

        return new Billboard(mappaProva, doors);
    }


    //No blank cells
    public static Billboard generateBillboard3() {
        HashMap<Cell, Position> mappaProva = new HashMap<>();
        ArrayList<Door> doors = new ArrayList<>();

        Cell c00 = new NormalCell(Color.RED);
        Cell c10 = new RegenerationCell(Color.RED);
        Cell c20 = new NormalCell(Color.WHITE);

        Cell c01 = new NormalCell(Color.BLUE);
        Cell c11 = new NormalCell(Color.PURPLE);
        Cell c21 = new NormalCell(Color.WHITE);

        Cell c02 = new RegenerationCell(Color.BLUE);
        Cell c12 = new NormalCell(Color.YELLOW);
        Cell c22 = new NormalCell(Color.YELLOW);

        Cell c03 = new NormalCell(Color.GREEN);
        Cell c13 = new NormalCell(Color.YELLOW);
        Cell c23 = new RegenerationCell(Color.YELLOW);

            mappaProva.put(c00, new Position(0, 0));
            mappaProva.put(c10, new Position(1, 0));
            mappaProva.put(c20, new Position(2, 0));

            mappaProva.put(c01, new Position(0, 1));
            mappaProva.put(c11, new Position(1, 1));
            mappaProva.put(c21, new Position(2, 1));

            mappaProva.put(c02, new Position(0, 2));
            mappaProva.put(c12, new Position(1, 2));
            mappaProva.put(c22, new Position(2, 2));

            mappaProva.put(c03, new Position(0, 3));
            mappaProva.put(c13, new Position(1, 3));
            mappaProva.put(c23, new Position(2, 3));

            doors.add(new Door(c10, c20));
            doors.add(new Door(c01, c11));
            doors.add(new Door(c11, c21));
            doors.add(new Door(c02, c12));
            doors.add(new Door(c03, c13));

            doors.add(new Door(c00, c01));
            doors.add(new Door(c02, c03));
            doors.add(new Door(c21, c22));

        return new Billboard(mappaProva, doors);
    }

    //one right blank cell
    public static Billboard generateBillboard4() {
        HashMap<Cell, Position> mappaProva = new HashMap<>();
        ArrayList<Door> doors = new ArrayList<>();

        Cell c00 = new NormalCell(Color.RED);
        Cell c10 = new RegenerationCell(Color.RED);
        Cell c20 = new NormalCell(Color.WHITE);

        Cell c01 = new NormalCell(Color.BLUE);
        Cell c11 = new NormalCell(Color.PURPLE);
        Cell c21 = new NormalCell(Color.WHITE);

        Cell c02 = new RegenerationCell(Color.BLUE);
        Cell c12 = new NormalCell(Color.PURPLE);
        Cell c22 = new NormalCell(Color.WHITE);

        Cell c13 = new NormalCell(Color.YELLOW);
        Cell c23 = new RegenerationCell(Color.YELLOW);

            mappaProva.put(c00, new Position(0, 0));
            mappaProva.put(c10, new Position(1, 0));
            mappaProva.put(c20, new Position(2, 0));

            mappaProva.put(c01, new Position(0, 1));
            mappaProva.put(c11, new Position(1, 1));
            mappaProva.put(c21, new Position(2, 1));

            mappaProva.put(c02, new Position(0, 2));
            mappaProva.put(c12, new Position(1, 2));
            mappaProva.put(c22, new Position(2, 2));

            mappaProva.put(c13, new Position(1, 3));
            mappaProva.put(c23, new Position(2, 3));

            doors.add(new Door(c00, c01));
            doors.add(new Door(c10, c20));
            doors.add(new Door(c11, c21));
            doors.add(new Door(c02, c12));
            doors.add(new Door(c12, c13));
            doors.add(new Door(c22, c23));


        return new Billboard(mappaProva, doors);
    }

    public static void main(String[] args) {

        Billboard myBillboard = generateBillboard();

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
