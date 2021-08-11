package fr.overrride.game.shooter.api.session.comps;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import fr.linkit.api.connection.cache.obj.behavior.annotation.FieldControl;
import fr.overrride.game.shooter.api.session.GameSession;
import fr.overrride.game.shooter.api.session.character.Collidable;
import fr.overrride.game.shooter.api.session.character.Colorable;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class RectangleComponent implements Collidable, Colorable {

    private final Texture texture = new Texture("componentBase.png");
    @FieldControl()
    protected final Vector2 position;
    private Color color;
    protected GameSession gameSession;
    private float rotation = 0;

    protected float width, height;
    private boolean canCollide = true;
    private boolean solid = true;

    public RectangleComponent(float x, float y, float width, float height, Color color) {
        this.position = new Vector2(x, y);
        this.color = color;
        this.width = width;
        this.height = height;
    }

    @Override
    public Optional<GameSession> getCurrentGameSession() {
        return Optional.ofNullable(gameSession);
    }

    @Override
    public void setGameSession(@Nullable GameSession gameSession) {
        this.gameSession = gameSession;
    }

    @Override
    public void update(float deltaTime) {

    }

    private boolean isTextureLoaded = false;

    @Override
    public void render(SpriteBatch batch) {
        if (!isTextureLoaded){
            texture.load(texture.getTextureData());
            isTextureLoaded = true;
        }
        Color old = new Color(batch.getColor());
        batch.setColor(color);
        batch.draw(texture, position.x, position.y, width, height);
        batch.setColor(old);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

    @Override
    public void onCollision(Collidable collidable) {

    }

    @Override
    public boolean canCollide() {
        return canCollide;
    }

    @Override
    public boolean isSolid() {
        return solid;
    }

    @Override
    public void setSolid(boolean solid) {
        this.solid = solid;
    }

    @Override
    public void setCollidable(boolean canCollide) {
        this.canCollide = canCollide;
    }

    @Override
    public Rectangle getHitBox() {
        return new Rectangle(position.x, position.y, width, height);
    }

    protected void render(SpriteBatch batch, float originX, float originY, float rotation) {
        Color old = new Color(batch.getColor());
        batch.setColor(color);
        batch.draw(texture, position.x, position.y, originX, originY, width, height, 1, 1, rotation, 0, 0, 50, 50, false, false);
        batch.setColor(old);

    }

    public void setPosition(Vector2 position) {
        this.position.set(position);
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    @Override
    public float getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    @Override
    public Vector2 getLocation() {
        return position;
    }

    @Override
    public DrawPriority getDrawPriority() {
        return DrawPriority.BACKGROUND;
    }
}
