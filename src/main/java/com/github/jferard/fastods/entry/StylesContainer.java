/*******************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FastODS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.github.jferard.fastods.entry;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.style.MasterPageStyle;
import com.github.jferard.fastods.style.StyleTag;
import com.github.jferard.fastods.util.Container;
import com.github.jferard.fastods.util.Container.Mode;
import com.github.jferard.fastods.util.MultiContainer;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

/**
 * content.xml/office:document-content
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class StylesContainer {
	public enum Dest {
		CONTENT_AUTOMATIC_STYLES, STYLES_AUTOMATIC_STYLES, STYLES_COMMON_STYLES,
	}

	private final Container<String, DataStyle> dataStylesContainer;
	private final Container<String, MasterPageStyle> masterPageStylesContainer;
	private final MultiContainer<String, StyleTag, Dest> styleTagsContainer;

	StylesContainer() {
		this.styleTagsContainer = new MultiContainer<String, StyleTag, Dest>(
				Dest.class);
		this.dataStylesContainer = new Container<String, DataStyle>();
		this.masterPageStylesContainer = new Container<String, MasterPageStyle>();
	}

	public void addDataStyle(final DataStyle dataStyle) {
		this.dataStylesContainer.add(dataStyle.getName(), dataStyle,
				Mode.CREATE_OR_UPDATE);
	}

	public boolean addDataStyle(final DataStyle dataStyle, final Mode mode) {
		return this.dataStylesContainer.add(dataStyle.getName(), dataStyle,
				mode);
	}

	public boolean addMasterPageStyle(final MasterPageStyle ps) {
		return this.addMasterPageStyle(ps, Mode.CREATE_OR_UPDATE);
	}

	public boolean addMasterPageStyle(final MasterPageStyle ps,
			final Mode mode) {
		if (this.masterPageStylesContainer.add(ps.getName(), ps, mode)) {
			ps.addEmbeddedStylesToStylesEntry(this, mode);
			return true;
		} else
			return false;
	}

	public void addStyleToContentAutomaticStyles(final StyleTag styleTag) {
		this.styleTagsContainer.add(this.buildKey(styleTag), styleTag,
				Dest.CONTENT_AUTOMATIC_STYLES, Mode.CREATE_OR_UPDATE);
	}

	public boolean addStyleToContentAutomaticStyles(final StyleTag styleTag,
			final Mode mode) {
		return this.styleTagsContainer.add(this.buildKey(styleTag), styleTag,
				Dest.CONTENT_AUTOMATIC_STYLES, mode);
	}

	/*
	@Deprecated
	public Map<String, StyleTag> getStyleTagByName() {
		return this.contentAutomaticStyleTagByName;
	}
	*/

	public void addStyleToStylesAutomaticStyles(final StyleTag styleTag) {
		this.styleTagsContainer.add(this.buildKey(styleTag), styleTag,
				Dest.STYLES_AUTOMATIC_STYLES, Mode.CREATE_OR_UPDATE);
	}

	public boolean addStyleToStylesAutomaticStyles(final StyleTag styleTag,
			final Mode mode) {
		return this.styleTagsContainer.add(this.buildKey(styleTag), styleTag,
				Dest.STYLES_AUTOMATIC_STYLES, mode);
	}

	public void addStyleToStylesCommonStyles(final StyleTag styleTag) {
		this.styleTagsContainer.add(this.buildKey(styleTag), styleTag,
				Dest.STYLES_COMMON_STYLES, Mode.CREATE_OR_UPDATE);
	}

	public boolean addStyleToStylesCommonStyles(final StyleTag styleTag,
			final Mode mode) {
		return this.styleTagsContainer.add(this.buildKey(styleTag), styleTag,
				Dest.STYLES_COMMON_STYLES, mode);
	}

	public Map<String, DataStyle> getDataStyles() {
		return this.dataStylesContainer.getValueByKey();
	}

	public Map<String, StyleTag> getStyleTagByName(final Dest dest) {
		return this.styleTagsContainer.getValueByKey(dest);
	}

	public HasFooterHeader hasFooterHeader() {
		boolean hasHeader = false;
		boolean hasFooter = false;

		for (final MasterPageStyle ps : this.masterPageStylesContainer
				.getValues()) {
			if (hasHeader && hasFooter)
				break;
			if (!hasHeader && ps.getHeader() != null)
				hasHeader = true;
			if (!hasFooter && ps.getFooter() != null)
				hasFooter = true;
		}
		return new HasFooterHeader(hasHeader, hasFooter);
	}

	public void writeContentAutomaticStyles(final XMLUtil util,
			final ZipUTF8Writer writer) throws IOException {
		this.write(this.styleTagsContainer
				.getValues(Dest.CONTENT_AUTOMATIC_STYLES), util, writer);
	}

	public void writeDataStyles(final XMLUtil util, final ZipUTF8Writer writer)
			throws IOException {
		for (final DataStyle dataStyle : this.dataStylesContainer.getValues())
			dataStyle.appendXML(util, writer);
	}

	public void writeMasterPageStylesToAutomaticStyles(final XMLUtil util,
			final ZipUTF8Writer writer) throws IOException {
		for (final MasterPageStyle ps : this.masterPageStylesContainer
				.getValues())
			ps.appendXMLToAutomaticStyle(util, writer);
	}

	public void writeMasterPageStylesToMasterStyles(final XMLUtil util,
			final ZipUTF8Writer writer) throws IOException {
		for (final MasterPageStyle ps : this.masterPageStylesContainer
				.getValues())
			ps.appendXMLToMasterStyle(util, writer);
	}

	public void writeStylesAutomaticStyles(final XMLUtil util,
			final ZipUTF8Writer writer) throws IOException {
		this.write(
				this.styleTagsContainer.getValues(Dest.STYLES_AUTOMATIC_STYLES),
				util, writer);
	}

	public void writeStylesCommonStyles(final XMLUtil util,
			final ZipUTF8Writer writer) throws IOException {
		this.write(this.styleTagsContainer.getValues(Dest.STYLES_COMMON_STYLES),
				util, writer);
	}

	private String buildKey(final StyleTag styleTag) {
		final String name = styleTag.getName();
		final String family = styleTag.getFamily();
		return family + "@" + name;
	}

	private void write(final Iterable<StyleTag> iterable, final XMLUtil util,
			final ZipUTF8Writer writer) throws IOException {
		for (final StyleTag ts : iterable)
			ts.appendXML(util, writer);
	}

	public Map<String, MasterPageStyle> getMasterPageStyles() {
		return this.masterPageStylesContainer.getValueByKey();
	}
}
