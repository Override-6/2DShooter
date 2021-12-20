package fr.overrride.game.server

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

object Tests {

    def main(args: Array[String]): Unit = {
        import java.io.{ObjectInputStream, ObjectOutputStream}

        val x     = (x: Int) => "jey" * x
        val bytes = new ByteArrayOutputStream()

        try {
            val outputStream = new ObjectOutputStream(bytes)
            try outputStream.writeObject(x)
            finally if (outputStream != null) outputStream.close()
        }

        try {
            val inputStream = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray))
            try inputStream.readObject()
            finally if (inputStream != null) inputStream.close()
        }
    }

}
