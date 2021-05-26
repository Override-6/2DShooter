package fr.overrride.game.shooter.session.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import fr.overrride.game.shooter.api.session.character.Shooter;
import fr.overrride.game.shooter.api.session.weapons.Weapon;
import fr.overrride.game.shooter.session.weapons.PistolMuzzle;
import fr.overrride.game.shooter.session.weapons.ShotgunMuzzle;

import java.util.function.Function;

public enum ItemType {

    PISTOL("pistol.png", character -> new Weapon(character, of("pistol.png"), 250, new PistolMuzzle())),
    SHOTGUN("shotgun.png", character -> new Weapon(character, of("shotgun.png"), 1000, new ShotgunMuzzle()));

    private final String texturePath;
    private final Function<Shooter, Weapon> factory;

    ItemType(String texturePath, Function<Shooter, Weapon> factory) {
        this.texturePath = texturePath;
        this.factory = factory;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public Weapon asWeapon(Shooter character) {
        return factory.apply(character);
    }

    private static Texture of(String s) {
        return new Texture(Gdx.files.internal(s));
    }

}
