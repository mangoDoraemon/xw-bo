package com.asiainfo.xwbo.xwbo.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.xwbo.xwbo.dao.ICommonExtDao;
import com.asiainfo.xwbo.xwbo.dao.sqlBuild.SqlBuilder;
import com.asiainfo.xwbo.xwbo.model.po.XwUserHandleInfoPo;
import com.asiainfo.xwbo.xwbo.model.po.XwUserInfoPo;
import com.asiainfo.xwbo.xwbo.model.so.XwSignSo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

@Slf4j
public class SignInterceptor implements HandlerInterceptor {

	@Resource
	private ICommonExtDao commonExtDao;


	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object obj) throws Exception {
		//检查签名
//		if(!validSign(req, obj)){
//			return false;
//		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse arg1, Object handler, Exception arg3)
			throws Exception {
//		if(!validSign(req, obj)){
////			return false;
////		}
		if(needSign(handler)) {
			RequestWrapper requestWrapper = new RequestWrapper(request);
            String jsonBody = requestWrapper.getBody();
            if(StringUtils.isBlank(jsonBody)){
            	throw new RuntimeException("缺少签名对象信息");
            }
            JSONObject jo = JSON.parseObject(jsonBody);
            XwSignSo sign = JSON.parseObject(jo.getString("sign"), XwSignSo.class);
			XwUserHandleInfoPo xwUserHandleInfoPo = new XwUserHandleInfoPo();
			xwUserHandleInfoPo.setUserId(sign.getSignId());
			xwUserHandleInfoPo.setUrl(request.getServletPath());
			xwUserHandleInfoPo.setParams(jo.toJSONString());
			xwUserHandleInfoPo.setType(sign.getType());
			xwUserHandleInfoPo.setCreateTime(new Date());
			XwUserInfoPo xwUserInfoPo = commonExtDao.queryForObject(SqlBuilder.build(XwUserInfoPo.class).eq("user_id", sign.getSignId()));
			xwUserHandleInfoPo.setCityId(xwUserInfoPo.getCityId());
			switch (request.getServletPath()){
				case "/area/qryCascadeAreaInfo":xwUserHandleInfoPo.setFeature("返回省市区级联信息");break;
				case "/group/qryAll":xwUserHandleInfoPo.setFeature("查询全部集团信息");break;
				case "/user/qrySubordinatesRoleList":xwUserHandleInfoPo.setFeature("查询下属角色信息");break;
				case "/user/qrySubordinatesWorkDetail":xwUserHandleInfoPo.setFeature("查询下属工作详情");break;
				case "/job/qryEstablishJobAll":xwUserHandleInfoPo.setFeature("查询全部工单信息");break;
				case "/group/qryAllPosition":xwUserHandleInfoPo.setFeature("地图打点，返回的集团信息（会根据传的可视区域四个角来返回）");break;
				case "/group/qryInfo":xwUserHandleInfoPo.setFeature("查询单个集团信息");break;
				case "/area/qryAreaInfo":xwUserHandleInfoPo.setFeature("查询区域信息");break;
				case "/area/qryAreaHandleInfo":xwUserHandleInfoPo.setFeature("查询地区商户状态(待摸排、摸排中、已摸排、在网)数量");break;
				case "/user/qryInfo":xwUserHandleInfoPo.setFeature("查询用户信息");break;
				case "/job/qryAcceptJobAll":xwUserHandleInfoPo.setFeature("查询全部工单信息");break;
				case "/job/qryAllJobCount":xwUserHandleInfoPo.setFeature("查询工单状态(待处理、处理中、已超时、已完成)数量");break;
				case "/user/qrySubordinates":xwUserHandleInfoPo.setFeature("查询用户微格边界数据");break;
				case "/group/qryUserHandleInfo":xwUserHandleInfoPo.setFeature("查询集团用户处理状态(待摸排、摸排中、已摸排、待处理人排摸)信息");break;
				case "/group/qryCaseItemInfo":xwUserHandleInfoPo.setFeature("查询行业案例");break;
				case "/group/qryProductInfo":xwUserHandleInfoPo.setFeature("查询推荐产品");break;
				case "/group/qryMember":xwUserHandleInfoPo.setFeature("查询集团成员");break;
				case "/group/sync":xwUserHandleInfoPo.setFeature("新建修改集团信息");break;
				case "/group/changeHandleState":xwUserHandleInfoPo.setFeature("修改摸排状态");break;
				default:break;
			}
			commonExtDao.save(SqlBuilder.build(XwUserHandleInfoPo.class), xwUserHandleInfoPo);
		}

	}

//	private boolean validSign(HttpServletRequest request, Object handler) {
//		if (needSign(handler)) {
//			RequestWrapper requestWrapper = new RequestWrapper(request);
//            String jsonBody = requestWrapper.getBody();
//            if(StringUtils.isBlank(jsonBody)){
//            	throw new RuntimeException("缺少签名对象信息");
//            }
//            JSONObject jo = JSON.parseObject(jsonBody);
//            String signId = JSON.parseObject(jo.getString("signId"), String.class);
//			System.out.println(signId);
////			XwUserHandleInfo
////
//		}
//		return true;
//	}

	private boolean needSign(Object handler){
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			Sign sign = method.getAnnotation(Sign.class);
			if(null != sign){
				return sign.value();
			}
		}
		return false;
	}

}
