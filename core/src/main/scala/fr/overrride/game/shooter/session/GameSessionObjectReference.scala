package fr.overrride.game.shooter.session

import fr.linkit.api.gnom.reference.NetworkObjectReference

case class GameSessionObjectReference(id: String) extends NetworkObjectReference {

    override def toString: String = s"@session/$id"

}
