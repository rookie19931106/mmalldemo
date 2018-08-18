package com.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.dao.CategoryMapper;
import com.mall.dao.ProductMapper;
import com.mall.pojo.Category;
import com.mall.pojo.Product;
import com.mall.service.ICategoryService;
import com.mall.service.IProductService;
import com.mall.utils.DateTimeUtils;
import com.mall.utils.PropertiesUtil;
import com.mall.vo.ProductDetailVo;
import com.mall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jay
 */

@Service("iProductService")
public class ProductServiceImpl implements IProductService{

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 更新和添加商品实现
     * @param product
     * @return
     */
    @Override
    public ServerResponse saveAllProduct(Product product){
        if(product !=null){
            //判断子图
            if (StringUtils.isNotBlank(product.getSubImages())){
                String[] snuImageArray = product.getSubImages().split(",");
                if(snuImageArray.length > 0){
                    product.setMainImage(snuImageArray[0]);
                }
                //判断产品
                if(product.getId() != null){
                   int resultRow = productMapper.updateByPrimaryKey(product);
                   if(resultRow > 0) {
                       return ServerResponse.createBySuccess("更新产品成功");
                   }else {
                       return ServerResponse.createByErrorMessage("更新产品失败");
                   }
                }else {
                    int resultRow = productMapper.insert(product);
                    if(resultRow > 0){
                        return ServerResponse.createBySuccess("新增产品成功");
                    }
                    return ServerResponse.createByErrorMessage("新增产品失败");
                }
            }
        }
        return ServerResponse.createByErrorMessage("更新产品不正确");
    }

    /**
     * 保存商品的实现
     * @param productId
     * @param status
     * @return
     */
    @Override
    public ServerResponse<String> setSaleStatus(Integer productId,Integer status){
        if(productId == null || status == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKey(product);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("修改产品信息成功");
        }
        return ServerResponse.createByErrorMessage("修改产品销售状态失败");
    }

@Override
    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId){
        if(productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }
        //vo对象
        ProductDetailVo productDetailVo =assembleProductVo(product);
        return ServerResponse.createBySuccess(productDetailVo);

    }

    //组装vo
    private ProductDetailVo assembleProductVo(Product product){
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setName(product.getName());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setId(product.getId());

        //imageHost
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category == null){
            productDetailVo.setParentCategoryId(0);
        }else{
            productDetailVo.setParentCategoryId(category.getId());
        }
        //createTIme
        productDetailVo.setCreateTime(DateTimeUtils.DateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtils.DateToStr(product.getUpdateTime()));
        return productDetailVo;

    }

    @Override
    public ServerResponse getProductList(Integer pageNum,Integer pageSize){
        //一定要startPage-start
        //填充自己的sql
        //pagehelper-收尾
        //建立分页
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.selectList();
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(Product productItem :productList){
            ProductListVo productListVo =assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccess(pageResult);
    }

    private ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setId(product.getId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setName(product.getName());
        productListVo.setPrice(product.getPrice());
        productListVo.setStatus(product.getStatus());
        productListVo.setSubtitle(product.getSubtitle());
        return productListVo;

    }

    @Override
    public ServerResponse<PageInfo> searchProduct(Integer productId,String productName,Integer pageNum,Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        if(StringUtils.isNotBlank(productName)){
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.selectByNameAndProductId(productName,productId);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productItem: productList) {
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccess(pageResult);

    }

    @Override
    public ServerResponse<ProductDetailVo> productDetail(Integer productId){
        if(productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }
        if(product.getStatus().equals(Const.ProductStatusEnum.ON_SALE.getCode())){
            return ServerResponse.createByErrorMessage("产品已下架");
        }
        //vo对象
        ProductDetailVo productDetailVo =assembleProductVo(product);
        return ServerResponse.createBySuccess(productDetailVo);

    }
//分页返回pageInfo
    @Override
    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyWord,Integer categoryId,Integer pageSize,Integer pageNum,String orderBy){
        if(StringUtils.isBlank(keyWord)&&categoryId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"非法参数");
        }
        List<Integer> categoryIdList = new ArrayList<>();
        if(categoryId!=null){
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if(category == null && StringUtils.isBlank(keyWord)){
                //没有命中数据
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVo> productListVoList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVoList);
                return ServerResponse.createBySuccess(pageInfo);
            }
            categoryIdList = iCategoryService.selectCategoryAndChildrenById(category.getId()).getData();
        }
        if(StringUtils.isNotBlank(keyWord)){
            keyWord = new StringBuilder().append("%").append(keyWord).append("%").toString();
        }
        PageHelper.startPage(pageNum,pageSize);
        //排序
        if(StringUtils.isNotBlank(orderBy)){
            if (Const.ProductListOrderBy.PRICE_DESC_ASC.contains(orderBy)) {
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0]+" "+orderByArray[1]);
            }
        }
        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyWord)?null:keyWord,categoryIdList.size()==0?null:categoryIdList);
        //把product封装成vo
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product item: productList
             ) {
            productListVoList.add(assembleProductListVo(item));
        }
        //开始分页
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
