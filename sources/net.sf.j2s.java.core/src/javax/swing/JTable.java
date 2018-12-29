/*
 * Some portions of this file have been modified by Robert Hanson hansonr.at.stolaf.edu 2012-2017
 * for use in SwingJS via transpilation into JavaScript using Java2Script.
 *
 * Copyright (c) 1997, 2010, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package javax.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Vector;

import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.TableUI;
import javax.swing.plaf.UIResource;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import sun.swing.SwingLazyValue;
import sun.swing.SwingUtilities2;
// SwingJS  TODO import java.text.DateFormat;
//import sun.swing.SwingLazyValue;
import swingjs.plaf.JSComponentUI;

/**
 * The <code>JTable</code> is used to display and edit regular two-dimensional
 * tables of cells. See <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/table.html">How
 * to Use Tables</a> in <em>The Java Tutorial</em> for task-oriented
 * documentation and examples of using <code>JTable</code>.
 *
 * <p>
 * The <code>JTable</code> has many facilities that make it possible to
 * customize its rendering and editing but provides defaults for these features
 * so that simple tables can be set up easily. For example, to set up a table
 * with 10 rows and 10 columns of numbers:
 * <p>
 * 
 * <pre>
 * TableModel dataModel = new AbstractTableModel() {
 * 	public int getColumnCount() {
 * 		return 10;
 * 	}
 * 
 * 	public int getRowCount() {
 * 		return 10;
 * 	}
 * 
 * 	public Object getValueAt(int row, int col) {
 * 		return new Integer(row * col);
 * 	}
 * };
 * JTable table = new JTable(dataModel);
 * JScrollPane scrollpane = new JScrollPane(table);
 * </pre>
 * <p>
 * {@code JTable}s are typically placed inside of a {@code JScrollPane}. By
 * default, a {@code JTable} will adjust its width such that a horizontal
 * scrollbar is unnecessary. To allow for a horizontal scrollbar, invoke
 * {@link #setAutoResizeMode} with {@code AUTO_RESIZE_OFF}. Note that if you
 * wish to use a <code>JTable</code> in a standalone view (outside of a
 * <code>JScrollPane</code>) and want the header displayed, you can get it using
 * {@link #getTableHeader} and display it separately.
 * <p>
 * To enable sorting and filtering of rows, use a {@code RowSorter}. You can set
 * up a row sorter in either of two ways:
 * <ul>
 * <li>Directly set the {@code RowSorter}. For example:
 * {@code table.setRowSorter(new TableRowSorter(model))}.
 * <li>Set the {@code autoCreateRowSorter} property to {@code true}, so that the
 * {@code JTable} creates a {@code RowSorter} for you. For example:
 * {@code setAutoCreateRowSorter(true)}.
 * </ul>
 * <p>
 * When designing applications that use the <code>JTable</code> it is worth
 * paying close attention to the data structures that will represent the table's
 * data. The <code>DefaultTableModel</code> is a model implementation that uses
 * a <code>Vector</code> of <code>Vector</code>s of <code>Object</code>s to
 * store the cell values. As well as copying the data from an application into
 * the <code>DefaultTableModel</code>, it is also possible to wrap the data in
 * the methods of the <code>TableModel</code> interface so that the data can be
 * passed to the <code>JTable</code> directly, as in the example above. This
 * often results in more efficient applications because the model is free to
 * choose the internal representation that best suits the data. A good rule of
 * thumb for deciding whether to use the <code>AbstractTableModel</code> or the
 * <code>DefaultTableModel</code> is to use the <code>AbstractTableModel</code>
 * as the base class for creating subclasses and the
 * <code>DefaultTableModel</code> when subclassing is not required.
 * <p>
 * The "TableExample" directory in the demo area of the source distribution
 * gives a number of complete examples of <code>JTable</code> usage, covering
 * how the <code>JTable</code> can be used to provide an editable view of data
 * taken from a database and how to modify the columns in the display to use
 * specialized renderers and editors.
 * <p>
 * The <code>JTable</code> uses integers exclusively to refer to both the rows
 * and the columns of the model that it displays. The <code>JTable</code> simply
 * takes a tabular range of cells and uses <code>getValueAt(int, int)</code> to
 * retrieve the values from the model during painting. It is important to
 * remember that the column and row indexes returned by various
 * <code>JTable</code> methods are in terms of the <code>JTable</code> (the
 * view) and are not necessarily the same indexes used by the model.
 * <p>
 * By default, columns may be rearranged in the <code>JTable</code> so that the
 * view's columns appear in a different order to the columns in the model. This
 * does not affect the implementation of the model at all: when the columns are
 * reordered, the <code>JTable</code> maintains the new order of the columns
 * internally and converts its column indices before querying the model.
 * <p>
 * So, when writing a <code>TableModel</code>, it is not necessary to listen for
 * column reordering events as the model will be queried in its own coordinate
 * system regardless of what is happening in the view. In the examples area
 * there is a demonstration of a sorting algorithm making use of exactly this
 * technique to interpose yet another coordinate system where the order of the
 * rows is changed, rather than the order of the columns.
 * <p>
 * Similarly when using the sorting and filtering functionality provided by
 * <code>RowSorter</code> the underlying <code>TableModel</code> does not need
 * to know how to do sorting, rather <code>RowSorter</code> will handle it.
 * Coordinate conversions will be necessary when using the row based methods of
 * <code>JTable</code> with the underlying <code>TableModel</code>. All of
 * <code>JTable</code>s row based methods are in terms of the
 * <code>RowSorter</code>, which is not necessarily the same as that of the
 * underlying <code>TableModel</code>. For example, the selection is always in
 * terms of <code>JTable</code> so that when using <code>RowSorter</code> you
 * will need to convert using <code>convertRowIndexToView</code> or
 * <code>convertRowIndexToModel</code>. The following shows how to convert
 * coordinates from <code>JTable</code> to that of the underlying model:
 * 
 * <pre>
 * int[] selection = table.getSelectedRows();
 * for (int i = 0; i &lt; selection.length; i++) {
 * 	selection[i] = table.convertRowIndexToModel(selection[i]);
 * }
 * // selection is now in terms of the underlying TableModel
 * </pre>
 * <p>
 * By default if sorting is enabled <code>JTable</code> will persist the
 * selection and variable row heights in terms of the model on sorting. For
 * example if row 0, in terms of the underlying model, is currently selected,
 * after the sort row 0, in terms of the underlying model will be selected.
 * Visually the selection may change, but in terms of the underlying model it
 * will remain the same. The one exception to that is if the model index is no
 * longer visible or was removed. For example, if row 0 in terms of model was
 * filtered out the selection will be empty after the sort.
 * <p>
 * J2SE 5 adds methods to <code>JTable</code> to provide convenient access to
 * some common printing needs. Simple new {@link #print()} methods allow for
 * quick and easy addition of printing support to your application. In addition,
 * a new {@link #getPrintable} method is available for more advanced printing
 * needs.
 * <p>
 * As for all <code>JComponent</code> classes, you can use {@link InputMap} and
 * {@link ActionMap} to associate an {@link Action} object with a
 * {@link KeyStroke} and execute the action under specified conditions.
 * <p>
 * <strong>Warning:</strong> Swing is not thread safe. For more information see
 * <a href="package-summary.html#threading">Swing's Threading Policy</a>.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 *
 * @beaninfo attribute: isContainer false description: A component which
 *           displays data in a two dimensional grid.
 *
 * @author Philip Milne
 * @author Shannon Hickey (printing support)
 * @see javax.swing.table.DefaultTableModel
 * @see javax.swing.table.TableRowSorter
 */
/*
 * The first versions of the JTable, contained in Swing-0.1 through Swing-0.4,
 * were written by Alan Chung.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class JTable extends JComponent implements TableModelListener, Scrollable, TableColumnModelListener,
		ListSelectionListener, CellEditorListener, /* Accessible, */ RowSorterListener {

	
	//
	// Static Constants
	//

	/**
	 * Do not adjust column widths automatically; use a horizontal scrollbar
	 * instead.
	 */
	public static final int AUTO_RESIZE_OFF = 0;

	/**
	 * When a column is adjusted in the UI, adjust the next column the opposite
	 * way.
	 */
	public static final int AUTO_RESIZE_NEXT_COLUMN = 1;

	/**
	 * During UI adjustment, change subsequent columns to preserve the total
	 * width; this is the default behavior.
	 */
	public static final int AUTO_RESIZE_SUBSEQUENT_COLUMNS = 2;

	/**
	 * During all resize operations, apply adjustments to the last column only.
	 */
	public static final int AUTO_RESIZE_LAST_COLUMN = 3;

	/** During all resize operations, proportionately resize all columns. */
	public static final int AUTO_RESIZE_ALL_COLUMNS = 4;

	/**
	 * Printing modes, used in printing <code>JTable</code>s.
	 *
	 * @see #print(JTable.PrintMode, MessageFormat, MessageFormat, boolean,
	 *      PrintRequestAttributeSet, boolean)
	 * @see #getPrintable
	 * @since 1.5
	 */
	public enum PrintMode {

		/**
		 * Printing mode that prints the table at its current size, spreading
		 * both columns and rows across multiple pages if necessary.
		 */
		NORMAL,

		/**
		 * Printing mode that scales the output smaller, if necessary, to fit
		 * the table's entire width (and thereby all columns) on each page; Rows
		 * are spread across multiple pages as necessary.
		 */
		FIT_WIDTH
	}

	//
	// Instance Variables
	//

	/** The <code>TableModel</code> of the table. */
	protected TableModel dataModel;

	/** The <code>TableColumnModel</code> of the table. */
	protected TableColumnModel columnModel;

	/**
	 * The <code>ListSelectionModel</code> of the table, used to keep track of
	 * row selections.
	 */
	protected ListSelectionModel selectionModel;

	/** The <code>TableHeader</code> working with the table. */
	protected JTableHeader tableHeader;

	/** The height in pixels of each row in the table. */
	protected int rowHeight;

	/** The height in pixels of the margin between the cells in each row. */
	protected int rowMargin;

	/** The color of the grid. */
	protected Color gridColor;

	/**
	 * The table draws horizontal lines between cells if
	 * <code>showHorizontalLines</code> is true.
	 */
	protected boolean showHorizontalLines;

	/**
	 * The table draws vertical lines between cells if
	 * <code>showVerticalLines</code> is true.
	 */
	protected boolean showVerticalLines;

	/**
	 * Determines if the table automatically resizes the width of the table's
	 * columns to take up the entire width of the table, and how it does the
	 * resizing.
	 */
	protected int autoResizeMode;

	/**
	 * The table will query the <code>TableModel</code> to build the default set
	 * of columns if this is true.
	 */
	protected boolean autoCreateColumnsFromModel;

	/**
	 * Used by the <code>Scrollable</code> interface to determine the initial
	 * visible area.
	 */
	protected Dimension preferredViewportSize;

	/** True if row selection is allowed in this table. */
	protected boolean rowSelectionAllowed;

	/**
	 * Obsolete as of Java 2 platform v1.3. Please use the
	 * <code>rowSelectionAllowed</code> property and the
	 * <code>columnSelectionAllowed</code> property of the
	 * <code>columnModel</code> instead. Or use the method
	 * <code>getCellSelectionEnabled</code>.
	 */
	/*
	 * If true, both a row selection and a column selection can be non-empty at
	 * the same time, the selected cells are the the cells whose row and column
	 * are both selected.
	 */
	protected boolean cellSelectionEnabled;

	/** If editing, the <code>Component</code> that is handling the editing. */
	transient protected Component editorComp;

	/**
	 * The active cell editor object, that overwrites the screen real estate
	 * occupied by the current cell and allows the user to change its contents.
	 * {@code null} if the table isn't currently editing.
	 */
	transient protected TableCellEditor cellEditor;

	/** Identifies the column of the cell being edited. */
	transient protected int editingColumn;

	/** Identifies the row of the cell being edited. */
	transient protected int editingRow;

	/**
	 * A table of objects that display the contents of a cell, indexed by class
	 * as declared in <code>getColumnClass</code> in the <code>TableModel</code>
	 * interface.
	 */
	transient protected UIDefaults defaultRenderersByColumnClass;

	/**
	 * A table of objects that display and edit the contents of a cell, indexed
	 * by class as declared in <code>getColumnClass</code> in the
	 * <code>TableModel</code> interface.
	 */
	transient protected UIDefaults defaultEditorsByColumnClass;

	/** The foreground color of selected cells. */
	protected Color selectionForeground;

	/** The background color of selected cells. */
	protected Color selectionBackground;

	//
	// Private state
	//

	// WARNING: If you directly access this field you should also change the
	// SortManager.modelRowSizes field as well.
	protected SizeSequence rowModel;
	private boolean dragEnabled;
	private boolean surrendersFocusOnKeystroke;
	private PropertyChangeListener editorRemover = null;
	/**
	 * The last value of getValueIsAdjusting from the column selection models
	 * columnSelectionChanged notification. Used to test if a repaint is needed.
	 */
	private boolean columnSelectionAdjusting;
	/**
	 * The last value of getValueIsAdjusting from the row selection models
	 * valueChanged notification. Used to test if a repaint is needed.
	 */
	private boolean rowSelectionAdjusting;

	// /**
	// * To communicate errors between threads during printing.
	// */
	// private Throwable printError;

	/**
	 * True when setRowHeight(int) has been invoked.
	 */
	private boolean isRowHeightSet;

	/**
	 * If true, on a sort the selection is reset.
	 */
	private boolean updateSelectionOnSort;

	/**
	 * Information used in sorting.
	 */
	private transient SortManager sortManager;

	/**
	 * If true, when sorterChanged is invoked it's value is ignored.
	 */
	private boolean ignoreSortChange;

	/**
	 * Whether or not sorterChanged has been invoked.
	 */
	private boolean sorterChanged;

	/**
	 * If true, any time the model changes a new RowSorter is set.
	 */
	private boolean autoCreateRowSorter;

	/**
	 * Whether or not the table always fills the viewport height.
	 * 
	 * @see #setFillsViewportHeight
	 * @see #getScrollableTracksViewportHeight
	 */
	private boolean fillsViewportHeight;

	/**
	 * The drop mode for this component.
	 */
	private DropMode dropMode = DropMode.USE_SELECTION;

	// /**
	// * The drop location.
	// */
	// private transient DropLocation dropLocation;

	// /**
	// * A subclass of <code>TransferHandler.DropLocation</code> representing
	// * a drop location for a <code>JTable</code>.
	// *
	// * @see #getDropLocation
	// * @since 1.6
	// */
	// public static final class DropLocation extends
	// TransferHandler.DropLocation {
	// private final int row;
	// private final int col;
	// private final boolean isInsertRow;
	// private final boolean isInsertCol;
	//
	// private DropLocation(Point p, int row, int col,
	// boolean isInsertRow, boolean isInsertCol) {
	//
	// super(p);
	// this.row = row;
	// this.col = col;
	// this.isInsertRow = isInsertRow;
	// this.isInsertCol = isInsertCol;
	// }
	//
	// /**
	// * Returns the row index where a dropped item should be placed in the
	// * table. Interpretation of the value depends on the return of
	// * <code>isInsertRow()</code>. If that method returns
	// * <code>true</code> this value indicates the index where a new
	// * row should be inserted. Otherwise, it represents the value
	// * of an existing row on which the data was dropped. This index is
	// * in terms of the view.
	// * <p>
	// * <code>-1</code> indicates that the drop occurred over empty space,
	// * and no row could be calculated.
	// *
	// * @return the drop row
	// */
	// public int getRow() {
	// return row;
	// }
	//
	// /**
	// * Returns the column index where a dropped item should be placed in the
	// * table. Interpretation of the value depends on the return of
	// * <code>isInsertColumn()</code>. If that method returns
	// * <code>true</code> this value indicates the index where a new
	// * column should be inserted. Otherwise, it represents the value
	// * of an existing column on which the data was dropped. This index is
	// * in terms of the view.
	// * <p>
	// * <code>-1</code> indicates that the drop occurred over empty space,
	// * and no column could be calculated.
	// *
	// * @return the drop row
	// */
	// public int getColumn() {
	// return col;
	// }
	//
	// /**
	// * Returns whether or not this location represents an insert
	// * of a row.
	// *
	// * @return whether or not this is an insert row
	// */
	// public boolean isInsertRow() {
	// return isInsertRow;
	// }
	//
	// /**
	// * Returns whether or not this location represents an insert
	// * of a column.
	// *
	// * @return whether or not this is an insert column
	// */
	// public boolean isInsertColumn() {
	// return isInsertCol;
	// }
	//
	// /**
	// * Returns a string representation of this drop location.
	// * This method is intended to be used for debugging purposes,
	// * and the content and format of the returned string may vary
	// * between implementations.
	// *
	// * @return a string representation of this drop location
	// */
	// public String toString() {
	// return getClass().getName()
	//// + "[dropPoint=" + getDropPoint() + ","
	// + "row=" + row + ","
	// + "column=" + col + ","
	// + "insertRow=" + isInsertRow + ","
	// + "insertColumn=" + isInsertCol + "]";
	// }
	// }

	//
	// Constructors
	//

	/**
	 * Constructs a default <code>JTable</code> that is initialized with a
	 * default data model, a default column model, and a default selection
	 * model.
	 *
	 * @see #createDefaultDataModel
	 * @see #createDefaultColumnModel
	 * @see #createDefaultSelectionModel
	 */
	public JTable() {
		this(null, null, null);
	}

	/**
	 * Constructs a <code>JTable</code> that is initialized with <code>dm</code>
	 * as the data model, a default column model, and a default selection model.
	 *
	 * @param dm
	 *            the data model for the table
	 * @see #createDefaultColumnModel
	 * @see #createDefaultSelectionModel
	 */
	public JTable(TableModel dm) {
		this(dm, null, null);
	}

	/**
	 * Constructs a <code>JTable</code> that is initialized with <code>dm</code>
	 * as the data model, <code>cm</code> as the column model, and a default
	 * selection model.
	 *
	 * @param dm
	 *            the data model for the table
	 * @param cm
	 *            the column model for the table
	 * @see #createDefaultSelectionModel
	 */
	public JTable(TableModel dm, TableColumnModel cm) {
		this(dm, cm, null);
	}

	/**
	 * Constructs a <code>JTable</code> that is initialized with <code>dm</code>
	 * as the data model, <code>cm</code> as the column model, and
	 * <code>sm</code> as the selection model. If any of the parameters are
	 * <code>null</code> this method will initialize the table with the
	 * corresponding default model. The <code>autoCreateColumnsFromModel</code>
	 * flag is set to false if <code>cm</code> is non-null, otherwise it is set
	 * to true and the column model is populated with suitable
	 * <code>TableColumns</code> for the columns in <code>dm</code>.
	 *
	 * @param dm
	 *            the data model for the table
	 * @param cm
	 *            the column model for the table
	 * @param sm
	 *            the row selection model for the table
	 * @see #createDefaultDataModel
	 * @see #createDefaultColumnModel
	 * @see #createDefaultSelectionModel
	 */
	public JTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
		super();
		setLayout(null);

		// setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
		// JComponent.getManagingFocusForwardTraversalKeys());
		// setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
		// JComponent.getManagingFocusBackwardTraversalKeys());
		if (cm == null) {
			cm = createDefaultColumnModel();
			autoCreateColumnsFromModel = true;
		}
		setColumnModel(cm);

		if (sm == null) {
			sm = createDefaultSelectionModel();
		}
		setSelectionModel(sm);

		// Set the model last, that way if the autoCreatColumnsFromModel has
		// been set above, we will automatically populate an empty columnModel
		// with suitable columns for the new model.
		if (dm == null) {
			dm = createDefaultDataModel();
		}
		setModel(dm);

		initializeLocalVars();
		updateUI();
	}

	@Override
	public String getUIClassID() {
		return "TableUI";
	}


	/**
	 * Constructs a <code>JTable</code> with <code>numRows</code> and
	 * <code>numColumns</code> of empty cells using
	 * <code>DefaultTableModel</code>. The columns will have names of the form
	 * "A", "B", "C", etc.
	 *
	 * @param numRows
	 *            the number of rows the table holds
	 * @param numColumns
	 *            the number of columns the table holds
	 * @see javax.swing.table.DefaultTableModel
	 */
	public JTable(int numRows, int numColumns) {
		this(new DefaultTableModel(numRows, numColumns));
	}

	/**
	 * Constructs a <code>JTable</code> to display the values in the
	 * <code>Vector</code> of <code>Vectors</code>, <code>rowData</code>, with
	 * column names, <code>columnNames</code>. The <code>Vectors</code>
	 * contained in <code>rowData</code> should contain the values for that row.
	 * In other words, the value of the cell at row 1, column 5 can be obtained
	 * with the following code:
	 * <p>
	 * 
	 * <pre>
	 * ((Vector) rowData.elementAt(1)).elementAt(5);
	 * </pre>
	 * <p>
	 * 
	 * @param rowData
	 *            the data for the new table
	 * @param columnNames
	 *            names of each column
	 */
	public JTable(Vector rowData, Vector columnNames) {
		this(new DefaultTableModel(rowData, columnNames));
	}

	/**
	 * Constructs a <code>JTable</code> to display the values in the two
	 * dimensional array, <code>rowData</code>, with column names,
	 * <code>columnNames</code>. <code>rowData</code> is an array of rows, so
	 * the value of the cell at row 1, column 5 can be obtained with the
	 * following code:
	 * <p>
	 * 
	 * <pre>
	 *  rowData[1][5];
	 * </pre>
	 * <p>
	 * All rows must be of the same length as <code>columnNames</code>.
	 * <p>
	 * 
	 * @param rowData
	 *            the data for the new table
	 * @param columnNames
	 *            names of each column
	 */
	public JTable(final Object[][] rowData, final Object[] columnNames) {
		this(new AbstractTableModel() {
			@Override
			public String getColumnName(int column) {
				return columnNames[column].toString();
			}

			@Override
			public int getRowCount() {
				return rowData.length;
			}

			@Override
			public int getColumnCount() {
				return columnNames.length;
			}

			@Override
			public Object getValueAt(int row, int col) {
				return rowData[row][col];
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return true;
			}

			@Override
			public void setValueAt(Object value, int row, int col) {
				rowData[row][col] = value;
				fireTableCellUpdated(row, col);
			}
		});
	}

	/**
	 * Calls the <code>configureEnclosingScrollPane</code> method.
	 *
	 * @see #configureEnclosingScrollPane
	 */
	@Override
	public void addNotify() {
		super.addNotify();
		configureEnclosingScrollPane();
	}

	/**
	 * If this <code>JTable</code> is the <code>viewportView</code> of an
	 * enclosing <code>JScrollPane</code> (the usual situation), configure this
	 * <code>ScrollPane</code> by, amongst other things, installing the table's
	 * <code>tableHeader</code> as the <code>columnHeaderView</code> of the
	 * scroll pane. When a <code>JTable</code> is added to a
	 * <code>JScrollPane</code> in the usual way, using
	 * <code>new JScrollPane(myTable)</code>, <code>addNotify</code> is called
	 * in the <code>JTable</code> (when the table is added to the viewport).
	 * <code>JTable</code>'s <code>addNotify</code> method in turn calls this
	 * method, which is protected so that this default installation procedure
	 * can be overridden by a subclass.
	 *
	 * @see #addNotify
	 */
	protected void configureEnclosingScrollPane() {
		Container p = getParent();
		if (p instanceof JViewport) {
			Container gp = p.getParent();
			if (gp instanceof JScrollPane) {
				JScrollPane scrollPane = (JScrollPane) gp;
				// Make certain we are the viewPort's view and not, for
				// example, the rowHeaderView of the scrollPane -
				// an implementor of fixed columns might do this.
				JViewport viewport = scrollPane.getViewport();
				if (viewport == null || viewport.getView() != this) {
					return;
				}
				scrollPane.setColumnHeaderView(getTableHeader());
				// configure the scrollpane for any LAF dependent settings
				configureEnclosingScrollPaneUI();
			}
		}
	}

	/**
	 * This is a sub-part of configureEnclosingScrollPane() that configures
	 * anything on the scrollpane that may change when the look and feel
	 * changes. It needed to be split out from configureEnclosingScrollPane() so
	 * that it can be called from updateUI() when the LAF changes without
	 * causing the regression found in bug 6687962. This was because updateUI()
	 * is called from the constructor which then caused
	 * configureEnclosingScrollPane() to be called by the constructor which
	 * changes its contract for any subclass that overrides it. So by splitting
	 * it out in this way configureEnclosingScrollPaneUI() can be called both
	 * from configureEnclosingScrollPane() and updateUI() in a safe manor.
	 */
	private void configureEnclosingScrollPaneUI() {
		Container p = getParent();
		if (p instanceof JViewport) {
			Container gp = p.getParent();
			if (gp instanceof JScrollPane) {
				JScrollPane scrollPane = (JScrollPane) gp;
				// Make certain we are the viewPort's view and not, for
				// example, the rowHeaderView of the scrollPane -
				// an implementor of fixed columns might do this.
				JViewport viewport = scrollPane.getViewport();
				if (viewport == null || viewport.getView() != this) {
					return;
				}
				// scrollPane.getViewport().setBackingStoreEnabled(true);
				Border border = scrollPane.getBorder();
				if (border == null || border instanceof UIResource) {
					Border scrollPaneBorder = UIManager.getBorder("Table.scrollPaneBorder");
					if (scrollPaneBorder != null) {
						scrollPane.setBorder(scrollPaneBorder);
					}
				}
				// add JScrollBar corner component if available from LAF and not
				// already set by the user
				Component corner = scrollPane.getCorner(JScrollPane.UPPER_TRAILING_CORNER);
				if (corner == null || corner instanceof UIResource) {
					corner = null;
					try {
						corner = (Component) UIManager.get("Table.scrollPaneCornerComponent");
					} catch (Exception e) {
						// just ignore and don't set corner
					}
					scrollPane.setCorner(JScrollPane.UPPER_TRAILING_CORNER, corner);
				}
			}
		}
	}

	/**
	 * Calls the <code>unconfigureEnclosingScrollPane</code> method.
	 *
	 * @see #unconfigureEnclosingScrollPane
	 */
	@Override
	public void removeNotify() {
		// KeyboardFocusManager.getCurrentKeyboardFocusManager().
		// removePropertyChangeListener("permanentFocusOwner", editorRemover);
		// editorRemover = null;
		// unconfigureEnclosingScrollPane();
		// super.removeNotify();
	}

	/**
	 * Reverses the effect of <code>configureEnclosingScrollPane</code> by
	 * replacing the <code>columnHeaderView</code> of the enclosing scroll pane
	 * with <code>null</code>. <code>JTable</code>'s <code>removeNotify</code>
	 * method calls this method, which is protected so that this default
	 * uninstallation procedure can be overridden by a subclass.
	 *
	 * @see #removeNotify
	 * @see #configureEnclosingScrollPane
	 * @since 1.3
	 */
	protected void unconfigureEnclosingScrollPane() {
		Container p = getParent();
		if (p instanceof JViewport) {
			Container gp = p.getParent();
			if (gp instanceof JScrollPane) {
				JScrollPane scrollPane = (JScrollPane) gp;
				// Make certain we are the viewPort's view and not, for
				// example, the rowHeaderView of the scrollPane -
				// an implementor of fixed columns might do this.
				JViewport viewport = scrollPane.getViewport();
				if (viewport == null || viewport.getView() != this) {
					return;
				}
				scrollPane.setColumnHeaderView(null);
				// remove ScrollPane corner if one was added by the LAF
				Component corner = scrollPane.getCorner(JScrollPane.UPPER_TRAILING_CORNER);
				if (corner instanceof UIResource) {
					scrollPane.setCorner(JScrollPane.UPPER_TRAILING_CORNER, null);
				}
			}
		}
	}

	@Override
	void setUIProperty(String propertyName, Object value) {
		if (propertyName == "rowHeight") {
			if (!isRowHeightSet) {
				setRowHeight(((Number) value).intValue());
				isRowHeightSet = false;
			}
			return;
		}
		super.setUIProperty(propertyName, value);
	}

	//
	// Static Methods
	//

	/**
	 * Equivalent to <code>new JScrollPane(aTable)</code>.
	 *
	 * @deprecated As of Swing version 1.0.2, replaced by
	 *             <code>new JScrollPane(aTable)</code>.
	 */
	@Deprecated
	static public JScrollPane createScrollPaneForTable(JTable aTable) {
		return new JScrollPane(aTable);
	}

	//
	// Table Attributes
	//

	/**
	 * Sets the <code>tableHeader</code> working with this <code>JTable</code>
	 * to <code>newHeader</code>. It is legal to have a <code>null</code>
	 * <code>tableHeader</code>.
	 *
	 * @param tableHeader
	 *            new tableHeader
	 * @see #getTableHeader
	 * @beaninfo bound: true description: The JTableHeader instance which
	 *           renders the column headers.
	 */
	public void setTableHeader(JTableHeader tableHeader) {
		if (this.tableHeader != tableHeader) {
			JTableHeader old = this.tableHeader;
			// Release the old header
			if (old != null) {
				old.setTable(null);
			}
			this.tableHeader = tableHeader;
			if (tableHeader != null) {
				tableHeader.setTable(this);
			}
			firePropertyChange("tableHeader", old, tableHeader);
		}
	}

	/**
	 * Returns the <code>tableHeader</code> used by this <code>JTable</code>.
	 *
	 * @return the <code>tableHeader</code> used by this table
	 * @see #setTableHeader
	 */
	public JTableHeader getTableHeader() {
		return tableHeader;
	}

	/**
	 * Sets the height, in pixels, of all cells to <code>rowHeight</code>,
	 * revalidates, and repaints. The height of the cells will be equal to the
	 * row height minus the row margin.
	 *
	 * @param rowHeight
	 *            new row height
	 * @exception IllegalArgumentException
	 *                if <code>rowHeight</code> is less than 1
	 * @see #getRowHeight
	 * @beaninfo bound: true description: The height of the specified row.
	 */
	public void setRowHeight(int rowHeight) {
		if (rowHeight <= 0) {
			throw new IllegalArgumentException("New row height less than 1");
		}
		int old = this.rowHeight;
		this.rowHeight = rowHeight;
		rowModel = null;
		if (sortManager != null) {
			sortManager.modelRowSizes = null;
		}
		isRowHeightSet = true;
		resizeAndRepaint();
		firePropertyChange("rowHeight", old, rowHeight);
	}

	/**
	 * Returns the height of a table row, in pixels. The default row height is
	 * 16.0.
	 *
	 * @return the height in pixels of a table row
	 * @see #setRowHeight
	 */
	public int getRowHeight() {
		return rowHeight;
	}

	private SizeSequence getRowModel() {
		if (rowModel == null) {
			rowModel = new SizeSequence(getRowCount(), getRowHeight());
		}
		return rowModel;
	}

	/**
	 * Sets the height for <code>row</code> to <code>rowHeight</code>,
	 * revalidates, and repaints. The height of the cells in this row will be
	 * equal to the row height minus the row margin.
	 *
	 * @param row
	 *            the row whose height is being changed
	 * @param rowHeight
	 *            new row height, in pixels
	 * @exception IllegalArgumentException
	 *                if <code>rowHeight</code> is less than 1
	 * @beaninfo bound: true description: The height in pixels of the cells in
	 *           <code>row</code>
	 * @since 1.3
	 */
	public void setRowHeight(int row, int rowHeight) {
		if (rowHeight <= 0) {
			throw new IllegalArgumentException("New row height less than 1");
		}
		getRowModel().setSize(row, rowHeight);
		if (sortManager != null) {
			sortManager.setViewRowHeight(row, rowHeight);
		}
		resizeAndRepaint();
	}

	/**
	 * Returns the height, in pixels, of the cells in <code>row</code>.
	 * 
	 * @param row
	 *            the row whose height is to be returned
	 * @return the height, in pixels, of the cells in the row
	 * @since 1.3
	 */
	public int getRowHeight(int row) {
		return (rowModel == null) ? getRowHeight() : rowModel.getSize(row);
	}

	/**
	 * Sets the amount of empty space between cells in adjacent rows.
	 *
	 * @param rowMargin
	 *            the number of pixels between cells in a row
	 * @see #getRowMargin
	 * @beaninfo bound: true description: The amount of space between cells.
	 */
	public void setRowMargin(int rowMargin) {
		int old = this.rowMargin;
		this.rowMargin = rowMargin;
		resizeAndRepaint();
		firePropertyChange("rowMargin", old, rowMargin);
	}

	/**
	 * Gets the amount of empty space, in pixels, between cells. Equivalent to:
	 * <code>getIntercellSpacing().height</code>.
	 * 
	 * @return the number of pixels between cells in a row
	 *
	 * @see #setRowMargin
	 */
	public int getRowMargin() {
		return rowMargin;
	}

	/**
	 * Sets the <code>rowMargin</code> and the <code>columnMargin</code> -- the
	 * height and width of the space between cells -- to
	 * <code>intercellSpacing</code>.
	 *
	 * @param intercellSpacing
	 *            a <code>Dimension</code> specifying the new width and height
	 *            between cells
	 * @see #getIntercellSpacing
	 * @beaninfo description: The spacing between the cells, drawn in the
	 *           background color of the JTable.
	 */
	public void setIntercellSpacing(Dimension intercellSpacing) {
		// Set the rowMargin here and columnMargin in the TableColumnModel
		setRowMargin(intercellSpacing.height);
		getColumnModel().setColumnMargin(intercellSpacing.width);

		resizeAndRepaint();
	}

	/**
	 * Returns the horizontal and vertical space between cells. The default
	 * spacing is (1, 1), which provides room to draw the grid.
	 *
	 * @return the horizontal and vertical spacing between cells
	 * @see #setIntercellSpacing
	 */
	public Dimension getIntercellSpacing() {
		return new Dimension(getColumnModel().getColumnMargin(), rowMargin);
	}

	/**
	 * Sets the color used to draw grid lines to <code>gridColor</code> and
	 * redisplays. The default color is look and feel dependent.
	 *
	 * @param gridColor
	 *            the new color of the grid lines
	 * @exception IllegalArgumentException
	 *                if <code>gridColor</code> is <code>null</code>
	 * @see #getGridColor
	 * @beaninfo bound: true description: The grid color.
	 */
	public void setGridColor(Color gridColor) {
		if (gridColor == null) {
			throw new IllegalArgumentException("New color is null");
		}
		Color old = this.gridColor;
		this.gridColor = gridColor;
		firePropertyChange("gridColor", old, gridColor);
		// Redraw
		repaint();
	}

	/**
	 * Returns the color used to draw grid lines. The default color is look and
	 * feel dependent.
	 *
	 * @return the color used to draw grid lines
	 * @see #setGridColor
	 */
	public Color getGridColor() {
		return gridColor;
	}

	/**
	 * Sets whether the table draws grid lines around cells. If
	 * <code>showGrid</code> is true it does; if it is false it doesn't. There
	 * is no <code>getShowGrid</code> method as this state is held in two
	 * variables -- <code>showHorizontalLines</code> and
	 * <code>showVerticalLines</code> -- each of which can be queried
	 * independently.
	 *
	 * @param showGrid
	 *            true if table view should draw grid lines
	 *
	 * @see #setShowVerticalLines
	 * @see #setShowHorizontalLines
	 * @beaninfo description: The color used to draw the grid lines.
	 */
	public void setShowGrid(boolean showGrid) {
		setShowHorizontalLines(showGrid);
		setShowVerticalLines(showGrid);

		// Redraw
		repaint();
	}

//	@Override
//    protected void validateTree() {
//		super.validateTree();
//	}
//
//	@Override
//	public void repaint() {
//		super.repaint();
//	}
//	
//	@Override
//	public void repaint(int x, int y, int width, int height) {
//		super.repaint(x, y, width, height);
//	}
	
	/**
	 * Sets whether the table draws horizontal lines between cells. If
	 * <code>showHorizontalLines</code> is true it does; if it is false it
	 * doesn't.
	 *
	 * @param showHorizontalLines
	 *            true if table view should draw horizontal lines
	 * @see #getShowHorizontalLines
	 * @see #setShowGrid
	 * @see #setShowVerticalLines
	 * @beaninfo bound: true description: Whether horizontal lines should be
	 *           drawn in between the cells.
	 */
	public void setShowHorizontalLines(boolean showHorizontalLines) {
		boolean old = this.showHorizontalLines;
		this.showHorizontalLines = showHorizontalLines;
		firePropertyChange("showHorizontalLines", old, showHorizontalLines);

		// Redraw
		repaint();
	}

	/**
	 * Sets whether the table draws vertical lines between cells. If
	 * <code>showVerticalLines</code> is true it does; if it is false it
	 * doesn't.
	 *
	 * @param showVerticalLines
	 *            true if table view should draw vertical lines
	 * @see #getShowVerticalLines
	 * @see #setShowGrid
	 * @see #setShowHorizontalLines
	 * @beaninfo bound: true description: Whether vertical lines should be drawn
	 *           in between the cells.
	 */
	public void setShowVerticalLines(boolean showVerticalLines) {
		boolean old = this.showVerticalLines;
		this.showVerticalLines = showVerticalLines;
		firePropertyChange("showVerticalLines", old, showVerticalLines);
		// Redraw
		repaint();
	}

	/**
	 * Returns true if the table draws horizontal lines between cells, false if
	 * it doesn't. The default is true.
	 *
	 * @return true if the table draws horizontal lines between cells, false if
	 *         it doesn't
	 * @see #setShowHorizontalLines
	 */
	public boolean getShowHorizontalLines() {
		return showHorizontalLines;
	}

	/**
	 * Returns true if the table draws vertical lines between cells, false if it
	 * doesn't. The default is true.
	 *
	 * @return true if the table draws vertical lines between cells, false if it
	 *         doesn't
	 * @see #setShowVerticalLines
	 */
	public boolean getShowVerticalLines() {
		return showVerticalLines;
	}

	/**
	 * Sets the table's auto resize mode when the table is resized. For further
	 * information on how the different resize modes work, see
	 * {@link #doLayout}.
	 *
	 * @param mode
	 *            One of 5 legal values: AUTO_RESIZE_OFF,
	 *            AUTO_RESIZE_NEXT_COLUMN, AUTO_RESIZE_SUBSEQUENT_COLUMNS,
	 *            AUTO_RESIZE_LAST_COLUMN, AUTO_RESIZE_ALL_COLUMNS
	 *
	 * @see #getAutoResizeMode
	 * @see #doLayout
	 * @beaninfo bound: true description: Whether the columns should adjust
	 *           themselves automatically. enum: AUTO_RESIZE_OFF
	 *           JTable.AUTO_RESIZE_OFF AUTO_RESIZE_NEXT_COLUMN
	 *           JTable.AUTO_RESIZE_NEXT_COLUMN AUTO_RESIZE_SUBSEQUENT_COLUMNS
	 *           JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS AUTO_RESIZE_LAST_COLUMN
	 *           JTable.AUTO_RESIZE_LAST_COLUMN AUTO_RESIZE_ALL_COLUMNS
	 *           JTable.AUTO_RESIZE_ALL_COLUMNS
	 */
	public void setAutoResizeMode(int mode) {
		if ((mode == AUTO_RESIZE_OFF) || (mode == AUTO_RESIZE_NEXT_COLUMN) || (mode == AUTO_RESIZE_SUBSEQUENT_COLUMNS)
				|| (mode == AUTO_RESIZE_LAST_COLUMN) || (mode == AUTO_RESIZE_ALL_COLUMNS)) {
			int old = autoResizeMode;
			autoResizeMode = mode;
			resizeAndRepaint();
			if (tableHeader != null) {
				tableHeader.resizeAndRepaint();
			}
			firePropertyChange("autoResizeMode", old, autoResizeMode);
		}
	}

	/**
	 * Returns the auto resize mode of the table. The default mode is
	 * AUTO_RESIZE_SUBSEQUENT_COLUMNS.
	 *
	 * @return the autoResizeMode of the table
	 *
	 * @see #setAutoResizeMode
	 * @see #doLayout
	 */
	public int getAutoResizeMode() {
		return autoResizeMode;
	}

	/**
	 * Sets this table's <code>autoCreateColumnsFromModel</code> flag. This
	 * method calls <code>createDefaultColumnsFromModel</code> if
	 * <code>autoCreateColumnsFromModel</code> changes from false to true.
	 *
	 * @param autoCreateColumnsFromModel
	 *            true if <code>JTable</code> should automatically create
	 *            columns
	 * @see #getAutoCreateColumnsFromModel
	 * @see #createDefaultColumnsFromModel
	 * @beaninfo bound: true description: Automatically populates the
	 *           columnModel when a new TableModel is submitted.
	 */
	public void setAutoCreateColumnsFromModel(boolean autoCreateColumnsFromModel) {
		if (this.autoCreateColumnsFromModel != autoCreateColumnsFromModel) {
			boolean old = this.autoCreateColumnsFromModel;
			this.autoCreateColumnsFromModel = autoCreateColumnsFromModel;
			if (autoCreateColumnsFromModel) {
				createDefaultColumnsFromModel();
			}
			firePropertyChange("autoCreateColumnsFromModel", old, autoCreateColumnsFromModel);
		}
	}

	/**
	 * Determines whether the table will create default columns from the model.
	 * If true, <code>setModel</code> will clear any existing columns and create
	 * new columns from the new model. Also, if the event in the
	 * <code>tableChanged</code> notification specifies that the entire table
	 * changed, then the columns will be rebuilt. The default is true.
	 *
	 * @return the autoCreateColumnsFromModel of the table
	 * @see #setAutoCreateColumnsFromModel
	 * @see #createDefaultColumnsFromModel
	 */
	public boolean getAutoCreateColumnsFromModel() {
		return autoCreateColumnsFromModel;
	}

	/**
	 * Creates default columns for the table from the data model using the
	 * <code>getColumnCount</code> method defined in the <code>TableModel</code>
	 * interface.
	 * <p>
	 * Clears any existing columns before creating the new columns based on
	 * information from the model.
	 *
	 * @see #getAutoCreateColumnsFromModel
	 */
	public void createDefaultColumnsFromModel() {
		TableModel m = getModel();
		if (m != null) {
			// Remove any current columns
			TableColumnModel cm = getColumnModel();
			while (cm.getColumnCount() > 0) {
				cm.removeColumn(cm.getColumn(0));
			}

			// Create new columns from the data model info
			for (int i = 0; i < m.getColumnCount(); i++) {
				TableColumn newColumn = new TableColumn(i);
				addColumn(newColumn);
			}
		}
	}

	/**
	 * Sets a default cell renderer to be used if no renderer has been set in a
	 * <code>TableColumn</code>. If renderer is <code>null</code>, removes the
	 * default renderer for this column class.
	 *
	 * @param columnClass
	 *            set the default cell renderer for this columnClass
	 * @param renderer
	 *            default cell renderer to be used for this columnClass
	 * @see #getDefaultRenderer
	 * @see #setDefaultEditor
	 */
	public void setDefaultRenderer(Class<?> columnClass, TableCellRenderer renderer) {
		if (renderer != null) {
			defaultRenderersByColumnClass.put(columnClass, renderer);
		} else {
			defaultRenderersByColumnClass.remove(columnClass);
		}
	}

	/**
	 * Returns the cell renderer to be used when no renderer has been set in a
	 * <code>TableColumn</code>. During the rendering of cells the renderer is
	 * fetched from a <code>Hashtable</code> of entries according to the class
	 * of the cells in the column. If there is no entry for this
	 * <code>columnClass</code> the method returns the entry for the most
	 * specific superclass. The <code>JTable</code> installs entries for
	 * <code>Object</code>, <code>Number</code>, and <code>Boolean</code>, all
	 * of which can be modified or replaced.
	 *
	 * @param columnClass
	 *            return the default cell renderer for this columnClass
	 * @return the renderer for this columnClass
	 * @see #setDefaultRenderer
	 * @see #getColumnClass
	 */
	public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
		if (columnClass == null) {
			return null;
		} else {
			Object renderer = defaultRenderersByColumnClass.get(columnClass);
			if (renderer != null) {
				return (TableCellRenderer) renderer;
			} else {
				Class c = null;
				/**
				 * 
				 * @j2sNative
				 * 
				 * 			c = columnClass.getSuperclass$ &&
				 *            columnClass.getSuperclass$();
				 */
				{
					columnClass.getSuperclass();
				}
				if (c == null && columnClass != Object.class) {
					c = Object.class;
				}
				return getDefaultRenderer(c);
			}
			//
			// return getDefaultRenderer(columnClass.getSuperclass());
			// }
		}
	}

	/**
	 * Sets a default cell editor to be used if no editor has been set in a
	 * <code>TableColumn</code>. If no editing is required in a table, or a
	 * particular column in a table, uses the <code>isCellEditable</code> method
	 * in the <code>TableModel</code> interface to ensure that this
	 * <code>JTable</code> will not start an editor in these columns. If editor
	 * is <code>null</code>, removes the default editor for this column class.
	 *
	 * @param columnClass
	 *            set the default cell editor for this columnClass
	 * @param editor
	 *            default cell editor to be used for this columnClass
	 * @see TableModel#isCellEditable
	 * @see #getDefaultEditor
	 * @see #setDefaultRenderer
	 */
	public void setDefaultEditor(Class<?> columnClass, TableCellEditor editor) {
		if (editor != null) {
			defaultEditorsByColumnClass.put(columnClass, editor);
		} else {
			defaultEditorsByColumnClass.remove(columnClass);
		}
	}

	/**
	 * Returns the editor to be used when no editor has been set in a
	 * <code>TableColumn</code>. During the editing of cells the editor is
	 * fetched from a <code>Hashtable</code> of entries according to the class
	 * of the cells in the column. If there is no entry for this
	 * <code>columnClass</code> the method returns the entry for the most
	 * specific superclass. The <code>JTable</code> installs entries for
	 * <code>Object</code>, <code>Number</code>, and <code>Boolean</code>, all
	 * of which can be modified or replaced.
	 *
	 * @param columnClass
	 *            return the default cell editor for this columnClass
	 * @return the default cell editor to be used for this columnClass
	 * @see #setDefaultEditor
	 * @see #getColumnClass
	 */
	public TableCellEditor getDefaultEditor(Class<?> columnClass) {
		if (columnClass == null) {
			return null;
		} else {
			Object editor = defaultEditorsByColumnClass.get(columnClass);
			if (editor != null) {
				return (TableCellEditor) editor;
			}

			else {
				Class c = null;
				/**
				 * 
				 * @j2sNative
				 * 
				 * 			c = columnClass.getSuperclass$ &&
				 *            columnClass.getSuperclass$();
				 */
				{
					columnClass.getSuperclass();
				}
				if (c == null && columnClass != Object.class) {
					c = Object.class;
				}
				return getDefaultEditor(c);
			}
			// else {
			// return getDefaultEditor(columnClass.getSuperclass());
			// }
		}
	}

	/**
	 * Turns on or off automatic drag handling. In order to enable automatic
	 * drag handling, this property should be set to {@code true}, and the
	 * table's {@code TransferHandler} needs to be {@code non-null}. The default
	 * value of the {@code dragEnabled} property is {@code false}.
	 * <p>
	 * The job of honoring this property, and recognizing a user drag gesture,
	 * lies with the look and feel implementation, and in particular, the
	 * table's {@code TableUI}. When automatic drag handling is enabled, most
	 * look and feels (including those that subclass {@code BasicLookAndFeel})
	 * begin a drag and drop operation whenever the user presses the mouse
	 * button over an item (in single selection mode) or a selection (in other
	 * selection modes) and then moves the mouse a few pixels. Setting this
	 * property to {@code true} can therefore have a subtle effect on how
	 * selections behave.
	 * <p>
	 * If a look and feel is used that ignores this property, you can still
	 * begin a drag and drop operation by calling {@code exportAsDrag} on the
	 * table's {@code TransferHandler}.
	 *
	 * @param b
	 *            whether or not to enable automatic drag handling
	 * @exception HeadlessException
	 *                if <code>b</code> is <code>true</code> and
	 *                <code>GraphicsEnvironment.isHeadless()</code> returns
	 *                <code>true</code>
	 * @see java.awt.GraphicsEnvironment#isHeadless
	 * @see #getDragEnabled
	 * @see #setTransferHandler
	 * @see TransferHandler
	 * @since 1.4
	 *
	 * @beaninfo description: determines whether automatic drag handling is
	 *           enabled bound: false
	 */
	public void setDragEnabled(boolean b) {
		// if (b && GraphicsEnvironment.isHeadless()) {
		// throw new HeadlessException();
		// }
		dragEnabled = b;
	}

	/**
	 * Returns whether or not automatic drag handling is enabled.
	 *
	 * @return the value of the {@code dragEnabled} property
	 * @see #setDragEnabled
	 * @since 1.4
	 */
	public boolean getDragEnabled() {
		return dragEnabled;
	}

	/**
	 * Sets the drop mode for this component. For backward compatibility, the
	 * default for this property is <code>DropMode.USE_SELECTION</code>. Usage
	 * of one of the other modes is recommended, however, for an improved user
	 * experience. <code>DropMode.ON</code>, for instance, offers similar
	 * behavior of showing items as selected, but does so without affecting the
	 * actual selection in the table.
	 * <p>
	 * <code>JTable</code> supports the following drop modes:
	 * <ul>
	 * <li><code>DropMode.USE_SELECTION</code></li>
	 * <li><code>DropMode.ON</code></li>
	 * <li><code>DropMode.INSERT</code></li>
	 * <li><code>DropMode.INSERT_ROWS</code></li>
	 * <li><code>DropMode.INSERT_COLS</code></li>
	 * <li><code>DropMode.ON_OR_INSERT</code></li>
	 * <li><code>DropMode.ON_OR_INSERT_ROWS</code></li>
	 * <li><code>DropMode.ON_OR_INSERT_COLS</code></li>
	 * </ul>
	 * <p>
	 * The drop mode is only meaningful if this component has a
	 * <code>TransferHandler</code> that accepts drops.
	 *
	 * @param dropMode
	 *            the drop mode to use
	 * @throws IllegalArgumentException
	 *             if the drop mode is unsupported or <code>null</code>
	 * @see #getDropMode
	 * @see #getDropLocation
	 * @see #setTransferHandler
	 * @see TransferHandler
	 * @since 1.6
	 */
	public final void setDropMode(DropMode dropMode) {
		if (dropMode != null) {
			switch (dropMode) {
			case USE_SELECTION:
			case ON:
			case INSERT:
			case INSERT_ROWS:
			case INSERT_COLS:
			case ON_OR_INSERT:
			case ON_OR_INSERT_ROWS:
			case ON_OR_INSERT_COLS:
				this.dropMode = dropMode;
				return;
			}
		}

		throw new IllegalArgumentException(dropMode + ": Unsupported drop mode for table");
	}

	/**
	 * Returns the drop mode for this component.
	 *
	 * @return the drop mode for this component
	 * @see #setDropMode
	 * @since 1.6
	 */
	public final DropMode getDropMode() {
		return dropMode;
	}

	// /**
	// * Calculates a drop location in this component, representing where a
	// * drop at the given point should insert data.
	// *
	// * @param p the point to calculate a drop location for
	// * @return the drop location, or <code>null</code>
	// */
	// DropLocation dropLocationForPoint(Point p) {
	// DropLocation location = null;
	//
	// int row = rowAtPoint(p);
	// int col = columnAtPoint(p);
	// boolean outside = Boolean.TRUE == getClientProperty("Table.isFileList")
	// && SwingUtilities2.pointOutsidePrefSize(this, row, col, p);
	//
	// Rectangle rect = getCellRect(row, col, true);
	// Section xSection, ySection;
	// boolean between = false;
	// boolean ltr = getComponentOrientation().isLeftToRight();
	//
	// switch(dropMode) {
	// case USE_SELECTION:
	// case ON:
	// if (row == -1 || col == -1 || outside) {
	// location = new DropLocation(p, -1, -1, false, false);
	// } else {
	// location = new DropLocation(p, row, col, false, false);
	// }
	// break;
	// case INSERT:
	// if (row == -1 && col == -1) {
	// location = new DropLocation(p, 0, 0, true, true);
	// break;
	// }
	//
	// xSection = SwingUtilities2.liesInHorizontal(rect, p, ltr, true);
	//
	// if (row == -1) {
	// if (xSection == LEADING) {
	// location = new DropLocation(p, getRowCount(), col, true, true);
	// } else if (xSection == TRAILING) {
	// location = new DropLocation(p, getRowCount(), col + 1, true, true);
	// } else {
	// location = new DropLocation(p, getRowCount(), col, true, false);
	// }
	// } else if (xSection == LEADING || xSection == TRAILING) {
	// ySection = SwingUtilities2.liesInVertical(rect, p, true);
	// if (ySection == LEADING) {
	// between = true;
	// } else if (ySection == TRAILING) {
	// row++;
	// between = true;
	// }
	//
	// location = new DropLocation(p, row,
	// xSection == TRAILING ? col + 1 : col,
	// between, true);
	// } else {
	// if (SwingUtilities2.liesInVertical(rect, p, false) == TRAILING) {
	// row++;
	// }
	//
	// location = new DropLocation(p, row, col, true, false);
	// }
	//
	// break;
	// case INSERT_ROWS:
	// if (row == -1 && col == -1) {
	// location = new DropLocation(p, -1, -1, false, false);
	// break;
	// }
	//
	// if (row == -1) {
	// location = new DropLocation(p, getRowCount(), col, true, false);
	// break;
	// }
	//
	// if (SwingUtilities2.liesInVertical(rect, p, false) == TRAILING) {
	// row++;
	// }
	//
	// location = new DropLocation(p, row, col, true, false);
	// break;
	// case ON_OR_INSERT_ROWS:
	// if (row == -1 && col == -1) {
	// location = new DropLocation(p, -1, -1, false, false);
	// break;
	// }
	//
	// if (row == -1) {
	// location = new DropLocation(p, getRowCount(), col, true, false);
	// break;
	// }
	//
	// ySection = SwingUtilities2.liesInVertical(rect, p, true);
	// if (ySection == LEADING) {
	// between = true;
	// } else if (ySection == TRAILING) {
	// row++;
	// between = true;
	// }
	//
	// location = new DropLocation(p, row, col, between, false);
	// break;
	// case INSERT_COLS:
	// if (row == -1) {
	// location = new DropLocation(p, -1, -1, false, false);
	// break;
	// }
	//
	// if (col == -1) {
	// location = new DropLocation(p, getColumnCount(), col, false, true);
	// break;
	// }
	//
	// if (SwingUtilities2.liesInHorizontal(rect, p, ltr, false) == TRAILING) {
	// col++;
	// }
	//
	// location = new DropLocation(p, row, col, false, true);
	// break;
	// case ON_OR_INSERT_COLS:
	// if (row == -1) {
	// location = new DropLocation(p, -1, -1, false, false);
	// break;
	// }
	//
	// if (col == -1) {
	// location = new DropLocation(p, row, getColumnCount(), false, true);
	// break;
	// }
	//
	// xSection = SwingUtilities2.liesInHorizontal(rect, p, ltr, true);
	// if (xSection == LEADING) {
	// between = true;
	// } else if (xSection == TRAILING) {
	// col++;
	// between = true;
	// }
	//
	// location = new DropLocation(p, row, col, false, between);
	// break;
	// case ON_OR_INSERT:
	// if (row == -1 && col == -1) {
	// location = new DropLocation(p, 0, 0, true, true);
	// break;
	// }
	//
	// xSection = SwingUtilities2.liesInHorizontal(rect, p, ltr, true);
	//
	// if (row == -1) {
	// if (xSection == LEADING) {
	// location = new DropLocation(p, getRowCount(), col, true, true);
	// } else if (xSection == TRAILING) {
	// location = new DropLocation(p, getRowCount(), col + 1, true, true);
	// } else {
	// location = new DropLocation(p, getRowCount(), col, true, false);
	// }
	//
	// break;
	// }
	//
	// ySection = SwingUtilities2.liesInVertical(rect, p, true);
	// if (ySection == LEADING) {
	// between = true;
	// } else if (ySection == TRAILING) {
	// row++;
	// between = true;
	// }
	//
	// location = new DropLocation(p, row,
	// xSection == TRAILING ? col + 1 : col,
	// between,
	// xSection != MIDDLE);
	//
	// break;
	// default:
	// assert false : "Unexpected drop mode";
	// }
	//
	// return location;
	// }

	// /**
	// * Called to set or clear the drop location during a DnD operation.
	// * In some cases, the component may need to use it's internal selection
	// * temporarily to indicate the drop location. To help facilitate this,
	// * this method returns and accepts as a parameter a state object.
	// * This state object can be used to store, and later restore, the
	// selection
	// * state. Whatever this method returns will be passed back to it in
	// * future calls, as the state parameter. If it wants the DnD system to
	// * continue storing the same state, it must pass it back every time.
	// * Here's how this is used:
	// * <p>
	// * Let's say that on the first call to this method the component decides
	// * to save some state (because it is about to use the selection to show
	// * a drop index). It can return a state object to the caller encapsulating
	// * any saved selection state. On a second call, let's say the drop
	// location
	// * is being changed to something else. The component doesn't need to
	// * restore anything yet, so it simply passes back the same state object
	// * to have the DnD system continue storing it. Finally, let's say this
	// * method is messaged with <code>null</code>. This means DnD
	// * is finished with this component for now, meaning it should restore
	// * state. At this point, it can use the state parameter to restore
	// * said state, and of course return <code>null</code> since there's
	// * no longer anything to store.
	// *
	// * @param location the drop location (as calculated by
	// * <code>dropLocationForPoint</code>) or <code>null</code>
	// * if there's no longer a valid drop location
	// * @param state the state object saved earlier for this component,
	// * or <code>null</code>
	// * @param forDrop whether or not the method is being called because an
	// * actual drop occurred
	// * @return any saved state for this component, or <code>null</code> if
	// none
	// */
	// Object setDropLocation(TransferHandler.DropLocation location,
	// Object state,
	// boolean forDrop) {
	//
	// Object retVal = null;
	// DropLocation tableLocation = (DropLocation)location;
	//
	// if (dropMode == DropMode.USE_SELECTION) {
	// if (tableLocation == null) {
	// if (!forDrop && state != null) {
	// clearSelection();
	//
	// int[] rows = (int[])((int[][])state)[0];
	// int[] cols = (int[])((int[][])state)[1];
	// int[] anchleads = (int[])((int[][])state)[2];
	//
	// for (int i = 0; i < rows.length; i++) {
	// addRowSelectionInterval(rows[i], rows[i]);
	// }
	//
	// for (int i = 0; i < cols.length; i++) {
	// addColumnSelectionInterval(cols[i], cols[i]);
	// }
	//
	// SwingUtilities2.setLeadAnchorWithoutSelection(
	// getSelectionModel(), anchleads[1], anchleads[0]);
	//
	// SwingUtilities2.setLeadAnchorWithoutSelection(
	// getColumnModel().getSelectionModel(),
	// anchleads[3], anchleads[2]);
	// }
	// } else {
	// if (dropLocation == null) {
	// retVal = new int[][]{
	// getSelectedRows(),
	// getSelectedColumns(),
	// {getAdjustedIndex(getSelectionModel()
	// .getAnchorSelectionIndex(), true),
	// getAdjustedIndex(getSelectionModel()
	// .getLeadSelectionIndex(), true),
	// getAdjustedIndex(getColumnModel().getSelectionModel()
	// .getAnchorSelectionIndex(), false),
	// getAdjustedIndex(getColumnModel().getSelectionModel()
	// .getLeadSelectionIndex(), false)}};
	// } else {
	// retVal = state;
	// }
	//
	// if (tableLocation.getRow() == -1) {
	// clearSelectionAndLeadAnchor();
	// } else {
	// setRowSelectionInterval(tableLocation.getRow(),
	// tableLocation.getRow());
	// setColumnSelectionInterval(tableLocation.getColumn(),
	// tableLocation.getColumn());
	// }
	// }
	// }
	//
	// DropLocation old = dropLocation;
	// dropLocation = tableLocation;
	// firePropertyChange("dropLocation", old, dropLocation);
	//
	// return retVal;
	// }

	// /**
	// * Returns the location that this component should visually indicate
	// * as the drop location during a DnD operation over the component,
	// * or {@code null} if no location is to currently be shown.
	// * <p>
	// * This method is not meant for querying the drop location
	// * from a {@code TransferHandler}, as the drop location is only
	// * set after the {@code TransferHandler}'s <code>canImport</code>
	// * has returned and has allowed for the location to be shown.
	// * <p>
	// * When this property changes, a property change event with
	// * name "dropLocation" is fired by the component.
	// *
	// * @return the drop location
	// * @see #setDropMode
	// * @see TransferHandler#canImport(TransferHandler.TransferSupport)
	// * @since 1.6
	// */
	// public final DropLocation getDropLocation() {
	// return dropLocation;
	// }

	/**
	 * Specifies whether a {@code RowSorter} should be created for the table
	 * whenever its model changes.
	 * <p>
	 * When {@code setAutoCreateRowSorter(true)} is invoked, a {@code
	 * TableRowSorter} is immediately created and installed on the table. While
	 * the {@code autoCreateRowSorter} property remains {@code true}, every time
	 * the model is changed, a new {@code
	 * TableRowSorter} is created and set as the table's row sorter.
	 *
	 * @param autoCreateRowSorter
	 *            whether or not a {@code RowSorter} should be automatically
	 *            created
	 * @see javax.swing.table.TableRowSorter
	 * @beaninfo bound: true preferred: true description: Whether or not to turn
	 *           on sorting by default.
	 * @since 1.6
	 */
	public void setAutoCreateRowSorter(boolean autoCreateRowSorter) {
		boolean oldValue = this.autoCreateRowSorter;
		this.autoCreateRowSorter = autoCreateRowSorter;
		if (autoCreateRowSorter) {
			setRowSorter(new TableRowSorter(getModel()));
		}
		firePropertyChange("autoCreateRowSorter", oldValue, autoCreateRowSorter);
	}

	/**
	 * Returns {@code true} if whenever the model changes, a new
	 * {@code RowSorter} should be created and installed as the table's sorter;
	 * otherwise, returns {@code false}.
	 *
	 * @return true if a {@code RowSorter} should be created when the model
	 *         changes
	 * @since 1.6
	 */
	public boolean getAutoCreateRowSorter() {
		return autoCreateRowSorter;
	}

	/**
	 * Specifies whether the selection should be updated after sorting. If true,
	 * on sorting the selection is reset such that the same rows, in terms of
	 * the model, remain selected. The default is true.
	 *
	 * @param update
	 *            whether or not to update the selection on sorting
	 * @beaninfo bound: true expert: true description: Whether or not to update
	 *           the selection on sorting
	 * @since 1.6
	 */
	public void setUpdateSelectionOnSort(boolean update) {
		if (updateSelectionOnSort != update) {
			updateSelectionOnSort = update;
			firePropertyChange("updateSelectionOnSort", !update, update);
		}
	}

	/**
	 * Returns true if the selection should be updated after sorting.
	 *
	 * @return whether to update the selection on a sort
	 * @since 1.6
	 */
	public boolean getUpdateSelectionOnSort() {
		return updateSelectionOnSort;
	}

	/**
	 * Sets the <code>RowSorter</code>. <code>RowSorter</code> is used to
	 * provide sorting and filtering to a <code>JTable</code>.
	 * <p>
	 * This method clears the selection and resets any variable row heights.
	 * <p>
	 * If the underlying model of the <code>RowSorter</code> differs from that
	 * of this <code>JTable</code> undefined behavior will result.
	 *
	 * @param sorter
	 *            the <code>RowSorter</code>; <code>null</code> turns sorting
	 *            off
	 * @see javax.swing.table.TableRowSorter
	 * @since 1.6
	 */
	public void setRowSorter(RowSorter<? extends TableModel> sorter) {
		RowSorter<? extends TableModel> oldRowSorter = null;
		if (sortManager != null) {
			oldRowSorter = sortManager.sorter;
			sortManager.dispose();
			sortManager = null;
		}
		rowModel = null;
		clearSelectionAndLeadAnchor();
		if (sorter != null) {
			sortManager = new SortManager(sorter);
		}
		resizeAndRepaint();
		firePropertyChange("rowSorter", oldRowSorter, sorter);
		firePropertyChange("sorter", oldRowSorter, sorter);
	}

	/**
	 * Returns the object responsible for sorting.
	 *
	 * @return the object responsible for sorting
	 * @since 1.6
	 */
	public RowSorter<? extends TableModel> getRowSorter() {
		return (sortManager != null) ? sortManager.sorter : null;
	}

	//
	// Selection methods
	//
	/**
	 * Sets the table's selection mode to allow only single selections, a single
	 * contiguous interval, or multiple intervals.
	 * <P>
	 * <bold>Note:</bold> <code>JTable</code> provides all the methods for
	 * handling column and row selection. When setting states, such as
	 * <code>setSelectionMode</code>, it not only updates the mode for the row
	 * selection model but also sets similar values in the selection model of
	 * the <code>columnModel</code>. If you want to have the row and column
	 * selection models operating in different modes, set them both directly.
	 * <p>
	 * Both the row and column selection models for <code>JTable</code> default
	 * to using a <code>DefaultListSelectionModel</code> so that
	 * <code>JTable</code> works the same way as the <code>JList</code>. See the
	 * <code>setSelectionMode</code> method in <code>JList</code> for details
	 * about the modes.
	 *
	 * @see JList#setSelectionMode
	 * @beaninfo description: The selection mode used by the row and column
	 *           selection models. enum: SINGLE_SELECTION
	 *           ListSelectionModel.SINGLE_SELECTION SINGLE_INTERVAL_SELECTION
	 *           ListSelectionModel.SINGLE_INTERVAL_SELECTION
	 *           MULTIPLE_INTERVAL_SELECTION
	 *           ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
	 */
	public void setSelectionMode(int selectionMode) {
		clearSelection();
		getSelectionModel().setSelectionMode(selectionMode);
		getColumnModel().getSelectionModel().setSelectionMode(selectionMode);
	}

	/**
	 * Sets whether the rows in this model can be selected.
	 *
	 * @param rowSelectionAllowed
	 *            true if this model will allow row selection
	 * @see #getRowSelectionAllowed
	 * @beaninfo bound: true attribute: visualUpdate true description: If true,
	 *           an entire row is selected for each selected cell.
	 */
	public void setRowSelectionAllowed(boolean rowSelectionAllowed) {
		boolean old = this.rowSelectionAllowed;
		this.rowSelectionAllowed = rowSelectionAllowed;
		if (old != rowSelectionAllowed) {
			repaint();
		}
		firePropertyChange("rowSelectionAllowed", old, rowSelectionAllowed);
	}

	/**
	 * Returns true if rows can be selected.
	 *
	 * @return true if rows can be selected, otherwise false
	 * @see #setRowSelectionAllowed
	 */
	public boolean getRowSelectionAllowed() {
		return rowSelectionAllowed;
	}

	/**
	 * Sets whether the columns in this model can be selected.
	 *
	 * @param columnSelectionAllowed
	 *            true if this model will allow column selection
	 * @see #getColumnSelectionAllowed
	 * @beaninfo bound: true attribute: visualUpdate true description: If true,
	 *           an entire column is selected for each selected cell.
	 */
	public void setColumnSelectionAllowed(boolean columnSelectionAllowed) {
		boolean old = columnModel.getColumnSelectionAllowed();
		columnModel.setColumnSelectionAllowed(columnSelectionAllowed);
		if (old != columnSelectionAllowed) {
			repaint();
		}
		firePropertyChange("columnSelectionAllowed", old, columnSelectionAllowed);
	}

	/**
	 * Returns true if columns can be selected.
	 *
	 * @return true if columns can be selected, otherwise false
	 * @see #setColumnSelectionAllowed
	 */
	public boolean getColumnSelectionAllowed() {
		return columnModel.getColumnSelectionAllowed();
	}

	/**
	 * Sets whether this table allows both a column selection and a row
	 * selection to exist simultaneously. When set, the table treats the
	 * intersection of the row and column selection models as the selected
	 * cells. Override <code>isCellSelected</code> to change this default
	 * behavior. This method is equivalent to setting both the
	 * <code>rowSelectionAllowed</code> property and
	 * <code>columnSelectionAllowed</code> property of the
	 * <code>columnModel</code> to the supplied value.
	 *
	 * @param cellSelectionEnabled
	 *            true if simultaneous row and column selection is allowed
	 * @see #getCellSelectionEnabled
	 * @see #isCellSelected
	 * @beaninfo bound: true attribute: visualUpdate true description: Select a
	 *           rectangular region of cells rather than rows or columns.
	 */
	public void setCellSelectionEnabled(boolean cellSelectionEnabled) {
		setRowSelectionAllowed(cellSelectionEnabled);
		setColumnSelectionAllowed(cellSelectionEnabled);
		boolean old = this.cellSelectionEnabled;
		this.cellSelectionEnabled = cellSelectionEnabled;
		firePropertyChange("cellSelectionEnabled", old, cellSelectionEnabled);
	}

	/**
	 * Returns true if both row and column selection models are enabled.
	 * Equivalent to <code>getRowSelectionAllowed() &&
	 * getColumnSelectionAllowed()</code>.
	 *
	 * @return true if both row and column selection models are enabled
	 *
	 * @see #setCellSelectionEnabled
	 */
	public boolean getCellSelectionEnabled() {
		return getRowSelectionAllowed() && getColumnSelectionAllowed();
	}

	/**
	 * Selects all rows, columns, and cells in the table.
	 */
	public void selectAll() {
		// If I'm currently editing, then I should stop editing
		if (isEditing()) {
			removeEditor();
		}
		if (getRowCount() > 0 && getColumnCount() > 0) {
			int oldLead;
			int oldAnchor;
			ListSelectionModel selModel;

			selModel = selectionModel;
			selModel.setValueIsAdjusting(true);
			oldLead = getAdjustedIndex(selModel.getLeadSelectionIndex(), true);
			oldAnchor = getAdjustedIndex(selModel.getAnchorSelectionIndex(), true);

			setRowSelectionInterval(0, getRowCount() - 1);

			// this is done to restore the anchor and lead
			SwingUtilities2.setLeadAnchorWithoutSelection(selModel, oldLead, oldAnchor);

			selModel.setValueIsAdjusting(false);

			selModel = columnModel.getSelectionModel();
			selModel.setValueIsAdjusting(true);
			oldLead = getAdjustedIndex(selModel.getLeadSelectionIndex(), false);
			oldAnchor = getAdjustedIndex(selModel.getAnchorSelectionIndex(), false);

			setColumnSelectionInterval(0, getColumnCount() - 1);

			// this is done to restore the anchor and lead
			SwingUtilities2.setLeadAnchorWithoutSelection(selModel, oldLead, oldAnchor);

			selModel.setValueIsAdjusting(false);
		}
	}

	/**
	 * Deselects all selected columns and rows.
	 */
	public void clearSelection() {
		selectionModel.clearSelection();
		columnModel.getSelectionModel().clearSelection();
	}

	private void clearSelectionAndLeadAnchor() {
		selectionModel.setValueIsAdjusting(true);
		columnModel.getSelectionModel().setValueIsAdjusting(true);

		clearSelection();

		selectionModel.setAnchorSelectionIndex(-1);
		selectionModel.setLeadSelectionIndex(-1);
		columnModel.getSelectionModel().setAnchorSelectionIndex(-1);
		columnModel.getSelectionModel().setLeadSelectionIndex(-1);

		selectionModel.setValueIsAdjusting(false);
		columnModel.getSelectionModel().setValueIsAdjusting(false);
	}

	private int getAdjustedIndex(int index, boolean row) {
		int compare = row ? getRowCount() : getColumnCount();
		return index < compare ? index : -1;
	}

	private int boundRow(int row) throws IllegalArgumentException {
		if (row < 0 || row >= getRowCount()) {
			throw new IllegalArgumentException("Row index out of range");
		}
		return row;
	}

	private int boundColumn(int col) {
		if (col < 0 || col >= getColumnCount()) {
			throw new IllegalArgumentException("Column index out of range");
		}
		return col;
	}

	/**
	 * Selects the rows from <code>index0</code> to <code>index1</code>,
	 * inclusive.
	 *
	 * @exception IllegalArgumentException
	 *                if <code>index0</code> or <code>index1</code> lie outside
	 *                [0, <code>getRowCount()</code>-1]
	 * @param index0
	 *            one end of the interval
	 * @param index1
	 *            the other end of the interval
	 */
	public void setRowSelectionInterval(int index0, int index1) {
		selectionModel.setSelectionInterval(boundRow(index0), boundRow(index1));
	}

	/**
	 * Selects the columns from <code>index0</code> to <code>index1</code>,
	 * inclusive.
	 *
	 * @exception IllegalArgumentException
	 *                if <code>index0</code> or <code>index1</code> lie outside
	 *                [0, <code>getColumnCount()</code>-1]
	 * @param index0
	 *            one end of the interval
	 * @param index1
	 *            the other end of the interval
	 */
	public void setColumnSelectionInterval(int index0, int index1) {
		columnModel.getSelectionModel().setSelectionInterval(boundColumn(index0), boundColumn(index1));
	}

	/**
	 * Adds the rows from <code>index0</code> to <code>index1</code>, inclusive,
	 * to the current selection.
	 *
	 * @exception IllegalArgumentException
	 *                if <code>index0</code> or <code>index1</code> lie outside
	 *                [0, <code>getRowCount()</code>-1]
	 * @param index0
	 *            one end of the interval
	 * @param index1
	 *            the other end of the interval
	 */
	public void addRowSelectionInterval(int index0, int index1) {
		selectionModel.addSelectionInterval(boundRow(index0), boundRow(index1));
	}

	/**
	 * Adds the columns from <code>index0</code> to <code>index1</code>,
	 * inclusive, to the current selection.
	 *
	 * @exception IllegalArgumentException
	 *                if <code>index0</code> or <code>index1</code> lie outside
	 *                [0, <code>getColumnCount()</code>-1]
	 * @param index0
	 *            one end of the interval
	 * @param index1
	 *            the other end of the interval
	 */
	public void addColumnSelectionInterval(int index0, int index1) {
		columnModel.getSelectionModel().addSelectionInterval(boundColumn(index0), boundColumn(index1));
	}

	/**
	 * Deselects the rows from <code>index0</code> to <code>index1</code>,
	 * inclusive.
	 *
	 * @exception IllegalArgumentException
	 *                if <code>index0</code> or <code>index1</code> lie outside
	 *                [0, <code>getRowCount()</code>-1]
	 * @param index0
	 *            one end of the interval
	 * @param index1
	 *            the other end of the interval
	 */
	public void removeRowSelectionInterval(int index0, int index1) {
		selectionModel.removeSelectionInterval(boundRow(index0), boundRow(index1));
	}

	/**
	 * Deselects the columns from <code>index0</code> to <code>index1</code>,
	 * inclusive.
	 *
	 * @exception IllegalArgumentException
	 *                if <code>index0</code> or <code>index1</code> lie outside
	 *                [0, <code>getColumnCount()</code>-1]
	 * @param index0
	 *            one end of the interval
	 * @param index1
	 *            the other end of the interval
	 */
	public void removeColumnSelectionInterval(int index0, int index1) {
		columnModel.getSelectionModel().removeSelectionInterval(boundColumn(index0), boundColumn(index1));
	}

	/**
	 * Returns the index of the first selected row, -1 if no row is selected.
	 * 
	 * @return the index of the first selected row
	 */
	public int getSelectedRow() {
		return selectionModel.getMinSelectionIndex();
	}

	/**
	 * Returns the index of the first selected column, -1 if no column is
	 * selected.
	 * 
	 * @return the index of the first selected column
	 */
	public int getSelectedColumn() {
		return columnModel.getSelectionModel().getMinSelectionIndex();
	}

	/**
	 * Returns the indices of all selected rows.
	 *
	 * @return an array of integers containing the indices of all selected rows,
	 *         or an empty array if no row is selected
	 * @see #getSelectedRow
	 */
	public int[] getSelectedRows() {
		int iMin = selectionModel.getMinSelectionIndex();
		int iMax = selectionModel.getMaxSelectionIndex();

		if ((iMin == -1) || (iMax == -1)) {
			return new int[0];
		}

		int[] rvTmp = new int[1 + (iMax - iMin)];
		int n = 0;
		for (int i = iMin; i <= iMax; i++) {
			if (selectionModel.isSelectedIndex(i)) {
				rvTmp[n++] = i;
			}
		}
		int[] rv = new int[n];
		System.arraycopy(rvTmp, 0, rv, 0, n);
		return rv;
	}

	/**
	 * Returns the indices of all selected columns.
	 *
	 * @return an array of integers containing the indices of all selected
	 *         columns, or an empty array if no column is selected
	 * @see #getSelectedColumn
	 */
	public int[] getSelectedColumns() {
		return columnModel.getSelectedColumns();
	}

	/**
	 * Returns the number of selected rows.
	 *
	 * @return the number of selected rows, 0 if no rows are selected
	 */
	public int getSelectedRowCount() {
		int iMin = selectionModel.getMinSelectionIndex();
		int iMax = selectionModel.getMaxSelectionIndex();
		int count = 0;

		for (int i = iMin; i <= iMax; i++) {
			if (selectionModel.isSelectedIndex(i)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Returns the number of selected columns.
	 *
	 * @return the number of selected columns, 0 if no columns are selected
	 */
	public int getSelectedColumnCount() {
		return columnModel.getSelectedColumnCount();
	}

	/**
	 * Returns true if the specified index is in the valid range of rows, and
	 * the row at that index is selected.
	 *
	 * @return true if <code>row</code> is a valid index and the row at that
	 *         index is selected (where 0 is the first row)
	 */
	public boolean isRowSelected(int row) {
		return selectionModel.isSelectedIndex(row);
	}

	/**
	 * Returns true if the specified index is in the valid range of columns, and
	 * the column at that index is selected.
	 *
	 * @param column
	 *            the column in the column model
	 * @return true if <code>column</code> is a valid index and the column at
	 *         that index is selected (where 0 is the first column)
	 */
	public boolean isColumnSelected(int column) {
		return columnModel.getSelectionModel().isSelectedIndex(column);
	}

	/**
	 * Returns true if the specified indices are in the valid range of rows and
	 * columns and the cell at the specified position is selected.
	 * 
	 * @param row
	 *            the row being queried
	 * @param column
	 *            the column being queried
	 *
	 * @return true if <code>row</code> and <code>column</code> are valid
	 *         indices and the cell at index <code>(row, column)</code> is
	 *         selected, where the first row and first column are at index 0
	 */
	public boolean isCellSelected(int row, int column) {
		if (!getRowSelectionAllowed() && !getColumnSelectionAllowed()) {
			return false;
		}
		return (!getRowSelectionAllowed() || isRowSelected(row))
				&& (!getColumnSelectionAllowed() || isColumnSelected(column));
	}

	private void changeSelectionModel(ListSelectionModel sm, int index, boolean toggle, boolean extend,
			boolean selected, int anchor, boolean anchorSelected) {
		if (extend) {
			if (toggle) {
				if (anchorSelected) {
					sm.addSelectionInterval(anchor, index);
				} else {
					sm.removeSelectionInterval(anchor, index);
					// this is a Windows-only behavior that we want for file
					// lists
					if (Boolean.TRUE == getClientProperty("Table.isFileList")) {
						sm.addSelectionInterval(index, index);
						sm.setAnchorSelectionIndex(anchor);
					}
				}
			} else {
				sm.setSelectionInterval(anchor, index);
			}
		} else {
			if (toggle) {
				if (selected) {
					sm.removeSelectionInterval(index, index);
				} else {
					sm.addSelectionInterval(index, index);
				}
			} else {
				sm.setSelectionInterval(index, index);
			}
		}
	}

	/**
	 * Updates the selection models of the table, depending on the state of the
	 * two flags: <code>toggle</code> and <code>extend</code>. Most changes to
	 * the selection that are the result of keyboard or mouse events received by
	 * the UI are channeled through this method so that the behavior may be
	 * overridden by a subclass. Some UIs may need more functionality than this
	 * method provides, such as when manipulating the lead for discontiguous
	 * selection, and may not call into this method for some selection changes.
	 * <p>
	 * This implementation uses the following conventions:
	 * <ul>
	 * <li><code>toggle</code>: <em>false</em>, <code>extend</code>:
	 * <em>false</em>. Clear the previous selection and ensure the new cell is
	 * selected.
	 * <li><code>toggle</code>: <em>false</em>, <code>extend</code>:
	 * <em>true</em>. Extend the previous selection from the anchor to the
	 * specified cell, clearing all other selections.
	 * <li><code>toggle</code>: <em>true</em>, <code>extend</code>:
	 * <em>false</em>. If the specified cell is selected, deselect it. If it is
	 * not selected, select it.
	 * <li><code>toggle</code>: <em>true</em>, <code>extend</code>:
	 * <em>true</em>. Apply the selection state of the anchor to all cells
	 * between it and the specified cell.
	 * </ul>
	 * 
	 * @param rowIndex
	 *            affects the selection at <code>row</code>
	 * @param columnIndex
	 *            affects the selection at <code>column</code>
	 * @param toggle
	 *            see description above
	 * @param extend
	 *            if true, extend the current selection
	 *
	 * @since 1.3
	 */
	public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
		ListSelectionModel rsm = getSelectionModel();
		ListSelectionModel csm = getColumnModel().getSelectionModel();

		int anchorRow = getAdjustedIndex(rsm.getAnchorSelectionIndex(), true);
		int anchorCol = getAdjustedIndex(csm.getAnchorSelectionIndex(), false);

		boolean anchorSelected = true;

		if (anchorRow == -1) {
			if (getRowCount() > 0) {
				anchorRow = 0;
			}
			anchorSelected = false;
		}

		if (anchorCol == -1) {
			if (getColumnCount() > 0) {
				anchorCol = 0;
			}
			anchorSelected = false;
		}

		// Check the selection here rather than in each selection model.
		// This is significant in cell selection mode if we are supposed
		// to be toggling the selection. In this case it is better to
		// ensure that the cell's selection state will indeed be changed.
		// If this were done in the code for the selection model it
		// might leave a cell in selection state if the row was
		// selected but the column was not - as it would toggle them both.
		boolean selected = isCellSelected(rowIndex, columnIndex);
		anchorSelected = anchorSelected && isCellSelected(anchorRow, anchorCol);

		changeSelectionModel(csm, columnIndex, toggle, extend, selected, anchorCol, anchorSelected);
		changeSelectionModel(rsm, rowIndex, toggle, extend, selected, anchorRow, anchorSelected);

		// Scroll after changing the selection as blit scrolling is immediate,
		// so that if we cause the repaint after the scroll we end up painting
		// everything!
		if (getAutoscrolls()) {
			Rectangle cellRect = getCellRect(rowIndex, columnIndex, false);
			if (cellRect != null) {
				scrollRectToVisible(cellRect);
			}
		}
	}

	/**
	 * Returns the foreground color for selected cells.
	 *
	 * @return the <code>Color</code> object for the foreground property
	 * @see #setSelectionForeground
	 * @see #setSelectionBackground
	 */
	public Color getSelectionForeground() {
		return selectionForeground;
	}

	/**
	 * Sets the foreground color for selected cells. Cell renderers can use this
	 * color to render text and graphics for selected cells.
	 * <p>
	 * The default value of this property is defined by the look and feel
	 * implementation.
	 * <p>
	 * This is a <a href=
	 * "http://java.sun.com/docs/books/tutorial/javabeans/whatis/beanDefinition.html">JavaBeans</a>
	 * bound property.
	 *
	 * @param selectionForeground
	 *            the <code>Color</code> to use in the foreground for selected
	 *            list items
	 * @see #getSelectionForeground
	 * @see #setSelectionBackground
	 * @see #setForeground
	 * @see #setBackground
	 * @see #setFont
	 * @beaninfo bound: true description: A default foreground color for
	 *           selected cells.
	 */
	public void setSelectionForeground(Color selectionForeground) {
		Color old = this.selectionForeground;
		this.selectionForeground = selectionForeground;
		firePropertyChange("selectionForeground", old, selectionForeground);
		if (!selectionForeground.equals(old)) {
			repaint();
		}
	}

	/**
	 * Returns the background color for selected cells.
	 *
	 * @return the <code>Color</code> used for the background of selected list
	 *         items
	 * @see #setSelectionBackground
	 * @see #setSelectionForeground
	 */
	public Color getSelectionBackground() {
		return selectionBackground;
	}

	/**
	 * Sets the background color for selected cells. Cell renderers can use this
	 * color to the fill selected cells.
	 * <p>
	 * The default value of this property is defined by the look and feel
	 * implementation.
	 * <p>
	 * This is a <a href=
	 * "http://java.sun.com/docs/books/tutorial/javabeans/whatis/beanDefinition.html">JavaBeans</a>
	 * bound property.
	 *
	 * @param selectionBackground
	 *            the <code>Color</code> to use for the background of selected
	 *            cells
	 * @see #getSelectionBackground
	 * @see #setSelectionForeground
	 * @see #setForeground
	 * @see #setBackground
	 * @see #setFont
	 * @beaninfo bound: true description: A default background color for
	 *           selected cells.
	 */
	public void setSelectionBackground(Color selectionBackground) {
		Color old = this.selectionBackground;
		this.selectionBackground = selectionBackground;
		firePropertyChange("selectionBackground", old, selectionBackground);
		if (!selectionBackground.equals(old)) {
			repaint();
		}
	}

	/**
	 * Returns the <code>TableColumn</code> object for the column in the table
	 * whose identifier is equal to <code>identifier</code>, when compared using
	 * <code>equals</code>.
	 *
	 * @return the <code>TableColumn</code> object that matches the identifier
	 * @exception IllegalArgumentException
	 *                if <code>identifier</code> is <code>null</code> or no
	 *                <code>TableColumn</code> has this identifier
	 *
	 * @param identifier
	 *            the identifier object
	 */
	public TableColumn getColumn(Object identifier) {
		TableColumnModel cm = getColumnModel();
		int columnIndex = cm.getColumnIndex(identifier);
		return cm.getColumn(columnIndex);
	}

	//
	// Informally implement the TableModel interface.
	//

	/**
	 * Maps the index of the column in the view at <code>viewColumnIndex</code>
	 * to the index of the column in the table model. Returns the index of the
	 * corresponding column in the model. If <code>viewColumnIndex</code> is
	 * less than zero, returns <code>viewColumnIndex</code>.
	 *
	 * @param viewColumnIndex
	 *            the index of the column in the view
	 * @return the index of the corresponding column in the model
	 *
	 * @see #convertColumnIndexToView
	 */
	public int convertColumnIndexToModel(int viewColumnIndex) {
		if (viewColumnIndex < 0) {
			return viewColumnIndex;
		}
		return getColumnModel().getColumn(viewColumnIndex).getModelIndex();
	}

	/**
	 * Maps the index of the column in the table model at
	 * <code>modelColumnIndex</code> to the index of the column in the view.
	 * Returns the index of the corresponding column in the view; returns -1 if
	 * this column is not being displayed. If <code>modelColumnIndex</code> is
	 * less than zero, returns <code>modelColumnIndex</code>.
	 *
	 * @param modelColumnIndex
	 *            the index of the column in the model
	 * @return the index of the corresponding column in the view
	 *
	 * @see #convertColumnIndexToModel
	 */
	public int convertColumnIndexToView(int modelColumnIndex) {
		if (modelColumnIndex < 0) {
			return modelColumnIndex;
		}
		TableColumnModel cm = getColumnModel();
		for (int column = 0; column < getColumnCount(); column++) {
			if (cm.getColumn(column).getModelIndex() == modelColumnIndex) {
				return column;
			}
		}
		return -1;
	}

	/**
	 * Maps the index of the row in terms of the <code>TableModel</code> to the
	 * view. If the contents of the model are not sorted the model and view
	 * indices are the same.
	 *
	 * @param modelRowIndex
	 *            the index of the row in terms of the model
	 * @return the index of the corresponding row in the view, or -1 if the row
	 *         isn't visible
	 * @throws IndexOutOfBoundsException
	 *             if sorting is enabled and passed an index outside the number
	 *             of rows of the <code>TableModel</code>
	 * @see javax.swing.table.TableRowSorter
	 * @since 1.6
	 */
	public int convertRowIndexToView(int modelRowIndex) {
		RowSorter sorter = getRowSorter();
		if (sorter != null) {
			return sorter.convertRowIndexToView(modelRowIndex);
		}
		return modelRowIndex;
	}

	/**
	 * Maps the index of the row in terms of the view to the underlying
	 * <code>TableModel</code>. If the contents of the model are not sorted the
	 * model and view indices are the same.
	 *
	 * @param viewRowIndex
	 *            the index of the row in the view
	 * @return the index of the corresponding row in the model
	 * @throws IndexOutOfBoundsException
	 *             if sorting is enabled and passed an index outside the range
	 *             of the <code>JTable</code> as determined by the method
	 *             <code>getRowCount</code>
	 * @see javax.swing.table.TableRowSorter
	 * @see #getRowCount
	 * @since 1.6
	 */
	public int convertRowIndexToModel(int viewRowIndex) {
		RowSorter sorter = getRowSorter();
		if (sorter != null) {
			return sorter.convertRowIndexToModel(viewRowIndex);
		}
		return viewRowIndex;
	}

	/**
	 * Returns the number of rows that can be shown in the <code>JTable</code>,
	 * given unlimited space. If a <code>RowSorter</code> with a filter has been
	 * specified, the number of rows returned may differ from that of the
	 * underlying <code>TableModel</code>.
	 *
	 * @return the number of rows shown in the <code>JTable</code>
	 * @see #getColumnCount
	 */
	public int getRowCount() {
		RowSorter sorter = getRowSorter();
		if (sorter != null) {
			return sorter.getViewRowCount();
		}
		return getModel().getRowCount();
	}

	/**
	 * Returns the number of columns in the column model. Note that this may be
	 * different from the number of columns in the table model.
	 *
	 * @return the number of columns in the table
	 * @see #getRowCount
	 * @see #removeColumn
	 */
	public int getColumnCount() {
		return getColumnModel().getColumnCount();
	}

	/**
	 * Returns the name of the column appearing in the view at column position
	 * <code>column</code>.
	 *
	 * @param column
	 *            the column in the view being queried
	 * @return the name of the column at position <code>column</code> in the
	 *         view where the first column is column 0
	 */
	public String getColumnName(int column) {
		return getModel().getColumnName(convertColumnIndexToModel(column));
	}

	/**
	 * Returns the type of the column appearing in the view at column position
	 * <code>column</code>.
	 *
	 * @param column
	 *            the column in the view being queried
	 * @return the type of the column at position <code>column</code> in the
	 *         view where the first column is column 0
	 */
	public Class<?> getColumnClass(int column) {
		return getModel().getColumnClass(convertColumnIndexToModel(column));
	}

	/**
	 * Returns the cell value at <code>row</code> and <code>column</code>.
	 * <p>
	 * <b>Note</b>: The column is specified in the table view's display order,
	 * and not in the <code>TableModel</code>'s column order. This is an
	 * important distinction because as the user rearranges the columns in the
	 * table, the column at a given index in the view will change. Meanwhile the
	 * user's actions never affect the model's column ordering.
	 *
	 * @param row
	 *            the row whose value is to be queried
	 * @param column
	 *            the column whose value is to be queried
	 * @return the Object at the specified cell
	 */
	public Object getValueAt(int row, int column) {
		return getModel().getValueAt(convertRowIndexToModel(row), convertColumnIndexToModel(column));
	}

	/**
	 * Sets the value for the cell in the table model at <code>row</code> and
	 * <code>column</code>.
	 * <p>
	 * <b>Note</b>: The column is specified in the table view's display order,
	 * and not in the <code>TableModel</code>'s column order. This is an
	 * important distinction because as the user rearranges the columns in the
	 * table, the column at a given index in the view will change. Meanwhile the
	 * user's actions never affect the model's column ordering.
	 *
	 * <code>aValue</code> is the new value.
	 *
	 * @param aValue
	 *            the new value
	 * @param row
	 *            the row of the cell to be changed
	 * @param column
	 *            the column of the cell to be changed
	 * @see #getValueAt
	 */
	public void setValueAt(Object aValue, int row, int column) {
		getModel().setValueAt(aValue, convertRowIndexToModel(row), convertColumnIndexToModel(column));
	}

	/**
	 * Returns true if the cell at <code>row</code> and <code>column</code> is
	 * editable. Otherwise, invoking <code>setValueAt</code> on the cell will
	 * have no effect.
	 * <p>
	 * <b>Note</b>: The column is specified in the table view's display order,
	 * and not in the <code>TableModel</code>'s column order. This is an
	 * important distinction because as the user rearranges the columns in the
	 * table, the column at a given index in the view will change. Meanwhile the
	 * user's actions never affect the model's column ordering.
	 *
	 *
	 * @param row
	 *            the row whose value is to be queried
	 * @param column
	 *            the column whose value is to be queried
	 * @return true if the cell is editable
	 * @see #setValueAt
	 */
	public boolean isCellEditable(int row, int column) {
		return getModel().isCellEditable(convertRowIndexToModel(row), convertColumnIndexToModel(column));
	}
	//
	// Adding and removing columns in the view
	//

	/**
	 * Appends <code>aColumn</code> to the end of the array of columns held by
	 * this <code>JTable</code>'s column model. If the column name of
	 * <code>aColumn</code> is <code>null</code>, sets the column name of
	 * <code>aColumn</code> to the name returned by
	 * <code>getModel().getColumnName()</code>.
	 * <p>
	 * To add a column to this <code>JTable</code> to display the
	 * <code>modelColumn</code>'th column of data in the model with a given
	 * <code>width</code>, <code>cellRenderer</code>, and
	 * <code>cellEditor</code> you can use:
	 * 
	 * <pre>
	 *
	 * addColumn(new TableColumn(modelColumn, width, cellRenderer, cellEditor));
	 *
	 * </pre>
	 * 
	 * [Any of the <code>TableColumn</code> constructors can be used instead of
	 * this one.] The model column number is stored inside the
	 * <code>TableColumn</code> and is used during rendering and editing to
	 * locate the appropriates data values in the model. The model column number
	 * does not change when columns are reordered in the view.
	 *
	 * @param aColumn
	 *            the <code>TableColumn</code> to be added
	 * @see #removeColumn
	 */
	public void addColumn(TableColumn aColumn) {
		if (aColumn.getHeaderValue() == null) {
			int modelColumn = aColumn.getModelIndex();
			String columnName = getModel().getColumnName(modelColumn);
			aColumn.setHeaderValue(columnName);
		}
		getColumnModel().addColumn(aColumn);
	}

	/**
	 * Removes <code>aColumn</code> from this <code>JTable</code>'s array of
	 * columns. Note: this method does not remove the column of data from the
	 * model; it just removes the <code>TableColumn</code> that was responsible
	 * for displaying it.
	 *
	 * @param aColumn
	 *            the <code>TableColumn</code> to be removed
	 * @see #addColumn
	 */
	public void removeColumn(TableColumn aColumn) {
		getColumnModel().removeColumn(aColumn);
	}

	/**
	 * Moves the column <code>column</code> to the position currently occupied
	 * by the column <code>targetColumn</code> in the view. The old column at
	 * <code>targetColumn</code> is shifted left or right to make room.
	 *
	 * @param column
	 *            the index of column to be moved
	 * @param targetColumn
	 *            the new index of the column
	 */
	public void moveColumn(int column, int targetColumn) {
		getColumnModel().moveColumn(column, targetColumn);
	}

	//
	// Cover methods for various models and helper methods
	//

	/**
	 * Returns the index of the column that <code>point</code> lies in, or -1 if
	 * the result is not in the range [0, <code>getColumnCount()</code>-1].
	 *
	 * @param point
	 *            the location of interest
	 * @return the index of the column that <code>point</code> lies in, or -1 if
	 *         the result is not in the range [0,
	 *         <code>getColumnCount()</code>-1]
	 * @see #rowAtPoint
	 */
	public int columnAtPoint(Point point) {
		int x = point.x;
		if (!getComponentOrientation().isLeftToRight()) {
			x = getWidth() - x - 1;
		}
		return getColumnModel().getColumnIndexAtX(x);
	}

	/**
	 * Returns the index of the row that <code>point</code> lies in, or -1 if
	 * the result is not in the range [0, <code>getRowCount()</code>-1].
	 *
	 * @param point
	 *            the location of interest
	 * @return the index of the row that <code>point</code> lies in, or -1 if
	 *         the result is not in the range [0, <code>getRowCount()</code>-1]
	 * @see #columnAtPoint
	 */
	public int rowAtPoint(Point point) {
		int y = point.y;
		int result = (rowModel == null) ? y / getRowHeight() : rowModel.getIndex(y);
		if (result < 0) {
			return -1;
		} else if (result >= getRowCount()) {
			return -1;
		} else {
			return result;
		}
	}

	/**
	 * Returns a rectangle for the cell that lies at the intersection of
	 * <code>row</code> and <code>column</code>. If <code>includeSpacing</code>
	 * is true then the value returned has the full height and width of the row
	 * and column specified. If it is false, the returned rectangle is inset by
	 * the intercell spacing to return the true bounds of the rendering or
	 * editing component as it will be set during rendering.
	 * <p>
	 * If the column index is valid but the row index is less than zero the
	 * method returns a rectangle with the <code>y</code> and
	 * <code>height</code> values set appropriately and the <code>x</code> and
	 * <code>width</code> values both set to zero. In general, when either the
	 * row or column indices indicate a cell outside the appropriate range, the
	 * method returns a rectangle depicting the closest edge of the closest cell
	 * that is within the table's range. When both row and column indices are
	 * out of range the returned rectangle covers the closest point of the
	 * closest cell.
	 * <p>
	 * In all cases, calculations that use this method to calculate results
	 * along one axis will not fail because of anomalies in calculations along
	 * the other axis. When the cell is not valid the
	 * <code>includeSpacing</code> parameter is ignored.
	 *
	 * @param row
	 *            the row index where the desired cell is located
	 * @param column
	 *            the column index where the desired cell is located in the
	 *            display; this is not necessarily the same as the column index
	 *            in the data model for the table; the
	 *            {@link #convertColumnIndexToView(int)} method may be used to
	 *            convert a data model column index to a display column index
	 * @param includeSpacing
	 *            if false, return the true cell bounds - computed by
	 *            subtracting the intercell spacing from the height and widths
	 *            of the column and row models
	 *
	 * @return the rectangle containing the cell at location
	 *         <code>row</code>,<code>column</code>
	 * @see #getIntercellSpacing
	 */
	public Rectangle getCellRect(int row, int column, boolean includeSpacing) {
		Rectangle r = new Rectangle();
		boolean valid = true;
		if (row < 0) {
			// y = height = 0;
			valid = false;
		} else if (row >= getRowCount()) {
			r.y = getHeight();
			valid = false;
		} else {
			r.height = getRowHeight(row);
			r.y = (rowModel == null) ? row * r.height : rowModel.getPosition(row);
		}

		if (column < 0) {
			if (!getComponentOrientation().isLeftToRight()) {
				r.x = getWidth();
			}
			// otherwise, x = width = 0;
			valid = false;
		} else if (column >= getColumnCount()) {
			if (getComponentOrientation().isLeftToRight()) {
				r.x = getWidth();
			}
			// otherwise, x = width = 0;
			valid = false;
		} else {
			TableColumnModel cm = getColumnModel();
			if (getComponentOrientation().isLeftToRight()) {
				for (int i = 0; i < column; i++) {
					r.x += cm.getColumn(i).getWidth();
				}
			} else {
				for (int i = cm.getColumnCount() - 1; i > column; i--) {
					r.x += cm.getColumn(i).getWidth();
				}
			}
			r.width = cm.getColumn(column).getWidth();
		}

		if (valid && !includeSpacing) {
			// Bound the margins by their associated dimensions to prevent
			// returning bounds with negative dimensions.
			int rm = Math.min(getRowMargin(), r.height);
			int cm = Math.min(getColumnModel().getColumnMargin(), r.width);
			// This is not the same as grow(), it rounds differently.
			r.setBounds(r.x + cm / 2, r.y + rm / 2, r.width - cm, r.height - rm);
		}
		return r;
	}

	private int viewIndexForColumn(TableColumn aColumn) {
		TableColumnModel cm = getColumnModel();
		for (int column = 0; column < cm.getColumnCount(); column++) {
			if (cm.getColumn(column) == aColumn) {
				return column;
			}
		}
		return -1;
	}

	/**
	 * Causes this table to lay out its rows and columns. Overridden so that
	 * columns can be resized to accomodate a change in the size of a containing
	 * parent. Resizes one or more of the columns in the table so that the total
	 * width of all of this <code>JTable</code>'s columns is equal to the width
	 * of the table.
	 * <p>
	 * Before the layout begins the method gets the <code>resizingColumn</code>
	 * of the <code>tableHeader</code>. When the method is called as a result of
	 * the resizing of an enclosing window, the <code>resizingColumn</code> is
	 * <code>null</code>. This means that resizing has taken place "outside" the
	 * <code>JTable</code> and the change - or "delta" - should be distributed
	 * to all of the columns regardless of this <code>JTable</code>'s automatic
	 * resize mode.
	 * <p>
	 * If the <code>resizingColumn</code> is not <code>null</code>, it is one of
	 * the columns in the table that has changed size rather than the table
	 * itself. In this case the auto-resize modes govern the way the extra (or
	 * deficit) space is distributed amongst the available columns.
	 * <p>
	 * The modes are:
	 * <ul>
	 * <li>AUTO_RESIZE_OFF: Don't automatically adjust the column's widths at
	 * all. Use a horizontal scrollbar to accomodate the columns when their sum
	 * exceeds the width of the <code>Viewport</code>. If the
	 * <code>JTable</code> is not enclosed in a <code>JScrollPane</code> this
	 * may leave parts of the table invisible.
	 * <li>AUTO_RESIZE_NEXT_COLUMN: Use just the column after the resizing
	 * column. This results in the "boundary" or divider between adjacent cells
	 * being independently adjustable.
	 * <li>AUTO_RESIZE_SUBSEQUENT_COLUMNS: Use all columns after the one being
	 * adjusted to absorb the changes. This is the default behavior.
	 * <li>AUTO_RESIZE_LAST_COLUMN: Automatically adjust the size of the last
	 * column only. If the bounds of the last column prevent the desired size
	 * from being allocated, set the width of the last column to the appropriate
	 * limit and make no further adjustments.
	 * <li>AUTO_RESIZE_ALL_COLUMNS: Spread the delta amongst all the columns in
	 * the <code>JTable</code>, including the one that is being adjusted.
	 * </ul>
	 * <p>
	 * <bold>Note:</bold> When a <code>JTable</code> makes adjustments to the
	 * widths of the columns it respects their minimum and maximum values
	 * absolutely. It is therefore possible that, even after this method is
	 * called, the total width of the columns is still not equal to the width of
	 * the table. When this happens the <code>JTable</code> does not put itself
	 * in AUTO_RESIZE_OFF mode to bring up a scroll bar, or break other
	 * commitments of its current auto-resize mode -- instead it allows its
	 * bounds to be set larger (or smaller) than the total of the column minimum
	 * or maximum, meaning, either that there will not be enough room to display
	 * all of the columns, or that the columns will not fill the
	 * <code>JTable</code>'s bounds. These respectively, result in the clipping
	 * of some columns or an area being painted in the <code>JTable</code>'s
	 * background color during painting.
	 * <p>
	 * The mechanism for distributing the delta amongst the available columns is
	 * provided in a private method in the <code>JTable</code> class:
	 * 
	 * <pre>
	 *   adjustSizes(long targetSize, final Resizable3 r, boolean inverse)
	 * </pre>
	 * 
	 * an explanation of which is provided in the following section.
	 * <code>Resizable3</code> is a private interface that allows any data
	 * structure containing a collection of elements with a size, preferred
	 * size, maximum size and minimum size to have its elements manipulated by
	 * the algorithm.
	 * <p>
	 * <H3>Distributing the delta</H3>
	 * <p>
	 * <H4>Overview</H4>
	 * <P>
	 * Call "DELTA" the difference between the target size and the sum of the
	 * preferred sizes of the elements in r. The individual sizes are calculated
	 * by taking the original preferred sizes and adding a share of the DELTA -
	 * that share being based on how far each preferred size is from its
	 * limiting bound (minimum or maximum).
	 * <p>
	 * <H4>Definition</H4>
	 * <P>
	 * Call the individual constraints min[i], max[i], and pref[i].
	 * <p>
	 * Call their respective sums: MIN, MAX, and PREF.
	 * <p>
	 * Each new size will be calculated using:
	 * <p>
	 * 
	 * <pre>
	 * size[i] = pref[i] + delta[i]
	 * </pre>
	 * 
	 * where each individual delta[i] is calculated according to:
	 * <p>
	 * If (DELTA < 0) we are in shrink mode where:
	 * <p>
	 * 
	 * <PRE>
	 *                        DELTA
	 *          delta[i] = ------------ * (pref[i] - min[i])
	 *                     (PREF - MIN)
	 * </PRE>
	 * 
	 * If (DELTA > 0) we are in expand mode where:
	 * <p>
	 * 
	 * <PRE>
	 *                        DELTA
	 *          delta[i] = ------------ * (max[i] - pref[i])
	 *                      (MAX - PREF)
	 * </PRE>
	 * <P>
	 * The overall effect is that the total size moves that same percentage, k,
	 * towards the total minimum or maximum and that percentage guarantees
	 * accomodation of the required space, DELTA.
	 *
	 * <H4>Details</H4>
	 * <P>
	 * Naive evaluation of the formulae presented here would be subject to the
	 * aggregated rounding errors caused by doing this operation in finite
	 * precision (using ints). To deal with this, the multiplying factor above,
	 * is constantly recalculated and this takes account of the rounding errors
	 * in the previous iterations. The result is an algorithm that produces a
	 * set of integers whose values exactly sum to the supplied
	 * <code>targetSize</code>, and does so by spreading the rounding errors
	 * evenly over the given elements.
	 *
	 * <H4>When the MAX and MIN bounds are hit</H4>
	 * <P>
	 * When <code>targetSize</code> is outside the [MIN, MAX] range, the
	 * algorithm sets all sizes to their appropriate limiting value (maximum or
	 * minimum).
	 *
	 */
	@Override
	public void doLayout() {
		TableColumn resizingColumn = getResizingColumn();
		if (resizingColumn == null) {
			setWidthsFromPreferredWidths(false);
		} else {
			// JTable behaves like a layout manger - but one in which the
			// user can come along and dictate how big one of the children
			// (columns) is supposed to be.

			// A column has been resized and JTable may need to distribute
			// any overall delta to other columns, according to the resize mode.
			int columnIndex = viewIndexForColumn(resizingColumn);
			int delta = getWidth() - getColumnModel().getTotalColumnWidth();
			accommodateDelta(columnIndex, delta);
			delta = getWidth() - getColumnModel().getTotalColumnWidth();

			// If the delta cannot be completely accomodated, then the
			// resizing column will have to take any remainder. This means
			// that the column is not being allowed to take the requested
			// width. This happens under many circumstances: For example,
			// AUTO_RESIZE_NEXT_COLUMN specifies that any delta be distributed
			// to the column after the resizing column. If one were to attempt
			// to resize the last column of the table, there would be no
			// columns after it, and hence nowhere to distribute the delta.
			// It would then be given entirely back to the resizing column,
			// preventing it from changing size.
			if (delta != 0) {
				resizingColumn.setWidth(resizingColumn.getWidth() + delta);
			}

			// At this point the JTable has to work out what preferred sizes
			// would have resulted in the layout the user has chosen.
			// Thereafter, during window resizing etc. it has to work off
			// the preferred sizes as usual - the idea being that, whatever
			// the user does, everything stays in synch and things don't jump
			// around.
			setWidthsFromPreferredWidths(true);
		}

		super.doLayout();
	}

	private TableColumn getResizingColumn() {
		return (tableHeader == null) ? null : tableHeader.getResizingColumn();
	}

	/**
	 * Sizes the table columns to fit the available space.
	 * 
	 * @deprecated As of Swing version 1.0.3, replaced by
	 *             <code>doLayout()</code>.
	 * @see #doLayout
	 */
	@Deprecated
	public void sizeColumnsToFit(boolean lastColumnOnly) {
		int oldAutoResizeMode = autoResizeMode;
		setAutoResizeMode(lastColumnOnly ? AUTO_RESIZE_LAST_COLUMN : AUTO_RESIZE_ALL_COLUMNS);
		sizeColumnsToFit(-1);
		setAutoResizeMode(oldAutoResizeMode);
	}

	/**
	 * Obsolete as of Java 2 platform v1.4. Please use the
	 * <code>doLayout()</code> method instead.
	 * 
	 * @param resizingColumn
	 *            the column whose resizing made this adjustment necessary or -1
	 *            if there is no such column
	 * @see #doLayout
	 */
	public void sizeColumnsToFit(int resizingColumn) {
		if (resizingColumn == -1) {
			setWidthsFromPreferredWidths(false);
		} else {
			if (autoResizeMode == AUTO_RESIZE_OFF) {
				TableColumn aColumn = getColumnModel().getColumn(resizingColumn);
				aColumn.setPreferredWidth(aColumn.getWidth());
			} else {
				int delta = getWidth() - getColumnModel().getTotalColumnWidth();
				accommodateDelta(resizingColumn, delta);
				setWidthsFromPreferredWidths(true);
			}
		}
	}

	private void setWidthsFromPreferredWidths(final boolean inverse) {
		int totalWidth = getWidth();
		int totalPreferred = getPreferredSize().width;
		int target = !inverse ? totalWidth : totalPreferred;

		final TableColumnModel cm = columnModel;
		Resizable3 r = new Resizable3() {
			@Override
			public int getElementCount() {
				return cm.getColumnCount();
			}

			@Override
			public int getLowerBoundAt(int i) {
				return cm.getColumn(i).getMinWidth();
			}

			@Override
			public int getUpperBoundAt(int i) {
				return cm.getColumn(i).getMaxWidth();
			}

			@Override
			public int getMidPointAt(int i) {
				if (!inverse) {
					return cm.getColumn(i).getPreferredWidth();
				} else {
					return cm.getColumn(i).getWidth();
				}
			}

			@Override
			public void setSizeAt(int s, int i) {
				if (!inverse) {
					cm.getColumn(i).setWidth(s);
				} else {
					cm.getColumn(i).setPreferredWidth(s);
				}
			}
		};

		adjustSizes(target, r, inverse);
	}

	// Distribute delta over columns, as indicated by the autoresize mode.
	private void accommodateDelta(int resizingColumnIndex, int delta) {
		int columnCount = getColumnCount();
		int from = resizingColumnIndex;
		int to = columnCount;

		// Use the mode to determine how to absorb the changes.
		switch (autoResizeMode) {
		case AUTO_RESIZE_NEXT_COLUMN:
			from = from + 1;
			to = Math.min(from + 1, columnCount);
			break;
		case AUTO_RESIZE_SUBSEQUENT_COLUMNS:
			from = from + 1;
			to = columnCount;
			break;
		case AUTO_RESIZE_LAST_COLUMN:
			from = columnCount - 1;
			to = from + 1;
			break;
		case AUTO_RESIZE_ALL_COLUMNS:
			from = 0;
			to = columnCount;
			break;
		default:
			return;
		}

		final int start = from;
		final int end = to;
		final TableColumnModel cm = columnModel;
		Resizable3 r = new Resizable3() {
			@Override
			public int getElementCount() {
				return end - start;
			}

			@Override
			public int getLowerBoundAt(int i) {
				return cm.getColumn(i + start).getMinWidth();
			}

			@Override
			public int getUpperBoundAt(int i) {
				return cm.getColumn(i + start).getMaxWidth();
			}

			@Override
			public int getMidPointAt(int i) {
				return cm.getColumn(i + start).getWidth();
			}

			@Override
			public void setSizeAt(int s, int i) {
				cm.getColumn(i + start).setWidth(s);
			}
		};

		int totalWidth = 0;
		for (int i = from; i < to; i++) {
			TableColumn aColumn = columnModel.getColumn(i);
			int input = aColumn.getWidth();
			totalWidth = totalWidth + input;
		}

		adjustSizes(totalWidth + delta, r, false);

		return;
	}

	private interface Resizable2 {
		public int getElementCount();

		public int getLowerBoundAt(int i);

		public int getUpperBoundAt(int i);

		public void setSizeAt(int newSize, int i);
	}

	private interface Resizable3 extends Resizable2 {
		public int getMidPointAt(int i);
	}

	private void adjustSizes(long target, final Resizable3 r, boolean inverse) {
		int N = r.getElementCount();
		long totalPreferred = 0;
		for (int i = 0; i < N; i++) {
			totalPreferred += r.getMidPointAt(i);
		}
		Resizable2 s;
		if ((target < totalPreferred) == !inverse) {
			s = new Resizable2() {
				@Override
				public int getElementCount() {
					return r.getElementCount();
				}

				@Override
				public int getLowerBoundAt(int i) {
					return r.getLowerBoundAt(i);
				}

				@Override
				public int getUpperBoundAt(int i) {
					return r.getMidPointAt(i);
				}

				@Override
				public void setSizeAt(int newSize, int i) {
					r.setSizeAt(newSize, i);
				}

			};
		} else {
			s = new Resizable2() {
				@Override
				public int getElementCount() {
					return r.getElementCount();
				}

				@Override
				public int getLowerBoundAt(int i) {
					return r.getMidPointAt(i);
				}

				@Override
				public int getUpperBoundAt(int i) {
					return r.getUpperBoundAt(i);
				}

				@Override
				public void setSizeAt(int newSize, int i) {
					r.setSizeAt(newSize, i);
				}

			};
		}
		adjustSizes(target, s, !inverse);
	}

	private void adjustSizes(long target, Resizable2 r, boolean limitToRange) {
		long totalLowerBound = 0;
		long totalUpperBound = 0;
		for (int i = 0; i < r.getElementCount(); i++) {
			totalLowerBound += r.getLowerBoundAt(i);
			totalUpperBound += r.getUpperBoundAt(i);
		}

		if (limitToRange) {
			target = Math.min(Math.max(totalLowerBound, target), totalUpperBound);
		}

		for (int i = 0; i < r.getElementCount(); i++) {
			int lowerBound = r.getLowerBoundAt(i);
			int upperBound = r.getUpperBoundAt(i);
			// Check for zero. This happens when the distribution of the delta
			// finishes early due to a series of "fixed" entries at the end.
			// In this case, lowerBound == upperBound, for all subsequent terms.
			int newSize;
			if (totalLowerBound == totalUpperBound) {
				newSize = lowerBound;
			} else {
				double f = (double) (target - totalLowerBound) / (totalUpperBound - totalLowerBound);
				newSize = (int) Math.round(lowerBound + f * (upperBound - lowerBound));
				// We'd need to round manually in an all integer version.
				// size[i] = (int)(((totalUpperBound - target) * lowerBound +
				// (target - totalLowerBound) *
				// upperBound)/(totalUpperBound-totalLowerBound));
			}
			r.setSizeAt(newSize, i);
			target -= newSize;
			totalLowerBound -= lowerBound;
			totalUpperBound -= upperBound;
		}
	}

	/**
	 * Overrides <code>JComponent</code>'s <code>getToolTipText</code> method in
	 * order to allow the renderer's tips to be used if it has text set.
	 * <p>
	 * <bold>Note:</bold> For <code>JTable</code> to properly display tooltips
	 * of its renderers <code>JTable</code> must be a registered component with
	 * the <code>ToolTipManager</code>. This is done automatically in
	 * <code>initializeLocalVars</code>, but if at a later point
	 * <code>JTable</code> is told <code>setToolTipText(null)</code> it will
	 * unregister the table component, and no tips from renderers will display
	 * anymore.
	 *
	 * @see JComponent#getToolTipText
	 */
	@Override
	public String getToolTipText(MouseEvent event) {
		String tip = null;
		Point p = event.getPoint();

		// Locate the renderer under the event location
		int hitColumnIndex = columnAtPoint(p);
		int hitRowIndex = rowAtPoint(p);

		if ((hitColumnIndex != -1) && (hitRowIndex != -1)) {
			TableCellRenderer renderer = getCellRenderer(hitRowIndex, hitColumnIndex);
			Component component = prepareRenderer(renderer, hitRowIndex, hitColumnIndex);

			// Now have to see if the component is a JComponent before
			// getting the tip
			if (component instanceof JComponent) {
				// Convert the event to the renderer's coordinate system
				Rectangle cellRect = getCellRect(hitRowIndex, hitColumnIndex, false);
				p.translate(-cellRect.x, -cellRect.y);
				MouseEvent newEvent = new MouseEvent(component, event.getID(), event.getWhen(), event.getModifiers(),
						p.x, p.y, event.getXOnScreen(), event.getYOnScreen(), event.getClickCount(),
						event.isPopupTrigger(), MouseEvent.NOBUTTON);

				tip = ((JComponent) component).getToolTipText(newEvent);
			}
		}

		// No tip from the renderer get our own tip
		if (tip == null)
			tip = getToolTipText();

		return tip;
	}

	//
	// Editing Support
	//

	/**
	 * Sets whether editors in this JTable get the keyboard focus when an editor
	 * is activated as a result of the JTable forwarding keyboard events for a
	 * cell. By default, this property is false, and the JTable retains the
	 * focus unless the cell is clicked.
	 *
	 * @param surrendersFocusOnKeystroke
	 *            true if the editor should get the focus when keystrokes cause
	 *            the editor to be activated
	 *
	 *
	 * @see #getSurrendersFocusOnKeystroke
	 * @since 1.4
	 */
	public void setSurrendersFocusOnKeystroke(boolean surrendersFocusOnKeystroke) {
		this.surrendersFocusOnKeystroke = surrendersFocusOnKeystroke;
	}

	/**
	 * Returns true if the editor should get the focus when keystrokes cause the
	 * editor to be activated
	 *
	 * @return true if the editor should get the focus when keystrokes cause the
	 *         editor to be activated
	 *
	 * @see #setSurrendersFocusOnKeystroke
	 * @since 1.4
	 */
	public boolean getSurrendersFocusOnKeystroke() {
		return surrendersFocusOnKeystroke;
	}

	/**
	 * Programmatically starts editing the cell at <code>row</code> and
	 * <code>column</code>, if those indices are in the valid range, and the
	 * cell at those indices is editable. Note that this is a convenience method
	 * for <code>editCellAt(int, int, null)</code>.
	 *
	 * @param row
	 *            the row to be edited
	 * @param column
	 *            the column to be edited
	 * @return false if for any reason the cell cannot be edited, or if the
	 *         indices are invalid
	 */
	public boolean editCellAt(int row, int column) {
		return editCellAt(row, column, null);
	}

	/**
	 * Programmatically starts editing the cell at <code>row</code> and
	 * <code>column</code>, if those indices are in the valid range, and the
	 * cell at those indices is editable. To prevent the <code>JTable</code>
	 * from editing a particular table, column or cell value, return false from
	 * the <code>isCellEditable</code> method in the <code>TableModel</code>
	 * interface.
	 *
	 * @param row
	 *            the row to be edited
	 * @param column
	 *            the column to be edited
	 * @param e
	 *            event to pass into <code>shouldSelectCell</code>; note that as
	 *            of Java 2 platform v1.2, the call to
	 *            <code>shouldSelectCell</code> is no longer made
	 * @return false if for any reason the cell cannot be edited, or if the
	 *         indices are invalid
	 */
	public boolean editCellAt(int row, int column, EventObject e) {
		if (cellEditor != null && !cellEditor.stopCellEditing()) {
			return false;
		}

		if (row < 0 || row >= getRowCount() || column < 0 || column >= getColumnCount()) {
			return false;
		}

		if (!isCellEditable(row, column))
			return false;

		if (editorRemover == null) {
			// KeyboardFocusManager fm =
			// KeyboardFocusManager.getCurrentKeyboardFocusManager();
			// editorRemover = new CellEditorRemover(fm);
			// fm.addPropertyChangeListener("permanentFocusOwner",
			// editorRemover);
		}

		TableCellEditor editor = getCellEditor(row, column);
		if (editor != null && editor.isCellEditable(e)) {
			JComponent comp = (JComponent) (editorComp = prepareEditor(editor, row, column));
			if (editorComp == null) {
				removeEditor();
				return false;
			}
			Rectangle rect = getCellRect(row, column, false);
			if (comp instanceof JTextField) {
				rect.y -= 3;
				rect.width -= 2;
				rect.height -= 3;
			}
			comp.setBounds(rect);
			add(comp);
			comp.validate();
			comp.repaint();
			comp.setVisible(true);
			// force domNode to be visible as well as outer node
			((JSComponentUI) comp.getUI()).setVisible(null, true);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					comp.requestFocus();
					((JSComponentUI) comp.getUI()).notifyFocus(true);
				}
			});

			// BH SwingJS - moved these next two lines up so that we have that
			// info in JSTableUI
			setEditingRow(row);
			setEditingColumn(column);
			setCellEditor(editor);
			// setEditingRow(row);
			// setEditingColumn(column);
			editor.removeCellEditorListener(this);
			editor.addCellEditorListener(this);

			return true;
		}
		return false;
	}

	/**
	 * Returns true if a cell is being edited.
	 *
	 * @return true if the table is editing a cell
	 * @see #editingColumn
	 * @see #editingRow
	 */
	public boolean isEditing() {
		return (cellEditor == null) ? false : true;
	}

	/**
	 * Returns the component that is handling the editing session. If nothing is
	 * being edited, returns null.
	 *
	 * @return Component handling editing session
	 */
	public Component getEditorComponent() {
		return editorComp;
	}

	/**
	 * Returns the index of the column that contains the cell currently being
	 * edited. If nothing is being edited, returns -1.
	 *
	 * @return the index of the column that contains the cell currently being
	 *         edited; returns -1 if nothing being edited
	 * @see #editingRow
	 */
	public int getEditingColumn() {
		return editingColumn;
	}

	/**
	 * Returns the index of the row that contains the cell currently being
	 * edited. If nothing is being edited, returns -1.
	 *
	 * @return the index of the row that contains the cell currently being
	 *         edited; returns -1 if nothing being edited
	 * @see #editingColumn
	 */
	public int getEditingRow() {
		return editingRow;
	}

	//
	// Managing TableUI
	//

	/**
	 * Returns the L&F object that renders this component.
	 *
	 * @return the <code>TableUI</code> object that renders this component
	 */
	@Override
	public TableUI getUI() {
		return (TableUI) ui;
	}

	/**
	 * Sets the L&F object that renders this component and repaints.
	 * 
	 * @param ui
	 *            the TableUI L&F object
	 * @see UIDefaults#getUI
	 * @beaninfo bound: true hidden: true attribute: visualUpdate true
	 *           description: The UI object that implements the Component's
	 *           LookAndFeel.
	 */
	public void setUI(TableUI ui) {
		if (this.ui != ui) {
			super.setUI(ui);
			repaint();
		}
	}

	/**
	 * Notification from the <code>UIManager</code> that the L&F has changed.
	 * Replaces the current UI object with the latest version from the
	 * <code>UIManager</code>.
	 *
	 * @see JComponent#updateUI
	 */
	@Override
	public void updateUI() {
		// Update the UIs of the cell renderers, cell editors and header
		// renderers.
		TableColumnModel cm = getColumnModel();
		for (int column = 0; column < cm.getColumnCount(); column++) {
			TableColumn aColumn = cm.getColumn(column);
			SwingUtilities.updateRendererOrEditorUI(aColumn.getCellRenderer());
			SwingUtilities.updateRendererOrEditorUI(aColumn.getCellEditor());
			SwingUtilities.updateRendererOrEditorUI(aColumn.getHeaderRenderer());
		}

		// Update the UIs of all the default renderers.
		Enumeration defaultRenderers = defaultRenderersByColumnClass.elements();
		while (defaultRenderers.hasMoreElements()) {
			SwingUtilities.updateRendererOrEditorUI(defaultRenderers.nextElement());
		}

		// Update the UIs of all the default editors.
		Enumeration defaultEditors = defaultEditorsByColumnClass.elements();
		while (defaultEditors.hasMoreElements()) {
			SwingUtilities.updateRendererOrEditorUI(defaultEditors.nextElement());
		}

		// Update the UI of the table header
		if (tableHeader != null && tableHeader.getParent() == null) {
			tableHeader.updateUI();
		}

		// Update UI applied to parent ScrollPane
		configureEnclosingScrollPaneUI();
		super.updateUI();
	}

	//
	// Managing models
	//

	/**
	 * Sets the data model for this table to <code>newModel</code> and registers
	 * with it for listener notifications from the new data model.
	 *
	 * @param dataModel
	 *            the new data source for this table
	 * @exception IllegalArgumentException
	 *                if <code>newModel</code> is <code>null</code>
	 * @see #getModel
	 * @beaninfo bound: true description: The model that is the source of the
	 *           data for this view.
	 */
	public void setModel(TableModel dataModel) {
		if (dataModel == null) {
			throw new IllegalArgumentException("Cannot set a null TableModel");
		}
		if (this.dataModel != dataModel) {
			TableModel old = this.dataModel;
			if (old != null) {
				old.removeTableModelListener(this);
			}
			this.dataModel = dataModel;
			dataModel.addTableModelListener(this);

			tableChanged(new TableModelEvent(dataModel, TableModelEvent.HEADER_ROW));

			firePropertyChange("model", old, dataModel);

			if (getAutoCreateRowSorter()) {
				setRowSorter(new TableRowSorter(dataModel));
			}
		}
	}

	/**
	 * Returns the <code>TableModel</code> that provides the data displayed by
	 * this <code>JTable</code>.
	 *
	 * @return the <code>TableModel</code> that provides the data displayed by
	 *         this <code>JTable</code>
	 * @see #setModel
	 */
	public TableModel getModel() {
		return dataModel;
	}

	/**
	 * Sets the column model for this table to <code>newModel</code> and
	 * registers for listener notifications from the new column model. Also sets
	 * the column model of the <code>JTableHeader</code> to
	 * <code>columnModel</code>.
	 *
	 * @param columnModel
	 *            the new data source for this table
	 * @exception IllegalArgumentException
	 *                if <code>columnModel</code> is <code>null</code>
	 * @see #getColumnModel
	 * @beaninfo bound: true description: The object governing the way columns
	 *           appear in the view.
	 */
	public void setColumnModel(TableColumnModel columnModel) {
		if (columnModel == null) {
			throw new IllegalArgumentException("Cannot set a null ColumnModel");
		}
		TableColumnModel old = this.columnModel;
		if (columnModel != old) {
			if (old != null) {
				old.removeColumnModelListener(this);
			}
			this.columnModel = columnModel;
			columnModel.addColumnModelListener(this);

			// Set the column model of the header as well.
			if (tableHeader != null) {
				tableHeader.setColumnModel(columnModel);
			}

			firePropertyChange("columnModel", old, columnModel);
			resizeAndRepaint();
		}
	}

	/**
	 * Returns the <code>TableColumnModel</code> that contains all column
	 * information of this table.
	 *
	 * @return the object that provides the column state of the table
	 * @see #setColumnModel
	 */
	public TableColumnModel getColumnModel() {
		return columnModel;
	}

	/**
	 * Sets the row selection model for this table to <code>newModel</code> and
	 * registers for listener notifications from the new selection model.
	 *
	 * @param newModel
	 *            the new selection model
	 * @exception IllegalArgumentException
	 *                if <code>newModel</code> is <code>null</code>
	 * @see #getSelectionModel
	 * @beaninfo bound: true description: The selection model for rows.
	 */
	public void setSelectionModel(ListSelectionModel newModel) {
		if (newModel == null) {
			throw new IllegalArgumentException("Cannot set a null SelectionModel");
		}

		ListSelectionModel oldModel = selectionModel;

		if (newModel != oldModel) {
			if (oldModel != null) {
				oldModel.removeListSelectionListener(this);
			}

			selectionModel = newModel;
			newModel.addListSelectionListener(this);

			firePropertyChange("selectionModel", oldModel, newModel);
			repaint();
		}
	}

	/**
	 * Returns the <code>ListSelectionModel</code> that is used to maintain row
	 * selection state.
	 *
	 * @return the object that provides row selection state, <code>null</code>
	 *         if row selection is not allowed
	 * @see #setSelectionModel
	 */
	public ListSelectionModel getSelectionModel() {
		return selectionModel;
	}

	//
	// RowSorterListener
	//

	/**
	 * <code>RowSorterListener</code> notification that the
	 * <code>RowSorter</code> has changed in some way.
	 *
	 * @param e
	 *            the <code>RowSorterEvent</code> describing the change
	 * @throws NullPointerException
	 *             if <code>e</code> is <code>null</code>
	 * @since 1.6
	 */
	@Override
	public void sorterChanged(RowSorterEvent e) {
		if (e.getType() == RowSorterEvent.Type.SORT_ORDER_CHANGED) {
			JTableHeader header = getTableHeader();
			if (header != null) {
				header.repaint();
			}
		} else if (e.getType() == RowSorterEvent.Type.SORTED) {
			sorterChanged = true;
			if (!ignoreSortChange) {
				sortedTableChanged(e, null);
			}
		}
	}

	/**
	 * SortManager provides support for managing the selection and variable row
	 * heights when sorting is enabled. This information is encapsulated into a
	 * class to avoid bulking up JTable.
	 */
	private final class SortManager {
		RowSorter<? extends TableModel> sorter;

		// Selection, in terms of the model. This is lazily created
		// as needed.
		private ListSelectionModel modelSelection;
		private int modelLeadIndex;
		// Set to true while in the process of changing the selection.
		// If this is true the selection change is ignored.
		private boolean syncingSelection;
		// Temporary cache of selection, in terms of model. This is only used
		// if we don't need the full weight of modelSelection.
		private int[] lastModelSelection;

		// Heights of the rows in terms of the model.
		protected SizeSequence modelRowSizes;

		SortManager(RowSorter<? extends TableModel> sorter) {
			this.sorter = sorter;
			sorter.addRowSorterListener(JTable.this);
		}

		/**
		 * Disposes any resources used by this SortManager.
		 */
		public void dispose() {
			if (sorter != null) {
				sorter.removeRowSorterListener(JTable.this);
			}
		}

		/**
		 * Sets the height for a row at a specified index.
		 */
		public void setViewRowHeight(int viewIndex, int rowHeight) {
			if (modelRowSizes == null) {
				modelRowSizes = new SizeSequence(getModel().getRowCount(), getRowHeight());
			}
			modelRowSizes.setSize(convertRowIndexToModel(viewIndex), rowHeight);
		}

		/**
		 * Invoked when the underlying model has completely changed.
		 */
		public void allChanged() {
			modelLeadIndex = -1;
			modelSelection = null;
			modelRowSizes = null;
		}

		/**
		 * Invoked when the selection, on the view, has changed.
		 */
		public void viewSelectionChanged(ListSelectionEvent e) {
			if (!syncingSelection && modelSelection != null) {
				modelSelection = null;
			}
		}

		/**
		 * Invoked when either the table model has changed, or the RowSorter has
		 * changed. This is invoked prior to notifying the sorter of the change.
		 */
		public void prepareForChange(RowSorterEvent sortEvent, ModelChange change) {
			if (getUpdateSelectionOnSort()) {
				cacheSelection(sortEvent, change);
			}
		}

		/**
		 * Updates the internal cache of the selection based on the change.
		 */
		private void cacheSelection(RowSorterEvent sortEvent, ModelChange change) {
			if (sortEvent != null) {
				// sort order changed. If modelSelection is null and filtering
				// is enabled we need to cache the selection in terms of the
				// underlying model, this will allow us to correctly restore
				// the selection even if rows are filtered out.
				if (modelSelection == null && sorter.getViewRowCount() != getModel().getRowCount()) {
					modelSelection = new DefaultListSelectionModel();
					ListSelectionModel viewSelection = getSelectionModel();
					int min = viewSelection.getMinSelectionIndex();
					int max = viewSelection.getMaxSelectionIndex();
					int modelIndex;
					for (int viewIndex = min; viewIndex <= max; viewIndex++) {
						if (viewSelection.isSelectedIndex(viewIndex)) {
							modelIndex = convertRowIndexToModel(sortEvent, viewIndex);
							if (modelIndex != -1) {
								modelSelection.addSelectionInterval(modelIndex, modelIndex);
							}
						}
					}
					modelIndex = convertRowIndexToModel(sortEvent, viewSelection.getLeadSelectionIndex());
					SwingUtilities2.setLeadAnchorWithoutSelection(modelSelection, modelIndex, modelIndex);
				} else if (modelSelection == null) {
					// Sorting changed, haven't cached selection in terms
					// of model and no filtering. Temporarily cache selection.
					cacheModelSelection(sortEvent);
				}
			} else if (change.allRowsChanged) {
				// All the rows have changed, chuck any cached selection.
				modelSelection = null;
			} else if (modelSelection != null) {
				// Table changed, reflect changes in cached selection model.
				switch (change.type) {
				case TableModelEvent.DELETE:
					modelSelection.removeIndexInterval(change.startModelIndex, change.endModelIndex);
					break;
				case TableModelEvent.INSERT:
					modelSelection.insertIndexInterval(change.startModelIndex, change.endModelIndex, true);
					break;
				default:
					break;
				}
			} else {
				// table changed, but haven't cached rows, temporarily
				// cache them.
				cacheModelSelection(null);
			}
		}

		private void cacheModelSelection(RowSorterEvent sortEvent) {
			lastModelSelection = convertSelectionToModel(sortEvent);
			modelLeadIndex = convertRowIndexToModel(sortEvent, selectionModel.getLeadSelectionIndex());
		}

		/**
		 * Inovked when either the table has changed or the sorter has changed
		 * and after the sorter has been notified. If necessary this will
		 * reapply the selection and variable row heights.
		 */
		public void processChange(RowSorterEvent sortEvent, ModelChange change, boolean sorterChanged) {
			if (change != null) {
				if (change.allRowsChanged) {
					modelRowSizes = null;
					rowModel = null;
				} else if (modelRowSizes != null) {
					if (change.type == TableModelEvent.INSERT) {
						modelRowSizes.insertEntries(change.startModelIndex,
								change.endModelIndex - change.startModelIndex + 1, getRowHeight());
					} else if (change.type == TableModelEvent.DELETE) {
						modelRowSizes.removeEntries(change.startModelIndex,
								change.endModelIndex - change.startModelIndex + 1);
					}
				}
			}
			if (sorterChanged) {
				setViewRowHeightsFromModel();
				restoreSelection(change);
			}
		}

		/**
		 * Resets the variable row heights in terms of the view from that of the
		 * variable row heights in terms of the model.
		 */
		private void setViewRowHeightsFromModel() {
			if (modelRowSizes != null) {
				rowModel.setSizes(getRowCount(), getRowHeight());
				for (int viewIndex = getRowCount() - 1; viewIndex >= 0; viewIndex--) {
					int modelIndex = convertRowIndexToModel(viewIndex);
					rowModel.setSize(viewIndex, modelRowSizes.getSize(modelIndex));
				}
			}
		}

		/**
		 * Restores the selection from that in terms of the model.
		 */
		private void restoreSelection(ModelChange change) {
			syncingSelection = true;
			if (lastModelSelection != null) {
				restoreSortingSelection(lastModelSelection, modelLeadIndex, change);
				lastModelSelection = null;
			} else if (modelSelection != null) {
				ListSelectionModel viewSelection = getSelectionModel();
				viewSelection.setValueIsAdjusting(true);
				viewSelection.clearSelection();
				int min = modelSelection.getMinSelectionIndex();
				int max = modelSelection.getMaxSelectionIndex();
				int viewIndex;
				for (int modelIndex = min; modelIndex <= max; modelIndex++) {
					if (modelSelection.isSelectedIndex(modelIndex)) {
						viewIndex = convertRowIndexToView(modelIndex);
						if (viewIndex != -1) {
							viewSelection.addSelectionInterval(viewIndex, viewIndex);
						}
					}
				}
				// Restore the lead
				int viewLeadIndex = modelSelection.getLeadSelectionIndex();
				if (viewLeadIndex != -1) {
					viewLeadIndex = convertRowIndexToView(viewLeadIndex);
				}
				SwingUtilities2.setLeadAnchorWithoutSelection(viewSelection, viewLeadIndex, viewLeadIndex);
				viewSelection.setValueIsAdjusting(false);
			}
			syncingSelection = false;
		}
	}

	/**
	 * ModelChange is used when sorting to restore state, it corresponds to data
	 * from a TableModelEvent. The values are precalculated as they are used
	 * extensively.
	 */
	private final class ModelChange {
		// Starting index of the change, in terms of the model
		int startModelIndex;

		// Ending index of the change, in terms of the model
		int endModelIndex;

		// Type of change
		int type;

		// Number of rows in the model
		int modelRowCount;

		// The event that triggered this.
		TableModelEvent event;

		// Length of the change (end - start + 1)
		int length;

		// True if the event indicates all the contents have changed
		boolean allRowsChanged;

		ModelChange(TableModelEvent e) {
			startModelIndex = Math.max(0, e.getFirstRow());
			endModelIndex = e.getLastRow();
			modelRowCount = getModel().getRowCount();
			if (endModelIndex < 0) {
				endModelIndex = Math.max(0, modelRowCount - 1);
			}
			length = endModelIndex - startModelIndex + 1;
			type = e.getType();
			event = e;
			allRowsChanged = (e.getLastRow() == Integer.MAX_VALUE);
		}
	}

	/**
	 * Invoked when <code>sorterChanged</code> is invoked, or when
	 * <code>tableChanged</code> is invoked and sorting is enabled.
	 */
	private void sortedTableChanged(RowSorterEvent sortedEvent, TableModelEvent e) {
		int editingModelIndex = -1;
		ModelChange change = (e != null) ? new ModelChange(e) : null;

		if ((change == null || !change.allRowsChanged) && this.editingRow != -1) {
			editingModelIndex = convertRowIndexToModel(sortedEvent, this.editingRow);
		}

		sortManager.prepareForChange(sortedEvent, change);

		if (e != null) {
			if (change.type == TableModelEvent.UPDATE) {
				repaintSortedRows(change);
			}
			notifySorter(change);
			if (change.type != TableModelEvent.UPDATE) {
				// If the Sorter is unsorted we will not have received
				// notification, force treating insert/delete as a change.
				sorterChanged = true;
			}
		} else {
			sorterChanged = true;
		}

		sortManager.processChange(sortedEvent, change, sorterChanged);

		if (sorterChanged) {
			// Update the editing row
			if (this.editingRow != -1) {
				int newIndex = (editingModelIndex == -1) ? -1 : convertRowIndexToView(editingModelIndex, change);
				restoreSortingEditingRow(newIndex);
			}

			// And handle the appropriate repainting.
			if (e == null || change.type != TableModelEvent.UPDATE) {
				resizeAndRepaint();
			}
		}

		// Check if lead/anchor need to be reset.
		if (change != null && change.allRowsChanged) {
			clearSelectionAndLeadAnchor();
			resizeAndRepaint();
		}
	}

	/**
	 * Repaints the sort of sorted rows in response to a TableModelEvent.
	 */
	private void repaintSortedRows(ModelChange change) {
		if (change.startModelIndex > change.endModelIndex || change.startModelIndex + 10 < change.endModelIndex) {
			// Too much has changed, punt
			repaint();
			return;
		}
		int eventColumn = change.event.getColumn();
		int columnViewIndex = eventColumn;
		if (columnViewIndex == TableModelEvent.ALL_COLUMNS) {
			columnViewIndex = 0;
		} else {
			columnViewIndex = convertColumnIndexToView(columnViewIndex);
			if (columnViewIndex == -1) {
				return;
			}
		}
		int modelIndex = change.startModelIndex;
		while (modelIndex <= change.endModelIndex) {
			int viewIndex = convertRowIndexToView(modelIndex++);
			if (viewIndex != -1) {
				Rectangle dirty = getCellRect(viewIndex, columnViewIndex, false);
				int x = dirty.x;
				int w = dirty.width;
				if (eventColumn == TableModelEvent.ALL_COLUMNS) {
					x = 0;
					w = getWidth();
				}
				repaint(x, dirty.y, w, dirty.height);
			}
		}
	}

	/**
	 * Restores the selection after a model event/sort order changes. All
	 * coordinates are in terms of the model.
	 */
	private void restoreSortingSelection(int[] selection, int lead, ModelChange change) {
		// Convert the selection from model to view
		for (int i = selection.length - 1; i >= 0; i--) {
			selection[i] = convertRowIndexToView(selection[i], change);
		}
		lead = convertRowIndexToView(lead, change);

		// Check for the common case of no change in selection for 1 row
		if (selection.length == 0 || (selection.length == 1 && selection[0] == getSelectedRow())) {
			return;
		}

		// And apply the new selection
		selectionModel.setValueIsAdjusting(true);
		selectionModel.clearSelection();
		for (int i = selection.length - 1; i >= 0; i--) {
			if (selection[i] != -1) {
				selectionModel.addSelectionInterval(selection[i], selection[i]);
			}
		}
		SwingUtilities2.setLeadAnchorWithoutSelection(selectionModel, lead, lead);
		selectionModel.setValueIsAdjusting(false);
	}

	/**
	 * Restores the editing row after a model event/sort order change.
	 *
	 * @param editingRow
	 *            new index of the editingRow, in terms of the view
	 */
	private void restoreSortingEditingRow(int editingRow) {
		if (editingRow == -1) {
			// Editing row no longer being shown, cancel editing
			TableCellEditor editor = getCellEditor();
			if (editor != null) {
				// First try and cancel
				editor.cancelCellEditing();
				if (getCellEditor() != null) {
					// CellEditor didn't cede control, forcefully
					// remove it
					removeEditor();
				}
			}
		} else {
			// Repositioning handled in BasicTableUI
			this.editingRow = editingRow;
			repaint();
		}
	}

	/**
	 * Notifies the sorter of a change in the underlying model.
	 */
	private void notifySorter(ModelChange change) {
		try {
			ignoreSortChange = true;
			sorterChanged = false;
			switch (change.type) {
			case TableModelEvent.UPDATE:
				if (change.event.getLastRow() == Integer.MAX_VALUE) {
					sortManager.sorter.allRowsChanged();
				} else if (change.event.getColumn() == TableModelEvent.ALL_COLUMNS) {
					sortManager.sorter.rowsUpdated(change.startModelIndex, change.endModelIndex);
				} else {
					sortManager.sorter.rowsUpdated(change.startModelIndex, change.endModelIndex,
							change.event.getColumn());
				}
				break;
			case TableModelEvent.INSERT:
				sortManager.sorter.rowsInserted(change.startModelIndex, change.endModelIndex);
				break;
			case TableModelEvent.DELETE:
				sortManager.sorter.rowsDeleted(change.startModelIndex, change.endModelIndex);
				break;
			}
		} finally {
			ignoreSortChange = false;
		}
	}

	/**
	 * Converts a model index to view index. This is called when the sorter or
	 * model changes and sorting is enabled.
	 *
	 * @param change
	 *            describes the TableModelEvent that initiated the change; will
	 *            be null if called as the result of a sort
	 */
	private int convertRowIndexToView(int modelIndex, ModelChange change) {
		if (modelIndex < 0) {
			return -1;
		}
		if (change != null && modelIndex >= change.startModelIndex) {
			if (change.type == TableModelEvent.INSERT) {
				if (modelIndex + change.length >= change.modelRowCount) {
					return -1;
				}
				return sortManager.sorter.convertRowIndexToView(modelIndex + change.length);
			} else if (change.type == TableModelEvent.DELETE) {
				if (modelIndex <= change.endModelIndex) {
					// deleted
					return -1;
				} else {
					if (modelIndex - change.length >= change.modelRowCount) {
						return -1;
					}
					return sortManager.sorter.convertRowIndexToView(modelIndex - change.length);
				}
			}
			// else, updated
		}
		if (modelIndex >= getModel().getRowCount()) {
			return -1;
		}
		return sortManager.sorter.convertRowIndexToView(modelIndex);
	}

	/**
	 * Converts the selection to model coordinates. This is used when the model
	 * changes or the sorter changes.
	 */
	private int[] convertSelectionToModel(RowSorterEvent e) {
		int[] selection = getSelectedRows();
		for (int i = selection.length - 1; i >= 0; i--) {
			selection[i] = convertRowIndexToModel(e, selection[i]);
		}
		return selection;
	}

	private int convertRowIndexToModel(RowSorterEvent e, int viewIndex) {
		if (e != null) {
			if (e.getPreviousRowCount() == 0) {
				return viewIndex;
			}
			// range checking handled by RowSorterEvent
			return e.convertPreviousRowIndexToModel(viewIndex);
		}
		// Make sure the viewIndex is valid
		if (viewIndex < 0 || viewIndex >= getRowCount()) {
			return -1;
		}
		return convertRowIndexToModel(viewIndex);
	}

	//
	// Implementing TableModelListener interface
	//

	/**
	 * Invoked when this table's <code>TableModel</code> generates a
	 * <code>TableModelEvent</code>. The <code>TableModelEvent</code> should be
	 * constructed in the coordinate system of the model; the appropriate
	 * mapping to the view coordinate system is performed by this
	 * <code>JTable</code> when it receives the event.
	 * <p>
	 * Application code will not use these methods explicitly, they are used
	 * internally by <code>JTable</code>.
	 * <p>
	 * Note that as of 1.3, this method clears the selection, if any.
	 */
	@Override
	public void tableChanged(TableModelEvent e) {
		if (e == null || e.getFirstRow() == TableModelEvent.HEADER_ROW) {
			// The whole thing changed
			clearSelectionAndLeadAnchor();

			rowModel = null;

			if (sortManager != null) {
				try {
					ignoreSortChange = true;
					sortManager.sorter.modelStructureChanged();
				} finally {
					ignoreSortChange = false;
				}
				sortManager.allChanged();
			}

			if (getAutoCreateColumnsFromModel()) {
				// This will effect invalidation of the JTable and JTableHeader.
				createDefaultColumnsFromModel();
				return;
			}

			resizeAndRepaint();
			return;
		}

		if (sortManager != null) {
			sortedTableChanged(null, e);
			return;
		}

		// The totalRowHeight calculated below will be incorrect if
		// there are variable height rows. Repaint the visible region,
		// but don't return as a revalidate may be necessary as well.
		if (rowModel != null) {
			repaint();
		}

		if (e.getType() == TableModelEvent.INSERT) {
			tableRowsInserted(e);
			return;
		}

		if (e.getType() == TableModelEvent.DELETE) {
			tableRowsDeleted(e);
			return;
		}

		int modelColumn = e.getColumn();
		int start = e.getFirstRow();
		int end = e.getLastRow();

		Rectangle dirtyRegion;
		if (modelColumn == TableModelEvent.ALL_COLUMNS) {
			// 1 or more rows changed
			dirtyRegion = new Rectangle(0, start * getRowHeight(), getColumnModel().getTotalColumnWidth(), 0);
		} else {
			// A cell or column of cells has changed.
			// Unlike the rest of the methods in the JTable, the TableModelEvent
			// uses the coordinate system of the model instead of the view.
			// This is the only place in the JTable where this "reverse mapping"
			// is used.
			int column = convertColumnIndexToView(modelColumn);
			dirtyRegion = getCellRect(start, column, false);
		}

		// Now adjust the height of the dirty region according to the value of
		// "end".
		// Check for Integer.MAX_VALUE as this will cause an overflow.
		if (end != Integer.MAX_VALUE) {
			dirtyRegion.height = (end - start + 1) * getRowHeight();
			repaint(dirtyRegion.x, dirtyRegion.y, dirtyRegion.width, dirtyRegion.height);
		}
		// In fact, if the end is Integer.MAX_VALUE we need to revalidate anyway
		// because the scrollbar may need repainting.
		else {
			clearSelectionAndLeadAnchor();
			resizeAndRepaint();
			rowModel = null;
		}
	}

	/*
	 * Invoked when rows have been inserted into the table. <p> Application code
	 * will not use these methods explicitly, they are used internally by
	 * JTable.
	 *
	 * @param e the TableModelEvent encapsulating the insertion
	 */
	private void tableRowsInserted(TableModelEvent e) {
		int start = e.getFirstRow();
		int end = e.getLastRow();
		if (start < 0) {
			start = 0;
		}
		if (end < 0) {
			end = getRowCount() - 1;
		}

		// Adjust the selection to account for the new rows.
		int length = end - start + 1;
		selectionModel.insertIndexInterval(start, length, true);

		// If we have variable height rows, adjust the row model.
		if (rowModel != null) {
			rowModel.insertEntries(start, length, getRowHeight());
		}
		int rh = getRowHeight();
		Rectangle drawRect = new Rectangle(0, start * rh, getColumnModel().getTotalColumnWidth(),
				(getRowCount() - start) * rh);

		revalidate();
		// PENDING(milne) revalidate calls repaint() if parent is a ScrollPane
		// repaint still required in the unusual case where there is no
		// ScrollPane
		repaint(drawRect);
	}

	/*
	 * Invoked when rows have been removed from the table. <p> Application code
	 * will not use these methods explicitly, they are used internally by
	 * JTable.
	 *
	 * @param e the TableModelEvent encapsulating the deletion
	 */
	private void tableRowsDeleted(TableModelEvent e) {
		int start = e.getFirstRow();
		int end = e.getLastRow();
		if (start < 0) {
			start = 0;
		}
		if (end < 0) {
			end = getRowCount() - 1;
		}

		int deletedCount = end - start + 1;
		int previousRowCount = getRowCount() + deletedCount;
		// Adjust the selection to account for the new rows
		selectionModel.removeIndexInterval(start, end);

		// If we have variable height rows, adjust the row model.
		if (rowModel != null) {
			rowModel.removeEntries(start, deletedCount);
		}

		int rh = getRowHeight();
		Rectangle drawRect = new Rectangle(0, start * rh, getColumnModel().getTotalColumnWidth(),
				(previousRowCount - start) * rh);

		revalidate();
		// PENDING(milne) revalidate calls repaint() if parent is a ScrollPane
		// repaint still required in the unusual case where there is no
		// ScrollPane
		repaint(drawRect);
	}

	//
	// Implementing TableColumnModelListener interface
	//

	/**
	 * Invoked when a column is added to the table column model.
	 * <p>
	 * Application code will not use these methods explicitly, they are used
	 * internally by JTable.
	 *
	 * @see TableColumnModelListener
	 */
	@Override
	public void columnAdded(TableColumnModelEvent e) {
		// If I'm currently editing, then I should stop editing
		if (isEditing()) {
			removeEditor();
		}
		resizeAndRepaint();
	}

	/**
	 * Invoked when a column is removed from the table column model.
	 * <p>
	 * Application code will not use these methods explicitly, they are used
	 * internally by JTable.
	 *
	 * @see TableColumnModelListener
	 */
	@Override
	public void columnRemoved(TableColumnModelEvent e) {
		// If I'm currently editing, then I should stop editing
		if (isEditing()) {
			removeEditor();
		}
		resizeAndRepaint();
	}

	/**
	 * Invoked when a column is repositioned. If a cell is being edited, then
	 * editing is stopped and the cell is redrawn.
	 * <p>
	 * Application code will not use these methods explicitly, they are used
	 * internally by JTable.
	 *
	 * @param e
	 *            the event received
	 * @see TableColumnModelListener
	 */
	@Override
	public void columnMoved(TableColumnModelEvent e) {
		// If I'm currently editing, then I should stop editing
		if (isEditing()) {
			removeEditor();
		}
		repaint();
	}

	/**
	 * Invoked when a column is moved due to a margin change. If a cell is being
	 * edited, then editing is stopped and the cell is redrawn.
	 * <p>
	 * Application code will not use these methods explicitly, they are used
	 * internally by JTable.
	 *
	 * @param e
	 *            the event received
	 * @see TableColumnModelListener
	 */
	@Override
	public void columnMarginChanged(ChangeEvent e) {
		if (isEditing()) {
			removeEditor();
		}
		TableColumn resizingColumn = getResizingColumn();
		// Need to do this here, before the parent's
		// layout manager calls getPreferredSize().
		if (resizingColumn != null && autoResizeMode == AUTO_RESIZE_OFF) {
			resizingColumn.setPreferredWidth(resizingColumn.getWidth());
		}
		resizeAndRepaint();
	}

	private int limit(int i, int a, int b) {
		return Math.min(b, Math.max(i, a));
	}

	/**
	 * Invoked when the selection model of the <code>TableColumnModel</code> is
	 * changed.
	 * <p>
	 * Application code will not use these methods explicitly, they are used
	 * internally by JTable.
	 *
	 * @param e
	 *            the event received
	 * @see TableColumnModelListener
	 */
	@Override
	public void columnSelectionChanged(ListSelectionEvent e) {
		boolean isAdjusting = e.getValueIsAdjusting();
		if (columnSelectionAdjusting && !isAdjusting) {
			// The assumption is that when the model is no longer adjusting
			// we will have already gotten all the changes, and therefore
			// don't need to do an additional paint.
			columnSelectionAdjusting = false;
			return;
		}
		columnSelectionAdjusting = isAdjusting;
		// The getCellRect() call will fail unless there is at least one row.
		if (getRowCount() <= 0 || getColumnCount() <= 0) {
			return;
		}
		int firstIndex = limit(e.getFirstIndex(), 0, getColumnCount() - 1);
		int lastIndex = limit(e.getLastIndex(), 0, getColumnCount() - 1);
		int minRow = 0;
		int maxRow = getRowCount() - 1;
		if (getRowSelectionAllowed()) {
			minRow = selectionModel.getMinSelectionIndex();
			maxRow = selectionModel.getMaxSelectionIndex();
			int leadRow = getAdjustedIndex(selectionModel.getLeadSelectionIndex(), true);

			if (minRow == -1 || maxRow == -1) {
				if (leadRow == -1) {
					// nothing to repaint, return
					return;
				}

				// only thing to repaint is the lead
				minRow = maxRow = leadRow;
			} else {
				// We need to consider more than just the range between
				// the min and max selected index. The lead row, which could
				// be outside this range, should be considered also.
				if (leadRow != -1) {
					minRow = Math.min(minRow, leadRow);
					maxRow = Math.max(maxRow, leadRow);
				}
			}
		}
		Rectangle firstColumnRect = getCellRect(minRow, firstIndex, false);
		Rectangle lastColumnRect = getCellRect(maxRow, lastIndex, false);
		Rectangle dirtyRegion = firstColumnRect.union(lastColumnRect);
		repaint(dirtyRegion);
	}

	//
	// Implementing ListSelectionListener interface
	//

	/**
	 * Invoked when the row selection changes -- repaints to show the new
	 * selection.
	 * <p>
	 * Application code will not use these methods explicitly, they are used
	 * internally by JTable.
	 *
	 * @param e
	 *            the event received
	 * @see ListSelectionListener
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (sortManager != null) {
			sortManager.viewSelectionChanged(e);
		}
		boolean isAdjusting = e.getValueIsAdjusting();
		if (rowSelectionAdjusting && !isAdjusting) {
			// The assumption is that when the model is no longer adjusting
			// we will have already gotten all the changes, and therefore
			// don't need to do an additional paint.
			rowSelectionAdjusting = false;
			return;
		}
		rowSelectionAdjusting = isAdjusting;
		// The getCellRect() calls will fail unless there is at least one
		// column.
		if (getRowCount() <= 0 || getColumnCount() <= 0) {
			return;
		}
		int firstIndex = limit(e.getFirstIndex(), 0, getRowCount() - 1);
		int lastIndex = limit(e.getLastIndex(), 0, getRowCount() - 1);
		Rectangle firstRowRect = getCellRect(firstIndex, 0, false);
		Rectangle lastRowRect = getCellRect(lastIndex, getColumnCount() - 1, false);
		Rectangle dirtyRegion = firstRowRect.union(lastRowRect);
		repaint(dirtyRegion);
	}

	//
	// Implementing the CellEditorListener interface
	//

	/**
	 * Invoked when editing is finished. The changes are saved and the editor is
	 * discarded.
	 * <p>
	 * Application code will not use these methods explicitly, they are used
	 * internally by JTable.
	 *
	 * @param e
	 *            the event received
	 * @see CellEditorListener
	 */
	@Override
	public void editingStopped(ChangeEvent e) {
		// Take in the new value
		TableCellEditor editor = getCellEditor();
		if (editor != null) {
			Object value = editor.getCellEditorValue();
			setValueAt(value, editingRow, editingColumn);
			removeEditor();
		}
	}

	/**
	 * Invoked when editing is canceled. The editor object is discarded and the
	 * cell is rendered once again.
	 * <p>
	 * Application code will not use these methods explicitly, they are used
	 * internally by JTable.
	 *
	 * @param e
	 *            the event received
	 * @see CellEditorListener
	 */
	@Override
	public void editingCanceled(ChangeEvent e) {
		removeEditor();
	}

	//
	// Implementing the Scrollable interface
	//

	/**
	 * Sets the preferred size of the viewport for this table.
	 *
	 * @param size
	 *            a <code>Dimension</code> object specifying the
	 *            <code>preferredSize</code> of a <code>JViewport</code> whose
	 *            view is this table
	 * @see Scrollable#getPreferredScrollableViewportSize
	 * @beaninfo description: The preferred size of the viewport.
	 */
	public void setPreferredScrollableViewportSize(Dimension size) {
		preferredViewportSize = size;
	}

	/**
	 * Returns the preferred size of the viewport for this table.
	 *
	 * @return a <code>Dimension</code> object containing the
	 *         <code>preferredSize</code> of the <code>JViewport</code> which
	 *         displays this table
	 * @see Scrollable#getPreferredScrollableViewportSize
	 */
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return preferredViewportSize;
	}

	/**
	 * Returns the scroll increment (in pixels) that completely exposes one new
	 * row or column (depending on the orientation).
	 * <p>
	 * This method is called each time the user requests a unit scroll.
	 *
	 * @param visibleRect
	 *            the view area visible within the viewport
	 * @param orientation
	 *            either <code>SwingConstants.VERTICAL</code> or
	 *            <code>SwingConstants.HORIZONTAL</code>
	 * @param direction
	 *            less than zero to scroll up/left, greater than zero for
	 *            down/right
	 * @return the "unit" increment for scrolling in the specified direction
	 * @see Scrollable#getScrollableUnitIncrement
	 */
	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		int leadingRow;
		int leadingCol;
		Rectangle leadingCellRect;

		int leadingVisibleEdge;
		int leadingCellEdge;
		int leadingCellSize;

		leadingRow = getLeadingRow(visibleRect);
		leadingCol = getLeadingCol(visibleRect);
		if (orientation == SwingConstants.VERTICAL && leadingRow < 0) {
			// Couldn't find leading row - return some default value
			return getRowHeight();
		} else if (orientation == SwingConstants.HORIZONTAL && leadingCol < 0) {
			// Couldn't find leading col - return some default value
			return 100;
		}

		// Note that it's possible for one of leadingCol or leadingRow to be
		// -1, depending on the orientation. This is okay, as getCellRect()
		// still provides enough information to calculate the unit increment.
		leadingCellRect = getCellRect(leadingRow, leadingCol, true);
		leadingVisibleEdge = leadingEdge(visibleRect, orientation);
		leadingCellEdge = leadingEdge(leadingCellRect, orientation);

		if (orientation == SwingConstants.VERTICAL) {
			leadingCellSize = leadingCellRect.height;

		} else {
			leadingCellSize = leadingCellRect.width;
		}

		// 4 cases:
		// #1: Leading cell fully visible, reveal next cell
		// #2: Leading cell fully visible, hide leading cell
		// #3: Leading cell partially visible, hide rest of leading cell
		// #4: Leading cell partially visible, reveal rest of leading cell

		if (leadingVisibleEdge == leadingCellEdge) { // Leading cell is fully
														// visible
			// Case #1: Reveal previous cell
			if (direction < 0) {
				int retVal = 0;

				if (orientation == SwingConstants.VERTICAL) {
					// Loop past any zero-height rows
					while (--leadingRow >= 0) {
						retVal = getRowHeight(leadingRow);
						if (retVal != 0) {
							break;
						}
					}
				} else { // HORIZONTAL
							// Loop past any zero-width cols
					while (--leadingCol >= 0) {
						retVal = getCellRect(leadingRow, leadingCol, true).width;
						if (retVal != 0) {
							break;
						}
					}
				}
				return retVal;
			} else { // Case #2: hide leading cell
				return leadingCellSize;
			}
		} else { // Leading cell is partially hidden
					// Compute visible, hidden portions
			int hiddenAmt = Math.abs(leadingVisibleEdge - leadingCellEdge);
			int visibleAmt = leadingCellSize - hiddenAmt;

			if (direction > 0) {
				// Case #3: hide showing portion of leading cell
				return visibleAmt;
			} else { // Case #4: reveal hidden portion of leading cell
				return hiddenAmt;
			}
		}
	}

	/**
	 * Returns <code>visibleRect.height</code> or
	 * <code>visibleRect.width</code>, depending on this table's orientation.
	 * Note that as of Swing 1.1.1 (Java 2 v 1.2.2) the value returned will
	 * ensure that the viewport is cleanly aligned on a row boundary.
	 *
	 * @return <code>visibleRect.height</code> or <code>visibleRect.width</code>
	 *         per the orientation
	 * @see Scrollable#getScrollableBlockIncrement
	 */
	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {

		if (getRowCount() == 0) {
			// Short-circuit empty table model
			if (SwingConstants.VERTICAL == orientation) {
				int rh = getRowHeight();
				return (rh > 0) ? Math.max(rh, (visibleRect.height / rh) * rh) : visibleRect.height;
			} else {
				return visibleRect.width;
			}
		}
		// Shortcut for vertical scrolling of a table w/ uniform row height
		if (null == rowModel && SwingConstants.VERTICAL == orientation) {
			int row = rowAtPoint(visibleRect.getLocation());
			// assert row != -1;
			int col = columnAtPoint(visibleRect.getLocation());
			Rectangle cellRect = getCellRect(row, col, true);

			if (cellRect.y == visibleRect.y) {
				int rh = getRowHeight();
				// assert rh > 0;
				return Math.max(rh, (visibleRect.height / rh) * rh);
			}
		}
		if (direction < 0) {
			return getPreviousBlockIncrement(visibleRect, orientation);
		} else {
			return getNextBlockIncrement(visibleRect, orientation);
		}
	}

	/**
	 * Called to get the block increment for upward scrolling in cases of
	 * horizontal scrolling, or for vertical scrolling of a table with variable
	 * row heights.
	 */
	private int getPreviousBlockIncrement(Rectangle visibleRect, int orientation) {
		// Measure back from visible leading edge
		// If we hit the cell on its leading edge, it becomes the leading cell.
		// Else, use following cell

		int row;
		int col;

		int newEdge;
		Point newCellLoc;

		int visibleLeadingEdge = leadingEdge(visibleRect, orientation);
		boolean leftToRight = getComponentOrientation().isLeftToRight();
		int newLeadingEdge;

		// Roughly determine the new leading edge by measuring back from the
		// leading visible edge by the size of the visible rect, and find the
		// cell there.
		if (orientation == SwingConstants.VERTICAL) {
			newEdge = visibleLeadingEdge - visibleRect.height;
			int x = visibleRect.x + (leftToRight ? 0 : visibleRect.width);
			newCellLoc = new Point(x, newEdge);
		} else if (leftToRight) {
			newEdge = visibleLeadingEdge - visibleRect.width;
			newCellLoc = new Point(newEdge, visibleRect.y);
		} else { // Horizontal, right-to-left
			newEdge = visibleLeadingEdge + visibleRect.width;
			newCellLoc = new Point(newEdge - 1, visibleRect.y);
		}
		row = rowAtPoint(newCellLoc);
		col = columnAtPoint(newCellLoc);

		// If we're measuring past the beginning of the table, we get an invalid
		// cell. Just go to the beginning of the table in this case.
		if (orientation == SwingConstants.VERTICAL & row < 0) {
			newLeadingEdge = 0;
		} else if (orientation == SwingConstants.HORIZONTAL & col < 0) {
			if (leftToRight) {
				newLeadingEdge = 0;
			} else {
				newLeadingEdge = getWidth();
			}
		} else {
			// Refine our measurement
			Rectangle newCellRect = getCellRect(row, col, true);
			int newCellLeadingEdge = leadingEdge(newCellRect, orientation);
			int newCellTrailingEdge = trailingEdge(newCellRect, orientation);

			// Usually, we hit in the middle of newCell, and want to scroll to
			// the beginning of the cell after newCell. But there are a
			// couple corner cases where we want to scroll to the beginning of
			// newCell itself. These cases are:
			// 1) newCell is so large that it ends at or extends into the
			// visibleRect (newCell is the leading cell, or is adjacent to
			// the leading cell)
			// 2) newEdge happens to fall right on the beginning of a cell

			// Case 1
			if ((orientation == SwingConstants.VERTICAL || leftToRight)
					&& (newCellTrailingEdge >= visibleLeadingEdge)) {
				newLeadingEdge = newCellLeadingEdge;
			} else if (orientation == SwingConstants.HORIZONTAL && !leftToRight
					&& newCellTrailingEdge <= visibleLeadingEdge) {
				newLeadingEdge = newCellLeadingEdge;
			}
			// Case 2:
			else if (newEdge == newCellLeadingEdge) {
				newLeadingEdge = newCellLeadingEdge;
			}
			// Common case: scroll to cell after newCell
			else {
				newLeadingEdge = newCellTrailingEdge;
			}
		}
		return Math.abs(visibleLeadingEdge - newLeadingEdge);
	}

	/**
	 * Called to get the block increment for downward scrolling in cases of
	 * horizontal scrolling, or for vertical scrolling of a table with variable
	 * row heights.
	 */
	private int getNextBlockIncrement(Rectangle visibleRect, int orientation) {
		// Find the cell at the trailing edge. Return the distance to put
		// that cell at the leading edge.
		int trailingRow = getTrailingRow(visibleRect);
		int trailingCol = getTrailingCol(visibleRect);

		Rectangle cellRect;
		boolean cellFillsVis;

		int cellLeadingEdge;
		int cellTrailingEdge;
		int newLeadingEdge;
		int visibleLeadingEdge = leadingEdge(visibleRect, orientation);

		// If we couldn't find trailing cell, just return the size of the
		// visibleRect. Note that, for instance, we don't need the
		// trailingCol to proceed if we're scrolling vertically, because
		// cellRect will still fill in the required dimensions. This would
		// happen if we're scrolling vertically, and the table is not wide
		// enough to fill the visibleRect.
		if (orientation == SwingConstants.VERTICAL && trailingRow < 0) {
			return visibleRect.height;
		} else if (orientation == SwingConstants.HORIZONTAL && trailingCol < 0) {
			return visibleRect.width;
		}
		cellRect = getCellRect(trailingRow, trailingCol, true);
		cellLeadingEdge = leadingEdge(cellRect, orientation);
		cellTrailingEdge = trailingEdge(cellRect, orientation);

		if (orientation == SwingConstants.VERTICAL || getComponentOrientation().isLeftToRight()) {
			cellFillsVis = cellLeadingEdge <= visibleLeadingEdge;
		} else { // Horizontal, right-to-left
			cellFillsVis = cellLeadingEdge >= visibleLeadingEdge;
		}

		if (cellFillsVis) {
			// The visibleRect contains a single large cell. Scroll to the end
			// of this cell, so the following cell is the first cell.
			newLeadingEdge = cellTrailingEdge;
		} else if (cellTrailingEdge == trailingEdge(visibleRect, orientation)) {
			// The trailing cell happens to end right at the end of the
			// visibleRect. Again, scroll to the beginning of the next cell.
			newLeadingEdge = cellTrailingEdge;
		} else {
			// Common case: the trailing cell is partially visible, and isn't
			// big enough to take up the entire visibleRect. Scroll so it
			// becomes the leading cell.
			newLeadingEdge = cellLeadingEdge;
		}
		return Math.abs(newLeadingEdge - visibleLeadingEdge);
	}

	/*
	 * Return the row at the top of the visibleRect
	 *
	 * May return -1
	 */
	private int getLeadingRow(Rectangle visibleRect) {
		Point leadingPoint;

		if (getComponentOrientation().isLeftToRight()) {
			leadingPoint = new Point(visibleRect.x, visibleRect.y);
		} else {
			leadingPoint = new Point(visibleRect.x + visibleRect.width - 1, visibleRect.y);
		}
		return rowAtPoint(leadingPoint);
	}

	/*
	 * Return the column at the leading edge of the visibleRect.
	 *
	 * May return -1
	 */
	private int getLeadingCol(Rectangle visibleRect) {
		Point leadingPoint;

		if (getComponentOrientation().isLeftToRight()) {
			leadingPoint = new Point(visibleRect.x, visibleRect.y);
		} else {
			leadingPoint = new Point(visibleRect.x + visibleRect.width - 1, visibleRect.y);
		}
		return columnAtPoint(leadingPoint);
	}

	/*
	 * Return the row at the bottom of the visibleRect.
	 *
	 * May return -1
	 */
	private int getTrailingRow(Rectangle visibleRect) {
		Point trailingPoint;

		if (getComponentOrientation().isLeftToRight()) {
			trailingPoint = new Point(visibleRect.x, visibleRect.y + visibleRect.height - 1);
		} else {
			trailingPoint = new Point(visibleRect.x + visibleRect.width - 1, visibleRect.y + visibleRect.height - 1);
		}
		return rowAtPoint(trailingPoint);
	}

	/*
	 * Return the column at the trailing edge of the visibleRect.
	 *
	 * May return -1
	 */
	private int getTrailingCol(Rectangle visibleRect) {
		Point trailingPoint;

		if (getComponentOrientation().isLeftToRight()) {
			trailingPoint = new Point(visibleRect.x + visibleRect.width - 1, visibleRect.y);
		} else {
			trailingPoint = new Point(visibleRect.x, visibleRect.y);
		}
		return columnAtPoint(trailingPoint);
	}

	/*
	 * Returns the leading edge ("beginning") of the given Rectangle. For
	 * VERTICAL, this is the top, for left-to-right, the left side, and for
	 * right-to-left, the right side.
	 */
	private int leadingEdge(Rectangle rect, int orientation) {
		if (orientation == SwingConstants.VERTICAL) {
			return rect.y;
		} else if (getComponentOrientation().isLeftToRight()) {
			return rect.x;
		} else { // Horizontal, right-to-left
			return rect.x + rect.width;
		}
	}

	/*
	 * Returns the trailing edge ("end") of the given Rectangle. For VERTICAL,
	 * this is the bottom, for left-to-right, the right side, and for
	 * right-to-left, the left side.
	 */
	private int trailingEdge(Rectangle rect, int orientation) {
		if (orientation == SwingConstants.VERTICAL) {
			return rect.y + rect.height;
		} else if (getComponentOrientation().isLeftToRight()) {
			return rect.x + rect.width;
		} else { // Horizontal, right-to-left
			return rect.x;
		}
	}

	/**
	 * Returns false if <code>autoResizeMode</code> is set to
	 * <code>AUTO_RESIZE_OFF</code>, which indicates that the width of the
	 * viewport does not determine the width of the table. Otherwise returns
	 * true.
	 *
	 * @return false if <code>autoResizeMode</code> is set to
	 *         <code>AUTO_RESIZE_OFF</code>, otherwise returns true
	 * @see Scrollable#getScrollableTracksViewportWidth
	 */
	@Override
	public boolean getScrollableTracksViewportWidth() {
		return !(autoResizeMode == AUTO_RESIZE_OFF);
	}

	/**
	 * Returns {@code false} to indicate that the height of the viewport does
	 * not determine the height of the table, unless
	 * {@code getFillsViewportHeight} is {@code true} and the preferred height
	 * of the table is smaller than the viewport's height.
	 *
	 * @return {@code false} unless {@code getFillsViewportHeight} is
	 *         {@code true} and the table needs to be stretched to fill the
	 *         viewport
	 * @see Scrollable#getScrollableTracksViewportHeight
	 * @see #setFillsViewportHeight
	 * @see #getFillsViewportHeight
	 */
	@Override
	public boolean getScrollableTracksViewportHeight() {
		return getFillsViewportHeight() && getParent() instanceof JViewport
				&& (((JViewport) getParent()).getHeight() > getPreferredSize().height);
	}

	/**
	 * Sets whether or not this table is always made large enough to fill the
	 * height of an enclosing viewport. If the preferred height of the table is
	 * smaller than the viewport, then the table will be stretched to fill the
	 * viewport. In other words, this ensures the table is never smaller than
	 * the viewport. The default for this property is {@code false}.
	 *
	 * @param fillsViewportHeight
	 *            whether or not this table is always made large enough to fill
	 *            the height of an enclosing viewport
	 * @see #getFillsViewportHeight
	 * @see #getScrollableTracksViewportHeight
	 * @since 1.6
	 * @beaninfo bound: true description: Whether or not this table is always
	 *           made large enough to fill the height of an enclosing viewport
	 */
	public void setFillsViewportHeight(boolean fillsViewportHeight) {
		boolean old = this.fillsViewportHeight;
		this.fillsViewportHeight = fillsViewportHeight;
		resizeAndRepaint();
		firePropertyChange("fillsViewportHeight", old, fillsViewportHeight);
	}

	/**
	 * Returns whether or not this table is always made large enough to fill the
	 * height of an enclosing viewport.
	 *
	 * @return whether or not this table is always made large enough to fill the
	 *         height of an enclosing viewport
	 * @see #setFillsViewportHeight
	 * @since 1.6
	 */
	public boolean getFillsViewportHeight() {
		return fillsViewportHeight;
	}

	//
	// Protected Methods
	//

	@Override
	protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
		boolean retValue = super.processKeyBinding(ks, e, condition, pressed);

		// Start editing when a key is typed. UI classes can disable this
		// behavior
		// by setting the client property JTable.autoStartsEdit to
		// Boolean.FALSE.
		if (!retValue && condition == WHEN_ANCESTOR_OF_FOCUSED_COMPONENT && isFocusOwner()
				&& !Boolean.FALSE.equals((Boolean) getClientProperty("JTable.autoStartsEdit"))) {
			// We do not have a binding for the event.
			Component editorComponent = getEditorComponent();
			if (editorComponent == null) {
				// Only attempt to install the editor on a KEY_PRESSED,
				if (e == null || e.getID() != KeyEvent.KEY_PRESSED) {
					return false;
				}
				// Don't start when just a modifier is pressed
				int code = e.getKeyCode();
				if (code == KeyEvent.VK_SHIFT || code == KeyEvent.VK_CONTROL || code == KeyEvent.VK_ALT) {
					return false;
				}
				// Try to install the editor
				int leadRow = getSelectionModel().getLeadSelectionIndex();
				int leadColumn = getColumnModel().getSelectionModel().getLeadSelectionIndex();
				if (leadRow != -1 && leadColumn != -1 && !isEditing()) {
					if (!editCellAt(leadRow, leadColumn, e)) {
						return false;
					}
				}
				editorComponent = getEditorComponent();
				if (editorComponent == null) {
					return false;
				}
			}
			// If the editorComponent is a JComponent, pass the event to it.
			if (editorComponent instanceof JComponent) {
				retValue = ((JComponent) editorComponent).processKeyBinding(ks, e, WHEN_FOCUSED, pressed);
				// If we have started an editor as a result of the user
				// pressing a key and the surrendersFocusOnKeystroke property
				// is true, give the focus to the new editor.
				if (getSurrendersFocusOnKeystroke()) {
					editorComponent.requestFocus();
				}
			}
		}
		return retValue;
	}

	private void setLazyValue(Hashtable h, Class c, String s) {
		h.put(c, new SwingLazyValue(s));
	}

	private void setLazyRenderer(Class c, String s) {
		setLazyValue(defaultRenderersByColumnClass, c, s);
	}

	/**
	 * Creates default cell renderers for objects, numbers, doubles, dates,
	 * booleans, and icons.
	 * 
	 * @see javax.swing.table.DefaultTableCellRenderer
	 *
	 */
	protected void createDefaultRenderers() {
		defaultRenderersByColumnClass = new UIDefaults(8, 0.75f);

		// Objects
		setLazyRenderer(Object.class, "javax.swing.table.DefaultTableCellRenderer$UIResource");
		setLazyRenderer(String.class, "javax.swing.table.DefaultTableCellRenderer$UIResource");

		// Numbers
		setLazyRenderer(Number.class, "javax.swing.JTable$NumberRenderer");

		// Doubles and Floats
		setLazyRenderer(Float.class, "javax.swing.JTable$DoubleRenderer");
		setLazyRenderer(Double.class, "javax.swing.JTable$DoubleRenderer");

		// Dates
		setLazyRenderer(Date.class, "javax.swing.JTable$DateRenderer");

		// Icons and ImageIcons
		setLazyRenderer(Icon.class, "javax.swing.JTable$IconRenderer");
		setLazyRenderer(ImageIcon.class, "javax.swing.JTable$IconRenderer");

		// Booleans
		setLazyRenderer(Boolean.class, "javax.swing.JTable$BooleanRenderer");
	}

	/**
	 * Default Renderers
	 **/
	static class NumberRenderer extends DefaultTableCellRenderer.UIResource {
		public NumberRenderer() {
			super();
			setHorizontalAlignment(JLabel.RIGHT);
		}
	}

	static class DoubleRenderer extends NumberRenderer {
		NumberFormat formatter;

		public DoubleRenderer() {
			super();
		}

		@Override
		public void setValue(Object value) {
			if (formatter == null) {
				formatter = NumberFormat.getInstance();
			}
			setText((value == null) ? "" : formatter.format(value));
		}
	}

	static class DateRenderer extends DefaultTableCellRenderer.UIResource {
		DateFormat formatter;

		public DateRenderer() {
			super();
		}

		public void setValue(Object value) {
			if (formatter == null) {
				formatter = DateFormat.getDateInstance();
			}
			setText((value == null) ? "" : formatter.format(value));
		}
	}

	static class IconRenderer extends DefaultTableCellRenderer.UIResource {
		public IconRenderer() {
			super();
			setHorizontalAlignment(JLabel.CENTER);
		}

		@Override
		public void setValue(Object value) {
			setIcon((value instanceof Icon) ? (Icon) value : null);
		}
	}

	public static class BooleanRenderer extends JCheckBox implements TableCellRenderer, UIResource {
		private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

		public BooleanRenderer() {
			super();
			setHorizontalAlignment(JLabel.CENTER);
			setBorderPainted(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(table.getBackground());
			}
			setSelected((value != null && ((Boolean) value).booleanValue()));

			if (hasFocus) {
				setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
			} else {
				setBorder(noFocusBorder);
			}

			return this;
		}
		
		public Component getComponent() {
			return this;
		}


	}

	private void setLazyEditor(Class c, String s) {
		setLazyValue(defaultEditorsByColumnClass, c, s);
	}

	/**
	 * Creates default cell editors for objects, numbers, and boolean values.
	 * 
	 * @see DefaultCellEditor
	 */
	protected void createDefaultEditors() {
		defaultEditorsByColumnClass = new UIDefaults(3, 0.75f);

		// Objects
		setLazyEditor(Object.class, "javax.swing.JTable$GenericEditor");

		// Numbers
		setLazyEditor(Number.class, "javax.swing.JTable$NumberEditor");

		// Booleans
		setLazyEditor(Boolean.class, "javax.swing.JTable$BooleanEditor");
	}

	/**
	 * Default Editors
	 */
	static class GenericEditor extends DefaultCellEditor {

		Class[] argTypes = new Class[] { String.class };
		Class constructorClass;
		Object value;
		private Constructor constructor;

		public GenericEditor() {
			super(new JTextField());
			getComponent().setName("Table.editor");
		}

		@Override
		public boolean stopCellEditing() {
			String s = (String) super.getCellEditorValue();
			// Here we are dealing with the case where a user
			// has deleted the string value in a cell, possibly
			// after a failed validation. Return null, so that
			// they have the option to replace the value with
			// null or use escape to restore the original.
			// For Strings, return "" for backward compatibility.
			try {
				if ("".equals(s) || constructorClass == String.class) {
					if (constructorClass == String.class) {
						value = s;
					}
				} else {
					value = constructor.newInstance(new Object[] { s });
				}
				return super.stopCellEditing();
			} catch (Exception e) {
				((JComponent) getComponent()).setBorder(new LineBorder(Color.red));
				return false;
			}
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			this.value = null;
			((JComponent) getComponent()).setBorder(new LineBorder(Color.black));
			try {
				Class type = table.getColumnClass(column);
				// Since our obligation is to produce a value which is
				// assignable for the required type it is OK to use the
				// String constructor for columns which are declared
				// to contain Objects. A String is an Object.
				if (type == Object.class) {
					type = String.class;
				}
				// ReflectUtil.checkPackageAccess(type);
				// SwingUtilities2.checkAccess(type.getModifiers());
				constructor = type.getConstructor(argTypes);
				constructorClass = type;// .getConstructor(argTypes);
			} catch (Exception e) {
				return null;
			}
			return super.getTableCellEditorComponent(table, value, isSelected, row, column);
		}

		@Override
		public Object getCellEditorValue() {
			return value;
		}
	}

	static class NumberEditor extends GenericEditor {

		public NumberEditor() {
			((JTextField) getComponent()).setHorizontalAlignment(JTextField.RIGHT);
		}
	}

	static class BooleanEditor extends DefaultCellEditor {
		public BooleanEditor() {
			super(new JCheckBox());
			JCheckBox checkBox = (JCheckBox) getComponent();
			checkBox.setHorizontalAlignment(JCheckBox.CENTER);
		}
	}

	/**
	 * Initializes table properties to their default values.
	 */
	protected void initializeLocalVars() {
		updateSelectionOnSort = true;
		setOpaque(true);
		createDefaultRenderers();
		createDefaultEditors();

		setTableHeader(createDefaultTableHeader());

		setShowGrid(true);
		setAutoResizeMode(AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		setRowHeight(16);
		isRowHeightSet = false;
		setRowMargin(1);
		setRowSelectionAllowed(true);
		setCellEditor(null);
		setEditingColumn(-1);
		setEditingRow(-1);
		setSurrendersFocusOnKeystroke(false);
		setPreferredScrollableViewportSize(new Dimension(450, 400));

		// I'm registered to do tool tips so we can draw tips for the renderers
		// ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
		// toolTipManager.registerComponent(this);

		setAutoscrolls(true);
	}

	/**
	 * Returns the default table model object, which is a
	 * <code>DefaultTableModel</code>. A subclass can override this method to
	 * return a different table model object.
	 *
	 * @return the default table model object
	 * @see javax.swing.table.DefaultTableModel
	 */
	protected TableModel createDefaultDataModel() {
		return new DefaultTableModel();
	}

	/**
	 * Returns the default column model object, which is a
	 * <code>DefaultTableColumnModel</code>. A subclass can override this method
	 * to return a different column model object.
	 *
	 * @return the default column model object
	 * @see javax.swing.table.DefaultTableColumnModel
	 */
	protected TableColumnModel createDefaultColumnModel() {
		return new DefaultTableColumnModel();
	}

	/**
	 * Returns the default selection model object, which is a
	 * <code>DefaultListSelectionModel</code>. A subclass can override this
	 * method to return a different selection model object.
	 *
	 * @return the default selection model object
	 * @see javax.swing.DefaultListSelectionModel
	 */
	protected ListSelectionModel createDefaultSelectionModel() {
		return new DefaultListSelectionModel();
	}

	/**
	 * Returns the default table header object, which is a
	 * <code>JTableHeader</code>. A subclass can override this method to return
	 * a different table header object.
	 *
	 * @return the default table header object
	 * @see javax.swing.table.JTableHeader
	 */
	protected JTableHeader createDefaultTableHeader() {
		return new JTableHeader(columnModel);
	}

	/**
	 * Equivalent to <code>revalidate</code> followed by <code>repaint</code>.
	 */
	protected void resizeAndRepaint() {
		revalidate();
		repaint();
	}

	/**
	 * Returns the active cell editor, which is {@code null} if the table is not
	 * currently editing.
	 *
	 * @return the {@code TableCellEditor} that does the editing, or
	 *         {@code null} if the table is not currently editing.
	 * @see #cellEditor
	 * @see #getCellEditor(int, int)
	 */
	public TableCellEditor getCellEditor() {
		return cellEditor;
	}

	/**
	 * Sets the active cell editor.
	 *
	 * @param anEditor
	 *            the active cell editor
	 * @see #cellEditor
	 * @beaninfo bound: true description: The table's active cell editor.
	 */
	public void setCellEditor(TableCellEditor anEditor) {
		TableCellEditor oldEditor = cellEditor;
		cellEditor = anEditor;
		firePropertyChange("tableCellEditor", oldEditor, anEditor);
	}

	/**
	 * Sets the <code>editingColumn</code> variable.
	 * 
	 * @param aColumn
	 *            the column of the cell to be edited
	 *
	 * @see #editingColumn
	 */
	public void setEditingColumn(int aColumn) {
		editingColumn = aColumn;
	}

	/**
	 * Sets the <code>editingRow</code> variable.
	 * 
	 * @param aRow
	 *            the row of the cell to be edited
	 *
	 * @see #editingRow
	 */
	public void setEditingRow(int aRow) {
		editingRow = aRow;
	}

	/**
	 * Returns an appropriate renderer for the cell specified by this row and
	 * column. If the <code>TableColumn</code> for this column has a non-null
	 * renderer, returns that. If not, finds the class of the data in this
	 * column (using <code>getColumnClass</code>) and returns the default
	 * renderer for this type of data.
	 * <p>
	 * <b>Note:</b> Throughout the table package, the internal implementations
	 * always use this method to provide renderers so that this default behavior
	 * can be safely overridden by a subclass.
	 *
	 * @param row
	 *            the row of the cell to render, where 0 is the first row
	 * @param column
	 *            the column of the cell to render, where 0 is the first column
	 * @return the assigned renderer; if <code>null</code> returns the default
	 *         renderer for this type of object
	 * @see javax.swing.table.DefaultTableCellRenderer
	 * @see javax.swing.table.TableColumn#setCellRenderer
	 * @see #setDefaultRenderer
	 */
	public TableCellRenderer getCellRenderer(int row, int column) {
		TableColumn tableColumn = getColumnModel().getColumn(column);
		TableCellRenderer renderer = tableColumn.getCellRenderer();
		if (renderer == null) {
			renderer = getDefaultRenderer(getColumnClass(column));
		}
		return renderer;
	}

	/**
	 * Prepares the renderer by querying the data model for the value and
	 * selection state of the cell at <code>row</code>, <code>column</code>.
	 * Returns the component (may be a <code>Component</code> or a
	 * <code>JComponent</code>) under the event location.
	 * <p>
	 * During a printing operation, this method will configure the renderer
	 * without indicating selection or focus, to prevent them from appearing in
	 * the printed output. To do other customizations based on whether or not
	 * the table is being printed, you can check the value of
	 * {@link javax.swing.JComponent#isPaintingForPrint()}, either here or
	 * within custom renderers.
	 * <p>
	 * <b>Note:</b> Throughout the table package, the internal implementations
	 * always use this method to prepare renderers so that this default behavior
	 * can be safely overridden by a subclass.
	 *
	 * @param renderer
	 *            the <code>TableCellRenderer</code> to prepare
	 * @param row
	 *            the row of the cell to render, where 0 is the first row
	 * @param column
	 *            the column of the cell to render, where 0 is the first column
	 * @return the <code>Component</code> under the event location
	 */
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		Object value = getValueAt(row, column);

		boolean isSelected = false;
		boolean hasFocus = false;

		// Only indicate the selection and focused cell if not printing
		if (!isPaintingForPrint()) {
			isSelected = isCellSelected(row, column);

			boolean rowIsLead = (selectionModel.getLeadSelectionIndex() == row);
			boolean colIsLead = (columnModel.getSelectionModel().getLeadSelectionIndex() == column);

			hasFocus = (rowIsLead && colIsLead) && isFocusOwner();
		}

		return renderer.getTableCellRendererComponent(this, value, isSelected, hasFocus, row, column);
	}

	/**
	 * Returns an appropriate editor for the cell specified by <code>row</code>
	 * and <code>column</code>. If the <code>TableColumn</code> for this column
	 * has a non-null editor, returns that. If not, finds the class of the data
	 * in this column (using <code>getColumnClass</code>) and returns the
	 * default editor for this type of data.
	 * <p>
	 * <b>Note:</b> Throughout the table package, the internal implementations
	 * always use this method to provide editors so that this default behavior
	 * can be safely overridden by a subclass.
	 *
	 * @param row
	 *            the row of the cell to edit, where 0 is the first row
	 * @param column
	 *            the column of the cell to edit, where 0 is the first column
	 * @return the editor for this cell; if <code>null</code> return the default
	 *         editor for this type of cell
	 * @see DefaultCellEditor
	 */
	public TableCellEditor getCellEditor(int row, int column) {
		TableColumn tableColumn = getColumnModel().getColumn(column);
		TableCellEditor editor = tableColumn.getCellEditor();
		if (editor == null) {
			editor = getDefaultEditor(getColumnClass(column));
		}
		return editor;
	}

	/**
	 * Prepares the editor by querying the data model for the value and
	 * selection state of the cell at <code>row</code>, <code>column</code>.
	 * <p>
	 * <b>Note:</b> Throughout the table package, the internal implementations
	 * always use this method to prepare editors so that this default behavior
	 * can be safely overridden by a subclass.
	 *
	 * @param editor
	 *            the <code>TableCellEditor</code> to set up
	 * @param row
	 *            the row of the cell to edit, where 0 is the first row
	 * @param column
	 *            the column of the cell to edit, where 0 is the first column
	 * @return the <code>Component</code> being edited
	 */
	public Component prepareEditor(TableCellEditor editor, int row, int column) {
		Object value = getValueAt(row, column);
		boolean isSelected = isCellSelected(row, column);
		JComponent comp = (JComponent) editor.getTableCellEditorComponent(this, value, isSelected, row, column);
		// if (comp instanceof JComponent) {
		// JComponent jComp = (JComponent)comp;
		// if (jComp.getNextFocusableComponent() == null) {
		// jComp.setNextFocusableComponent(this);
		// }
		// }
		((JSComponentUI) comp.getUI()).setRenderer(comp, 0, 0);
		return comp;
	}

	/**
	 * Discards the editor object and frees the real estate it used for cell
	 * rendering.
	 */
	public void removeEditor() {
		// KeyboardFocusManager.getCurrentKeyboardFocusManager().
		// removePropertyChangeListener("permanentFocusOwner", editorRemover);
		editorRemover = null;

		TableCellEditor editor = getCellEditor();
		if (editor != null) {
			editor.removeCellEditorListener(this);
			if (editorComp != null) {
				// Component focusOwner =
				// KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
				// boolean isFocusOwnerInTheTable = focusOwner != null?
				// SwingUtilities.isDescendingFrom(focusOwner, this):false;
				remove(editorComp);
				// if(isFocusOwnerInTheTable) {
				// requestFocusInWindow();
				// }
			}

			Rectangle cellRect = getCellRect(editingRow, editingColumn, false);

			setCellEditor(null);
			setEditingColumn(-1);
			setEditingRow(-1);
			editorComp = null;

			repaint(cellRect);
		}
	}

	
} // End of Class JTable
