package com.denizshopping.ecommerce.mapper;

import com.denizshopping.ecommerce.config.CentralMapperConfig;
import com.denizshopping.ecommerce.dto.StoreDto;
import com.denizshopping.ecommerce.entity.Store;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface StoreMapper extends BaseMapper<StoreDto, Store> {
}