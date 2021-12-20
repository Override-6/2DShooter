package fr.overrride.game.server

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.{ParticleEffect, SpriteBatch}
import com.badlogic.gdx.math.Vector2
import fr.overrride.game.shooter.api.other.actions.RestAction
import fr.overrride.game.shooter.api.session.ParticleManager

class ServerSideParticleManager extends ParticleManager {

    private final val NoEffect = RestAction.build(new ParticleEffect())

    override def playEffect(file: String, position: Vector2): RestAction[ParticleEffect] = NoEffect

    override def playEffect(file: String, position: Vector2, tint: Color): RestAction[ParticleEffect] = NoEffect

    override def playEffect(file: String, x: Float, y: Float, tint: Color): RestAction[ParticleEffect] = NoEffect

    override def playEffect(file: String, x: Float, y: Float, tint: Color, highMax: Float, highMin: Float, lowMax: Float, lowMin: Float): RestAction[ParticleEffect] = NoEffect

    override def update(deltaTime: Float): Unit = ()

    override def render(batch: SpriteBatch): Unit = ()

    override def dispose(): Unit = ()
}
