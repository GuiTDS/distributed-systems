package validators.fieldsvalidators;

public class ValidateKmRange {
    public boolean validate(String kmRange) {
		if((kmRange.trim().length() >= 3 && kmRange.trim().length() <= 7) || kmRange.equals(""))
			return true;
		return false;
	}
}
