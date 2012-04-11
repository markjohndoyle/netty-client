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

import java.net.URI;
import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link NettyClientEndpoint}.
 */
public class NettyClientComponent extends DefaultComponent {
	private NettyClientConfiguration configuration;
    @Override
	protected Endpoint createEndpoint(final String uri, final String remaining, final Map<String, Object> parameters) throws Exception {

    	NettyClientConfiguration config;
        if (configuration != null) {
            config = configuration.copy();
        } else {
            config = new NettyClientConfiguration();
        }

        config.parseURI(new URI(remaining), parameters, this);

        Endpoint endpoint = new NettyClientEndpoint(uri, this, config);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}
