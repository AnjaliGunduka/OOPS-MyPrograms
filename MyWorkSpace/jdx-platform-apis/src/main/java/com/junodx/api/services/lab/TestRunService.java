package com.junodx.api.services.lab;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.controllers.lab.payloads.TestRunRetestPayload;
import com.junodx.api.controllers.payloads.ReportConfigurationPayload;
import com.junodx.api.controllers.payloads.SampleIdListPayload;
import com.junodx.api.models.commerce.Order;
import com.junodx.api.models.commerce.OrderStatus;
import com.junodx.api.models.commerce.types.OrderStatusType;
import com.junodx.api.models.laboratory.*;
import com.junodx.api.models.laboratory.reports.Report;
import com.junodx.api.models.laboratory.types.LaboratoryStatusType;
import com.junodx.api.repositories.lab.TestRunRepository;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.exceptions.JdxServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TestRunService extends ServiceBase {

    @Autowired
    private TestRunRepository testRunRepository;

    @Autowired
    private LaboratoryOrderService laboratoryOrderService;

    @Autowired
    private BatchRunService batchRunService;


    @Autowired
    private KitService kitService;

    private ObjectMapper mapper;

    private static final Logger logger = LoggerFactory.getLogger(TestRunService.class);

    public TestRunService(){
        this.mapper = new ObjectMapper();
    }

    public Optional<TestRun> getTestRun(String id) {
        return testRunRepository.findById(id);
    }

    public List<TestRun> getAllTestRuns(String laboratoryOrderId) {
        return testRunRepository.findTestRunsByLaboratoryOrder_Id(laboratoryOrderId);
    }

    public Page<TestRun> getAllTestRuns(Pageable pageable){
        return testRunRepository.findAll(pageable);
    }

    public Optional<TestRun> getTestRunForKitId(String id){
        return testRunRepository.findTestRunByKit_Id(id);
    }

    public Optional<TestRun> findTestRunByLimsReportId(String id){
        return testRunRepository.findTestRunByLimsReportId(id);
    }

    public Optional<TestRun> getTestRunForKitAndBatchId(String id, String batchId){
        return testRunRepository.findTestRunByKit_IdAndBatch_Id(id, batchId);
    }

    public TestRun updateTestRunLaboratoryStatus(TestRun run, LaboratoryStatusType type) {
        LaboratoryStatus status = new LaboratoryStatus();
        if(run == null)
            return run;

        if(run.getStatus() == null || run.getStatus().size() == 0 || !run.getStatus().stream().filter(x->x.getStatus().equals(type)).findAny().isPresent()) {
            status.setStatus(type);
            status.setTestRun(run);
            status.setCurrent(true);
            status.setCreatedAt(Calendar.getInstance());

            run.addStatus(status);
        } else
            logger.info("Already have a status of type: " + type);

        return run;
    }

    //Can only save a test run if the parent LaboratoryOrderId is provided
    public TestRun saveTestRun(TestRun run, String labOrderId, UserDetailsImpl user) {
        try {
            logger.info("Trying to save test run with object " + mapper.writeValueAsString(run));
        } catch (Exception e ){ }

        //batch.setMeta(buildMeta(user));
        if(run != null) {
            if(testRunRepository.existsById(run.getId()))
                throw new JdxServiceException("Cannot create this test run, one already exists with this Id");
                Optional<LaboratoryOrder> labOrder = laboratoryOrderService.getLaboratoryOrder(labOrderId);
                if (labOrder.isPresent()) {
                    run.setLaboratoryOrder(labOrder.get());
                    if(run.getKit() != null && run.getKit().getId() != null) {
                        Optional<Kit> kit = kitService.getKit(run.getKit().getId());
                        if(kit.isPresent()){
                            if(kit.get().isAssigned())
                                throw new JdxServiceException("Cannot assign kit " + kit.get().getId() + " as it is already assigned");
                            if(kit.get().isUnusable())
                                throw new JdxServiceException("Cannot assign kit " + kit.get().getId() + " is set to unusable");
                            kit.get().setAssigned(true);
                            run.setKit(kit.get());
                        }
                        if(run.getStatus() != null) {
                            if(run.getStatus().size() == 1) {
                                if(run.getKit() == null
                                        && run.getStatus().get(0).getStatus() != LaboratoryStatusType.NO_KIT_ASSIGNED
                                        && run.getStatus().get(0).getStatus() != LaboratoryStatusType.REDRAW)
                                    throw new JdxServiceException("Cannot set this status without assigning a kit");
                                run.getStatus().get(0).setCurrent(true);
                                run.getStatus().get(0).setCreatedAt(Calendar.getInstance());
                                run.getStatus().get(0).setCreatedBy(user.getEmail());

                                return testRunRepository.save(run);
                            }
                            else if(run.getStatus().size() > 1)
                                throw new JdxServiceException("Cannot provide > 1 statuses ");
                        }
                    }
                }
                else
                    throw new JdxServiceException("Cannot associate Test Run to Lab Order");
        }
        else
            throw new JdxServiceException("Trying to update a null Test Run");

        return null;
    }

    public TestRun createRetestRequest(TestRunRetestPayload payload, UserDetailsImpl updater) throws JdxServiceException {
        if(payload == null)
            throw new JdxServiceException("Cannot create a retest request as the request payload is not present");

        if(payload.getCurrentTestRunId() == null)
            throw new JdxServiceException("Cannot find current test run as the test run id is not present");

        Optional<TestRun> testRun = testRunRepository.findById(payload.getCurrentTestRunId());
        if(testRun.isEmpty())
            throw new JdxServiceException("Cannot find test run in the system for id: " + payload.getCurrentTestRunId());

        testRun.get().setRetest(true);
        LaboratoryStatus status = new LaboratoryStatus();
        status.setStatus(LaboratoryStatusType.RETEST);
        status.setCreatedAt(Calendar.getInstance());
        status.setCurrent(true);
        status.setCreatedBy(updater.getEmail());
        status.setTestRun(testRun.get());
        testRun.get().addStatus(status);

        testRun.get().setCompleted(true);

        //TestRun newRun = new TestRun();
        return testRunRepository.save(testRun.get());
    }

    public TestRun update(TestRun run, UserDetailsImpl user){
        if(run == null)
            throw new JdxServiceException("Cannot update test run as the update object is missing");

        try {
            Optional<TestRun> runO = testRunRepository.findById(run.getId());
            if (runO.isPresent()) {
                TestRun r = runO.get();
                //If there isn't a kit assigned to this Test Run, and one is provided, then assign it to this test run
                //Only if the kit isn't already assigned
                if (run.getKit() != null) {
                    if (r.getKit() == null) {
                        if (run.getKit().isAssigned())
                            throw new JdxServiceException("Kit is already assigned, cannot assign it to this Test Run");
                        run.getKit().setAssigned(true);
                        r.setKit(run.getKit());
                    } else if (!r.getKit().getId().equals(run.getKit().getId()))
                        throw new JdxServiceException("Cannot change the kit assignment to this Test run, you must create a new test run");
                }
                if (run.getStartTime() != null) r.setStartTime(run.getStartTime());
                if (run.getEndTime() != null) r.setEndTime(run.getEndTime());
                if (run.getBatch() != null && run.getBatch().getId() != null) {
                    Optional<BatchRun> batch = batchRunService.getBatchRun(run.getBatch().getId());
                    if (batch.isPresent())
                        r.setBatch(batch.get());
                }
                if (run.getStatus() != null && run.getStatus().size() > 1)
                    throw new JdxServiceException("Cannot provide > 1 status updates at one time");
                else if (run.getStatus().size() == 1) {
                    if (r.getKit() == null
                            && run.getStatus().get(0).getStatus() != LaboratoryStatusType.NO_KIT_ASSIGNED
                            && run.getStatus().get(0).getStatus() != LaboratoryStatusType.REDRAW)
                        throw new JdxServiceException("Cannot set this status without assigning a kit");
                    //If a new status is present, add this one and set it to current and all others to not current
                    if (!r.getStatus().stream().anyMatch(x -> x.getStatus().equals(run.getStatus().get(0).getStatus()))) {
                        run.getStatus().get(0).setCurrent(true);
                        run.getStatus().get(0).setCreatedAt(Calendar.getInstance());
                        run.getStatus().get(0).setCreatedBy(user.getEmail());
                        for (LaboratoryStatus l : r.getStatus())
                            l.setCurrent(false);
                        r.getStatus().add(run.getStatus().get(0));
                    }
                }

                logger.info("About to update this batch run " + mapper.writeValueAsString(r));
                return testRunRepository.save(r);
            }
        }
        catch(Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot update test run");
        }

        throw new JdxServiceException("Cannot update test run");
    }

    public void delete(TestRun run) throws JdxServiceException {
        testRunRepository.delete(run);
    }

    public TestRun overrideCompletedState(String testRunId) throws JdxServiceException {
        try {
            Optional<TestRun> testRun = testRunRepository.findById(testRunId);
            if(testRun.isPresent()){
                testRun.get().setCompleted(true);
                return testRunRepository.save(testRun.get());
            }
        } catch (Exception e ){
            throw new JdxServiceException("Cannot set testRun to completed as an override: " + e.getMessage());
        }

        throw new JdxServiceException("Cannot set testRun to completed as an override: ");
    }

    public List<ReportConfigurationPayload> getReportConfigurationsForSampleIds(List<String> sampleIds) throws JdxServiceException {
        try {
            for(String id : sampleIds)
                logger.info("Ids to look up are : " + id);

            List<Kit> kits = kitService.findKitsBySampleNumbersInList(sampleIds);
            for(Kit k : kits)
                logger.info("Kit: " + k.getId());

            List<TestRun> testRuns = testRunRepository.findTestRunsByKitIn(kits);
            for(TestRun r : testRuns)
                logger.info("run: " + r.getId());

            List<ReportConfigurationPayload> response = new ArrayList<>();

            for (TestRun run : testRuns) {
                ReportConfigurationPayload payload = new ReportConfigurationPayload();
                if (run.getReportConfiguration() != null)
                    payload.setReportConfiguration(run.getReportConfiguration());
                if (run.getId() != null)
                    payload.setTestRunId(run.getId());
                if (run.getKit() != null && run.getKit().getId() != null) {
                    payload.setKitId(run.getKit().getId());
                    payload.setSampleId(run.getKit().getSampleNumber());
                }

                response.add(payload);
            }

            return response;
        } catch (Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot obtain report configurations " + e.getMessage());
        }
    }

    public List<TestRun> getTestRunsForLabOrder(String sampleId) throws JdxServiceException {
        try {
            Optional<Kit> kit = kitService.findKitBySampleNumber(sampleId);
            if (kit.isEmpty())
                throw new JdxServiceException("Cannot find kit associated to this sample");

            Optional<TestRun> testRun = testRunRepository.findTestRunByKit_Id(kit.get().getId());
            Optional<LaboratoryOrder> labOrder = null;

            if (testRun.isEmpty())
                throw new JdxServiceException("Cannot find test run associated with this sample");

            labOrder = laboratoryOrderService.findLaboratoryOrderByTestRun(testRun.get());

            if (labOrder.isEmpty())
                throw new JdxServiceException("Cannot find laboratory order for this sample so cannot find any other test runs");

            return labOrder.get().getTestRuns();
        } catch (Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot find associated orders");
        }
    }
}
