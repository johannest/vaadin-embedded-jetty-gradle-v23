package com.vaadin.starter.skeleton;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath(value = "/render")
public class RenderRSApplication extends Application {
    public void foo() {
        System.out.println("BAR");
    }
}
