package fr.overrride.game.shooter.api.session.character;

import fr.overrride.game.shooter.api.session.GameSessionObject;

public interface Controllable<T extends Controllable<T>> extends GameSessionObject, Shooter, Deplacable {

    Controller<T> getController();

    void setController(Controller<T> controller);

    AxisController getAxisController();

    void jump();

    void left();

    void shoot();

    void right();

}
