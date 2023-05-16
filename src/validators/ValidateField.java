package validators;

public class ValidateField {
	private final int minLenName = 3, maxLenName = 32; //name
	private final int minLenPassword = 8, maxLenPassword = 32; //password
	private final int minLenEmail = 16, maxLenEmail = 50;
	
	
	public boolean validateName(String name) {
		//valida tamanho do nome
		if(name.length() < this.minLenName || name.length() > this.maxLenName) {
			return false;
		} // validando se o nome possui numeros
		else if(name.replaceAll("\\d", "").length() != name.length()) {
			return false;
		}
		return true;
	}
	
	public boolean validatePassword(String password) {
		//System.out.println("Validando senha");
		return password.length() >= this.minLenPassword && password.length() <= this.maxLenPassword;
	}
	
	public boolean validateEmail(String email) {
		return email.contains("@") && email.length() >= this.minLenEmail && email.length() <= this.maxLenEmail;
	}

	public boolean validateHighway(String highway) {
		if(highway.length() == 6) {
			String[] highwayParts = highway.split("-");
			try {
				@SuppressWarnings("unused")
				int number = Integer.parseInt(highwayParts[1]); // tem 3 digitos na parte final ==> ok
				try {
					@SuppressWarnings("unused")
					double test = Double.parseDouble(highwayParts[0]);
					return false;
				}catch(NumberFormatException e) {
					return true; // a primeira parte da string é formada por letras ==> ok
				}
			}catch(NumberFormatException e) {
				return false;
			}
				
		}
		return false;
	}
	
	public boolean validateKM(int km) {
		String kmStr = Integer.toString(km);
		if(kmStr.length() > 0 && kmStr.length() <= 3)
			return true;
		return false;
	}
	
	public boolean validateIncidentType(int incident, int maxAcident) {
		if(incident >= 1 && incident <= maxAcident) {
			return true;
		}
		return false;
	}

	public boolean validateKmRange(String kmRange) {
		if((kmRange.trim().length() >= 3 && kmRange.trim().length() <= 7) || kmRange.equals(""))
			return true;
		return false;
	}

	public boolean validatePeriod(int period) {
		if(period < 1 || period > 4) 
			return false;
		return true;
	}
	
}
