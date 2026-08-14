package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.dto.ProductRequest;
import com.ecommerce.productservice.dto.ProductResponse;
import com.ecommerce.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody ProductRequest request
    ) {
        log.info("Creating product - name: {}", request.getName());

        ProductResponse product = productService.createProduct(request);

        log.info("Product successfully created - productId: {}", product.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest request
    ) {
        log.info("Updating product - productId: {}", id);

        ProductResponse product = productService.updateProduct(id, request);

        log.info("Product successfully updated - productId: {}", id);

        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        log.info("Fetching all products");

        List<ProductResponse> products = productService.getAllProducts();

        log.info("Products successfully fetched - count: {}", products.size());

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(
            @PathVariable Long id
    ) {
        log.info("Fetching product - productId: {}", id);

        ProductResponse product = productService.getProduct(id);

        log.info("Product successfully fetched - productId: {}", id);

        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id
    ) {
        log.info("Deleting product - productId: {}", id);

        productService.deleteProject(id);

        log.info("Product successfully deleted - productId: {}", id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam String keyword
    ) {
        log.info("Searching products - keyword: {}", keyword);

        List<ProductResponse> products = productService.searchProducts(keyword);

        log.info("Product search completed - keyword: {}, results: {}",
                keyword, products.size());

        return ResponseEntity.ok(products);
    }
}