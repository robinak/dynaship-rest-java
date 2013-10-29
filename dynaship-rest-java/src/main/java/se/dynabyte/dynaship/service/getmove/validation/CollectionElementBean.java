package se.dynabyte.dynaship.service.getmove.validation;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class CollectionElementBean {

private Object notNull;
private String notBlank;
private long min;
private long max;

@NotNull
public Object getNotNull() { return notNull; }
public void setNotNull(Object notNull) { this.notNull = notNull; }

@NotBlank
public String getNotBlank() { return notBlank; }
public void setNotBlank(String notBlank) { this.notBlank = notBlank; }

@Min(value = 0)
public long getMin() { return min; }
public void setMin(long min) { this.min = min; }

@Max(value = 0)
public long getMax() { return max; }
public void setMax(long max) { this.max = max; }

}
