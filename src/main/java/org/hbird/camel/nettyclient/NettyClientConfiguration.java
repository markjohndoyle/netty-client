package org.hbird.camel.nettyclient;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.camel.RuntimeCamelException;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClientConfiguration implements Cloneable {
	private static final transient Logger LOG = LoggerFactory.getLogger(NettyClientConfiguration.class);

	private String protocol;
	private String host;
	private int port;

	private List<ChannelUpstreamHandler> decoders = new ArrayList<ChannelUpstreamHandler>();

	/**
	 * Returns a copy of this configuration
	 */
	public NettyClientConfiguration copy() {
		try {
			final NettyClientConfiguration answer = (NettyClientConfiguration) clone();
			// Do any deep clone work here, Collection management etc.
			final List<ChannelUpstreamHandler> decodersCopy = new ArrayList<ChannelUpstreamHandler>(decoders);
			answer.setDecoders(decodersCopy);
			return answer;
		} catch (final CloneNotSupportedException e) {
			throw new RuntimeCamelException(e);
		}
	}


	public void parseURI(final URI uri, final Map<String, Object> parameters, final NettyClientComponent component) throws Exception {
		protocol = uri.getScheme();

		if ((!protocol.equalsIgnoreCase("tcp")) && (!protocol.equalsIgnoreCase("udp"))) {
			throw new IllegalArgumentException("Unrecognized Netty protocol: " + protocol + " for uri: " + uri);
		}

		this.host = uri.getHost();
		this.port = uri.getPort();

		// Get list of registry based beans representing ChannelUpstreamHandlers, i.e., decoders.
		final List<ChannelUpstreamHandler> referencedDecoders = component.resolveAndRemoveReferenceListParameter(parameters, "decoders", ChannelUpstreamHandler.class, null);
		if(LOG.isDebugEnabled()) {
			if(referencedDecoders != null) {
				LOG.debug("Discovered the following " + referencedDecoders.size() + " decoder(s): " + StringUtils.join(referencedDecoders.toArray(), ","));
			}
			else {
				LOG.debug("Discovered 0 decoders");
			}
		}
		NettyClientConfiguration.addToHandlersList(decoders, referencedDecoders, ChannelUpstreamHandler.class);
	}

	private static <T> void addToHandlersList(final List<T> configured, final List<T> handlers, final Class<T> handlerType) {
		if (handlers != null) {
			for (int x = 0; x < handlers.size(); x++) {
				final T handler = handlers.get(x);
				if (handlerType.isInstance(handler)) {
					configured.add(handler);
				}
			}
		}
	}

	public boolean isTcp() {
		return protocol.equalsIgnoreCase("tcp");
	}

	public String getAddress() {
		return host + ":" + port;
	}

	public String getProtocol() {
		return protocol;
	}


	public void setProtocol(final String protocol) {
		this.protocol = protocol;
	}


	public String getHost() {
		return host;
	}


	public void setHost(final String host) {
		this.host = host;
	}


	public int getPort() {
		return port;
	}


	public void setPort(final int port) {
		this.port = port;
	}


	public List<ChannelUpstreamHandler> getDecoders() {
		return decoders;
	}


	public void setDecoders(final List<ChannelUpstreamHandler> decoders) {
		this.decoders = decoders;
	}

}
