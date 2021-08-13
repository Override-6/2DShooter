import com.badlogic.gdx.graphics.Texture
import fr.linkit.engine.connection.packet.persistence.config.PersistenceConfigurationMethods._

putMiniPersistor[Texture, String](_.toString)(new Texture(_))