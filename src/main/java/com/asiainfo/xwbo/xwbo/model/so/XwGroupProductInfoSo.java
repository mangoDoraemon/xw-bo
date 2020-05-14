package com.asiainfo.xwbo.xwbo.model.so;

import lombok.Data;

/**
 * @author jiahao jin
 * @create 2020-05-14 10:27
 */
@Data
public class XwGroupProductInfoSo {
    private Long groupId;
    private Integer productId;
    private String productName;
    private String productType;
    private String productDesc;

}
