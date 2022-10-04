package com.gosari.repick_project.offlineshopping;

import com.gosari.repick_project.exception.DataNotFoundException;
import com.gosari.repick_project.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class OfflineService {

    private final OfflineRepository offlineRepository;

    public Page<OfflineShop> getList(Pageable pageable) {
        return offlineRepository.findAll(pageable);
    }

    //getOfflineShop 데이터 데이터가 없을 경우에는 DataNotFoundException을 발생
    public OfflineShop getOfflineShop(Integer id) {
        Optional<OfflineShop> offlineshop = this.offlineRepository.findById(id);
        if (offlineshop.isPresent()) {
            return offlineshop.get();
        } else {
            throw new DataNotFoundException("OfflineShop not found");
        }
    }

    public OfflineShop create(String ShopName, String ShopNumber,
                       String ShopTime, String ShopAddress, String ShopContents,
                       SiteUser user) {
        OfflineShop off = new OfflineShop();
        off.setShopName(ShopName);
        off.setShopNumber(ShopNumber);
        off.setShopTime(ShopTime);
        off.setShopAddress(ShopAddress);
        off.setShopContents(ShopContents);
        off.setAuthor(user);
        this.offlineRepository.save(off);
        return off;
    }

    public void modify(OfflineShop offlineShop,
                       String ShopName, String ShopNumber,
                       String ShopTime, String ShopAddress, String ShopContents) {

        offlineShop.setShopName(ShopName);
        offlineShop.setShopNumber(ShopNumber);
        offlineShop.setShopTime(ShopTime);
        offlineShop.setShopContents(ShopContents);
        offlineShop.setShopAddress(ShopAddress);
        offlineShop.setModifyDate(new Date());
        this.offlineRepository.save(offlineShop);
    }

    public void delete(OfflineShop offlineShop) {
        this.offlineRepository.delete(offlineShop);
    }
}
