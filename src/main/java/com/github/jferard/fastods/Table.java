/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
*    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.github.jferard.fastods;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.github.jferard.fastods.TableCell.Type;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file Table.java is part of FastODS.
 *
 *         SimpleOds 0.5.1 Changed all 'throw Exception' to 'throw
 *         FastOdsException'
 *
 *         WHERE ?
 *         content.xml/office:document-content/office:body/office:spreadsheet/
 *         table:table
 */
public class Table implements NamedObject {
	private static void appendColumnStyle(final Appendable appendable,
			final Util util, final String sSytle,
			final String sDefaultCellSytle, final int nCount)
			throws IOException {
		appendable.append("<table:table-column ");
		util.appendAttribute(appendable, "table:style-name", sSytle);
		util.appendAttribute(appendable, "table:number-columns-repeated",
				nCount);
		util.appendAttribute(appendable, "table:default-cell-style-name",
				sDefaultCellSytle);
		appendable.append("/>");
	}

	private final ConfigItem activeSplitRange;
	private final ConfigItem cursorPositionX;

	private final ConfigItem cursorPositionY;
	private final ConfigItem horizontalSplitMode;
	private final ConfigItem horizontalSplitPosition;
	private int nLastCol; // The highest column in the table TODO: Check if
							// this can be removed
	private final OdsFile odsFile;
	private final ConfigItem pageViewZoomValue;
	private final ConfigItem positionBottom;
	private final ConfigItem positionLeft;
	private final ConfigItem positionRight;
	private final ConfigItem positionTop;
	private final List<TableColumnStyle> qColumnStyles;
	private final List<TableRow> qTableRows;
	private String sName;
	private String styleName;

	private final ConfigItem verticalSplitMode;
	private final ConfigItem verticalSplitPosition;
	private final ConfigItem zoomType;

	private final ConfigItem zoomValue;

	Table(final OdsFile odsFile, final String sName) {
		this.odsFile = odsFile;
		this.sName = sName;
		this.styleName = "ta1";
		this.nLastCol = 0; // The highest column in the table TODO: Check if
							// this can be removed

		this.cursorPositionX = new ConfigItem("CursorPositionX", "int", "0");
		this.cursorPositionY = new ConfigItem("cursorPositionY", "int", "0");
		this.horizontalSplitMode = new ConfigItem("horizontalSplitMode",
				"short", "0");
		this.verticalSplitMode = new ConfigItem("verticalSplitMode", "short",
				"0");
		this.horizontalSplitPosition = new ConfigItem("horizontalSplitPosition",
				"int", "0");
		this.verticalSplitPosition = new ConfigItem("verticalSplitPosition",
				"int", "0");
		this.activeSplitRange = new ConfigItem("activeSplitRange", "short",
				"2");
		this.positionLeft = new ConfigItem("positionLeft", "int", "0");
		this.positionRight = new ConfigItem("PositionRight", "int", "0");
		this.positionTop = new ConfigItem("PositionTop", "int", "0");
		this.positionBottom = new ConfigItem("positionBottom", "int", "0");
		this.zoomType = new ConfigItem("zoomType", "short", "0");
		this.zoomValue = new ConfigItem("zoomValue", "int", "100");
		this.pageViewZoomValue = new ConfigItem("pageViewZoomValue", "int",
				"60");

		this.qColumnStyles = FullList.newList();
		this.qTableRows = FullList.newList();
	}

	public void appendXMLToContentEntry(final Util util,
			final Appendable appendable) throws IOException {
		appendable.append("<table:table");
		util.appendAttribute(appendable, "table:name", this.sName);
		util.appendAttribute(appendable, "table:style-name", this.styleName);
		util.appendAttribute(appendable, "table:print", false);
		appendable.append("><office:forms");
		util.appendAttribute(appendable, "form:automatic-focus", false);
		util.appendAttribute(appendable, "form:apply-design-mode", false);
		appendable.append("/>");
		this.appendColumnStyles(appendable, util);
		this.appendRows(appendable, util);
		appendable.append("</table:table>");
	}

	public void appendXMLToSettingsEntry(final Util util,
			final Appendable appendable) throws IOException {
		appendable.append("<config:config-item-map-entry");
		util.appendAttribute(appendable, "config:name", this.sName);
		appendable.append(">");
		this.cursorPositionX.appendXMLToObject(util, appendable);
		this.cursorPositionY.appendXMLToObject(util, appendable);
		this.horizontalSplitMode.appendXMLToObject(util, appendable);
		this.verticalSplitMode.appendXMLToObject(util, appendable);
		this.horizontalSplitMode.appendXMLToObject(util, appendable);
		this.verticalSplitMode.appendXMLToObject(util, appendable);
		this.horizontalSplitPosition.appendXMLToObject(util, appendable);
		this.verticalSplitPosition.appendXMLToObject(util, appendable);
		this.activeSplitRange.appendXMLToObject(util, appendable);
		this.positionLeft.appendXMLToObject(util, appendable);
		this.positionRight.appendXMLToObject(util, appendable);
		this.positionTop.appendXMLToObject(util, appendable);
		this.positionBottom.appendXMLToObject(util, appendable);
		this.zoomType.appendXMLToObject(util, appendable);
		this.zoomValue.appendXMLToObject(util, appendable);
		this.pageViewZoomValue.appendXMLToObject(util, appendable);
		appendable.append("</config:config-item-map-entry>");
	}

	/**
	 * Get a TableCell, if no TableCell was present at this nRow,nCol, create a
	 * new one with a default of TableCell.STYLE_STRING and a content of "".
	 *
	 * @param nRow
	 *            The row
	 * @param nCol
	 *            The column
	 * @return The TableCell for this position, maybe a new TableCell
	 */
	public TableCell getCell(final int nRow, final int nCol) {

		// -------------------------------------------------------------
		// Check if this row already exists and create a new one if not
		// -------------------------------------------------------------
		TableRow tr = this.qTableRows.get(nRow);
		if (tr == null) {
			tr = new TableRow(this.odsFile, nRow);
			this.qTableRows.set(nRow, tr);
		}
		return tr.getCell(nCol);
	}

	public List<TableColumnStyle> getColumnStyles() {
		return this.qColumnStyles;
	}

	public int getLastCol() {
		return this.nLastCol;
	}

	public int getLastRow() {
		return this.qTableRows.size() - 1;
	}

	/**
	 * Get the name of this table.
	 *
	 * @return The name of this table.
	 */
	@Override
	public String getName() {
		return this.sName;
	}

	public TableRow getRow(final int nRow) throws FastOdsException {
		this.checkRow(nRow);

		TableRow tr;
		if (nRow >= this.qTableRows.size()) {
			tr = new TableRow(this.odsFile, nRow);
			this.qTableRows.set(nRow, tr);
		} else {
			tr = this.qTableRows.get(nRow);
		}
		return tr;
	}

	public List<TableRow> getRows() {
		return this.qTableRows;
	}

	/**
	 * Get the current TableFamilyStyle
	 *
	 * @return The current TableStlye
	 */
	public String getStyleName() {
		return this.styleName;
	}

	public TableRow nextRow() throws FastOdsException {
		final int nRow = this.qTableRows.size();
		this.checkRow(nRow);

		final TableRow tr = new TableRow(this.odsFile, nRow);
		this.qTableRows.add(tr);
		return tr;
	}

	/**
	 * Set the value of a cell.
	 *
	 * @param nRow
	 *            The row
	 * @param nCol
	 *            The column
	 * @param type
	 *            The type of the value,
	 *            TableCell.STYLE_STRING,TableCell.STYLE_FLOAT or
	 *            TableCell.STYLE_PERCENTAGE
	 * @param value
	 *            The value to be set
	 * @return true
	 * @throws FastOdsException
	 *             Thrown when nRow or nCol have wrong values.
	 */
	public boolean setCell(final int nRow, final int nCol, final Type type,
			final String value) throws FastOdsException {
		this.checkRow(nRow);
		this.checkCol(nCol);

		if (nCol > this.nLastCol) {
			this.nLastCol = nCol;
		}
		final TableRow tr = this.getRow(nRow);
		tr.setCell(nCol, type, value);
		return true;
	}

	/**
	 * Set the value of a cell.
	 *
	 * @param nRow
	 *            The row
	 * @param nCol
	 *            The column
	 * @param valuetype
	 *            The type of the value,
	 *            TableCell.STYLE_STRING,TableCell.STYLE_FLOAT or
	 *            TableCell.STYLE_PERCENTAGE
	 * @param value
	 *            The value to be set
	 * @param ts
	 *            The TableFamilyStyle to be used for this cell.
	 * @return true
	 * @throws FastOdsException
	 *             Thrown when nRow or nCol have wrong values.
	 */
	public boolean setCell(final int nRow, final int nCol, final Type valuetype,
			final String value, final TableCellStyle ts)
			throws FastOdsException {

		this.setCell(nRow, nCol, valuetype, value);
		this.setCellStyle(nRow, nCol, ts);
		return true;
	}

	/**
	 * Set the cell style for the specified cell.
	 *
	 * @param nRow
	 *            The row number
	 * @param nCol
	 *            The column number
	 * @param ts
	 *            The TableFamilyStyle to be used
	 * @return TRUE The cell style was set
	 * @throws FastOdsException
	 *             when nRow or nCol have wrong values
	 */
	public boolean setCellStyle(final int nRow, final int nCol,
			final TableCellStyle ts) throws FastOdsException {

		this.checkCol(nCol);
		if (nCol > this.nLastCol) {
			this.nLastCol = nCol;
		}
		final TableRow tr = this.getRow(nRow);
		tr.setCellStyle(nCol, ts);
		return true;
	}

	/**
	 * Set the style of a column.
	 *
	 * @param nCol
	 *            The column number
	 * @param ts
	 *            The style to be used, make sure the style is of type
	 *            TableFamilyStyle.STYLEFAMILY_TABLECOLUMN
	 * @throws FastOdsException
	 *             Thrown if nCol has an invalid value.
	 */
	public void setColumnStyle(final int nCol, final TableColumnStyle ts)
			throws FastOdsException {
		this.checkCol(nCol);
		this.qColumnStyles.set(nCol, ts);
	}

	/**
	 * Set the name of this table.
	 *
	 * @param name
	 *            The name of this table.
	 */
	public void setName(final String name) {
		this.sName = name;
	}

	/**
	 * Set a new TableFamilyStyle
	 *
	 * @param style
	 *            The new TableStlye to be used
	 */
	public void setStyle(final String style) {
		this.styleName = style;
	}

	private void appendColumnStyles(final Appendable appendable,
			final Util util) throws IOException {
		TableColumnStyle ts0 = null;
		TableColumnStyle ts1 = null;
		String sDefaultCellSytle0 = "Default";
		String sDefaultCellSytle1 = "Default";

		// Loop through all table column styles and write the informations
		String sSytle0 = "co1";
		String sSytle1 = "co1";

		// If there is only one column style in column one, just write this
		// info to OutputStream o
		if (this.getColumnStyles().size() == 1) {
			ts0 = this.getColumnStyles().get(0);
			Table.appendColumnStyle(appendable, util, ts0.getName(),
					ts0.getDefaultCellStyle(), 1);

		}

		// If there is more than one column with a style, loop through all
		// styles and
		// write the info to OutputStream o
		if (this.getColumnStyles().size() > 1) {
			int nCount = 1;

			final Iterator<TableColumnStyle> iterator = this.getColumnStyles()
					.iterator();
			ts1 = iterator.next();
			while (iterator.hasNext()) {
				ts0 = ts1;
				ts1 = iterator.next();

				if (ts0 == null) {
					sSytle0 = "co1";
					sDefaultCellSytle0 = "Default";
				} else {
					sSytle0 = ts0.getName();
					sDefaultCellSytle0 = ts0.getDefaultCellStyle();
				}
				if (ts1 == null) {
					sSytle1 = "co1";
					sDefaultCellSytle1 = "Default";
				} else {
					sSytle1 = ts1.getName();
					sDefaultCellSytle1 = ts1.getDefaultCellStyle();
				}

				if (sSytle0.equalsIgnoreCase(sSytle1)) {
					nCount++;
				} else {
					Table.appendColumnStyle(appendable, util, sSytle0,
							sDefaultCellSytle0, nCount);

					nCount = 1;
				}

			}
			Table.appendColumnStyle(appendable, util, sSytle1,
					sDefaultCellSytle1, nCount);

		}
	}

	private void appendRows(final Appendable appendable, final Util util)
			throws IOException {
		// Loop through all rows
		for (final TableRow tr : this.getRows()) {
			if (tr == null) {
				appendable.append("<table:table-row");
				util.appendEAttribute(appendable, "table:style-name", "ro1");
				appendable.append("><table:table-cell");
				util.appendAttribute(appendable,
						"table:number-columns-repeated", this.nLastCol);
				appendable.append("/></table:table-row>");
			} else
				tr.appendXMLToTable(util, appendable);
		}
	}

	private void checkCol(final int nCol) throws FastOdsException {
		if (nCol < 0) {
			throw new FastOdsException(new StringBuilder(
					"Negative column number exception, column value:[")
							.append(nCol).append("]").toString());
		}
	}

	private void checkRow(final int nRow) throws FastOdsException {
		if (nRow < 0) {
			throw new FastOdsException(new StringBuilder(
					"Negative row number exception, row value:[").append(nRow)
							.append("]").toString());
		}
	}

}
