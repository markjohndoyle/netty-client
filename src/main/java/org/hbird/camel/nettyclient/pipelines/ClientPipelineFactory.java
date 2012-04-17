package org.hbird.camel.nettyclient.pipelines;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;
import org.hbird.camel.nettyclient.NettyClientConsumer;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

public abstract class ClientPipelineFactory implements ChannelPipelineFactory {
	protected NettyClientConsumer consumer;
	protected Exchange exchange;
	protected AsyncCallback callback;

	public ClientPipelineFactory() {
	}

	public ClientPipelineFactory(final NettyClientConsumer consumer, final Exchange exchange, final AsyncCallback callback) {
		this.consumer = consumer;
		this.exchange = exchange;
		this.callback = callback;
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		final ChannelPipeline channelPipeline = Channels.pipeline();
		return channelPipeline;
	}

	public NettyClientConsumer getConsumer() {
		return consumer;
	}

	public void setProducer(final NettyClientConsumer consumer) {
		this.consumer = consumer;
	}

	public Exchange getExchange() {
		return exchange;
	}

	public void setExchange(final Exchange exchange) {
		this.exchange = exchange;
	}

	public AsyncCallback getCallback() {
		return callback;
	}

	public void setCallback(final AsyncCallback callback) {
		this.callback = callback;
	}

}
