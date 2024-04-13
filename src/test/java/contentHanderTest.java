import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.junit.jupiter.api.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.colbertlum.IrsSalesConverterApplication;
import com.colbertlum.SalesConverter;
import com.colbertlum.contentHandler.IrsSalesReportContentHandler;
import com.colbertlum.contentHandler.UnUsableItemContentHandler;
import com.colbertlum.contentHandler.uomContentHandler;
import com.colbertlum.entity.MoveOut;
import com.colbertlum.entity.UOM;
import com.colbertlum.entity.UnsableItem;

public class contentHanderTest {
    
    @Test
    public void salesConverterTest(){
        ArrayList<MoveOut> moveOuts = new ArrayList<MoveOut>();

        try {
            File reportFile = new File("C:\\Users\\comag\\Downloads\\rptProductSalesListingSummary11 (1).xlsx");
            XSSFReader xssfReader = new XSSFReader(OPCPackage.open(reportFile));
            IrsSalesReportContentHandler contentHandler = new IrsSalesReportContentHandler(xssfReader.getSharedStringsTable(), xssfReader.getStylesTable(), moveOuts);
            XMLReader XMLReader = XMLHelper.newXMLReader();
            XMLReader.setContentHandler(contentHandler);
            InputSource inputSource = new InputSource(xssfReader.getSheetsData().next());
            XMLReader.parse(inputSource);

        } catch (IOException | OpenXML4JException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        
        File uomFile = new File("C:\\Users\\comag\\Downloads\\EXPORT_ITEM.XLSX");
        ArrayList<UOM> UOMs = new ArrayList<UOM>();

        try {
            XSSFReader xssfReader = new XSSFReader(OPCPackage.open(uomFile));
            uomContentHandler contentHandler = new uomContentHandler(xssfReader.getSharedStringsTable(), xssfReader.getStylesTable(), UOMs);
            XMLReader XMLReader = XMLHelper.newXMLReader();
            XMLReader.setContentHandler(contentHandler);
            InputSource inputSource = new InputSource(xssfReader.getSheetsData().next());
            XMLReader.parse(inputSource);

        } catch (IOException | OpenXML4JException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SalesConverter salesConverter = new SalesConverter();
        salesConverter.convert(moveOuts, UOMs);
        System.out.println(moveOuts);
    }
}
