package com.iamsajan.shoppingcart.service.image;

import com.iamsajan.shoppingcart.dto.image.ImageDto;
import com.iamsajan.shoppingcart.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(String id);

    void deleteImageById(String id);

    List<ImageDto> saveImage(List<MultipartFile> files, Long productId);

    void updateImage(MultipartFile file, String imageId);
}
