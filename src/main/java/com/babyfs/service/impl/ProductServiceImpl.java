package com.babyfs.service.impl;

import com.babyfs.dataobject.ProductInfo;
import com.babyfs.enums.ProductStatusEnum;
import com.babyfs.enums.ResultEnum;
import com.babyfs.exception.SellException;
import com.babyfs.repository.ProductInfoRepository;
import com.babyfs.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductInfoRepository repository;

    @Override
    public Optional<ProductInfo> findOne(String productId) {
        return repository.findById(productId);
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return repository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }

    @Override
    public ProductInfo onSale(String productId) {
        return setProductStatus(productId, ProductStatusEnum.UP);
    }

    @Override
    public ProductInfo offSale(String productId) {
        return setProductStatus(productId, ProductStatusEnum.DOWN);
    }

    private ProductInfo setProductStatus(String productId, ProductStatusEnum status){
        Optional<ProductInfo> productInfo = findOne(productId);
        if (productInfo.isPresent()){
            ProductInfo product = productInfo.get();
            if (Objects.equals(product.getProductStatus(), status.getCode())){
                throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
            }

            product.setProductStatus(status.getCode());
            return repository.save(product);
        }
        else {
            throw new SellException(ResultEnum.PRODUCT_NOT_EXI);
        }
    }
}
