package com.babyfs.repository;

import com.babyfs.dataobject.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 颛顼
 * @Description:
 * @date 2018-03-14 下午5:48
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductCategoryRepositoryTest {
    public static final int CATEGORY_ID = 1;
    public static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
    @Autowired
    private ProductCategoryRepository repository;

    @Test
    public void findOneTest() {
        ProductCategory probe = new ProductCategory();
        probe.setCategoryId(CATEGORY_ID);
        Optional<ProductCategory> productCategory = repository.findOne(Example.of(probe));
        if (productCategory.isPresent()) {
            System.out.println(productCategory.get().toString());
        } else {
            System.out.println("product_category: " + CATEGORY_ID + " not exist.");
        }
    }

    @Test
    @Transactional
    public void saveTest() throws Exception {
        ProductCategory productCategory = new ProductCategory("女性服装",3);
        Assert.assertNotNull(repository.save(productCategory));
    }

    @Test
    public void updateTest() throws Exception {
        Optional<ProductCategory> product = repository.findById(CATEGORY_ID);
        if (!product.isPresent()) {
            System.out.println("no id:" + CATEGORY_ID + " exist");
        } else {
            ProductCategory productCategory = product.get();
            String categoryName = productCategory.getCategoryName();
            categoryName = categoryName.substring(0, categoryName.length() > DATEFORMAT.length() ? categoryName.length()-DATEFORMAT.length() : categoryName.length());
            productCategory.setCategoryName(categoryName+getNowDate());
            repository.save(productCategory);
        }
    }

    public String getNowDate(){
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(DATEFORMAT);
        return formatter.format(currentTime);
    }

    @Test
    public void findByCategoryTypeIn() throws Exception {
        List<Integer> list = Arrays.asList(1,2,3);
        List<ProductCategory> productCategories = repository.findByCategoryTypeIn(list);
        Assert.assertNotEquals(0, productCategories.size());
    }
}