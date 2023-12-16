package com.example.myapplication1;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

public class numberpicker extends AlertDialog {

    private NumberPicker numberPicker;
    private OnValueChangeListener onValueChangeListener;

    protected numberpicker(Context context, int initialValue, OnValueChangeListener listener) {
        super(context);
        this.onValueChangeListener = listener;

        // Inflate layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.number_picker_dialog, null);
        setView(view);

        // Set up NumberPicker
        numberPicker = view.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(30);
        numberPicker.setValue(initialValue);

        setButton(BUTTON_POSITIVE, "Simpan", (dialog, which) -> {
            if (onValueChangeListener != null) {
                onValueChangeListener.onValueChanged(numberPicker.getValue());
            }
        });

        setButton(BUTTON_NEGATIVE, "Batal", (dialog, which) -> {
            // Tindakan ketika tombol "Cancel" ditekan
            cancel();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public interface OnValueChangeListener {
        void onValueChanged(int value);
    }
}
