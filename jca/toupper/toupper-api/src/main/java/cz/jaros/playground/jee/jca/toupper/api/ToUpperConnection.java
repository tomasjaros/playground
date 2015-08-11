package cz.jaros.playground.jee.jca.toupper.api;

public interface ToUpperConnection {

    String send(String input, long timeout) throws ToUpperException;

    void close();

}
