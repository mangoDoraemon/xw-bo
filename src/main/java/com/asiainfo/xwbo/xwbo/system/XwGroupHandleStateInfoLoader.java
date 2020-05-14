package com.asiainfo.xwbo.xwbo.system;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.xwbo.xwbo.dao.ICommonExtDao;
import com.asiainfo.xwbo.xwbo.dao.sqlBuild.SqlBuilder;
import com.asiainfo.xwbo.xwbo.model.po.XwGroupHandleStateInfoPo;
import com.asiainfo.xwbo.xwbo.model.vo.XwGroupHandleStateInfoVo;
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
public class XwGroupHandleStateInfoLoader {
    @Resource
    private ICommonExtDao commonExtDao;

    private Map<Integer, XwGroupHandleStateInfoPo> map = new ConcurrentHashMap<>();

    private List<XwGroupHandleStateInfoVo> list = new ArrayList<>();

    @PostConstruct
    public void init() {
        map.clear();
        list.clear();
        List<XwGroupHandleStateInfoPo> xwGroupHandleStateInfoPoList = commonExtDao.query(SqlBuilder.build(XwGroupHandleStateInfoPo.class));
        if(null != xwGroupHandleStateInfoPoList && xwGroupHandleStateInfoPoList.size() > 0) {
            map = xwGroupHandleStateInfoPoList.stream().collect(Collectors.toMap(XwGroupHandleStateInfoPo::getId, xwGroupHandleStateInfoPo -> xwGroupHandleStateInfoPo));
            list = xwGroupHandleStateInfoPoList.stream().map(po -> XwGroupHandleStateInfoPoToVo(po)).collect(Collectors.toList());
            log.info("初始化排摸状态： "+JSONObject.toJSONString(map));
        }

    }

    private XwGroupHandleStateInfoVo XwGroupHandleStateInfoPoToVo(XwGroupHandleStateInfoPo po) {

        XwGroupHandleStateInfoVo vo = new XwGroupHandleStateInfoVo();
        vo.setId(po.getId());
        vo.setMessage(po.getMessage());
        return vo;
    }

    public XwGroupHandleStateInfoPo get(Integer id) {
        if(null == id) {
            return null;
        }
        XwGroupHandleStateInfoPo po = map.get(id);
        if(null == po) {
            update();
        }
        return map.get(id);
    }

    public void update() {
        init();
    }

    public List<XwGroupHandleStateInfoVo> getList() {
        return list;
    }
}
