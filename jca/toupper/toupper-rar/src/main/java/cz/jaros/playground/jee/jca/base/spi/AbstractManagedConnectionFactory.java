package cz.jaros.playground.jee.jca.base.spi;

import java.io.PrintWriter;
import java.util.Set;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterAssociation;
import javax.security.auth.Subject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract implementation of factories for managed connections.
 *
 * @author tomas.jaros
 * @param <T> Type representing managed connections this factory produces.
 */
public abstract class AbstractManagedConnectionFactory<T extends ManagedConnection>
        implements ManagedConnectionFactory, ResourceAdapterAssociation {

    private static final long serialVersionUID = 8362797283103281838L;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ResourceAdapter resourceAdapter;
    private PrintWriter logWriter;


    //~ Abstract methods to be implemented by child classes

    protected abstract T doCreateManagedConnection();
    protected abstract Object doCreateConnectionFactory(ConnectionManager cxManager);

    @Override
    public Object createConnectionFactory(ConnectionManager cxManager) throws ResourceException {
        logMethodCall("createConnectionFactory()");
        return doCreateConnectionFactory(cxManager);
    }

    @Override
    public Object createConnectionFactory() throws ResourceException {
        throw new NotSupportedException("Non-managed connection factory not supported.");
    }

    @Override
    public ManagedConnection createManagedConnection(Subject subject,
            ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        logMethodCall("createManagedConnection()");
        T managedConnection = doCreateManagedConnection();
        logger.info("Constructed managed connection: " + managedConnection);
        return managedConnection;
    }

    @Override
    public ManagedConnection matchManagedConnections(@SuppressWarnings("rawtypes") Set connectionSet,
            Subject subject, ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        logMethodCall("matchManagedConnections()");

        ManagedConnection match = null;
        /* This resource adapter has no additional parameters for connections,
         * so any open connection can be used by an application */
        for (Object mco : connectionSet) {
            if (mco != null) {
                match = (ManagedConnection) mco;
                logger.info("Connection match!");
                break;
            } else {
                logger.info("Connection MISMATCH!");
            }
        }
        return match;
    }

    @Override
    public ResourceAdapter getResourceAdapter() {
        return resourceAdapter;
    }

    @Override
    public void setResourceAdapter(ResourceAdapter ra) throws ResourceException {
        this.resourceAdapter = ra;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws ResourceException {
        this.logWriter = out;
    }

    @Override
    public PrintWriter getLogWriter() throws ResourceException {
        return logWriter;
    }

    private void logMethodCall(String msg) {
        logger.info("" + this + " -- " + msg);
    }

}
