package com.mall.dao;

import com.mall.pojo.Category;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    /**
     * 根据父节点选择子节点（平级)
     * @param categoryId
     * @return
     */
    List<Category> getChildrenCategoryByParentId(Integer categoryId);
}