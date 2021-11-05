import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import fr.linkit.api.application.ApplicationContext
import fr.linkit.api.gnom.packet.traffic.PacketTraffic
import fr.linkit.engine.gnom.persistence.context.PersistenceConfigBuilder
import org.graalvm.compiler.nodes.virtual.LockState

import java.util.concurrent.locks.LockSupport

//Start Of Context
val builder: PersistenceConfigBuilder = null
val app    : ApplicationContext       = null
val traffic: PacketTraffic            = null

import builder._

//ENd Of Context
setTConverter[Texture, String](_.toString) { str =>
    val thread = Thread.currentThread()
    var result: Texture = null
    Gdx.app.postRunnable(() => {
        result = new Texture(str)
        LockSupport.unpark(thread)
        println("Unparked ! (gdx)")
    })
    if (result eq null) {
        println("Parking (wt)")
        LockSupport.park()
        println("Unparked ! (wt)")
    }
    result
}