package net.vicp.lylab.server;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.vicp.lylab.core.CoreDef;

public class ServerRuntime implements ServletContextListener {
	public static void main(String[] arg) {
		try {
			CoreDef.config.reload(CoreDef.rootPath + File.separator + "config" + File.separator + "config.txt");
			System.out.println("server started");
		} catch (Exception e) {
			e.printStackTrace();
			close();
		}
	}
	
	public static void close() {
		CoreDef.config.deepClose();
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		CoreDef.rootPath = sce.getServletContext().getRealPath("/");
		try {
			CoreDef.config.reload(CoreDef.rootPath + "WEB-INF" + File.separator + "classes" + File.separator + "config.txt");
		} catch (Exception e) {
			e.printStackTrace();
			close();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		CoreDef.config.deepClose();
	}
	
}
