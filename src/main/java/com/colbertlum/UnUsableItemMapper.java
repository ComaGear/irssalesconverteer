package com.colbertlum;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.colbertlum.contentHandler.UnUsableItemContentHandler;
import com.colbertlum.entity.UnsableItem;

public class UnUsableItemMapper {

    private List<UnsableItem> unsableItems;

    public UnsableItem findItem(String unsableItemId){
        
        int lo = 0;
        int hi = unsableItems.size() -1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;

            UnsableItem unsableItem = unsableItems.get(mid);
            int compareTo = unsableItemId.toLowerCase().compareTo(unsableItem.getUnsableId().toLowerCase());
            if(compareTo > 0) hi = mid-1; 
            else if(compareTo < 0) lo = mid+1;
            else{
                return unsableItem;
            }
        }
        return null;
    }

    public List<UnsableItem> findItems(String unsableItemId){
        
        unsableItems.sort(new Comparator<UnsableItem>() {

            @Override
            public int compare(UnsableItem o1, UnsableItem o2) {
                return o1.getUnsableId().toLowerCase().compareTo(o2.getUnsableId().toLowerCase());
            }
            
        });

        ArrayList<UnsableItem> foundItems = new ArrayList<UnsableItem>();
        for(UnsableItem item : unsableItems){
            if(item.getUnsableId().toLowerCase().compareTo(unsableItemId.toLowerCase()) == 0){
                foundItems.add(item);
            }
        }

        return foundItems;
    }

    public UnUsableItemMapper(){
        unsableItems = new ArrayList<UnsableItem>();
        
        try {
            File file = new File(IrsSalesConverterApplication.getProperty(IrsSalesConverterApplication.UNSABLE_ITEM));
            XSSFReader xssfReader = new XSSFReader(OPCPackage.open(file));
            UnUsableItemContentHandler contentHandler = new UnUsableItemContentHandler(xssfReader.getSharedStringsTable(), xssfReader.getStylesTable(), unsableItems);
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
    }
}
