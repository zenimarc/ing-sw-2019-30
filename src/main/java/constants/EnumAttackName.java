package constants;

/**
 * EnumAttackName gives info about attacks
 */
public enum EnumAttackName {


    BASE_ATTACK_NAME("Base Attack",""),
    SUPPORT_ATTACK("LOREM IPSUM",""),
    TEST_ATTACK("Test attack",""),

    //Simple Weapon
    LOCK_RIFLE_BASE("Basic effect","Deal 2 damage and 1 mark to a target you can see."),
    LOCK_RIFLE_OPT1("With second lock","Deal 1 mark to a different target you can see."),
    MACHINE_GUN_BASE("Basic effect","Deal 1 damage to two different target."),
    MACHINE_GUN_OP1("With focus shot","Deal 1 additional damage to the first of those targets."),
    MACHINE_GUN_OP2("With turret tripod","Deal 1 additional damage to the second of those targets and/or deal 1 damage to a third target you can see."),
    ELECTROSCYHE_BASE("Basic mode","Deal 1 damage to every other player on your square."),
    ELECTROSCYHE_OPT1("Reaper mode","Deal 2 damage to every other player on your square."),
    ZX_2_BASE("Basic mode","Deal 1 damage and 2 marks to 1 target you can see."),
    ZX_2_OP1("Scanner mode","Choose up 3 targets you can see and deal 1 mark to each."),
    THOR_BASE("Basic effect", "Deal 2 damage to 1 target you can see."),
    THOR_OPT1("Chain reaction", "reaction: Deal 1 damage to a second target that your first target can see."),
    THOR_OPT2("Rocket fist mode", "voltage: Deal 2 damage to a third target that your second target can see. You cannot use this effect unless you first use the chain reaction."),
    HEATSEEKER_BASE("Effect","Choose 1 target you cannot see and deal 3 damage to it."),
    //Area weapon
    FURNACE_BASE("Basic mode","Choose a room you can see, but not the room you are in. Deal 1 damage to everyone in that room."),
    FURNACE_OPT1("Cozy fire mode","Choose a square exactly one move away. Deal 1 damage and 1 mark to everyone on that square."),
    //Movement weapon
    TRACTOR_BEAN_OPT1("Punisher mode","Chose a target 0,1 or 2 moves away from you. Move the target to your square and deal 3 damage to it."),
    VORTEX_CANNON_OPT1("With black hole","Chose up 2 other targets on the vortex or 1 move away from it. Move them onto the vortex and give them each 1 damage."),
    SHOTGUN_OP1("Long barrel mode","lorem ipsum"),
    SLEDGE_HAMMER("Pulverize mode","lorem ipsum"),
    //Distance Weapon
    WHISPER_BASE("Effect","Deal 3 damage and 1 mark to 1 target you can see. Your target must be at least 2 moves away from you"),
    HELLION_BASE("Basic mode","Deal 1 damage to 1 target you can see at least 1 move away. Than give 1 mark to that target and everyone else on that square"),
    HELLION_OPT1("Nano-tracer mode","Deal 1 damage to 1 target you can see at least 1 move away. Them give 2 marks to that target and everyone else on that square."),
    SHOCK_WAVE_BASE("Basic Mode","Choose up to 3 targets on different squares, each exactly 1 move away. Deal 1 damage to each target."),
    SHOCK_WAVE_OPT1("Tsunami mode","Deal 1 damage to all targets that are exactly 1 move away"),
    //Cardinal attack
    RAILGUN_BASE("Basic mode", "Choose a cardinal direction and 1 target in that direction. Deal 3 damage to it."),
    RAILGUN_OP1("Piercing mode", "Choose a cardinal direction and 1 or 2 targets in that direction. Deal 2 damage to each."),
    FLAMETHROWER_BASE("Basic mode", "mode: Choose a square 1 move away and possibly a second square 1 more move away in the same direction. On each square, you may choose 1 target and give it 1 damage."),
    FLAMETHROWER_OP1("Barbecue mode", "Choose 2 squares as above. Deal 2 damage to everyone on the first square and 1 damage to everyone on the second square."),
    POWERGLOVE_BASE("Basic mode", "Choose 1 target on any square exactly 1 move away. Move onto that squareand give the target 1 damage and 2 marks."),
    POWERGLOVE_OP1("Rocket fist mode", "Choose a square exactly 1 move away. Move onto that square. You may deal 2 damage to 1 target there. If you want, you may move 1 more square in that same direction (but only if it is a legal move). You may deal 2 damage to 1 target there, as well."),
    //Priority weapon
    CYBERBLADE_BASE("Basic effect", "Deal 2 damage to 1 target on your square."),
    CYBERBLADE_OPT1("Shadow effect", "Move 1 square before or after the basic effect."),
    CYBERBLADE_OPT2("Slice and dice", "Deal 2 damage to a different target on your square. The shadowstep may be used before or after this effect."),
    ROCKET_LAUNCHER_BASE("Rocket jump", "Move 1 or 2 squares. This effect can be used either before or after the basic effect."),
    ROCKET_LAUNCHER_OPT1("Basic mode", "Choose 1 target on any square exactly 1 move away. Move onto that squareand give the target 1 damage and 2 marks."),
    ROCKET_LAUNCHER_OPT2("Fragmenting warhead", "During the basic effect, deal 1 damage to every player on your target's original square – including the target, even if you move it."),
    PLASMA_GUN_BASE("Basic effect", "Deal 2 damage to 1 target you can see."),
    PLASMA_GUN_OPT1("Phase glide", "Move 1 or 2 squares. This effect can be used either before or after the basic effect."),
    PLASMA_GUN_OPT2("Charged shot", "shot: Deal 1 additional damage to your target."),
    GRENADE_LAUNCHER_BASE("Basic effect", "effect: Deal 1 damage to 1 target you can see. Then you may move the target 1 square."),
    GRENADE_LAUNCHER_OPT1("Extra Grenade", "grenade: Deal 1 damage to every player on a square you can see. You can use this before or after the basic effect's move."),
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
