package org.hbird.camel.nettyclient.pipelines;

import java.util.List;

import org.apache.camel.Exchange;
import org.hbird.camel.nettyclient.NettyClientConsumer;
import org.hbird.camel.nettyclient.handlers.SimpleCamelForwarderClientHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.channel.Channels;

public class DefaultClientPipelineFactory extends ClientPipelineFactory {

	private final SimpleCamelForwarderClientHandler camelHandler;

	public DefaultClientPipelineFactory(final NettyClientConsumer consumer, final Exchange exchange) {
		super(consumer, exchange);
		camelHandler = new SimpleCamelForwarderClientHandler(consumer, exchange);
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		final ChannelPipeline channelPipeline = Channels.pipeline();

		final List<ChannelUpstreamHandler> decoders = consumer.getConfiguration().getDecoders();
		if(decoders != null && decoders.size() > 0) {
			for(final ChannelUpstreamHandler decoder : decoders) {
				channelPipeline.addLast("UserSpecifieddecoder-" + decoder.toString(), decoder);
			}
		}

		channelPipeline.addLast("SimpleCamelForwarderClientHandler", camelHandler);

		return channelPipeline;
	}
}
