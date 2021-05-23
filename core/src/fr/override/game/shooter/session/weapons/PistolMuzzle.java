package fr.override.game.shooter.session.weapons;

import com.badlogic.gdx.math.Vector2;
import fr.overrride.game.shooter.api.other.animations.Animator;
import fr.overrride.game.shooter.api.other.animations.RotationAnimation;
import fr.overrride.game.shooter.api.session.weapons.Bullet;
import fr.overrride.game.shooter.api.session.weapons.Muzzle;
import fr.overrride.game.shooter.api.session.weapons.Weapon;
import org.jetbrains.annotations.Nullable;

public class PistolMuzzle implements Muzzle {

    @Nullable
    private Animator shootAnimator;

    @Override
    public void fire(Vector2 direction, Weapon weapon) {
        Bullet.create(weapon.getOwner(), weapon.getLocation(), direction.scl(1500), 15, 5.5F);
        makeShootAnimation(direction, weapon);
    }

    @Override
    public void update(float deltaTime) {
        if (shootAnimator != null)
            shootAnimator.update(deltaTime);
    }

    private void makeShootAnimation(Vector2 direction, Weapon weapon) {
        float rotation = direction.angle();
        shootAnimator = new RotationAnimation(weapon, rotation, rotation + (rotation > 90 && rotation < 270 ? -45 : 45), 350);
        shootAnimator.play()
                .then(() -> shootAnimator.playReverse()
                        .then(() -> shootAnimator = null));
    }

}
