package com.denizshopping.ecommerce.mapper;

import com.denizshopping.ecommerce.config.CentralMapperConfig;
import com.denizshopping.ecommerce.dto.CategoryDto;
import com.denizshopping.ecommerce.entity.Category;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface CategoryMapper extends BaseMapper<CategoryDto, Category> {
}