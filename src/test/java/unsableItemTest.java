import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import com.colbertlum.UnUsableItemMapper;
import com.colbertlum.contentHandler.UnUsableItemContentHandler;
import com.colbertlum.contentHandler.uomContentHandler;
import com.colbertlum.entity.MoveOut;
import com.colbertlum.entity.UOM;
import com.colbertlum.entity.UnsableItem;

public class unsableItemTest {

    Logger logger = Logger.getLogger(this.getClass().getName());
    
    @Test
    public void loadUnsableItems(){
        UnUsableItemMapper unUsableItemMapper = new UnUsableItemMapper();
        List<UnsableItem> items = unUsableItemMapper.findItems("A16");
        logger.log(Level.INFO, items.get(0).getUnsableId());
        logger.log(Level.INFO, items.get(0).getToUseId());
        assertTrue(items.size() > 0);
        assertTrue(items.get(0).getUnsableId().compareTo("A16") == 0);
    }

    @Test
    public void readingUnsableItemXlsx(){
        ArrayList<UnsableItem> arrayList = new ArrayList<UnsableItem>();
        
        try {
            File reportFile = new File(IrsSalesConverterApplication.getProperty(IrsSalesConverterApplication.UNSABLE_ITEM));
            XSSFReader xssfReader = new XSSFReader(OPCPackage.open(reportFile));
            UnUsableItemContentHandler contentHandler = new UnUsableItemContentHandler(xssfReader.getSharedStringsTable(), xssfReader.getStylesTable(), arrayList);
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

        assertTrue(!arrayList.isEmpty());
        for(UnsableItem item : arrayList){
            assertNotNull(item.getToUseId());
            assertNotNull(item.getUnsableId());
            assertNotNull(item.getMeasurement());
            assertTrue(item.getMeasurement() > 0);
        }
    }

    @Test
    public void premappingTest(){

        MoveOut moveOut = new MoveOut();
        moveOut.setProductId("A16");
        moveOut.setProductName("A16 with lid");
        moveOut.setQuantity(1.0);
        moveOut.setTotalAmount(1);
        moveOut.setUom("unit");
        ArrayList<MoveOut> arrayList = new ArrayList<MoveOut>();
        arrayList.add(moveOut);

        SalesConverter salesConverter = new SalesConverter();
        salesConverter.premapping(arrayList);
    }

    @Test
    public void convertWithPremappingTest(){

        MoveOut A16moveOut = new MoveOut();
        A16moveOut.setProductId("A16");
        A16moveOut.setProductName("A16 with lid");
        A16moveOut.setQuantity(1.0);
        A16moveOut.setTotalAmount(1);
        A16moveOut.setUom("unit");

        MoveOut lidMoveOut = new MoveOut();
        lidMoveOut.setProductId("9555660802033");
        lidMoveOut.setProductName("EC LID");
        lidMoveOut.setQuantity(1.0);
        lidMoveOut.setTotalAmount(0);
        lidMoveOut.setUom("unit");

        ArrayList<MoveOut> arrayList = new ArrayList<MoveOut>();
        arrayList.add(A16moveOut);
        arrayList.add(lidMoveOut);


        arrayList.sort(new Comparator<MoveOut>() {

            @Override
            public int compare(MoveOut o1, MoveOut o2) {
                return o1.getProductId().toLowerCase().compareTo(o2.getProductId().toLowerCase());
            }
            
        });

        ArrayList<UOM> UOMs = new ArrayList<UOM>();

        try {
            XSSFReader xssfReader = new XSSFReader(OPCPackage.open(IrsSalesConverterApplication.getProperty(IrsSalesConverterApplication.UOM_FILE)));
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
        salesConverter.convert(salesConverter.premapping(arrayList), UOMs);
    }

}
