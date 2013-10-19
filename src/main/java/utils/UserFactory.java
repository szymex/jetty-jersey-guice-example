package utils;

import java.util.Random;
import javax.inject.Inject;
import javax.ws.rs.QueryParam;
import org.glassfish.hk2.api.Factory;

/**
 *
 * @author szymon
 */
public class UserFactory implements Factory<User> {
    //@Inject HttpContext c;

    @Inject
    @QueryParam("user")
    String usName;

    @Override
    public User provide() {
        return new User("dupa" + new Random().nextInt(100) + usName);
        //return new User(c.getUriInfo().getQueryParameters().getFirst("user"));
    }

    @Override
    public void dispose(User instance) {
    }
}
