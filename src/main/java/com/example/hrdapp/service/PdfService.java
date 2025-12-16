package com.example.hrdapp.service;

import com.example.hrdapp.model.Transaction;
import com.example.hrdapp.model.TransactionDetail;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    public ByteArrayInputStream generateInvoicePdf(Transaction transaction) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            // Title
            Paragraph title = new Paragraph("Struk Belanja Toko Buku", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Transaction Info
            document.add(new Paragraph("ID Transaksi: " + transaction.getId(), bodyFont));
            document.add(new Paragraph("Tanggal: " + transaction.getTransactionDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), bodyFont));
            document.add(new Paragraph("Kasir: " + transaction.getUser().getUsername(), bodyFont));
            document.add(Chunk.NEWLINE);

            // Table for items
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3, 1, 2, 2});

            // Table Header
            PdfPCell cell = new PdfPCell(new Paragraph("Item", headerFont));
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Qty", headerFont));
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Harga", headerFont));
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Subtotal", headerFont));
            table.addCell(cell);

            // Table Body
            for (TransactionDetail detail : transaction.getDetails()) {
                table.addCell(detail.getProduct().getName());
                table.addCell(String.valueOf(detail.getQuantity()));
                table.addCell("Rp " + String.format("%,.2f", detail.getPrice()));
                table.addCell("Rp " + String.format("%,.2f", detail.getPrice() * detail.getQuantity()));
            }
            document.add(table);

            // Total
            document.add(Chunk.NEWLINE);
            Paragraph total = new Paragraph("Total Belanja: Rp " + String.format("%,.2f", transaction.getTotalPrice()), headerFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
