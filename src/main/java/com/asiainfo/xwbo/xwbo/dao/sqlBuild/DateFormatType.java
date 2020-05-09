package com.asiainfo.xwbo.xwbo.dao.sqlBuild;

/**
 * 时间格式类型
 * @author t-xiabin
 *
 */
public enum DateFormatType {
	yyyy,
	yyyyMM,
	yyyyMMdd,
	yyyyMMddHH,
	yyyyMMddHHmm,
	yyyyMMddHHmmss;
	public String getDateFormatType(DateFormatType dateFormatType){
		if(dateFormatType == yyyy){
			return "yyyy";
		}else if(dateFormatType == yyyyMM){
			return "yyyyMM";
		}else if(dateFormatType == yyyyMMdd){
			return "yyyyMMdd";
		}else if(dateFormatType == yyyyMMddHH){
			return "yyyyMMddHH";
		}else if(dateFormatType == yyyyMMddHHmm){
			return "yyyyMMddHHmm";
		}else if(dateFormatType == yyyyMMddHHmmss){
			return "yyyyMMddHHmmss";
		}
		return "yyyyMMdd";
	}
}
