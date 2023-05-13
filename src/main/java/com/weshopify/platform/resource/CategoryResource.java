package com.weshopify.platform.resource;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.weshopify.platform.bean.CategoryBean;
import com.weshopify.platform.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
public class CategoryResource {

	private CategoryService catService;

	public CategoryResource(CategoryService catService) {
		this.catService = catService;
	}

	@Operation(summary = "findAllCategories", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/categories")
	public ResponseEntity<List<CategoryBean>> findAllCategories() {
		return ResponseEntity.ok(catService.findAllCategories());

	}

	@Operation(summary = "findChildCategories", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/categories/childs/{parentId}")
	public ResponseEntity<List<CategoryBean>> findChildCategories(@PathVariable("parentId") int parentId) {
		return ResponseEntity.ok(catService.findAllChilds(parentId));

	}

	@Operation(summary = "createCategory", security = @SecurityRequirement(name = "bearerAuth"))
	@PostMapping("/categories")
	public ResponseEntity<CategoryBean> createCategory(@RequestBody CategoryBean catBean) {
		return ResponseEntity.ok(catService.createCategory(catBean));
	}

	@Operation(summary = "updateCategory", security = @SecurityRequirement(name = "bearerAuth"))
	@PutMapping("/categories")
	public ResponseEntity<CategoryBean> updateCategory(@RequestBody CategoryBean catBean) {
		return ResponseEntity.ok(catService.updateCategory(catBean));
	}

	@Operation(summary = "deleteCategory", security = @SecurityRequirement(name = "bearerAuth"))
	@DeleteMapping("/categories/{catId}")
	public ResponseEntity<List<CategoryBean>> deleteCategory(@PathVariable("catId") int catId) {
		return ResponseEntity.ok(catService.deleteCategory(catId));
	}

	@Operation(summary = "getCategoryById", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/categories/{catId}")
	public ResponseEntity<CategoryBean> getCategoryById(@PathVariable("catId") int catId) {
		log.info("get category by caegory id is invoking");
		return ResponseEntity.ok(catService.findCategoryById(catId));
	}

}
