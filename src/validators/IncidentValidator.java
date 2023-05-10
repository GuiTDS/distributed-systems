package validators;

import model.Incident;

public class IncidentValidator extends Validator {
	private Incident incident;
	
	public void setIncident(Incident incident) {
		this.incident = incident;
	}

	@Override
	public boolean isValid() { //FALTA VALIDAR O DATE
		ValidateField validaCampo = new ValidateField();
		if(incident.getHighway() == null || !validaCampo.validateHighway(incident.getHighway())) {
			super.setErrorMessage("A rodovia deve seguir o formato: BR-111");
			super.setOpResponse(super.getFailOpCode());
			return false;
		} else if((Integer)incident.getKm() == null || !validaCampo.validateKM(incident.getKm())) {
			super.setErrorMessage("O KM deve possuir no minimo 1 e no maximo 3 digitos");
			super.setOpResponse(super.getFailOpCode());
			return false;
		} else if((Integer)incident.getIncidentType() == null || !validaCampo.validateIncidentType(incident.getIncidentType(), incident.getIncidentTypes())) {
			super.setErrorMessage("O codigo do incidente informado nao existe");
			super.setOpResponse(super.getFailOpCode());
			return false;
		}//validar a data aqui
		super.setOpResponse(super.getSucessOpCode());
		return true;
	}
	
}
