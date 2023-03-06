package com.vaadin.starter.skeleton;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath(value = "/report/download")
public class AnalyticsRSApplication extends Application {

    public void foo() {
        System.out.println("FOO");
    }
}
