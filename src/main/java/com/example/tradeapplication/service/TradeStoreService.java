package com.example.tradeapplication.service;

import com.example.tradeapplication.exception.LowerVersionTradeException;
import com.example.tradeapplication.exception.MaturityDateValidationException;
import com.example.tradeapplication.model.TradeStore;
import com.example.tradeapplication.repository.TradeStoreRepository;
import com.example.tradeapplication.validation.TradeValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeStoreService {

    private static Logger logger = LoggerFactory.getLogger(TradeStoreService.class);

    @Autowired
    private TradeStoreRepository tradeStoreRepository;

    @Autowired
    private TradeValidation validation;

    public List<TradeStore> findAllTrades() {
        return tradeStoreRepository.findAll();
    }

    public String save(final TradeStore tradeStore) {
        if (validation.validateTradeMaturityDate(tradeStore.getMaturityDate())) {
            List<TradeStore> tradeVersion = tradeStoreRepository.findByVersion(tradeStore.getVersion());
            if (tradeVersion.isEmpty() && tradeStore.getVersion() == 0) {
                logger.error("Lower version received for trade : " + tradeStore.getVersion());
                throw new LowerVersionTradeException("Lower Trade Version Received.");
            } else if (!tradeVersion.isEmpty() && validation.validateTradeVersion(tradeStore, tradeVersion)) {
                updateTradeStore(tradeStore, tradeVersion);
                logger.info("Trade Updated Successfully with values : " + tradeStore.toString());
                return "Trade Updated Successfully.";
            } else {
                tradeStoreRepository.save(tradeStore);
                logger.info("New Trade saved successfully with values : " + tradeStore.toString());
                return "New Trade Saved Successfully.";
            }
        }
        logger.error("Maturity date is less than today's date : " + tradeStore.getMaturityDate());
        throw new MaturityDateValidationException("Trade not saved as failed maturity date validation.");
    }

    public String updateTradeStore(TradeStore tradeStore, List<TradeStore> tradeStoreList) {
        for (TradeStore existingTrade : tradeStoreList) {
            if (tradeStore.getId() == existingTrade.getId()) {
                existingTrade.setId(tradeStore.getId());
                existingTrade.setTradeId(tradeStore.getTradeId());
                existingTrade.setVersion(tradeStore.getVersion());
                existingTrade.setCounterPartyId(tradeStore.getCounterPartyId());
                existingTrade.setBookId(tradeStore.getBookId());
                existingTrade.setMaturityDate(tradeStore.getMaturityDate());
                existingTrade.setCreatedDate(tradeStore.getCreatedDate());
                existingTrade.setExpired(tradeStore.getExpired());
                tradeStoreRepository.save(existingTrade);
            } else {
                throw new RuntimeException();
            }
        }
        return "Trade Updated Successfully.";
    }

    public void expireTrade() {
        tradeStoreRepository.findAll().stream().forEach(t -> {
            if (!validation.validateTradeMaturityDate(t.getMaturityDate())) {
                t.setExpired("Y");
                tradeStoreRepository.save(t);
            }
        });
    }

}