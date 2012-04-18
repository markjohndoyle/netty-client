package org.hbird.camel.nettyclient;

import java.net.URI;
import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link NettyClientEndpoint}.
 */
public class NettyClientComponent extends DefaultComponent {
	private NettyClientConfiguration configuration;
    @Override
	protected Endpoint createEndpoint(final String uri, final String remaining, final Map<String, Object> parameters) throws Exception {

    	NettyClientConfiguration config;
        if (configuration != null) {
            config = configuration.copy();
        } else {
            config = new NettyClientConfiguration();
        }

        config.parseURI(new URI(remaining), parameters, this);

        Endpoint endpoint = new NettyClientEndpoint(uri, this, config);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}
