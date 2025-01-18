package org.stockservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stockservice.model.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
}

