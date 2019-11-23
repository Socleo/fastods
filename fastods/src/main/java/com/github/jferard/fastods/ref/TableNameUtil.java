/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.ref;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;

/**
 * A TableNameUtil checks the table name and escapes it.
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class TableNameUtil {
    /**
     * A single quote
     */
    private static final char SINGLE_QUOTE = '\'';
    private static final char[] FORBIDDEN_CHARS = "[]*?:/\\".toCharArray();

    /**
     * Check if the table name is ok. Currently, this does stick to LO limitations (excepted for
     * the check of duplicate names), but may the conditions may be relaxed in a future version.
     *
     * @param name the name to check
     */
    public void checkTableName(final CharSequence name) {
        if (name.charAt(0) == SINGLE_QUOTE) {
            throw new IllegalArgumentException(
                    "Table name should not start with " + SINGLE_QUOTE + ": " + name);
        }
        for (int i = 0; i < name.length(); i++) {
            final char c = name.charAt(i);
            for (final char fc : FORBIDDEN_CHARS) {
                if (c == fc) {
                    throw new IllegalArgumentException(
                            "Table name should not contain " + new String(FORBIDDEN_CHARS) + ": " +
                                    name);
                }
            }
        }
    }

    /**
     * 9.2.1 Referencing Table Cells
     * > If the name of the table contains blanks, dots “.” (U+002E, FULL STOP) or apostrophes
     * “'” (U+0027, APOSTROPHE), the name shall be quoted with apostrophes “'” (U+0027,
     * APOSTROPHE). Any apostrophes in the name shall be escaped by doubling the”'”( U+0027,
     * APOSTROPHE) character.
     *
     * @param tableName the name of the table
     * @return the name of the table escaped
     */
    public String escapeTableName(final CharSequence tableName) {
        // step 1: to quote or no? how many single quotes?
        boolean toQuote = false;
        int apostropheCount = 0;
        for (int i = 0; i < tableName.length(); i++) {
            final char c = tableName.charAt(i);
            if (Character.isWhitespace(c) || c == '.') {
                toQuote = true;
            } else if (c == '\'') {
                toQuote = true;
                apostropheCount += 1;
            }
        }

        final StringBuilder sb =
                new StringBuilder(tableName.length() + (toQuote ? 2 : 0) + apostropheCount);

        // step 2: build the string
        if (toQuote) {
            sb.append(SINGLE_QUOTE);
        }
        for (int i = 0; i < tableName.length(); i++) {
            final char c = tableName.charAt(i);
            sb.append(c);
            if (c == '\'') {
                sb.append(SINGLE_QUOTE);
            }
        }
        if (toQuote) {
            sb.append(SINGLE_QUOTE);
        }
        return sb.toString();
    }

    /**
     * @param filename the name
     * @return the name with quotes unescaped
     * @throws ParseException
     * @throws UnsupportedEncodingException
     */
    public String unescapeFilename(final String filename)
            throws ParseException, UnsupportedEncodingException {
        return URLDecoder.decode(this.unescapeQuotes(filename), "UTF-8");
    }

    /**
     * @param str the name
     * @return the name with quotes unescaped
     * @throws ParseException
     */
    public String unescapeQuotes(final String str) throws ParseException {
        final int length = str.length();
        if (str.charAt(0) == SINGLE_QUOTE && str.charAt(length - 1) == SINGLE_QUOTE) {
            return this.unescapeEscaped(str.substring(1, length - 1));
        } else {
            final int i = str.indexOf(SINGLE_QUOTE);
            if (i != -1) {
                throw new ParseException("Unquoted name " + str, i);
            }
            return str;
        }
    }

    private String unescapeEscaped(final String escaped) throws ParseException {
        final StringBuilder sb = new StringBuilder();
        int quotes = 0;
        for (int i = 0; i < escaped.length(); i++) {
            final char c = escaped.charAt(i);
            if (c == SINGLE_QUOTE) {
                quotes++;
                if (quotes == 2) {
                    quotes = 0;
                    sb.append(SINGLE_QUOTE);
                }
            } else {
                if (quotes == 1) {
                    throw new ParseException("Missing closing quote in filename " + escaped, i);
                }
                sb.append(c);
            }
        }
        if (quotes != 0) {
            throw new ParseException("Random quote in filename", 0);
        }
        return sb.toString();
    }
}
