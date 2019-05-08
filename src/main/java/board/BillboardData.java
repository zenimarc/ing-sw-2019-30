package board;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
//import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import deck.AmmoCard;
import deck.Color;
//import org.omg.PortableServer.POA;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BillboardData {
    private ArrayList<CellData> cells;
    private ArrayList<PositionPair> doors;

    public BillboardData(){
        this.cells = new ArrayList<>();
        this.doors = new ArrayList<>();
    }
    public void addCell(CellData cell){
        this.cells.add(cell);
    }
    public void addDoor(PositionPair door){
        this.doors.add(door);
    }
    public List<CellData> getCells(){
        return this.cells;
    }

    public static void main(String[] args){
        BillboardData billboardData = new BillboardData();
        billboardData.addCell(new CellData(0, 0, Color.YELLOW, "NormalCell"));
        billboardData.addCell(new CellData(0, 1, Color.YELLOW, "RegenerationCell"));
        billboardData.addCell(new CellData(1, 1, Color.BLUE, "NormalCell"));

        billboardData.addDoor(new PositionPair(new Position(0,0), new Position(0, 1)));
        billboardData.addDoor(new PositionPair(new Position(0,1), new Position(1, 1)));


        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String billboardjson = gson.toJson(billboardData);
        try {
            Files.write(Paths.get("./src/resources/billboard/billboard2.json"), billboardjson.getBytes());
            System.out.println(billboardjson);
        } catch (IOException ioe) {
            ioe.fillInStackTrace();
        }

        try {
            //convert the json string back to object
            BillboardData billboard = gson.fromJson(new BufferedReader(new FileReader("src/resources/billboard/billboard2.json")), BillboardData.class);
        }
        catch (FileNotFoundException fnfe) {
            fnfe.fillInStackTrace();
        }

    }
}


class CellData{
    private Position pos;
    private Color color;
    private String type;

    CellData(int x, int y, Color color, String type){
        this.pos = new Position(x,y);
        this.color = color;
        this.type = type;
    }
    public Position getPos(){
        return this.pos;
    }
    public Color getColor(){
        return this.color;
    }
    public String getType(){
        return this.type;
    }
}

class PositionPair{
    private Position pos1;
    private Position pos2;

    PositionPair(Position pos1, Position pos2){
        this.pos1 = pos1;
        this.pos2 = pos2;
    }
    public Position getCellPos1(){
        return this.pos1;
    }
    public Position getCellPos2(){
        return this.pos2;
    }
}
