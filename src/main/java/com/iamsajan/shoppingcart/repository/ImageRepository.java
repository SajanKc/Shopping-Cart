package com.iamsajan.shoppingcart.repository;

import com.iamsajan.shoppingcart.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
}
