package org.hbird.camel.nettyclient.handlers;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.hbird.camel.nettyclient.NettyClientConsumer;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleCamelForwarderClientHandler extends SimpleChannelHandler {
	private static final transient Logger LOG = LoggerFactory.getLogger(SimpleCamelForwarderClientHandler.class);
	private final NettyClientConsumer consumer;

	public SimpleCamelForwarderClientHandler(final NettyClientConsumer nettyClientConsumer) {
		this.consumer = nettyClientConsumer;
	}

	@Override
	public void messageReceived(final ChannelHandlerContext ctx, final MessageEvent e) throws Exception {
		final ChannelBuffer buf = (ChannelBuffer) e.getMessage();
		final byte[] incomingByteArray = buf.array();

		LOG.debug("Incoming byte array size: " + incomingByteArray.length);

		final Exchange exchange = consumer.getEndpoint().createExchange();
		exchange.setPattern(ExchangePattern.OutOnly);
		exchange.getIn().setBody(incomingByteArray);

		try {
			consumer.getProcessor().process(exchange);
		}
		catch (final Throwable e1) {
			consumer.getExceptionHandler().handleException(e1);
		}

		e.getChannel().close();
	}

	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final ExceptionEvent e) {
		LOG.debug(e.getCause().getStackTrace().toString());
		e.getChannel().close();
	}

}
