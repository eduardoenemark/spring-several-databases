package com.severaldatabases.repository;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.severaldatabases.model.Product;
import static java.lang.System.out;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableTransactionManagement
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductRepository testProductRepository;

    @Test
    @Transactional(transactionManager = "transactionManager")
    public void saveOnMainTransactionManager() {
        this.productRepository.save(new Product(1001, "Book", 21d));
        assertTrue(this.productRepository.findById(1001).isPresent());
    }

    @Test
    @Transactional(transactionManager = "testTransactionManager")
    public void saveOnTestTransactionManager() {
        this.testProductRepository.save(new Product(1002, "Coffe", 10));
        assertTrue(this.testProductRepository.findById(1002).isPresent());
    }

    @After
    public void after() {
//        assertFalse(this.productRepository.findById(1002).isPresent());
//        assertFalse(this.testProductRepository.findById(1001).isPresent());
        out.println("IS PRESENT?:" + this.productRepository.findById(1002).isPresent());
        Product product = this.productRepository.findById(1002).get();
        out.println("PRODUCT NAME: " + product.getName());
    }
}
