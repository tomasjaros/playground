package cz.jaros.playground.jee.jca.toupper.rar;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.Connector;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.xa.XAResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Connector(
        displayName = "ToUpperResourceAdapter",
        vendorName = "jaros.playground",
        version = "1.0")
public class ToUpperResourceAdapter implements ResourceAdapter {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void start(BootstrapContext ctx) throws ResourceAdapterInternalException {
        logger.info("To-Upper resource adapter started.");
    }

    @Override
    public void stop() {
        logger.info("To-Upper resource adapter stopped.");
    }

    @Override
    public void endpointActivation(MessageEndpointFactory endpointFactory, ActivationSpec spec) throws ResourceException {
        throw new NotSupportedException("Unsupported inbound connection.");
    }

    @Override
    public void endpointDeactivation(MessageEndpointFactory endpointFactory, ActivationSpec spec) {
        throw new RuntimeException(new NotSupportedException("Unsupported inbound connection."));
    }

    @Override
    public XAResource[] getXAResources(ActivationSpec[] specs) throws ResourceException {
        throw new NotSupportedException("Unsupported XA trx.");
    }

}
