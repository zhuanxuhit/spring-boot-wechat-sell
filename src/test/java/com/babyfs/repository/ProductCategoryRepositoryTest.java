package com.babyfs.repository;

import com.babyfs.dataobject.ProductCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

/**
 * @author 颛顼
 * @Description:
 * @date 2018-03-14 下午5:48
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductCategoryRepositoryTest {
    public static final int CATEGORY_ID = 1;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Test
    public void findOneTest() {
        ProductCategory probe = new ProductCategory();
        probe.setCategoryId(CATEGORY_ID);
        Optional<ProductCategory> productCategory = productCategoryRepository.findOne(Example.of(probe));
        if (productCategory.isPresent()){
            System.out.println(productCategory.get().toString());
        }else {
            System.out.println("product_category: "+CATEGORY_ID+" not exist.");
        }
    }
}