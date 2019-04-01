package costants;

public enum Costants {

    MAX_WEAPON_REGENERATIONCELL(3);

    private int value;

    Costants(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

}
