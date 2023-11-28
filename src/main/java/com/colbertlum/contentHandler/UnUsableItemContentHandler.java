package com.colbertlum.contentHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.colbertlum.entity.UnsableItem;

public class UnUsableItemContentHandler extends DefaultHandler {

    enum dataType {
        NUMBER, SSTINDEX,
    }

    private static final String ID = "NOT USED ID";
    private static final String USE_ID = "TO USE ID";
    private static final String MEASUREMENT = "MEASUREMENT";

    private List<UnsableItem> unsableItems;
    private StylesTable stylesTable;
    private SharedStrings sharedStringsTable;
    private Map<String, String> headerPosition;
    private boolean isValue;
    private dataType readingVDataType;
    private int formatIndex;
    private String formatString;
    private int readingRow = 0;
    private UnsableItem unsableItem;
    private String columString;
    private StringBuilder value;
    private DataFormatter dataFormatter;

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (value == null)
            this.value = new StringBuilder();

        if (isValue)
            value.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        String string = null;

        if ("v".equals(qName)) {
            switch (readingVDataType) {
                case NUMBER:
                    if (this.formatString == null)
                        string = value.toString();
                    else
                        string = dataFormatter.formatRawCellContents(Float.parseFloat(value.toString()),
                                this.formatIndex, this.formatString);
                    
                    if(string.contains("(") || string.contains(")")){
                        string = string.replace("(", "");
                        string = string.replace(")", "");
                        string = "-" + string;
                    }
                    string = string.replace(",", "");

                    break;
                case SSTINDEX:
                    // String sstIndex = value.toString();
                    String sstIndex = value.toString().replaceAll("\\D+","");
                    try {
                        RichTextString rts = sharedStringsTable.getItemAt(Integer.parseInt(sstIndex));
                        string = rts.toString();
                    } catch (NumberFormatException e) {
                    }
                    break;
                default:
                    string = "(TODO: Unexpected type: " + readingVDataType + ")";
                    break;
            }

            if (readingRow == 0 && string != null) {
                switch (string) {
                    case ID:
                        headerPosition.put(columString, ID);
                        break;
                    case USE_ID:
                        headerPosition.put(columString, USE_ID);
                        break;
                    case MEASUREMENT:
                        headerPosition.put(columString, MEASUREMENT);
                        break;
                }
                return;
            }


            String property = null;
            if (headerPosition.containsKey(columString))
                property = headerPosition.get(columString);
            switch (property) {
                case ID:
                    unsableItem.setUnsableId(string);
                    break;
                case USE_ID:
                    unsableItem.setToUseId(string);
                    break;
                case MEASUREMENT:
                    unsableItem.setMeasurement(Double.parseDouble(string));
                    break;
            }
        }

        if ("row".equals(qName)) {
            if (readingRow > 0)
                if(unsableItem.getToUseId() != null) unsableItems.add(unsableItem);
            this.readingRow += 1;
            this.unsableItem = null;
        }

        if (value != null && value.length() > 0)
            value.delete(0, value.length());

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {

        if ("v".equals(qName)) { // 'v' is a tag name that contains cell's value.
            isValue = true;
            return;
        }

        if ("c".equals(qName)) { // 'c' is tag name parent node of 'v'. this is cell itself.

            int firstDigit = 0;
            String references = attributes.getValue("r"); // 'r' is reference like A1, C3.
            for (int i = 0; i < references.length(); i++) {
                if (Character.isDigit(references.charAt(i))) {
                    firstDigit = i;
                    break;
                }
            }
            // this.columnPosition = columnReferenceToPosition(references.substring(0, firstDigit));
            this.columString = references.substring(0, firstDigit);

            readingVDataType = dataType.NUMBER;
            this.formatIndex = -1;
            this.formatString = null;
            String cellType = attributes.getValue("t");
            String cellStyleString = attributes.getValue("s");

            if ("s".equals(cellType)) {
                this.readingVDataType = dataType.SSTINDEX;
                return;
            }
            if (cellStyleString != null) {
                XSSFCellStyle style = stylesTable.getStyleAt(Integer.parseInt(cellStyleString));
                this.formatString = style.getDataFormatString();
                this.formatIndex = style.getDataFormat();
                if (this.formatString == null)
                    this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
                return;
            }

        }
        if ("row".equals(qName)) {
            if (readingRow == 0)
                return;
            this.unsableItem = new UnsableItem();
        }
    }

    // private int columnReferenceToPosition(String reference) {
    //     int column = -1;
    //     for (int i = 0; i < reference.length(); ++i) {
    //         int c = reference.charAt(i);
    //         column = (column + 1) * 26 + c - 'A';
    //     }
    //     return column;
    // }


    public UnUsableItemContentHandler(SharedStrings sharedStrings, StylesTable stylesTable,
            List<UnsableItem> unsableItems) {
        this.sharedStringsTable = sharedStrings;
        this.stylesTable = stylesTable;
        this.unsableItems = unsableItems;

        this.dataFormatter = new DataFormatter();
        headerPosition = new HashMap<String, String>();
    }

}
