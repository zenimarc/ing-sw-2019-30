package constants;

public enum EnumString {


    BASE_ATTACK_NAME("Base Attack"),
    SUPPORT_ATTACK("LOREM IPSUM"),
    LOCKRIFLE_OPT1("LOREM IPSUM"),
    MACHINEGUN_OP1("LOREM IPSUM"),
    MACHINEGUN_OP2("LOREM IPSUM"),
    ELECTROSCYHE_OPT1("LOREM IPSUM"),
    TRACTOR_BEAN_OPT1("LOREM IPSUM"),
    VORTEX_CANNON_OPT1("LOREM IPSUM"),
    ZX_2_OP1("LOREM IPSUM"),
    HELLION_OPT1("LOREM IPSUM")
    ;

    private String string;

    EnumString(String value){
        this.string = value;
    }

    public String getString(){
        return string;
    }
}
