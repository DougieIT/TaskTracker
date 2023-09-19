package taskTracker.dataHandling;

import java.io.IOException;

//the use of this interface allows us to easily add new ways of accesssing data.
public interface IDataStorageHandler {
    void addEntry(String addedTask) throws IOException;
    void addGoalDate(String dateToCompleteBy, int row);
    String[][] getEntries();
    void deleteToDo(int row);
}
