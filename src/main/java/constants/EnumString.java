package constants;

public enum EnumString {

    BASE_ATTACK_NAME("Base Attack"),
    LOCKRIFLE_OPT1(""),
    MACHINEGUN_OP1(""),
    MACHINEGUN_OP2(""),
    ELECTROSCYHE_OPT1(""),
    TRACTOR_BEAN_OPT1(""),
    VORTEX_CANNON_OPT1(""),
    ZX_2_OP1("")
    ;

    private String string;

    EnumString(String value){
        this.string = value;
    }

    public String getString(){
        return string;
    }
}
