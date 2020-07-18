package fr.override.game.shooter.character;

import com.badlogic.gdx.math.Vector2;
import fr.override.game.shooter.session.GameSessionObject;
import fr.override.game.shooter.weapons.Bullet;
import fr.override.game.shooter.weapons.Weapon;

public interface Shooter extends GameSessionObject, Deplacable {

    Weapon getWeapon();

    void setWeapon(Weapon weapon);

    boolean canShoot();

    void shoot();

}
