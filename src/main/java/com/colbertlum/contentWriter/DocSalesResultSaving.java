package com.colbertlum.contentWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.colbertlum.constants.DateTimePattern;
import com.colbertlum.entity.AutoCountOutputMoveOut;
import com.colbertlum.entity.AutoCountOutputResult;
import com.colbertlum.entity.BiztoryOutputMoveOut;
import com.colbertlum.entity.BiztoryOutputResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocSalesResultSaving {

    public static final String IRS_CASH_FILE_FORMAT = "irsSaleCashReport_";
    public static final String IRS_SPECIFY_FILE_FORMAT = "irsSpecifyDocNoReport_";

    public static final String IRS_TO_AUTO_COUNT_CASH_FILE_FORMAT = "AutoCountConvertedFromIrs_CashReport_";
    public static final String IRS_TO_AUTO_COUNT_SPCIFY_FILE_FORMAT = "AutoCountConvertedFromIrs_Specify_";

    public static final String FILE_SUFFIX = ".xlsx";

    public void savingToBiztory(File folder, BiztoryOutputResult result) {
        log.debug("start saving to biztory with dates of {}, to {}", result.cashDocLocalDates().size(), folder.getAbsolutePath());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        Set<LocalDate> cashDocLocalDates = result.cashDocLocalDates();
        for (LocalDate localDate : cashDocLocalDates) {
            String newDate = (folder.getAbsolutePath() + File.separator + IRS_CASH_FILE_FORMAT
                    + DateTimePattern.parseStringPathSafety(localDate) + FILE_SUFFIX);
            writeBiztoryFile(newDate, result.getCashDoc(localDate));
        }

        Set<LocalDate> specifyDocLocalDates = result.specifyDocLocalDates();
        for (LocalDate localDate : specifyDocLocalDates) {
            String newDate = (folder.getAbsolutePath() + File.separator + IRS_SPECIFY_FILE_FORMAT
                    + DateTimePattern.parseStringPathSafety(localDate) + FILE_SUFFIX);
            writeBiztoryFile(newDate, result.getAllSpecifyDocListByLocalDate(localDate));
        }
    }

    public void savingToAutoCount(File folder, AutoCountOutputResult result) {
        if (!folder.exists()) {
            folder.mkdirs();
        }
        Set<LocalDate> cashDocLocalDates = result.cashDocLocalDates();
        for (LocalDate localDate : cashDocLocalDates) {
            String newDate = (folder.getAbsolutePath() + File.separator + IRS_CASH_FILE_FORMAT
                    + DateTimePattern.parseStringPathSafety(localDate) + FILE_SUFFIX);
            writeAutoCountFile(newDate, result.getCashDoc(localDate));
        }

        Set<LocalDate> specifyDocLocalDates = result.specifyDocLocalDates();
        for (LocalDate localDate : specifyDocLocalDates) {
            Set<String> docNoKeySet = result.getSpecifyDocNoKeySet(localDate);
            for (String docNo : docNoKeySet) {
                String newDate = (folder.getAbsolutePath() + File.separator +
                        IRS_SPECIFY_FILE_FORMAT + DateTimePattern.parseStringPathSafety(localDate) + " " + docNo
                        + FILE_SUFFIX);

                writeAutoCountFile(newDate, result.getSpecifyMoveOuts(localDate, docNo));
            }
        }
    }

    private void writeBiztoryFile(String newDateFileString, List<BiztoryOutputMoveOut> moveOuts) {
        XSSFWorkbook workbook = null;
        try {
            File file = new File(newDateFileString);

            System.out.println("try to write file with path : " + file.getAbsolutePath() + " : " + newDateFileString);

            if (!file.exists())
                file.createNewFile();

            workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("sheet1");

            ContentWriter<BiztoryOutputMoveOut> contentWriter = new ContentWriter<>(sheet,
                    new BiztoryOutputMoveOutMapper(), moveOuts);
            contentWriter.writeAll();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void writeAutoCountFile(String newDateFileString, List<AutoCountOutputMoveOut> moveOuts) {
        XSSFWorkbook workbook = null;
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(newDateFileString);

            if (!file.exists())
                file.createNewFile();

            workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("sheet1");

            ContentWriter<AutoCountOutputMoveOut> contentWriter = new ContentWriter<>(sheet,
                    new AutoCountOutputMoveOutMapper(), moveOuts);
            contentWriter.writeAll();
            fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
