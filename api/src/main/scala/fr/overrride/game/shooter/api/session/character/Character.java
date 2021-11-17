package fr.overrride.game.shooter.api.session.character;

import fr.overrride.game.shooter.api.session.GameSessionObject;

public interface Character extends Controllable<Character>, Shooter, GameSessionObject, Colorable, LivingEntity, Deplacable {

    void dash();

}
