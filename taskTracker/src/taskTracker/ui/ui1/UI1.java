package taskTracker.ui.ui1;

import taskTracker.dataHandling.IDataStorageHandler;
import taskTracker.ui.AbstractUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class UI1 extends AbstractUI {

    public UI1(IDataStorageHandler shortTermDataHandler, IDataStorageHandler longTermDataHandler) {
        super(shortTermDataHandler, longTermDataHandler);
        String[][] shortTermCombinedArray = shortTermDataHandler.getEntries();
        String[][] longTermCombinedArray = longTermDataHandler.getEntries();
        // Frame
        JFrame frame = JFrameSetUp();

        //spans the whole frame has two panels within next to each other. one sub panel contains the table, the other sub panel contains another sub panel which contains the buttons and text boxes
        JPanel framePanel = new JPanel();

        // Table
        JTable shortTermTable = shortTermTable(shortTermCombinedArray, new String[] {"Short Term Task", "Time", "Goal Date", "Times failed to meet goal"},400, 200, shortTermDataHandler);
        JTable longTermTable = longTermTable(longTermCombinedArray, new String[] {"Long Term Task", "Time"}, 400 ,200);



        JPanel outputPanel = outputPanel(shortTermTable, longTermTable);
        JPanel inputPanel = inputPanel(shortTermDataHandler, longTermDataHandler, shortTermTable, longTermTable);

        framePanel.add(outputPanel);
        framePanel.add(inputPanel);
        frame.add(framePanel);
        frame.pack();
    }

    public JFrame JFrameSetUp(){
        JFrame jf = new JFrame();
        jf.setVisible(true);
        jf.setTitle("Task Tracker");
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return jf;
    }

    public JPanel inputPanel(IDataStorageHandler shortTermFileHandler, IDataStorageHandler longTermFileHandler, JTable shortTermTable, JTable longTermTable) {
        JPanel inputPanel = new JPanel();
        GridLayout gc = new GridLayout(2, 1);
        inputPanel.setLayout(gc);

        //since two sub methods depend on this we define them here. If we didn't define them here and pass them into the subclasses it could get confusing as one subclass would need to be called before the other.
        JRadioButton longTermRadioButton = new JRadioButton("long term");
        JRadioButton shortTermRadioButton = new JRadioButton("short term");

        JPanel textBoxButtonSubPanel = textBoxButtonSubPanel(shortTermFileHandler, longTermFileHandler, shortTermRadioButton, longTermRadioButton, shortTermTable, longTermTable);
        JPanel radioButtonSubPanel = radioButtonsSubPanel(shortTermRadioButton, longTermRadioButton);

        //text box and buttons go on top of the radio buttons
        inputPanel.add(textBoxButtonSubPanel);
        inputPanel.add(radioButtonSubPanel);

        return inputPanel;
    }
    public JPanel textBoxButtonSubPanel(IDataStorageHandler shortTermFileHandler, IDataStorageHandler longTermFileHandler, JRadioButton shortTermRadioButton, JRadioButton longTermRadioButton, JTable shortTermTable, JTable longTermTable){
        JPanel textBoxButtonSubPanel = new JPanel();
        JTextField textBox1 = new JTextField();
        textBox1.setPreferredSize(new Dimension(100, 20));
        textBoxButtonSubPanel.add(textBox1);
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");

        addButton.setActionCommand(UI1.Actions.ADD.name());
        deleteButton.setActionCommand(UI1.Actions.DELETE.name());

        DefaultTableModel longTermTableModel = (DefaultTableModel) longTermTable.getModel();
        DefaultTableModel shortTermTableModel = (DefaultTableModel) shortTermTable.getModel();

        ActionListener onButtonPressed = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand() == UI1.Actions.ADD.name()) {

                    if (shortTermRadioButton.isSelected()) {
                        shortTermTableModel.addRow(new Object[]{textBox1.getText(), "now"});
                        try {
                            shortTermFileHandler.addEntry(textBox1.getText());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (longTermRadioButton.isSelected()) {
                        longTermTableModel.addRow(new Object[]{textBox1.getText(), "now"});
                        try {
                            longTermFileHandler.addEntry(textBox1.getText());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                if (e.getActionCommand() == UI1.Actions.DELETE.name()) {

                    if (longTermTable.getSelectedRow() != -1) {     //is a row selected
                        int selectedRow = longTermTable.getSelectedRow();
                        longTermFileHandler.deleteToDo(selectedRow); //deletetoDo must come before remove row as once the row is deleted their=
                        longTermTableModel.removeRow(selectedRow);

                    }
                    if (shortTermTable.getSelectedRow() != -1) {    //is a row selected
                        int selectedRow = shortTermTable.getSelectedRow();
                        shortTermFileHandler.deleteToDo(selectedRow);
                        shortTermTableModel.removeRow(selectedRow);

                    }

                }
            }
        };

        addButton.addActionListener(onButtonPressed);
        deleteButton.addActionListener(onButtonPressed);

        textBoxButtonSubPanel.add(addButton);
        textBoxButtonSubPanel.add(deleteButton);

        return textBoxButtonSubPanel;
    }
    public JPanel radioButtonsSubPanel(JRadioButton shortRB,JRadioButton longRB){
        JPanel bottomPanel = new JPanel();

        bottomPanel.add(shortRB);
        bottomPanel.add(longRB);
        ButtonGroup radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(shortRB);
        radioButtonGroup.add(longRB);

        return bottomPanel;
    }

    public JPanel outputPanel(JTable shortTermTable, JTable longTermTable) {
        JPanel outputPanel = new JPanel();

        JScrollPane shortTermTableScrollPane = new JScrollPane(shortTermTable);
        JScrollPane longTermTableScrollPane = new JScrollPane(longTermTable);

        //this is required so that two rows in two different tables aren't selected for deletion at once.
        MouseListener longTermTableMouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                shortTermTable.clearSelection();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }


        };
        longTermTable.addMouseListener(longTermTableMouseListener);
        MouseListener shortTermTableMouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                longTermTable.clearSelection();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
        shortTermTable.addMouseListener(shortTermTableMouseListener);

        outputPanel.add(shortTermTableScrollPane);
        outputPanel.add(longTermTableScrollPane);
        return outputPanel;
    }
    public JTable longTermTable(String[][] arrayData, String[] columnNames, int width, int height){
        JTable subLongTermTable = new JTable(new DefaultTableModel(arrayData, columnNames));
        subLongTermTable.setPreferredScrollableViewportSize(new Dimension(width, height));
        return subLongTermTable;
    }
    public JTable shortTermTable(String[][] arrayData, String[] columnNames, int width, int height, IDataStorageHandler shortTermFileHandler){

        //creating new table with data as parameters
        JTable shortTermTable = new UI1ShortTermTable(arrayData, columnNames, shortTermFileHandler);

        //size of table
        shortTermTable.setPreferredScrollableViewportSize(new Dimension(width, height));


        return shortTermTable;
    }

    public Object[][] getTableData(JTable table){
        int numberOfColumns = table.getColumnCount();
        int numberOfRows = table.getRowCount();

        Object[][] tableData = new Object[numberOfRows][numberOfColumns];

        for(int rowCounter = 0; rowCounter< numberOfRows; rowCounter++){
            for(int columnCounter = 0; columnCounter < numberOfColumns; columnCounter++){
                tableData[rowCounter][columnCounter] = table.getValueAt(rowCounter, columnCounter);
            }
        }
        return tableData;
    }

    private enum Actions {
        ADD,
        DELETE
    }

}
