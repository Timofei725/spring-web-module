package ru.edu.springweb;

import org.apache.catalina.LifecycleException;
import ru.edu.springweb.server.ServerInitializer;
import ru.edu.springweb.server.TomcatInitializer;

public class App {
    public static void main(String[] args) throws LifecycleException {
        ServerInitializer tomcatInitializer = new TomcatInitializer();
        tomcatInitializer.init();
    }
}
