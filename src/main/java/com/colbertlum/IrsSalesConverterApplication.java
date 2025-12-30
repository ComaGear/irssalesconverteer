package com.colbertlum;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.colbertlum.Utils.BiztoryOutputConverter;
import com.colbertlum.contentHandler.IrsSalesReportContentHandler;
import com.colbertlum.contentHandler.uomContentHandler;
import com.colbertlum.contentWriter.DocSalesResultSaving;
import com.colbertlum.entity.DocSalesConverterResult;
import com.colbertlum.entity.MoveOut;
import com.colbertlum.entity.UOM;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class IrsSalesConverterApplication extends Application {

    public static final String UOM_FILE = "uomfile";
    public static final String SOURCE_FILE = "sourcefile";
    public static final String IRS_SALES_ORIGIN_REPORT_NAME = "irsSalesOriginReport_";
    public static final String UNSABLE_ITEM = "unsable-item";
    private static String uomfile = "";
    private String pathname = "";
    private String outputPath = "";
    private String outputFileNameStr;

    private static Context context;

    // public static final String PRI_PATH = "C:\\Program Files\\ColbertLum\\irssalesconverter";

    @Override
    public void start(Stage priStage) throws Exception {

        try{
            Properties properties = getProperties();
            this.uomfile = properties.getProperty(UOM_FILE);
            this.pathname = properties.getProperty(SOURCE_FILE);
            this.outputPath = properties.getProperty("output.path");
        }catch(IOException exception){
            exception.printStackTrace();
        }
        Text reportPathText = new Text(pathname);
        Font font = new Font("monospace", 16);
        reportPathText.setFont(font);
        Text uomPathText = new Text(uomfile);
        uomPathText.setFont(font);
        Text unsableItemMappingPathText = new Text(IrsSalesConverterApplication.getProperty(UNSABLE_ITEM));
        unsableItemMappingPathText.setFont(font);

        priStage.setTitle("Irs Sales Report Converter");
        priStage.setWidth(600);
        priStage.setHeight(400);

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("excel File", "*.xlsx"));
        // fileChooser.setInitialDirectory(new File(pathname));

        Button selectReportButton = new Button("select Report");
        selectReportButton.setOnAction(e -> {
            File report = fileChooser.showOpenDialog(priStage);
            reportPathText.setText(report.getPath());
            pathname = report.getPath();
            saveProperty(SOURCE_FILE, report.getPath());
        });

        Button selectuomButton = new Button("select uom file");
        selectuomButton.setOnAction(e -> {
            File uomFile = fileChooser.showOpenDialog(priStage);
            uomfile = uomFile.getPath();
            uomPathText.setText(uomFile.getPath());
            saveProperty(UOM_FILE, uomFile.getPath());
        });

        Button selectUnsableItemMappingButton = new Button("select item mapping");
        selectUnsableItemMappingButton.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(priStage);
            saveProperty(UNSABLE_ITEM, file.getPath());
            unsableItemMappingPathText.setText(file.getPath());
        });

        LocalDate now = LocalDate.now();
        String date = now.getYear() + "." + now.getMonthValue() + "." + now.getDayOfMonth();
        TextField outputFileName = new TextField(IRS_SALES_ORIGIN_REPORT_NAME + date);
        outputFileNameStr = outputFileName.getText();
        outputFileName.setOnAction(e ->{
            this.outputFileNameStr =  outputFileName.getText();
        });

        Text errorTextLabel = new Text("error item No:");
        TextArea textArea = new TextArea();
        textArea.setDisable(true);

        Text toExcludeDocListText = new Text("above is to generating e-invoice Doc No List (Seperate by ',')");
        TextArea toExcludeDocTextArea = new TextArea();
        
        Button processButton = new Button("generate");
        processButton.setOnAction(e ->{
            DocSalesConverter docSalesConverter = new DocSalesConverter();
            docSalesConverter.setToExcludeDocList(parseExcludeDoc(toExcludeDocTextArea));
            DocSalesConverterResult result = docSalesConverter.process(new File(pathname));

            DocSalesResultSaving saving = new DocSalesResultSaving();
            saving.savingToBiztory(biztoryFolder, BiztoryOutputConverter.converting(result));
            saving.savingToAutoCount(AutoCountFolder, AutoCountOutputConverter.converting(result));
            saveResult into folder by seperate each date.
            cash into single cash.xlsx
            excluded doc into seperate {DocNo}.xlsx
            // SalesConverter converter = process(pathname);

            // this.outputFileNameStr = outputFileName.getText();
            // saveOutput(converter.getResult());

            textArea.setText(converter.getUnfoundMoveOuts().toString());
            textArea.setDisable(false);
        });


        HBox reportHBox = new HBox(selectReportButton, reportPathText);
        HBox uomHBox = new HBox(selectuomButton, uomPathText);
        HBox mappingBox = new HBox(selectUnsableItemMappingButton, unsableItemMappingPathText);
        VBox errorBox = new VBox(processButton, errorTextLabel, textArea);
        VBox toExcludeDocBox = new VBox(toExcludeDocListText, toExcludeDocTextArea);

        VBox vbox = new VBox(reportHBox, uomHBox, mappingBox, outputFileName, errorBox, toExcludeDocBox);
        Scene scene = new Scene(vbox, 600, 300);

        priStage.setScene(scene);
        priStage.show();
    }
    


    private List<String> parseExcludeDoc(TextArea toExcludeDocTextArea) {
        String text = toExcludeDocTextArea.getText();
        List<String> result = Arrays.asList(text.split(",")).stream()
        .filter(s -> !s.trim().isEmpty())
        .collect(Collectors.toList());
        return result;
    }



    public static void main(String[] args) {

        Application.launch(args);
    }

    private SalesConverter process(String reportPath){

        ArrayList<MoveOut> moveOuts = new ArrayList<MoveOut>();

        try {
            File reportFile = new File(reportPath);
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

        List<UOM> UOMs = getContext().getUom();

        SalesConverter converter = new SalesConverter();

        // add preprocess for un-usable item mapping to validate item.
        converter.convert(converter.premapping(moveOuts));

        return converter;
    }

    private void saveOutput(List<MoveOut> result) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("sheet1");
        

        int rowCount = 0;
        XSSFRow headerRow = sheet.createRow(rowCount++);
        headerRow.createCell(0).setCellValue("Code");
        headerRow.createCell(1).setCellValue("Description");
        headerRow.createCell(2).setCellValue("Qty");
        headerRow.createCell(3).setCellValue("unit");
        headerRow.createCell(4).setCellValue("Unit Price");

        for(MoveOut moveOut : result){

            if(moveOut.getQuantity() == 0) continue; 

            XSSFRow row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(moveOut.getProductId());
            row.createCell(1).setCellValue(moveOut.getProductName());
            row.createCell(2).setCellValue(moveOut.getQuantity());
            row.createCell(3).setCellValue("");
            row.createCell(4).setCellValue(moveOut.getTotalAmount() / moveOut.getQuantity());
        }

        try{
            
            FileOutputStream fileOutputStream = new FileOutputStream(outputPath + "\\"+ outputFileNameStr + ".xlsx");
            // FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\comag\\Desktop\\" + outputFileNameStr + ".xlsx");
            workbook.write(fileOutputStream);

            fileOutputStream.close();
        }catch(IOException exception){
            exception.printStackTrace();
            System.out.println(exception.toString());
        }
    }

    public static Context getContext(){
        if(context == null) {
            context = loadContext();
        }


        return context;
    }

    private static Context loadContext() {

        context = new Context();

        File uomFile = new File(uomfile);
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

        context.setUom(UOMs);

        return context;
    }



    private static Properties getProperties() throws IOException{
        Properties properties;
        
        // InputStream inputStream = ClassLoader.getSystemResourceAsStream("/IrsSalesConverter.properties");
        InputStream inputStream = new FileInputStream("./IrsSalesConverter.properties");
        properties = new Properties();

        properties.load(inputStream);
        inputStream.close();

        return properties;
    }

    public static String getProperty(String key){
        try{
            Properties properties = getProperties();
            return properties.getProperty(key);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private static void saveProperty(String key, String value) {
        try{
            Properties properties = getProperties();
            properties.setProperty(key, value);
            saveProperties(properties);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    private static void saveProperties(Properties properties) throws IOException{
        FileOutputStream fileOutputStream = new FileOutputStream("./IrsSalesConverter.properties");
        properties.store(fileOutputStream, null);

        fileOutputStream.close();
    }
}