package fr.override.game.shooter.animations;

import fr.override.game.shooter.actions.Action;
import fr.override.game.shooter.actions.ActionCompleter;
import fr.override.game.shooter.actions.SimpleAction;

public class RotationAnimation implements Animator {

    private final float from, to, millis;
    private float current;
    private Animable animated;

    private ActionCompleter currentAction;

    /**
     * -1 = play backward
     * 0 = stopped
     * 1 = play forward
     */
    private byte state = 0;

    public RotationAnimation(Animable animated, float from, float to, float millis) {
        this.from = from;
        this.to = to;
        this.millis = millis;
        this.current = from;
        this.animated = animated;
    }

    @Override
    public void stepForward(float deltaTime) {
        float toSub = (from - to) / (deltaTime * millis);
        current -= toSub;

        if (current >= to) {
            current = to;
            state = 0;
        } else state = 1;

        animated.setRotation(current);
    }

    @Override
    public void stepBackward(float deltaTime) {
        float toAdd = (from - to) / (deltaTime * millis);

        current += toAdd;

        if (current <= from) {
            current = from;
            state = 0;
        } else state = -1;

        animated.setRotation(current);
    }

    @Override
    public void reset() {
        current = from;
    }

    @Override
    public boolean isTerminated() {
        return state == 0;
    }

    @Override
    public Action play() {
        state = 1;
        current = from;
        SimpleAction action = Action.build();
        currentAction = action;
        return action;
    }

    @Override
    public void setAnimated(Animable animated) {
        this.animated = animated;
    }

    @Override
    public Action playReverse() {
        state = -1;
        current = to;
        SimpleAction action = Action.build();
        currentAction = action;
        return action;
    }

    @Override
    public void stop() {
        state = 0;
    }

    @Override
    public void update(float deltaTime) {
        switch (state) {
            case -1:
                stepBackward(deltaTime);
                return;
            case 1:
                stepForward(deltaTime);
                return;
            case 0:
                if (currentAction != null) {
                    currentAction.onActionCompleted();
                    currentAction = null;
                }
        }
    }

}
