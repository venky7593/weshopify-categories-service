package com.weshopify.platform.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.weshopify.platform.model.Category;

public interface CategoriesRepository extends JpaRepository<Category, Integer> {
	
	@Query(name = "findChildsOfAParent", value = "from Category cat where cat.parent.id=:patId")
	public List<Category> findChildsOfAParent(@Param("patId") int parentId);
}
