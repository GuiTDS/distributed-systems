package validators.fieldsvalidators;

public class ValidateIncidentType {
    public boolean validate(int incident, int maxAcident) {
		if(incident >= 1 && incident <= maxAcident) {
			return true;
		}
		return false;
	}
}
