package com.junodx.api.services.lab;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.mockito.junit.jupiter.MockitoExtension;

import com.junodx.api.models.commerce.DataBuilderOrder;
import com.junodx.api.models.laboratory.Kit;
import com.junodx.api.models.laboratory.Laboratory;
import com.junodx.api.repositories.lab.LaboratoryRepository;
import com.junodx.api.services.ServiceBase.ServiceResponse;

@ExtendWith(MockitoExtension.class)
public class LaboratoryServiceTest {
	@Mock
	private LaboratoryRepository laboratoryRepository;

	@InjectMocks
	private LaboratoryService laboratoryService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testgetLaboratory() {
		when(laboratoryRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		assertEquals(laboratoryService.getLaboratory("1L").getClass(),
				Optional.of(DataBuilderOrder.mockLaboratory()).getClass());
	}

	@Test
	void testgetAllLaboratories() {
		when(laboratoryRepository.findAll()).thenReturn(null);
		assertEquals(laboratoryService.getAllLaboratories().getClass(), ServiceResponse.class);
	}

	@Test
	void testgetAllLaboratoriesAsnullvalues() {
		assertEquals(laboratoryService.getAllLaboratories().getClass(), ServiceResponse.class);
	}

	@Test
	void testgetDefaultLaboratory() {
		when(laboratoryRepository.findLaboratoryByDefaultLaboratoryIsTrue())
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		assertEquals(laboratoryService.getDefaultLaboratory().getClass(),
				Optional.of(DataBuilderOrder.mockLaboratory()).getClass());
	}

	@Test
	void testgetAllLaboratoriesByIds() {
		List<Laboratory> labs = new ArrayList<>();
		labs.add(DataBuilderOrder.mockLaboratory());
		List<String> sampleNumbers = new ArrayList<>();
		when(laboratoryRepository.findAllByIdIn(Mockito.anyList())).thenReturn(labs);
		assertEquals(laboratoryService.getAllLaboratoriesByIds(sampleNumbers).getClass(), labs.getClass());
	}

	@Test
	void testsaveLaboratory() {
		when(laboratoryRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockLaboratory());
		assertEquals(laboratoryService
				.saveLaboratory(DataBuilderOrder.mockLaboratory(), DataBuilderOrder.userDetailsImpl).getClass(),
				DataBuilderOrder.mockLaboratory().getClass());
	}

	@Test
	void testsaveLaboratoryEmpty() {
		when(laboratoryRepository.save(Mockito.any())).thenReturn(null);
		laboratoryService.saveLaboratory(DataBuilderOrder.mockLaboratory(), DataBuilderOrder.userDetailsImpl);
	}

	@Test
	void testupdateLaboratory() {
		when(laboratoryRepository.getById(Mockito.anyString())).thenReturn(DataBuilderOrder.mockLaboratory());
		when(laboratoryRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockLaboratory());
		assertEquals(laboratoryService
				.updateLaboratory(DataBuilderOrder.mockLaboratory(), DataBuilderOrder.userDetailsImpl).getClass(),
				DataBuilderOrder.mockLaboratory().getClass());
	}

	@Test
	void testupdateLaboratoryEmpty() {
//		when(laboratoryRepository.getById(Mockito.anyString())).thenReturn(DataBuilderOrder.mockLaboratory());
//		when(laboratoryRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockLaboratory());
		laboratoryService.updateLaboratory(DataBuilderOrder.mockLaboratory(), DataBuilderOrder.userDetailsImpl);
	}

	@Test
	void testdeleteLaboratory() {
		when(laboratoryRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		verify(laboratoryRepository, times(0)).delete(DataBuilderOrder.mockLaboratory());
		assertEquals(laboratoryService
				.deleteLaboratory("f450237e-20a3-4bf1-b64d-9ecaab16be7a", DataBuilderOrder.userDetailsImpl).getClass(),
				ServiceResponse.class);
	}

	@Test
	void testdeleteLaboratoryEmpty() {
		when(laboratoryRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		// verify(laboratoryRepository,
		// times(0)).delete(DataBuilderOrder.mockLaboratory());
		assertEquals(laboratoryService
				.deleteLaboratory("f450237e-20a3-4bf1-b64d-9ecaab16be7a", DataBuilderOrder.userDetailsImpl).getClass(),
				ServiceResponse.class);
	}
}
