package fr.override.game.shooter.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.override.game.shooter.api.other.GraphicComponent;

public class Button implements GraphicComponent, InteractiveElement {

    private final Texture normal, hover, pressed;
    private final float x, y;
    private boolean isHover, isPressed;
    private final Runnable onAction;

    public Button(Texture normal, Texture hover, Texture pressed, float x, float y, Runnable onAction) {
        this.normal = normal;
        this.hover = hover;
        this.pressed = pressed;
        this.x = x;
        this.y = y;
        this.onAction = onAction;
        isHover = false;
        isPressed = false;
    }

    public Button(Texture normal, float x, float y, Runnable onAction) {
        this(normal, normal, normal, x, y, onAction);
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void render(SpriteBatch batch) {
        if (isPressed && !isHover) { //handle click cancelling by dragging the mouse out of the element.
            release();
            return;
        }
        if (isPressed) {
            batch.draw(pressed, x, y);
            return;
        }
        if (isHover) {
            batch.draw(pressed, x, y);
            return;
        }
        batch.draw(normal, x, y);
    }

    @Override
    public void dispose() {
        normal.dispose();
        hover.dispose();
        pressed.dispose();
    }

    @Override
    public void press() {
        isPressed = true;
    }

    @Override
    public void release() {
        isPressed = false;
        onAction.run();
    }

    @Override
    public void hover() {
        isHover = true;
    }

    @Override
    public void unHover() {
        isHover = false;
    }
}
