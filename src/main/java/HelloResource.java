

import java.io.IOException;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import hello.HelloWorld;

@Path("/")
@Singleton
public class HelloResource {

    @Inject
    HelloWorld helloWord;

    @GET
    @Path("/hello.json")
    @Produces(MediaType.APPLICATION_JSON)
    public Data getJsonMessage() {
        return new Data(helloWord.say());
    }

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed("user")
    public String getTxtMessage() {
        return helloWord.say();
    }

    @GET
    @Path("/async-hello")
    public void getAsyncData(@Context HttpServletRequest request,
            @QueryParam("d") @DefaultValue("1") final int delaySec) throws IOException {

        final AsyncContext asyncContext = request.startAsync();
        asyncContext.start(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delaySec * 1000);
                    
                    HttpServletResponse resp = (HttpServletResponse)asyncContext.getResponse();
                    resp.getWriter().println(helloWord.say());
                    resp.setStatus(200);
                    asyncContext.complete();
                } catch (InterruptedException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static class Data {

        private String text;

        public Data(String data) {
            this.text = data;
        }

        public String getData() {
            return text;
        }
    }
}