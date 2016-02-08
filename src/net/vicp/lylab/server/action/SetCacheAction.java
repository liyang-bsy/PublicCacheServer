package net.vicp.lylab.server.action;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;

import net.vicp.lylab.core.BaseAction;
import net.vicp.lylab.core.CoreDef;
import net.vicp.lylab.core.model.ObjectContainer;
import net.vicp.lylab.utils.Utils;
import net.vicp.lylab.utils.cache.LYCache;

public class SetCacheAction extends BaseAction {

	private String server;
	private String module;
	private String key;
	private Long expireTime;
	private Object data;

	@Override
	public boolean foundBadParameter() {

		server = (String) getRequest().getBody().get("server");
		module = (String) getRequest().getBody().get("module");
		key = (String) getRequest().getBody().get("key");
		expireTime = (Long) getRequest().getBody().get("expireTime");
		data = getRequest().getBody().get("data");

		if(StringUtils.isBlank(server)) {
			badParameter = "server";
			return true;
		}
		if(StringUtils.isBlank(module)) {
			badParameter = "module";
			return true;
		}
		if(StringUtils.isBlank(key)) {
			badParameter = "key";
			return true;
		}
		if (data == null) {
			badParameter = "data";
			return true;
		}
		
		return false;
	}
	
	@Override
	public void exec() {
		do {
			LYCache cache = (LYCache) CoreDef.config.getConfig("Singleton").getObject("LYCache");
			
			String json = Utils.serialize(ObjectContainer.fromObject(data));
			byte[] bytes;
			try {
				bytes = json.getBytes(CoreDef.CHARSET());
			} catch (UnsupportedEncodingException e) {
				getResponse().setCode(0x00010002);
				getResponse().setMessage("Param data needs specific encoding");
				break;
			}
			if(expireTime == null)
				expireTime = 0L;

			synchronized (lock) {
				cache.set(server + "_" + module + "_" + key, bytes);
			}

		getResponse().success(); } while (false);
	}

}
