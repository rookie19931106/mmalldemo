package com.mall.controller.portal;

import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.ICartService;
import com.mall.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author jay
 */
@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;

    @RequestMapping("addCart.do")
    @ResponseBody
    public ServerResponse<CartVO> add(HttpSession session, Integer count, Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
    return iCartService.add(user.getId(),count,productId);
    }

    @RequestMapping("updateCart.do")
    @ResponseBody
    public ServerResponse<CartVO> update(HttpSession session, Integer count, Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        return iCartService.update(user.getId(),count,productId);
    }

    @RequestMapping("deleteProduct.do")
    @ResponseBody
    public ServerResponse<CartVO> deleteProduct(HttpSession session,String productIds){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        return iCartService.deleteProduct(user.getId(),productIds);
    }

    @RequestMapping("listProduct.do")
    @ResponseBody
    public ServerResponse<CartVO> list(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        return iCartService.list(user.getId());
    }

    @RequestMapping("checkedAll.do")
    @ResponseBody
    public ServerResponse<CartVO> selectAll(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        return iCartService.selectAllOrUnSelectAll(user.getId(),Const.CartConst.CHECKED,null);
    }

    @RequestMapping("UncheckedAll.do")
    @ResponseBody
    public ServerResponse<CartVO> UnSelectAll(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        return iCartService.selectAllOrUnSelectAll(user.getId(),Const.CartConst.UNCHECKED,null);
    }

    @RequestMapping("Unchecked.do")
    @ResponseBody
    public ServerResponse<CartVO> UnSelect(HttpSession session,Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        return iCartService.selectAllOrUnSelectAll(user.getId(),Const.CartConst.UNCHECKED,productId);
    }

    @RequestMapping("checked.do")
    @ResponseBody
    public ServerResponse<CartVO> Select(HttpSession session,Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        return iCartService.selectAllOrUnSelectAll(user.getId(),Const.CartConst.CHECKED,productId);
    }

    @RequestMapping("selectProductCount.do")
    @ResponseBody
    public ServerResponse<Integer> selectProductCount(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createBySuccess(0);
        }
        return iCartService.selectProductCount(user.getId());
    }
}
