package com.example.firebaseauthentication;

import android.content.Context;
import android.view.Gravity;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

public class Loader {

    DialogPlus dialog;
    Context context;

    public Loader(Context context) {
        this.context = context;

        dialog = DialogPlus.newDialog(context)
                .setContentBackgroundResource(R.color.transparent)
                .setContentHolder(new ViewHolder(R.layout.loader))
                .setGravity(Gravity.CENTER)
                .setMargin(100,0,100,0)
                .setCancelable(false)
                .create();

    }

    public void show(){
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }
}
