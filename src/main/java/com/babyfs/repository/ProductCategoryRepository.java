package com.babyfs.repository;

import com.babyfs.dataobject.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author 颛顼
 * @Description: dao 层
 * @date 2018-03-14 下午5:07
 */
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer>{
    /**
     * 根据类目id查找类目
     * @param categoryTypeList
     * @return
     */
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    /**
     * 查询返回
     * @param type
     * @return
     */
    ProductCategory findByCategoryType(Integer type);
}
