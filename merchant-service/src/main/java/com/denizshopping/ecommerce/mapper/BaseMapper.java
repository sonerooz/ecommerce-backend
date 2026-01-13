package com.denizshopping.ecommerce.mapper;

import java.util.List;

/**
 * @param <D> DTO (Data Transfer Object) - Genelde Record
 * @param <E> Entity (Veritabanı Nesnesi)
 */
public interface BaseMapper<D, E> {

    D toDto(E entity);

    E toEntity(D dto);

    List<D> toDtoList(List<E> entityList);

    List<E> toEntityList(List<D> dtoList);
}