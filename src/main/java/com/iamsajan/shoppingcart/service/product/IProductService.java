package com.iamsajan.shoppingcart.service.product;

import com.iamsajan.shoppingcart.dto.request.AddProductRequest;
import com.iamsajan.shoppingcart.dto.request.ProductUpdateRequest;
import com.iamsajan.shoppingcart.model.Product;

import java.util.List;

public interface IProductService {

    Product addProduct(AddProductRequest productRequest);

    Product getProductById(Long id);

    void deleteProductById(Long id);

    Product updateProduct(ProductUpdateRequest productUpdateRequest, Long productId);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByBrand(String brand);

    List<Product> getProductsByCategoryAndBrand(String category, String brand);

    List<Product> getProductsByName(String name);

    List<Product> getProductsByBrandAndName(String brand, String name);

    Long countProductsByBrandAndName(String brand, String name);
}
