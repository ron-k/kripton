package sqlite.feature.schema;

import com.abubusoft.kripton.annotation.BindType;

@BindType
public class Person extends Entity {

	public String surname;
	
	public String email;
}
