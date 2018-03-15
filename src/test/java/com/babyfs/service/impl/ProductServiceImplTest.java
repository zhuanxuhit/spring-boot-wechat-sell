package com.babyfs.service.impl;

import com.babyfs.dataobject.ProductInfo;
import com.babyfs.enums.ProductStatusEnum;
import com.babyfs.repository.ProductInfoRepository;
import com.babyfs.service.ProductService;
import net.bytebuddy.asm.Advice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductServiceImplTest {

    @Autowired
    private ProductService productService;

    @Test
    public void findOne() throws Exception {
        Optional<ProductInfo> productInfo = productService.findOne("123123456456");
        assertNotNull(productInfo.get());
    }

    @Test
    @Transactional
    public void findUpAll() throws Exception {
        ProductInfo productInfo = productService.findOne("123123456456").get();
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        productService.save(productInfo);
        assertNotEquals(0,productService.findUpAll().size());
    }

    @Test
    public void findAll() throws Exception {
        PageRequest request = PageRequest.of(0,2);
        Page<ProductInfo> page = productService.findAll(request);
        assertNotEquals(0,page.getTotalElements());
    }

    @Test
    @Transactional
    public void save() throws Exception {
        ProductInfo result = newProduct(ProductStatusEnum.UP);
        assertNotNull(result);
    }

    public ProductInfo newProduct(ProductStatusEnum status){
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("1234573");
        productInfo.setProductName("food");
        productInfo.setProductPrice(new BigDecimal(3.2));
        productInfo.setProductStock(100);
        productInfo.setProductDescription("good food");
        productInfo.setProductIcon("http://xxxxx.jpg");
        productInfo.setProductStatus(status.getCode());
        productInfo.setCategoryType(2);
        return productService.save(productInfo);
    }

    @Test
    @Transactional
    public void onSale() throws Exception {
        ProductInfo result = newProduct(ProductStatusEnum.DOWN);
        result = productService.onSale(result.getProductId());
        assertEquals(ProductStatusEnum.UP.getCode(),result.getProductStatus());
    }

    @Test
    @Transactional
    public void offSale() throws Exception {
        ProductInfo result = newProduct(ProductStatusEnum.UP);
        result = productService.offSale(result.getProductId());
        assertEquals(ProductStatusEnum.DOWN.getCode(),result.getProductStatus());
    }

}