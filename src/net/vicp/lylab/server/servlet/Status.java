package net.vicp.lylab.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import net.vicp.lylab.core.CoreDef;
import net.vicp.lylab.utils.cache.LYCache;

/**
 * 后台服务运行状态
 */
@WebServlet("/status")
public class Status extends HttpServlet {
	private static final long serialVersionUID = 3200296505570525661L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("unused")
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		StringBuilder content = new StringBuilder(1024);
		String cmdResult = "";
		// 命令模式
		if(!StringUtils.isBlank(request.getParameter("cmdMode")))
		{
			String cmd = request.getParameter("cmdMode");
			if(false);
			else if(cmd.equals("clearCache"))
			{
				((LYCache) CoreDef.config.getConfig("Singleton").getObject("LYCache")).clear();
				cmdResult = "ok";
			}
		}
		content.append("<!DOCTYPE html>");
		content.append("<html>");
		content.append("<meta charset=\"UTF-8\">");
		content.append("<body>");
		content.append("当前时间：\t\t" + System.currentTimeMillis() + "<br/>");
		content.append("当前缓存条目数：\t\t" + ((LYCache) CoreDef.config.getConfig("Singleton").getObject("LYCache")).getEntrySize() + "<br/>");
		content.append("当前缓存容量：\t\t" + ((LYCache) CoreDef.config.getConfig("Singleton").getObject("LYCache")).getMemorySize() + "<br/>");
		content.append("哈希算法：\t\t" + ((LYCache) CoreDef.config.getConfig("Singleton").getObject("LYCache")).getHashAlgorithm() + "<br/>");

		if(!StringUtils.isBlank(cmdResult))
			content.append("命令结果:<br/>" + cmdResult + "<br/>");
		
		content.append("\n");
		content.append("</body>");
		content.append("</html>");
		response.getOutputStream().write(content.toString().getBytes("UTF-8"));
	}

}
