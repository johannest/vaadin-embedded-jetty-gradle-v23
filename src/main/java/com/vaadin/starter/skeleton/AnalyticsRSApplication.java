package com.vaadin.starter.skeleton;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath(value = "/report/download")
public class AnalyticsRSApplication extends Application {

    public void foo() {
        System.out.println("FOO");
    }
}
