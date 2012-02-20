/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.rendering.wikimodel.common;

import java.io.Reader;

import org.xwiki.rendering.wikimodel.IWemListener;
import org.xwiki.rendering.wikimodel.IWikiParser;
import org.xwiki.rendering.wikimodel.WikiParserException;
import org.xwiki.rendering.wikimodel.common.javacc.CommonWikiScanner;
import org.xwiki.rendering.wikimodel.common.javacc.ParseException;
import org.xwiki.rendering.wikimodel.impl.WikiScannerContext;

/**
 * @author MikhailKotelnikov
 */
public class CommonWikiParser implements IWikiParser
{
    /**
     *
     */
    public CommonWikiParser()
    {
        super();
    }

    /**
     * @see org.xwiki.rendering.wikimodel.IWikiParser#parse(java.io.Reader,
     *      org.xwiki.rendering.wikimodel.IWemListener)
     */
    public void parse(Reader reader, IWemListener listener)
        throws WikiParserException
    {
        try {
            CommonWikiScanner scanner = new CommonWikiScanner(reader);
            WikiScannerContext context = new WikiScannerContext(listener);
            scanner.parse(context);
        } catch (ParseException e) {
            throw new WikiParserException(e);
        }
    }
}