package com.junodx.api.services.fulfillment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.junodx.api.models.commerce.DataBuilderOrder;
import com.junodx.api.models.commerce.Product;
import com.junodx.api.models.fulfillment.FulfillmentProvider;
import com.junodx.api.repositories.fulfillment.FulfillmentProviderRepository;
import com.junodx.api.services.commerce.ProductService;
import com.junodx.api.services.exceptions.JdxServiceException;

@ExtendWith(MockitoExtension.class)
public class FulfillmentProviderServiceTest {

	@Mock
	private FulfillmentProviderRepository fulfillmentProviderRepository;
	@Mock
	private ProductService productService;

	@InjectMocks
	private FulfillmentProviderService fulfillmentProviderService;

	@Test
	void testgetProvider() {
		String[] includes = { "anjali", "gunduka" };
		when(fulfillmentProviderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockFulfillmentProvider()));
		assertEquals(fulfillmentProviderService.getProvider("1L", includes).getClass(),
				Optional.of(DataBuilderOrder.mockFulfillmentProvider()).getClass());
	}

	@Test
	void testgetAllProviders() {
		String[] includes = { "anjali", "gunduka" };
		List<FulfillmentProvider> providers = new ArrayList<>();
		providers.add(DataBuilderOrder.mockFulfillmentProvider());
		when(fulfillmentProviderRepository.findAll()).thenReturn(providers);
		assertEquals(fulfillmentProviderService.getAllProviders(includes), providers);
	}

	@Test
	void testgetDefaultProvider() {
		when(fulfillmentProviderRepository.findFulfillmentProviderByDefaultProviderIsTrue())
				.thenReturn(Optional.of(DataBuilderOrder.mockFulfillmentProvider()));
		assertEquals(fulfillmentProviderService.getDefaultProvider().getClass(),
				Optional.of(DataBuilderOrder.mockFulfillmentProvider()).getClass());
	}

	@Test
	void testgetAllProvidersByIds() {
		List<FulfillmentProvider> providers = new ArrayList<>();
		providers.add(DataBuilderOrder.mockFulfillmentProvider());
		List<String> examples = new ArrayList<>();
		when(fulfillmentProviderRepository.findAllByIdIn(examples)).thenReturn(providers);
		assertEquals(fulfillmentProviderService.getAllProvidersByIds(examples).getClass(), providers.getClass());
	}

	@Test
	void testfindProviderByName() {
		List<FulfillmentProvider> providers = new ArrayList<>();
		providers.add(DataBuilderOrder.mockFulfillmentProvider());
		when(fulfillmentProviderRepository.findFulfillmentProviderByName("examples"))
				.thenReturn(Optional.of(DataBuilderOrder.mockFulfillmentProvider()));
		assertEquals(fulfillmentProviderService.findProviderByName("examples").getClass(),
				Optional.of(DataBuilderOrder.mockFulfillmentProvider()).getClass());
	}

	@Test
	public void testfindProviderByNameException() throws JdxServiceException {
		when(fulfillmentProviderRepository.findFulfillmentProviderByName("examples"))
				.thenThrow(JdxServiceException.class);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> fulfillmentProviderService.findProviderByName("examples"));
	}

	@Test
	void testsaveProvider() {
		when(fulfillmentProviderRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockFulfillmentProvider());
		assertEquals(fulfillmentProviderService
				.saveProvider(DataBuilderOrder.mockFulfillmentProvider(), DataBuilderOrder.userDetailsImpl).getClass(),
				DataBuilderOrder.mockFulfillmentProvider().getClass());
	}

	@Test
	void testdeleteProvider() {
		List<FulfillmentProvider> providers = new ArrayList<>();
		providers.add(DataBuilderOrder.mockFulfillmentProvider());
		List<Product> attachedProducts = new ArrayList<>();
		attachedProducts.add(DataBuilderOrder.mockProduct());
		when(productService.getAllByFulfillmentProvider(Mockito.any())).thenReturn(attachedProducts);
		when(productService.update(Mockito.any())).thenReturn(DataBuilderOrder.mockProduct());
		verify(fulfillmentProviderRepository, times(0)).delete(Mockito.any());
		fulfillmentProviderService.deleteProvider(DataBuilderOrder.mockFulfillmentProvider(),
				DataBuilderOrder.userDetailsImpl);

	}

	@Test
	void testdeleteProviderById() {
		List<FulfillmentProvider> providers = new ArrayList<>();
		providers.add(DataBuilderOrder.mockFulfillmentProvider());
		List<Product> attachedProducts = new ArrayList<>();
		attachedProducts.add(DataBuilderOrder.mockProduct());
		when(fulfillmentProviderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockFulfillmentProvider()));
		when(productService.getAllByFulfillmentProvider(Mockito.any())).thenReturn(attachedProducts);
		when(productService.update(Mockito.any())).thenReturn(DataBuilderOrder.mockProduct());
		verify(fulfillmentProviderRepository, times(0)).delete(Mockito.any());
		fulfillmentProviderService.deleteProvider(DataBuilderOrder.mockFulfillmentProvider(),
				DataBuilderOrder.userDetailsImpl);
		fulfillmentProviderService.deleteProviderById("1L", DataBuilderOrder.userDetailsImpl);

	}

}
