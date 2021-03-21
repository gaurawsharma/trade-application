package com.example.tradeapplication;

import com.example.tradeapplication.exception.LowerVersionTradeException;
import com.example.tradeapplication.exception.MaturityDateValidationException;
import com.example.tradeapplication.model.TradeStore;
import com.example.tradeapplication.resource.TradeStoreController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class TradeApplicationTests {

    @Autowired
    private TradeStoreController tradeStoreController;

    @Test
    void contextLoads() {
    }

    @Test
    void whenTradeValid_thenSaveToTradeStore() throws RuntimeException {
        ResponseEntity responseEntity = tradeStoreController.tradeStore(storeTrade(5, "T1", 5, LocalDate.now()));
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(), responseEntity);
        List<TradeStore> tradeList = tradeStoreController.allTrades();
        Assertions.assertEquals(5, tradeList.get(4).getId());
        Assertions.assertEquals("T1", tradeList.get(4).getTradeId());
        Assertions.assertEquals(5, tradeList.get(4).getVersion());
        Assertions.assertEquals(LocalDate.now(), tradeList.get(4).getMaturityDate());
        Assertions.assertEquals(LocalDate.now(), tradeList.get(4).getCreatedDate());
    }

    @Test
    void whenTradeWithExistingVersion_thenOverrideExistingTrade() throws RuntimeException {
        ResponseEntity responseEntity = tradeStoreController.tradeStore(storeTrade(2, "T1", 2, LocalDate.now()));
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(), responseEntity);
        List<TradeStore> tradeList = tradeStoreController.allTrades();
        Assertions.assertEquals("T1", tradeList.get(1).getTradeId());
        Assertions.assertEquals(2, tradeList.get(1).getVersion());
        Assertions.assertEquals("B1", tradeList.get(1).getBookId());
    }

    @Test
    void whenMaturityDateLessThanTodayDate_thenFailTrade() throws MaturityDateValidationException {
        LocalDate maturityDate = getLocalDate(2020, 05, 20);
        Throwable exception = Assertions.assertThrows(MaturityDateValidationException.class, () -> {
            tradeStoreController.tradeStore(storeTrade(2, "T2", 1, maturityDate));
        });
        Assertions.assertEquals("Trade not saved as failed maturity date validation.", exception.getMessage());
    }

    @Test
    void whenTradeWithLowerVersion_thenThrowException() throws LowerVersionTradeException {
        Throwable exception = Assertions.assertThrows(LowerVersionTradeException.class, () -> {
            tradeStoreController.tradeStore(storeTrade(5, "T1", 0, LocalDate.now()));
        });
        Assertions.assertEquals("Lower Trade Version Received.", exception.getMessage());
    }

    @Test
    void whenTradeWithNewIdAndVersion_thenCreateTrade() throws RuntimeException {
        ResponseEntity responseEntity = tradeStoreController.tradeStore(storeTrade(6, "T1", 6, LocalDate.now()));
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(), responseEntity);
        List<TradeStore> tradeList = tradeStoreController.allTrades();
        Assertions.assertEquals(6, tradeList.size());
        Assertions.assertEquals(6, tradeList.get(5).getId());
        Assertions.assertEquals("T1", tradeList.get(5).getTradeId());
        Assertions.assertEquals(6, tradeList.get(5).getVersion());
        Assertions.assertEquals("B1", tradeList.get(5).getBookId());
    }

    private TradeStore storeTrade(int id, String tradeId, int version, LocalDate maturityDate) {
        TradeStore trade = new TradeStore();
        trade.setId(id);
        trade.setTradeId(tradeId);
        trade.setVersion(version);
        trade.setCounterPartyId("CP-2");
        trade.setBookId("B1");
        trade.setMaturityDate(maturityDate);
        trade.setCreatedDate(LocalDate.now());
        trade.setExpired("Y");
        return trade;
    }

    public static LocalDate getLocalDate(int year, int month, int day) {
        LocalDate localDate = LocalDate.of(year, month, day);
        return localDate;
    }

}
