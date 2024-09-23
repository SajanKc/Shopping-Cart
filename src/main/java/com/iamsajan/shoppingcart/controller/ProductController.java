package com.iamsajan.shoppingcart.controller;

import com.iamsajan.shoppingcart.dto.ApiResponse;
import com.iamsajan.shoppingcart.dto.request.AddProductRequest;
import com.iamsajan.shoppingcart.dto.request.ProductUpdateRequest;
import com.iamsajan.shoppingcart.model.Product;
import com.iamsajan.shoppingcart.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @GetMapping("/all")
    private ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse("Products found!", products));
    }

    @GetMapping("/{productId}")
    private ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Product found!", product));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/by-name/{name}")
    private ResponseEntity<ApiResponse> getProductsByName(@PathVariable String name) {
        try {
            List<Product> products = productService.getProductsByName(name);
            if (products.isEmpty())
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found!", null));

            return ResponseEntity.ok(new ApiResponse("Products found!", products));

        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/by-brand/{brand}")
    private ResponseEntity<ApiResponse> getProductsByBrand(@PathVariable String brand) {
        try {
            List<Product> products = productService.getProductsByBrand(brand);
            if (products.isEmpty())
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found!", null));

            return ResponseEntity.ok(new ApiResponse("Products found!", products));

        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/by-brand-and-name/{brand}/{productName}")
    private ResponseEntity<ApiResponse> getProductsByBrandAndName(@PathVariable String brand, @PathVariable String productName) {
        try {
            List<Product> products = productService.getProductsByBrandAndName(brand, productName);
            if (products.isEmpty())
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found!", null));
            return ResponseEntity.ok(new ApiResponse("Products found!", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/by-category/{category}")
    private ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable String category) {
        try {
            List<Product> products = productService.getProductsByCategory(category);
            if (products.isEmpty())
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found!", null));
            return ResponseEntity.ok(new ApiResponse("Products found!", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/by-category-and-brand/{category}/{brand}")
    private ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@PathVariable String category, @PathVariable String brand) {
        try {
            List<Product> products = productService.getProductsByCategoryAndBrand(category, brand);
            if (products.isEmpty())
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found!", null));
            return ResponseEntity.ok(new ApiResponse("Products found!", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/count-by-brand-and-name")
    private ResponseEntity<ApiResponse> countProductByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            var productCount = productService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.ok(new ApiResponse("Product count!", productCount));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    private ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
        try {
            Product savedProduct = productService.addProduct(product);
            return ResponseEntity.ok(new ApiResponse("Product saved!", savedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{productId}")
    private ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest productUpdateRequest, @PathVariable Long productId) {
        try {
            Product updatedProduct = productService.updateProduct(productUpdateRequest, productId);
            return ResponseEntity.ok(new ApiResponse("Product updated!", updatedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{productId}")
    private ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Product deleted!", null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

}
