package com.mall.dao;

import com.mall.pojo.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectList();

    List<Product> selectByNameAndProductId(@Param("productName")String name,@Param("productId") Integer productId);

    List<Product> selectByNameAndCategoryIds(@RequestParam("productName") String productName,@RequestParam("categoryIdList") List<Integer> categoryIdList);


}