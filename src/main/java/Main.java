
import utils.GsonMessageBodyHandler;
import hello.HelloWorldPL;
import com.google.inject.Guice;
import com.google.inject.Provides;
import com.google.inject.servlet.GuiceFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Singleton;
import hello.HelloWorld;
import hello.HelloWorldFI;
import utils.UserProvider;
import org.eclipse.jetty.servlet.DefaultServlet;

public class Main {

    public static void main(String[] args) throws Exception {
        Guice.createInjector(new HelloJerseyModule(args));
               
        Server server = new Server(8080);
        ServletContextHandler servletHandler = new ServletContextHandler();
        servletHandler.addFilter(GuiceFilter.class, "/*", null);
        servletHandler.addServlet(DefaultServlet.class, "/");

        server.setHandler(servletHandler);
        server.start();
        server.join();
    }

    private static class HelloJerseyModule extends JerseyServletModule {

        private String[] args;

        public HelloJerseyModule(String[] args) {
            this.args = args;
        }

        @Provides
        public HelloWorld provideHelloWorld() {
            if (args.length > 0 && args[0].equals("fi")) {
                return new HelloWorldFI();
            } else {
                return new HelloWorldPL();
            }
        }

        @Override
        protected void configureServlets() {
            bind(HelloResource.class);
            bind(GsonMessageBodyHandler.class).in(Singleton.class);
            bind(UserProvider.class);
            
            filter("/*").through(HelloFilter.class);

            Map<String, String> params = new HashMap<>();
            params.put("com.sun.jersey.spi.container.ResourceFilters", "com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory");
            params.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
            serve("/*").with(GuiceContainer.class, params);
        }
    }
}
