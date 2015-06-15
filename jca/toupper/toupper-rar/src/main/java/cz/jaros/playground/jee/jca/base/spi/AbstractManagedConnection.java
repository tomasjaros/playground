package cz.jaros.playground.jee.jca.base.spi;

import java.io.PrintWriter;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionEvent;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.LocalTransaction;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionMetaData;
import javax.resource.spi.SharingViolationException;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract generic base implementation of managed connections.
 *
 * @author tomas.jaros
 * @param <T> Type of application connection handle.
 */
public abstract class AbstractManagedConnection<T> implements ManagedConnection {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private PrintWriter logWriter;
    private ConcurrentLinkedQueue<ConnectionEventListener> listeners = new ConcurrentLinkedQueue<>();
    private T connectionHandle;    // Application connection handle


    protected abstract T doGetConnection(AbstractManagedConnection<T> managedConnection)
            throws ResourceException;
    protected abstract ManagedConnectionMetaData createMetaData();


    @Override
    public T getConnection(Subject subject, ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        logMethodCall("getConnection()");
        connectionHandle = doGetConnection(this);
        return connectionHandle;
    }

    @Override
    public void destroy() throws ResourceException {
        logMethodCall("destroy()");
        listeners.clear();
    }

    @Override
    public void cleanup() throws ResourceException {
        logMethodCall("cleanup()");
        this.connectionHandle = null;
    }

    protected void fireConnectionCloseEvent() {
        logMethodCall("fireConnectionCloseEvent() -> " + connectionHandle);
        ConnectionEvent event = new ConnectionEvent(this, ConnectionEvent.CONNECTION_CLOSED);
        event.setConnectionHandle(connectionHandle);
        ConnectionEventListener[] listenersArray = listeners.toArray(new ConnectionEventListener[listeners.size()]);
        for (ConnectionEventListener listener : listenersArray) {
            listener.connectionClosed(event);
        }
    }

    @Override
    public void associateConnection(Object connection) throws ResourceException {
        logMethodCall("associateConnection()");
        throw new SharingViolationException("Connection sharing is not supported.");
    }

    @Override
    public void addConnectionEventListener(ConnectionEventListener listener) {
        logMethodCall("addConnectionEventListener() -> " + listener);
        listeners.add(listener);
    }

    @Override
    public void removeConnectionEventListener(ConnectionEventListener listener) {
        logMethodCall("removeConnectionEventListener() -> " + listener);
        listeners.remove(listener);
    }

    @Override
    public XAResource getXAResource() throws ResourceException {
        logMethodCall("getXAResource()");
        throw new NotSupportedException("XA resources are not supported.");
    }

    @Override
    public LocalTransaction getLocalTransaction() throws ResourceException {
        logMethodCall("getLocalTransaction()");
        throw new NotSupportedException("Local transactions are not supported");
    }

    @Override
    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        logMethodCall("getMetaData()");
        return createMetaData();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws ResourceException {
        logMethodCall("setLogWriter() -> " + out);
        this.logWriter = out;
    }

    @Override
    public PrintWriter getLogWriter() throws ResourceException {
        logMethodCall("getLogWriter()");
        return logWriter;
    }


    private void logMethodCall(String msg) {
        logger.info("" + this + " -- " + msg);
    }

}
