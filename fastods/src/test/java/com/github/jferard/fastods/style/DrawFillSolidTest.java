/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2023 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.style;

import com.github.jferard.fastods.TestHelper;
import com.github.jferard.fastods.attribute.SimpleColor;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;

public class DrawFillSolidTest {
    @Test
    public void testAppendAttributes() throws IOException {
        final DrawFillSolid solid = new DrawFillSolid(SimpleColor.GREEN);
        final StringBuilder sb = new StringBuilder();
        solid.appendAttributes(XMLUtil.create(), sb);
        Assert.assertEquals(" draw:fill=\"solid\" draw:fill-color=\"#008000\"", sb.toString());
    }

    @Test
    public void testAddToStyles() throws IOException {
        final StylesContainer container = PowerMock.createMock(StylesContainer.class);
        final DrawFillSolid solid = new DrawFillSolid(SimpleColor.GREEN);

        PowerMock.resetAll();
        // do nothing

        PowerMock.replayAll();
        solid.addEmbeddedStyles(container);

        PowerMock.verifyAll();
    }
}