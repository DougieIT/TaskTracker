package taskTracker.dataFormatting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateFormatter implements IDateFormatter {
	static SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy-HH");

	public DateFormatter(){
		format.setLenient(false);
	}

	@Override
	public  boolean isDateBeforeNow(Date dateThen){
		if(dateThen == null){
			return true;
		}
		Date dateNow = new Date();
		return dateThen.before(dateNow);
	}

	@Override
	public  String dateToString(Date dateAsDate) {
		String dateAsString = format.format(dateAsDate);
		if(dateAsString == null){
			return "";
		}
		return dateAsString;
	}

	@Override
	public  Date stringToDate(String dateAsString) {
		try {
			Date date = format.parse(dateAsString);
			return date;
		} catch(ParseException | NullPointerException e){
			return null;
		}
	}

	@Override
	public  String[] timesSinceDates(String[] myTimeThenArg) {
			String[] returnArray = new String[myTimeThenArg.length];
			for (int i = 0; i < myTimeThenArg.length; i++) {
				Date myTimeThen = stringToDate(myTimeThenArg[i]);
				if(myTimeThen == null) {
					returnArray[i] = "";
				} else {
					Date myTimeNow = new Date();
					long dateDifference = myTimeNow.getTime() - myTimeThen.getTime();
					long days = TimeUnit.MILLISECONDS.toDays(dateDifference);
					long hours = TimeUnit.MILLISECONDS.toHours(dateDifference - days * 1000 * 24 * 3600);
					String myString = days + " days " + hours + " hours";
					returnArray[i] = myString;
				}
			}
			return returnArray;
	}
}
