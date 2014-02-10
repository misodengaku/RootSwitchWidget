package com.misodengaku.rootswitchwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;
import android.app.PendingIntent;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;

public class SwitchWidgetProvider extends AppWidgetProvider {

    final String btn1Filter = "com.misodengaku.rootswitchwidget.BUTTON_CLICKED";


	@Override
	public void onEnabled(Context context) {
		Log.v("SwitchWidget", "onEnabled");
		super.onEnabled(context);
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		Log.v("SwitchWidget", "onUpdate");
		super.onUpdate(context, appWidgetManager, appWidgetIds);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);
        // BUTTON1
        Intent btn1Intent = new Intent(btn1Filter);
        PendingIntent btn1Pending = PendingIntent.getBroadcast(context, 0, btn1Intent, 0);
        views.setOnClickPendingIntent(R.id.btn1_id, btn1Pending);
        appWidgetManager.updateAppWidget(appWidgetIds, views);
	}
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		Log.v("SwitchWidget", "onDeleted");
		super.onDeleted(context, appWidgetIds);
	}
	
	@Override
	public void onDisabled(Context context) {
		Log.v("SwitchWidget", "onDisabled");
		super.onDisabled(context);
	}

	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v("SwitchWidget", "onReceive");
		super.onReceive(context, intent);




        String action = intent.getAction();
        if (action.equals(btn1Filter)) {
            Log.v("Widget02", "Button1 Clicked");

            boolean isRooted;
            try {
                Process process = Runtime.getRuntime().exec("su");
                process.destroy();
                isRooted = true;
                Log.v("SwitchWidget", "su is available.");
            } catch (IOException e) {
                //e.printStackTrace();
                Log.v("SwitchWidget", "su is not found.");
                isRooted = false;
            }

            if (isRooted)
            {
                try {
                    Process p = Runtime.getRuntime().exec("su");
                    DataOutputStream dos = new DataOutputStream(p.getOutputStream());
                    dos.writeBytes("mount -o rw,remount /system\n"); // 押す
                    dos.writeBytes("mv /system/xbin/su /system/xbin/backupsu\n");
                    dos.writeBytes("mount -o ro,remount /system\n"); // 押す
                    Toast.makeText(context, "temp unrootしました", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "temp unrootに失敗しました", Toast.LENGTH_LONG).show();
                }
            }else
            {
                try {
                    Process p = Runtime.getRuntime().exec("backupsu");
                    DataOutputStream dos = new DataOutputStream(p.getOutputStream());
                    dos.writeBytes("mount -o rw,remount /system\n"); // 押す
                    dos.writeBytes("mv /system/xbin/backupsu /system/xbin/su\n");
                    dos.writeBytes("mount -o ro,remount /system\n"); // 押す
                    Toast.makeText(context, "rootを取得可能にしました", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "suのインストール失敗", Toast.LENGTH_LONG).show();

                }

            }
        }
	}
}
