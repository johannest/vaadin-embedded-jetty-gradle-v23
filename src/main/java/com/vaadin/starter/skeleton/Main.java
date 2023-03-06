package com.vaadin.starter.skeleton;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

import javax.servlet.Servlet;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.stream.Collectors;

// This example utilises the source code of this library https://github.com/mvysny/vaadin-boot/tree/main/vaadin-boot
// see README for more information
public class Main {

    private static final Boolean PRODUCTION_MODE = true;
    private static final int PORT = 8080;
    private Class<? extends Servlet> vaadinServletClass;
    private Server server;

    public static void main(String argv[]) throws Exception {
        Main main = new Main();
        main.run();
    }

    protected void run() throws Exception {
        try {
            vaadinServletClass = Class.forName("com.vaadin.flow.server.VaadinServlet").asSubclass(Servlet.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.setProperty("vaadin.productionMode", String.valueOf(PRODUCTION_MODE));

        fixClasspath();
        final WebAppContext context = createWebAppContext();
        server = new Server(PORT);
        server.setHandler(context);
        server.start();

        server.join();
    }

    // copied from: https://github.com/mvysny/vaadin-boot/tree/main/vaadin-boot
    protected WebAppContext createWebAppContext() throws MalformedURLException {
        final WebAppContext context = new WebAppContext();
        context.setBaseResource(findWebRoot());
        context.setContextPath("/analytics");
        context.addServlet(vaadinServletClass, "/*");
        // this will properly scan the classpath for all @WebListeners, including the most important
        // com.vaadin.flow.server.startup.ServletContextListeners.
        // See also https://mvysny.github.io/vaadin-lookup-vs-instantiator/
        context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*\\.jar|.*/classes/.*");
        context.setConfigurationDiscovered(true);
        context.getServletContext().setExtendedListenerTypes(true);

        ServletHolder holder = new ServletHolder(new HttpServletDispatcher());
        holder.setInitParameter("javax.ws.rs.Application", AnalyticsRSApplication.class.getName());
        holder.setInitParameter("resteasy.scan", "true");
        holder.setInitParameter("resteasy.servlet.mapping.prefix", "/report/download");
        context.addServlet(holder, "/report/download/*");

        ServletHolder renderHolder = new ServletHolder(new HttpServletDispatcher());
        renderHolder.setInitParameter("javax.ws.rs.Application", RenderRSApplication.class.getName());
        renderHolder.setInitParameter("resteasy.scan", "true");
        renderHolder.setInitParameter("resteasy.servlet.mapping.prefix", "/render");
        context.addServlet(renderHolder, "/render/*");

        return context;
    }

    /**
     * Stops your app. Blocks until the webapp is fully stopped. Mostly used for tests.
     * @param reason why we're shutting down. Logged as info.
     */
    // copied from: https://github.com/mvysny/vaadin-boot/tree/main/vaadin-boot
    public void stop(String reason) {
        try {
            if (server != null) {
                server.stop(); // blocks until the webapp stops fully
                System.out.println("Stopped");
                server = null;
            }
        } catch (Throwable t) {
            System.out.println("stop() failed: " + t);
        }
    }

    // copied from: https://github.com/mvysny/vaadin-boot/tree/main/vaadin-boot
    protected void fixClasspath() {
        final String classpath = System.getProperty("java.class.path");
        if (classpath != null) {
            final String[] entries = classpath.split("[" + File.pathSeparator + "]");
            final String filteredClasspath = Arrays.stream(entries)
                    .filter(it -> !it.isBlank() && new File(it).exists())
                    .collect(Collectors.joining(File.pathSeparator));
            System.setProperty("java.class.path", filteredClasspath);
        }
    }

    // copied from: https://github.com/mvysny/vaadin-boot/tree/main/vaadin-boot
    private static Resource findWebRoot() throws MalformedURLException {
        // don't look up directory as a resource, it's unreliable: https://github.com/eclipse/jetty.project/issues/4173#issuecomment-539769734
        // instead we'll look up the /webapp/ROOT and retrieve the parent folder from that.
        final URL f = Main.class.getResource("/webapp/ROOT");
        if (f == null) {
            throw new IllegalStateException("Invalid state: the resource /webapp/ROOT doesn't exist, has webapp been packaged in as a resource?");
        }
        final String url = f.toString();
        if (!url.endsWith("/ROOT")) {
            throw new RuntimeException("Parameter url: invalid value " + url + ": doesn't end with /ROOT");
        }
        System.out.println("/webapp/ROOT is " + f);

        // Resolve file to directory
        URL webRoot = new URL(url.substring(0, url.length() - 5));
        System.out.println("WebRoot is " + webRoot);
        return Resource.newResource(webRoot);
    }
}
