package costants;

public enum Costants {

    MAX_WEAPON_REGENERATIONCELL(3),
    DIMENSION_X(4),
    DIMENSION_Y(3);

    private int value;

    Costants(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

}
