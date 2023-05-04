package com.junodx.api.services.lab;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.models.commerce.DataBuilderOrder;
import com.junodx.api.models.laboratory.Kit;
import com.junodx.api.repositories.lab.KitRepository;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.exceptions.JdxServiceException;

@ExtendWith(MockitoExtension.class)
public class KitServiceTest {
	@Mock
	private KitRepository kitRepository;
	@Mock
	private ObjectMapper mapper;

	@Spy
	@InjectMocks
	private KitService kitService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testgetKit() {
		when(kitRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		assertEquals(kitService.getKit("1L").getClass(), Optional.of(DataBuilderOrder.mockKit()).getClass());
	}

	@Test
	void testfindKitsBySampleNumber() {
		List<Kit> kit = new ArrayList<>();
		kit.add(DataBuilderOrder.mockKit());
		when(kitRepository.findKitsBySampleNumber(Mockito.anyString())).thenReturn(kit);
		assertEquals(kitService.findKitsBySampleNumber("1L").getClass(), kit.getClass());
	}

	@Test
	public void testfindKitsBySampleNumberException() throws JdxServiceException {
		when(kitRepository.findKitsBySampleNumber(Mockito.anyString())).thenThrow(JdxServiceException.class);
		assertThrows(JdxServiceException.class, () -> kitService.findKitsBySampleNumber("1L"));

	}

	@Test
	void testfindKitsBySampleNumbersInList() {
		List<Kit> kit = new ArrayList<>();
		kit.add(DataBuilderOrder.mockKit());
		List<String> sampleNumbers = new ArrayList<>();
		when(kitRepository.findKitsBySampleNumberIn(Mockito.anyList())).thenReturn(kit);
		assertEquals(kitService.findKitsBySampleNumbersInList(sampleNumbers).getClass(), kit.getClass());
	}

	@Test
	public void testfindKitsBySampleNumbersInListException() throws JdxServiceException {
		List<String> sampleNumbers = new ArrayList<>();
		when(kitRepository.findKitsBySampleNumberIn(Mockito.anyList())).thenThrow(JdxServiceException.class);
		assertThrows(JdxServiceException.class, () -> kitService.findKitsBySampleNumbersInList(sampleNumbers));

	}

	@Test
	void testfindKitsByKitCodeOrSampleNumberOrSleeveNumber() {
		List<Kit> kit = new ArrayList<>();
		kit.add(DataBuilderOrder.mockKit());
		when(kitRepository.findKitsByCodeOrSampleNumberOrPsdSleeveNumber(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(kit);
		assertEquals(kitService.findKitsByKitCodeOrSampleNumberOrSleeveNumber("JO135wwa33jj789", "123455", "9897987")
				.getClass(), kit.getClass());
	}

	@Test
	public void testfindKitsByKitCodeOrSampleNumberOrSleeveNumberException() throws JdxServiceException {
		when(kitRepository.findKitsByCodeOrSampleNumberOrPsdSleeveNumber(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString())).thenThrow(JdxServiceException.class);
		assertThrows(JdxServiceException.class,
				() -> kitService.findKitsByKitCodeOrSampleNumberOrSleeveNumber("JO135wwa33jj789", "123455", "9897987"));

	}

	@Test
	void testfindKitsByKitCodeOrSampleNumber() {
		List<Kit> kit = new ArrayList<>();
		kit.add(DataBuilderOrder.mockKit());
		when(kitRepository.findKitsByCodeOrSampleNumber(Mockito.anyString(), Mockito.anyString())).thenReturn(kit);
		assertEquals(kitService.findKitsByKitCodeOrSampleNumber("JO135wwa33jj789", "123455").getClass(),
				kit.getClass());
	}

	@Test
	public void testfindKitsByKitCodeOrSampleNumberException() throws JdxServiceException {
		when(kitRepository.findKitsByCodeOrSampleNumber(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(JdxServiceException.class);
		assertThrows(JdxServiceException.class,
				() -> kitService.findKitsByKitCodeOrSampleNumber("JO135wwa33jj789", "123455"));

	}

	@Test
	void testfindKitBySampleNumber() {
		when(kitRepository.findKitBySampleNumber(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		assertEquals(kitService.findKitBySampleNumber("123455").getClass(),
				(Optional.of(DataBuilderOrder.mockKit())).getClass());
	}

	@Test
	public void testfindKitBySampleNumberException() throws JdxServiceException {
		when(kitRepository.findKitBySampleNumber(Mockito.anyString())).thenThrow(JdxServiceException.class);
		assertThrows(JdxServiceException.class, () -> kitService.findKitBySampleNumber("123455"));

	}

	@Test
	void testgetAllKits() {
		List<Kit> kit = new ArrayList<>();
		kit.add(DataBuilderOrder.mockKit());
		Pageable paging = PageRequest.of(0, 2);
		final Page<Kit> pages = new PageImpl<>(kit, paging, 5);
		when(kitRepository.findAll(paging)).thenReturn(pages);
		assertEquals(kitService.getAllKits(paging).getClass(), pages.getClass());
	}

	@Test
	public void testgetAllKitsException() throws JdxServiceException {
		Pageable paging = PageRequest.of(0, 2);
		when(kitRepository.findAll(paging)).thenThrow(JdxServiceException.class);
		assertThrows(JdxServiceException.class, () -> kitService.getAllKits(paging));

	}

	@Test
	void testfindKitsByIds() {
		List<Kit> kit = new ArrayList<>();
		kit.add(DataBuilderOrder.mockKit());
		List<String> sampleNumbers = new ArrayList<>();
		when(kitRepository.findAllById(sampleNumbers)).thenReturn(kit);
		assertEquals(kitService.findKitsByIds(sampleNumbers).getClass(), kit.getClass());
	}

	@Test
	void testsaveKit() {
		when(kitRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockKit());
		assertEquals(kitService.saveKit(DataBuilderOrder.mockKit(), DataBuilderOrder.userDetailsImpl).getClass(),
				DataBuilderOrder.mockKit().getClass());
	}

	@Test
	public void testsaveKitUnusableException() throws JdxServiceException {
		assertThrows(JdxServiceException.class,
				() -> kitService.saveKit(DataBuilderOrder.mockKitEmpty(), DataBuilderOrder.userDetailsImpl));
	}

	@Test
	public void testsaveKitException() throws JdxServiceException {
		when(kitRepository.save(Mockito.any())).thenThrow(JdxServiceException.class);
		assertThrows(JdxServiceException.class,
				() -> kitService.saveKit(DataBuilderOrder.mockKit(), DataBuilderOrder.userDetailsImpl));

	}

	@Test
	void testupdateKit() {
		List<Kit> kit = new ArrayList<>();
		kit.add(DataBuilderOrder.mockKit());
		when(kitRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		when(kitRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockKit());
		assertEquals(kitService.updateKit(DataBuilderOrder.mockKit(), DataBuilderOrder.userDetailsImpl).getClass(),
				DataBuilderOrder.mockKit().getClass());
	}

	@Test
	void testupdateKitUpdate() {
		List<Kit> kit = new ArrayList<>();
		kit.add(DataBuilderOrder.mockKit());
		when(kitRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockKitSample()));
		when(kitRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockKit());
		assertEquals(kitService.updateKit(DataBuilderOrder.mockKit(), DataBuilderOrder.userDetailsImpl).getClass(),
				DataBuilderOrder.mockKit().getClass());
	}

	@Test
	void testupdateKitAnotherSample() {
		List<Kit> kit = new ArrayList<>();
		kit.add(DataBuilderOrder.mockKitSample());
		when(kitRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockKitPsdSleeveNumber()));
		when(kitRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockKitSample());
		assertEquals(
				kitService.updateKit(DataBuilderOrder.mockKitSample(), DataBuilderOrder.userDetailsImpl).getClass(),
				DataBuilderOrder.mockKit().getClass());
	}

	@Test
	void testupdateKitAnother() {
		List<Kit> kit = new ArrayList<>();
		kit.add(DataBuilderOrder.mockKit());
		when(kitRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockKitPsdSleeveNumber()));
		when(kitRepository.findKitsByCodeOrSampleNumber(Mockito.anyString(), Mockito.anyString())).thenReturn(kit);
		when(kitRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockKitPsdSleeveNumber());
		assertEquals(kitService.updateKit(DataBuilderOrder.mockKitPsdSleeveNumber(), DataBuilderOrder.userDetailsImpl)
				.getClass(), DataBuilderOrder.mockKit().getClass());
	}

	@Test
	void testupdateKitAnotherException() {
		List<Kit> kit = new ArrayList<>();
		kit.add(DataBuilderOrder.mockKitPsdSleeveNumber());
		kit.add(DataBuilderOrder.mockKitPsdSleeveNumber());
		kit.add(DataBuilderOrder.mockKitPsdSleeveNumber());
		when(kitRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockKitPsdSleeveNumber()));
		when(kitRepository.findKitsByCodeOrSampleNumber(Mockito.anyString(), Mockito.anyString())).thenReturn(kit);
		assertThrows(JdxServiceException.class, () -> kitService.updateKit(DataBuilderOrder.mockKitPsdSleeveNumber(),
				DataBuilderOrder.userDetailsImpl));
	}

	
	@Test
	void testupdateKitExceptions() {
		assertThrows(JdxServiceException.class, () -> kitService.updateKit(null, DataBuilderOrder.userDetailsImpl));
	}

	@Test
	public void testupdateKitException() throws JdxServiceException {
		when(kitRepository.findById(Mockito.anyString())).thenThrow(JdxServiceException.class);
		assertThrows(JdxServiceException.class,
				() -> kitService.updateKit(DataBuilderOrder.mockKit(), DataBuilderOrder.userDetailsImpl));

	}

	@Test
	public void testupdateKitSaveException() throws JdxServiceException {
		when(kitRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		when(kitRepository.save(Mockito.any())).thenThrow(JdxServiceException.class);
		assertThrows(JdxServiceException.class,
				() -> kitService.updateKit(DataBuilderOrder.mockKit(), DataBuilderOrder.userDetailsImpl));

	}

	@Test
	void testdeleteKit() {
		List<Kit> kit = new ArrayList<>();
		kit.add(DataBuilderOrder.mockKit());
		when(kitRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		verify(kitRepository, times(0)).delete(DataBuilderOrder.mockKit());
		kitService.deleteKit("1L", DataBuilderOrder.userDetailsImpl);

	}
	@Test
	void testdeleteKitAssaigned() {
		List<Kit> kit = new ArrayList<>();
		kit.add(DataBuilderOrder.mockKitEmpty());
		when(kitRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockKitEmpty()));
		verify(kitRepository, times(0)).delete(DataBuilderOrder.mockKitEmpty());
		assertThrows(JdxServiceException.class, () -> kitService.deleteKit("1L", DataBuilderOrder.userDetailsImpl));

	}
	
	@Test
	public void testdeleteKitException() throws JdxServiceException {
		when(kitRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		assertThrows(JdxServiceException.class, () -> kitService.deleteKit("1L", DataBuilderOrder.userDetailsImpl));

	}
}
