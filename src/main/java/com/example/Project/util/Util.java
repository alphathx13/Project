package com.example.Project.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Util{
	
	public static String today() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return LocalDate.now().format(formatter);
	}
}