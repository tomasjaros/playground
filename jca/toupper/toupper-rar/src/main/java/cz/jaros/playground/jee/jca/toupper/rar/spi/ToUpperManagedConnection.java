package cz.jaros.playground.jee.jca.toupper.rar.spi;

import java.io.IOException;

import javax.resource.ResourceException;
import javax.resource.spi.ManagedConnectionMetaData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.jaros.playground.jee.jca.base.spi.TcpBasedManagedConnection;
import cz.jaros.playground.jee.jca.toupper.rar.ci.ToUpperConnectionImpl;


public class ToUpperManagedConnection extends TcpBasedManagedConnection<ToUpperConnectionImpl> {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    public ToUpperManagedConnection(String host, String port) {
        super(host, Integer.parseInt(port));
    }

    @Override
    protected ToUpperConnectionImpl doCreateAppConnection() {
        return new ToUpperConnectionImpl(this);
    }

    @Override
    protected ManagedConnectionMetaData createMetaData() {
        return new ManagedConnectionMetaData() {
            @Override
            public String getEISProductName() throws ResourceException {
                return "To-Upper system service";
            }

            @Override
            public String getEISProductVersion() throws ResourceException {
                return "1.0.0";
            }

            @Override
            public int getMaxConnections() throws ResourceException {
                return 5;
            }

            @Override
            public String getUserName() throws ResourceException {
                return "defaultUser";
            }
        };
    }

    public String sendAndReceive(String data, long timeout) throws ResourceException {
        try {
            logger.info("Sending input...");
            writeString(data);
            logger.info("Reading output...");
            String output = readString(timeout);
            return output;
        } catch (IOException e) {
            disconnect();
            throw new ResourceException("Error occurred while communicating with EIS.", e);
        }
    }

    public void close(boolean closeConnection) {
        if (closeConnection) {
            disconnect();
        }
        fireConnectionCloseEvent();
    }

}
