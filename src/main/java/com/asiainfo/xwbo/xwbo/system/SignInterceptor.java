package com.asiainfo.xwbo.xwbo.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.xwbo.xwbo.dao.ICommonExtDao;
import com.asiainfo.xwbo.xwbo.dao.sqlBuild.SqlBuilder;
import com.asiainfo.xwbo.xwbo.model.po.XwUserHandleInfoPo;
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
            String signId = JSON.parseObject(jo.getString("signId"), String.class);
			XwUserHandleInfoPo xwUserHandleInfoPo = new XwUserHandleInfoPo();
			xwUserHandleInfoPo.setUserId(signId);
			xwUserHandleInfoPo.setUrl(request.getServletPath());
			xwUserHandleInfoPo.setParams(jo.toJSONString());
			xwUserHandleInfoPo.setCreateTime(new Date());
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
