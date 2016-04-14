package ru.thesooply;

import java.io.*;
import com.linuxense.javadbf.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SimpleTableDemo extends JPanel{
    private boolean DEBUG = true;

    public SimpleTableDemo() {
        super(new GridLayout(1,0));

        String workingDir = System.getProperty("user.dir");
        int i;
        int j;
        int k;
        String[][] strArr;
        String [] strFields;

        try {
            InputStream inputStream = new FileInputStream(workingDir + "/src/files/pickinfo.dbf");
            DBFReader reader = new DBFReader(inputStream);
            reader.setCharactersetName("CP866");

            int numberOfFields = reader.getFieldCount();
            int numberOfRows = reader.getRecordCount();
            strArr = new String[numberOfRows][numberOfFields];
            strFields = new String[numberOfFields];

            System.out.println("numberOfFields: " + numberOfFields);
            System.out.println("numberOfRows: " + numberOfRows);

            for (k = 0; k<numberOfFields; k++) {
                DBFField field = reader.getField(k);
                //System.out.print(" | " + field.getName());
                strFields[k] = field.getName();
                System.out.println("numberOfFields - k: " + k + " поле:" + strFields[k]);
            }

            System.out.println("Выход из цикла полей");

            i = 0;
            Object[] rowObjects;
            while ((rowObjects = reader.nextRecord()) != null) {
                for (j = 0; j < rowObjects.length; j++) {
                    strArr[i][j] = String.valueOf(rowObjects[j]);
                }
                i++;
            }

            inputStream.close();
        }
        catch (DBFException e){
            System.out.println(e.getMessage());
            strArr = new String[0][0];
            strFields = new String[0];

        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            strArr = new String[0][0];
            strFields = new String[0];
        }

        final JTable table = new JTable(strArr, strFields);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

        if (DEBUG) {
            table.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    printDebugData(table);
                }
            });
        }

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        add(scrollPane);
        table.setAutoCreateRowSorter(true);
    }

    private void printDebugData(JTable table) {
        int numRows = table.getRowCount();
        int numCols = table.getColumnCount();
        javax.swing.table.TableModel model = table.getModel();

        System.out.println("Value of data: ");
        for (int i=0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j=0; j < numCols; j++) {
                System.out.print("  " + model.getValueAt(i, j));
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("SimpleTableDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        SimpleTableDemo newContentPane = new SimpleTableDemo();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

}