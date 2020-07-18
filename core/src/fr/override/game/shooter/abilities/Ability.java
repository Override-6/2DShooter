package fr.override.game.shooter.abilities;

public interface Ability {

    void use();

    boolean canUse();

    boolean isUsing();

    void update(float deltaTime);

}
