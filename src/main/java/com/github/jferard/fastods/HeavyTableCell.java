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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.github.jferard.fastods.style.DataStyles;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.Util;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file HeavyTableCell.java is part of FastODS.
 *
 *         WHERE ?
 *         content.xml/office:document-content/office:body/office:spreadsheet/
 *         table:table/table:table-row/table:table-cell
 */
public class HeavyTableCell implements TableCell {

	/**
	 * 19.385 office:value-type
	 */
	public static enum Type {
		BOOLEAN("boolean"), CURRENCY("currency"), DATE("date"), FLOAT(
				"float"), PERCENTAGE("percentage"), STRING(
						"string"), TIME("time"), VOID("void");

		private final String attrValue;

		private Type(final String attrValue) {
			this.attrValue = attrValue;
		}

		public String getAttrValue() {
			return this.attrValue;
		}
	}

	public static final Type DEFAULT_TYPE = Type.VOID;

	/**
	 * XML Schema Part 2, 3.2.7 dateTime
	 */
	static final SimpleDateFormat DATE_VALUE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSS");

	private final DataStyles dataStyles;
	// private final int nCol;
	private int nColumnsSpanned;
	// private final int nRow;
	private int nRowsSpanned;
	private final OdsFile odsFile;
	/**
	 * 19.384 office:value
	 */
	private String sCurrency;

	private TableCellStyle style;

	private String sValue;

	private final Util util;

	private Type valueType;

	/**
	 * A table cell.
	 *
	 * @param valuetype
	 *            The value type for this cell.
	 * @param value
	 *            The String content for this cell.
	 */
	HeavyTableCell(final OdsFile odsFile, final Util util,
			final DataStyles dataStyles, final int nRow, final int nCol) {
		this.util = util;
		this.dataStyles = dataStyles;
		this.odsFile = odsFile;
		// this.nRow = nRow;
		// this.nCol = nCol;
		this.valueType = HeavyTableCell.DEFAULT_TYPE;
		this.nColumnsSpanned = 0;
		this.nRowsSpanned = 0;
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#appendXMLToTableRow(com.github.jferard.fastods.util.XMLUtil, java.lang.Appendable)
	 */
	@Override
	public void appendXMLToTableRow(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<table:table-cell");

		if (this.style != null) {
			util.appendEAttribute(appendable, "table:style-name",
					this.style.getName());
		}

		util.appendEAttribute(appendable, "office:value-type",
				this.valueType.attrValue);
		switch (this.valueType) {
		case BOOLEAN:
			util.appendEAttribute(appendable, "office:boolean-value",
					this.sValue);
			break;
		case CURRENCY:
			util.appendAttribute(appendable, "office:currency", this.sCurrency);
			//$FALL-THROUGH$
		case FLOAT:
		case PERCENTAGE:
			util.appendEAttribute(appendable, "office:value", this.sValue);
			break;
		case DATE:
			util.appendEAttribute(appendable, "office:date-value", this.sValue);
			break;
		case STRING:
			util.appendAttribute(appendable, "office:string-value",
					this.sValue);
			break;
		case TIME:
			util.appendEAttribute(appendable, "office:time-value", this.sValue);
			break;
		case VOID:
		default:
			break;
		}

		if (this.nColumnsSpanned > 0) {
			util.appendEAttribute(appendable, "table:number-columns-spanned",
					this.nColumnsSpanned);
		}
		if (this.nRowsSpanned > 0) {
			util.appendEAttribute(appendable, "table:number-rows-spanned",
					this.nRowsSpanned);
		}

		// if (this.sText == null)
		appendable.append("/>");
		// else {
		// appendable.append("><text:p>").append(this.sText)
		// .append("</text:p>");
		// appendable.append("</table:table-cell>");
		// }
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#getBooleanValue()
	 */
	@Override
	public String getBooleanValue() {
		return this.sValue;
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#getColumnsSpanned()
	 */
	@Override
	public int getColumnsSpanned() {
		return this.nColumnsSpanned;
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#getCurrency()
	 */
	@Override
	public String getCurrency() {
		return this.sCurrency;
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#getCurrencyValue()
	 */
	@Override
	public String getCurrencyValue() {
		return this.sValue;
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#getDateValue()
	 */
	@Override
	public String getDateValue() {
		return this.sValue;
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#getFloatValue()
	 */
	@Override
	public String getFloatValue() {
		return this.sValue;
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#getPercentageValue()
	 */
	@Override
	public String getPercentageValue() {
		return this.sValue;
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#getRowsSpanned()
	 */
	@Override
	public int getRowsSpanned() {
		return this.nRowsSpanned;
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#getStringValue()
	 */
	@Override
	public String getStringValue() {
		return this.sValue;
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#getStyleName()
	 */
	@Override
	public String getStyleName() {
		return this.style.getName();
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#getTimeValue()
	 */
	@Override
	public String getTimeValue() {
		return this.sValue;
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#getValueType()
	 */
	@Override
	public Type getValueType() {
		return this.valueType;
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setBooleanValue(boolean)
	 */
	@Override
	public void setBooleanValue(final boolean value) {
		this.sValue = value ? "true" : "false";
		this.valueType = HeavyTableCell.Type.BOOLEAN;
		this.setStyle(this.dataStyles.getBooleanStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setColumnsSpanned(int)
	 */
	@Override
	public void setColumnsSpanned(final int n) {
		if (n < 0) {
			this.nColumnsSpanned = 0;
		} else {
			this.nColumnsSpanned = n;
		}
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setCurrencyValue(float, java.lang.String)
	 */
	@Override
	public void setCurrencyValue(final float value, final String currency) {
		this.sValue = Float.toString(value);
		this.sCurrency = currency; // escape here
		this.valueType = HeavyTableCell.Type.CURRENCY;
		this.setStyle(this.dataStyles.getCurrencyStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setCurrencyValue(int, java.lang.String)
	 */
	@Override
	public void setCurrencyValue(final int value, final String currency) {
		this.sValue = Integer.toString(value);
		this.sCurrency = currency; // escape here
		this.valueType = HeavyTableCell.Type.CURRENCY;
		this.setStyle(this.dataStyles.getCurrencyStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setCurrencyValue(java.lang.Number, java.lang.String)
	 */
	@Override
	public void setCurrencyValue(final Number value, final String currency) {
		this.sValue = value.toString();
		this.sCurrency = currency; // escape here
		this.valueType = HeavyTableCell.Type.CURRENCY;
		this.setStyle(this.dataStyles.getCurrencyStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setDateValue(java.util.Calendar)
	 */
	@Override
	public void setDateValue(final Calendar cal) {
		this.setDateValue(cal.getTime());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setDateValue(java.util.Date)
	 */
	@Override
	public void setDateValue(final Date value) {
		this.sValue = HeavyTableCell.DATE_VALUE_FORMAT.format(value);
		this.valueType = HeavyTableCell.Type.DATE;
		this.setStyle(this.dataStyles.getDateStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setFloatValue(float)
	 */
	@Override
	public void setFloatValue(final float value) {
		this.sValue = Float.toString(value);
		this.valueType = HeavyTableCell.Type.FLOAT;
		this.setStyle(this.dataStyles.getNumberStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setFloatValue(int)
	 */
	@Override
	public void setFloatValue(final int value) {
		this.sValue = Integer.toString(value);
		this.valueType = HeavyTableCell.Type.FLOAT;
		this.setStyle(this.dataStyles.getNumberStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setFloatValue(java.lang.Number)
	 */
	@Override
	public void setFloatValue(final Number value) {
		this.sValue = value.toString();
		this.valueType = HeavyTableCell.Type.FLOAT;
		this.setStyle(this.dataStyles.getNumberStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setObjectValue(java.lang.Object)
	 */
	@Override
	public void setObjectValue(final Object value) {
		if (value == null)
			return;

		final Object retValue;
		if (value instanceof String)
			this.setStringValue((String) value);
		else if (value instanceof Number)
			this.setFloatValue((Number) value);
		else if (value instanceof Date) {
			this.setDateValue((Date) value);
		} else if (value instanceof Calendar) {
			this.setDateValue((Calendar) value);
		} else
			this.setStringValue(value.toString());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setPercentageValue(float)
	 */
	@Override
	public void setPercentageValue(final float value) {
		this.sValue = Float.toString(value);
		this.valueType = HeavyTableCell.Type.PERCENTAGE;
		this.setStyle(this.dataStyles.getPercentageStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setPercentageValue(java.lang.Number)
	 */
	@Override
	public void setPercentageValue(final Number value) {
		this.sValue = value.toString();
		this.valueType = HeavyTableCell.Type.PERCENTAGE;
		this.setStyle(this.dataStyles.getPercentageStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setRowsSpanned(int)
	 */
	@Override
	public void setRowsSpanned(final int n) {
		this.nRowsSpanned = n < 0 ? 0 : n;
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setStringValue(java.lang.String)
	 */
	@Override
	public void setStringValue(final String value) {
		this.sValue = value;// TODO ESCAPE
		this.valueType = HeavyTableCell.Type.STRING;
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setStyle(com.github.jferard.fastods.style.TableCellStyle)
	 */
	@Override
	public void setStyle(final TableCellStyle style) {
		if (style == null)
			return;

		style.addToFile(this.odsFile);
		if (style.getDataStyle() == null && this.style != null
				&& this.style.getDataStyle() != null)
			style.setDataStyle(this.style.getDataStyle());
		this.style = style;
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setTimeValue(long)
	 */
	@Override
	public void setTimeValue(final long timeInMillis) {
		this.sValue = this.util.formatTimeInterval(timeInMillis);
		this.valueType = HeavyTableCell.Type.TIME;
		this.setStyle(this.dataStyles.getTimeStyle());
	}
}