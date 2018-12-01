package com.fprochazka.hibernateenhancementbug.hibernateenhancementbug;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.*;
import java.time.Clock;
import java.util.*;

@SpringBootApplication
public class BuggyApplication implements CommandLineRunner
{

    private static Logger log = LoggerFactory.getLogger(BuggyApplication.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private Clock clock = Clock.systemDefaultZone();

    @Override
    public void run(final String... args) throws Exception
    {
        new TransactionTemplate(transactionManager).execute(status -> {
            prepareData();
            return null;
        });

        new TransactionTemplate(transactionManager).execute(status -> {
            queryData();
            return null;
        });
    }

    private void queryData()
    {
        String productsQuery = "SELECT product "
            + "FROM Product product "
            + "LEFT JOIN FETCH product.name productName "
            + "LEFT JOIN FETCH product.description productDescription ";
        List<Product> products = entityManager.createQuery(productsQuery, Product.class).getResultList();

        log.info(String.format("Products %d", products.size()));
    }

    private void prepareData()
    {
        entityManager.createNativeQuery("TRUNCATE product, product_name, product_description, product_additional_settings CASCADE").executeUpdate();

        Product product1 = new Product(clock);
        product1.setName(new ProductName(product1, "product 1: name 1"));
        product1.setName(new ProductName(product1, "product 1: name 2"));
        product1.setName(new ProductName(product1, "product 1: name 3"));
        product1.setDescription(new ProductDescription(product1, "something cool"));
        product1.setAdditionalSettings(new ProductAdditionalSettings(product1, true));

        Product product2 = new Product(clock);
        product2.setName(new ProductName(product2, "product 2: name 1"));
        product2.setDescription(new ProductDescription(product2, "also cool"));

        Product product3 = new Product(clock);
        product3.setName(new ProductName(product3, "product 3: name 1"));
        product3.setDescription(new ProductDescription(product3, "meh"));
        product3.setDescription(new ProductDescription(product3, "not meh"));
        product3.setAdditionalSettings(new ProductAdditionalSettings(product3, false));

        entityManager.persist(product1);
        entityManager.persist(product2);
        entityManager.persist(product3);
        entityManager.flush();
    }

    public static void main(String[] args)
    {
        SpringApplication.run(BuggyApplication.class, args);
    }

}
