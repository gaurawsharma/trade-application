package com.example.tradeapplication.resource;

import com.example.tradeapplication.model.TradeStore;
import com.example.tradeapplication.service.TradeStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/tradestore")
public class TradeStoreController {

    @Autowired
    private TradeStoreService tradeStoreService;

    @GetMapping(value = "/trades")
    public List<TradeStore> allTrades() {
        return tradeStoreService.findAllTrades();
    }

    @PostMapping(value = "/trades")
    public ResponseEntity<String> tradeStore(@RequestBody TradeStore tradeStore) {
        tradeStoreService.save(tradeStore);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
