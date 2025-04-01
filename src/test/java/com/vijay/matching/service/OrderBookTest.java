package com.vijay.matching.service;

import com.vijay.matching.model.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class OrderBookTest {

    @Mock
    private ExecutionsPublisher executionsPublisher;

    @InjectMocks
    private OrderBook orderBook;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        orderBook = new OrderBook("EURUSD", executionsPublisher);
    }

    @Test
        //1,EURUSD,USD,S,10000,20250130,UserA - order added as-is
        //2,EURUSD,USD,B,5000,20250130,UserA
        //expected: 1,EURUSD,USD,S,5000,20250130,UserA
        //3,EURUSD,USD,B,5000,20250130,UserB - order added as-is
    void testScenarioA() {
        //Order1
        Order newOrder = new Order().id(1).symbol("EURUSD").dealt("USD").qty(10000).isBuy(false).valueDate("20250130").user("UserA");
        assertTrue(orderBook.addOrder(newOrder));
        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(executionsPublisher, times(1)).publish(captor.capture());
        Order aggregatedOrder = captor.getValue();
        Assertions.assertNotNull(aggregatedOrder);
        assertEquals(1, aggregatedOrder.id());
        assertEquals("UserA", aggregatedOrder.user());
        assertEquals("USD", aggregatedOrder.dealt());
        assertEquals("20250130", aggregatedOrder.valueDate());
        assertFalse(aggregatedOrder.isBuy());
        assertEquals(10000, aggregatedOrder.qty());

        //Order2
        newOrder = new Order().id(2).symbol("EURUSD").dealt("USD").qty(5000).isBuy(true).valueDate("20250130").user("UserA");
        assertTrue(orderBook.addOrder(newOrder));
        captor = ArgumentCaptor.forClass(Order.class);
        verify(executionsPublisher, times(2)).publish(captor.capture());
        aggregatedOrder = captor.getValue();
        Assertions.assertNotNull(aggregatedOrder);
        assertEquals(1, aggregatedOrder.id());
        assertEquals("UserA", aggregatedOrder.user());
        assertEquals("USD", aggregatedOrder.dealt());
        assertEquals("20250130", aggregatedOrder.valueDate());
        assertFalse(aggregatedOrder.isBuy());
        assertEquals(5000, aggregatedOrder.qty());

        //Order3
        newOrder = new Order().id(3).symbol("EURUSD").dealt("USD").qty(5000).isBuy(true).valueDate("20250130").user("UserB");
        assertTrue(orderBook.addOrder(newOrder));
        captor = ArgumentCaptor.forClass(Order.class);
        verify(executionsPublisher, times(3)).publish(captor.capture());
        aggregatedOrder = captor.getValue();
        Assertions.assertNotNull(aggregatedOrder);
        assertEquals(3, aggregatedOrder.id());
        assertEquals("UserB", aggregatedOrder.user());
        assertEquals("USD", aggregatedOrder.dealt());
        assertEquals("20250130", aggregatedOrder.valueDate());
        assertTrue(aggregatedOrder.isBuy());
        assertEquals(5000, aggregatedOrder.qty());
    }

    @Test
        //1,EURUSD,USD,S,1000,20250501,UserA
        //Expected: order added to book as-is
    void testSimpleSellAdd() {
        Order newOrder = new Order().id(1).symbol("EURUSD").dealt("USD").qty(1000).isBuy(false).valueDate("20250501").user("UserA");

        assertTrue(orderBook.addOrder(newOrder));

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(executionsPublisher, times(1)).publish(captor.capture());

        Order aggregatedOrder = captor.getValue();
        Assertions.assertNotNull(aggregatedOrder);
        assertEquals(1, aggregatedOrder.id());
        assertEquals("UserA", aggregatedOrder.user());
        assertEquals("USD", aggregatedOrder.dealt());
        assertEquals("20250501", aggregatedOrder.valueDate());
        assertFalse(aggregatedOrder.isBuy());
        assertEquals(1000, aggregatedOrder.qty());
    }

    @Test
        //1,EURUSD,USD,B,1000,20250501,UserA
        //2,EURUSD,USD,S,10000,20250501,UserB
        //Expected: orders added to book as-is
    void testSimpleBuyAdd_2users() {
        Order newOrder = new Order().id(1).symbol("EURUSD").dealt("USD").qty(1000).isBuy(true).valueDate("20250501").user("UserA");

        assertTrue(orderBook.addOrder(newOrder));

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(executionsPublisher, times(1)).publish(captor.capture());

        Order aggregatedOrder = captor.getValue();
        Assertions.assertNotNull(aggregatedOrder);
        assertEquals(1, aggregatedOrder.id());
        assertEquals("UserA", aggregatedOrder.user());
        assertEquals("USD", aggregatedOrder.dealt());
        assertEquals("20250501", aggregatedOrder.valueDate());
        assertTrue(aggregatedOrder.isBuy());
        assertEquals(1000, aggregatedOrder.qty());

        newOrder = new Order().id(2).symbol("EURUSD").dealt("USD").qty(1000).isBuy(true).valueDate("20250501").user("UserB");

        assertTrue(orderBook.addOrder(newOrder));

        captor = ArgumentCaptor.forClass(Order.class);
        verify(executionsPublisher, times(2)).publish(captor.capture());

        aggregatedOrder = captor.getValue();
        Assertions.assertNotNull(aggregatedOrder);
        assertEquals(2, aggregatedOrder.id());
        assertEquals("UserB", aggregatedOrder.user());
        assertEquals("USD", aggregatedOrder.dealt());
        assertEquals("20250501", aggregatedOrder.valueDate());
        assertTrue(aggregatedOrder.isBuy());
        assertEquals(1000, aggregatedOrder.qty());
    }

    @Test
        //1,EURUSD,USD,B,10000,20250501,UserA
        //2,EURUSD,USD,S,10000,20250501,UserA
        //Expected: 1,EURUSD,USD,B,0,20250501,UserA
    void testTwoOrdersCancelEachOther() {
        Order newOrder = new Order().id(1).symbol("EURUSD").dealt("USD").qty(10000).isBuy(true).valueDate("20250501").user("UserA");

        assertTrue(orderBook.addOrder(newOrder));

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(executionsPublisher, times(1)).publish(captor.capture());

        Order aggregatedOrder = captor.getValue();
        Assertions.assertNotNull(aggregatedOrder);
        assertEquals(1, aggregatedOrder.id());
        assertEquals("UserA", aggregatedOrder.user());
        assertEquals("USD", aggregatedOrder.dealt());
        assertEquals("20250501", aggregatedOrder.valueDate());
        assertTrue(aggregatedOrder.isBuy());
        assertEquals(10000, aggregatedOrder.qty());

        newOrder = new Order().id(2).symbol("EURUSD").dealt("USD").qty(10000).isBuy(false).valueDate("20250501").user("UserA");

        assertTrue(orderBook.addOrder(newOrder));

        captor = ArgumentCaptor.forClass(Order.class);
        verify(executionsPublisher, times(2)).publish(captor.capture());

        aggregatedOrder = captor.getValue();
        Assertions.assertNotNull(aggregatedOrder);
        assertEquals(1, aggregatedOrder.id());
        assertEquals("UserA", aggregatedOrder.user());
        assertEquals("USD", aggregatedOrder.dealt());
        assertEquals("20250501", aggregatedOrder.valueDate());
        assertTrue(aggregatedOrder.isBuy());
        assertEquals(0, aggregatedOrder.qty());
    }

    @Test
        //1,EURUSD,USD,B,10000,20250501,UserA - oldOrder
        //2,EURUSD,USD,S,5000,20250501,UserA - newOrder
        //Expected: 1,EURUSD,USD,B,5000,20250501,UserA
    void testOnlyNewOrderIsReducedToZero() {
        Order newOrder = new Order().id(1).symbol("EURUSD").dealt("USD").qty(10000).isBuy(true).valueDate("20250501").user("UserA");

        assertTrue(orderBook.addOrder(newOrder));

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(executionsPublisher, times(1)).publish(captor.capture());

        Order aggregatedOrder = captor.getValue();
        Assertions.assertNotNull(aggregatedOrder);
        assertEquals(1, aggregatedOrder.id());
        assertEquals("UserA", aggregatedOrder.user());
        assertEquals("USD", aggregatedOrder.dealt());
        assertEquals("20250501", aggregatedOrder.valueDate());
        assertTrue(aggregatedOrder.isBuy());
        assertEquals(10000, aggregatedOrder.qty());

        newOrder = new Order().id(2).symbol("EURUSD").dealt("USD").qty(5000).isBuy(false).valueDate("20250501").user("UserA");

        assertTrue(orderBook.addOrder(newOrder));

        captor = ArgumentCaptor.forClass(Order.class);
        verify(executionsPublisher, times(2)).publish(captor.capture());

        aggregatedOrder = captor.getValue();
        Assertions.assertNotNull(aggregatedOrder);
        assertEquals(1, aggregatedOrder.id());
        assertEquals("UserA", aggregatedOrder.user());
        assertEquals("USD", aggregatedOrder.dealt());
        assertEquals("20250501", aggregatedOrder.valueDate());
        assertTrue(aggregatedOrder.isBuy());
        assertEquals(5000, aggregatedOrder.qty());
    }

    @Test
        //1,EURUSD,USD,B,5000,20250501,UserA - oldOrder
        //2,EURUSD,USD,S,10000,20250501,UserA - newOrder
        //Expected: 2,EURUSD,USD,S,5000,20250501,UserA
    void testNewOrderReducesOldToZero() {
        Order newOrder = new Order().id(1).symbol("EURUSD").dealt("USD").qty(5000).isBuy(true).valueDate("20250501").user("UserA");

        assertTrue(orderBook.addOrder(newOrder));

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(executionsPublisher, times(1)).publish(captor.capture());

        Order aggregatedOrder = captor.getValue();
        Assertions.assertNotNull(aggregatedOrder);
        assertEquals(1, aggregatedOrder.id());
        assertEquals("UserA", aggregatedOrder.user());
        assertEquals("USD", aggregatedOrder.dealt());
        assertEquals("20250501", aggregatedOrder.valueDate());
        assertTrue(aggregatedOrder.isBuy());
        assertEquals(5000, aggregatedOrder.qty());

        newOrder = new Order().id(2).symbol("EURUSD").dealt("USD").qty(10000).isBuy(false).valueDate("20250501").user("UserA");

        assertTrue(orderBook.addOrder(newOrder));

        captor = ArgumentCaptor.forClass(Order.class);
        verify(executionsPublisher, times(2)).publish(captor.capture());

        aggregatedOrder = captor.getValue();
        Assertions.assertNotNull(aggregatedOrder);
        assertEquals(2, aggregatedOrder.id());
        assertEquals("UserA", aggregatedOrder.user());
        assertEquals("USD", aggregatedOrder.dealt());
        assertEquals("20250501", aggregatedOrder.valueDate());
        assertFalse(aggregatedOrder.isBuy());
        assertEquals(5000, aggregatedOrder.qty());
    }

}
