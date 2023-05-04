package com.junodx.api.jobs;

import com.junodx.api.models.auth.User;
import com.junodx.api.models.auth.types.UserType;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.laboratory.Laboratory;
import com.junodx.api.models.laboratory.LaboratoryReviewStatistics;
import com.junodx.api.models.laboratory.LaboratoryStatistics;
import com.junodx.api.repositories.lab.TestReportRepository;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.lab.LaboratoryReviewStatisticsService;
import com.junodx.api.services.lab.LaboratoryService;
import com.junodx.api.services.lab.LaboratoryStatisticsService;
import com.junodx.api.services.lab.TestReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

@Service
@Transactional
//@Priority(1)
public class StatisticsUpdateJob { //implements ApplicationEventListener {

    private final static Logger log = LoggerFactory.getLogger(StatisticsUpdateJob.class);

    @Autowired
    LaboratoryStatisticsService laboratoryStatisticsService;

    @Autowired
    LaboratoryReviewStatisticsService laboratoryReviewStatisticsService;

    @Autowired
    TestReportService testReportService;

    LaboratoryService laboratoryService;

    TestReportRepository testReportRepository;

    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> mainFuture;
    private static final int WAIT = 60 * 1000;

    public StatisticsUpdateJob(TestReportRepository testReportRepository, LaboratoryStatisticsService laboratoryStatisticsService, LaboratoryService laboratoryService){
        this.testReportRepository = testReportRepository;
        this.laboratoryStatisticsService = laboratoryStatisticsService;
        this.laboratoryService = laboratoryService;
    }

    /*
    @Override
    public void onEvent(ApplicationEvent applicationEvent) {
        log.info("got event: {}", applicationEvent.getType());

        switch (applicationEvent.getType()) {
            case INITIALIZATION_FINISHED:
                if (log.isTraceEnabled()) {
                    log.trace("app init finished, statsUpdaterJobs will be initialized...");
                }
                startAutoUpdate();
                break;
            case DESTROY_FINISHED:
                if (log.isTraceEnabled()) {
                    log.trace("destroy finished, stop auto update...");
                }
                cancelAutoUpdate();
                break;
            case RELOAD_FINISHED:
                if (log.isTraceEnabled()) {
                    log.trace("app reloaded, statsUpdaterJob is now restarted");
                }
            default:
                break;
        }
    }

    @Override
    public RequestEventListener onRequest(RequestEvent requestEvent) {
        return null;
    }

     */

    public void startAutoUpdate() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        if (mainFuture != null) {
            mainFuture.cancel(true);
            mainFuture = null;
        }
        /*
        mainFuture = scheduler.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                try {
                    log.info("calling updateLaboratoryStats()...");
                    updateLaboratoryStats();
                } catch (Throwable t) {
                    if (!mainFuture.isCancelled()) {
                        log.warn("Failed to call updateOrdersFromShopify...", t);
                    }
                }
            }
        }, 1, WAIT, TimeUnit.MILLISECONDS);
        if (log.isDebugEnabled()) {
            log.debug("mainFuture is scheduled to call updateOrdersFromShopify every " + WAIT / 1000 + " seconds");
        }

         */
    }

    private void cancelAutoUpdate() {
        if (mainFuture != null) {
            mainFuture.cancel(true);
        }
        scheduler.shutdownNow();
    }

    private void updateLaboratoryStats(){
        LaboratoryStatistics laboratoryStatistics = new LaboratoryStatistics();
        LaboratoryReviewStatistics laboratoryReviewStatistics = new LaboratoryReviewStatistics();

        //List<LaboratoryStatistics> stats = laboratoryStatisticsService.getAllStatisticsForAllLaboratorties(null);

        Calendar twoWeeksAgo = Calendar.getInstance();
        twoWeeksAgo.add(Calendar.DATE, -14);


        Optional<Laboratory> lab = laboratoryService.getLaboratory("b7388524-c1b3-446e-8ad2-0d5cd706a0a7");
        if (lab.isPresent()) {

            Optional<LaboratoryStatistics> s = laboratoryStatisticsService.getLaboratoryStatisticsByLaboratory("b7388524-c1b3-446e-8ad2-0d5cd706a0a7", null);
            if(s.isPresent()) {
                s.get().setLaboratory(lab.get());
              //  laboratoryStatistics.setResultsAwaitingSignOutConfirmation(testReportRepository.countTestReportsRequiringConfirmation(s.get().getLaboratory().getId(), twoWeeksAgo));
              //  laboratoryStatistics.setRecentlySignedOut(testReportRepository.countRecentlySignedOut(s.get().getLaboratory().getId(), twoWeeksAgo));
              //  laboratoryStatistics.setResultsAwaitingInvestigation(testReportRepository.countTestReportsRequiringReview(s.get().getLaboratory().getId(), twoWeeksAgo));
              //  laboratoryStatistics.setRecentlyManuallySignedOut(testReportRepository.countRecentlySignedOutManual(s.get().getLaboratory().getId(), twoWeeksAgo));
              //  laboratoryStatistics.setRecentlyAutomaticallySignedOut(testReportRepository.countRecentlySignedOutAutomatic(s.get().getLaboratory().getId(), twoWeeksAgo));

                laboratoryStatistics.setMeta(updateMeta(laboratoryStatistics.getMeta(), null));
                laboratoryStatisticsService.updateLaboratoryStatistics(s.get().getLaboratory().getId(), s.get(), null);
            }
        }

        /*
        private int interval;
    private IntervalType intervalType;
    private int runsToBeApproved;
    private int resultsAwaitingSignOutConfirmation;
    private int resultsAwaitingInvestigation;
    private int recentlySignedOut;
    private int recentlyManuallySignedOut;
    private int recentlyAutomaticallySignedOut;
    private int upcomingResults;
         */



    }

    protected Meta updateMeta(Meta meta, UserDetailsImpl user){
        User u = new User();
        u.setUserType(UserType.SYSTEM);
        u.setUsername("juno_system_user");

        if(user == null)
            user = UserDetailsImpl.build(u);

        if(meta == null) {
            meta = new Meta();
            meta.setCreatedAt(Calendar.getInstance());
            meta.setCreatedBy(user.getEmail());
            meta.setCreatedById(user.getUserId());
        }

        meta.setLastModifiedAt(Calendar.getInstance());
        meta.setLastModifiedBy(user.getEmail());
        meta.setLastModifiedById(user.getUserId());

        return meta;
    }
}
