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
package com.github.jferard.fastods;

import com.github.jferard.fastods.style.TextProperties;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class SpanTest {
	private XMLUtil util;

	@Before
	public void setUp() {
		this.util = XMLUtil.create();
	}

	@Test
	public final void testFHTextWithStyle() throws IOException {
		final TextStyle ts = TextProperties.builder().buildStyle("test");
		final Span fhtext = new Span("text", ts);
		final StringBuilder sbo = new StringBuilder();
		fhtext.appendXMLContent(this.util, sbo);
		DomTester.assertEquals(
				"<text:span text:style-name=\"test\">text</text:span>",
				sbo.toString());
		final StringBuilder sbt = new StringBuilder();
	}

	@Test
	public final void testSimpleFHText() throws IOException {
		final Span fhtext = new Span("text");
		final StringBuilder sbo = new StringBuilder();
		fhtext.appendXMLContent(this.util, sbo);
		Assert.assertEquals("text", sbo.toString());
	}
}
