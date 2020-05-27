package com.asiainfo.xwbo.xwbo.system;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.xwbo.xwbo.dao.ICommonExtDao;
import com.asiainfo.xwbo.xwbo.dao.sqlBuild.SqlBuilder;
import com.asiainfo.xwbo.xwbo.model.po.XwGroupManagementStateInfoPo;
import com.asiainfo.xwbo.xwbo.model.vo.XwGroupManagementStateInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author jiahao jin
 * @create 2020-05-08 17:46
 */
@Component
@Slf4j
@DependsOn({"beanFactory"})
public class XwGroupManagementStateInfoLoader {
    @Resource
    private ICommonExtDao commonExtDao;

    private Map<Integer, XwGroupManagementStateInfoPo> map = new ConcurrentHashMap<>();

    private List<XwGroupManagementStateInfoVo> list = new ArrayList<>();

    @PostConstruct
    public void init() {
        map.clear();
        list.clear();
        List<XwGroupManagementStateInfoPo> xwGroupManagementStateInfoPoList = commonExtDao.query(SqlBuilder.build(XwGroupManagementStateInfoPo.class));
        if(null != xwGroupManagementStateInfoPoList && xwGroupManagementStateInfoPoList.size() > 0) {
            map = xwGroupManagementStateInfoPoList.stream().collect(Collectors.toMap(XwGroupManagementStateInfoPo::getId, xwGroupManagementStateInfoPo -> xwGroupManagementStateInfoPo));
            list = xwGroupManagementStateInfoPoList.stream().map(po -> XwGroupManagementStateInfoPoToVo(po)).collect(Collectors.toList());
            log.info("初始化经营状态： "+JSONObject.toJSONString(map));
        }

    }

    public XwGroupManagementStateInfoPo get(Integer id) {
        XwGroupManagementStateInfoPo po = map.get(id);
//        if(null == po) {
//            update();
//        }
        return map.get(id);
    }

    public void update() {
        init();

    }

    private XwGroupManagementStateInfoVo XwGroupManagementStateInfoPoToVo(XwGroupManagementStateInfoPo po) {
        XwGroupManagementStateInfoVo vo = new XwGroupManagementStateInfoVo();
        vo.setId(po.getId());
        vo.setMessage(po.getMessage());
        return vo;
    }

    public List<XwGroupManagementStateInfoVo> getList() {
        return list;
    }
}
