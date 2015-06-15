package cz.jaros.playground.jee.jca.toupper.rar.ci;

import javax.resource.ResourceException;

import cz.jaros.playground.jee.jca.toupper.api.ToUpperConnection;
import cz.jaros.playground.jee.jca.toupper.api.ToUpperException;
import cz.jaros.playground.jee.jca.toupper.rar.spi.ToUpperManagedConnection;

public class ToUpperConnectionImpl implements ToUpperConnection {

    private final ToUpperManagedConnection managedConnection;


    public ToUpperConnectionImpl(ToUpperManagedConnection managedConnection) {
        this.managedConnection = managedConnection;
    }

    @Override
    public String send(String input) throws ToUpperException {
        try {
            return managedConnection.sendAndReceive(input);
        } catch (ResourceException e) {
            throw new ToUpperException("Can not get response from ToUpper service.", e);
        }
    }

    @Override
    public void close() {
        managedConnection.close(false);
    }

}
