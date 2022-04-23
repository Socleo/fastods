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

package com.github.jferard.fastods.crypto;

import com.github.jferard.fastods.odselement.EncryptParameters;
import com.github.jferard.fastods.odselement.EncryptParametersBuilder;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import com.github.jferard.fastods.util.ZipUTF8WriterBuilder;
import com.github.jferard.fastods.util.ZipUTF8WriterBuilderImpl;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * A builder for `ZipUTF8CryptoWriter`.
 */
public class ZipUTF8CryptoWriterBuilder implements ZipUTF8WriterBuilder {
    public static final String AES_CBC_ISO_10126_PADDING = "AES/CBC/ISO10126Padding";
    public static final String SHA_1_PRNG = "SHA1PRNG";

    /**
     * **Beware: for security reasons, this fills the password array with 0's**
     *
     * @param password the password to encrypt data
     * @return a builder
     * @throws NoSuchAlgorithmException won't happen since SHA-256 is pretty common
     */
    public static ZipUTF8WriterBuilder create(final char[] password)
            throws NoSuchAlgorithmException {
        return new ZipUTF8CryptoWriterBuilder(new ZipUTF8WriterBuilderImpl(),
                EncryptParameters.builder(), password);
    }


    private final ZipUTF8WriterBuilderImpl writerBuilder;
    private final EncryptParametersBuilder parametersBuilder;
    private final byte[] hashedPassword;

    /**
     * **Beware: for security reasons, this fills the password array with 0's**
     * @param writerBuilder the writer builder. May be initialized
     * @param parametersBuilder the parameters builder. May be initialized
     * @param password the password
     * @throws NoSuchAlgorithmException won't happen since SHA-256 is pretty common
     */
    public ZipUTF8CryptoWriterBuilder(final ZipUTF8WriterBuilderImpl writerBuilder,
                                      final EncryptParametersBuilder parametersBuilder,
                                      final char[] password) throws NoSuchAlgorithmException {
        this.writerBuilder = writerBuilder;
        this.parametersBuilder = parametersBuilder;
        // We hash password and void array as soon as possible
        this.hashedPassword = Util.getPasswordChecksum(password, "SHA-256");
    }

    @Override
    public ZipUTF8CryptoWriter build(final OutputStream outputStream) {
        try {
            return new ZipUTF8CryptoWriter(this.writerBuilder.build(outputStream),
                    new StandardEncrypter(SecureRandom.getInstance(SHA_1_PRNG),
                            Cipher.getInstance(AES_CBC_ISO_10126_PADDING), 100000, 32, 32, this.parametersBuilder
                    ), this.hashedPassword);
        } catch (final NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }
}
