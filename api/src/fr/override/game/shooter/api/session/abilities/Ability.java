package fr.override.game.shooter.api.session.abilities;

public interface Ability {

    void use();

    boolean canUse();

    boolean isUsing();

    void update(float deltaTime);

}
