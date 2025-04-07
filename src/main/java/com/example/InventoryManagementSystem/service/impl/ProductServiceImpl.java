package com.example.InventoryManagementSystem.service.impl;

import com.example.InventoryManagementSystem.dto.ProductDTO;
import com.example.InventoryManagementSystem.dto.Response;
import com.example.InventoryManagementSystem.dto.SupplierDTO;
import com.example.InventoryManagementSystem.entity.Category;
import com.example.InventoryManagementSystem.entity.Product;
import com.example.InventoryManagementSystem.exceptions.NotFoundException;
import com.example.InventoryManagementSystem.repository.CategoryRepository;
import com.example.InventoryManagementSystem.repository.ProductRepository;
import com.example.InventoryManagementSystem.repository.UserRepository;
import com.example.InventoryManagementSystem.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.management.modelmbean.ModelMBean;
import java.io.File;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ModelMapper modelMapper;
//    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private static final String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/product-image/";

    @Override
    public Response saveProduct(ProductDTO productDTO, MultipartFile imageFile) {
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        Product productToSave = Product.builder()
                .name(productDTO.getName())
                .sku(productDTO.getSku())
                .price(productDTO.getPrice())
                .stockQuantity(productDTO.getStockQuantity())
                .description(productDTO.getDescription())
                .category(category)
                .build();

        if (imageFile != null) {
            String imagePath = saveImage(imageFile);
            productToSave.setImageUrl(imagePath);
        }

        productRepository.save(productToSave);

        return Response.builder()
                .status(200)
                .message("Product successfully saved")
                .build();

    }
//    @Override
//    public Response saveProduct(ProductDTO productDTO, MultipartFile imageFile) {
//        Product product = modelMapper.map(productDTO, Product.class);
////        product.setImageUrl(imageFile);
//        Product savedProduct = productRepository.save(product);
//        ProductDTO productDTO1 = modelMapper.map(savedProduct, ProductDTO.class);
//
//        return Response.builder()
//                .product(productDTO1)
//                .message("product saved successfully")
//                .build();
//    }

    @Override
    public Response updateProduct(ProductDTO productDTO, MultipartFile imageFile) {

        System.out.println(productDTO.getProductId());
        Product existingProduct = productRepository.findById(productDTO.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        // check if image is associated with the update request
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = saveImage(imageFile);
            existingProduct.setImageUrl(imagePath);
        }

        // check if category is to be changed for the the product
        if (productDTO.getCategoryId() != null && productDTO.getCategoryId() > 0 ) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category Not Found"));

            existingProduct.setCategory(category);
        }

        if (productDTO.getName() != null && !productDTO.getName().isBlank()) {
            existingProduct.setName(productDTO.getName());
        }

        if (productDTO.getSku() != null && !productDTO.getSku().isBlank()) {
            existingProduct.setSku(productDTO.getSku());
        }

        if (productDTO.getDescription() != null && !productDTO.getDescription().isBlank()) {
            existingProduct.setDescription(productDTO.getDescription());
        }

        if (productDTO.getPrice() != null && productDTO.getPrice().compareTo(BigDecimal.ZERO) >= 0) {
            existingProduct.setPrice(productDTO.getPrice());

        }

        if (productDTO.getStockQuantity() != null && productDTO.getStockQuantity() >= 0) {
            existingProduct.setStockQuantity(productDTO.getStockQuantity());
        }


        productRepository.save(existingProduct);

        ProductDTO productDTO1 = modelMapper.map(existingProduct, ProductDTO.class);
        return Response.builder()
                .status(200)
                .product(productDTO1)
                .message("product updated successfully updated")
                .build();


    }

//
//    @Override
//    public Response updateProduct(ProductDTO productDTO, MultipartFile imageFile) {
//
//        Product product = productRepository.findById(productDTO.getProductId()).orElseThrow(() -> new NotFoundException("Product not Found"));
//
//        //        Product product = modelMapper.map(productDTO, Product.class);
//
//        if (productDTO.getName() != null) product.setName(productDTO.getName());
//        if (productDTO.getPrice() != null) product.setPrice(productDTO.getPrice());
////        if (productDTO.getSupplierId() != null) product.setSupplierId(productDTO.getSupplierId())
//
//        if (productDTO.getDescription() != null) product.setDescription(productDTO.getDescription());
//        if (productDTO.getSku() != null) product.setSku(productDTO.getSku());
//        if (productDTO.getStockQuantity() != null) product.setStockQuantity(productDTO.getStockQuantity());
//
//        if (productDTO.getCategoryId() != null)  {
//            Category category = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(() -> new NotFoundException("Category not Found"));
//            product.setCategory(category);
//        }
//
//        Product savedProduct = productRepository.save(product);
//        ProductDTO productDTO1 = modelMapper.map(savedProduct, ProductDTO.class);
//
//        return Response.builder()
//                .product(productDTO1)
//                .message("product updated successfully")
//                .build();
//    }

    @Override
    public Response getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOS = modelMapper.map(products, new TypeToken<List<ProductDTO>>() {}.getType());

        return Response
                .builder()
                .status(200)
                .message("success")
                .products(productDTOS)
                .build();
    }

    @Override
    public Response getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("product not found"));
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

        return Response.builder()
                .product(productDTO)
                .message("product retrieved successfully")
                .build();
    }

    @Override
    public Response deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("product not found"));

        productRepository.deleteById(id);

        return Response.builder()
                .message("product deleted successfully")
                .build();
    }

    private String saveImage(MultipartFile imageFile) {
        if (!imageFile.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        File directory = new File(IMAGE_DIRECTORY);

        if (!directory.exists()) {
            directory.mkdir();
            log.info("Directyory was created");
        }
        // generate unique file name for the image
        String uniqueFileName = UUID.randomUUID() + " " + imageFile.getOriginalFilename();

        //get the absolute path of the image
        String imagePath = IMAGE_DIRECTORY + uniqueFileName;

        try {
            File destinationFile = new File(imagePath);
            imageFile.transferTo(destinationFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return imagePath;
    }
}
















