package ObjectClasses;

import javax.swing.table.AbstractTableModel;

public class TurnsTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	/* Table Columns */
	public String[] columnNames = { "Turn", "Day", "Actions", "Kills"};
	
	/* Table Data */
	public Object[][] data = {{"1", "1", "none", "1"}};
	
/****************************************************************************************
* GETTER: getColumnCount()
****************************************************************************************/
	public int getColumnCount() { 
		return columnNames.length; 
	}

	
	
/****************************************************************************************
* GETTER: getRowCount()
****************************************************************************************/
	public int getRowCount() { 
		return data.length; 
	}

	
	
/****************************************************************************************
* GETTER: getValueAt()
****************************************************************************************/
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}
	
	
		
/****************************************************************************************
* FUNCTION: isCellEditable()
****************************************************************************************/
	public boolean isCellEditable(int row, int col){
		if(col < 2){
			return false;
		} else {
			return true;
		}
	}
	
	
} /* CLOSES CLASS */
