package cz.jaros.playground.jee.jca.toupper.rar.spi;

import javax.resource.spi.ConfigProperty;
import javax.resource.spi.ConnectionDefinition;
import javax.resource.spi.ConnectionManager;

import cz.jaros.playground.jee.jca.base.spi.AbstractManagedConnectionFactory;
import cz.jaros.playground.jee.jca.toupper.api.ToUpperConnection;
import cz.jaros.playground.jee.jca.toupper.api.ToUpperConnectionFactory;
import cz.jaros.playground.jee.jca.toupper.rar.ci.ToUpperConnectionFactoryImpl;
import cz.jaros.playground.jee.jca.toupper.rar.ci.ToUpperConnectionImpl;


@ConnectionDefinition(
        connection = ToUpperConnection.class,
        connectionImpl = ToUpperConnectionImpl.class,
        connectionFactory = ToUpperConnectionFactory.class,
        connectionFactoryImpl = ToUpperConnectionFactoryImpl.class)
public class ToUpperManagedConnectionFactory extends AbstractManagedConnectionFactory<ToUpperManagedConnection> {

    private static final long serialVersionUID = 7848684102544389489L;

    @ConfigProperty(type = String.class, defaultValue = "localhost")
    private String host;
    @ConfigProperty(type = String.class, defaultValue = "6666")
    private String port;


    @Override
    protected ToUpperManagedConnection doCreateManagedConnection() {
        return new ToUpperManagedConnection(host, port);
    }

    @Override
    protected Object doCreateConnectionFactory(ConnectionManager cxManager) {
        return new ToUpperConnectionFactoryImpl(cxManager, this);
    }

    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }
    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        ToUpperManagedConnectionFactory that = (ToUpperManagedConnectionFactory) obj;
        if (host == null) {
            if (that.host != null) {
                return false;
            }
        } else if (!host.equals(that.host)) {
            return false;
        }
        if (port == null) {
            if (that.port != null) {
                return false;
            }
        } else if (!port.equals(that.port)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return (host == null ? 0 : host.hashCode())
                + 17 * (port == null ? 0 : port.hashCode());
    }

}
