package com.ijsp.schedule;

import com.ijsp.aop.LogExecutionTime;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ijsp
 * @since
 */
@Log4j2
@NoArgsConstructor
@Service
public class StatsTask {


    //    Cron Expression
//      0 0 * * * * top of every hour of every day
//      */10 * * * * * every ten seconds
//      0 0 8-10 * * * 8, 9 and 10 o’clock of every day
//      0 0 6,19 * * * 6:00 AM and 7:00 PM every day
//      0 0/30 8-10 * * * 8:00, 8:30, 9:00, 9:30, 10:00 and 10:30 every day
//      0 0 9-17 * * MON-FRI on the hour nine-to-five weekdays
//      0 0 0 25 12 ? every Christmas Day at midnight

    @Scheduled(cron ="0 0 * * * *")
    @Transactional
    @LogExecutionTime
    public void task() throws InterruptedException {
        log.info("Fixed rate task async");
    }


}
