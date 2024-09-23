package com.iamsajan.shoppingcart.repository;

import com.iamsajan.shoppingcart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryNameIgnoreCase(String category);

    List<Product> findByBrandIgnoreCase(String brand);

    List<Product> findByCategoryNameIgnoreCaseAndBrandIgnoreCase(String category, String brand);

    @Query("SELECT p FROM Product  p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findByName(String name);

    List<Product> findByBrandIgnoreCaseAndNameIgnoreCase(String brand, String name);

    Long countByBrandIgnoreCaseAndNameIgnoreCase(String brand, String name);

}
