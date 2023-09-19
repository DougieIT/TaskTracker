package taskTracker.ui;

import taskTracker.dataHandling.IDataStorageHandler;

public abstract class AbstractUI {

    IDataStorageHandler shortTermFile;
    IDataStorageHandler longTermFile;
    protected AbstractUI(IDataStorageHandler shortTermFile, IDataStorageHandler longTermFile){
        this.shortTermFile = shortTermFile;
        this.longTermFile = longTermFile;
    }
}
