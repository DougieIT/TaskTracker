package taskTracker;

import taskTracker.dataFormatting.DateFormatter;
import taskTracker.dataFormatting.IDateFormatter;
import taskTracker.ui.AbstractUI;
import taskTracker.ui.ui1.UI1;
import taskTracker.dataHandling.IDataStorageHandler;
import taskTracker.dataHandling.TextFileHandler;

import java.io.IOException;
import java.text.ParseException;

public class Controller {

	public static void main(String[] args) throws ParseException, IOException {
		//For buttons https://stackoverflow.com/questions/5936261/how-to-add-action-listener-that-listens-to-multiple-buttons
		//

		IDateFormatter dateFormatter = new DateFormatter();
		IDataStorageHandler shortTermFiles = new TextFileHandler("shortTermTaskFile.txt", "shortTermTimeFile.txt", "shortTermGoalDateFile.txt", "shortTermTimesFailedFile.txt",  dateFormatter);
		IDataStorageHandler longTermFiles = new TextFileHandler("longTermTaskFile.txt", "longTermTimeFile.txt", "longTermGoalDateFile.txt", "longTermTimesFailed.txt", dateFormatter);
		AbstractUI oneUI = new UI1(shortTermFiles,longTermFiles);
	}
}