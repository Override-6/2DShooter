package fr.override.game.shooter.animations;

import com.badlogic.gdx.math.Vector2;

public interface Animable {

    Vector2 getLocation();

    void setLocation(Vector2 vector2);

    float getRotation();

    void setRotation(float angle);

    Vector2 getSize();

    void setSize(float width, float height);

}
