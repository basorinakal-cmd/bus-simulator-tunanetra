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
import android.view.ViewGroup;
import android.util.Base64;

public class MainActivity extends Activity {
    private WebView web;

    // Ini jauh lebih aman dan bersih daripada menjejalkan semuanya langsung di dalam fungsi web.loadUrl()
    private static final String GAME_HTML = "<!DOCTYPE html>\n" +
            "<html lang=\"id\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>Bus Simulator untuk Tunanetra</title>\n" +
            "    <script src=\"https://cdn.tailwindcss.com\"></script>\n" +
            "    <style>\n" +
            "        /* Gaya khusus untuk memastikan tombol memenuhi area sentuh dan memiliki kontras tinggi */\n" +
            "        body {\n" +
            "            background-color: #1a202c; /* Latar belakang gelap untuk mengurangi silau */\n" +
            "            color: #f7fafc;\n" +
            "            touch-action: manipulation; /* Mencegah zoom ganda pada sentuhan cepat */\n" +
            "            overflow: hidden; /* Mencegah scrolling agar fokus pada tombol */\n" +
            "            margin: 0; padding: 0;\n" +
            "        }\n" +
            "        \n" +
            "        #game-container {\n" +
            "            display: flex;\n" +
            "            flex-direction: column;\n" +
            "            height: 100vh;\n" +
            "            width: 100vw;\n" +
            "        }\n" +
            "\n" +
            "        #info-panel {\n" +
            "            flex: 1;\n" +
            "            display: flex;\n" +
            "            flex-direction: column;\n" +
            "            justify-content: center;\n" +
            "            align-items: center;\n" +
            "            background-color: #2d3748;\n" +
            "            padding: 1rem;\n" +
            "            text-align: center;\n" +
            "        }\n" +
            "\n" +
            "        #controls-panel {\n" +
            "            flex: 2;\n" +
            "            display: grid;\n" +
            "            grid-template-columns: 1fr 1fr;\n" +
            "            grid-template-rows: 1fr 1fr 1fr;\n" +
            "            gap: 10px;\n" +
            "            padding: 10px;\n" +
            "            background-color: #000;\n" +
            "        }\n" +
            "\n" +
            "        .control-btn {\n" +
            "            background-color: #4a5568;\n" +
            "            color: white;\n" +
            "            font-size: 2rem;\n" +
            "            font-weight: bold;\n" +
            "            border: 4px solid #718096;\n" +
            "            border-radius: 12px;\n" +
            "            cursor: pointer;\n" +
            "            transition: background-color 0.1s;\n" +
            "            display: flex;\n" +
            "            justify-content: center;\n" +
            "            align-items: center;\n" +
            "            user-select: none; /* Mencegah teks terpilih saat diketuk berulang */\n" +
            "            -webkit-user-select: none;\n" + // Kompatibilitas WebView
            "            -webkit-tap-highlight-color: transparent;\n" + // Menghilangkan highlight kotak biru default android
            "        }\n" +
            "\n" +
            "        .control-btn:active {\n" +
            "            background-color: #cbd5e0;\n" +
            "            color: #1a202c;\n" +
            "            border-color: #e2e8f0;\n" +
            "        }\n" +
            "\n" +
            "        /* Tombol Start khusus menutupi seluruh layar di awal */\n" +
            "        #start-screen {\n" +
            "            position: fixed;\n" +
            "            top: 0;\n" +
            "            left: 0;\n" +
            "            width: 100vw;\n" +
            "            height: 100vh;\n" +
            "            background-color: #2b6cb0;\n" +
            "            z-index: 50;\n" +
            "            display: flex;\n" +
            "            justify-content: center;\n" +
            "            align-items: center;\n" +
            "        }\n" +
            "\n" +
            "        #btn-start-game {\n" +
            "            width: 80%;\n" +
            "            height: 60%;\n" +
            "            font-size: 3rem;\n" +
            "            background-color: #ecc94b;\n" +
            "            color: #744210;\n" +
            "            border: none;\n" +
            "            border-radius: 20px;\n" +
            "            font-weight: 900;\n" +
            "        }\n" +
            "\n" +
            "        /* Utils */\n" +
            "        .hidden { display: none !important; }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n" +
            "    <div id=\"start-screen\">\n" +
            "        <button id=\"btn-start-game\">KETUK UNTUK MULAI<br><span style=\"font-size: 1.5rem; font-weight: normal;\">(Akan mengaktifkan Suara)</span></button>\n" +
            "    </div>\n" +
            "\n" +
            "    <div id=\"game-container\" class=\"hidden\">\n" +
            "        \n" +
            "        <!-- Panel Informasi Status (Dibacakan saat berubah) -->\n" +
            "        <div id=\"info-panel\">\n" +
            "            <h1 class=\"text-3xl font-bold mb-4\">Status Bus</h1>\n" +
            "            <p id=\"status-text\" class=\"text-2xl text-blue-300\">Mesin Mati</p>\n" +
            "            <p id=\"speed-text\" class=\"text-xl mt-2\">Kecepatan: 0 km/jam</p>\n" +
            "        </div>\n" +
            "\n" +
            "        <!-- Panel Kontrol (Grid 2 kolom) -->\n" +
            "        <div id=\"controls-panel\">\n" +
            "            <button id=\"btn-engine\" class=\"control-btn\" style=\"grid-column: span 2; background-color: #c53030;\">MESIN ON/OFF</button>\n" +
            "            <button id=\"btn-left\" class=\"control-btn\">KIRI</button>\n" +
            "            <button id=\"btn-right\" class=\"control-btn\">KANAN</button>\n" +
            "            <button id=\"btn-brake\" class=\"control-btn\" style=\"background-color: #dd6b20;\">REM</button>\n" +
            "            <button id=\"btn-gas\" class=\"control-btn\" style=\"background-color: #38a169;\">GAS</button>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "\n" +
            "    <script>\n" +
            "        // State Game\n" +
            "        const gameState = {\n" +
            "            engineOn: false,\n" +
            "            speed: 0,\n" +
            "            maxSpeed: 80,\n" +
            "            direction: 'Tengah',\n" +
            "            isStarted: false\n" +
            "        };\n" +
            "\n" +
            "        // Elemen DOM\n" +
            "        const startScreen = document.getElementById('start-screen');\n" +
            "        const gameContainer = document.getElementById('game-container');\n" +
            "        const statusText = document.getElementById('status-text');\n" +
            "        const speedText = document.getElementById('speed-text');\n" +
            "        \n" +
            "        const btnStart = document.getElementById('btn-start-game');\n" +
            "        const btnEngine = document.getElementById('btn-engine');\n" +
            "        const btnGas = document.getElementById('btn-gas');\n" +
            "        const btnBrake = document.getElementById('btn-brake');\n" +
            "        const btnLeft = document.getElementById('btn-left');\n" +
            "        const btnRight = document.getElementById('btn-right');\n" +
            "\n" +
            "        const synth = window.speechSynthesis;\n" +
            "        let voiceID = null;\n" +
            "\n" +
            "        function initVoice() {\n" +
            "            // Coba cari suara bahasa Indonesia\n" +
            "            const voices = synth.getVoices();\n" +
            "            for(let i = 0; i < voices.length; i++) {\n" +
            "                if(voices[i].lang.includes('id') || voices[i].lang.includes('ID')) {\n" +
            "                    voiceID = voices[i];\n" +
            "                    break;\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        // Pastikan voice di-load (dibutuhkan beberapa browser)\n" +
            "        if (speechSynthesis.onvoiceschanged !== undefined) {\n" +
            "            speechSynthesis.onvoiceschanged = initVoice;\n" +
            "        }\n" +
            "\n" +
            "        function speak(text, interrupt = true) {\n" +
            "            if (interrupt) {\n" +
            "                synth.cancel(); // Hentikan ucapan sebelumnya agar responsif\n" +
            "            }\n" +
            "            \n" +
            "            const utterThis = new SpeechSynthesisUtterance(text);\n" +
            "            if (voiceID) utterThis.voice = voiceID;\n" +
            "            utterThis.rate = 1.2; // Sedikit lebih cepat agar instruksi cepat tersampaikan\n" +
            "            utterThis.pitch = 1;\n" +
            "            \n" +
            "            synth.speak(utterThis);\n" +
            "        }\n" +
            "\n" +
            "        // Menggunakan Web Audio API untuk efek suara sederhana karena kita tidak punya aset file audio luar\n" +
            "        const audioCtx = new (window.AudioContext || window.webkitAudioContext)();\n" +
            "        let engineOscillator = null;\n" +
            "\n" +
            "        function playEngineSound() {\n" +
            "            if (engineOscillator) return;\n" +
            "            \n" +
            "            engineOscillator = audioCtx.createOscillator();\n" +
            "            const gainNode = audioCtx.createGain();\n" +
            "            \n" +
            "            engineOscillator.type = 'square';\n" +
            "            // Frekuensi dasar mesin\n" +
            "            engineOscillator.frequency.value = 50 + (gameState.speed / 2); \n" +
            "            \n" +
            "            // Volume rendah\n" +
            "            gainNode.gain.value = 0.1;\n" +
            "            \n" +
            "            engineOscillator.connect(gainNode);\n" +
            "            gainNode.connect(audioCtx.destination);\n" +
            "            engineOscillator.start();\n" +
            "        }\n" +
            "\n" +
            "        function stopEngineSound() {\n" +
            "            if (engineOscillator) {\n" +
            "                engineOscillator.stop();\n" +
            "                engineOscillator.disconnect();\n" +
            "                engineOscillator = null;\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        function updateEnginePitch() {\n" +
            "            if (engineOscillator) {\n" +
            "                // Pitch naik seiring kecepatan\n" +
            "                engineOscillator.frequency.setValueAtTime(50 + (gameState.speed * 1.5), audioCtx.currentTime);\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        function playHornSound() {\n" +
            "            // Efek suara klakson sederhana\n" +
            "            const osc = audioCtx.createOscillator();\n" +
            "            const gainNode = audioCtx.createGain();\n" +
            "            \n" +
            "            osc.type = 'sawtooth';\n" +
            "            osc.frequency.setValueAtTime(400, audioCtx.currentTime); // Nada klakson\n" +
            "            osc.frequency.setValueAtTime(410, audioCtx.currentTime + 0.1);\n" +
            "            \n" +
            "            gainNode.gain.setValueAtTime(0.3, audioCtx.currentTime);\n" +
            "            gainNode.gain.exponentialRampToValueAtTime(0.01, audioCtx.currentTime + 0.5);\n" +
            "            \n" +
            "            osc.connect(gainNode);\n" +
            "            gainNode.connect(audioCtx.destination);\n" +
            "            osc.start();\n" +
            "            osc.stop(audioCtx.currentTime + 0.5);\n" +
            "        }\n" +
            "\n" +
            "        function updateUI() {\n" +
            "            speedText.innerText = `Kecepatan: ${Math.round(gameState.speed)} km/jam`;\n" +
            "            \n" +
            "            if (gameState.engineOn) {\n" +
            "                statusText.innerText = `Mesin Menyala. Arah: ${gameState.direction}`;\n" +
            "                statusText.className = \"text-2xl text-green-400\";\n" +
            "                btnEngine.style.backgroundColor = \"#2f855a\"; // Hijau saat menyala\n" +
            "                btnEngine.innerText = \"MATIKAN MESIN\";\n" +
            "            } else {\n" +
            "                statusText.innerText = \"Mesin Mati\";\n" +
            "                statusText.className = \"text-2xl text-red-400\";\n" +
            "                btnEngine.style.backgroundColor = \"#c53030\"; // Merah saat mati\n" +
            "                btnEngine.innerText = \"NYALAKAN MESIN\";\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "\n" +
            "        // Tombol Mulai (Wajib untuk mengaktifkan Web Audio & Speech di browser modern)\n" +
            "        btnStart.addEventListener('click', () => {\n" +
            "            // Resume audio context jika suspended (kebijakan autoplay browser)\n" +
            "            if (audioCtx.state === 'suspended') {\n" +
            "                audioCtx.resume();\n" +
            "            }\n" +
            "            \n" +
            "            initVoice();\n" +
            "            startScreen.classList.add('hidden');\n" +
            "            gameContainer.classList.remove('hidden');\n" +
            "            gameState.isStarted = true;\n" +
            "            \n" +
            "            speak(\"Selamat datang di Simulator Bus. Layar terbagi menjadi tombol-tombol besar. Atas untuk mesin, tengah kiri kanan untuk kemudi, bawah kiri untuk rem, bawah kanan untuk gas.\");\n" +
            "        });\n" +
            "\n" +
            "        // Tombol Mesin\n" +
            "        btnEngine.addEventListener('click', () => {\n" +
            "            if (!gameState.engineOn) {\n" +
            "                gameState.engineOn = true;\n" +
            "                playEngineSound();\n" +
            "                speak(\"Mesin bus dihidupkan.\");\n" +
            "            } else {\n" +
            "                gameState.engineOn = false;\n" +
            "                gameState.speed = 0;\n" +
            "                stopEngineSound();\n" +
            "                speak(\"Mesin bus dimatikan.\");\n" +
            "            }\n" +
            "            updateUI();\n" +
            "        });\n" +
            "\n" +
            "        // Tombol Gas\n" +
            "        // Menggunakan mousedown/touchstart untuk simulasi menekan pedal\n" +
            "        let gasInterval;\n" +
            "        const pressGas = (e) => {\n" +
            "            e.preventDefault(); // Mencegah aksi default (seperti scrolling/highlighting)\n" +
            "            if (!gameState.engineOn) {\n" +
            "                speak(\"Nyalakan mesin terlebih dahulu.\");\n" +
            "                return;\n" +
            "            }\n" +
            "            \n" +
            "            speak(\"Gas\", false);\n" +
            "            \n" +
            "            gasInterval = setInterval(() => {\n" +
            "                if (gameState.speed < gameState.maxSpeed) {\n" +
            "                    gameState.speed += 1;\n" +
            "                    updateUI();\n" +
            "                    updateEnginePitch();\n" +
            "                    \n" +
            "                    // Baca kecepatan setiap kelipatan 10\n" +
            "                    if (Math.round(gameState.speed) % 10 === 0 && Math.round(gameState.speed) > 0) {\n" +
            "                        speak(`${Math.round(gameState.speed)} kilometer per jam`, false);\n" +
            "                    }\n" +
            "                }\n" +
            "            }, 100);\n" +
            "        };\n" +
            "        const releaseGas = (e) => {\n" +
            "            e.preventDefault();\n" +
            "            clearInterval(gasInterval);\n" +
            "            if(gameState.engineOn && gameState.speed > 0) speak(\"Lepas Gas\", false);\n" +
            "        };\n" +
            "\n" +
            "        btnGas.addEventListener('mousedown', pressGas);\n" +
            "        btnGas.addEventListener('touchstart', pressGas, {passive: false}); // Tambahan passive: false untuk WebView\n" +
            "        btnGas.addEventListener('mouseup', releaseGas);\n" +
            "        btnGas.addEventListener('touchend', releaseGas);\n" +
            "        btnGas.addEventListener('mouseleave', releaseGas);\n" +
            "\n" +
            "        // Tombol Rem\n" +
            "        let brakeInterval;\n" +
            "        const pressBrake = (e) => {\n" +
            "            e.preventDefault();\n" +
            "            if (gameState.speed === 0) return;\n" +
            "            \n" +
            "            speak(\"Mengerem\", false);\n" +
            "            \n" +
            "            brakeInterval = setInterval(() => {\n" +
            "                if (gameState.speed > 0) {\n" +
            "                    gameState.speed -= 2; // Rem lebih kuat dari gas\n" +
            "                    if (gameState.speed <= 0) {\n" +
            "                        gameState.speed = 0;\n" +
            "                        clearInterval(brakeInterval);\n" +
            "                        speak(\"Bus berhenti.\");\n" +
            "                    }\n" +
            "                    updateUI();\n" +
            "                    updateEnginePitch();\n" +
            "                }\n" +
            "            }, 100);\n" +
            "        };\n" +
            "        const releaseBrake = (e) => {\n" +
            "            e.preventDefault();\n" +
            "            clearInterval(brakeInterval);\n" +
            "        };\n" +
            "\n" +
            "        btnBrake.addEventListener('mousedown', pressBrake);\n" +
            "        btnBrake.addEventListener('touchstart', pressBrake, {passive: false});\n" +
            "        btnBrake.addEventListener('mouseup', releaseBrake);\n" +
            "        btnBrake.addEventListener('touchend', releaseBrake);\n" +
            "        btnBrake.addEventListener('mouseleave', releaseBrake);\n" +
            "\n" +
            "        // Tombol Belok Kiri\n" +
            "        btnLeft.addEventListener('touchstart', (e) => {\n" +
            "            e.preventDefault();\n" +
            "            if (!gameState.engineOn) return;\n" +
            "            \n" +
            "            gameState.direction = 'Kiri';\n" +
            "            updateUI();\n" +
            "            \n" +
            "            // Beri umpan balik suara dan klakson kecil\n" +
            "            speak(\"Belok Kiri\");\n" +
            "            playHornSound();\n" +
            "            \n" +
            "            // Kembalikan ke tengah setelah beberapa saat\n" +
            "            setTimeout(() => {\n" +
            "                gameState.direction = 'Tengah';\n" +
            "                updateUI();\n" +
            "            }, 2000);\n" +
            "}, {passive: false}); // Menggunakan touchstart agar responsif di Android WebView\n" +
            "\n" +
            "        // Tombol Belok Kanan\n" +
            "        btnRight.addEventListener('touchstart', (e) => {\n" +
            "            e.preventDefault();\n" +
            "            if (!gameState.engineOn) return;\n" +
            "            \n" +
            "            gameState.direction = 'Kanan';\n" +
            "            updateUI();\n" +
            "            \n" +
            "            speak(\"Belok Kanan\");\n" +
            "            playHornSound();\n" +
            "            \n" +
            "            setTimeout(() => {\n" +
            "                gameState.direction = 'Tengah';\n" +
            "                updateUI();\n" +
            "            }, 2000);\n" +
            "        }, {passive: false});\n" +
            "\n" +
            "        // Loop simulasi dasar (kehilangan kecepatan karena gesekan jika tidak digas)\n" +
            "        setInterval(() => {\n" +
            "            if (gameState.engineOn && !gasInterval && !brakeInterval && gameState.speed > 0) {\n" +
            "                gameState.speed -= 0.1; // Gesekan alami\n" +
            "                if (gameState.speed < 0) gameState.speed = 0;\n" +
            "                updateUI();\n" +
            "                updateEnginePitch();\n" +
            "            }\n" +
            "        }, 100);\n" +
            "\n" +
            "    </script>\n" +
            "</body>\n" +
            "</html>";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        web = new WebView(this);
        web.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.MATCH_PARENT
        ));
        setContentView(web);

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);
        s.setBuiltInZoomControls(false); // Menonaktifkan zoom sangat penting untuk game sentuh
        s.setDisplayZoomControls(false);
        s.setSupportZoom(false);
        
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);

        // PENTING: Memungkinkan audio diputar tanpa klik dari pengguna (bergantung pada versi Android)
        // Meskipun di HTML kita sudah akali dengan layar start.
        s.setMediaPlaybackRequiresUserGesture(false); 

        CookieManager.getInstance().setAcceptCookie(true);
        web.setWebViewClient(new WebViewClient());
        web.setWebChromeClient(new WebChromeClient());

        if (savedInstanceState != null) {
            web.restoreState(savedInstanceState);
        } else {
            String encodedHtml = Base64.encodeToString(GAME_HTML.getBytes(), Base64.NO_WRAP);
            web.loadData(encodedHtml, "text/html", "base64");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (web != null) web.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (web != null && web.canGoBack()) {
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