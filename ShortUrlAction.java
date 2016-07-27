package com.action.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import com.action.frontend.BaseAction;
import com.AbstractMapStoreShortUrlService;
import com.ActionHelper;
import com.SequenceBaseShortUrlService;
import com.Utility;
import com.VariableFactory;

import net.sf.json.JSONObject;

/**
 * 短链接Action
 * 
 * @author LUOMINQING
 *
 */
public class ShortUrlAction extends BaseAction {
	/**
	 * 从本站点获取短链接
	 * 
	 * @return
	 */
	public String getShortUrlFromLocal() {
		super.init();
		JSONObject jsonResult = null;
		Boolean isExe = true;
		if (Utility.isNullOrEmpty(url)) {
			jsonResult = ActionHelper.getFailResult("url参数不能为空");
			isExe = false;
		}
		if (isExe) {
			synchronized (shortUrlService) {
				String strShortUrl = "";
				jsonResult = ActionHelper.getDefaultSuccessResult();
				try {
					strShortUrl = shortUrlService.convertShort(url);
					jsonResult.put(VariableFactory.DATAKEY, strShortUrl);
				} catch (Exception e) {
					log.error(e.getMessage());
					jsonResult.put(VariableFactory.DATAKEY, strShortUrl);
				}
			}
		}
		
		setAttributeToSession(jsonResult);
		
		return SUCCESS;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

}
