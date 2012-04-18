package org.hbird.camel.nettyclient;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The NettyClient producer.
 */
public class NettyClientProducer extends DefaultProducer {
    private static final transient Logger LOG = LoggerFactory.getLogger(NettyClientProducer.class);
    private NettyClientEndpoint endpoint;

    public NettyClientProducer(NettyClientEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
        System.out.println(exchange.getIn().getBody());    
    }

}
