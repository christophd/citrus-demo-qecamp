/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
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

package org.citrusframework.demo;

import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.http.client.HttpClientBuilder;
import com.consol.citrus.http.server.HttpServer;
import com.consol.citrus.http.server.HttpServerBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EndpointConfig {

    private static final int FRUIT_STORE_SERVICE_PORT = 8080;
    private static final int MARKET_SERVICE_PORT = 8081;

    @Bean
    public HttpClient fruitStoreClient() {
        return new HttpClientBuilder()
                .requestUrl(String.format("http://localhost:%s", FRUIT_STORE_SERVICE_PORT))
            .build();
    }

    @Bean
    public HttpServer marketPriceService() {
        return new HttpServerBuilder()
                .port(MARKET_SERVICE_PORT)
                .autoStart(true)
            .build();
    }

    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper();
    }
}
