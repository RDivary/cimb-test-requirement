package com.divary.cimbtestrequirement.service.impl;

import com.divary.cimbtestrequirement.config.handler.exception.ErrorException;
import com.divary.cimbtestrequirement.model.TransactionHistory;
import com.divary.cimbtestrequirement.model.User;
import com.divary.cimbtestrequirement.service.ExcelGenerator;
import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Service
@Log4j2
public class ExcelGeneratorImpl implements ExcelGenerator {

    private static final String[] columns = {"user id", "username", "account number", "transaction id", "amount", "transaction code", "transaction name", "time"};

    @Override
    public byte[] generateTransactionHistory(User user) {

        DateFormat df = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");

        try {
            @Cleanup Workbook workbook = new XSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Sheet sheet = workbook.createSheet("Transaction History");
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowIdx = 1;
            for (TransactionHistory trxHistory : user.getTransactionHistories()) {
                Row row = sheet.createRow(rowIdx);

                row.createCell(0).setCellValue(user.getUserId());
                row.createCell(1).setCellValue(user.getUsername());
                row.createCell(2).setCellValue(user.getAccountNumber());
                row.createCell(3).setCellValue(trxHistory.getTransactionHistoryId());
                row.createCell(4).setCellValue(trxHistory.getAmount());
                row.createCell(5).setCellValue(trxHistory.getTransactionType().getTransactionCode());
                row.createCell(6).setCellValue(trxHistory.getTransactionType().getTransactionName());
                row.createCell(7).setCellValue(df.format(trxHistory.getActivityDate()));
                rowIdx++;
            }

            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            log.error("error with message {}", e.getMessage());
            throw new ErrorException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
