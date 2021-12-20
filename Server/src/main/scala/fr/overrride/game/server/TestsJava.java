package fr.overrride.game.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.function.Function;

public class TestsJava {

    public static void main(String[] args) throws Exception {
        Function<Integer, String> x = (j) -> "jey" + j;
        var bytes = new ByteArrayOutputStream();
        var outputStream = new ObjectOutputStream(bytes);
        outputStream.writeObject(x);
        var inputStream = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()));
        inputStream.readObject();
    }

}
