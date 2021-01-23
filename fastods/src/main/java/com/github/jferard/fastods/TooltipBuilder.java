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

import com.github.jferard.fastods.attribute.Length;
import com.github.jferard.fastods.style.GraphicStyle;
import com.github.jferard.fastods.style.ShowableBuilder;
import com.github.jferard.fastods.util.SVGRectangle;

public class TooltipBuilder implements ShowableBuilder<TooltipBuilder> {
    private final String escapedContent;
    private GraphicStyle graphicStyle;
    private Length width;
    private Length height;
    private boolean display;
    private Length x;
    private Length y;
    private SVGRectangle rectangle;

    public TooltipBuilder(final String escapedContent) {
        this.escapedContent = escapedContent;
    }

    public TooltipBuilder rectangle(final SVGRectangle rectangle) {
        this.rectangle = rectangle;
        return this;
    }

    public TooltipBuilder graphicStyle(final GraphicStyle graphicStyle) {
        this.graphicStyle = graphicStyle;
        return this;
    }

    @Override
    public TooltipBuilder visible() {
        this.display = true;
        return this;
    }

    public Tooltip build() {
        return new Tooltip(this.escapedContent, this.rectangle, this.display, this.graphicStyle);
    }
}
