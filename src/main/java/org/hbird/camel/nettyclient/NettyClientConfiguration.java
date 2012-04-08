package org.hbird.camel.nettyclient;

import java.net.URI;
import java.util.Map;

import org.apache.camel.RuntimeCamelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClientConfiguration implements Cloneable {
	private static final transient Logger LOG = LoggerFactory.getLogger(NettyClientConfiguration.class);

	private String protocol;
	private String host;
	private int port;

	/**
	 * Returns a copy of this configuration
	 */
	public NettyClientConfiguration copy() {
		try {
			final NettyClientConfiguration answer = (NettyClientConfiguration) clone();
			// Do any deep clone work here, Collection management etc.
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

}
