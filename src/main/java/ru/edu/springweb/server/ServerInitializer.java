package ru.edu.springweb.server;

import org.apache.catalina.LifecycleException;

public interface ServerInitializer {
    void init() throws LifecycleException;
}