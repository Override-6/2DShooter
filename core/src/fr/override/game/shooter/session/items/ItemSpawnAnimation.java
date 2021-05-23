package fr.override.game.shooter.session.items;

import fr.overrride.game.shooter.api.other.actions.Action;
import fr.overrride.game.shooter.api.other.actions.ActionCompleter;
import fr.overrride.game.shooter.api.other.actions.SimpleAction;
import fr.overrride.game.shooter.api.other.animations.Animable;
import fr.overrride.game.shooter.api.other.animations.Animator;

public class ItemSpawnAnimation implements Animator {

    private Animable animated;
    private final float width, height;
    private final int millis;
    private float currentWidth = 0;
    private float currentHeight = 0;
    private ActionCompleter currentAction;
    /**
     * -1 = play backward
     * 0 = stopped
     * 1 = play forward
     */
    private byte state = 0;

    public ItemSpawnAnimation(Item animated, float width, float height, int millis) {
        this.animated = animated;
        this.width = width;
        this.height = height;
        this.millis = millis;
    }

    @Override
    public void stepForward(float deltaTime) {
        float toAddWidth = width / (deltaTime * millis);
        float toAddHeight = height / (deltaTime * millis);
        currentWidth += toAddWidth;
        currentHeight += toAddHeight;
        animated.setSize(currentWidth, currentHeight);
        if (currentWidth >= width || currentHeight >= height)
            state = 0;
        else state = 1;
    }

    @Override
    public void stepBackward(float deltaTime) {
        float toSubWidth = width / (deltaTime * millis);
        float toSubHeight = height / (deltaTime * millis);
        currentWidth -= toSubWidth;
        currentHeight -= toSubHeight;
        animated.setSize(currentWidth, currentHeight);
        if (currentWidth <= 0 || currentHeight <= 0)
            state = 0;
        else state = -1;
    }

    @Override
    public void reset() {
        currentHeight = 0;
        currentWidth = 0;
    }

    @Override
    public boolean isTerminated() {
        return state == 0;
    }

    @Override
    public Action playReverse() {
        state = -1;
        SimpleAction action = Action.build();
        currentAction = action;
        return action;
    }

    @Override
    public void stop() {
        state = 0;
    }

    @Override
    public Action play() {
        state = 1;
        SimpleAction action = Action.build();
        currentAction = action;
        return action;
    }

    @Override
    public void setAnimated(Animable animated) {
        this.animated = animated;
    }

    @Override
    public void update(float deltaTime) {
        switch (state) {
            case 1:
                stepForward(deltaTime);
                break;
            case -1:
                stepBackward(deltaTime);
        }
        if (state == 0 && currentAction != null) {
            currentAction.onActionCompleted();
            currentAction = null;
        }
    }
}