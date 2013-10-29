package se.dynabyte.dynaship.service.getmove.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CollectionValidator.class)
public @interface ValidCollection {

	Class<?> elementType();

	Class<?>[] constraints() default {};

	boolean allViolationMessages() default true;

	String message() default "may not contain invalid elements.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
