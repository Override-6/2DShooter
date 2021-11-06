import com.badlogic.gdx.graphics.Texture
import fr.linkit.api.application.ApplicationContext
import fr.linkit.api.gnom.packet.traffic.PacketTraffic
import fr.linkit.engine.gnom.persistence.context.PersistenceConfigBuilder
import fr.overrride.game.shooter.session.PlayState.lwjglProcrastinator

//Start Of Context
val builder: PersistenceConfigBuilder = null
val app    : ApplicationContext       = null
val traffic: PacketTraffic            = null
import builder._
//ENd Of Context
setTConverter[Texture, String](_.toString)(new Texture(_), lwjglProcrastinator)
/*
setTConverter[Texture, String](_.toString) { str =>
    val thread = Thread.currentThread()
    if (thread.getName == "LWJGL Application")
        new Texture(str)
    else {
        var result: Texture = null
        Gdx.app.postRunnable(() => {
            result = new Texture(str)
            LockSupport.unpark(thread)
            AppLogger.warn("Unparked ! (gdx)")
        })
        if (result eq null) {
            AppLogger.warn("Parking (wt)")
            LockSupport.park()
            AppLogger.warn("Unparked ! (wt)")
        }
        result
    }
}*/