package com.denizshopping.ecommerce.mapper;

import com.denizshopping.ecommerce.config.CentralMapperConfig;
import com.denizshopping.ecommerce.dto.ProductDto;
import com.denizshopping.ecommerce.entity.Product;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface ProductMapper extends BaseMapper<ProductDto, Product> {
}