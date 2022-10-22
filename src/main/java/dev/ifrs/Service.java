package dev.ifrs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;

import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

@Path("/fault")
public class Service {
    
    @GET
    @Path("/retry/{value}")
    //@Retry(maxRetries = 2, delay = 1000)
    @Fallback(fallbackMethod = "recover")
    @Timeout(7000)
    public String retry(@PathParam("value") String value) {

        try {
            Thread.sleep(10000);
        }
        catch(InterruptedException e) {
            System.out.println("timeout");
        }
        
        System.out.println("retry");

        if ( value.equalsIgnoreCase("error") ) {
            throw new WebApplicationException();
        }

        return value;
    }

    /**
     * Fallback method
     * @return fallback message
     */
    public String recover(String value) {

        return value + ": internal error";
    }

    /**
     * Bulkhead method
     * @param value
     * @return value
     */
    @GET
    @Path("/bulkhead/{value}")
    @Bulkhead(2)
    public String bulkhead(@PathParam("value") String value) {
        
        return value;
    }

}
