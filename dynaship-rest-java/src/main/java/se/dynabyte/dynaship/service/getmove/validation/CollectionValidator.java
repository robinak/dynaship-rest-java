package se.dynabyte.dynaship.service.getmove.validation;

import java.beans.Introspector;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollectionValidator implements ConstraintValidator<ValidCollection, Collection<?>> {

	  private static final Logger logger = LoggerFactory.getLogger(CollectionValidator.class);

	    private final ValidatorContext validatorContext = Validation.buildDefaultValidatorFactory().usingContext();

	    private Class<?> elementType;
	    private Class<?>[] constraints;
	    private boolean allViolationMessages;


	    @Override
	    public void initialize(ValidCollection constraintAnnotation) {
	        elementType = constraintAnnotation.elementType();
	        constraints = constraintAnnotation.constraints();
	        allViolationMessages = constraintAnnotation.allViolationMessages();
	    }

	    @Override
	    public boolean isValid(Collection<?> collection, ConstraintValidatorContext context) {
	        boolean valid = true;
	        
	        Validator validator = validatorContext.getValidator();
	        boolean beanConstrained = validator.getConstraintsForClass(elementType).isBeanConstrained();

	        if(collection != null) {
	        	for(Object element : collection) {
	        		Set<ConstraintViolation<?>> violations = new HashSet<ConstraintViolation<?>> ();
	        		
	        		if(beanConstrained) {
	        			violations.addAll(validator.validate(element));
	        			
	        		} else {
	                    for(Class<?> constraint : constraints) {
	                        String propertyName = Introspector.decapitalize(constraint.getSimpleName());
	                        violations.addAll(validator.validateValue(CollectionElementBean.class, propertyName, element));
	                    }
	                }
	        		
	        		if(!violations.isEmpty()) {
	        			valid = false;
	        		}
	        		
	        		if(allViolationMessages) {
	        			for(ConstraintViolation<?> violation : violations) {
	        				StringBuilder builder = new StringBuilder();
	        				builder.append("contains invalid element.");
	        				builder.append(" Element of type ");
	        				builder.append(elementType.getName());
	        				builder.append(" have property with invalid value [");
	        				builder.append(violation.getInvalidValue());
	        				builder.append("].");
	        				builder.append(" Constraint: ");
	        				builder.append(violation.getConstraintDescriptor().getAnnotation());
	        				
	        				String message = builder.toString();
	        				logger.debug(message);
	        				
	        				ConstraintViolationBuilder violationBuilder = context.buildConstraintViolationWithTemplate(message);
	        				violationBuilder.addConstraintViolation();
	        			}
	        		}
	        		
	        	}
	        }

	        return valid;
	    }
}
