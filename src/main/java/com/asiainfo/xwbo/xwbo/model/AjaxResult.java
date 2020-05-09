package com.asiainfo.xwbo.xwbo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.asiainfo.xwbo.xwbo.system.constants.BootConstant;

@ApiModel
public class AjaxResult {

	@ApiModelProperty("状态(0成功1错误2未登陆3刷新4重复提交)")
	private Integer status;
	@ApiModelProperty("错误信息")
	private String msg;
	@ApiModelProperty("数据对象")
	private Object obj;

	public static boolean isSuccess(AjaxResult ajaxResult) {
		return BootConstant.AJAX_STATUS.success.equals(ajaxResult.getStatus());
	}

	/**
	 * 标记成功
	 *
	 * @return
	 */
	public static AjaxResult markSuccess() {
		AjaxResult mrv = new AjaxResult();
		mrv.setStatus(BootConstant.AJAX_STATUS.success);
		return mrv;
	}

	/**
	 * 标记成功
	 *
	 * @return
	 */
	public static AjaxResult markSuccess(Object obj) {
		AjaxResult mrv = new AjaxResult();
		mrv.setStatus(BootConstant.AJAX_STATUS.success);
		mrv.setObj(obj);
		return mrv;
	}

	/**
	 * 标记成功
	 *
	 * @return
	 */
	public static AjaxResult markSuccess(Object obj, String msg) {
		AjaxResult mrv = new AjaxResult();
		mrv.setStatus(BootConstant.AJAX_STATUS.success);
		mrv.setObj(obj);
		mrv.setMsg(msg);
		return mrv;
	}

	/**
	 * 标记失败
	 *
	 * @param error_info
	 *            失败信息
	 * @return
	 */
	public static AjaxResult markError(String msg) {
		AjaxResult mrv = new AjaxResult();
		mrv.setStatus(BootConstant.AJAX_STATUS.error);
		mrv.setMsg(msg);
		return mrv;
	}

	/**
	 * 标记失败
	 *
	 * @param error_info
	 *            失败信息
	 * @return
	 */
	public static AjaxResult markNoLogin() {
		AjaxResult mrv = new AjaxResult();
		mrv.setStatus(BootConstant.AJAX_STATUS.nologin);
		return mrv;
	}

	/**
	 * 标记刷新
	 *
	 * @return
	 */
	public static AjaxResult markRefresh() {
		AjaxResult mrv = new AjaxResult();
		mrv.setStatus(BootConstant.AJAX_STATUS.refresh);
		return mrv;
	}

	/**
	 * 标记重复提交
	 *
	 * @return
	 */
	public static AjaxResult markRepeat(String msg) {
		AjaxResult mrv = new AjaxResult();
		mrv.setStatus(BootConstant.AJAX_STATUS.repeat);
		mrv.setMsg(msg);
		return mrv;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

}
