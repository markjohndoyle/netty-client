/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hbird.camel.nettyclient;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.hbird.camel.nettyclient.pipelines.DefaultClientPipelineFactory;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The NettyClient consumer.
 */
public class NettyClientConsumer extends DefaultConsumer {
	private static final transient Logger LOG = LoggerFactory.getLogger(NettyClientConsumer.class);

	private final NettyClientEndpoint endpoint;

	private final NettyClientConfiguration configuration;

	private final DefaultChannelGroup allChannels;


	public NettyClientConsumer(final NettyClientEndpoint endpoint, final Processor processor, final NettyClientConfiguration configuration) {
		super(endpoint, processor);
		this.endpoint = endpoint;
		this.configuration = configuration;
		this.allChannels = new DefaultChannelGroup("NettyClientConsumer-" + endpoint.getEndpointUri());
	}

	@Override
	protected void doStart() throws Exception {
		super.doStart();
		LOG.debug("Netty consumer binding to: {}", configuration.getAddress());

		super.doStart();
		if (configuration.isTcp()) {
			initializeTCPClientSocketCommunicationLayer();
		}
		else {
			initializeUDPClientSocketCommunicationLayer();
		}

		LOG.info("Netty consumer bound to: " + configuration.getAddress());
	}

	private void initializeTCPClientSocketCommunicationLayer() {
		final ChannelFactory factory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());

		final ClientBootstrap bootstrap = new ClientBootstrap(factory);

		final DefaultClientPipelineFactory defaultPipelineFactory = new DefaultClientPipelineFactory(this, getEndpoint().createExchange());
		bootstrap.setPipelineFactory(defaultPipelineFactory);

		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);

		final ChannelFuture channel = bootstrap.connect(new InetSocketAddress(configuration.getHost(), configuration.getPort()));
		allChannels.add(channel.getChannel());
	}

	private void initializeUDPClientSocketCommunicationLayer() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("UDP currently not implemented");
	}

	@Override
	protected void doStop() throws Exception {
		if(LOG.isDebugEnabled()) {
			LOG.debug("NettyClient consumer unbinding from: {}", configuration.getAddress());
		}

		// close all channels
		if(LOG.isTraceEnabled()) {
			LOG.trace("Closing {} channels", allChannels.size());
		}
		final ChannelGroupFuture future = allChannels.close();
		future.awaitUninterruptibly();

		super.doStop();

		LOG.info("NettyClient consumer unbound from: " + configuration.getAddress());
	}

}
