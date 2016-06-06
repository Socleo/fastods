package com.github.jferard.fastods;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.github.jferard.fastods.HeavyTableCell.Type;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.XMLUtil;

public interface TableCell {

	void appendXMLToTableRow(XMLUtil util, Appendable appendable)
			throws IOException;

	/**
	 * Return the currently set boolean value.
	 *
	 * @return The currency value
	 */
	String getBooleanValue();

	/**
	 * @return The number of columns that this cell spans overs.
	 */
	int getColumnsSpanned();

	/**
	 * Return the currently set currency value.<br>
	 * There is no check if this is really a table cell with style
	 * STYLE_CURRENCY.
	 *
	 * @return The currency value
	 */
	String getCurrency();

	/**
	 * Return the currently set currency value.<br>
	 * There is no check if this is really a table cell with style
	 * STYLE_CURRENCY.
	 *
	 * @return The currency value
	 */
	String getCurrencyValue();

	/**
	 * @return The date value set by setDateValue() or an empty string if
	 *         nothing was set.
	 */
	String getDateValue();

	/**
	 * @return The float value set by setFloatValue() or an empty string if
	 *         nothing was set.
	 */
	String getFloatValue();

	/**
	 * @return The percentage value set by setPercentageValue() or an empty
	 *         string if nothing was set.
	 */
	String getPercentageValue();

	/**
	 * @return The number of rows that this cell spans overs.
	 */
	int getRowsSpanned();

	/**
	 * @return The string value set by setStringValue() or an empty string if
	 *         nothing was set.
	 */
	String getStringValue();

	String getStyleName();

	/**
	 * @return The timee value set by setTimeValue() or an empty string if
	 *         nothing was set.
	 */
	String getTimeValue();

	Type getValueType();

	/**
	 * Set the currency value and table cell style to STYLE_CURRENCY.
	 *
	 * @param currency
	 *            The currency value
	 */
	void setBooleanValue(boolean value);

	/**
	 * To merge cells, set the number of columns that should be merged.
	 *
	 * @param n
	 *            - The number of cells to be merged
	 */
	void setColumnsSpanned(int n);

	/**
	 * Set the currency value and table cell style to STYLE_CURRENCY.
	 *
	 * @param currency
	 *            The currency value
	 */
	void setCurrencyValue(float value, String currency);

	/**
	 * Set the currency value and table cell style to STYLE_CURRENCY.
	 *
	 * @param currency
	 *            The currency value
	 */
	void setCurrencyValue(int value, String currency);

	/**
	 * Set the currency value and table cell style to STYLE_CURRENCY.
	 *
	 * @param currency
	 *            The currency value
	 */
	void setCurrencyValue(Number value, String currency);

	/**
	 * Set the date value for a cell with HeavyTableCell.STYLE_DATE.
	 *
	 * @param cal
	 *            - A Calendar object with the date to be used
	 */
	void setDateValue(Calendar cal);

	void setDateValue(Date value);

	/**
	 * Set the float value for a cell with HeavyTableCell.Type.FLOAT.
	 *
	 * @param value
	 *            - A double object with the value to be used
	 */
	void setFloatValue(float value);

	/**
	 * Set the float value for a cell with HeavyTableCell.Type.FLOAT.
	 *
	 * @param value
	 *            - A double object with the value to be used
	 */
	void setFloatValue(int value);

	/**
	 * Set the float value for a cell with HeavyTableCell.Type.FLOAT.
	 *
	 * @param value
	 *            - A double object with the value to be used
	 */
	void setFloatValue(Number value);

	/**
	 * Set the float value for a cell with HeavyTableCell.Type.STRING.
	 *
	 * @param value
	 *            - A double object with the value to be used
	 */
	void setObjectValue(Object value);

	/**
	 * Set the float value for a cell with HeavyTableCell.Type.PERCENTAGE.
	 *
	 * @param value
	 *            - A double object with the value to be used
	 */
	void setPercentageValue(float value);

	/**
	 * Set the float value for a cell with HeavyTableCell.Type.PERCENTAGE.
	 *
	 * @param value
	 *            - A double object with the value to be used
	 */
	void setPercentageValue(Number value);

	/**
	 * To merge cells, set the number of rows that should be merged.
	 *
	 * @param n
	 *            - The number of rows to be merged
	 */
	void setRowsSpanned(int n);

	/**
	 * Set the float value for a cell with HeavyTableCell.Type.STRING.
	 *
	 * @param value
	 *            - A double object with the value to be used
	 */
	void setStringValue(String value);

	void setStyle(TableCellStyle style);

	void setTimeValue(long timeInMillis);

}