package com.denizshopping.ecommerce.mapper;

import com.denizshopping.ecommerce.config.CentralMapperConfig;
import com.denizshopping.ecommerce.dto.UserDto;
import com.denizshopping.ecommerce.entity.User;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface UserMapper extends BaseMapper<UserDto, User> {
}