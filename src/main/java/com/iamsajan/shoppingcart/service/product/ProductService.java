package com.iamsajan.shoppingcart.service.product;

import com.iamsajan.shoppingcart.dto.request.AddProductRequest;
import com.iamsajan.shoppingcart.dto.request.ProductUpdateRequest;
import com.iamsajan.shoppingcart.exceptions.ProductNotFoundException;
import com.iamsajan.shoppingcart.model.Category;
import com.iamsajan.shoppingcart.model.Product;
import com.iamsajan.shoppingcart.repository.CategoryRepository;
import com.iamsajan.shoppingcart.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product addProduct(AddProductRequest productRequest) {
        Category category = Optional.ofNullable(categoryRepository.findByName(productRequest.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(productRequest.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        productRequest.setCategory(category);

        return productRepository.save(createProduct(productRequest, category));
    }

    private Product createProduct(AddProductRequest productRequest, Category category) {
        return new Product(
                productRequest.getName(),
                productRequest.getBrand(),
                productRequest.getPrice(),
                productRequest.getInventory(),
                productRequest.getDescription(),
                category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                        () -> {
                            throw new ProductNotFoundException("Product not found");
                        });
    }

    @Override
    public Product updateProduct(ProductUpdateRequest productUpdateRequest, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct ->
                        updateExistingProduct(existingProduct, productUpdateRequest))
                .map(productRepository::save)
                .orElseThrow(() -> new ProductNotFoundException("Product not found!"));
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest productUpdateRequest) {
        existingProduct.setName(productUpdateRequest.getName());
        existingProduct.setBrand(productUpdateRequest.getBrand());
        existingProduct.setPrice(productUpdateRequest.getPrice());
        existingProduct.setInventory(productUpdateRequest.getInventory());
        existingProduct.setDescription(productUpdateRequest.getDescription());

        Category category = categoryRepository.findByName(productUpdateRequest.getCategory().getName());
        existingProduct.setCategory(category);

        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryNameIgnoreCase(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrandIgnoreCase(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameIgnoreCaseAndBrandIgnoreCase(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandIgnoreCaseAndNameIgnoreCase(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandIgnoreCaseAndNameIgnoreCase(brand, name);
    }

}
