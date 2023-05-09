package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Teste {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LocalDateTime myDateObj = LocalDateTime.now();
	    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String dataFormatada = myDateObj.format(myFormatObj);
		System.out.println("Data no formato string: " + dataFormatada);
		
		LocalDateTime dataFormatadaJava = LocalDateTime.parse(dataFormatada, myFormatObj);
		dataFormatadaJava = dataFormatadaJava.withDayOfMonth(20);
		System.out.println("data no formato localdatetime: " + dataFormatadaJava);
		
	}

}
