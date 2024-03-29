package fr.overrride.game.shooter.session.weapons;

import com.badlogic.gdx.math.Vector2;
import fr.overrride.game.shooter.api.other.animations.Animator;
import fr.overrride.game.shooter.api.other.animations.RotationAnimation;
import fr.overrride.game.shooter.api.session.weapon.Bullet;
import fr.overrride.game.shooter.api.session.weapon.Muzzle;
import fr.overrride.game.shooter.api.session.weapon.Weapon;
import org.jetbrains.annotations.Nullable;

public class ShotgunMuzzle implements Muzzle {

    @Nullable
    private transient Animator shootAnimator;
    @Override
    public boolean isPlayingRecoilAnimation() {
        return shootAnimator != null;
    }

    @Override
    public void fire(Vector2 direction, Weapon weapon) {
        direction.scl(2000);
        Bullet.create(weapon, new Vector2(direction).rotate(-5), 10, 15);
        Bullet.create(weapon, new Vector2(direction), 10, 15);
        Bullet.create(weapon, new Vector2(direction).rotate(5), 10, 15);
        makeShootAnimations(direction, weapon);
    }

    private void makeShootAnimations(Vector2 direction, Weapon weapon) {
        this.shootAnimator = new RotationAnimation(weapon, weapon.getRotation(), weapon.getRotation() + 45, 350);
        shootAnimator.play()
                .then(() -> shootAnimator.playReverse()
                        .then(() -> shootAnimator = null));
        Vector2 ownerVel = weapon.getOwner().getVelocity();
        ownerVel.set(-direction.x, -direction.y);
    }

    @Override
    public void update(float deltaTime) {
        if (shootAnimator != null)
            shootAnimator.update(deltaTime);
    }
}
