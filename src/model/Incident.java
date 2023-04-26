package model;

import java.util.Date;

public class Incident {
	private Date date;
	private int incidentType, km;
	private String highway;
	private final int incidentTypes = 14; //numero de tipos de incidentes possiveis para ser cadastrados
	
	public Incident(Date date, int incidentType, int km, String highway) {
		super();
		this.date = date;
		this.incidentType = incidentType;
		this.km = km;
		this.highway = highway;
	}

	
	
	
}