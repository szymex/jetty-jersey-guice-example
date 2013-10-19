package utils;

import hello.User;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.PerRequestTypeInjectableProvider;
import javax.inject.Singleton;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author szymon
 */
@Provider
@Singleton
public class UserProvider extends PerRequestTypeInjectableProvider<CustomContext, User> {

    public UserProvider() {
        super(User.class);
    }

    @Override
    public Injectable<User> getInjectable(ComponentContext ic, CustomContext a) {

        return new AbstractHttpContextInjectable<User>() {
            @Override
            public User getValue(HttpContext c) {
                return new User(c.getRequest().getQueryParameters().getFirst("user"));
            }
        };
    }
}
