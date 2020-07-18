package fr.override.game.shooter.character;

import fr.override.game.shooter.session.GameSessionObject;

public interface Controllable extends GameSessionObject, Shooter, Deplacable {

    void right();

    void left();

    void jump();

    Controller getController();

    void setController(Controller controller);

    CharacterAbilities getAbilities();

    AxisController getAxisController();

}
