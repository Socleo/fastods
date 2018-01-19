/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
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
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.jferard.fastods.datastyle;

import com.github.jferard.fastods.TableCell;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

public class DataStylesTest {
    private DataStyles dataStyles;

    @Before
    public void setUp() {
        this.dataStyles = DataStylesFactory.create(Locale.US);
    }

    @Test
    public void testGetAll() {
        for (final TableCell.Type type : TableCell.Type.values()) {
            Assert.assertNotNull(this.dataStyles.getDataStyle(type));
        }
    }

    @Test
    public void testGetString() {
         Assert.assertNotNull(this.dataStyles.getDataStyle(TableCell.Type.STRING));
    }
}