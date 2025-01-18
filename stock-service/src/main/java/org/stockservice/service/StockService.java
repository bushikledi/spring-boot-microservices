package org.stockservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.stockservice.dto.StockUpdate;
import org.stockservice.event.OrderEventPublisher;
import org.stockservice.model.Stock;
import org.stockservice.repository.StockRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final OrderEventPublisher orderEventPublisher;

    public Stock saveStock(Stock stock) {
        return stockRepository.save(stock);
    }

    public List<Stock> getAllInventories() {
        return stockRepository.findAll();
    }

    public Optional<Stock> getStockById(Long id) {
        return stockRepository.findById(id);
    }

    public Optional<Stock> updateStock(Long id, Stock stockDetails) {
        Optional<Stock> stock = stockRepository.findById(id);
        if (stock.isPresent()) {
            Stock existingStock = stock.get();
            existingStock.setProductName(stockDetails.getProductName());
            existingStock.setCategory(stockDetails.getCategory());
            existingStock.setSupplier(stockDetails.getSupplier());
            existingStock.setQuantity(stockDetails.getQuantity());
            existingStock.setPrice(stockDetails.getPrice());
            return Optional.of(stockRepository.save(existingStock));
        }
        return Optional.empty();
    }

    public void increaseStock(StockUpdate stockUpdate, Boolean isCancelling) {
            Optional<Stock> stock = stockRepository.findById(stockUpdate.getProductId());
            if (stock.isPresent()) {
                Stock existingStock = stock.get();
                existingStock.setQuantity(existingStock.getQuantity() + stockUpdate.getQuantity());
                stockRepository.save(existingStock);
                if(!isCancelling){
                    orderEventPublisher.updateSuccessOrderEvent(stockUpdate.getId());
                }
            } else {
                if(!isCancelling) {
                    orderEventPublisher.updateFailedOrderEvent(stockUpdate.getId());
                }
            }
    }

    public void decreaseStock(StockUpdate stockUpdate) {
            Optional<Stock> stock = stockRepository.findById(stockUpdate.getProductId());
            if (stock.isPresent()) {
                Stock existingStock = stock.get();
                int newQuantity = existingStock.getQuantity() - stockUpdate.getQuantity();
                if (newQuantity < 0) {
                    orderEventPublisher.updateFailedOrderEvent(stockUpdate.getId());

                } else {
                    orderEventPublisher.updateSuccessOrderEvent(stockUpdate.getId());
                }
            } else {
                orderEventPublisher.updateFailedOrderEvent(stockUpdate.getId());

            }
    }


    public boolean deleteStock(Long id) {
        if (stockRepository.existsById(id)) {
            stockRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
