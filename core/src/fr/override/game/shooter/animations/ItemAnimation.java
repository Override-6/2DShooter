package fr.override.game.shooter.animations;

import fr.override.game.shooter.actions.Action;

public class ItemAnimation implements Animator {

    private Animable animated;
    private boolean end = true;

    public ItemAnimation(Animable item) {
        this.animated = item;
    }

    @Override
    public void stepForward(float deltaTime) {

    }

    @Override
    public void stepBackward(float deltaTime) {

    }

    @Override
    public void reset() {

    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public Action playReverse() {
        return null;
    }

    @Override
    public void stop() {
        this.end = true;
    }

    @Override
    public Action play() {
        return null;
    }

    @Override
    public void setAnimated(Animable animated) {
        this.animated = animated;
    }

    @Override
    public void update(float deltaTime) {

    }
}
