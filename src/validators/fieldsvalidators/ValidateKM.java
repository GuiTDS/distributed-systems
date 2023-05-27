package validators.fieldsvalidators;

public class ValidateKM {
    public boolean validate(int km) {
		String kmStr = Integer.toString(km);
		if(kmStr.length() > 0 && kmStr.length() <= 3)
			return true;
		return false;
	}
}
