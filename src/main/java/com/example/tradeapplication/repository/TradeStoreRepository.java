package com.example.tradeapplication.repository;

import com.example.tradeapplication.model.TradeStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TradeStoreRepository extends JpaRepository<TradeStore, Integer> {

    @Query(value = "SELECT t FROM TradeStore t where t.version = :version")
    List<TradeStore> findByVersion(@Param("version") Integer version);
}