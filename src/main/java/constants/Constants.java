package constants;

public enum Constants {

    MAX_WEAPON_REGENERATIONCELL(3),
    MAX_WEAPON_HAND_SIZE(3), //max number of weapon cards in hand
    MAX_POWER_HAND_SIZE(3), //max number of power up cards in hand
    MAX_DAMAGE(12), //max number of damage
    MAX_BULLET_PER_COLOR(3),
    ACTION_PER_TURN_NORMAL_MODE(2);

    private int value;

    Constants(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

}
