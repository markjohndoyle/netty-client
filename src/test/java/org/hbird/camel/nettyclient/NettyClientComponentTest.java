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

import java.net.ServerSocket;
import java.net.Socket;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;

public class NettyClientComponentTest extends CamelTestSupport {

	private ServerSocket serverSocket;

	private static final byte BYTE = 1;
	private static final int INT_ONE = 255;
	private static final int INT_TWO = 90;
	private static final int BYTE_INT_MASK = 0xff;
	private static final int TEST_PORT = 4444;

	@Before
	public void setUpSockets() throws Exception {
		serverSocket = new ServerSocket(TEST_PORT);
	}

	@Test
	public void testNettyClient() throws Exception {
		Socket clientSocket = serverSocket.accept();

		final byte[] rawOut = NettyClientComponentTest.createRawTestData();
		clientSocket.getOutputStream().write(rawOut, 0, 9);

		MockEndpoint mock = getMockEndpoint("mock:result");
		mock.expectedMinimumMessageCount(1);
		mock.expectedBodiesReceived(rawOut);
		assertMockEndpointsSatisfied();

		Object msgBody = mock.getExchanges().get(0).getIn().getBody();
		byte[] receivedBytes = (byte[]) msgBody;

		// Extra asserts - really checking!
		assertEquals(rawOut.length, receivedBytes.length);

		assertEquals(BYTE, receivedBytes[0]);

		//@formatter:off
		int oneReceived = receivedBytes[1] & BYTE_INT_MASK +
						 (receivedBytes[2] & BYTE_INT_MASK << 8) +
						 (receivedBytes[3] & BYTE_INT_MASK << 16) +
						 (receivedBytes[4] & BYTE_INT_MASK << 24);
		assertEquals(INT_ONE, oneReceived);

		int twoReceived = receivedBytes[5] & BYTE_INT_MASK +
						 (receivedBytes[6] & BYTE_INT_MASK << 8) +
						 (receivedBytes[7] & BYTE_INT_MASK << 16) +
						 (receivedBytes[8] & BYTE_INT_MASK << 24);
		assertEquals(INT_TWO, twoReceived);
		//@formatter:on

	}

	private static byte[] createRawTestData() {
		byte[] bytes = new byte[9];

		bytes[0] = BYTE;
		bytes[1] = (byte) INT_ONE;
		bytes[2] = (byte) (INT_ONE >> 8);
		bytes[3] = (byte) (INT_ONE >> 16);
		bytes[4] = (byte) (INT_ONE >> 24);
		bytes[5] = (byte) INT_TWO;
		bytes[6] = (byte) (INT_TWO >> 8);
		bytes[7] = (byte) (INT_TWO >> 16);
		bytes[8] = (byte) (INT_TWO >> 24);

		return bytes;
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			@Override
			public void configure() {
				from("nettyclient:tcp://localhost:" + TEST_PORT).to("mock:result");
			}
		};
	}
}
