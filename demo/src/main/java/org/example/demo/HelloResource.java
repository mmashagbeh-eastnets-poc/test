package org.example.demo;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/hello-world")
public class HelloResource {
    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }

    @GET
    @Produces("text/plain")
    public String helloTest() {

        System.out.println("Hello, World!");
        String toket = "{token:ddddddd}";
        return "Hello, World!";
    }
}