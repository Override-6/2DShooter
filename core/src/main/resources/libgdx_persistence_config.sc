import fr.linkit.api.application.ApplicationContext
import fr.linkit.api.gnom.packet.traffic.PacketTraffic
import fr.linkit.engine.gnom.persistence.context.PersistenceConfigBuilder

//Start Of Context
val builder: PersistenceConfigBuilder = null
val app    : ApplicationContext       = null
val traffic: PacketTraffic            = null

//ENd Of Context
/*setTConverter[Texture, String](_.toString) { str =>
    val thread = Thread.currentThread()
    if (thread.getName == "LWJGL Application")
        new Texture(str)
    else {
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
}*/