package weapon;

public class WeaponFactory {

    public WeaponCard getFactory(enumWeapon type) {

        switch (type){
            case DESTRUCTOR:
                return new Destructor();
        }





        return null;
    }
}
