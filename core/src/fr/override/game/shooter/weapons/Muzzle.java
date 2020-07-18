package fr.override.game.shooter.weapons;

import com.badlogic.gdx.math.Vector2;

public interface Muzzle {

    void fire(Vector2 direction, Weapon weapon);

    void update(float deltaTime);

}
