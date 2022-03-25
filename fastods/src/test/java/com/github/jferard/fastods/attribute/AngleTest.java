/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2022 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.attribute;

import org.junit.Assert;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

public class AngleTest {
    @Test
    public void testDeg() {
        final Angle r = Angle.deg(10);
        Assert.assertEquals("10", r.toString());
    }

    @Test
    public void testRad() {
        final Angle r = Angle.rad(3.14);
        Assert.assertEquals("3.14rad", r.toString());
    }

    @Test
    public void testGrad() {
        final Angle r = Angle.grad(16);
        Assert.assertEquals("16.0grad", r.toString());
    }

    @Test
    public void testOther() {
        final Angle r = new Angle(12, null);
        Assert.assertThrows(NullPointerException.class, () -> r.getValue());
    }
}