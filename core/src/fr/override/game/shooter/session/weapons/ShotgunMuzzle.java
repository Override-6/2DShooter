package fr.override.game.shooter.session.weapons;

import com.badlogic.gdx.math.Vector2;
import fr.override.game.shooter.api.other.animations.Animator;
import fr.override.game.shooter.api.other.animations.RotationAnimation;
import fr.override.game.shooter.api.session.weapons.Bullet;
import fr.override.game.shooter.api.session.weapons.Muzzle;
import fr.override.game.shooter.api.session.weapons.Weapon;
import org.jetbrains.annotations.Nullable;

public class ShotgunMuzzle implements Muzzle {

    @Nullable
    private Animator shootAnimation;

    @Override
    public void fire(Vector2 direction, Weapon weapon) {
        direction.scl(2000);
        Bullet.create(weapon, new Vector2(direction).rotate(-5), 10, 15);
        Bullet.create(weapon, new Vector2(direction), 10, 15);
        Bullet.create(weapon, new Vector2(direction).rotate(5), 10, 15);
        makeShootAnimations(direction, weapon);
    }

    private void makeShootAnimations(Vector2 direction, Weapon weapon) {
        this.shootAnimation = new RotationAnimation(weapon, weapon.getRotation(), weapon.getRotation() + 45, 350);
        shootAnimation.play()
                .then(() -> shootAnimation.playReverse()
                        .then(() -> shootAnimation = null));
        Vector2 ownerVel = weapon.getOwner().getVelocity();
        ownerVel.set(-direction.x, -direction.y);
    }

    @Override
    public void update(float deltaTime) {
        if (shootAnimation != null)
            shootAnimation.update(deltaTime);
    }
}
