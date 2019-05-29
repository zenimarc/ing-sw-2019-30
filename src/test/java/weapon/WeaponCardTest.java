package weapon;

import deck.Bullet;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WeaponCardTest {

    @Test
    void getGrabCost() {
        WeaponCard weaponCard_1 = new SimpleWeapon(EnumWeapon.ELECTROSCYTHE);
        WeaponCard weaponCard_2 = new SimpleWeapon(EnumWeapon.LOCK_RIFLE);
        WeaponCard weaponCard_3 = new SimpleWeapon(EnumWeapon.HEATSEEKER);

        List<Bullet> cost_1 = weaponCard_1.getGrabCost();
        List<Bullet> cost_2 = weaponCard_2.getGrabCost();
        List<Bullet> cost_3 = weaponCard_3.getGrabCost();

        assertEquals( weaponCard_1.getCost().size() -1, cost_1.size());
        assertEquals( weaponCard_2.getCost().size() -1, cost_2.size());
        assertEquals( weaponCard_3.getCost().size() -1, cost_3.size());


    }
}