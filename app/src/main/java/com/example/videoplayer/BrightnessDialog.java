package com.example.videoplayer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class BrightnessDialog extends AppCompatDialogFragment {
    private TextView brightness_no;
    private ImageView cross;
    SeekBar seekBar;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflator=getActivity().getLayoutInflater();
        View view=inflator.inflate(R.layout.brt_dialog_item,null);
        builder.setView(view);
        cross=view.findViewById(R.id.brt_close);
        brightness_no=view.findViewById(R.id.brt_number);
        seekBar=view.findViewById(R.id.seekbar);
        int brightness= Settings.System.getInt(getContext().getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS,0);
        brightness_no.setText(brightness+"");
        seekBar.setProgress(brightness);
        seekBar.setMax(255);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                Context context=getContext().getApplicationContext();
                boolean canWrite=Settings.System.canWrite(context);
                if(canWrite)
                {
                    int sBrightness=progress*255/255;
                    brightness_no.setText(sBrightness+"");
                    Settings.System.putInt(context.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS_MODE,
                            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                    Settings.System.putInt(context.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS,sBrightness);
                }
                else//If Write settings are not enabled, navigate the user to settings
                {
                    Toast.makeText(context, "Enable write settings for brightness control", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.setData(Uri.parse("package:"+context.getPackageName()));
                    startActivityForResult(intent,0 );

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return builder.create();
    }
}
