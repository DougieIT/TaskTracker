package taskTracker.dataHandling;

import taskTracker.dataFormatting.IDateFormatter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class TextFileHandler implements IDataStorageHandler {


	//make sure you close all FileReaders/Writers to prevent errors

	private final File taskFile;
	private final File timeAddedFile;
	private final  File goalDateFile;
	private  final File timesFailedFile;

	private final IDateFormatter dateFormatter;
	private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy-HH");

	public TextFileHandler(String taskFileName, String timeAddedFileName, String goalDateFileName, String timesFailedFileName, IDateFormatter dateFormatter) {
		this.dateFormatter = dateFormatter;
		this.taskFile = new File(taskFileName);
		this.timeAddedFile = new File(timeAddedFileName);
		this.goalDateFile = new File(goalDateFileName);
		this.timesFailedFile = new File(timesFailedFileName);
		createFiles(taskFile,timeAddedFile,goalDateFile,timesFailedFile);
		checkIfDeadlineFailed();
	}

	private void checkIfDeadlineFailed(){
		String[] goalDates = readFile(goalDateFile);
		String[] timesFailed = readFile(timesFailedFile);
		for(int i = 0; i < goalDates.length; i++){
			String dateAsString = goalDates[i];
			try{
				Date dateAsDate = format.parse(dateAsString);
				if(dateAsDate.before(new Date())) {
					int timesFailedEntry = Integer.parseInt(timesFailed[i]) + 1;
					timesFailed[i] = String.valueOf(timesFailedEntry);
				}
			} catch (ParseException e){
			}
		}
		writeToFile(timesFailed, timesFailedFile);
	}

	private void createFiles(File taskFile, File timeAddedFile, File goalDateFile, File timesFailedFile) {
		try {
			if(taskFile.createNewFile()) {
				System.out.println("File Created: " + taskFile.getName());
			}
		} catch (IOException e) {
			System.out.println("Fail");
		}

		try {
			if(timeAddedFile.createNewFile()) {
				System.out.println("File Created: " + timeAddedFile.getName());
			}
		} catch (IOException e) {
			System.out.println("Fail");
		}

		try {
			if(goalDateFile.createNewFile()) {
				System.out.println("File Created: " + goalDateFile.getName());
			}
		} catch (IOException e) {
			System.out.println("Fail");
		}

		try {
			if(timesFailedFile.createNewFile()) {
				System.out.println("File Created: " + timesFailedFile.getName());
			}
		} catch (IOException e) {
			System.out.println("Fail");
		}
	}

	private void writeToFile(String[] data, File file){
		try{
			FileWriter fr = new FileWriter(file);
			for (String line: data) {
				fr.write(line + "\n");
			}
			fr.close();
		} catch(IOException e){
			System.out.println("write failed");
		}

	}

	private String[][] combineArrays(String[] taskArray, String[] timesAddedArray, String[] goalDateArray, String[] timesFailedArray){
		String[][] combinedArray = new String[taskArray.length][4];
		for (int i = 0; i < taskArray.length; i++) {
			combinedArray[i][0] = taskArray[i];
			combinedArray[i][1] = timesAddedArray[i];
			combinedArray[i][2] = goalDateArray[i];
			combinedArray[i][3] = timesFailedArray[i];
		}
		return combinedArray;
	}

	@Override
	public void addEntry(String addedTask) throws IOException {
		addToFile(addedTask, taskFile);
		addToFile("\n", goalDateFile);

		String dateToday = format.format(new Date());
		addToFile(dateToday, timeAddedFile);

		addToFile("0",timesFailedFile);
	}

	@Override
	public void addGoalDate(String dateToCompleteBy, int row) {
		String[] oldData = readFile(goalDateFile);
		System.out.println(Arrays.toString(oldData));
		String[] newData = replaceDataInArray(dateToCompleteBy, row, oldData);
		System.out.println(Arrays.toString(newData));
		writeToFile(newData, goalDateFile);
	}

	public void addToFile(String addLineTo, File file){
		String[] textAlreadyInFile = readFile(file);
		String[] addedToFile = Arrays.copyOf(textAlreadyInFile, textAlreadyInFile.length +1);
		addedToFile[textAlreadyInFile.length] = addLineTo;
		try {
			FileWriter fileWriter = new FileWriter(file);
			for (String line : textAlreadyInFile) {
				fileWriter.write(line + "\n");
			}
			fileWriter.write(addLineTo);
			fileWriter.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public String[][] getEntries() {

		//Read the files
		String[] rawTimesArray = readFile(timeAddedFile);
		String[] tasksArray = readFile(taskFile);
		String[] goalDatesArray = readFile(goalDateFile);
		String[] timesFailedArray = readFile(timesFailedFile);

		//converting date added to time since.
		String[] formattedTimesArray = dateFormatter.timesSinceDates(rawTimesArray);

		//combining the two arrays together
		String[][] combinedArray = combineArrays(tasksArray, formattedTimesArray, goalDatesArray, timesFailedArray);
		return combinedArray;
	}

	@Override
	public void deleteToDo(int row) {
		String[] taskData = readFile(taskFile);
		String[] timeData = readFile(timeAddedFile);
		String[] goalDateData = readFile(goalDateFile);

		String[] newTaskData = deleteIndexFromArray(row, taskData);
		String[] newTimeData = deleteIndexFromArray(row, timeData);
		String[] newGoalData = deleteIndexFromArray(row, goalDateData);

		writeToFile(newTaskData, taskFile);
		writeToFile(newTimeData, timeAddedFile);
		writeToFile(newGoalData, goalDateFile);
	}

	public static String[] replaceDataInArray(String dateToCompleteBy, int index, String[] array) {
		String[] arrayToAddTo = array;
		String[] newGoalDateData = new String[arrayToAddTo.length];

		for(int i = 0; i < arrayToAddTo.length; i++){
			newGoalDateData[i] = arrayToAddTo[i];
			if(i == index){
				newGoalDateData[i] = dateToCompleteBy;
			}
		}
		return newGoalDateData;
	}
	public static String[] deleteIndexFromArray(int index, String[] array){
		int newDataIndex = 0;
		String[] newArray = new String[array.length - 1];
		for(int oldDataIndex = 0; oldDataIndex < array.length; oldDataIndex++){
			if(oldDataIndex != index){
				newArray[newDataIndex] = array[oldDataIndex];
				newDataIndex++;
			}
		}
		return newArray;
	}

	public String[] readFile(File file){
		try {
			Scanner fileReader = new Scanner(file);
			ArrayList<String> lines = new ArrayList<String>();
			while(fileReader.hasNextLine()) {
				lines.add(fileReader.nextLine());
			}
			String[] tasks = lines.toArray(new String[]{});
			fileReader.close();
			return tasks;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}

