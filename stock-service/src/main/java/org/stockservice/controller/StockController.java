package org.stockservice.controller;

import lombok.RequiredArgsConstructor;
import org.stockservice.model.Stock;
import org.stockservice.service.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping
    public ResponseEntity<Stock> createOrUpdateStock(@RequestBody Stock stock) {
        Stock savedStock = stockService.saveStock(stock);
        return new ResponseEntity<>(savedStock, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Stock> getAllInventories() {
        return stockService.getAllInventories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> getStockById(@PathVariable Long id) {
        Optional<Stock> stock = stockService.getStockById(id);
        return stock.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable Long id, @RequestBody Stock stockDetails) {
        Optional<Stock> updatedStock = stockService.updateStock(id, stockDetails);
        return updatedStock.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        boolean isDeleted = stockService.deleteStock(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
