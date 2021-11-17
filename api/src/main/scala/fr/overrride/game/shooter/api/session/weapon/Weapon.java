package fr.overrride.game.shooter.api.session.weapon;

import fr.overrride.game.shooter.api.other.animations.Animable;
import fr.overrride.game.shooter.api.session.GameSessionObject;
import fr.overrride.game.shooter.api.session.character.Shooter;

public interface Weapon extends GameSessionObject, Animable {
    void shoot();

    boolean canShoot();

    Shooter getOwner();
}
