package com.weshopify.platform.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.RandomStringUtils;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.weshopify.platform.bean.CategoryBean;
import com.weshopify.platform.cqrs.commands.CategoryCommand;
import com.weshopify.platform.model.Category;
import com.weshopify.platform.repo.CategoriesRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

	private CategoriesRepository catRepo;
	
	@Autowired
	private CommandGateway commandBus;

	CategoryServiceImpl(CategoriesRepository catRepo) {
		this.catRepo = catRepo;
	}

	@Override
	public CategoryBean createCategory(CategoryBean catBean) {
		return convertEntityToBean(catRepo.save(convertBeanToEntity(catBean)));
	}

	@Override
	public CategoryBean updateCategory(CategoryBean catBean) {
		catBean = convertEntityToBean(catRepo.save(convertBeanToEntity(catBean)));
		
		CategoryCommand catCommand = createCommand(catBean);
		
		log.info("step-1: Command sending to the Command Handler");
		CompletableFuture<CategoryCommand> future = commandBus.send(catCommand);
		if(future.isDone()) {
			log.info("category updates were delivered to consumer services");
		}else {
			log.error("category updates were not delivered to the consumers. they may retry the event store");
		}
		return catBean;
	}

	@Override
	public List<CategoryBean> findAllCategories() {
		List<Category> catEntityList = catRepo.findAll();
		if (!CollectionUtils.isEmpty(catEntityList)) {
			List<CategoryBean> catBeanList = new ArrayList<>();
			catEntityList.stream().forEach(catEntity -> {
				catBeanList.add(convertEntityToBean(catEntity));
			});
			return catBeanList;
		} else {
			throw new RuntimeException("No Categories Found in Database");
		}
	}

	@Override
	public CategoryBean findCategoryById(int catId) {
		log.info("in category service-->findCategoryById ");
		return convertEntityToBean(catRepo.findById(catId).get());
	}

	@Override
	public List<CategoryBean> findAllChilds(int parentId) {
		List<Category> catEntityList =  catRepo.findChildsOfAParent(parentId);
		if (!CollectionUtils.isEmpty(catEntityList)) {
			List<CategoryBean> catBeanList = new ArrayList<>();
			catEntityList.stream().forEach(catEntity -> {
				catBeanList.add(convertEntityToBean(catEntity));
			});
			return catBeanList;
		} else {
			throw new RuntimeException("No Child Categories Found For the Parent:\t"+parentId);
		}
	}

	@Override
	public List<CategoryBean> deleteCategory(int catId) {
		catRepo.deleteById(catId);
		return findAllCategories();
	}

	/**
	 * converting the bean to entity model
	 * 
	 * @param catBean
	 * @return
	 */
	private Category convertBeanToEntity(CategoryBean catBean) {
		Category catEntity = new Category();
		catEntity.setAlias(catBean.getAlias());
		catEntity.setEnabled(true);

		// catEntity.setImagePath(catBean.get);
		catEntity.setName(catBean.getName());
		if (catBean.getPcategory() > 0) {
			catEntity.setParent(catRepo.findById(catBean.getPcategory()).get());
		}

		// for update operation
		if (catBean.getId() > 0) {
			log.info("updating the entity");
			catEntity.setId(catBean.getId());
		}

		return catEntity;
	}

	/**
	 * converting the entity model to bean
	 * 
	 * @param catBean
	 * @return
	 */
	private CategoryBean convertEntityToBean(Category catEntity) {
		CategoryBean catBean = new CategoryBean();
		catBean.setAlias(catEntity.getAlias());
		catBean.setName(catEntity.getName());
	    catBean.setEnabled(catEntity.isEnabled());
	    catBean.setId(catEntity.getId());
	    if(catEntity.getParent() != null) {
	    	catBean.setPcategory(catEntity.getParent().getId());
	    }
	    
	    return catBean;
	}
	
	/**
	 * Converting the updated bean to the command
	 * @param catBean
	 * @return
	 */
	private CategoryCommand createCommand(CategoryBean catBean) {
		CategoryCommand catCommand = new CategoryCommand();
		catCommand.setAlias(catBean.getAlias());
		catCommand.setName(catBean.getName());
		catCommand.setEnabled(catBean.isEnabled());
		String randomId = RandomStringUtils.randomAlphanumeric(17).toUpperCase();
		catCommand.setEventId(randomId);
		catCommand.setId(catBean.getId());
		catCommand.setPcategory(catBean.getPcategory());
	    return catCommand;
	}

}
