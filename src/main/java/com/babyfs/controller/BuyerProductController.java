package com.babyfs.controller;

import com.babyfs.VO.ProductInfoVO;
import com.babyfs.VO.ProductVO;
import com.babyfs.VO.ResultVO;
import com.babyfs.dataobject.ProductCategory;
import com.babyfs.dataobject.ProductInfo;
import com.babyfs.service.CategoryService;
import com.babyfs.service.ProductService;
import com.babyfs.utils.ResultVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResultVO list(){
        // 1. 获取所有的上架商品
        List<ProductInfo> productInfoList =  productService.findUpAll();
        // 2. 获取商品类目信息
        List<Integer> categoryTypeList = productInfoList.stream().map(ProductInfo::getCategoryType).collect(Collectors.toList());

        List<ProductCategory> productCategoryList =  categoryService.findByCategoryTypeIn(categoryTypeList);

        // 3. 拼装返回
        List<ProductVO> productVOList = new ArrayList<>();
        for (ProductCategory category :
                productCategoryList) {
            ProductVO productVO = new ProductVO();
            productVO.setCategoryName(category.getCategoryName());
            productVO.setCategoryType(category.getCategoryType());
            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for (ProductInfo info :
                    productInfoList) {
                if (Objects.equals(info.getCategoryType(), category.getCategoryType())){
                    ProductInfoVO productInfoVO = new ProductInfoVO();
//                    productInfoVO.setProductId(info.getProductId());
//                    productInfoVO.setProductDescription(info.getProductDescription());
//                    productInfoVO.setProductIcon(info.getProductIcon());
//                    productInfoVO.setProductName(info.getProductName());
//                    productInfoVO.setProductPrice(info.getProductPrice());
                    BeanUtils.copyProperties(info, productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }
        return ResultVOUtil.success(productVOList);
    }
}
