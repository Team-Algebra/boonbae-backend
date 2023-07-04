package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.funding.FirstCategory;
import com.agebra.boonbaebackend.domain.funding.SecondCategory;
import com.agebra.boonbaebackend.dto.CategoryDto;
import com.agebra.boonbaebackend.exception.CategoryDuplicateException;
import com.agebra.boonbaebackend.exception.NotFoundException;
import com.agebra.boonbaebackend.repository.FirstCategoryRepository;
import com.agebra.boonbaebackend.repository.SecondCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class CategoryService {
  private final SecondCategoryRepository secondCategoryRepository;
  private final FirstCategoryRepository firstCategoryRepository;

  public void addSecondCategory(CategoryDto.Add dto) throws NotFoundException, CategoryDuplicateException {
    FirstCategory firstCategory = firstCategoryRepository.findById(dto.getFirst_category_id())
      .orElseThrow(() -> new NotFoundException("해당하는 첫 번째 카테고리가 없습니다"));

    SecondCategory findSecond = secondCategoryRepository.findByFirstCategoryAndName(firstCategory, dto.getName())
      .orElseGet(() -> null);
    if (findSecond != null)
      throw new CategoryDuplicateException("같은 이름의 두 번째 카테고리가 존재합니다");

    SecondCategory newSecond = SecondCategory.builder()
      .name(dto.getName())
      .firstCategory(firstCategory)
      .build();

    secondCategoryRepository.save(newSecond);
  }

  public void addFirstCategory(String name) throws CategoryDuplicateException {
    FirstCategory firstCategory = firstCategoryRepository.findByName(name)
      .orElseGet(() -> null);
    if (firstCategory != null)
      throw new CategoryDuplicateException("같은 이름의 첫 번째 카테고리가 존재합니다");

    FirstCategory newFirstCategory = FirstCategory.builder()
      .name(name)
      .build();

    firstCategoryRepository.save(newFirstCategory);
  }

  public CategoryDto.All getAll() {
    List<FirstCategory> firstCategories = firstCategoryRepository.findAll();

    List<CategoryDto.FirstCategoryDto> firstCategoryDtos = new ArrayList<>();

    for (FirstCategory firstCategory: firstCategories) {
      List<SecondCategory> secondCategories = secondCategoryRepository.findAllByFirstCategory(firstCategory);

      List<CategoryDto.SecondCategoryDto> secondDtoList = secondCategories.stream().map(secondCategory -> {
        return new CategoryDto.SecondCategoryDto(secondCategory.getPk(), secondCategory.getName());
      }).toList();

      CategoryDto.FirstCategoryDto firstCategoryDto = CategoryDto.FirstCategoryDto.builder()
        .second_category(secondDtoList)
        .second_category_cnt(secondDtoList.size())
        .name(firstCategory.getName())
        .build();

      firstCategoryDtos.add(firstCategoryDto);
    }

    return CategoryDto.All.builder()
      .first_category_cnt(firstCategories.size())
      .first_category(firstCategoryDtos)
      .build();
  }
}
