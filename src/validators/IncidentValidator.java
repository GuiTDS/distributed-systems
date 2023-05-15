package validators;

import model.Incident;

public class IncidentValidator extends Validator {
	private Incident incident;
	
	public void setIncident(Incident incident) {
		this.incident = incident;
	}

	@Override
	public boolean isValid() { //report incident
		ValidateField validateField = new ValidateField();
		if(!validateField.validateHighway(incident.getHighway())) {
			super.setErrorMessage("A rodovia deve seguir o formato: BR-111");
			super.setOpResponse(super.getFailOpCode());
			return false;
		} else if(!validateField.validateKM(incident.getKm())) {
			super.setErrorMessage("O KM deve possuir no minimo 1 e no maximo 3 digitos");
			super.setOpResponse(super.getFailOpCode());
			return false;
		} else if(!validateField.validateIncidentType(incident.getIncidentType(), incident.getIncidentTypes())) {
			super.setErrorMessage("O codigo do incidente informado nao existe");
			super.setOpResponse(super.getFailOpCode());
			return false;
		}
		super.setOpResponse(super.getSucessOpCode());
		return true;
	}

	public boolean isValidGetListOfIncidents(Incident incident, String kmRange, int period) {
		ValidateField validateField = new ValidateField();
		if(!validateField.validateHighway(incident.getHighway())) {
			super.setErrorMessage("A rodovia deve seguir o formato: BR-111");
			super.setOpResponse(super.getFailOpCode());
			return false;
		} else if (!validateField.validateKmRange(kmRange)) {
			super.setErrorMessage("A faixa de km deve estar no formato: 111-999");
			super.setOpResponse(super.getFailOpCode());
			return false;
		} else if(!validateField.validatePeriod(period)) {
			super.setErrorMessage("O periodo deve estar entre 1 e 4");
			super.setOpResponse(super.getFailOpCode());
			return false;
		}
		return true;
	}
	
}
