package fr.override.game.shooter.api.session.character;

public interface Character extends Controllable<Character>, Shooter, GameSessionObject, Colorable, LivingEntity, Deplacable {

    void dash();

}
