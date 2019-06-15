package constants;

public enum EnumAttackName {


    BASE_ATTACK_NAME("Base Attack",""),
    SUPPORT_ATTACK("LOREM IPSUM",""),
    TEST_ATTACK("Test attack",""),

    LOCK_RIFLE_OPT1("With second lock","Deal 1 mark to a different target you can see."),
    MACHINE_GUN_BASE("Base Attack","Deal 1 damage to two different target"),
    MACHINE_GUN_OP1("With focus shot","Deal 1 additional damage to the first of those targets."),
    MACHINE_GUN_OP2("With turret tripod","Deal 1 additional damage to the second of those targets and/or deal 1 damage to a third target you can see."),
    ELECTROSCYHE_OPT1("Reaper mode","Deal 2 damage to every other player on your square."),
    TRACTOR_BEAN_OPT1("Punisher mode","Chose a target 0,1 or 2 moves away from you. Move the target to your square and deal 3 damage to it."),
    VORTEX_CANNON_OPT1("With black hole","Chose up 2 other targets on the vortex or 1 move away from it. Move them onto the vortex and give them each 1 damage."),
    ZX_2_OP1("Scanner mode","Choose up 3 targets you can see and deal 1 mark to each."),
    HELLION_OPT1("Nano-tracer mode","Deal 1 damage to 1 target you can see at least 1 move away. Them give 2 marks to that target and everyone else on that square."),
    SHOCKWAVE_OPT1("Tsunami mode","lorem ipsum"),
    FURNACE_OPT1("Cozy fire mode","lorem ipsum"),
    SHOTGUN_OP1("Long barrel mode","lorem ipsum"),
    SLEDGE_HAMMER("Pulverize mode","lorem ipsum")
    ;

    private String name;
    private String description;

    EnumAttackName(String name, String description){
        this.name = name;
        this.description = description;
    }

    public String getString(){
        return name;
    }

    public String getDescription() {
        return description;
    }
}
