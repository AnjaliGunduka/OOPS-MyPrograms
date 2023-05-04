package com.junodx.api.services.lab;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.junodx.api.models.commerce.DataBuilderCheckOut;
import com.junodx.api.models.commerce.DataBuilderOrder;
import com.junodx.api.models.laboratory.Kit;
import com.junodx.api.models.laboratory.LaboratoryOrder;
import com.junodx.api.models.laboratory.TestReport;
import com.junodx.api.models.laboratory.types.TestRunType;
import com.junodx.api.repositories.lab.LaboratoryOrderRepository;
import com.junodx.api.services.commerce.OrderLineItemService;
import com.junodx.api.services.commerce.OrderService;
import com.junodx.api.services.exceptions.JdxServiceException;

@ExtendWith(MockitoExtension.class)
public class LaboratoryOrderServiceTest {

	@Mock
	private LaboratoryOrderRepository laboratoryOrderRepository;

	@Mock
	private OrderLineItemService orderLineItemService;

	@Mock
	private OrderService orderService;

	@Mock
	private TestRunService testRunService;

	@InjectMocks
	private LaboratoryOrderService laboratoryOrderService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testgetLaboratoryOrder() {
		when(laboratoryOrderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratoryOrder()));
		when(orderLineItemService.getOrderLineItemByLaboratoryOrderId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrderLineItem()));
		when(orderService.findByLineItem(Mockito.any())).thenReturn(Optional.of(DataBuilderOrder.mockOrder()));
		assertEquals(laboratoryOrderService.getLaboratoryOrder("1L").getClass(),
				Optional.of(DataBuilderOrder.mockLaboratoryOrder()).getClass());
	}

	@Test
	public void testgetLaboratoryOrderEmpty() throws JdxServiceException {
		when(laboratoryOrderRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		assertEquals(laboratoryOrderService.getLaboratoryOrder("1L").getClass(),
				Optional.of(DataBuilderOrder.mockLaboratoryOrder()).getClass());
	}

	@Test
	void testgetLaboratoryOrderException() {
		when(laboratoryOrderRepository.findById(Mockito.anyString())).thenThrow(JdxServiceException.class);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> laboratoryOrderService.getLaboratoryOrder("1L"));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}
 
	@Test
	void testfindLaboratoryOrderByTestRun() {
		when(laboratoryOrderRepository.findLaboratoryOrderByTestRuns(Mockito.any()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratoryOrder()));
		when(orderLineItemService.getOrderLineItemByLaboratoryOrderId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrderLineItem()));
		when(orderService.findByLineItem(Mockito.any())).thenReturn(Optional.of(DataBuilderOrder.mockOrder()));
		assertEquals(laboratoryOrderService.findLaboratoryOrderByTestRun(DataBuilderOrder.mockTestRun()).getClass(),
				Optional.of(DataBuilderOrder.mockLaboratoryOrder()).getClass());
	}

	@Test
	void testfindLaboratoryOrderByTestRunEmpty() {
		when(laboratoryOrderRepository.findLaboratoryOrderByTestRuns(Mockito.any())).thenReturn(Optional.empty());
		assertEquals(laboratoryOrderService.findLaboratoryOrderByTestRun(DataBuilderOrder.mockTestRun()).getClass(),
				Optional.of(DataBuilderOrder.mockLaboratoryOrder()).getClass());
	}

	@Test
	public void testfindLaboratoryOrderByTestRunException() throws JdxServiceException {
		when(laboratoryOrderRepository.findLaboratoryOrderByTestRuns(Mockito.any()))
				.thenThrow(JdxServiceException.class);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> laboratoryOrderService.findLaboratoryOrderByTestRun(DataBuilderOrder.mockTestRun()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	void testfindLaboratoryOrderByTestRunId() {
		when(laboratoryOrderRepository.findLaboratoryOrderByTestRuns(Mockito.any()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratoryOrder()));
		when(testRunService.getTestRun(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockTestRun()));
		when(orderLineItemService.getOrderLineItemByLaboratoryOrderId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrderLineItem()));
		when(orderService.findByLineItem(Mockito.any())).thenReturn(Optional.of(DataBuilderOrder.mockOrder()));
		assertEquals(laboratoryOrderService.findLaboratoryOrderByTestRunId(DataBuilderOrder.mockTestRun().getId())
				.getClass(), Optional.of(DataBuilderOrder.mockLaboratoryOrder()).getClass());
	}

	@Test
	void testfindLaboratoryOrderByTestRunIdEmpty() {
		// when(laboratoryOrderRepository.findLaboratoryOrderByTestRuns(Mockito.any())).thenReturn(Optional.empty());
		assertEquals(laboratoryOrderService.findLaboratoryOrderByTestRunId(DataBuilderOrder.mockTestRun().getId())
				.getClass(), Optional.of(DataBuilderOrder.mockLaboratoryOrder()).getClass());
	}

	@Test
	public void testfindLaboratoryOrderByTestRunIdException() throws JdxServiceException {
		when(testRunService.getTestRun(Mockito.anyString())).thenThrow(JdxServiceException.class);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> laboratoryOrderService.findLaboratoryOrderByTestRunId(DataBuilderOrder.mockTestRun().getId()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	void testfindLaboratoryOrderByOrderLineItemId() {
		when(testRunService.getTestRun(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockTestRun()));
		when(laboratoryOrderRepository.findLaboratoryOrderByOrderLineItem_Id(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratoryOrder()));
		when(orderLineItemService.getOrderLineItemByLaboratoryOrderId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrderLineItem()));
		when(orderService.findByLineItem(Mockito.any())).thenReturn(Optional.of(DataBuilderOrder.mockOrder()));
		assertEquals(laboratoryOrderService.findLaboratoryOrderByOrderLineItemId("1L").getClass(),
				Optional.of(DataBuilderOrder.mockLaboratoryOrder()).getClass());
	}

	@Test
	void testfindLaboratoryOrderByOrderLineItemIdEmpty() {
		when(testRunService.getTestRun(Mockito.anyString())).thenReturn(Optional.empty());
		assertEquals(laboratoryOrderService.findLaboratoryOrderByOrderLineItemId("1L").getClass(),
				Optional.of(DataBuilderOrder.mockLaboratoryOrder()).getClass());
	}

	@Test
	public void testfindLaboratoryOrderByOrderLineItemIdException() throws JdxServiceException {
		when(testRunService.getTestRun(Mockito.anyString())).thenThrow(JdxServiceException.class);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> laboratoryOrderService.findLaboratoryOrderByOrderLineItemId("1L"));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	void testgetAllLabOrders() {
		List<LaboratoryOrder> lab = new ArrayList<>();
		lab.add(DataBuilderOrder.mockLaboratoryOrder());
		Pageable paging = PageRequest.of(0, 2);
		final Page<LaboratoryOrder> pages = new PageImpl<>(lab, paging, 5);
		when(laboratoryOrderRepository.findAll(paging)).thenReturn(pages);
		assertEquals(laboratoryOrderService.getAllLabOrders(paging).getClass(), pages.getClass());
	}

	@Test
	public void testgetAllLabOrderException() throws JdxServiceException {
		Pageable paging = PageRequest.of(0, 2);
		when(laboratoryOrderRepository.findAll(paging)).thenThrow(JdxServiceException.class);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> laboratoryOrderService.getAllLabOrders(paging));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	void testfindLabOrdersByIds() {
		List<LaboratoryOrder> lab = new ArrayList<>();
		lab.add(DataBuilderOrder.mockLaboratoryOrder());
		List<String> sampleNumbers = new ArrayList<>();
		when(laboratoryOrderRepository.findLaboratoryOrdersByIdIn(Mockito.anyList())).thenReturn(lab);
		assertEquals(laboratoryOrderService.findLabOrdersByIds(sampleNumbers).getClass(), lab.getClass());
	}

	@Test
	void testsaveLaboratoryOrder() {
		when(orderLineItemService.getOrderLineItemById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrderLineItem()));
		when(laboratoryOrderRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockLaboratoryOrders());
		assertEquals(laboratoryOrderService
				.saveLaboratoryOrder(DataBuilderOrder.mockLaboratoryOrders(), DataBuilderOrder.userDetailsImpl)
				.getClass(), DataBuilderOrder.mockLaboratoryOrders().getClass());
	}

	@Test
	public void testsaveLaboratoryOrderException() throws JdxServiceException {
		when(laboratoryOrderRepository.save(Mockito.any())).thenThrow(JdxServiceException.class);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> laboratoryOrderService
				.saveLaboratoryOrder(DataBuilderOrder.mockLaboratoryOrders(), DataBuilderOrder.userDetailsImpl));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	void testsaveLaboratoryOrderEmpty() {
		laboratoryOrderService.saveLaboratoryOrder(DataBuilderOrder.mockLaboratoryOrderEmpty(),
				DataBuilderOrder.userDetailsImpl);

	}

	@Test
	void testupdate() {
		when(laboratoryOrderRepository.getById(Mockito.anyString()))
				.thenReturn(DataBuilderOrder.mockLaboratoryOrders());
		when(laboratoryOrderRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockLaboratoryOrders());
		assertEquals(laboratoryOrderService
				.update(DataBuilderOrder.mockLaboratoryOrders(), DataBuilderOrder.userDetailsImpl).getClass(),
				DataBuilderOrder.mockLaboratoryOrders().getClass());
	}

	@Test
	void testupdateExceptions() {
		when(laboratoryOrderRepository.getById(Mockito.anyString())).thenReturn(null);
		assertThrows(JdxServiceException.class, () -> laboratoryOrderService
				.update(DataBuilderOrder.mockLaboratoryOrders(), DataBuilderOrder.userDetailsImpl));
	}

	@Test
	public void testupdateException() throws JdxServiceException {
		when(laboratoryOrderRepository.getById(Mockito.anyString()))
				.thenReturn(DataBuilderOrder.mockLaboratoryOrders());
		when(laboratoryOrderRepository.save(Mockito.any())).thenThrow(JdxServiceException.class);
		assertThrows(JdxServiceException.class, () -> laboratoryOrderService
				.update(DataBuilderOrder.mockLaboratoryOrders(), DataBuilderOrder.userDetailsImpl));

	}

	@Test
	void testupdateTestRunWithKit() {
		assertEquals(
				laboratoryOrderService.updateTestRunWithKit(DataBuilderCheckOut.mockLaboratoryOrder(),
						DataBuilderCheckOut.mockKitss(), TestRunType.STANDARD, true, false).getClass(),
				DataBuilderCheckOut.mockLaboratoryOrder().getClass());
	}

	@Test
	public void testupdateTestRunWithKitOrderException() throws JdxServiceException {

		assertThrows(JdxServiceException.class, () -> laboratoryOrderService.updateTestRunWithKit(null,
				DataBuilderOrder.mockKit(), TestRunType.STANDARD, false, false));

	}

	@Test
	public void testupdateTestRunWithKitException() throws JdxServiceException {
		assertThrows(JdxServiceException.class,
				() -> laboratoryOrderService.updateTestRunWithKit(DataBuilderOrder.mockLaboratoryOrders(), null,
						TestRunType.STANDARD, false, false));

	}

	@Test
	public void testupdateTestRunWithKitRetestException() throws JdxServiceException {
		assertThrows(JdxServiceException.class,
				() -> laboratoryOrderService.updateTestRunWithKit(DataBuilderOrder.mockLaboratoryOrder(),
						DataBuilderOrder.mockKitss(), TestRunType.STANDARD, true, true));
	}

	@Test
	void testremoveTestRun() {
		DataBuilderOrder.testRuns.add(DataBuilderOrder.mockTestRuns());
		when(laboratoryOrderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratoryOrder()));
		when(laboratoryOrderRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockLaboratoryOrders());
		verify(testRunService, times(0)).delete(DataBuilderOrder.mockTestRun());
		assertEquals(laboratoryOrderService.removeTestRun("76c97d43-347f-4132-ba18-ddf3b313c226", "1L").getClass(),
				DataBuilderOrder.mockLaboratoryOrders().getClass());
	}

	
	@Test
	public void testremoveTestRunEmptyLabException() throws JdxServiceException {
		when(laboratoryOrderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratoryOrder()));
	//	when(laboratoryOrderRepository.save(Mockito.any())).thenReturn(null);
		assertThrows(JdxServiceException.class,
				() -> laboratoryOrderService.removeTestRun("76c97d43-347f-4132-ba18-ddf3b313c226", null));

	}

	@Test
	public void testremoveTestRunEmptyException() throws JdxServiceException {
		when(laboratoryOrderRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		assertThrows(JdxServiceException.class,
				() -> laboratoryOrderService.removeTestRun("76c97d43-347f-4132-ba18-ddf3b313c226", "1L"));

	}

	@Test
	public void testremoveTestRunException() throws JdxServiceException {
		when(laboratoryOrderRepository.findById(Mockito.anyString())).thenThrow(JdxServiceException.class);
		assertThrows(JdxServiceException.class,
				() -> laboratoryOrderService.removeTestRun("76c97d43-347f-4132-ba18-ddf3b313c226", "1L"));

	}
}
