package constants;

public enum EnumActionParam {

    NORMAL_MOVE(3),
    NORMAL_GRAB_MOVE(1),
    ADRENALINIC_GRAB_MOVE(2),
    NORMAL_SHOOT_MOVE(0),
    ADRENALINIC_SHOOT_MOVE(1),
    ADRENALINIC_FIRST_STEP(3),
    ADRENALINIC_SECOND_STEP(6),
    FRENZY_MOVE(4),
    FRENZY_GRAB_MOVEX1(3),
    FRENZY_GRAB_MOVEX2(2),
    FRENZY_SHOOT_MOVEX1(2),
    FRENZY_SHOOT_MOVEX2(1)
    ;

    private int num;

    EnumActionParam(int num){
        this.num = num;
    }

    public int getNum() {
        return num;
    }
}
