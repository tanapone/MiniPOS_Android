package com.miniproject.minipos.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.miniproject.minipos.R;

import static xdroid.toaster.Toaster.toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class Cashier extends Fragment implements View.OnClickListener{

    private String barcodeResult;
    ImageButton searchButton;
    public Cashier() {
        // Required empty public constructor
    }

    public void scanBarcode(){
        IntentIntegrator.forSupportFragment(this).initiateScan();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cashier, container, false);
        searchButton = (ImageButton) v.findViewById(R.id.searchButton);



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cashier, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getActivity(),"Canceled scan",Toast.LENGTH_SHORT).show();
            } else {
                barcodeResult = result.getContents();
                toast(barcodeResult);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (getView().getId()){
            case R.id.searchButton :
                scanBarcode();
                break;
        }
    }
}
