package com.colbertlum;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.colbertlum.entity.MoveOut;

public class IrsSalesReportContentHandler extends DefaultHandler {

    enum dataType {
        NUMBER, SSTINDEX,
    }

    private static final String ID = "Item No";
    private static final String UOM = "UOM";
    private static final String QUANTITY = "Qty";

    private ArrayList<MoveOut> moveOuts;
    private StylesTable stylesTable;
    private SharedStrings sharedStringsTable;
    private Map<String, Integer> headerPosition;
    private boolean isValue;
    private dataType readingVDataType;
    private int formatIndex;
    private String formatString;
    private int readingRow = 0;
    private MoveOut moveOut;
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
                    break;
                case SSTINDEX:
                    String sstIndex = value.toString();
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
                        headerPosition.put(columString, 0);
                        break;
                    case UOM:
                        headerPosition.put(columString, 1);
                        break;
                    case QUANTITY:
                        headerPosition.put(columString, 2);
                        break;
                }
                return;
            }

            Integer integer = -1;
            if (headerPosition.containsKey(columString))
                integer = headerPosition.get(columString);
            switch (integer) {
                case 0:
                    moveOut.setProductId(string);
                    break;
                case 1:
                    moveOut.setUom(string);
                    break;
                case 2:
                    moveOut.setQuantity(Float.parseFloat(string));
                    break;
                default:
                    break;
            }
            return;
        }

        if ("row".equals(qName)) {
            if (readingRow > 0)
                if(moveOut.getProductId() != null) moveOuts.add(moveOut);
            this.readingRow += 1;
            this.moveOut = null;
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
            this.moveOut = new MoveOut();
        }
    }

    public IrsSalesReportContentHandler(SharedStrings sharedStrings, StylesTable stylesTable,
            ArrayList<MoveOut> moveOuts) {
        this.sharedStringsTable = sharedStrings;
        this.stylesTable = stylesTable;
        this.moveOuts = moveOuts;

        this.dataFormatter = new DataFormatter();
        headerPosition = new HashMap<String, Integer>();
    }
}
