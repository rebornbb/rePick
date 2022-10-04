package com.gosari.repick_project.offlineshopping;

import com.gosari.repick_project.user.SiteUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
public class OfflineShop {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String ShopName;

    private String ShopNumber;

    private String ShopTime;

    private String ShopAddress;

    private String ShopContents;

    @Column(insertable = false, updatable = false, columnDefinition = "date default LOCALTIMESTAMP")
    private Date createDate;

    @Column(insertable = true, updatable = true, columnDefinition = "date default LOCALTIMESTAMP")
    private Date modifyDate;

    @ManyToOne //여러개의 글이 한명의 사람에게 작성될수있으므로 @ManyToOne
    private SiteUser author;
}
