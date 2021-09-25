package com.ap.greenpole.transactioncomponent.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ap.greenpole.transactioncomponent.entity.ModuleRequest;
import com.ap.greenpole.transactioncomponent.enums.ApprovalStatus;

public class Utils {

	private static Logger log = LoggerFactory.getLogger(Utils.class);
	
	private Utils() {}
	
	public static boolean isEmptyString(String str) {
		return str == null || str.trim().equalsIgnoreCase("") || str.equalsIgnoreCase("null");
	}

	public static boolean isValidEmails(List<String> emails) {
		boolean status = false;
		List<Boolean> stat = new ArrayList<>();
		if (emails != null && !emails.isEmpty()) {

			for (String e : emails) {
				boolean s = isValidEmail(e);
				stat.add(s);

			}
			status = (!stat.isEmpty() && stat.contains(false));
		}

		return status;
	}

	public static boolean isValidEmail(String email) {
		String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		return email.matches(EMAIL_REGEX);
	}

	public static boolean isValidListValues(List<String> listValues) {
		boolean status = false;
		List<Boolean> stat = new ArrayList<>();
		for (String value : listValues) {
			boolean b = !isEmptyString(value);
			stat.add(b);
		}
		status = (!stat.isEmpty() && stat.contains(false));

		return status;
	}


	public static List<String> commaSeperatedToList(String src) {
		List<String> result = new ArrayList<>();
		if (!isEmptyString(src)) {
			result = Arrays.asList(StringUtils.split(src, ','));
		}
		return result;
	}

	public static String getDateString(Date date, String format) {
		SimpleDateFormat sdfDate = new SimpleDateFormat(format);
		sdfDate.format(0);
		
		return sdfDate.format(date);
	}

	static String splitCamelCase(String s) {
		return s.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
				"(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
	}

	public static ModuleRequest setRequestStatus(int status, Long approverId, String reason, ModuleRequest request) {
		if (request != null) {
			request.setStatus(status);
			request.setApproverId(approverId);
			request.setReason(reason);
		}
		return request;
	}

	public static boolean isValidDate(String format, String date) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		try {
			LocalDateTime ldt = LocalDateTime.parse(date, formatter);
			String result = ldt.format(formatter);
			
			return result.equals(date);
		}catch(DateTimeParseException ex) {
			try {
				LocalDate ldt = LocalDate.parse(date, formatter);
				String result = ldt.format(formatter);
				
				return result.equals(date);
			}catch(DateTimeParseException exp) {
				try {
					LocalTime ldt = LocalTime.parse(date, formatter);
					String result = ldt.format(formatter);
					
					return result.equals(date);
				}catch(DateTimeParseException exp1) {
					log.info("Unable to parse date: {}. Error: {}", date, exp1);
				}
			}
		}
		return false;
	}
	
	public static Date getDate(String format, String input) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = dateFormat.parse(input);
		}catch(ParseException ex) {
			throw new Exception(String.format("Unable to parse date: %s, Error:", input));
		}
		return date;
	}

	public static String getDateToString(Date date) {
		DateFormat dateFormat = new SimpleDateFormat(ApplicationConstant.FILTER_DATE_FORMAT);
		return dateFormat.format(date);
	}
	
	public static Calendar getEndOfDayTime(int hr, int min, int sec, int milSec) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hr);
		cal.set(Calendar.MINUTE, min);
		cal.set(Calendar.SECOND, sec);
		cal.set(Calendar.MILLISECOND, milSec);

		return cal;
	}
	
	public static String capitalizeFirstLetter(String input) {
		if (input == null || input.length() == 0) {
	        return input;
	    }
		return input.substring(0, 1).toUpperCase() + input.substring(1);
	}
	
	public static int getApprovalStatusCode(String status) {
		int result = -1;
		for(ApprovalStatus approvalStatus: ApprovalStatus.values()) {
			if(approvalStatus.name().equalsIgnoreCase(status)) {
				result = approvalStatus.getCode();
				break;
			}
		}
		return result;
	}

	public static String getRequestCode(String stockBroker) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmsss");
		String date = simpleDateFormat.format(new Date());
		return stockBroker + date;
	}
}
