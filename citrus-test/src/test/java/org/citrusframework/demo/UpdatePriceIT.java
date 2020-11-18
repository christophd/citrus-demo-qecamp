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

import java.math.BigDecimal;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.http.server.HttpServer;
import com.consol.citrus.junit.JUnit4CitrusSupport;
import org.citrusframework.demo.behavior.AddFruitBehavior;
import org.citrusframework.demo.fruits.Category;
import org.citrusframework.demo.fruits.Fruit;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.actions.CreateVariablesAction.Builder.createVariable;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

/**
 * @author Christoph Deppisch
 */
@ContextConfiguration(classes = EndpointConfig.class)
public class UpdatePriceIT extends JUnit4CitrusSupport {

    @Autowired
    private HttpClient fruitStoreClient;

    @Autowired
    private HttpServer marketPriceService;

    @Test
    @CitrusTest
    public void shouldUpdatePrice() {
        Fruit fruit = getTestFruit();

        given(createVariable("price", "0.99"));
        given(applyBehavior(new AddFruitBehavior(fruit, fruitStoreClient)));

        when(http().client(fruitStoreClient)
                .send()
                .get("/fruits/price/" + fruit.id)
                .fork(true)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        then(http().server(marketPriceService)
                .receive()
                .get("/prices/fruits/" + fruit.name.toLowerCase()));

        then(http().server(marketPriceService)
                .send()
                .response(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .payload("{ \"name\": \"" + fruit.name.toLowerCase() + "\", \"value\": ${price} }"));

        then(http().client(fruitStoreClient)
                .receive()
                .response(HttpStatus.OK));
    }

    private Fruit getTestFruit() {
        Fruit fruit = new Fruit();
        fruit.name = "Peach";
        fruit.category = new Category(1L, "pomme");
        fruit.status = Fruit.Status.PENDING;
        fruit.price = BigDecimal.valueOf(0.00D);
        fruit.tags = new String[] {"summer"};
        return fruit;
    }

}
