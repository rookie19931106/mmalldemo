package com.mall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.dao.CartMapper;
import com.mall.dao.ProductMapper;
import com.mall.pojo.Cart;
import com.mall.pojo.Product;
import com.mall.service.ICartService;
import com.mall.utils.BigDecimalUtil;
import com.mall.utils.PropertiesUtil;
import com.mall.vo.CartProductVo;
import com.mall.vo.CartVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author jay
 */
@Service("iCartService")
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ServerResponse<CartVO> add(Integer userId, Integer count, Integer productId) {
        if (productId == null || count == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "非法参数");
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        //购物车中没有该产品
        if (cart == null) {
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.CartConst.CHECKED);
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartMapper.insert(cartItem);
        } else {
            //这个产品已经在购物车里了
            //产品已存在，数量相加
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return list(userId);
    }

    @Override
    public ServerResponse<CartVO> update(Integer userId, Integer count, Integer productId) {
        if (productId == null || count == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "非法参数");
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if (cart != null) {
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        return list(userId);
    }

    @Override
    public ServerResponse<CartVO> deleteProduct(Integer userId, String productIds) {
        List<String> productIdList = Splitter.on(",").splitToList(productIds);
        if (CollectionUtils.isEmpty(productIdList)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "非法参数");
        }
        cartMapper.deleteCartByUserIdAndProductIdList(userId, productIdList);
        return list(userId);
    }


    @Override
    public ServerResponse<CartVO> list(Integer userId) {
        CartVO cartVO = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVO);
    }

@Override
    public ServerResponse<CartVO> selectAllOrUnSelectAll(Integer userId, Integer checked,Integer productId) {
        cartMapper.selectAllOrUnSelectAll(userId, checked,productId);
        return list(userId);
    }

    @Override
    public ServerResponse<Integer> selectProductCount(Integer userId){
       return ServerResponse.createBySuccess(cartMapper.selectProductCount(userId));

    }
    /**
     * 获得购物车的信息
     *
     * @param userId
     * @return
     */
    private CartVO getCartVoLimit(Integer userId) {

        CartVO cartVO = new CartVO();
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        List<CartProductVo> cartProductVoList = Lists.newArrayList();
        BigDecimal cartTotalPrice = new BigDecimal("0");
        if (CollectionUtils.isNotEmpty(cartList)) {
            for (Cart cartItem : cartList
                    ) {
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setUserId(cartItem.getUserId());
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setProductId(cartItem.getProductId());

                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if (product != null) {
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());
                    cartProductVo.setSubTitle(product.getSubtitle());
                    //判断库存
                    int buyLimitCount = 0;
                    if (product.getStock() >= cartItem.getQuantity()) {
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.CartConst.LIMIT_NUM_SUCCESS);
                    } else {
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.CartConst.LIMIT_NUM_FAIL);
                        //跟新有效库存
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    //计算总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductVo.getQuantity().doubleValue()));
                    cartProductVo.setProductChecked(cartItem.getChecked());
                }
                if (cartItem.getChecked() == Const.CartConst.CHECKED) {
                    //如果已经勾选，增加到整个购物车中
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
                cartVO.setCartTotalPrice(cartTotalPrice);
                cartVO.setCartProductVoList(cartProductVoList);
                cartVO.setAllChecked(this.productIsChecked(userId));
                cartVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
            }
        }
        return cartVO;
    }

    private Boolean productIsChecked(Integer userId) {
        if (userId == null) {
            return false;
        }
        return cartMapper.selectIsCheckedByUserId(userId) == 0;
    }
}
