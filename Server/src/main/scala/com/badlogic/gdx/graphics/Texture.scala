package com.badlogic.gdx.graphics

class Texture(path: String) extends GLTexture(0, 0) {
    override def toString: String = path

    override def getWidth: Int = 0

    override def getHeight: Int = 0

    override def getDepth: Int = 0

    override def isManaged: Boolean = false

    override def reload(): Unit = 0
}
