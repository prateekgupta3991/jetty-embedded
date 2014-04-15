package com.extrema.jetty.embedded;

import com.google.common.base.Splitter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.Configuration.ClassList;
import org.eclipse.jetty.webapp.WebAppContext;

import java.util.Iterator;


/**
 * Arranca un Jetty server embebido junto con las aplicaciones pasadas como argumento
 * en el formato domain:project-name, ej: helloworld:helloworld.
 */
public final class JettyServer {

	private JettyServer() {
	}

	public static void main(String[] args) throws Exception {

		Server server = new Server(8080);

        ClassList classlist = ClassList.setServerDefault(server);
        // Enable parsing of jndi-related parts of web.xml and jetty-env.xml
        classlist.addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
                "org.eclipse.jetty.annotations.AnnotationConfiguration");


        HandlerCollection handlers = new HandlerCollection();

		if (args != null) {
			for (String arg : args) {
				Iterator<String> it = Splitter.on(":").split(arg).iterator();
				String domain = it.next();
				String projectName = it.hasNext() ? it.next() : domain;
				handlers.addHandler(createContext(domain, projectName));
			}
		}
        server.setHandler(handlers);

        server.start();
        server.join();
    }

	private static WebAppContext createContext(String domain, String projectName) {
		WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/" + domain);
        webapp.setWar("../" + projectName + "/src/main/webapp");

        return webapp;
	}

}