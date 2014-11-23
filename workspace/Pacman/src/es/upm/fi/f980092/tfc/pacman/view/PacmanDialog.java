/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adrián Ángeles Ramón
 * Universidad: Politécnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 */
package es.upm.fi.f980092.tfc.pacman.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class PacmanDialog {
	
	public static AlertDialog buildInfoDialog(Context context, int title, int texto) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(texto)
		       .setCancelable(true)
		       .setTitle(title)
		       .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.cancel();
		           }
		       });
		
		return builder.create();
	}
	
	public static AlertDialog buildFatalErrorDialog(Context context, Activity owner, int title, int texto) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(texto)
		       .setCancelable(true)
		       .setTitle(title)
		       .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.cancel();
		        	   ((AlertDialog) dialog).getOwnerActivity().finish();
		           }
		       });
		
		return builder.create();
	}
}
