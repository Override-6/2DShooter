package fr.overrride.test

import com.badlogic.gdx.graphics.Color
import fr.linkit.api.connection.cache.obj.PuppetWrapper
import fr.linkit.api.connection.cache.obj.description.PuppeteerInfo
import fr.linkit.api.local.resource.external.ResourceFolder
import fr.linkit.api.local.system.config.ApplicationConfiguration
import fr.linkit.api.local.system.fsa.FileSystemAdapter
import fr.linkit.api.local.system.security.ApplicationSecurityManager
import fr.linkit.api.local.system.{AppLogger, Version}
import fr.linkit.engine.connection.cache.obj.description.annotation.AnnotationBasedMemberBehaviorFactory
import fr.linkit.engine.connection.cache.obj.description.{SimplePuppetClassDescription, SimpleWrapperBehavior, TreeViewDefaultBehavior}
import fr.linkit.engine.connection.cache.obj.generation.{CloneHelper, PuppetWrapperClassGenerator, WrappersClassResource}
import fr.linkit.engine.connection.cache.obj.invokation.remote.InstancePuppeteer
import fr.linkit.engine.local.LinkitApplication
import fr.linkit.engine.local.generation.compilation.access.DefaultCompilerCenter
import fr.linkit.engine.local.system.fsa.LocalFileSystemAdapters
import fr.overrride.game.shooter.session.GameSessionImpl
import fr.overrride.game.shooter.session.character.ShooterCharacter
import fr.overrride.game.shooter.session.levels.DefaultLevel
import fr.overrride.test.LinkitObjectGenerationTests.forObject
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.{Assertions, BeforeAll, Test, TestInstance}
import org.mockito.Mockito

import scala.reflect.runtime.universe.TypeTag

@TestInstance(Lifecycle.PER_CLASS)
class LinkitObjectGenerationTests {

    @Test
    def detachedClone(): Unit = {
        //println(Nil.hashCode())
        //println(mutable.HashMap.empty[AnyRef, AnyRef].contains(Nil))
        val obj = forObject(new ShooterCharacter(4, 5, Color.LIME))
        val clone = obj.detachedSnapshot()
        println(s"clone = $clone")
    }


}

object LinkitObjectGenerationTests {
    private var resources: ResourceFolder    = _
    private val app      : LinkitApplication = Mockito.mock(classOf[LinkitApplication])

    @BeforeAll
    def init(): Unit = {
        val config      = new ApplicationConfiguration {
            override val pluginFolder   : Option[String]             = None
            override val resourceFolder : String                     = System.getenv("LinkitHome")
            override val fsAdapter      : FileSystemAdapter          = LocalFileSystemAdapters.Nio
            override val securityManager: ApplicationSecurityManager = null
        }
        val testVersion = Version("Tests", "0.0.0", false)
        System.setProperty("LinkitImplementationVersion", testVersion.toString)

        resources = LinkitApplication.prepareApplication(testVersion, config, Seq(getClass))
        Mockito.when(app.getAppResources).thenReturn(resources)
        Mockito.when(app.compilerCenter).thenReturn(new DefaultCompilerCenter)
        LinkitApplication.setInstance(app)
        AppLogger.useVerbose = true
    }

    def forObject[A: TypeTag](obj: A): A with PuppetWrapper[A] = {
        Assertions.assertNotNull(resources)
        val cl = obj.getClass.asInstanceOf[Class[A]]

        import fr.linkit.engine.local.resource.external.LocalResourceFolder._
        val resource    = resources.getOrOpenThenRepresent[WrappersClassResource](LinkitApplication.getProperty("compilation.working_dir.classes"))
        val generator   = new PuppetWrapperClassGenerator(new DefaultCompilerCenter, resource)
        val desc = SimplePuppetClassDescription[A](cl)
        val puppetClass = generator.getPuppetClass[A](desc)
        println(s"puppetClass = $puppetClass")
        val factory = AnnotationBasedMemberBehaviorFactory()
        val pup     = new InstancePuppeteer[A](null, app, null, PuppeteerInfo("", 8, "", Array(1)), SimpleWrapperBehavior(desc, new TreeViewDefaultBehavior(factory)))
        val wrapper  = CloneHelper.instantiateFromOrigin[A](puppetClass, obj)

        wrapper.initPuppeteer(pup)
        wrapper.getChoreographer.forceLocalInvocation {
            println(s"wrapper = $wrapper")
            println(s"wrapper.getWrappedClass = ${wrapper.getWrappedClass}")
        }
        wrapper
    }
}
