package model;



public class Incident {
	private String date;
	private int incidentType, km;
	private String highway;
	private final int incidentTypes = 14; //numero de tipos de incidentes possiveis para ser cadastrados
	private int period;
	private String kmRange;
	private int idIncident;

	public Incident(int idIncident) {
		this.idIncident = idIncident;
	}

	public Incident(String date, String highway, int period, String kmRange) {
		this.date = date;
		this.highway = highway;
		this.period = period;
		this.kmRange = kmRange;
	}

	public Incident(String date, int incidentType, int km, String highway) {
		super();
		this.date = date;
		this.incidentType = incidentType;
		this.km = km;
		this.highway = highway;
	}

	public Incident(String date, String highway) {
		this.date = date;
		this.highway = highway;
	}

	public Incident(String date, int incidentType, int km, String highway, int idIncident) {
		this.date = date;
		this.incidentType = incidentType;
		this.km = km;
		this.highway = highway;
		this.idIncident = idIncident;
	}

	public String getDate() {
		return date;
	}

	public int getIncidentType() {
		return incidentType;
	}

	public int getKm() {
		return km;
	}

	public String getHighway() {
		return highway;
	}

	public int getIncidentTypes() {
		return incidentTypes;
	}

	public int getPeriod() {
		return period;
	}

	public String getKmRange() {
		return kmRange;
	}

	public int getIdIncident() {
		return idIncident;
	}
	
	
}
