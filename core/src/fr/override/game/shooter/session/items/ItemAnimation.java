package fr.override.game.shooter.session.items;

import com.badlogic.gdx.math.Vector2;
import fr.override.game.shooter.api.other.actions.Action;
import fr.override.game.shooter.api.other.actions.ActionCompleter;
import fr.override.game.shooter.api.other.actions.SimpleAction;
import fr.override.game.shooter.api.other.animations.Animable;
import fr.override.game.shooter.api.other.animations.Animator;
import org.jetbrains.annotations.Nullable;

public class ItemAnimation implements Animator {

    private Animable animated;
    /**
     * -1 = play backward
     * 0 = stopped
     * 1 = play forward
     */
    private int state = 0;
    private float current = 0;
    private final float yMax = 50;
    private final float millis = 1500;
    @Nullable
    private ActionCompleter currentAction;
    //TODO
    public ItemAnimation(Item item) {
        this.animated = item;
    }

    @Override
    public void stepForward(float deltaTime) {
        float toAdd = yMax / (deltaTime * millis);
        current += toAdd;
        System.out.println(current);
        if (current > yMax) {
            current = yMax;
            stop();
        }
        Vector2 loc = animated.getLocation();
        animated.setLocation(loc.x, loc.y + current);
    }

    @Override
    public void stepBackward(float deltaTime) {
        float toSub = yMax / (deltaTime * millis);
        current -= toSub;
        if (current < 0) {
            current = 0;
            stop();
        }
        Vector2 loc = animated.getLocation();
        animated.setLocation(loc.x, loc.y + current);
    }

    @Override
    public void reset() {
        current = 0;
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
        System.out.println(state);
        switch (state) {
            case 0:
                return;
            case 1:
                stepForward(deltaTime);
                break;
            case -1:
                stepBackward(deltaTime);
        }
        if (isTerminated() && currentAction != null)
            currentAction.onActionCompleted();
    }
}
