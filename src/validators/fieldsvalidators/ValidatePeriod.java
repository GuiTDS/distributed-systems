package validators.fieldsvalidators;

public class ValidatePeriod {
    public boolean validate(int period) {
		if(period < 1 || period > 4) 
			return false;
		return true;
	}
}
