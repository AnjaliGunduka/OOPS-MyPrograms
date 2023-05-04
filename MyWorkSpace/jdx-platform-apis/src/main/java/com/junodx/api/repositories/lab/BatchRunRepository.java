package com.junodx.api.repositories.lab;

import com.junodx.api.controllers.SortType;
import com.junodx.api.controllers.lab.types.BatchRunSortBy;
import com.junodx.api.models.core.types.IntervalType;
import com.junodx.api.models.laboratory.BatchRun;
import com.junodx.api.models.laboratory.BatchRunMetaProjection;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public interface BatchRunRepository extends JpaRepository<BatchRun, String> {
    Optional<BatchRun> findBatchRunBySequencingRunId(String id);
    Optional<BatchRun> findBatchRunByPipelineRunId(String id);
    Page<BatchRun> findBatchRunsByPipelineVersion(String version, Pageable page);
    Page<BatchRun> findBatchRunsByModelId(String id, Pageable page);

    @Query("select b from BatchRun b where b.id = :id")
    Optional<BatchRunMetaProjection> findBatchRunMetaById(@Param("id") String id);

    @Query("select b from BatchRun b where (:pipelineRunId is null or b.pipelineRunId = :pipelineRunId)" +
            " and (:sequencingRunId is null or b.sequencingRunId = :sequencingRunId)" +
            " and (:pipelineVersion is null or b.pipelineVersion = :pipelineVersion)" +
            " and (:modelId is null or b.modelId = :modelId)" +
            " and (:reviewed is null or b.reviewed = :reviewed)" +
            " and (:labId is null or b.laboratory.id = :labId)" +
            " and (cast(:after as timestamp) is null or b.endTime >= :after)" +
            " order by b.endTime ASC")
    Page<BatchRun> searchOrderByEndTimeASC(@Param("pipelineRunId") String pipelineRunId,
                                      @Param("sequencingRunId") String sequencingRunId,
                                      @Param("pipelineVersion") String pipelineVersion,
                                      @Param("modelId") String modelId,
                                      @Param("reviewed") Boolean reviewed,
                                      @Param("labId") String labId,
                                      @Param("after") Calendar after,
                                      Pageable pageable);

    @Query("select b from BatchRun b where (:pipelineRunId is null or b.pipelineRunId = :pipelineRunId)" +
            " and (:sequencingRunId is null or b.sequencingRunId = :sequencingRunId)" +
            " and (:pipelineVersion is null or b.pipelineVersion = :pipelineVersion)" +
            " and (:modelId is null or b.modelId = :modelId)" +
            " and (:reviewed is null or b.reviewed = :reviewed)" +
            " and (:labId is null or b.laboratory.id = :labId)" +
            " and (cast(:after as timestamp) is null or b.endTime >= :after)" +
            " order by b.endTime DESC")
    Page<BatchRun> searchOrderByEndTimeDSC(@Param("pipelineRunId") String pipelineRunId,
                                           @Param("sequencingRunId") String sequencingRunId,
                                           @Param("pipelineVersion") String pipelineVersion,
                                           @Param("modelId") String modelId,
                                           @Param("reviewed") Boolean reviewed,
                                           @Param("labId") String labId,
                                           @Param("after") Calendar after,
                                           Pageable pageable);

    @Query("select b from BatchRun b where (:pipelineRunId is null or b.pipelineRunId = :pipelineRunId)" +
            " and (:sequencingRunId is null or b.sequencingRunId = :sequencingRunId)" +
            " and (:pipelineVersion is null or b.pipelineVersion = :pipelineVersion)" +
            " and (:modelId is null or b.modelId = :modelId)" +
            " and (:reviewed is null or b.reviewed = :reviewed)" +
            " and (:labId is null or b.laboratory.id = :labId)" +
            " and (cast(:after as timestamp) is null or b.endTime >= :after)" +
            " order by b.endTime ASC")
    Page<BatchRun> searchOrderByStartTimeASC(@Param("pipelineRunId") String pipelineRunId,
                          @Param("sequencingRunId") String sequencingRunId,
                          @Param("pipelineVersion") String pipelineVersion,
                          @Param("modelId") String modelId,
                          @Param("reviewed") Boolean reviewed,
                          @Param("labId") String labId,
                          @Param("after") Calendar after,
                          Pageable pageable);

    @Query("select b from BatchRun b where (:pipelineRunId is null or b.pipelineRunId = :pipelineRunId)" +
            " and (:sequencingRunId is null or b.sequencingRunId = :sequencingRunId)" +
            " and (:pipelineVersion is null or b.pipelineVersion = :pipelineVersion)" +
            " and (:modelId is null or b.modelId = :modelId)" +
            " and (:reviewed is null or b.reviewed = :reviewed)" +
            " and (:labId is null or b.laboratory.id = :labId)" +
            " and (cast(:after as timestamp) is null or b.endTime >= :after)" +
            " order by b.endTime DESC")
    Page<BatchRun> searchOrderByStartTimeDSC(@Param("pipelineRunId") String pipelineRunId,
                                             @Param("sequencingRunId") String sequencingRunId,
                                             @Param("pipelineVersion") String pipelineVersion,
                                             @Param("modelId") String modelId,
                                             @Param("reviewed") Boolean reviewed,
                                             @Param("labId") String labId,
                                             @Param("after") Calendar after,
                                             Pageable pageable);

    @Query("select b from BatchRun b where (:pipelineRunId is null or b.pipelineRunId = :pipelineRunId)" +
            " and (:sequencingRunId is null or b.sequencingRunId = :sequencingRunId)" +
            " and (:pipelineVersion is null or b.pipelineVersion = :pipelineVersion)" +
            " and (:modelId is null or b.modelId = :modelId)" +
            " and (:reviewed is null or b.reviewed = :reviewed)" +
            " and (:labId is null or b.laboratory.id = :labId)" +
            " and (cast(:after as timestamp) is null or b.endTime >= :after)" +
            " order by b.reviewedAt ASC")
    Page<BatchRun> searchOrderByReviewedAtASC(@Param("pipelineRunId") String pipelineRunId,
                                             @Param("sequencingRunId") String sequencingRunId,
                                             @Param("pipelineVersion") String pipelineVersion,
                                             @Param("modelId") String modelId,
                                             @Param("reviewed") Boolean reviewed,
                                             @Param("labId") String labId,
                                             @Param("after") Calendar after,
                                             Pageable pageable);

    @Query("select b from BatchRun b where (:pipelineRunId is null or b.pipelineRunId = :pipelineRunId)" +
            " and (:sequencingRunId is null or b.sequencingRunId = :sequencingRunId)" +
            " and (:pipelineVersion is null or b.pipelineVersion = :pipelineVersion)" +
            " and (:modelId is null or b.modelId = :modelId)" +
            " and (:reviewed is null or b.reviewed = :reviewed)" +
            " and (:labId is null or b.laboratory.id = :labId)" +
            " and (cast(:after as timestamp) is null or b.endTime >= :after)" +
            " order by b.endTime DESC")
    Page<BatchRun> searchOrderByReviewedAtDESC(@Param("pipelineRunId") String pipelineRunId,
                                             @Param("sequencingRunId") String sequencingRunId,
                                             @Param("pipelineVersion") String pipelineVersion,
                                             @Param("modelId") String modelId,
                                             @Param("reviewed") Boolean reviewed,
                                             @Param("labId") String labId,
                                             @Param("after") Calendar after,
                                             Pageable pageable);

    List<BatchRun> findBatchRunsByIdIn(List<String> ids);



}
