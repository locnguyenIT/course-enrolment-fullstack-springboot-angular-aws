package com.example.demo.category;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getListCategory() {
        return categoryRepository.findAll();
    }

    public void addCategory(Category category) {
        //1. check if the name of the category is already in the database
        Optional<Category> categoryByName = categoryRepository.findByName(category.getName());
        if(categoryByName.isPresent()){
          throw new BadRequestException(String.format("Category %s already used", category.getName()));
        }
        //2. Save
        categoryRepository.save(category);
    }

    public void deleteCategory(Integer categoryId) {
        //1. check if the category is exists
        boolean existsCategory = categoryRepository.existsById(categoryId);
        if(!existsCategory){
            throw new NotFoundException(String.format("Category with id %d was not found",categoryId));
        }
        //2. Delete
        categoryRepository.deleteById(categoryId);
    }

    public void updateCategory(Category category) {
        //1. check if the name of the category is already in the database
        Optional<Category> categoryByName = categoryRepository.findByName(category.getName());
        if(categoryByName.isPresent()){
            throw new BadRequestException(String.format("Category %s already used", category.getName()));
        }
        //2. Update
        categoryRepository.save(category);
    }
}
