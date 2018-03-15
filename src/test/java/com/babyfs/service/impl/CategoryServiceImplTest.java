package com.babyfs.service.impl;

import com.babyfs.dataobject.ProductCategory;
import com.babyfs.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CategoryServiceImplTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void findOne() throws Exception {
        Optional<ProductCategory> productCategory = categoryService.findOne(1);
        assertTrue(productCategory.isPresent());
    }

    @Test
    public void findAll() throws Exception {
        assertNotEquals(0,categoryService.findAll().size());
    }

    @Test
    public void findByCategoryTypeIn() throws Exception {
        List<Integer> lists = Arrays.asList(1, 2, 2);
        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(lists);
        assertNotEquals(0, productCategoryList.size());
    }

    @Test
    @Transactional
    public void save() throws Exception {
        ProductCategory productCategory = new ProductCategory("summer-hot", 12);
        assertNotNull(categoryService.save(productCategory));
    }

}