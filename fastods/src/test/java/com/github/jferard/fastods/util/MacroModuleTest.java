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

package com.github.jferard.fastods.util;

import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.testlib.DomTester;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MacroModuleTest {
    @Test
    public void testIndexLine() throws IOException {
        final MacroModule module = new MacroModule("n", "l", "module content");
        final StringBuilder sb = new StringBuilder();
        module.appendIndexLine(XMLUtil.create(), sb);

        DomTester.assertEquals("<library:element library:name=\"n\"/>", sb.toString());
    }

    @Test
    public void testAdd() throws IOException {
        final OdsDocument document = PowerMock.createMock(OdsDocument.class);
        final MacroModule module = new MacroModule("n", "l", "module content");
        final Capture<byte[]> bs = Capture.newInstance();

        PowerMock.resetAll();
        document.addExtraFile(EasyMock.eq("slash/n.xml"), EasyMock.eq("text/xml"),
                EasyMock.capture(bs));

        PowerMock.replayAll();
        module.add(XMLUtil.create(), document, "slash/");

        PowerMock.verifyAll();
        Assert.assertEquals(XMLUtil.XML_PROLOG +
                "<!DOCTYPE script:module" +
                " PUBLIC \"-//OpenOffice.org//DTD OfficeDocument 1.0//EN\" \"module" +
                ".dtd\"><script:module xmlns:script=\"http://openoffice.org/2000/script\" " +
                "script:name=\"n\" script:language=\"l\" script:moduleType=\"normal\">module " +
                "content</script:module>", new String(bs.getValue(), StandardCharsets.UTF_8));
    }
}