package taskTracker.dataFormatting;

import java.util.Date;

public interface IDateFormatter {
    public  String dateToString(Date dateAsDate);

    public  Date stringToDate(String dateAsString);

    public  String[] timesSinceDates(String[] myTimeThenArg);

    public  boolean isDateBeforeNow(Date dateThen);
}
