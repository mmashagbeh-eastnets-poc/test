package org.example.demo;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/hello-world")
public class HelloResource {
    /**
     * Returns a "Hello, World!" message as a plain text response.
     *
     * <p>This method handles HTTP GET requests for the "/hello-world" endpoint and simply returns the greeting message "Hello, World!".</p>
     *
     * @return a greeting message "Hello, World!"
     */
    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }

    /**
     * Responds to HTTP GET requests by logging and returning a plain text greeting.
     *
     * <p>This method prints "Hello, World!" to the console and returns the same greeting. Note that a token variable is declared but not used.
     *
     * @return the greeting "Hello, World!" as plain text
     */
    @GET
    @Produces("text/plain")
    public String helloTest() {

        System.out.println("Hello, World!");
        String toket = "{token:ddddddd}";
        return "Hello, World!";
    }
}