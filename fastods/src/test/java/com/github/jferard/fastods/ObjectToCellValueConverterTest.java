/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2021 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * FastODS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.jferard.fastods;

import com.github.jferard.fastods.attribute.CellType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ObjectToCellValueConverterTest {
    private static final String FAST_ODS = "FastODS";
    private static final Date DATE_123 = new Date(123);
    private static final Calendar CALENDAR_456 =
            new GregorianCalendar(0, Calendar.JANUARY, 0, 0, 0, 456);

    private static final Date DATE_456000 = CALENDAR_456.getTime();
    private ObjectToCellValueConverter converter;

    @Before
    public void setUp() {
        this.converter = new ObjectToCellValueConverter("USD");
    }

    @Test
    public void testFromNull() {
        Assert.assertEquals(VoidValue.INSTANCE, this.converter.from(null));
    }

    @Test
    public void testFromHintNull() throws FastOdsException {
        Assert.assertEquals(VoidValue.INSTANCE, this.converter.from(CellType.VOID, null));
    }

    @Test(expected = FastOdsException.class)
    public void testFromHintFailNull() throws FastOdsException {
        Assert.assertEquals(VoidValue.INSTANCE, this.converter.from(CellType.FLOAT, null));
    }

    @Test
    public void testFromString() {
        Assert.assertEquals(new StringValue(FAST_ODS), this.converter.from(FAST_ODS));
    }

    @Test
    public void testFromHintString() throws FastOdsException {
        Assert.assertEquals(new StringValue("10"), this.converter.from(CellType.STRING, 10));
    }

    @Test(expected = FastOdsException.class)
    public void testFromHintFailString() throws FastOdsException {
        this.converter.from(CellType.VOID, FAST_ODS);
    }

    @Test
    public void testFromText() {
        final Text content = Text.content(FAST_ODS);
        Assert.assertEquals(new TextValue(content), this.converter.from(content));
    }

    @Test
    public void testFromHintText() throws FastOdsException {
        final Text content = Text.content(FAST_ODS);
        Assert.assertEquals(new TextValue(content), this.converter.from(CellType.STRING, content));
    }

    @Test(expected = FastOdsException.class)
    public void testFromHintFailText() throws FastOdsException {
        this.converter.from(CellType.VOID, Text.content(FAST_ODS));
    }

    @Test
    public void testFromNumber() {
        Assert.assertEquals(new FloatValue(8.5), this.converter.from(8.5));
    }

    @Test
    public void testFromHintNumber() throws FastOdsException {
        Assert.assertEquals(new FloatValue(8.5), this.converter.from(CellType.FLOAT, 8.5));
    }

    @Test(expected = FastOdsException.class)
    public void testFromHintFailNumber() throws FastOdsException {
        this.converter.from(CellType.VOID, 8.5);
    }

    @Test
    public void testFromBoolean() {
        Assert.assertEquals(new BooleanValue(true), this.converter.from(true));
    }

    @Test
    public void testFromHintBoolean() throws FastOdsException {
        Assert.assertEquals(new BooleanValue(true), this.converter.from(CellType.BOOLEAN, true));
    }

    @Test(expected = FastOdsException.class)
    public void testFromHintFailBoolean() throws FastOdsException {
        this.converter.from(CellType.VOID, true);
    }

    @Test
    public void testFromDate() {
        Assert.assertEquals(new DateValue(DATE_123), this.converter.from(DATE_123));
    }

    @Test
    public void testFromHintDate() throws FastOdsException {
        Assert.assertEquals(new DateValue(DATE_123), this.converter.from(CellType.DATE, 123));
    }

    @Test(expected = FastOdsException.class)
    public void testFromHintFailDate() throws FastOdsException {
        this.converter.from(CellType.VOID, DATE_123);
    }

    @Test
    public void testFromCalendar() {
        Assert.assertEquals(new DateValue(DATE_456000), this.converter.from(CALENDAR_456));
    }

    @Test
    public void testFromHintCalendar() throws FastOdsException {
        Assert.assertEquals(new DateValue(DATE_456000),
                this.converter.from(CellType.DATE, CALENDAR_456));
    }

    @Test(expected = FastOdsException.class)
    public void testFromHintFailCalendar() throws FastOdsException {
        this.converter.from(CellType.VOID, CALENDAR_456);
    }

    @Test
    public void testFromHintCurrency() throws FastOdsException {
        Assert.assertEquals(new CurrencyValue(5, "USD"), this.converter.from(CellType.CURRENCY, 5));
    }

    @Test(expected = FastOdsException.class)
    public void testFromHintFailCurrency() throws FastOdsException {
        this.converter.from(CellType.CURRENCY, "0.47");
    }

    @Test
    public void testFromHintPercentage() throws FastOdsException {
        Assert.assertEquals(new PercentageValue(0.46),
                this.converter.from(CellType.PERCENTAGE, 0.46));
    }

    @Test(expected = FastOdsException.class)
    public void testFromHintFailPercentage() throws FastOdsException {
        this.converter.from(CellType.PERCENTAGE, "0.49");
    }

    @Test
    public void testFromHintTime() throws FastOdsException {
        Assert.assertEquals(new TimeValue(false, 0, 0, 0, 0, 0, 165),
                this.converter.from(CellType.TIME, 165 * 1000));
    }

    @Test(expected = FastOdsException.class)
    public void testFromHintFailTime() throws FastOdsException {
        this.converter.from(CellType.TIME, "165");
    }

    @Test
    public void testFromCellValue() throws FastOdsException {
        Assert.assertEquals(new FloatValue(10), this.converter.from(FloatValue.from(10)));
    }

    @Test
    public void testFromHintCellValue() throws FastOdsException {
        Assert.assertEquals(new FloatValue(10.0),
                this.converter.from(CellType.FLOAT, FloatValue.from(10.0)));
    }

    @Test(expected = FastOdsException.class)
    public void testFromHintFailCellValue() throws FastOdsException {
        this.converter.from(CellType.VOID, FloatValue.from(10));
    }

}