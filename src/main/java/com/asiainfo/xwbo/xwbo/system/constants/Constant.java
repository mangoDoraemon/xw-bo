package com.asiainfo.xwbo.xwbo.system.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jiahao jin
 * @create 2020-05-06 14:38
 */
public  class Constant {

    public static final String SESSION_KEY = "_XW_USER";

    public interface XW_USER_ROLE {
        Integer SUPERINTENDENT = 0;
        Integer ZHIXIAORENYUAN = 1;
        Integer WANGGEZHANG = 2;
        Integer WANGGEDUDAO = 3;

        Map<Integer, String> MAPPER = new HashMap<Integer, String>(){
            {
                put(SUPERINTENDENT, "管理人员");
                put(ZHIXIAORENYUAN, "直销人员");
                put(WANGGEZHANG, "网格长");
                put(WANGGEDUDAO, "网格督导");
            }
        };

    }

    public interface XW_AREA_LEVEL {
        Integer PROV = 1;
        Integer CITY = 2;
        Integer COUNTY = 3;
        Integer GRID = 4;
        Integer MICRO = 72;
        Map<Integer, String> MAPPER = new HashMap<Integer, String>(){
            {
                put(PROV, "prov_id");
                put(CITY, "city_id");
                put(COUNTY, "county_id");
                put(GRID, "grid_id");
                put(MICRO, "micro_Id");
            }
        };
    }

    public interface XW_GROUP_HANDLE_STATE {
        Integer DAIPAIMO = 1;	// 待摸排  还未有直销人员去实地确认过的企业状态
        Integer PAIMOZHONG = 2;	// 摸排中  当前有直销人员在实地摸排中的企业状态
        Integer YIPAIMO = 3;	    // 已摸排  已摸排完但未办理产品的企业
        Integer SHANGJI = 4;	    // 商机    根据企业画像可以关联上推荐产品的企业
        Integer ZAIWANG = 5;	    // 在网    已办理过业务产品的企业
        Map<Integer, String> MAPPER = new HashMap<Integer, String>(){
            {
                put(DAIPAIMO, "待摸排");
                put(PAIMOZHONG, "摸排中");
                put(YIPAIMO, "已摸排");
                put(SHANGJI, "商机");
                put(ZAIWANG, "在网");
            }
        };
    }

    public interface XW_GROUP_MANAGEMENT_STATE {
        Integer XUCUN = 1;	    //存续
        Integer ZAIYE = 2;	    //在业
        Integer DIAOXIAO = 3;	//吊销
        Integer ZHUXIAO = 4;	    //注销
        Integer QIANRU = 5;	    //迁入
        Integer QIANCHU = 6;	    //迁出
        Integer TINGYE = 7;	    //停业
        Integer QINGSUAN = 8;	//清算
        Map<Integer, String> MAPPER = new HashMap<Integer, String>(){
            {
                put(XUCUN, "存续");
                put(ZAIYE, "在业");
                put(DIAOXIAO, "吊销");
                put(ZHUXIAO, "注销");
                put(QIANRU, "迁入");
                put(QIANCHU, "迁出");
                put(TINGYE, "停业");
                put(QINGSUAN, "清算");
            }
        };
    }
}