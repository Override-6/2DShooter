package fr.overrride.game.shooter.api.other.animations;

import com.badlogic.gdx.math.Vector2;

public interface Animable {

    Vector2 getLocation();

    void setLocation(float x, float y);

    float getRotation();

    void setRotation(float angle);

    Vector2 getSize();

    void setSize(float width, float height);

}
