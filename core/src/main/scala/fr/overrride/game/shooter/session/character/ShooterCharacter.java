package fr.overrride.game.shooter.session.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import fr.linkit.api.connection.cache.repo.description.annotation.MethodControl;
import fr.overrride.game.shooter.api.session.GameSession;
import fr.overrride.game.shooter.api.session.abilities.Ability;
import fr.overrride.game.shooter.api.session.character.AxisController;
import fr.overrride.game.shooter.api.session.character.Character;
import fr.overrride.game.shooter.api.session.character.Controller;
import fr.overrride.game.shooter.api.session.comps.RectangleComponent;
import fr.overrride.game.shooter.api.session.character.Collidable;
import fr.overrride.game.shooter.session.abilities.Dash;
import fr.overrride.game.shooter.session.components.ProgressBar;
import fr.overrride.game.shooter.api.session.weapons.Bullet;
import fr.overrride.game.shooter.api.session.weapons.Weapon;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static fr.linkit.api.connection.cache.repo.description.annotation.InvocationKind.ONLY_LOCAL;


public class ShooterCharacter extends RectangleComponent implements Character, Collidable {

    private Weapon weapon;
    private Controller<Character> controller = null;
    private GameSession session = null;
    private final AxisController axisController;
    private final ProgressBar healthBar;

    private int airJumps = 0;
    private boolean isOnGround = false;
    private final Vector2 velocity, lastPosition, lastVelocity;

    private final Ability dash;

    public static final float GRAVITY = 100;
    public static final float AIR_FRICTION = 1.25F;
    public static final float GROUND_FRICTION = 1.35F;
    public static final int SPEED = 200;
    public static final int JUMP_HEIGHT = 1750;
    public static final int MAX_HEALTH = 100;

    public ShooterCharacter(float x, float y, Color color) {
        super(x, y, 60, 60, color);

        weapon = Weapon.empty(this);
        velocity = new Vector2();
        lastPosition = new Vector2(position);
        lastVelocity = new Vector2();
        healthBar = new ProgressBar(Color.WHITE, Color.GREEN, 0, 0, (int) width, 7, MAX_HEALTH, MAX_HEALTH);
        axisController = new AxisControllerImpl(this);
        this.dash = new Dash(this);
    }

    @Override
    @MethodControl(ONLY_LOCAL)
    public void update(float deltaTime) {
        lastPosition.set(position);
        lastVelocity.set(velocity);

        handleVelocity(deltaTime);
        handleFriction();
        handleWalkingEffect();

        weapon.update(deltaTime);
        axisController.update(deltaTime);

        healthBar.setPosition(position.x, position.y + height + 5);
        isOnGround = false;
    }

    @Override
    @MethodControl(ONLY_LOCAL)
    public void render(SpriteBatch batch) {
        super.render(batch);
        healthBar.render(batch);
        weapon.render(batch);
    }

    @Override
    @MethodControl(ONLY_LOCAL)
    public void dispose() {
        super.dispose();
        weapon.dispose();
        healthBar.dispose();
    }

    @Override
    public void right() {
        velocity.x += SPEED;
    }

    @Override
    public void left() {
        velocity.x -= SPEED;
    }

    @Override
    public void jump() {

        if (isOnGround)
            airJumps = 0;
        else airJumps++;

        if (airJumps >= 2)
            return;

        velocity.y += JUMP_HEIGHT;

    }

    @Override
    public void shoot() {
        weapon.shoot();
    }

    @Override
    @MethodControl(ONLY_LOCAL)
    public Controller<Character> getController() {
        return controller;
    }

    @Override
    public void setController(Controller<Character> controller) {
        this.controller = controller;
    }

    @Override
    @MethodControl(ONLY_LOCAL)
    public Weapon getWeapon() {
        return weapon;
    }

    @Override
    public void setWeapon(Weapon weapon) {
        this.weapon.dispose();
        weapon.setGameSession(session);
        this.weapon = weapon;
    }

    @Override
    public boolean canShoot() {
        return weapon.canShoot();
    }

    @Override
    @MethodControl(ONLY_LOCAL)
    public Vector2 getVelocity() {
        return velocity;
    }

    @Override
    @MethodControl(ONLY_LOCAL)
    public Vector2 getLocation() {
        return position;
    }

    @Override
    @MethodControl(ONLY_LOCAL)
    public void onCollision(Collidable collidable) {
        if (collidable instanceof Bullet) {
            handleBulletCollision((Bullet) collidable);
            return;
        }

        if (!collidable.isSolid())
            return;

        Rectangle hitBox = collidable.getHitBox();
        replaceNextTo(hitBox);

        if (collidable instanceof Character) {
            handleCharacterCollision((Character) collidable);
        }
    }

    @Override
    @MethodControl(ONLY_LOCAL)
    public boolean canCollide() {
        return true;
    }

    @Override
    @MethodControl(ONLY_LOCAL)
    public void setCollidable(boolean canCollide) {

    }

    @Override
    @MethodControl(ONLY_LOCAL)
    public Optional<GameSession> getCurrentGameSession() {
        return Optional.ofNullable(session);
    }

    @Override
    @MethodControl(ONLY_LOCAL)
    public void setGameSession(@Nullable GameSession gameSession) {
        this.session = gameSession;
        if (weapon != null)
            weapon.setGameSession(gameSession);
        healthBar.setGameSession(gameSession);
    }

    @Override
    @MethodControl(ONLY_LOCAL)
    public float getHealth() {
        return healthBar.getProgress();
    }

    @Override
    @MethodControl(ONLY_LOCAL)
    public float getMaxHealth() {
        return MAX_HEALTH;
    }

    @Override
    public void damage(float f) {
        healthBar.setProgress(healthBar.getProgress() - f);
        if (getHealth() <= 0)
            kill();
    }

    @Override
    public void heal(float f) {
        if (getHealth() <= MAX_HEALTH)
            healthBar.setProgress(healthBar.getProgress() + f);
    }

    @Override
    public void setHealth(float f) {
        healthBar.setProgress(f);
        if (getHealth() <= 0)
            kill();

    }

    @Override
    public void kill() {
        getCurrentGameSession().ifPresent(session -> {
            session.getParticleManager()
                    .playEffect("particles/dead.party", weapon.getLocation().x, position.y - 30, getColor());
            dispose();
            session.removeObject(this);
        });
        healthBar.setProgress(0);
    }

    @Override
    public AxisController getAxisController() {
        return axisController;
    }

    @Override
    public String toString() {
        return "ShooterCharacter{" +
                "healthBar=" + healthBar +
                ", color=" + getColor() +
                '}';
    }

    private void applyBulletVelocity(Bullet bullet) {
        Vector2 bulletVelocity = bullet.getVelocity();
        float weight = bullet.getWeight();
        float multiplier = weight / (3.6F * 5);
        velocity.set(bulletVelocity.x * multiplier, bulletVelocity.y * multiplier);
    }

    private void handleCharacterCollision(Character character) {
        if (dash.isUsing()) {
            character.getVelocity().x = lastVelocity.x * 1.5F;

            character.damage(50);
            axisController.blockXAxisFor(0);

            boolean isBackward = lastVelocity.x > 0;

            float x = position.x + (isBackward ? width : 0);

            if (isBackward)
                session.getParticleManager().playEffect("particles/dashImpact.party", x, position.y, getColor());
            else session.getParticleManager().playEffect("particles/dashImpact.party", x, position.y, getColor(),
                    -50.0F, -150.0F, 0, 0);
        }
    }

    private void replaceNextTo(Rectangle hitBox) {
        float boxX = hitBox.x;
        float boxY = hitBox.y;
        float boxWidth = hitBox.width;
        float boxHeight = hitBox.height;

        boolean top = lastPosition.y >= boxY + boxHeight;
        boolean bottom = lastPosition.y + height <= boxY;
        boolean right = lastPosition.x >= boxX + boxWidth - 15;
        boolean left = lastPosition.x + width - 15 <= boxX;

        if (top) {
            position.y = boxY + boxHeight;
            velocity.y = 0;
            airJumps = -1;
        } else if (bottom) {
            position.y = boxY - height;
            velocity.y = 0;
        } else if (right) {
            position.x = boxX + boxWidth;
            velocity.x = 0;
        } else if (left) {
            position.x = boxX - width;
            velocity.x = 0;
        }
        isOnGround = top;
        healthBar.setPosition(position.x, position.y + height + 5);
    }

    private void handleVelocity(float deltaTime) {
        velocity.sub(0, GRAVITY);
        velocity.scl(deltaTime);
        position.add(velocity);
        velocity.scl(1 / deltaTime);
    }

    private void handleFriction() {
        if (velocity.x < 10 && velocity.x > -10) {
            velocity.x = 0;
            return;
        }
        if (isOnGround)
            velocity.x /= GROUND_FRICTION;
        else velocity.x /= AIR_FRICTION;
    }

    private void handleBulletCollision(Bullet bullet) {
        if (bullet.getShooter() == this)
            return;
        applyBulletVelocity(bullet);
    }

    private void handleWalkingEffect() {
        if (!isOnGround || (velocity.x >= -10 && velocity.x <= 10))
            return;
        boolean isBackward = velocity.x < 0;
        float x = isBackward ? position.x + width : position.x;
        session.getParticleManager()
                .playEffect("particles/walk.party", x, position.y, getColor());
    }

    @Override
    public void dash() {
        dash.use();
    }
}
