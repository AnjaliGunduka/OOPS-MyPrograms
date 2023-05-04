package com.junodx.api.services.commerce;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.junodx.api.models.commerce.DataBuilderOrder;
import com.junodx.api.models.commerce.OrderLineItem;
import com.junodx.api.models.providers.DataBuilder;
import com.junodx.api.repositories.commerce.OrderLineItemRepository;
import com.junodx.api.services.exceptions.JdxServiceException;

@ExtendWith(MockitoExtension.class)
public class OrderLineItemServiceTest {
	@Mock
	private OrderLineItemRepository orderLineItemRepository;
	@Mock
	private OrderService orderService;
	@InjectMocks
	private OrderLineItemService orderLineItemService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testgetOrderLineItemById() {
		when(orderLineItemRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrderLineItem()));
		assertEquals(orderLineItemService.getOrderLineItemById("1L").getClass(),
				Optional.of(DataBuilderOrder.mockOrderLineItem()).getClass());
	}

	@Test
	void testgetAllLineItemsForOrderId() {
		List<OrderLineItem> orderLineItemtems = new ArrayList<>();
		orderLineItemtems.add(DataBuilderOrder.mockOrderLineItem());
		when(orderLineItemRepository.findOrderLineItemsByOrderId(Mockito.anyString())).thenReturn(orderLineItemtems);
		assertEquals(orderLineItemService.getAllLineItemsForOrderId("1L").getClass(), orderLineItemtems.getClass());
	}

	@Test
	void testgetOrderLineItemByLaboratoryOrderId() {
		when(orderLineItemRepository.findOrderLineItemByLaboratoryOrderDetails_Id(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrderLineItem()));
		assertEquals(orderLineItemService.getOrderLineItemByLaboratoryOrderId("1L").getClass(),
				Optional.of(DataBuilderOrder.mockOrderLineItem()).getClass());
	}

	@Test
	void testgetOrderLineItemByLaboratoryOrderIdCatchException() {
		doThrow(new NoSuchElementException()).when(orderLineItemRepository)
				.findOrderLineItemByLaboratoryOrderDetails_Id(Mockito.anyString());
		Exception exception = assertThrows(JdxServiceException.class,
				() -> orderLineItemService.getOrderLineItemByLaboratoryOrderId("1L"));
		assertEquals("The system experienced an error processing the client's request.", exception.getMessage());
	}

	@Test
	void testsave() {
		DataBuilder.OrderlineItems.add(DataBuilderOrder.mockOrderLineItems());
		DataBuilderOrder.fulfillments.add(DataBuilderOrder.mockFulfillment());
		String[] includes = {};
		when(orderService.findOneByOrderId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8", includes,
				DataBuilder.getMockUserDetailsImpl())).thenReturn(Optional.of(DataBuilder.mockOrderlineitems()));
		when(orderLineItemRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockOrderLineItems());

		assertEquals(orderLineItemService
				.save(DataBuilderOrder.mockOrderLineItemsOrder(), DataBuilder.getMockUserDetailsImpl()).getClass(),
				DataBuilderOrder.mockOrderLineItemsOrder().getClass());
	}

	@Test
	void testSaveNull() {
		orderLineItemService.save(DataBuilderOrder.mockOrderLineItemOrderExcep(), DataBuilder.getMockUserDetailsImpl());

	}
 
	@Test 
	public void testsaveException() throws JdxServiceException {
		doThrow(new NoSuchElementException()).when(orderLineItemRepository).save(Mockito.any());
		Exception exception = assertThrows(JdxServiceException.class, () -> orderLineItemService
				.save(DataBuilderOrder.mockOrderLineItems(), DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", exception.getMessage());
	}
}
