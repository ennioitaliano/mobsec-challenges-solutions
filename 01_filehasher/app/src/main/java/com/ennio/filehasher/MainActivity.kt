package com.ennio.filehasher

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.InputStream
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val receivedIntent = intent
        val action = receivedIntent.action
        val data: Uri? = receivedIntent.data
        val expectedAction = "com.mobiotsec.intent.action.HASHFILE"

        if (action == expectedAction && data != null) {
            Log.d("FileHasherApp", "Received data URI: $data")

            try {
                contentResolver.openInputStream(data)?.use { inputStream ->
                    val fileHash = Hasher.hash(inputStream)
                    Log.d("FileHasherApp", "Computed SHA-256 Hash: $fileHash")

                    val resultIntent = Intent()
                    resultIntent.putExtra("hash", fileHash)
                    setResult(Activity.RESULT_OK, resultIntent)
                }
            } catch (e: Exception) {
                Log.e("FileHasherApp", "Failed to read file content", e)
                setResult(Activity.RESULT_CANCELED)
            }
        } else {
            setResult(Activity.RESULT_CANCELED)
        }

        finish()
    }
}

class Hasher {
    companion object {
        fun hash(stream: InputStream): String {
            val bytes = stream.readBytes()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            return digest.fold("", { str, it -> str + "%02x".format(it) })
        }
    }
}