package fr.override.game.shooter.api.session.character;


import com.badlogic.gdx.math.Rectangle;

public interface Collidable extends GameSessionObject {

    void onCollision(Collidable collidable);

    boolean canCollide();

    boolean isSolid();

    default void setSolid(boolean solid) {
        /* no-op */
    }

    default void setCollidable(boolean canCollide) {
        /* no-op */
    }

    Rectangle getHitBox();

    float getRotation();

    void setRotation(float angle);

}
