package uk.co.savant.barry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import com.google.zxing.qrcode.QRCodeWriter;

import uk.co.savant.barry.database.ScannedProducts;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_tropos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_tropos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_tropos extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private int pageNo;
    private View view;


    public fragment_tropos() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_tropos.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_tropos newInstance(String param1, String param2) {
        fragment_tropos fragment = new fragment_tropos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tropos, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setView(View view) {
        this.view = view;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

//    public void execute() {
//
//        if ((!(null == ScannedProducts.getAlScannedProducts())) && (!ScannedProducts.getAlScannedProducts().isEmpty())) {
//
//            setPageNo(0);
//            drawNextPage();
//
//        }
//
//
//    }

//    public void drawNextPage() {
//
//        ScannedProducts.ScannedProductRecord scannedProductRecord = (ScannedProducts.ScannedProductRecord) ScannedProducts.getAlScannedProducts().get(getPageNo());
//        buildAndWriteQRCodeString(scannedProductRecord);
//
//        if (getPageNo() < ScannedProducts.getAlScannedProducts().size() - 1) {
//            setPageNo(getPageNo() + 1);
//        }
//
//    }


//    Data Matrix barcode will contain :-
//    Donation ID (first 13 characters not including the = data identifier)
//    Plus The type of product ( W, R, P, U)
//    Plus <tab>
//    Plus Date the product was drawn ( format DD/MM/YY
//    Plus one space
//    Plus Short product description ( 5 characters )
//    Plus one space
//    Plus the product code ( 5 characters)

//    private void buildAndWriteQRCodeString(ScannedProducts.ScannedProductRecord productRecord) {
//        String donationID = productRecord.getDonationNumber();
//
//        String productType = productRecord.getProductType();
//        String tab = "\t";
//        String drawDate = productRecord.getDrawdate();
//        String productDescription = productRecord.getProductDescription();
//        String productCode = productRecord.getProductCode();
//
//        String qrCodeText = donationID + productType + tab + drawDate + " " + productDescription + " " + productCode;
//
//        writeQRCode(qrCodeText);
//
//    }

//    public void commitRecord() {
//        ScannedProducts.ScannedProductRecord scannedProductRecord = (ScannedProducts.ScannedProductRecord) ScannedProducts.getAlScannedProducts().get(getPageNo());
//
//        // Save this record somewhere
//        String donationID = scannedProductRecord.getDonationNumber();
//        String productType = scannedProductRecord.getProductType();
//        String drawDate = scannedProductRecord.getDrawdate();
//        String productDescription = scannedProductRecord.getProductDescription();
//        String productCode = scannedProductRecord.getProductCode();
//
//    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public LayoutInflater getLayoutInflater(Bundle savedInstanceState) {
        return super.getLayoutInflater(savedInstanceState);
    }


//    private void writeQRCode(String text) {
//        try {
//
//            Bitmap qrCode = generateQRCode(text);
//
//            ImageView imageView = (ImageView) view.findViewById(R.id.imageViewQRCode);
//            imageView.setImageBitmap(qrCode);
//
//
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//    }


//    public static void writeQRCode(ImageView imageView) {
//        try {
//
//            Bitmap qrCode = generateQRCode("testing");
//
//            imageView.setImageBitmap(qrCode);
//
//
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//    }

//    private static Bitmap generateQRCode(String content) throws WriterException {
//        int qrCodeSize = 128;
//
//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//
//        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize);
//        int bmWidth = bitMatrix.getWidth();
//        int bmHeight = bitMatrix.getHeight();
//
//        // Need to convert the bitmatrix to a bitmap, which apparently means writing each bit individually.
//        Bitmap bitmap = Bitmap.createBitmap(qrCodeSize, qrCodeSize, Bitmap.Config.ARGB_8888);
//        for (int x = 0; x < bmWidth; x++) {
//            for (int y = 0; y < bmHeight; y++) {
//                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
//            }
//        }
//
//        return bitmap;
//    }

//    public int getPageNo() {
//        return pageNo;
//    }
//
//    public void setPageNo(int pageNo) {
//        this.pageNo = pageNo;
//    }
}

