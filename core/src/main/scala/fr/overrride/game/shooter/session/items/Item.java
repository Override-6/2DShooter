package fr.overrride.game.shooter.session.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import fr.overrride.game.shooter.api.other.animations.Animable;
import fr.overrride.game.shooter.api.other.animations.Animator;
import fr.overrride.game.shooter.api.session.GameSession;
import fr.overrride.game.shooter.api.session.GameSessionObject;
import fr.overrride.game.shooter.api.session.character.Collidable;
import fr.overrride.game.shooter.api.session.character.Shooter;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class Item implements GameSessionObject, Collidable, Animable {

    private float lifeTime;
    private final ItemType type;
    private final Texture texture;
    private final Vector2 position;
    private float width, height, rotation;
    private transient Animator spawnAnimator;

    private GameSession session;

    public Item(float x, float y, float lifeTime, ItemType type) {
        this.lifeTime = lifeTime;
        this.type = type;
        this.texture = new Texture(type.getTexturePath());
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        this.rotation = 0;
        this.position = new Vector2(x, y);
    }

    public Item(float x, float y, ItemType type) {
        this(x, y, Float.MAX_VALUE, type);
    }

    private void createAnimation() {
        this.spawnAnimator = new ItemSpawnAnimation(this, texture.getWidth(), texture.getHeight(), 1000);
        spawnAnimator.play();
    }

    @Override
    public void update(float deltaTime) {
        if (spawnAnimator == null)
            createAnimation();
        lifeTime -= deltaTime * 3600;
        if (lifeTime <= 0)
            spawnAnimator.playReverse();
                    //.then(this::dispose);
        spawnAnimator.update(deltaTime);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y, .5F, .5F, 1, 1, width, height, rotation, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
    }

    @Override
    public Optional<GameSession> getCurrentGameSession() {
        return Optional.ofNullable(session);
    }

    @Override
    public void setGameSession(@Nullable GameSession gameSession) {
        this.session = gameSession;
        if (gameSession != null)
            session.addObject(this);
    }

    @Override
    public void dispose() {
        if (session != null)
            session.removeObject(this);
        texture.dispose();
    }

    @Override
    public void onCollision(Collidable collidable) {
        if (collidable instanceof Shooter) {
            Shooter shooter = (Shooter) collidable;
            shooter.setWeapon(type.asWeapon(shooter));
            dispose();
        }
    }

    @Override
    public boolean canCollide() {
        return true;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public Rectangle getHitBox() {
        return new Rectangle(position.x, position.y, 25, 25);
    }

    @Override
    public float getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(float angle) {
        this.rotation = angle;
    }

    @Override
    public Vector2 getSize() {
        return new Vector2(width, height);
    }

    @Override
    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public Vector2 getLocation() {
        return position;
    }

    @Override
    public DrawPriority getDrawPriority() {
        return DrawPriority.FOREGROUND;
    }

    @Override
    public void setLocation(float x, float y) {
        position.set(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Float.compare(item.lifeTime, lifeTime) == 0 && Float.compare(item.width, width) == 0 && Float.compare(item.height, height) == 0 && Float.compare(item.rotation, rotation) == 0 && type == item.type && Objects.equals(texture, item.texture) && Objects.equals(position, item.position) && Objects.equals(spawnAnimator, item.spawnAnimator) && Objects.equals(session, item.session);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lifeTime, type, texture, position, width, height, rotation, spawnAnimator, session);
    }
}
