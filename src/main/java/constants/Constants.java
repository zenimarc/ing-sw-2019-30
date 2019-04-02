package constants;

public enum Constants {

    MAX_WEAPON_REGENERATIONCELL(3),
    MAX_WEAPON_HAND_SIZE(3), //max number of weapon cards in hand
    MAX_POWER_HAND_SIZE(3); //max number of power up cards in hand

    private int value;

    Constants(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

}
