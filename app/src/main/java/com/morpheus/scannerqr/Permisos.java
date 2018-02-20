package com.morpheus.scannerqr;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by Morpheus on 19/02/2018.
 */

public class Permisos extends Activity
{
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;
    private Activity activity;

    public Permisos(Activity activity)
    {
        this.activity = activity;
    }

    //Checa si los permisos que necesita la aplicacion están habilitados
    public void checarPermisos()
    {
        //Checa la version del SO, si su API es menor a la 23, termina el metodo (debido a que no requiere de permisos en tiempo real)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
        {
            //Manda un mensaje de que el permiso fue dado
            //Toast.makeText(activity, "El permiso ha sido concedido", Toast.LENGTH_SHORT).show();
        }

        //Le hace una recomendación al usuario
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA))
        {
            AlertDialog.Builder dialogo = new AlertDialog.Builder(activity);
            dialogo.setTitle("Permiso desactivado");
            dialogo.setMessage("Debe aceptar el permiso para el correcto funcionamiento de la app");

            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                }
            });

            dialogo.show();
        }
        else
        {
            //Requiere el permiso
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        }
    }

    //Los resultados de los requerimientos de los permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //El permiso se ha dado
                    //Toast.makeText(activity, "El permiso ha sido dado", Toast.LENGTH_SHORT).show();
                }
                //Genera el permiso de manera Manual
                else
                {
                    final CharSequence opciones[] = {"Si", "No"};
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle("¿Desea configurar los permisos de manera manual?");
                    builder.setItems(opciones, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            if (opciones[i].equals("Si"))
                            {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(activity, "Los permisos no fueron aceptados", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            }
                        }
                    });

                    builder.show();
                }
                break;
        }
    }
}
