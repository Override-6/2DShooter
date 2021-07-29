package fr.overrride.game.shooter.session.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import fr.overrride.game.shooter.api.session.character.Shooter;
import fr.overrride.game.shooter.session.weapons.SimpleWeapon;
import fr.overrride.game.shooter.session.weapons.PistolMuzzle;
import fr.overrride.game.shooter.session.weapons.ShotgunMuzzle;

import java.util.function.Function;

public enum ItemType {

    PISTOL("pistol.png", character -> new SimpleWeapon(character, of("pistol.png"), 250, new PistolMuzzle())),
    SHOTGUN("shotgun.png", character -> new SimpleWeapon(character, of("shotgun.png"), 1000, new ShotgunMuzzle()));

    private final String texturePath;
    private final Function<Shooter, SimpleWeapon> factory;

    ItemType(String texturePath, Function<Shooter, SimpleWeapon> factory) {
        this.texturePath = texturePath;
        this.factory = factory;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public SimpleWeapon asWeapon(Shooter character) {
        return factory.apply(character);
    }

    private static Texture of(String s) {
        return new Texture(Gdx.files.internal(s));
    }

}
