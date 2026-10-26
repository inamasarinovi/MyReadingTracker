
package com.example.myreadingtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyReadingTrackerThemeWrapper {
                ReadingTrackerScreen()
            }
        }
    }
}

@Composable
fun MyReadingTrackerThemeWrapper(content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}

@Composable
fun ReadingTrackerScreen() {
    var title by rememberSaveable { mutableStateOf("") }
    var author by rememberSaveable { mutableStateOf("") }
    var pagesRead by rememberSaveable { mutableIntStateOf(0) }
    var finished by rememberSaveable { mutableStateOf(false) }
    var feedback by rememberSaveable { mutableStateOf("") }

    val isValid = title.isNotBlank() && author.isNotBlank()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "My Reading Tracker",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Text("Catat buku dan progres membaca kamu.")

            HorizontalDivider()

            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    feedback = ""
                },
                label = { Text("Judul buku") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = author,
                onValueChange = {
                    author = it
                    feedback = ""
                },
                label = { Text("Nama penulis") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Text(
                text = "Halaman yang sudah dibaca",
                fontWeight = FontWeight.SemiBold
            )

            PageCounter(
                pagesRead = pagesRead,
                onDecrease = {
                    if (pagesRead > 0) pagesRead--
                },
                onIncrease = {
                    pagesRead++
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Sudah selesai dibaca?",
                        fontWeight = FontWeight.Medium
                    )
                    Text(if (finished) "Selesai" else "Sedang dibaca")
                }

                Switch(
                    checked = finished,
                    onCheckedChange = { finished = it }
                )
            }

            Text(
                text = if (finished) {
                    "Status: Buku selesai dibaca"
                } else {
                    "Progres: $pagesRead halaman"
                }
            )

            Button(
                onClick = {
                    feedback = "Buku \"$title\" berhasil disimpan!"
                },
                enabled = isValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simpan buku")
            }

            OutlinedButton(
                onClick = {
                    title = ""
                    author = ""
                    pagesRead = 0
                    finished = false
                    feedback = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reset")
            }

            if (!isValid) {
                Text(
                    "Isi judul dan nama penulis agar bisa menyimpan buku.",
                    color = MaterialTheme.colorScheme.error
                )
            }

            if (feedback.isNotBlank()) {
                Text(
                    feedback,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun PageCounter(
    pagesRead: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(
            onClick = onDecrease,
            enabled = pagesRead > 0
        ) {
            Text("−")
        }

        Text("$pagesRead halaman", fontSize = 18.sp)

        OutlinedButton(onClick = onIncrease) {
            Text("+")
        }
    }
}
