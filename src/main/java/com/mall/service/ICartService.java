package com.mall.service;

import com.mall.common.ServerResponse;
import com.mall.vo.CartVO;

/**
 * @author jay
 */
public interface ICartService {

    /**
     * 给购物车添加商品
     * @param userId
     * @param count
     * @param productId
     * @return
     */
    public ServerResponse add(Integer userId, Integer count, Integer productId);

    /**
     * 修改购物车商品的数量
     * @param userId
     * @param count
     * @param productId
     * @return
     */
    public ServerResponse<CartVO> update(Integer userId, Integer count, Integer productId);

    /**
     * 删除购物车的商品
     * @param userId
     * @param productIds
     * @return
     */
    public ServerResponse<CartVO> deleteProduct(Integer userId,String productIds);

    /**
     * 查询购物车
     * @param userId
     * @return
     */
    public ServerResponse<CartVO> list(Integer userId);

    /**
     * 对购物车进行选择操作
     * @param userId
     * @param checked
     * @param productId
     * @return
     */
    public ServerResponse<CartVO> selectAllOrUnSelectAll(Integer userId, Integer checked,Integer productId);

    /**
     * 查找购物车中的商品数量
     * @param userId
     * @return
     */
    public ServerResponse<Integer> selectProductCount(Integer userId);
}
