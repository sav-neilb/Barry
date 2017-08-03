package uk.co.savant.barry;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.qrcode.QRCodeWriter;

import uk.co.savant.barry.database.BarryDatabase;
import uk.co.savant.barry.database.ScannedProducts;


class TroposScreen {
    private int recordNo;
    private View view;

    private BarryDatabase barryDatabase;
    private boolean bShowArchived = false;

    TroposScreen(View view) {
        this.setView(view);
        barryDatabase = null;
    }


    void drawNextPage() {

        writeFormData();

    }

    private void clearFormdata() {
        TextView textView = (TextView) view.findViewById(R.id.textViewDonationIdTropos);
        textView.setText("");

        textView = (TextView) view.findViewById(R.id.textViewProductNameTropos);
        textView.setText("");
        textView = (TextView) view.findViewById(R.id.textViewProductCodeTropos);
        textView.setText("");
        textView = (TextView) view.findViewById(R.id.textViewTypeCodeTropos);
        textView.setText("");
        textView = (TextView) view.findViewById(R.id.textViewProductTypeDescriptionTropos);
        textView.setText("");
        textView = (TextView) view.findViewById(R.id.textViewExpiryDateTropos);
        textView.setText("");
        textView = (TextView) view.findViewById(R.id.textViewDrawDateTropos);
        textView.setText("");

        writeQRCode("");
    }

    private void writeFormData() {
        ScannedProducts.ScannedProductRecord scannedProductRecord = null;
        int scannedProductsCount = barryDatabase.countScannedProducts();

        clearFormdata();

        if (scannedProductsCount > getRecordNo()) {
            setRecordNo(getRecordNo() + 1);
        } else {
            setRecordNo(1);
        }

        int counter = 0;
        boolean found = false;
        while (!found) {

            counter++;
            if (counter > scannedProductsCount) {  // Checked them all, not found one.
                break;
            }

            scannedProductRecord = barryDatabase.readScannedProductrecord(getRecordNo());


            if ((null != scannedProductRecord)) {

                if ((scannedProductRecord.isCommited()) && (!isbShowArchived())) {
                    scannedProductRecord = null;
                    setRecordNo(getRecordNo() + 1);
                    if (getRecordNo() > scannedProductsCount) {
                        setRecordNo(1);
                    }
                } else {
                    found = true;
                }
            } else {
                if (counter == scannedProductsCount) {  // Checked them all, not found one. Shouldn't get here, actually.
                    break;
                }

            }
        }

        if (found) {

            Button button = (Button) view.findViewById(R.id.buttonCommit);

            if (scannedProductRecord.isCommited()) {
                button.setBackgroundResource(R.drawable.button_shape_rounded_grey);

                button.setText("Uncommit");

            } else {
                button.setBackgroundResource(R.drawable.button_shape_rounded_green);

                button.setText("Commit");

            }

            String nOfn = getRecordNo() + " of " + scannedProductsCount;

            TextView textView = (TextView) view.findViewById(R.id.textViewCountInSequence);
            textView.setText(nOfn);

            String donationID = scannedProductRecord.getDonationNumber();

            String strPaddedDntno = DonationNumberValidation.padDonationNumber(donationID);

            String productType = scannedProductRecord.getProductType();
            String expiryDate = scannedProductRecord.getExpiryDate();
            String drawDate = scannedProductRecord.getDrawdate();
            String productName = scannedProductRecord.getProductDescription();
            String productCode = scannedProductRecord.getProductCode();
            String ProductTypeDescription = scannedProductRecord.getProductTypeDescription();

            textView = (TextView) view.findViewById(R.id.textViewDonationIdTropos);
            textView.setText(strPaddedDntno);

            textView = (TextView) view.findViewById(R.id.textViewProductNameTropos);
            textView.setText(productName);
            textView = (TextView) view.findViewById(R.id.textViewProductCodeTropos);
            textView.setText(productCode);
            textView = (TextView) view.findViewById(R.id.textViewTypeCodeTropos);
            textView.setText(productType);
            textView = (TextView) view.findViewById(R.id.textViewProductTypeDescriptionTropos);
            textView.setText(ProductTypeDescription);
            textView = (TextView) view.findViewById(R.id.textViewExpiryDateTropos);
            textView.setText(expiryDate);
            textView = (TextView) view.findViewById(R.id.textViewDrawDateTropos);
            textView.setText(drawDate);

            //    Data Matrix barcode will contain :-
            //    Donation ID (first 13 characters not including the = data identifier)
            //    Plus The type of product ( W, R, P, U)
            //    Plus <tab>
            //    Plus Date the product was drawn ( format DD/MM/YY
            //    Plus one space
            //    Plus Short product description ( 5 characters )
            //    Plus one space
            //    Plus the product code ( 5 characters)
            drawDate = drawDate.substring(0, 6) + drawDate.substring(8, 10);  // Drop the century
            productCode = productCode + "     ";
            productCode = productCode.substring(0, 5);

            String TAB = "\t";
            String qrCodeText = donationID.substring(0, 13) + productType + TAB + drawDate + " " + productName.substring(0, 5) + " " + productCode +
                    getStrCodeEndChar();


            writeQRCode(qrCodeText);

        } else {
            Button button = (Button) view.findViewById(R.id.buttonCommit);
            button.setBackgroundColor(view.getResources().getColor(R.color.colorHolo_grey));

            TextView textView = (TextView) view.findViewById(R.id.textViewCountInSequence);
            textView.setText("0 of 0");
        }
    }

    void commitRecord() {

        barryDatabase.updateRecordToCommitted(getRecordNo());

    }


    private void writeQRCode(String text) {
        Bitmap qrCode = null;
        try {

            if (!text.isEmpty()) {
                qrCode = generateQRCode(text);
//                qrCode = generateDataMatrix(text);
            }

            ImageView imageView = (ImageView) view.findViewById(R.id.imageViewQRCode);
            imageView.setImageBitmap(qrCode);


        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


    private static Bitmap generateQRCode(String content) throws WriterException {
        int qrCodeSize = 128;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize);


        // As the matrix is defined above as square (qrCodeSize x qrCodeSize), these will be the same as 'qrCodeSize'
        int bmWidth = bitMatrix.getWidth();
        int bmHeight = bitMatrix.getHeight();

        // Need to convert the bitmatrix to a bitmap, which apparently means writing each bit individually.
        Bitmap bitmap = Bitmap.createBitmap(qrCodeSize, qrCodeSize, Bitmap.Config.ARGB_8888);
        for (int x = 0; x < bmWidth; x++) {
            for (int y = 0; y < bmHeight; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }

        return bitmap;
    }

    private static Bitmap generateDataMatrix(String content) throws WriterException {
        int requestedWidth = 128;
        int requestedHeight = 128;

        DataMatrixWriter qrCodeWriter = new DataMatrixWriter();

        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.DATA_MATRIX, requestedWidth, requestedHeight);


        // As the matrix is defined above as square (qrCodeSize x qrCodeSize), these will be the same as 'qrCodeSize'
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();


        // calculating the scaling factor
        int pixelsize = requestedWidth/width;
        if (pixelsize > requestedHeight/height)
        {
            pixelsize = requestedHeight/height;
        }

        int[] pixels = new int[requestedWidth * requestedHeight];

        // All are 0, or black, by default
        for (int y = 0; y < height; y++) {
            int offset = y * requestedWidth * pixelsize;

            // scaling pixel height
            for (int pixelsizeHeight = 0; pixelsizeHeight < pixelsize; pixelsizeHeight++, offset+=requestedWidth) {
                for (int x = 0; x < width; x++) {
                    int color = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;

                    // scaling pixel width
                    for (int pixelsizeWidth = 0; pixelsizeWidth < pixelsize; pixelsizeWidth++) {
                        pixels[offset + x * pixelsize + pixelsizeWidth] = color;
                    }
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(requestedWidth, requestedHeight, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, requestedWidth, 0, 0, requestedWidth, requestedHeight);


        return bitmap;
    }

    private int getRecordNo() {
        return recordNo;
    }

    void setRecordNo(int recordNo) {
        this.recordNo = recordNo;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    private String getStrCodeEndChar() {
        String strCodeEndChar = "";
        return strCodeEndChar;
    }

//    public void setStrCodeEndChar(String strCodeEndChar) {
//        this.strCodeEndChar = strCodeEndChar;
//    }

    void setBarryDatabase(BarryDatabase barryDatabase) {
        this.barryDatabase = barryDatabase;
    }

    boolean isbShowArchived() {
        return bShowArchived;
    }

    void setbShowArchived(boolean bShowArchived) {
        this.bShowArchived = bShowArchived;
    }

    void unCommitRecord() {

        barryDatabase.updateRecordToUnCommitted(getRecordNo());

    }

//    public void deleteOldRecords(int iKeepDays) {
//        barryDatabase.deleteBeforeDate(iKeepDays);
//    }
}

