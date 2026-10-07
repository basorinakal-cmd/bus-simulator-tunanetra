package com.bsr.id;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.CookieManager;
import android.view.Window;
import android.graphics.Color;

public class MainActivity extends Activity {
    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        web = new WebView(this);
        web.setBackgroundColor(Color.TRANSPARENT);
        setContentView(web);

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);
        s.setBuiltInZoomControls(false);
        s.setDisplayZoomControls(false);
        s.setSupportZoom(false);
        s.setMediaPlaybackRequiresUserGesture(true);

        CookieManager.getInstance().setAcceptCookie(true);
        web.setWebViewClient(new WebViewClient());
        web.setWebChromeClient(new WebChromeClient());

        if (savedInstanceState != null) {
            web.restoreState(savedInstanceState);
        } else {
            web.loadUrl("<!DOCTYPE html>\n<html lang=\"id\">\n<head>\n    <meta charset=\"UTF-8\">\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n    <title>Bus Simulator untuk Tunanetra</title>\n    <script src=\"https://cdn.tailwindcss.com\"></script>\n    <style>\n        /* Gaya khusus untuk memastikan tombol memenuhi area sentuh dan memiliki kontras tinggi */\n        body {\n            background-color: #1a202c; /* Latar belakang gelap untuk mengurangi silau */\n            color: #f7fafc;\n            touch-action: manipulation; /* Mencegah zoom ganda pada sentuhan cepat */\n            overflow: hidden; /* Mencegah scrolling agar fokus pada tombol */\n        }\n        \n        #game-container {\n            display: flex;\n            flex-direction: column;\n            height: 100vh;\n            width: 100vw;\n        }\n\n        #info-panel {\n            flex: 1;\n            display: flex;\n            flex-direction: column;\n            justify-content: center;\n            align-items: center;\n            background-color: #2d3748;\n            padding: 1rem;\n            text-align: center;\n        }\n\n        #controls-panel {\n            flex: 2;\n            display: grid;\n            grid-template-columns: 1fr 1fr;\n            grid-template-rows: 1fr 1fr 1fr;\n            gap: 10px;\n            padding: 10px;\n            background-color: #000;\n        }\n\n        .control-btn {\n            background-color: #4a5568;\n            color: white;\n            font-size: 2rem;\n            font-weight: bold;\n            border: 4px solid #718096;\n            border-radius: 12px;\n            cursor: pointer;\n            transition: background-color 0.1s;\n            display: flex;\n            justify-content: center;\n            align-items: center;\n            user-select: none; /* Mencegah teks terpilih saat diketuk berulang */\n        }\n\n        .control-btn:active {\n            background-color: #cbd5e0;\n            color: #1a202c;\n            border-color: #e2e8f0;\n        }\n\n        /* Tombol Start khusus menutupi seluruh layar di awal */\n        #start-screen {\n            position: fixed;\n            top: 0;\n            left: 0;\n            width: 100vw;\n            height: 100vh;\n            background-color: #2b6cb0;\n            z-index: 50;\n            display: flex;\n            justify-content: center;\n            align-items: center;\n        }\n\n        #btn-start-game {\n            width: 80%;\n            height: 60%;\n            font-size: 3rem;\n            background-color: #ecc94b;\n            color: #744210;\n            border: none;\n            border-radius: 20px;\n            font-weight: 900;\n        }\n\n        /* Utils */\n        .hidden { display: none !important; }\n    </style>\n</head>\n<body>\n\n    <div id=\"start-screen\">\n        <button id=\"btn-start-game\">KETUK UNTUK MULAI<br><span style=\"font-size: 1.5rem; font-weight: normal;\">(Akan mengaktifkan Suara)</span></button>\n    </div>\n\n    <div id=\"game-container\" class=\"hidden\">\n        \n        <!-- Panel Informasi Status (Dibacakan saat berubah) -->\n        <div id=\"info-panel\">\n            <h1 class=\"text-3xl font-bold mb-4\">Status Bus</h1>\n            <p id=\"status-text\" class=\"text-2xl text-blue-300\">Mesin Mati</p>\n            <p id=\"speed-text\" class=\"text-xl mt-2\">Kecepatan: 0 km/jam</p>\n        </div>\n\n        <!-- Panel Kontrol (Grid 2 kolom) -->\n        <div id=\"controls-panel\">\n            <button id=\"btn-engine\" class=\"control-btn\" style=\"grid-column: span 2; background-color: #c53030;\">MESIN ON/OFF</button>\n            <button id=\"btn-left\" class=\"control-btn\">KIRI</button>\n            <button id=\"btn-right\" class=\"control-btn\">KANAN</button>\n            <button id=\"btn-brake\" class=\"control-btn\" style=\"background-color: #dd6b20;\">REM</button>\n            <button id=\"btn-gas\" class=\"control-btn\" style=\"background-color: #38a169;\">GAS</button>\n        </div>\n    </div>\n\n    <script>\n        // State Game\n        const gameState = {\n            engineOn: false,\n            speed: 0,\n            maxSpeed: 80,\n            direction: 'Tengah',\n            isStarted: false\n        };\n\n        // Elemen DOM\n        const startScreen = document.getElementById('start-screen');\n        const gameContainer = document.getElementById('game-container');\n        const statusText = document.getElementById('status-text');\n        const speedText = document.getElementById('speed-text');\n        \n        const btnStart = document.getElementById('btn-start-game');\n        const btnEngine = document.getElementById('btn-engine');\n        const btnGas = document.getElementById('btn-gas');\n        const btnBrake = document.getElementById('btn-brake');\n        const btnLeft = document.getElementById('btn-left');\n        const btnRight = document.getElementById('btn-right');\n\n        const synth = window.speechSynthesis;\n        let voiceID = null;\n\n        function initVoice() {\n            // Coba cari suara bahasa Indonesia\n            const voices = synth.getVo");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (web != null) web.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (true && web != null && web.canGoBack()) {
            web.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (web != null) {
            web.loadUrl("about:blank");
            web.stopLoading();
            web.destroy();
        }
        super.onDestroy();
    }
}
