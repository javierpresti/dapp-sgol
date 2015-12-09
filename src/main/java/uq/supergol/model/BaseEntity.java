package uq.supergol.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {
	
	@Id
	@GeneratedValue
	protected Long id;
	
	public BaseEntity() {}
	
	public String toString(String attributes, Object... attributeValues) {
		Object[] values = new Object[attributeValues.length + 1];
		values[0] = getId();
		System.arraycopy(attributeValues, 0, values, 1, attributeValues.length);
		return String.format(getClass().getSimpleName() + "[id=%d" + attributes + "]", values);
	}
	
	@Override
	public String toString() {
		return toString("", new Object[] {});
	}
	
	public Long getId() {
		return id;
	}

}
