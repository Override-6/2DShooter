package fr.overrride.game.shooter.api.session.character;

public interface LivingEntity extends Deplacable {

    float getHealth();

    float getMaxHealth();

    void damage(float i);

    void heal(float i);

    void setHealth(float i);

    void kill();

}
