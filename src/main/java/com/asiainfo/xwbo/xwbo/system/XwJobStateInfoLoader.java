package com.asiainfo.xwbo.xwbo.system;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.xwbo.xwbo.dao.ICommonExtDao;
import com.asiainfo.xwbo.xwbo.dao.sqlBuild.SqlBuilder;
import com.asiainfo.xwbo.xwbo.model.po.XwGroupManagementStateInfoPo;
import com.asiainfo.xwbo.xwbo.model.po.XwJobStateInfoPo;
import com.asiainfo.xwbo.xwbo.model.vo.XwGroupManagementStateInfoVo;
import com.asiainfo.xwbo.xwbo.model.vo.XwJobStateInfoVo;
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
 * @create 2020-05-20 15:46
 */
@Component
@Slf4j
@DependsOn({"beanFactory"})
public class XwJobStateInfoLoader {
    @Resource
    private ICommonExtDao commonExtDao;

    private Map<Integer, XwJobStateInfoPo> map = new ConcurrentHashMap<>();

    private List<XwJobStateInfoVo> list = new ArrayList<>();

    @PostConstruct
    public void init() {
        map.clear();
        list.clear();
        List<XwJobStateInfoPo> xwJobStateInfoPoList = commonExtDao.query(SqlBuilder.build(XwJobStateInfoPo.class));
        if(null != xwJobStateInfoPoList && xwJobStateInfoPoList.size() > 0) {
            map = xwJobStateInfoPoList.stream().collect(Collectors.toMap(XwJobStateInfoPo::getId, xwJobStateInfoPo -> xwJobStateInfoPo));
            list = xwJobStateInfoPoList.stream().map(po -> XwJobStateInfoPoToVo(po)).collect(Collectors.toList());
            log.info("初始化工单状态： "+ JSONObject.toJSONString(map));
        }

    }

    public XwJobStateInfoPo get(Integer id) {
        XwJobStateInfoPo po = map.get(id);
//        if(null == po) {
//            update();
//        }
        return map.get(id);
    }

    public void update() {
        init();

    }

    private XwJobStateInfoVo XwJobStateInfoPoToVo(XwJobStateInfoPo po) {
        XwJobStateInfoVo vo = new XwJobStateInfoVo();
        vo.setId(po.getId());
        vo.setMessage(po.getMessage());
        return vo;
    }

    public List<XwJobStateInfoVo> getList() {
        return list;
    }

}
