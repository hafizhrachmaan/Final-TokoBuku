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

            // === Font Definitions (slightly smaller for receipt feel) ===
            Font storeNameFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLACK);
            Font addressFont = FontFactory.getFont(FontFactory.HELVETICA, 7, Color.DARK_GRAY);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 7);
            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
            Font thankYouFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 7, Color.GRAY);

            // === Header Section ===
            Paragraph storeName = new Paragraph("NITNOT TOKO BUKU", storeNameFont);
            storeName.setAlignment(Element.ALIGN_CENTER);
            document.add(storeName);

            Paragraph storeAddress = new Paragraph("Jl. Raya Teknologi No. 1, Surabaya\nTelp: (031) 123-4567", addressFont);
            storeAddress.setAlignment(Element.ALIGN_CENTER);
            document.add(storeAddress);
            
            // A single, clean separator
            Paragraph separator = new Paragraph("------------------------------------------------------------------", bodyFont);
            separator.setSpacingBefore(5);
            separator.setSpacingAfter(5);
            document.add(separator);

            // === Transaction Info ===
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setWidths(new float[]{1.5f, 4}); // Adjust column ratio
            infoTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            addBodyCell(infoTable, "No. Struk:", Element.ALIGN_LEFT, bodyFont);
            addBodyCell(infoTable, String.valueOf(transaction.getId()), Element.ALIGN_LEFT, bodyFont);
            addBodyCell(infoTable, "Tanggal:", Element.ALIGN_LEFT, bodyFont);
            addBodyCell(infoTable, transaction.getTransactionDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), Element.ALIGN_LEFT, bodyFont);
            addBodyCell(infoTable, "Kasir:", Element.ALIGN_LEFT, bodyFont);
            addBodyCell(infoTable, transaction.getUser().getUsername(), Element.ALIGN_LEFT, bodyFont);
            document.add(infoTable);

            // === Items Table ===
            PdfPTable itemsTable = new PdfPTable(4);
            itemsTable.setWidthPercentage(100);
            itemsTable.setWidths(new float[]{4, 1, 2.5f, 2.5f}); // Adjust column ratio
            itemsTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            itemsTable.getDefaultCell().setPaddingBottom(4);
            itemsTable.setSpacingBefore(10);
            
            // --- Table Header ---
            addHeaderCell(itemsTable, "Item", Element.ALIGN_LEFT, headerFont);
            addHeaderCell(itemsTable, "Qty", Element.ALIGN_CENTER, headerFont);
            addHeaderCell(itemsTable, "Harga", Element.ALIGN_RIGHT, headerFont);
            addHeaderCell(itemsTable, "Subtotal", Element.ALIGN_RIGHT, headerFont);

            for (TransactionDetail detail : transaction.getDetails()) {
                addBodyCell(itemsTable, detail.getProduct().getName(), Element.ALIGN_LEFT, bodyFont);
                addBodyCell(itemsTable, String.valueOf(detail.getQuantity()), Element.ALIGN_CENTER, bodyFont);
                addBodyCell(itemsTable, formatAmount(detail.getPrice()), Element.ALIGN_RIGHT, bodyFont);
                addBodyCell(itemsTable, formatAmount(detail.getPrice() * detail.getQuantity()), Element.ALIGN_RIGHT, bodyFont);
            }
            document.add(itemsTable);
            
            // === Footer Section (Reordered as per user request) ===
            
            // Thank you message first
            Paragraph thankYou = new Paragraph("Terima kasih atas kunjungan Anda!", thankYouFont);
            thankYou.setAlignment(Element.ALIGN_CENTER);
            thankYou.setSpacingBefore(15);
            document.add(thankYou);
            
            // Total section last
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(100);
            totalTable.setWidths(new float[]{6, 4}); // Adjust column ratio
            totalTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            totalTable.setSpacingBefore(5);

            addBodyCell(totalTable, "GRAND TOTAL", Element.ALIGN_RIGHT, totalFont);
            addBodyCell(totalTable, formatAmount(transaction.getTotalPrice()), Element.ALIGN_RIGHT, totalFont);
            document.add(totalTable);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addHeaderCell(PdfPTable table, String text, int align, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.BOTTOM); // Add a bottom border to the header
        cell.setBorderColor(Color.LIGHT_GRAY);
        cell.setHorizontalAlignment(align);
        cell.setPaddingBottom(5);
        table.addCell(cell);
    }
    
    private void addBodyCell(PdfPTable table, String text, int align, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(align);
        cell.setPaddingTop(2);
        cell.setPaddingBottom(2);
        table.addCell(cell);
    }

    private String formatAmount(double amount) {
        // Use a simpler format that works for Indonesian currency, without decimals.
        return String.format("%,.0f", amount);
    }
}
