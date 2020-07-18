package fr.override.game.shooter.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Controller {

    private int left, right, jump, dash;
    private final Controllable controllable;


    public Controller(Controllable controllable) {
        this.controllable = controllable;
    }

    public void setJumpKey(int jumpKey) {
        this.jump = jumpKey;
    }

    public void setLeftKey(int leftKey) {
        this.left = leftKey;
    }

    public void setRightKey(int rightKey) {
        this.right = rightKey;
    }

    public void setDashKey(int dashKey) {
        this.dash = dashKey;
    }

    public Controllable getCharacter() {
        return controllable;
    }

    public void handleInputs() {
        handleDirectionAndShoot();
        handleAbilities();
    }

    private void handleAbilities() {
        CharacterAbilities abilities = controllable.getAbilities();
        if (Gdx.input.isKeyJustPressed(dash) && abilities.dash().canUse())
            abilities.dash().use();
    }

    private void handleDirectionAndShoot() {
        if (Gdx.input.isKeyPressed(left))
            controllable.left();
        if (Gdx.input.isKeyPressed(right))
            controllable.right();
        if (Gdx.input.isKeyJustPressed(jump))
            controllable.jump();
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && controllable.canShoot())
            controllable.shoot();
    }
}
