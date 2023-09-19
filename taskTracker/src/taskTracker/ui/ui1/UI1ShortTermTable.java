package taskTracker.ui.ui1;

import taskTracker.dataFormatting.DateFormatter;
import taskTracker.dataHandling.IDataStorageHandler;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.awt.*;
import java.util.Date;

public class UI1ShortTermTable extends JTable {
    private static JTable currentJTable;
    private static DefaultTableModel customTableModel;
    private static final int columnToValidateDate = 2;
    static IDataStorageHandler shortTermDataHandler;

    private static TableModel paramSetup(String[][] arrayData, String[] columnNames){
        currentJTable = new JTable();
        currentJTable.setModel(new DefaultTableModel(arrayData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if(column == 0 | column == 1 | column == 3){
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        });
        customTableModel = (DefaultTableModel) currentJTable.getModel();

        //cell renderer not working for some reason
        currentJTable.setDefaultRenderer(customTableModel.getColumnClass(2), new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                System.out.println("invoked");
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(Color.RED);
                return c;
            }
        });

        customTableModel.setColumnIdentifiers(columnNames);
        customTableModel.addTableModelListener(tc);
        System.out.println(currentJTable.getValueAt(0,2));
        return customTableModel;

    }

    public UI1ShortTermTable(String[][] arrayData, String[] columnNames, IDataStorageHandler shortTermDataHandler){
        super(paramSetup(arrayData,columnNames));

        /*
        super(new DefaultTableModel(arrayData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if(column == 0 | column == 1 | column == 3){
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        });
        */


/*
        currentJTable = new JTable();
        currentJTable.setDefaultRenderer(getColumn("Goal Date").getClass(), new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(Color.RED);
                return c;
            }
        });
        currentJTable.setModel(tableModel);
*/
        this.shortTermDataHandler = shortTermDataHandler;
    }

    private static TableModelListener tc = new TableModelListener() {
        boolean changedFlag = false;
        @Override
        public void tableChanged(TableModelEvent e) {
            int highlightedRow = e.getFirstRow();
            int highlightedColumn = e.getColumn();

            if(highlightedColumn == columnToValidateDate){
                String valueInEntry = (String) currentJTable.getValueAt(highlightedRow, highlightedColumn);

                //we need this to prevent infinite recursion by the tableChangedEvent, if we want to change an event back it calls a new event and we get stuck in an infinite loop
                if(changedFlag == true){
                    changedFlag = false;
                    return;
                }

                if(validEntry(valueInEntry)){
                    changedFlag = true;
                    currentJTable.setValueAt(valueInEntry, highlightedRow,highlightedColumn);
                    System.out.println(valueInEntry + highlightedRow);
                    shortTermDataHandler.addGoalDate(valueInEntry, highlightedRow);
                    //shortTermDataHandler.ad
                } else {
                    changedFlag = true;
                    currentJTable.setValueAt("", highlightedRow, highlightedColumn);
                }
            }
        }

        private Boolean validEntry(String value) {
            String objectAsString = value;
            Date dateAsDate = (new DateFormatter()).stringToDate(objectAsString);
            if ((new DateFormatter()).isDateBeforeNow(dateAsDate)) {
                return false;
            } else {
                return true;
            }
        }
    };

}
