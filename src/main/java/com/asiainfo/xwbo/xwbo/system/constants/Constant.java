package com.asiainfo.xwbo.xwbo.system.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jiahao jin
 * @create 2020-05-06 14:38
 */
public class Constant {

    public interface XW_USER_ROLE_ID {
        public Integer SUPERINTENDENT = 0;
        public Integer ZHIXIAORENYUAN = 1;
        public Integer WANGGEZHANG = 2;
        public Integer WANGGEDUTAO = 3;
    }

    public interface XW_GROUP_HANDLE_STATE {
        public Integer DAIPAIMO = 1;	// 待摸排  还未有直销人员去实地确认过的企业状态
        public Integer PAIMOZHONG = 2;	// 摸排中  当前有直销人员在实地摸排中的企业状态
        public Integer YIPAIMO = 3;	    // 已摸排  已摸排完但未办理产品的企业
        public Integer SHANGJI = 4;	    // 商机    根据企业画像可以关联上推荐产品的企业
        public Integer ZAIWANG = 5;	    // 在网    已办理过业务产品的企业
        public Map<Integer, String> MAPPER = new HashMap<Integer, String>(){
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
        public Integer XUCUN = 1;	    //存续
        public Integer ZAIYE = 2;	    //在业
        public Integer DIAOXIAO = 3;	//吊销
        public Integer ZHUXIAO = 4;	    //注销
        public Integer QIANRU = 5;	    //迁入
        public Integer QIANCHU = 6;	    //迁出
        public Integer TINGYE = 7;	    //停业
        public Integer QINGSUAN = 8;	//清算
        public Map<Integer, String> MAPPER = new HashMap<Integer, String>(){
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