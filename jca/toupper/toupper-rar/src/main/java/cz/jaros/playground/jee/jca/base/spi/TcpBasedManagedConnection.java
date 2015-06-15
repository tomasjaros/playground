package cz.jaros.playground.jee.jca.base.spi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.resource.ResourceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TcpBasedManagedConnection<T> extends AbstractManagedConnection<T> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String host;
    private final int port;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;


    public TcpBasedManagedConnection(String host, int port) {
        super();
        this.host = host;
        this.port = port;
    }

    protected abstract T doCreateAppConnection(TcpBasedManagedConnection<T> managedConnection);

    @Override
    protected T doGetConnection(AbstractManagedConnection<T> managedConnection)
            throws ResourceException {
        if (socket == null || socket.isClosed()) {
            try {
                connect();
            } catch (IOException e) {
                throw new ResourceException("Failed to establish new connection.", e);
            }
        }
        return doCreateAppConnection(this);
    }

    private void connect() throws IOException {
        logger.info(String.format("Connecting to %s on port %s...", host, port));
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        logger.info("Connected!");
    }

    protected void disconnect() {
        try {
            socket.close();
            socket = null;
        } catch (IOException e) {
            logger.error("Failed to close open TCP connection.", e);
        }
    }

    public String readString() throws IOException {
        String line = in.readLine();
        logger.info("IN  <- " + line);
        return line;
    }

    public void writeString(String data) {
        logger.info("OUT -> " + data);
        out.println(data);
    }

}
