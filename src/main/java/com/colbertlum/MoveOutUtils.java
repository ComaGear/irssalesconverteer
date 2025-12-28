package com.colbertlum;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.colbertlum.contentHandler.IrsSalesReportContentHandler;
import com.colbertlum.contentHandler.IrsSalesReportWithDocIdContentHandler;
import com.colbertlum.entity.Doc;
import com.colbertlum.entity.MoveOut;
import com.colbertlum.entity.MoveOutDocResults;
import com.colbertlum.entity.UnsableItem;

public class MoveOutUtils {
    
    public static MoveOutDocResults loadMoveOutsWithDoc(File file) {
        // String sourFileString = IrsSalesConverterApplication.getProperty(IrsSalesConverterApplication.SOURCE_FILE);

        List<Doc> docList = new ArrayList<Doc>();
        List<MoveOut> moveOuts = new ArrayList<MoveOut>();
        try {
            File reportFile = file;
            XSSFReader xssfReader = new XSSFReader(OPCPackage.open(reportFile));
            IrsSalesReportWithDocIdContentHandler contentHandler = new IrsSalesReportWithDocIdContentHandler(xssfReader.getSharedStringsTable(), xssfReader.getStylesTable(), docList, moveOuts);
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

        return new MoveOutDocResults(docList, moveOuts);
    }

    public static List<MoveOut> premapping(List<MoveOut> preMoveOuts) {

        UnUsableItemMapper unUsableItemMapper = new UnUsableItemMapper();

        ArrayList<MoveOut> toRemove = new ArrayList<MoveOut>();
        ArrayList<MoveOut> toAdd = new ArrayList<MoveOut>();
        for(MoveOut moveOut : preMoveOuts){
            List<UnsableItem> foundItems = unUsableItemMapper.findItems(moveOut.getProductId());

            if(foundItems == null) continue;

            for(UnsableItem unsableItem : foundItems){
                MoveOut newMoveOut = new MoveOut();
                newMoveOut.setProductId(unsableItem.getToUseId());
                newMoveOut.setProductName(moveOut.getProductName());
                newMoveOut.setQuantity(moveOut.getQuantity() * unsableItem.getMeasurement());
                if(unsableItem.getUnitPrice() > 0){
                    double pricePercent = unsableItem.getUnitPrice() / unsableItem.getBundlePrice();
                    newMoveOut.setTotalAmount(moveOut.getTotalAmount() * pricePercent);
                } else {
                    newMoveOut.setTotalAmount(moveOut.getTotalAmount());
                }
                newMoveOut.setUom(moveOut.getUom());

                if(unsableItem.isReduceOriginUnitQuantity()){
                    MoveOut rankMoveOut = findMoveOut(preMoveOuts, unsableItem.getToUseId());
                    rankMoveOut.setQuantity(rankMoveOut.getQuantity() - newMoveOut.getQuantity());
                }

                toAdd.add(newMoveOut);
                toRemove.add(moveOut);
            }
        }
        preMoveOuts.removeAll(toRemove);
        preMoveOuts.addAll(toAdd);

        // preMoveOuts.sort(new Comparator<MoveOut>() {

        //     @Override
        //     public int compare(MoveOut o1, MoveOut o2) {
        //         o1
        //     }
        
        // });
        return preMoveOuts;
    }


    private static MoveOut findMoveOut(List<MoveOut> moveOuts, String id) {

        int lo = 0;
        int hi = moveOuts.size()-1;

        while(lo <= hi) {
            int mid = lo + (hi-lo) / 2;
            MoveOut moveOut = moveOuts.get(mid);
            int compareTo = moveOut.getProductId().compareTo(id);
            if(compareTo > 0) hi = mid-1; 
            else if(compareTo < 0) lo = mid+1;
            else{
                return moveOut;
            }
        }
        return null;
    }

}
