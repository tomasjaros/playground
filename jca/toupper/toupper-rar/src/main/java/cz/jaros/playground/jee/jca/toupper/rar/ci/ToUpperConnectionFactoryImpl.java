package cz.jaros.playground.jee.jca.toupper.rar.ci;

import javax.resource.spi.ConnectionManager;

import cz.jaros.playground.jee.jca.base.ci.BaseConnectionFactoryImpl;
import cz.jaros.playground.jee.jca.toupper.api.ToUpperConnection;
import cz.jaros.playground.jee.jca.toupper.api.ToUpperConnectionFactory;
import cz.jaros.playground.jee.jca.toupper.rar.spi.ToUpperManagedConnectionFactory;

/**
 * Factory for application connection handles.
 *
 * @author tomas.jaros
 */
public class ToUpperConnectionFactoryImpl extends BaseConnectionFactoryImpl<ToUpperManagedConnectionFactory>
        implements ToUpperConnectionFactory {

    private static final long serialVersionUID = 7073796382738388132L;

    public ToUpperConnectionFactoryImpl(
            ConnectionManager connectionManager,
            ToUpperManagedConnectionFactory managedConnectionFactory) {
        super(connectionManager, managedConnectionFactory);
    }

    @Override
    public ToUpperConnection getConnection() {
        return (ToUpperConnection) super.getConnection();
    }

}
