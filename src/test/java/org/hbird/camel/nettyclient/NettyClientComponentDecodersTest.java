package org.hbird.camel.nettyclient;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.junit.Before;
import org.junit.Test;

public class NettyClientComponentDecodersTest extends CamelTestSupport {

	private ServerSocket serverSocket;

	public static final int TEST_PORT = 4444;

	@Before
	public void setUpSockets() throws Exception {
		serverSocket = new ServerSocket(TEST_PORT);
	}

	@Test
	public void testNettyClient() throws Exception {
		final Socket clientSocket = serverSocket.accept();

		final byte[] rawOut = TestData.createRawTestData();
		clientSocket.getOutputStream().write(rawOut, 0, 9);

		final MockEndpoint mock = getMockEndpoint("mock:result");
		mock.expectedMinimumMessageCount(1);
		mock.expectedBodiesReceived(rawOut);
		assertMockEndpointsSatisfied();

		final Object msgBody = mock.getExchanges().get(0).getIn().getBody();
		final byte[] receivedBytes = (byte[]) msgBody;

		// Extra asserts - really checking!
		assertEquals(rawOut.length, receivedBytes.length);

		assertEquals(TestData.BYTE, receivedBytes[0]);

		//@formatter:off
		final int oneReceived = receivedBytes[1] & TestData.BYTE_INT_MASK +
				(receivedBytes[2] & TestData.BYTE_INT_MASK << 8) +
				(receivedBytes[3] & TestData.BYTE_INT_MASK << 16) +
				(receivedBytes[4] & TestData.BYTE_INT_MASK << 24);
		assertEquals(TestData.INT_ONE, oneReceived);

		final int twoReceived = receivedBytes[5] & TestData.BYTE_INT_MASK +
				(receivedBytes[6] & TestData.BYTE_INT_MASK << 8) +
				(receivedBytes[7] & TestData.BYTE_INT_MASK << 16) +
				(receivedBytes[8] & TestData.BYTE_INT_MASK << 24);
		assertEquals(TestData.INT_TWO, twoReceived);
		//@formatter:on

	}


	@Override
	protected JndiRegistry createRegistry() throws Exception {
		final JndiRegistry reg = super.createRegistry();

		final LengthFieldBasedFrameDecoder lengthDecoder = new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4);
		reg.bind("lengthDecoder", lengthDecoder);

		final List<ChannelUpstreamHandler> decoders = new ArrayList<ChannelUpstreamHandler>();
		decoders.add(lengthDecoder);

		reg.bind("decoders", decoders);
		return reg;
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			@Override
			public void configure() {
				from("nettyclient:tcp://localhost:" + TEST_PORT +"?decoders=#lengthDecoder").to("mock:result");
			}
		};
	}
}