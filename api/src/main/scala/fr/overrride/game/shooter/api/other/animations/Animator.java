package fr.overrride.game.shooter.api.other.animations;

import fr.overrride.game.shooter.api.other.actions.Action;

public interface Animator {

    void stepForward(float deltaTime);

    void stepBackward(float deltaTime);

    void reset();

    boolean isTerminated();

    Action playReverse();

    void stop();

    Action play();

    void setAnimated(Animable animated);

    void update(float deltaTime);

}
