package com.badlogic.gdx.graphics

class Texture(str: String) extends GLTexture(0, 0) {

    override def toString: String = str

    override def getWidth: Int = 0

    override def getHeight: Int = 0

    override def getDepth: Int = 0

    override def isManaged: Boolean = false

    override def reload(): Unit = ()
}
