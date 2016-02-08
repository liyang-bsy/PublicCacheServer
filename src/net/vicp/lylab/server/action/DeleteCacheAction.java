package net.vicp.lylab.server.action;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;

import net.vicp.lylab.core.BaseAction;
import net.vicp.lylab.core.CoreDef;
import net.vicp.lylab.core.model.ObjectContainer;
import net.vicp.lylab.utils.Utils;
import net.vicp.lylab.utils.cache.LYCache;

public class DeleteCacheAction extends BaseAction {

	private String server;
	private String module;
	private String key;

	@Override
	public boolean foundBadParameter() {
		server = (String) getRequest().getBody().get("server");
		module = (String) getRequest().getBody().get("module");
		key = (String) getRequest().getBody().get("key");

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
		return false;
	}
	
	@Override
	public void exec() {
		do {
			LYCache cache = (LYCache) CoreDef.config.getConfig("Singleton").getObject("LYCache");

			byte[] bytes;
			synchronized (lock) {
				bytes = cache.delete(server + "_" + module + "_" + key);
			}
			String json = null;
			try {
				json = new String(bytes, CoreDef.CHARSET());
			} catch (UnsupportedEncodingException e) {
				getResponse().setCode(0x00010002);
				getResponse().setMessage("Cached data needs specific encoding");
				break;
			}

			ObjectContainer<?> data = Utils.deserialize(ObjectContainer.class, json);
			
			getResponse().getBody().put("data", data.getObject());

		getResponse().success(); } while (false);
	}

}
