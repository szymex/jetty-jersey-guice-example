
import utils.GsonMessageBodyHandler;
import com.google.inject.AbstractModule;
import hello.HelloWorldPL;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import javax.inject.Singleton;
import hello.HelloWorld;
import hello.HelloWorldFI;
import javax.inject.Inject;
import utils.UserFactory;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import utils.User;

public class Main {

    static Injector INJECTOR;

    public static void main(String[] args) throws Exception {
        INJECTOR = Guice.createInjector(new HelloModule(args));

        Server server = new Server(8080);
        ServletContextHandler servletHandler = new ServletContextHandler();

        ServletHolder sh = new ServletHolder(new ServletContainer());
        sh.setInitParameter("javax.ws.rs.Application", HelloApplication.class.getName());
        servletHandler.addFilter(new FilterHolder(INJECTOR.getInstance(HelloFilter.class)), "/*", null);
        servletHandler.addServlet(sh, "/*");

        server.setHandler(servletHandler);
        server.start();
        server.join();
    }

    private static class HelloApplication extends ResourceConfig {

        @Inject
        public HelloApplication(ServiceLocator serviceLocator) {
            GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
            GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
            guiceBridge.bridgeGuiceInjector(Main.INJECTOR);

            registerInstances(new AbstractBinder() {
                @Override
                protected void configure() {
                    bindFactory(UserFactory.class).to(User.class);
                }
            });

            register(GsonMessageBodyHandler.class);
            register(HelloResource.class);
        }
    }

    private static class HelloModule extends AbstractModule {

        private String[] args;

        public HelloModule(String[] args) {
            this.args = args;
        }

        @Provides
        @Singleton
        public HelloWorld provideHelloWorld() {
            if (args.length > 0 && args[0].equals("fi")) {
                return new HelloWorldFI();
            } else {
                return new HelloWorldPL();
            }
        }

        @Override
        protected void configure() {
            bind(UserFactory.class);
        }
    }
}
