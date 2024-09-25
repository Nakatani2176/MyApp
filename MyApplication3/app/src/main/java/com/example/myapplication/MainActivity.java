package com.example.myapplication;

import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityMainBinding;

import android.os.ParcelFileDescriptor;
import android.widget.ImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {
            ImageView imageView = findViewById(R.id.pdf);
            PdfViewer pdfViewer = new PdfViewer();
            pdfViewer.displayPdf(getResources(), imageView, R.raw.testpdf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class PdfViewer {

        public void displayPdf(Resources resources, ImageView imageView, int pdfResourceId) throws IOException {
            // 1. PDF を Bitmap に変換
            Bitmap bitmap = getBitmapFromPdf(resources, pdfResourceId);

            // 2. Bitmap を ImageView に設定
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }

        private Bitmap getBitmapFromPdf(Resources resources, int pdfResourceId) throws IOException {
            AssetFileDescriptor afd = resources.openRawResourceFd(pdfResourceId);;
            if (afd != null) {
                ParcelFileDescriptor fileDescriptor = afd.getParcelFileDescriptor();
                if (fileDescriptor != null) {
                    PdfRenderer renderer = new PdfRenderer(fileDescriptor);
                    PdfRenderer.Page page = renderer.openPage(0); // 最初のページを表示
                    Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                    page.close();
                    renderer.close();
                    fileDescriptor.close();
                    return bitmap;
                }
            }
            return null;
        }
    }
}