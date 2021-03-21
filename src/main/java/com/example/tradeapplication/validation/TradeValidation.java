package com.example.tradeapplication.validation;

import com.example.tradeapplication.model.TradeStore;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class TradeValidation {

    //  During transmission If the version is same it will override the existing record.
    public boolean validateTradeVersion(TradeStore trade, List<TradeStore> TradeObjects) {
        for(TradeStore previousTrades : TradeObjects) {
            if(trade.getVersion() == previousTrades.getVersion()){
                return true;
            }
        }
        return false;
    }

    // Store should not allow the trade which has less maturity date then today date.
    public boolean validateTradeMaturityDate(LocalDate maturityDate) {
        return maturityDate.isBefore(LocalDate.now()) ? false : true;
    }

}
