package fr.override.game.shooter.session.character;

import fr.override.game.shooter.api.session.character.Character;
import fr.override.game.shooter.api.session.character.Controller;
import fr.override.game.shooter.api.session.character.KeyControl;

import java.util.ArrayList;
import java.util.Collection;

public class CharacterController implements Controller<Character> {

    private final Collection<KeyControl<Character>> controls = new ArrayList<>();
    private final Character character;


    public CharacterController(Character controllable) {
        this.character = controllable;
    }

    @Override
    public void addKeyControl(KeyControl<Character> control) {
        controls.add(control);
    }

    @Override
    public void update() {
        controls.forEach(control -> control.update(character));
    }
}
