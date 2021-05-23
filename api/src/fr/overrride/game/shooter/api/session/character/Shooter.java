package fr.overrride.game.shooter.api.session.character;

import fr.overrride.game.shooter.api.session.weapons.Weapon;

public interface Shooter extends GameSessionObject, Deplacable {

    Weapon getWeapon();

    void setWeapon(Weapon weapon);

    boolean canShoot();

    void shoot();

}
