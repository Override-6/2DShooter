package fr.overrride.game.shooter.api.session.character;

import com.badlogic.gdx.Gdx;

import java.util.function.Consumer;

public final class KeyControl<T extends Controllable> {

    private final KeyType identifier;
    private final int keyCode;
    private final Consumer<T> onPressed;

    private KeyControl(KeyType identifier, int keyCode, Consumer<T> onPressed) {
        this.identifier = identifier;
        this.keyCode = keyCode;
        this.onPressed = onPressed;
    }

    public void update(T controllable) {
        if (isPressed())
            onPressed.accept(controllable);
    }

    private boolean isPressed() {
        return identifier.canBeHold()
                ? Gdx.input.isKeyPressed(keyCode)
                : Gdx.input.isKeyJustPressed(keyCode);
    }

    public KeyType getIdentifier() {
        return identifier;
    }

    public static <T extends Controllable> KeyControl<T> of(KeyType identifier, int keyCode, Consumer<T> onPressed) {
        return new KeyControl<T>(identifier, keyCode, onPressed);
    }

}
