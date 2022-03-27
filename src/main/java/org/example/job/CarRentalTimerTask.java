package org.example.job;

import org.example.service.ICarsRentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class CarRentalTimerTask {
    private Logger logger = Logger.getLogger("CarRentalTimerTask");

    private ScheduledExecutorService scheduledExecutorService;

    private Thread thread;

    @Autowired
    private ICarsRentalService carsRentalService;

    private static final int INITIAL_DELAY_TIME = 30;

    private static final int PERIOD = 5;

    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    @PostConstruct
    public void init() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                logger.log(Level.INFO, "Start to check if the date is overdue!");

                int result = carsRentalService.unlockCarRecords();

                logger.log(Level.INFO, "There are " + result + " records updated in DB!");
            }
        });
        scheduledExecutorService.scheduleAtFixedRate(thread, INITIAL_DELAY_TIME, PERIOD, TIME_UNIT);
    }

    public boolean shutdownSchedule() {
        boolean result;
        scheduledExecutorService.shutdown();
        try {
            result = scheduledExecutorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Shutdown schedule exception ,ex= " + e.getMessage());
            result = false;
        }
        return result;
    }

    public String healthCheck() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[schedule is terminated:");
        stringBuffer.append(scheduledExecutorService.isTerminated() + "]，");
        stringBuffer.append("[schedule is shutdown:");
        stringBuffer.append(scheduledExecutorService.isShutdown() + "]，");
        stringBuffer.append("[thread is alive:");
        stringBuffer.append(thread.isAlive() + "]，");
        stringBuffer.append("[thread is interrupted:");
        stringBuffer.append(thread.isInterrupted() + "]，");
        stringBuffer.append("[thread is daemon:");
        stringBuffer.append(thread.isDaemon() + "]，");
        return stringBuffer.toString();
    }
}
