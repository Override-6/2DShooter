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
