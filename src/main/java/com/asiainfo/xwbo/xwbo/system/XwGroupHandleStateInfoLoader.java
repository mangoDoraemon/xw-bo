package com.asiainfo.xwbo.xwbo.system;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.xwbo.xwbo.dao.ICommonExtDao;
import com.asiainfo.xwbo.xwbo.dao.sqlBuild.SqlBuilder;
import com.asiainfo.xwbo.xwbo.model.po.XwGroupHandleStateInfoPo;
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
public class XwGroupHandleStateInfoLoader {
    @Resource
    private ICommonExtDao commonExtDao;

    private Map<Long, XwGroupHandleStateInfoPo> map = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        List<XwGroupHandleStateInfoPo> xwGroupHandleStateInfoPoList = commonExtDao.query(SqlBuilder.build(XwGroupHandleStateInfoPo.class));
        if(null != xwGroupHandleStateInfoPoList && xwGroupHandleStateInfoPoList.size() > 0) {
            map = xwGroupHandleStateInfoPoList.stream().collect(Collectors.toMap(XwGroupHandleStateInfoPo::getId, xwGroupHandleStateInfoPo -> xwGroupHandleStateInfoPo));
            log.info("初始化排摸状态： "+JSONObject.toJSONString(map));
        }

    }

    public XwGroupHandleStateInfoPo get(Long id) {
        XwGroupHandleStateInfoPo po = map.get(id);
        if(null == po) {
            update(id);
        }
        return map.get(id);
    }

    public void update(Long id) {
        XwGroupHandleStateInfoPo po = commonExtDao.queryForObject(SqlBuilder.build(XwGroupHandleStateInfoPo.class).eq("id", id));
        map.put(id, po);
    }
}
