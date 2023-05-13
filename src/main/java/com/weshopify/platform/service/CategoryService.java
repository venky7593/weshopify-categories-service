package com.weshopify.platform.service;

import java.util.List;

import com.weshopify.platform.bean.CategoryBean;

public interface CategoryService {

	CategoryBean createCategory(CategoryBean catBean);
	CategoryBean updateCategory(CategoryBean catBean);
	
	List<CategoryBean> findAllCategories();
	CategoryBean findCategoryById(int catId);
	
	List<CategoryBean> findAllChilds(int parentId);
	List<CategoryBean> deleteCategory(int catId);
	
}
