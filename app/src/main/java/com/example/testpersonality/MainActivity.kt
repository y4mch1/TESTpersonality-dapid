package com.example.testpersonality

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*

enum class Category { AUTISME, ADHD, IDD }
data class Question(val text: String, val category: Category, var answer: Boolean? = null)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { AppNavigation() } }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "welcome") {
        composable("welcome") { WelcomeScreen(navController) }
        composable("questions") { QuestionnaireScreen(navController) }
        composable("result/{aut}/{adhd}/{idd}") {
            val aut = it.arguments?.getString("aut")?.toInt() ?: 0
            val adhd = it.arguments?.getString("adhd")?.toInt() ?: 0
            val idd = it.arguments?.getString("idd")?.toInt() ?: 0
            ResultScreen(aut, adhd, idd, navController)
        }
    }
}

@Composable
fun WelcomeScreen(navController: NavHostController) {
    Box(
        Modifier.fillMaxSize().background(
            Brush.verticalGradient(
                listOf(Color(0xFF1976D2), Color(0xFF2196F3))
            )
        )
    ) {
        Column(
            Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.95f)),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.elevatedCardElevation(8.dp)
            ) {
                Column(
                    Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Asesmen Perkembangan Anak",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2),
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Aplikasi ini membantu menilai aspek perkembangan anak dalam 3 kategori:",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                    Spacer(Modifier.height(16.dp))
                    listOf("• Autisme", "• ADHD", "• Intellectual Disability").forEach {
                        Text(it, fontSize = 14.sp, color = Color(0xFF666666))
                    }
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = { navController.navigate("questions") },
                        colors = ButtonDefaults.buttonColors(Color(0xFF1976D2)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Text("Mulai Asesmen", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionnaireScreen(navController: NavHostController) {
    var questions by remember { mutableStateOf(generateQuestions()) }
    val answered = questions.count { it.answer != null }
    val total = questions.size

    Column(
        Modifier.fillMaxSize()
            .background(Brush.radialGradient(
                listOf(Color(0xFFE3F2FD), Color(0xFFF5F9FF), Color.White),
                radius = 1500f
            ))
            .padding(16.dp)
    ) {

        Card(
            colors = CardDefaults.cardColors(
                Brush.horizontalGradient(
                    listOf(Color(0xFF1976D2), Color(0xFF2196F3))
                ).run { Color.White }
            ),
            elevation = CardDefaults.elevatedCardElevation(8.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Box(
                Modifier.fillMaxWidth()
                    .background(Brush.horizontalGradient(
                        listOf(Color(0xFF1976D2), Color(0xFF2196F3))
                    ))
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        "Asesmen Perkembangan Anak",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Pertanyaan $answered dari $total",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = answered / total.toFloat(),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f),
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                    )
                }
            }
        }

        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(vertical = 16.dp)
        ) {
            questions.forEachIndexed { index, q ->
                Card(
                    Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.elevatedCardElevation(4.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(Modifier.padding(20.dp)) {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                Modifier.size(28.dp)
                                    .background(
                                        Color(0xFF1976D2).copy(alpha = 0.1f),
                                        RoundedCornerShape(14.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "${index + 1}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1976D2)
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Text(
                                q.text,
                                fontSize = 15.sp,
                                color = Color(0xFF2C2C2C),
                                lineHeight = 20.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(Modifier.height(16.dp))


                        Row(Modifier.fillMaxWidth()) {
                            Card(
                                onClick = {
                                    questions = questions.toMutableList().also { list ->
                                        list[index] = q.copy(answer = true)
                                    }
                                },
                                colors = CardDefaults.cardColors(
                                    if (q.answer == true) Color(0xFF1976D2) else Color(0xFFF8FBFF)
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f).height(48.dp),
                                elevation = if (q.answer == true) CardDefaults.elevatedCardElevation(6.dp) else CardDefaults.elevatedCardElevation(2.dp)
                            ) {
                                Box(
                                    Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "✓ Ya",
                                        color = if (q.answer == true) Color.White else Color(0xFF1976D2),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Spacer(Modifier.width(12.dp))

                            Card(
                                onClick = {
                                    questions = questions.toMutableList().also { list ->
                                        list[index] = q.copy(answer = false)
                                    }
                                },
                                colors = CardDefaults.cardColors(
                                    if (q.answer == false) Color(0xFF1976D2) else Color(0xFFF8FBFF)
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f).height(48.dp),
                                elevation = if (q.answer == false) CardDefaults.elevatedCardElevation(6.dp) else CardDefaults.elevatedCardElevation(2.dp)
                            ) {
                                Box(
                                    Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "✗ Tidak",
                                        color = if (q.answer == false) Color.White else Color(0xFF1976D2),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }


        Card(
            onClick = {
                val aut = questions.filter { it.category == Category.AUTISME && it.answer == true }.size
                val adhd = questions.filter { it.category == Category.ADHD && it.answer == true }.size
                val idd = questions.filter { it.category == Category.IDD && it.answer == true }.size
                navController.navigate("result/$aut/$adhd/$idd")
            },
            colors = CardDefaults.cardColors(
                if (answered == total) Color(0xFF1976D2) else Color(0xFFE0E0E0)
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(56.dp),
            elevation = if (answered == total) CardDefaults.elevatedCardElevation(8.dp) else CardDefaults.elevatedCardElevation(2.dp),
            enabled = answered == total
        ) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (answered == total) "Lihat Hasil Asesmen" else "Lengkapi semua pertanyaan",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (answered == total) Color.White else Color.Gray
                )
            }
        }
    }
}

@Composable
fun ResultScreen(autScore: Int, adhdScore: Int, iddScore: Int, navController: NavHostController) {
    val autPercent = autScore * 100 / 18
    val adhdPercent = adhdScore * 100 / 18
    val iddPercent = iddScore * 100 / 18

    Column(
        Modifier.fillMaxSize()
            .background(Brush.radialGradient(
                listOf(Color(0xFFE3F2FD), Color(0xFFF5F9FF), Color.White),
                radius = 1200f
            ))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.elevatedCardElevation(12.dp),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                Modifier.fillMaxWidth()
                    .background(Brush.radialGradient(
                        listOf(Color(0xFF1976D2), Color(0xFF2196F3), Color(0xFF42A5F5)),
                        radius = 800f
                    ))
                    .padding(32.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "",
                        fontSize = 48.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Hasil Asesmen",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "Perkembangan Anak",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))


        Card(
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.elevatedCardElevation(8.dp),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(24.dp)) {
                ResultCard("Spektrum Autisme", autScore, 18, autPercent, Color(0xFF1976D2), "🧩")
                Spacer(Modifier.height(20.dp))
                ResultCard("ADHD", adhdScore, 18, adhdPercent, Color(0xFF2196F3), "⚡")
                Spacer(Modifier.height(20.dp))
                ResultCard("Intellectual Disability", iddScore, 18, iddPercent, Color(0xFF42A5F5), "🧠")
            }
        }

        Spacer(Modifier.height(24.dp))


        Card(
            colors = CardDefaults.cardColors(Color(0xFFF8FBFF)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("ℹ️", fontSize = 20.sp)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Catatan Penting",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2)
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    "Hasil ini hanya sebagai panduan awal. Konsultasikan dengan ahli untuk diagnosis yang akurat.",
                    fontSize = 13.sp,
                    color = Color(0xFF666666),
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(Modifier.height(20.dp))


        Row(Modifier.fillMaxWidth()) {
            Card(
                onClick = { navController.navigate("welcome") },
                colors = CardDefaults.cardColors(Color.White),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f).height(56.dp),
                elevation = CardDefaults.elevatedCardElevation(4.dp)
            ) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "🔄 Ulangi",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1976D2)
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Card(
                onClick = { /* Share functionality */ },
                colors = CardDefaults.cardColors(Color(0xFF1976D2)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f).height(56.dp),
                elevation = CardDefaults.elevatedCardElevation(8.dp)
            ) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "📋 Simpan",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ResultCard(label: String, score: Int, total: Int, percent: Int, color: Color, icon: String) {
    Column {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(icon, fontSize = 24.sp)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        label,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C2C2C)
                    )
                    Text(
                        "$score dari $total pertanyaan",
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                }
            }
            Card(
                colors = CardDefaults.cardColors(color.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "$percent%",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = color,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Box(
            Modifier.fillMaxWidth().height(12.dp)
                .background(color.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
        ) {
            Box(
                Modifier.fillMaxHeight()
                    .fillMaxWidth(percent / 100f)
                    .background(
                        Brush.horizontalGradient(
                            listOf(color.copy(alpha = 0.8f), color)
                        ),
                        RoundedCornerShape(6.dp)
                    )
            )
        }
    }
}

fun generateQuestions(): List<Question> {
    val autisme = listOf(
        "Apakah anak Anda kesulitan dalam memahami instruksi atau perintah dari orang lain?",
        "Ketika orang lain berbicara kepada anak, apakah anak kesulitan menanggapi?",
        "Apakah anak menunjukkan ketidakpedulian saat ada orang lain yang menangis/marah?",
        "Apakah anak menunjukkan ekspresi wajah yang tidak sesuai situasi sosial?",
        "Apakah anak jarang menggunakan gerakan tangan atau tubuh saat menyampaikan sesuatu?",
        "Apakah anak kesulitan melakukan kontak mata?",
        "Apakah anak sering kesulitan untuk mendekati anak lain?",
        "Apakah anak kesulitan beradaptasi dengan situasi/orang baru?",
        "Apakah anak menunjukkan gerakan tubuh berulang?",
        "Apakah anak sering mengulang kata atau menirukan dialog TV?",
        "Apakah anak bisa memainkan mainan sesuai fungsinya?",
        "Apakah anak kesulitan beradaptasi dengan perubahan rutinitas?",
        "Apakah anak marah/tantrum saat di lingkungan baru?",
        "Apakah anak memilih makanan/aktivitas yang sama setiap hari?",
        "Apakah anak enggan mencoba hal baru?",
        "Apakah anak fokus berlebihan pada satu objek/topik?",
        "Apakah anak menolak disentuh di area tubuh tertentu?",
        "Apakah anak tidak menangis saat terluka?"
    )
    val adhd = listOf(
        "Apakah anak mudah ceroboh?",
        "Apakah anak susah fokus saat mengerjakan tugas?",
        "Apakah anak sulit konsentrasi saat orang lain bicara?",
        "Apakah anak kerap tidak mengikuti instruksi?",
        "Apakah anak kesulitan mengatur jadwal?",
        "Apakah anak gemar menunda tugas sulit?",
        "Apakah anak sering menghilangkan barang penting?",
        "Apakah fokus anak mudah teralihkan?",
        "Apakah anak kesulitan mengingat jadwal?",
        "Apakah anak sering menggerakkan tangan/kaki saat duduk lama?",
        "Apakah anak sering meninggalkan tempat duduk?",
        "Apakah anak sering berlari/memanjat berlebihan?",
        "Apakah anak sulit bermain dengan tenang?",
        "Apakah anak tidak bisa diam dan terus bergerak?",
        "Apakah anak suka bicara terus meskipun harus diam?",
        "Apakah anak suka memotong pembicaraan?",
        "Apakah anak sulit menunggu giliran?",
        "Apakah anak sering mengganggu orang lain?"
    )
    val idd = listOf(
        "Apakah anak kesulitan memahami atau menggunakan uang?",
        "Apakah anak kesulitan mengenal angka 1–10 dan huruf A–F?",
        "Apakah anak kesulitan memahami isyarat sosial?",
        "Apakah anak kesulitan menyiapkan atau mengatur barangnya sendiri?",
        "Apakah anak kesulitan mengenal huruf, angka, membaca sederhana?",
        "Apakah anak kesulitan memahami konsep waktu atau nilai uang?",
        "Apakah anak kesulitan memahami maksud/perasaan orang lain?",
        "Apakah anak sering kesulitan membangun/menjaga pertemanan?",
        "Apakah anak bisa merawat diri tapi masih sering perlu diingatkan?",
        "Apakah anak memerlukan waktu lama untuk terbiasa menjaga kebersihan diri?",
        "Apakah anak tidak tahu waktu istirahat sekolah?",
        "Apakah anak hanya bicara 1–2 kata saja?",
        "Apakah anak masih perlu bantuan dalam kegiatan dasar?",
        "Apakah anak harus selalu diawasi?",
        "Apakah anak hanya mampu menggunakan benda sederhana?",
        "Apakah anak kesulitan mencocokkan benda berdasarkan warna/bentuk/ukuran?",
        "Apakah anak lebih sering berkomunikasi tanpa kata?",
        "Apakah anak kadang tantrum atau menolak aktivitas tanpa alasan jelas?"
    )

    return buildList {
        autisme.forEach { add(Question(it, Category.AUTISME)) }
        adhd.forEach { add(Question(it, Category.ADHD)) }
        idd.forEach { add(Question(it, Category.IDD)) }
    }
}