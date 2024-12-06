package ru.edu.springweb.server;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

public class TomcatInitializer implements ServerInitializer {

    @Override
    public void init() throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(System.getProperty("java.io.tmpdir"));
        tomcat.setPort(8080);

        StandardContext context = (StandardContext) tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());

        File webInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(context);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", webInfClasses.getAbsolutePath(), "/"));
        context.setResources(resources);

        tomcat.getConnector();
        tomcat.start();
        tomcat.getServer().await();
    }
}