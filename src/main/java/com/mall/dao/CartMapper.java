package com.mall.dao;

import com.mall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectCartByUserIdProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    List<Cart> selectCartByUserId(Integer userId);

    int selectIsCheckedByUserId(Integer userId);

    int deleteCartByUserIdAndProductIdList(@Param("userId") Integer userId, @Param("productIdList") List<String> productIdList);

    int selectAllOrUnSelectAll(@Param("userId") Integer userId, @Param("checked") Integer checked,@Param("productId") Integer productId);

    int selectProductCount(Integer userId);
}