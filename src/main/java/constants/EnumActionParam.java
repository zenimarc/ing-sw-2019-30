package constants;

/**
 * EnumActionParam gives info about a specific action
 */
public enum EnumActionParam {

    NORMAL_MOVE(3),
    NORMAL_GRAB_MOVE(1),
    ADRENALINIC_GRAB_MOVE(2),
    NORMAL_SHOOT_MOVE(0),
    ADRENALINIC_SHOOT_MOVE(1),
    ADRENALINIC_FIRST_STEP(3),
    ADRENALINIC_SECOND_STEP(6),
    FRENZY_MOVE(4),
    FRENZY_GRAB_MOVE_AFTER_FIRST(3),
    FRENZY_GRAB_MOVE_BEFORE_FIRST(2),
    FRENZY_SHOOT_MOVE_AFTER_FIRST(2),
    FRENZY_SHOOT_MOVE_BEFORE_FIRST(1)
    ;

    private int num;

    EnumActionParam(int num){
        this.num = num;
    }

    public int getNum() {
        return num;
    }
}
