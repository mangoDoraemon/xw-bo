package com.asiainfo.xwbo.xwbo.system;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.xwbo.xwbo.dao.ICommonExtDao;
import com.asiainfo.xwbo.xwbo.dao.sqlBuild.SqlBuilder;
import com.asiainfo.xwbo.xwbo.model.vo.XwIndustryClassInfoVo;
import com.asiainfo.xwbo.xwbo.model.po.XwIndustryClassInfoPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author jiahao jin
 * @create 2020-05-08 17:19
 */
@Component
@Slf4j
@DependsOn({"beanFactory"})
public class XwIndustryClassInfoLoader {

    @Resource
    private ICommonExtDao commonExtDao;

    private Map<Integer, XwIndustryClassInfoPo> keyMap = new ConcurrentHashMap<>();

    private List<XwIndustryClassInfoVo> headerList = new CopyOnWriteArrayList<>();

    private Map<Integer, XwIndustryClassInfoVo> indexMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        keyMap.clear();
        headerList.clear();
        indexMap.clear();
        List<XwIndustryClassInfoPo> xwindustryClassInfoPoList = commonExtDao.query(SqlBuilder.build(XwIndustryClassInfoPo.class).
                setOrder("id", "asc")
                .setOrder("pid", "asc")
                .setOrder("level", "asc"));
        if(null != xwindustryClassInfoPoList && xwindustryClassInfoPoList.size() > 0) {
            for(XwIndustryClassInfoPo po : xwindustryClassInfoPoList) {
                Integer id = po.getId();
                keyMap.put(id, po);

                XwIndustryClassInfoVo xwIndustryClassInfoVo = xwIndustryClassInfoPoToVo(po);
                indexMap.put(id, xwIndustryClassInfoVo);
                Integer pid = po.getPid();
                if(null == pid) {
                    headerList.add(xwIndustryClassInfoVo);

                }else {
                    XwIndustryClassInfoVo pVo = indexMap.get(pid);
                    if(null == pVo.getChild()) {
                        pVo.setChild(new ArrayList<>());
                    }
                    List<XwIndustryClassInfoVo> child = pVo.getChild();
                    child.add(xwIndustryClassInfoVo);
                }
            }
            log.info("初始化行业分类： "+JSONObject.toJSONString(keyMap));
            log.info("初始化行业列表： "+JSONObject.toJSONString(headerList));


        }

    }

    public XwIndustryClassInfoPo get(Integer id) {
        if(null == id) {
            return null;
        }
        XwIndustryClassInfoPo po = keyMap.get(id);
        if(null == po) {
            update();
        }
        return keyMap.get(id);
    }

    public void update() {
        init();
    }

    public Map<Integer, XwIndustryClassInfoPo> getMap() {
        return keyMap;
    }

    public List<XwIndustryClassInfoVo> getHeaderList() {
        return headerList;
    }

    private XwIndustryClassInfoVo xwIndustryClassInfoPoToVo(XwIndustryClassInfoPo xwIndustryClassInfoPo) {
        XwIndustryClassInfoVo xwIndustryClassInfoVo = new XwIndustryClassInfoVo();
        xwIndustryClassInfoVo.setId(xwIndustryClassInfoPo.getId());
        xwIndustryClassInfoVo.setName(xwIndustryClassInfoPo.getName());
        return xwIndustryClassInfoVo;
    }
}

