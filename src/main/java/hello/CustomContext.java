package hello;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author szymon
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD })  
@Retention(RetentionPolicy.RUNTIME)  
public @interface CustomContext {
}
