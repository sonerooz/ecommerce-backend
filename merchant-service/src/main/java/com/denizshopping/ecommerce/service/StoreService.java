package com.denizshopping.ecommerce.service;

import com.denizshopping.ecommerce.dto.StoreDto;
import com.denizshopping.ecommerce.entity.Store;
import com.denizshopping.ecommerce.mapper.StoreMapper;
import com.denizshopping.ecommerce.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;

    @Transactional
    public List<StoreDto> getAllStores() {
        return storeMapper.toDtoList(storeRepository.findAll());
    }

    @Transactional
    public StoreDto createStore(StoreDto storeDto) {
        Store store = storeMapper.toEntity(storeDto);
        // İş kuralları buraya gelebilir (Örn: Vergi no kontrolü)
        Store savedStore = storeRepository.save(store);
        return storeMapper.toDto(savedStore);
    }

    @Transactional
    public StoreDto getStoreById(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        return storeMapper.toDto(store);
    }
}