package cz.jaros.playground.jee.jca.base.ci;

import java.io.Serializable;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.resource.Referenceable;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ManagedConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base implementation of factories for application connection handles.
 *
 * @author tomas.jaros
 * @param <T> type of factory for managed connections.
 */
public abstract class BaseConnectionFactoryImpl<T extends ManagedConnectionFactory>
        implements Serializable, Referenceable {

    private static final long serialVersionUID = 1L;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ConnectionManager connectionManager;
    private final T managedConnectionFactory;

    private Reference reference;


    public BaseConnectionFactoryImpl(ConnectionManager connectionManager, T managedConnectionFactory) {
        this.connectionManager = connectionManager;
        this.managedConnectionFactory = managedConnectionFactory;
    }

    /**
     * Get new application connection handle using the provided connection manager
     * and factory of managed connections.
     */
    public Object getConnection() {
        logger.info("getConnection()");
        try {
            return connectionManager.allocateConnection(managedConnectionFactory, null);
        } catch (ResourceException e) {
            throw new IllegalStateException("Can not create new application connection handle.", e);
        }
    }

    @Override
    public Reference getReference() throws NamingException {
        return reference;
    }

    @Override
    public void setReference(Reference reference) {
        this.reference = reference;
    }

}
