package fr.override.game.shooter.character;

import fr.override.game.shooter.abilities.Ability;
import fr.override.game.shooter.abilities.Dash;

public class CharacterAbilities {

    private final Ability dash;

    public CharacterAbilities(Character character) {
        this.dash = new Dash(character);
    }

    public Ability dash() {
        return dash;
    }


    public void updateAbilities(float dt) {
        dash.update(dt);
    }

}
