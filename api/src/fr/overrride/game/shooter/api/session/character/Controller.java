package fr.overrride.game.shooter.api.session.character;

public interface Controller<T extends Controllable<?>> {

    void addKeyControl(KeyControl<T> control);

    void update();

}
