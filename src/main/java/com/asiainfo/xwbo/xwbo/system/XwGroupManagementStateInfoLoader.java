package com.asiainfo.xwbo.xwbo.system;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.xwbo.xwbo.dao.ICommonExtDao;
import com.asiainfo.xwbo.xwbo.dao.sqlBuild.SqlBuilder;
import com.asiainfo.xwbo.xwbo.model.po.XwGroupManagementStateInfoPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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

    private Map<Long, XwGroupManagementStateInfoPo> map = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        List<XwGroupManagementStateInfoPo> xwGroupManagementStateInfoPoList = commonExtDao.query(SqlBuilder.build(XwGroupManagementStateInfoPo.class));
        if(null != xwGroupManagementStateInfoPoList && xwGroupManagementStateInfoPoList.size() > 0) {
            map = xwGroupManagementStateInfoPoList.stream().collect(Collectors.toMap(XwGroupManagementStateInfoPo::getId, xwGroupManagementStateInfoPo -> xwGroupManagementStateInfoPo));
            log.info("初始化经营状态： "+JSONObject.toJSONString(map));
        }

    }

    public XwGroupManagementStateInfoPo get(Long id) {
        XwGroupManagementStateInfoPo po = map.get(id);
        if(null == po) {
            update(id);
        }
        return map.get(id);
    }

    public void update(Long id) {
        XwGroupManagementStateInfoPo po = commonExtDao.queryForObject(SqlBuilder.build(XwGroupManagementStateInfoPo.class).eq("id", id));
        map.put(id, po);
    }
}
