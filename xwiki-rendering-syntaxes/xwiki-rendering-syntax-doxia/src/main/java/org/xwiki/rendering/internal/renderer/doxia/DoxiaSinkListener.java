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
package org.xwiki.rendering.internal.renderer.doxia;

import java.util.Map;

import org.apache.maven.doxia.sink.Sink;
import org.xwiki.rendering.listener.Format;
import org.xwiki.rendering.listener.HeaderLevel;
import org.xwiki.rendering.listener.ListType;
import org.xwiki.rendering.listener.Listener;
import org.xwiki.rendering.listener.MetaData;
import org.xwiki.rendering.listener.reference.ResourceReference;
import org.xwiki.rendering.syntax.Syntax;

/**
 * Send Listener events to the Sink (except for Table Rows events which are sent in {@link DoxiaListener}.
 *
 * @version $Id$
 * @since 4.3M1
 */
public class DoxiaSinkListener implements Listener
{
    /**
     * The Doxia Renderer to which to emit events to.
     */
    private Sink sink;

    /**
     * Since we need to tell Doxia the section level and since XWiki Events only give the level for begin/endHeader
     * events, we need to remember the header level in order to be able to properly close the section by sending
     * the correct Doxia event.
     */
    private HeaderLevel headerLevel;

    /**
     * @param sink the Doxia Renderer to which to emit events to
     */
    public DoxiaSinkListener(Sink sink)
    {
        this.sink = sink;
    }

    @Override
    public void beginDocument(MetaData metadata)
    {
        this.sink.head();
        this.sink.head_();
        this.sink.body();
    }

    @Override
    public void endDocument(MetaData metadata)
    {
        this.sink.body_();
    }

    @Override
    public void beginGroup(Map<String, String> parameters)
    {
        // Do nothing since Doxia doesn't support groups
    }

    @Override
    public void endGroup(Map<String, String> parameters)
    {
        // Do nothing since Doxia doesn't support groups
    }

    @Override
    public void onVerbatim(String content, boolean inline, Map<String, String> parameters)
    {
        // TODO: Handle parameters
        this.sink.verbatim(null);
        this.sink.text(content);
        this.sink.verbatim_();
    }

    @Override
    public void beginFormat(Format format, Map<String, String> parameters)
    {
        // TODO: Handle parameters
        switch (format) {
            case BOLD:
                this.sink.bold();
                break;
            case ITALIC:
                this.sink.italic();
                break;
            case STRIKEDOUT:
                // TODO: Implement when we move to Doxia 1.0 beta 1.
                // See http://jira.codehaus.org/browse/DOXIA-204
                break;
            case UNDERLINED:
                // TODO: Implement when we move to Doxia 1.0 beta 1.
                // See http://jira.codehaus.org/browse/DOXIA-204
                break;
            default:
                // Unhandled format, don't do anything.
        }
    }

    @Override
    public void endFormat(Format format, Map<String, String> parameters)
    {
        // TODO: Handle parameters
        switch (format) {
            case BOLD:
                this.sink.bold_();
                break;
            case ITALIC:
                this.sink.italic_();
                break;
            case STRIKEDOUT:
                // TODO: Implement when we move to Doxia 1.0 beta 1.
                // See http://jira.codehaus.org/browse/DOXIA-204
                break;
            case UNDERLINED:
                // TODO: Implement when we move to Doxia 1.0 beta 1.
                // See http://jira.codehaus.org/browse/DOXIA-204
                break;
            default:
                // Unhandled format, don't do anything.
        }
    }

    @Override
    public void beginList(ListType type, Map<String, String> parameters)
    {
        if (type == ListType.BULLETED) {
            this.sink.list();
        } else {
            // TODO: Handle other numerotations (Roman, etc)
            this.sink.numberedList(Sink.NUMBERING_DECIMAL);
        }
    }

    @Override
    public void beginListItem()
    {
        this.sink.listItem();
    }

    @Override
    public void beginMacroMarker(String name, Map<String, String> parameters, String content, boolean isInline)
    {
        // Don't do anything since Doxia doesn't have macro markers and anyway we shouldn't
        // do anything.
    }

    @Override
    public void beginParagraph(Map<String, String> parameters)
    {
        this.sink.paragraph();
    }

    @Override
    public void beginSection(Map<String, String> parameters)
    {
        // Note: The logic is in beginHeader.
    }

    @Override
    public void beginHeader(HeaderLevel level, String id, Map<String, String> parameters)
    {
        // Doxia has only 5 section levels!
        int levelAsInt = (level.getAsInt() < 6) ? level.getAsInt() : 5;
        this.sink.section(levelAsInt, null);
        this.sink.sectionTitle(levelAsInt, null);

        // Remember the header level for endSection() handling.
        this.headerLevel = level;
    }

    @Override
    public void endList(ListType type, Map<String, String> parameters)
    {
        if (type == ListType.BULLETED) {
            this.sink.list_();
        } else {
            this.sink.numberedList_();
        }
    }

    @Override
    public void endListItem()
    {
        this.sink.listItem_();
    }

    @Override
    public void endMacroMarker(String name, Map<String, String> parameters, String content, boolean isInline)
    {
        // Don't do anything since Doxia doesn't have macro markers and anyway we shouldn't
        // do anything.
    }

    @Override
    public void endParagraph(Map<String, String> parameters)
    {
        this.sink.paragraph_();
    }

    @Override
    public void endSection(Map<String, String> parameters)
    {
        // Doxia has only 5 section levels!
        int levelAsInt = (this.headerLevel.getAsInt() < 6) ? this.headerLevel.getAsInt() : 5;
        this.sink.section_(levelAsInt);
    }

    @Override
    public void endHeader(HeaderLevel level, String id, Map<String, String> parameters)
    {
        // Doxia has only 5 section levels!
        int levelAsInt = (level.getAsInt() < 6) ? level.getAsInt() : 5;
        this.sink.sectionTitle_(levelAsInt);
    }

    @Override
    public void onMacro(String id, Map<String, String> parameters, String content, boolean inline)
    {
        // Don't do anything since macros have already been transformed so this method
        // should not be called.
    }

    @Override
    public void onNewLine()
    {
        // TODO: Decide when to generate a line break and when to generate a new line

        // Since there's no On NewLine event in Doxia we simply generate text
        this.sink.text("\n");
    }

    @Override
    public void onSpace()
    {
        // Since there's no On Space event in Doxia we simply generate text
        this.sink.text(" ");
    }

    @Override
    public void onSpecialSymbol(char symbol)
    {
        // Since there's no On Special Symbol event in Doxia we simply generate text
        this.sink.text(String.valueOf(symbol));
    }

    @Override
    public void onWord(String word)
    {
        this.sink.text(word);
    }

    @Override
    public void onId(String name)
    {
        // TODO: Find out what to do...
    }

    @Override
    public void onRawText(String text, Syntax syntax)
    {
        // TODO: Ensure this is correct. The problem is that Doxia doesn't seem to have a syntax
        // associated with the raw text so I'm not sure how the renderers (sink in Doxia language)
        // can decide whether to print it or not.
        this.sink.rawText(text);
    }

    @Override
    public void onHorizontalLine(Map<String, String> parameters)
    {
        // TODO: Handle parameters
        this.sink.horizontalRule();
    }

    @Override
    public void onEmptyLines(int count)
    {
        // TODO: Find what to do...
    }

    @Override
    public void beginDefinitionList(Map<String, String> parameters)
    {
        // TODO: Handle parameters
        this.sink.definitionList();
    }

    @Override
    public void endDefinitionList(Map<String, String> parameters)
    {
        // TODO: Handle parameters
        this.sink.definitionList_();
    }

    @Override
    public void beginDefinitionTerm()
    {
        this.sink.definedTerm();
    }

    @Override
    public void beginDefinitionDescription()
    {
        this.sink.definition();
    }

    @Override
    public void endDefinitionTerm()
    {
        this.sink.definedTerm_();
    }

    @Override
    public void endDefinitionDescription()
    {
        this.sink.definition_();
    }

    @Override
    public void beginQuotation(Map<String, String> parameters)
    {
        // TODO: Doxia doesn't seem to have support for quotation... Find out what to do...
    }

    @Override
    public void endQuotation(Map<String, String> parameters)
    {
        // TODO: Doxia doesn't seem to have support for quotation... Find out what to do...
    }

    @Override
    public void beginQuotationLine()
    {
        // TODO: Doxia doesn't seem to have support for quotation... Find out what to do...
    }

    @Override
    public void endQuotationLine()
    {
        // TODO: Doxia doesn't seem to have support for quotation... Find out what to do...
    }

    @Override
    public void beginTable(Map<String, String> parameters)
    {
        this.sink.table();
    }

    @Override
    public void beginTableCell(Map<String, String> parameters)
    {
        this.sink.tableCell();
    }

    @Override
    public void beginTableHeadCell(Map<String, String> parameters)
    {
        this.sink.tableHeaderCell();
    }

    @Override
    public void beginTableRow(Map<String, String> parameters)
    {
        this.sink.tableRow();
    }

    @Override
    public void endTable(Map<String, String> parameters)
    {
        this.sink.table_();
    }

    @Override
    public void endTableCell(Map<String, String> parameters)
    {
        this.sink.tableCell_();
    }

    @Override
    public void endTableHeadCell(Map<String, String> parameters)
    {
        this.sink.tableHeaderCell_();
    }

    @Override
    public void endTableRow(Map<String, String> parameters)
    {
        this.sink.tableRow_();
    }

    @Override
    public void beginLink(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        this.sink.link(reference.getReference());
    }

    @Override
    public void endLink(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        this.sink.link_();
    }

    @Override
    public void onImage(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        this.sink.figure();
        // TODO: handle special XWiki format for image locations. How do we pass image bits to Doxia?
        // TODO: Handle parameters
        // TODO: Handle free standing URI (if supported by Doxia)
        this.sink.figureGraphics(reference.getReference());
        this.sink.figure_();
    }

    @Override
    public void beginMetaData(MetaData metadata)
    {
        // Doxia doesn't support the notion of metadata
    }

    @Override
    public void endMetaData(MetaData metadata)
    {
        // Doxia doesn't support the notion of metadata
    }
}
