package fr.override.game.shooter.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import fr.override.game.shooter.character.Shooter;
import fr.override.game.shooter.weapons.PistolMuzzle;
import fr.override.game.shooter.weapons.ShotgunMuzzle;
import fr.override.game.shooter.weapons.Weapon;

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
