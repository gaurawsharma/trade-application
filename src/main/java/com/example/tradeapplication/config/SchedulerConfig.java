package com.example.tradeapplication.config;

import com.example.tradeapplication.model.TradeStore;
import com.example.tradeapplication.service.TradeStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@EnableScheduling
@Configuration
public class SchedulerConfig {

    @Autowired
    SimpMessagingTemplate template;

    @Autowired
    private TradeStoreService tradeStoreService;

    @Scheduled(cron = "${trade.expiry.scheduler}")
    public void expireTrade() {
        tradeStoreService.expireTrade();
    }

    @Scheduled(fixedDelay = 5000)
    public void sendAdhocMessages() {
        List<TradeStore> tradeStoreList = tradeStoreService.findAllTrades();
        if (!tradeStoreList.isEmpty()) {
            for (TradeStore trades : tradeStoreList) {
                template.convertAndSend("/topic/trades", trades);
            }
        }
    }

}
