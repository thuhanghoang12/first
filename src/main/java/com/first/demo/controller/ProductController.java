package com.first.demo.controller;

import com.first.demo.models.Product;
import com.first.demo.models.ResponseObject;
import com.first.demo.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/vd/Products")
public class ProductController {
    @Autowired
    private ProductRepository repository;

    // Method get
    @GetMapping("")
    //http:/localhost:8080/api/vd/Products
    List<Product> getAllProducts() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> getById(@PathVariable Long id) {
        Optional<Product> foundProduct = repository.findById(id);
        return foundProduct.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Query product successfully", foundProduct)
                ) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("false", "Cannot product with id = " + id, "")
                );
    }

    //insert new Product with POST method
    @PostMapping("/insertProduct")
    ResponseEntity<ResponseObject> insertProduct(@RequestBody Product newProduct) {
        List<Product> foundProducts = repository.findByProductName(newProduct.getProductName().trim());
        if (foundProducts.size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("false", "product name duplicate", " ")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "insert product succsessfully", repository.save(newProduct))
        );
    }

    //update, upsert = update if found, otherswise insert
    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> upsertProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        Product upsertProduct = repository.findById(id)
                .map(product -> {
                    product.setProductName(newProduct.getProductName());
                    product.setYear(newProduct.getYear());
                    product.setPrice(newProduct.getPrice());
                    return repository.save(product);
                }).orElseGet(() -> {
                    newProduct.setId(id);
                    return repository.save(newProduct);
                });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "update product succsessfully", upsertProduct)
        );
    }

    //Delete a product = DELETE method
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id) {
        boolean exists = repository.existsById(id);
        if(exists){
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Delete product successfully","")
            );
        }return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("OK","Cannot find product to delete","")
        );
    }
}
