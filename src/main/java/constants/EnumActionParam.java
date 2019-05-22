package constants;

public enum EnumActionParam {

    NORMAL_MOVE(3),
    NORMAL_GRAB_MOVE(1),
    ADRENALINIC_GRAB_MOVE(2),
    NORMAL_SHOOT_MOVE(0),
    ADRENALINIC_SHOOT_MOVE(1),
    ADRENALINIC_FIRST_STEP(3),
    ADRENALINIC_SECOND_STEP(6)
    ;


    private int num;

    EnumActionParam(int num){
        this.num = num;
    }

    public int getNum() {
        return num;
    }
}
