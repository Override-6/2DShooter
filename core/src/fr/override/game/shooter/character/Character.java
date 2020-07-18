package fr.override.game.shooter.character;

import fr.override.game.shooter.session.Colorable;
import fr.override.game.shooter.session.GameSessionObject;

public interface Character extends Controllable, Shooter, GameSessionObject, Colorable, LivingEntity, Deplacable {

}
