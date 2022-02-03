package fr.overrride.game.shooter.session.weapons;

import com.badlogic.gdx.math.Vector2;
import fr.linkit.api.gnom.cache.sync.invocation.InvocationChoreographer;
import fr.linkit.api.gnom.cache.sync.invocation.InvocationChoreographer$;
import fr.overrride.game.shooter.api.other.animations.Animator;
import fr.overrride.game.shooter.api.other.animations.RotationAnimation;
import fr.overrride.game.shooter.api.session.weapon.Bullet;
import fr.overrride.game.shooter.api.session.weapon.Muzzle;
import fr.overrride.game.shooter.api.session.weapon.Weapon;
import org.jetbrains.annotations.Nullable;
import scala.runtime.BoxedUnit;

public class PistolMuzzle implements Muzzle {

    @Nullable
    private transient Animator shootAnimator;

    @Override
    public boolean isPlayingRecoilAnimation() {
        return shootAnimator != null;
    }

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
