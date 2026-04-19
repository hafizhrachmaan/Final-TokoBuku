package com.tokobuku.nitnot.service;

import com.tokobuku.nitnot.model.Transaction;
import com.tokobuku.nitnot.model.TransactionDetail;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class PdfService {

    public ByteArrayInputStream generateInvoicePdf(Transaction transaction) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A7.rotate(), 10, 10, 20, 10); // Smaller page size, like a real receipt

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // === Font Definitions ===
            Font storeNameFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.BLACK);
            Font addressFont = FontFactory.getFont(FontFactory.HELVETICA, 8, Color.DARK_GRAY);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            Font thankYouFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, Color.GRAY);

            // === Header Section ===
            Paragraph storeName = new Paragraph("NITNOT TOKO BUKU", storeNameFont);
            storeName.setAlignment(Element.ALIGN_CENTER);
            document.add(storeName);

            Paragraph storeAddress = new Paragraph("Jl. Raya Teknologi No. 1, Surabaya\nTelp: (031) 123-4567", addressFont);
            storeAddress.setAlignment(Element.ALIGN_CENTER);
            document.add(storeAddress);
            
            document.add(new Paragraph("------------------------------------------------------------------", bodyFont));

            // === Transaction Info ===
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setWidths(new float[]{1, 3});
            infoTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            infoTable.addCell(new Paragraph("No. Struk:", bodyFont));
            infoTable.addCell(new Paragraph(String.valueOf(transaction.getId()), bodyFont));
            infoTable.addCell(new Paragraph("Tanggal:", bodyFont));
            infoTable.addCell(new Paragraph(transaction.getTransactionDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), bodyFont));
            infoTable.addCell(new Paragraph("Kasir:", bodyFont));
            infoTable.addCell(new Paragraph(transaction.getUser().getUsername(), bodyFont));
            document.add(infoTable);
            
            document.add(new Paragraph("------------------------------------------------------------------", bodyFont));


            // === Items Table ===
            PdfPTable itemsTable = new PdfPTable(4);
            itemsTable.setWidthPercentage(100);
            itemsTable.setWidths(new float[]{4, 1, 3, 3});
            itemsTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            itemsTable.getDefaultCell().setPaddingBottom(4);
            
            // --- Table Header ---
            addHeaderCell(itemsTable, "Item", Element.ALIGN_LEFT, headerFont);
            addHeaderCell(itemsTable, "Qty", Element.ALIGN_CENTER, headerFont);
            addHeaderCell(itemsTable, "Harga", Element.ALIGN_RIGHT, headerFont);
            addHeaderCell(itemsTable, "Subtotal", Element.ALIGN_RIGHT, headerFont);
            
            // --- Table Body ---
            Locale idLocale = new Locale("in", "ID");
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(idLocale);

            for (TransactionDetail detail : transaction.getDetails()) {
                addBodyCell(itemsTable, detail.getProduct().getName(), Element.ALIGN_LEFT, bodyFont);
                addBodyCell(itemsTable, String.valueOf(detail.getQuantity()), Element.ALIGN_CENTER, bodyFont);
                addBodyCell(itemsTable, formatAmount(detail.getPrice()), Element.ALIGN_RIGHT, bodyFont);
                addBodyCell(itemsTable, formatAmount(detail.getPrice() * detail.getQuantity()), Element.ALIGN_RIGHT, bodyFont);
            }
            document.add(itemsTable);
            
            document.add(new Paragraph("------------------------------------------------------------------", bodyFont));
            
            // === Total Section ===
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(100);
            totalTable.setWidths(new float[]{7, 3});
            totalTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            addBodyCell(totalTable, "GRAND TOTAL", Element.ALIGN_RIGHT, totalFont);
            addBodyCell(totalTable, formatAmount(transaction.getTotalPrice()), Element.ALIGN_RIGHT, totalFont);
            document.add(totalTable);
            
            // === Footer Section ===
            document.add(Chunk.NEWLINE);
            Paragraph thankYou = new Paragraph("Terima kasih atas kunjungan Anda!", thankYouFont);
            thankYou.setAlignment(Element.ALIGN_CENTER);
            document.add(thankYou);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addHeaderCell(PdfPTable table, String text, int align, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(align);
        cell.setPaddingBottom(8);
        table.addCell(cell);
    }
    
    private void addBodyCell(PdfPTable table, String text, int align, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(align);
        table.addCell(cell);
    }

    private String formatAmount(double amount) {
        // Use a simpler format that works for Indonesian currency, without decimals.
        return String.format("%,.0f", amount);
    }
}
