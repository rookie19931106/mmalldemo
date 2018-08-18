package com.mall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mall.common.ServerResponse;
import com.mall.service.IProductService;
import com.mall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author jay
 */
@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    /**
     * 根据id查找产品
     * @param productId
     * @return
     */

    @RequestMapping("/productDetail.do")
    @ResponseBody
    public ServerResponse<ProductDetailVo> productDetail(Integer productId){
        return iProductService.productDetail(productId);
    }

    /**
     * 查找商品列表
     * @param keyWord
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/productInfo.do")
    @ResponseBody
    public ServerResponse<PageInfo> productInfo(@RequestParam(value = "keyWord",required = false) String keyWord,
                                                @RequestParam(value = "categoryId",required = false) Integer categoryId,
                                                @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                                @RequestParam(value = "orderBy",defaultValue = "") String orderBy,
                                                @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        return iProductService.getProductByKeywordCategory(keyWord,categoryId,pageSize,pageNum,orderBy);
    }
}
