package org.hbird.camel.nettyclient;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;

/**
 * Represents a NettyClient endpoint.
 */
public class NettyClientEndpoint extends DefaultEndpoint {

	private NettyClientConfiguration configuration;

    public NettyClientEndpoint(final String uri, final NettyClientComponent component, final NettyClientConfiguration config) {
    	super(uri, component);
    	configuration = config;
    }

    public NettyClientEndpoint(final String uri, final NettyClientComponent component) {
        super(uri, component);
    }

    @Override
	public Producer createProducer() throws Exception {
    	throw new UnsupportedOperationException("Producing not implemented for this endpoint:" + getEndpointUri());
    }

    @Override
	public Consumer createConsumer(final Processor processor) throws Exception {
        return new NettyClientConsumer(this, processor, configuration);
    }

    @Override
	public boolean isSingleton() {
        return true;
    }
}
