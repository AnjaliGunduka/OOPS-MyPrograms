package com.junodx.api.services.lab;

import com.junodx.api.controllers.SortType;
import com.junodx.api.controllers.lab.KitController;
import com.junodx.api.controllers.lab.types.BatchRunSortBy;
import com.junodx.api.logging.LogCode;
import com.junodx.api.models.laboratory.BatchRun;
import com.junodx.api.models.laboratory.BatchRunMetaProjection;
import com.junodx.api.models.laboratory.Laboratory;
import com.junodx.api.models.laboratory.TestRun;
import com.junodx.api.models.laboratory.types.ReportConfiguration;
import com.junodx.api.models.laboratory.types.ReportConfigurationCounts;
import com.junodx.api.repositories.lab.BatchRunRepository;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.exceptions.JdxServiceException;
import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class BatchRunService extends ServiceBase {

    @Autowired
    private BatchRunRepository batchRunRepository;

    @Autowired
    private LaboratoryService laboratoryService;

    private static final Logger logger = LoggerFactory.getLogger(BatchRunService.class);

    public Optional<BatchRun> getBatchRun(String id) {
        Optional<BatchRun> batch = batchRunRepository.findById(id);
        if(batch.isEmpty())
            return batch;
        else {
            BatchRun run = batch.get();
            run = updateReportConfigurationCounts(run);
            return Optional.of(run);
        }
    }

    public Optional<BatchRunMetaProjection> getBatchRunMeta(String id) {
        Optional<BatchRunMetaProjection> batch = batchRunRepository.findBatchRunMetaById(id);
        if(batch.isEmpty())
            return batch;
        else {
            BatchRunMetaProjection run = batch.get();
           // run = updateReportConfigurationCounts(run);
            return Optional.of(run);
        }
    }

    public Optional<BatchRun> findBySequencingRunId(String id) {
        Optional<BatchRun> batch = batchRunRepository.findBatchRunBySequencingRunId(id);
        if(batch.isEmpty())
            return batch;
        else {
            BatchRun run = batch.get();
            run = updateReportConfigurationCounts(run);
            return Optional.of(run);
        }
    }

    public Optional<BatchRun> findByPipelineRunId(String id) {
        Optional<BatchRun> batch = batchRunRepository.findBatchRunByPipelineRunId(id);
        if(batch.isEmpty())
            return batch;
        else {
            BatchRun run = batch.get();
            run = updateReportConfigurationCounts(run);
            return Optional.of(run);
        }
    }

    public Page<BatchRun> getAllBatches(Pageable pageable) {
        Page<BatchRun> runs = batchRunRepository.findAll(pageable);
        Iterator<BatchRun> iter = runs.iterator();
        while(iter.hasNext()){
            BatchRun batch = iter.next();
            List<ReportConfigurationCounts> counts = new ArrayList<>();

            logger.info("Trying to update reportConfigs for batch " + batch.getId());

            batch = updateReportConfigurationCounts(batch);
        }

        return runs;
    }

    public Page<BatchRun> findByPipelineVersion(String version, Pageable pageable) {
        Page<BatchRun> runs = batchRunRepository.findBatchRunsByPipelineVersion(version, pageable);
        Iterator<BatchRun> iter = runs.iterator();
        while(iter.hasNext()){
            BatchRun batch = iter.next();
            List<ReportConfigurationCounts> counts = new ArrayList<>();

            logger.info("Trying to update reportConfigs for batch " + batch.getId());

            batch = updateReportConfigurationCounts(batch);
        }

        return runs;
    }

    public Page<BatchRun> findByModelId(String id, Pageable pageable) {
        Page<BatchRun> runs = batchRunRepository.findBatchRunsByModelId(id, pageable);
        Iterator<BatchRun> iter = runs.iterator();
        while(iter.hasNext()){
            BatchRun batch = iter.next();
            List<ReportConfigurationCounts> counts = new ArrayList<>();

            logger.info("Trying to update reportConfigs for batch " + batch.getId());

            batch = updateReportConfigurationCounts(batch);
        }

        return runs;
    }

    public Page<BatchRun> search(Optional<String> pipelineRunId,
                                 Optional<String> sequencingRunId,
                                 Optional<String> pipelineVersion,
                                 Optional<String> modelId,
                                 Optional<Boolean> reviewed,
                                 Optional<String> labId,
                                 Optional<Calendar> after,
                                 Optional<BatchRunSortBy> sortBy,
                                 Optional<SortType> sortType,
                                 Pageable pageable) {
        String p = null;
        String s = null;
        String v = null;
        String m = null;
        String i = null;
        Boolean r = null;
        Calendar c = null;
        BatchRunSortBy sort = null;
        SortType type = null;
        if(pipelineRunId.isPresent()) p = pipelineRunId.get();
        if(sequencingRunId.isPresent()) s = sequencingRunId.get();
        if(pipelineVersion.isPresent()) v = pipelineVersion.get();
        if(modelId.isPresent()) m = modelId.get();
        if(labId.isPresent()) i = labId.get();
        if(reviewed.isPresent()) r = reviewed.get();
        if(after.isPresent()) c = after.get();

        Page<BatchRun> runs = null;

        if(sortBy.isPresent() && sortType.isPresent()) {
            switch(sortBy.get()) {
                case reviewedAt:
                    if(sortType.get().equals(SortType.ASC))
                        runs = batchRunRepository.searchOrderByReviewedAtASC(p, s, v, m, r, i, c, pageable);
                    else
                        runs = batchRunRepository.searchOrderByReviewedAtDESC(p, s, v, m, r, i, c, pageable);
                case startTime:
                    if(sortType.get().equals(SortType.ASC))
                        runs = batchRunRepository.searchOrderByStartTimeASC(p, s, v, m, r, i, c, pageable);
                    else
                        runs = batchRunRepository.searchOrderByStartTimeDSC(p, s, v, m, r, i, c, pageable);
                default:
                    if(sortType.get().equals(SortType.ASC))
                        runs = batchRunRepository.searchOrderByEndTimeASC(p, s, v, m, r, i, c, pageable);
                    else
                        runs = batchRunRepository.searchOrderByEndTimeDSC(p, s, v, m, r, i, c, pageable);
            }
        } else
            runs = batchRunRepository.searchOrderByEndTimeASC(p, s, v, m, r, i, c, pageable);

        //Update all of the reportConfig counts across the batches in the returnable page
        Iterator<BatchRun> iter = runs.iterator();
        while(iter.hasNext()){
            BatchRun batch = iter.next();
            List<ReportConfigurationCounts> counts = new ArrayList<>();

            logger.info("Trying to update reportConfigs for batch " + batch.getId());

            batch = updateReportConfigurationCounts(batch);
        }

        return runs;
    }

    public BatchRun save(BatchRun batch, UserDetailsImpl user) throws JdxServiceException {
        if(batch != null) {
            //If we have an existing batchRun by pipelineRunId, then return it.
            if(batch.getPipelineRunId() != null) {
                Optional<BatchRun> foundBatchRun = batchRunRepository.findBatchRunByPipelineRunId(batch.getPipelineRunId());
                if (foundBatchRun.isPresent())
                    return foundBatchRun.get();
            }

            batch.setMeta(buildMeta(user));
            if (batch.getLaboratory() != null && batch.getLaboratory().getId() != null){
                Optional<Laboratory> laboratory = laboratoryService.getLaboratory(batch.getLaboratory().getId());
                if (laboratory.isPresent())
                    batch.setLaboratory(laboratory.get());
            }
            if(batch.getLaboratory() == null) {
                Optional<Laboratory> laboratory = laboratoryService.getDefaultLaboratory();
                if(laboratory.isPresent())
                    batch.setLaboratory(laboratory.get());
                else
                    throw new JdxServiceException("Cannot resolve laboratory associated with batch run data nor find a default laboratory");
            }

            log(LogCode.RESOURCE_CREATE, "Created a Batch " + batch.getId(), user);

            if(batch.getTestRuns() != null)
                for (TestRun t : batch.getTestRuns())
                   t.setBatch(batch);

            batch = updateReportConfigurationCounts(batch);


            batch = batchRunRepository.save(batch);
            if (batch != null)
                return batch;
        }

        return null;
    }

    public List<BatchRun> updateAll(List<BatchRun> batches, UserDetailsImpl user) throws JdxServiceException {
        try {
            if (batches != null) {
                List<BatchRun> runs = batchRunRepository.findBatchRunsByIdIn(batches.stream().map(x -> x.getId()).collect(Collectors.toList()));
                if (batches != null && batches.size() > 0) {
                    for (BatchRun batch : batches) {
                        Optional<BatchRun> run = runs.stream().filter(x -> x.getId().equals(batch.getId())).findFirst();
                        if (run.isPresent())
                            updateData(batch, run.get(), user);
                    }

                    return batchRunRepository.saveAll(runs);
                }
            }
        } catch(Exception e){
            throw new JdxServiceException("Cannot update batches " +  e.getMessage());
        }

        throw new JdxServiceException("Cannot update batches ");
    }

    public BatchRun update(BatchRun batch, UserDetailsImpl user) {
        if(batch != null) {
            Optional<BatchRun> run = batchRunRepository.findById(batch.getId());
            if(run.isPresent()) {
                updateData(batch, run.get(), user);

                batch = batchRunRepository.save(run.get());
                if (batch != null)
                    return batch;
            }
        }

        return null;
    }

    private void updateData(BatchRun a, BatchRun b, UserDetailsImpl user){
        b.setMeta(buildMeta(user));

        log(LogCode.RESOURCE_CREATE, "Created a Batch " + a.getId(), user);

        b.setReviewed(a.isReviewed());
        if(a.getReviewedAt() != null) b.setReviewedAt(a.getReviewedAt());
        if(a.getPipelineRunId() != null) b.setPipelineRunId(a.getPipelineRunId());
        if(a.getLimsPlateId() != null) b.setLimsPlateId(a.getLimsPlateId());
        if(a.getSequencingRunId() != null) b.setSequencingRunId(a.getSequencingRunId());
        if(a.getModelId() != null) b.setModelId(a.getModelId());
        if(a.getPipelineVersion() != null) b.setPipelineVersion(a.getPipelineVersion());
    }

    public void delete(String batchId, UserDetailsImpl updater) throws JdxServiceException{
        Optional<BatchRun> batch = batchRunRepository.findById(batchId);
        try {
            if (batch.isPresent()) {
                batchRunRepository.delete(batch.get());
            }
        } catch (Exception e) {
            throw new JdxServiceException("Cannot delete batch run " + batchId);
        }
    }

    protected BatchRun updateReportConfigurationCounts(BatchRun batch) throws JdxServiceException {
        if(batch.getTestRuns() != null) {
            List<ReportConfigurationCounts> counts = new ArrayList<>();
            for (TestRun t : batch.getTestRuns()) {
                if(t.getReport() != null && !t.getReport().isApproved()){
                    ReportConfiguration config = t.getReport().getReportConfiguration();
                    Optional<ReportConfigurationCounts> oCounts = counts.stream().filter(x -> x.getConfig() == config).findFirst();
                    if (oCounts.isPresent()) {
                        oCounts.get().setCount(oCounts.get().getCount() + 1);
                    } else {
                        ReportConfigurationCounts newCount = new ReportConfigurationCounts();
                        newCount.setConfig(config);
                        newCount.setCount(newCount.getCount() + 1);
                        counts.add(newCount);
                    }
                }
            }
            batch.setReportConfigurations(counts);
        }

        return batch;
    }

}
