package com.example.Project.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Util{
	
	public static String today() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		return LocalDate.now().format(formatter);
	}
	
	public static String Now() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		return LocalDateTime.now().format(formatter);
	}
	
	public static boolean isEmpty(String str) {
		return str == null || str.equals("");
	}
	
	public static String jsReplace(String msg, String uri) {
		
		if (msg == null) {
			msg = "";
		}
		
		if (uri == null) {
			uri = "";
		}
		
		return String.format("""
							<script>
								const msg = '%s'.trim();
								
								if (msg.length > 0) {
									alert(msg);
								}
								
								location.replace('%s');
								
							</script>
							""", msg, uri);
	}
	
	public static String jsConfirm(String msg, String trueUri, String falseUri) {
		
		if (msg == null) {
			msg = "";
		}
		
		if (trueUri == null) {
			trueUri = "";
		}
		
		if (falseUri == null) {
			falseUri = "";
		}
		
		return String.format("""
							<script>
								const msg = '%s'.trim();
								
								if (msg.length > 0) {
								
									if(confirm(msg)) {
										location.replace('%s');
									} else {
										location.replace('%s');
									}
								}
								
							</script>
							""", msg, trueUri, falseUri);
	}

	public static String jsBack(String msg) {
		if (msg == null) {
			msg = "";
		}

		return String.format("""
				<script>
					const msg = '%s'.trim();
					
					if (msg.length > 0) {
						alert(msg);
					}
					
					history.back();
				</script>
			""", msg);
	}
	
	public static String createTempPassword() {
		return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
	}
	
}