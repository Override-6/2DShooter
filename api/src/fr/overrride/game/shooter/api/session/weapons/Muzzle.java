package fr.overrride.game.shooter.api.session.weapons;

import com.badlogic.gdx.math.Vector2;

public interface Muzzle {

    void fire(Vector2 direction, Weapon weapon);

    void update(float deltaTime);

}
