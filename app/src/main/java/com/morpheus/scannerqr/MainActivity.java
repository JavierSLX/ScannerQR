package com.morpheus.scannerqr;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler, View.OnClickListener
{
    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Checa permisos
        new Permisos(this).checarPermisos();
        referenciarElementos();
    }

    //El resultado del escaneo
    @Override
    public void handleResult(Result result)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        setContentView(R.layout.activity_main);
        referenciarElementos();

        String dato = result.getText();
        scannerView.stopCamera();

        builder.setTitle("Resultado del escaner");
        builder.setMessage("El resultado es " + dato);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });
        builder.create().show();

        TextView txtCodigo = (TextView) findViewById(R.id.txtCodigo);
        txtCodigo.setText(dato);
        Log.i("codigo", dato);

        //Reinicia scanner
        scannerView.resumeCameraPreview(this);
    }

    @Override
    public void onClick(View view)
    {
        //Inicializa la vista
        scannerView = new ZXingScannerView(this);

        //Manda el resultado
        scannerView.setResultHandler(this);
        setContentView(scannerView);
        scannerView.startCamera();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        scannerView.stopCamera();
    }

    private void referenciarElementos()
    {
        Button btEscaner = (Button)findViewById(R.id.btEscaner);
        btEscaner.setOnClickListener(this);
    }
}
