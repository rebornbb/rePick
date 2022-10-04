package com.gosari.repick_project.offlineshopping;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfflineRepository extends JpaRepository<OfflineShop, Integer> {
    Page<OfflineShop> findAll(Pageable pageable);
}
