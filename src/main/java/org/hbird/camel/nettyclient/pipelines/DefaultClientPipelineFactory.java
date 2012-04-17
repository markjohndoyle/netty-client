package org.hbird.camel.nettyclient.pipelines;

import org.apache.camel.Exchange;
import org.hbird.camel.nettyclient.NettyClientConsumer;
import org.hbird.camel.nettyclient.handlers.SimpleCamelForwarderClientHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;

public class DefaultClientPipelineFactory extends ClientPipelineFactory {

	private final SimpleCamelForwarderClientHandler camelHandler;

	public DefaultClientPipelineFactory(final NettyClientConsumer consumer, final Exchange exchange) {
		super();
		camelHandler = new SimpleCamelForwarderClientHandler(consumer, exchange);
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		final ChannelPipeline channelPipeline = Channels.pipeline();

		channelPipeline.addLast("SimpleCamelForwarderClientHandler", camelHandler);
		return channelPipeline;
	}
}
