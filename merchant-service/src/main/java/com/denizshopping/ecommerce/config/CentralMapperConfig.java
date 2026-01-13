package com.denizshopping.ecommerce.config;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
        componentModel = "spring",               // Her mapper otomatik @Component olur
        unmappedTargetPolicy = ReportingPolicy.IGNORE // Eşleşmeyen alanları dert etme
)
public interface CentralMapperConfig {
}