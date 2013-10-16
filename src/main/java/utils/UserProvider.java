package utils;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;
import hello.CustomContext;
import java.lang.reflect.Type;
import javax.inject.Singleton;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author szymon
 */
@Provider
@Singleton
public class UserProvider extends AbstractHttpContextInjectable<User> implements InjectableProvider<CustomContext, Type> {  

    @Override  
    public Injectable<User> getInjectable(ComponentContext ic, CustomContext a, Type c) {  
        if (c.equals(User.class)) {  
            return this;  
        }  
        return null;  
    }  
  
    @Override  
    public ComponentScope getScope() {  
        return ComponentScope.PerRequest;  
    }  
  
    @Override  
    public User getValue(HttpContext c) {  
        return new User(c.getRequest().getQueryParameters().getFirst("user"));  
    }  
        
}
