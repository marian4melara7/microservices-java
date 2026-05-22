package br.edu.atitus.productservice.controllers;

import br.edu.atitus.productservice.clients.CurrencyClient;
import br.edu.atitus.productservice.clients.CurrencyResponse;
import br.edu.atitus.productservice.dtos.ProductDTO;
import br.edu.atitus.productservice.entities.ProductEntity;
import br.edu.atitus.productservice.repositories.ProductRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("products")
public class ProductController {

    private final ProductRepository repository;
    private final CurrencyClient currencyClient;

    @Value("${server.port}")
    private String port;

    public ProductController(ProductRepository repository, CurrencyClient currencyClient) {
        this.repository = repository;
        this.currencyClient = currencyClient;
    }

    @GetMapping("/{idproduct}")
    @Cacheable(value = "products", key = "#idproduct + '-' + #targetCurrency")
    @CircuitBreaker(name = "currency-service", fallbackMethod = "getProductFallback")
    @Retry(name = "currency-service")
    public ResponseEntity<ProductDTO> getProduct(
            @PathVariable Long idproduct,
            @RequestParam String targetCurrency) throws Exception {
        targetCurrency = targetCurrency.toUpperCase();

        ProductEntity entity = repository.findById(idproduct)
                .orElseThrow(() -> new Exception("Product not found"));

        Double convertedPrice = null;
        String environment = "Product-service running on port: " + port;
        String requestCurrency = targetCurrency;

        if (targetCurrency.equals(entity.getCurrency())) {
            convertedPrice = entity.getPrice();
        } else {
            CurrencyResponse currency = currencyClient.getCurrency(entity.getCurrency(), targetCurrency);
            convertedPrice = entity.getPrice() * currency.conversionRate();
            environment = environment + " - " + currency.environment();
        }

        ProductDTO dto = new ProductDTO(
                entity.getId(),
                entity.getDescription(),
                entity.getBrand(),
                entity.getModel(),
                entity.getPrice(),
                entity.getCurrency(),
                entity.getStock(),
                environment,
                convertedPrice,
                requestCurrency
        );

        return ResponseEntity.ok(dto);
    }

    public ResponseEntity<ProductDTO> getProductFallback(
            Long idproduct, String targetCurrency, Throwable t) {
        return repository.findById(idproduct).map(entity -> {
            ProductDTO dto = new ProductDTO(
                    entity.getId(),
                    entity.getDescription(),
                    entity.getBrand(),
                    entity.getModel(),
                    entity.getPrice(),
                    entity.getCurrency(),
                    entity.getStock(),
                    "Product-service running on port: " + port + " - fallback (currency-service unavailable)",
                    entity.getPrice(),
                    targetCurrency
            );
            return ResponseEntity.ok(dto);
        }).orElse(ResponseEntity.notFound().build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        String message = e.getMessage().replace("/r/n", "");
        return ResponseEntity.badRequest().body(message);
    }
}