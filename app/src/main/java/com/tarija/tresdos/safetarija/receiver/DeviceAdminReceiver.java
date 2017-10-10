package com.tarija.tresdos.safetarija.receiver;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.valdesekamdem.library.mdtoast.MDToast;

public class DeviceAdminReceiver extends android.app.admin.DeviceAdminReceiver {

	@Override
	public void onDisabled(Context context, Intent intent) {
		// TODO Auto-generated method stub
		MDToast mdToast = MDToast.makeText(context, "Funcion Desactivada", MDToast.LENGTH_SHORT, MDToast.TYPE_INFO);
		mdToast.show();
		super.onDisabled(context, intent);
	}

	@Override
	public void onEnabled(Context context, Intent intent) {
		// TODO Auto-generated method stub
		MDToast mdToast = MDToast.makeText(context, "Funcion Activada", MDToast.LENGTH_SHORT, MDToast.TYPE_INFO);
		mdToast.show();
		super.onEnabled(context, intent);
	}

	@Override
	public CharSequence onDisableRequested(Context context, Intent intent) {
		// TODO Auto-generated method stub
		MDToast mdToast = MDToast.makeText(context, "DPM ha sido desactivado", MDToast.LENGTH_SHORT, MDToast.TYPE_INFO);
		mdToast.show();
		return super.onDisableRequested(context, intent);
	}

}
