package weapon;

public enum EnumWeaponType {

    SIMPLE_WEAPON("SimpleWeapon"),
    MOVEMENT_WEAPON("MovementWeapon"),
    DISTANCE_WEAPON("DistanceWeapon"),
    AREA_WEAPON("AreaWeapon"),
    CARDINAL_WEAPON("CardinalWeapon"),
    PRIORITY_WEAPON("PriorityWeapon");


    private String name;

    EnumWeaponType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
