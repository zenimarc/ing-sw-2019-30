package constants;

public enum EnumString {


    BASE_ATTACK_NAME("Base Attack"),
    SUPPORT_ATTACK("LOREM IPSUM"),
    TEST_ATTACK("Test attack"),
    LOCKRIFLE_OPT1("With second lock"),
    MACHINEGUN_OP1("With focus shot"),
    MACHINEGUN_OP2("With turret tripod"),
    ELECTROSCYHE_OPT1("Reaper mode"),
    TRACTOR_BEAN_OPT1("Punisher mode"),
    VORTEX_CANNON_OPT1("With black hole"),
    ZX_2_OP1("Scanner mode"),
    HELLION_OPT1("Nano-tracer mode"),
    SHOCKWAVE_OPT1("Tsunami mode"),
    FURNACE_OPT1(""),
    SHOTGUN_OP1("")
    ;

    private String string;

    EnumString(String value){
        this.string = value;
    }

    public String getString(){
        return string;
    }
}
