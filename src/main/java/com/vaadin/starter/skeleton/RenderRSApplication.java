package com.vaadin.starter.skeleton;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath(value = "/render")
public class RenderRSApplication extends Application {
    public void foo() {
        System.out.println("BAR");
    }
}
