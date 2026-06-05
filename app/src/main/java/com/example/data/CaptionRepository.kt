package com.example.data

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CaptionRepository(private val captionDao: CaptionDao) {

    val allCaptions: Flow<List<Caption>> = captionDao.getAllCaptions()
    val likedCaptions: Flow<List<Caption>> = captionDao.getLikedCaptions()
    val featuredCaptions: Flow<List<Caption>> = captionDao.getFeaturedCaptions()
    val userSubmittedCaptions: Flow<List<Caption>> = captionDao.getUserSubmittedCaptions()
    val pendingSubmissions: Flow<List<Caption>> = captionDao.getPendingSubmissions()

    fun getCaptionsByCategory(category: String): Flow<List<Caption>> {
        return captionDao.getCaptionsByCategory(category)
    }

    suspend fun insert(caption: Caption): Long = withContext(Dispatchers.IO) {
        captionDao.insertCaption(caption)
    }

    suspend fun update(caption: Caption) = withContext(Dispatchers.IO) {
        captionDao.updateCaption(caption)
    }

    suspend fun delete(caption: Caption) = withContext(Dispatchers.IO) {
        captionDao.deleteCaption(caption)
    }

    suspend fun updateLikeStatus(id: Int, isLiked: Boolean) = withContext(Dispatchers.IO) {
        captionDao.updateLikeStatus(id, isLiked)
    }

    suspend fun approveCaption(id: Int) = withContext(Dispatchers.IO) {
        captionDao.approveCaption(id)
    }

    suspend fun prepopulateIfEmpty() = withContext(Dispatchers.IO) {
        val count = captionDao.getCount()
        if (count == 0) {
            val defaultCaptions = listOf(
                Caption(
                    text = "সীমার মাঝে অসীম তুমি বাজাও আপন সুর,\nআমার মাঝে তোমার প্রকাশ তাই এত মধুর।",
                    category = "Spirituality",
                    author = "রবীন্দ্রনাথ ঠাকুর",
                    meaning = "আমাদের ক্ষুদ্র সীমার মাঝেও পরমাত্মার অনন্ত সৌন্দর্যের যে প্রকাশ ঘটে, কবি এখানে চমৎকার আধ্যাত্মিকভাবে তা তুলে ধরেছেন।",
                    isFeatured = true
                ),
                Caption(
                    text = "মন রে কৃষি কাজ জানো না,\nএমন মানব জমিন রইল পতিত, আবাদ করলে ফলত সোনা।",
                    category = "Spirituality",
                    author = "রামপ্রসাদ সেন",
                    meaning = "মানুষের মন হলো একটি উর্বর ক্ষেত্র, অবহেলায় একে পতিত না রেখে আধ্যাত্মিক সাধনার মাধ্যমে একে মূল্যবান করতে হবে।",
                    isFeatured = false
                ),
                Caption(
                    text = "নদীর একূল ভাঙে ওকূল গড়ে, এই তো নদীর খেলা।\nসকাল বেলার ধনী রে ভাই সিন্ধু বেলার কাঙাল।",
                    category = "Life Philosophy",
                    author = "কাজী নজরুল ইসলাম",
                    meaning = "পৃথিবীর কোনো কিছুই স্থায়ী নয়। ধনী-দরিদ্রের ভাগ্যচক্র নদীর জোয়ার-ভাটার মতোই পরিবর্তনশীল ও ক্ষণস্থায়ী।",
                    isFeatured = true
                ),
                Caption(
                    text = "জীবন হলো একটি জটিল অংক, যার সমাধান মরণেই লুকিয়ে আছে। বেঁচে থাকা তো কেবল হিসাব মেলানোর ব্যর্থ লড়াই।",
                    category = "Life Philosophy",
                    author = "সংগৃহীত",
                    meaning = "জীবনের গভীর রহস্য ও সমাধান জীবনের শেষেই স্পষ্ট হয়, জীবদ্দশায় মানুষ কেবল নানা সংশয়ের মধ্য দিয়ে এগিয়ে যায়।",
                    isFeatured = false
                ),
                Caption(
                    text = "তোমাকে পাওয়া ভালোবাসার শেষ সার্থকতা নয়, তোমাকে প্রতিনিয়ত অনুভব করতে পারাটাই ভালোবাসার অনন্ত পথ চলা।",
                    category = "Love & Emotions",
                    author = "রবীন্দ্রনাথ ঠাকুর (ভাবানুসারী)",
                    meaning = "ভালোবাসা কোনো পার্থিব অর্জনের নাম নয়, এটি একটি চিরন্তন অনুভূতি যা দূর থেকেও হৃদয়কে আলোড়িত করে।",
                    isFeatured = true
                ),
                Caption(
                    text = "কিছু কথা রয়ে যায় চোখের পাতায়, কিছু কান্না গুমড়ে মরে নীরব হাসির সীমানায়। আমরা লুকিয়ে ভালো রাখি, প্রকাশ করে হারিয়ে ফেলি না।",
                    category = "Love & Emotions",
                    author = "হুমাযুন আহমেদ (অনুপ্রেরণায়)",
                    meaning = "গভীর মানসিক অনুভূতি ও প্রেম অনেক সময় অন্তরালেই বেশি সুরক্ষিত থাকে, জনসমক্ষে আসার চেয়ে হৃদয়ের একাকীত্বে তার গভীরতা বেশি।"
                ),
                Caption(
                    text = "মেঘ দেখে কেউ করিসনে ভয়, আড়ালে তার সূর্য হাসে।\nহারা শশীর হারা পেতে, অন্য কোণে জোয়ার আসে।",
                    category = "Motivation",
                    author = "রবীন্দ্রনাথ ঠাকুর",
                    meaning = "জীবনের দুঃখ-কষ্ট সাময়িক। বিপদের মেঘ কেটে যাবেই এবং আশার আলো অবশ্যই দেখা দেবে- এই বিশ্বাস ধরে রাখতে হবে।",
                    isFeatured = true
                ),
                Caption(
                    text = "আমাদের জীবন আমাদের চিন্তারই প্রতিফলন। নিজেকে যদি একটু বদলাতে চাও, তবে সবার আগে নিজের চিন্তার গভীরতাটুকু বদলে নাও।",
                    category = "Motivation",
                    author = "সংগৃহীত",
                    meaning = "আমাদের চারপাশের কঠিন পরিস্থিতিকে মূলত আমাদের অন্তর্গত মানসিকতা দ্বারাই নিয়ন্ত্রণ সম্ভব। ইতিবাচক দৃষ্টিভঙ্গিই পরিবর্তনের ভিত্তি।"
                ),
                Caption(
                    text = "সবার উপরে মানুষ সত্য, তাহার উপরে নাই।\nযখন ধুলার ধরায় মনুষ্যত্ব কাঁদে, মন্দির বা মসজিদের ঈশ্বরও তখন মৌন থাকেন।",
                    category = "Human Values",
                    author = "চণ্ডীদাস (বর্ধিত)",
                    meaning = "বিশ্বে জাত-পাত বা ধর্মের চেয়েও জীব মানুষের শ্রেষ্ঠত্ব ও মানবিক মূল্যবোধই সবচেয়ে সত্য ও পরম পবিত্র।"
                ),
                Caption(
                    text = "জ্ঞানের চেয়ে নম্রতা অনেক মহৎ, কারণ তা হৃদয়ের অপরিসীম গভীরতা ও সুউচ্চ মানবিক মূল্যবোধকে প্রকাশ করে।",
                    category = "Human Values",
                    author = "সংগৃহীত",
                    meaning = "অহংকারহীন জ্ঞানই প্রকৃত সম্পদ। বিনয় মানুষকে হৃদয়ের কাছাকাছি নিয়ে যায় এবং মহান মানবিকতার জন্ম দেয়।"
                ),
                Caption(
                    text = "অভিমান",
                    category = "Word Meaning Analysis",
                    author = "শব্দার্থ বিশ্লেষণ",
                    meaning = "যে ভালোবাসার ভেতরে গভীর বিশ্বাস ও অধিকার জড়িয়ে থাকে, কেবলমাত্র সেখানেই অভিমানের জন্ম হয়। অশ্রু আর মৌনতার মিশেলে গড়া এক নীরব আত্মপ্রকাশ।"
                ),
                Caption(
                    text = "মায়া",
                    category = "Word Meaning Analysis",
                    author = "শব্দার্থ বিশ্লেষণ",
                    meaning = "অদৃশ্য এক নরম সুতোর বাঁধন, যা মানুষকে এই ক্ষণস্থায়ী ধুলোর ধরণীর নানা আনন্দ-বেদনার সাথে আষ্টেপৃষ্ঠে বেঁধে রাখে। এ বড় মধুময় এক মরণবাণ।"
                ),
                Caption(
                    text = "নিস্তব্ধতা",
                    category = "Word Meaning Analysis",
                    author = "শব্দার্থ বিশ্লেষণ",
                    meaning = "নীরবতার চেয়েও গভীর এক শূন্য অথচ পূর্ণ অবস্থা, যেখানে বাইরের সমস্ত গোলমাল স্তব্ধ হয়ে যায় এবং নিজের হৃদয়ের আসল হাহাকার ও উত্তর খুঁজে পাওয়া যায়।"
                ),
                Caption(
                    text = "উদাসীনতা",
                    category = "Word Meaning Analysis",
                    author = "শব্দার্থ বিশ্লেষণ",
                    meaning = "অনুভূতিহীনতার মুখোশ, যা মানুষ সচরাচর বুকফাটা অভিমান বা অবহেলার আঘাত আড়াল করতে পরিধান করে। বাইরের কঠোর পাথর কিন্তু ভেতরে দ্রবীভূত জল।"
                ),
                Caption(
                    text = "মহাজীবন",
                    category = "Life Philosophy",
                    author = "জীবন দর্শন",
                    meaning = "কেবলমাত্র সেকেন্ড ও মিনিটের কাঁটায় বেঁচে থাকার নাম জীবন নয়; বরং প্রতিটি ক্ষুদ্র মুহূর্তের রহস্য, বেদনা এবং সত্যকে সর্বান্তঃকরণে আলিঙ্গন করার নামই মহাজীবন।",
                    isFeatured = true
                )
            )
            captionDao.insertCaptions(defaultCaptions)
            Log.d("CaptionRepository", "Successfully prepopulated ${defaultCaptions.size} captions!")
        }
    }

    suspend fun generateCaptionsWithAI(keyword: String, style: String): List<Caption> {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            throw Exception("Gemini API key is not set properly. Please configure it in the AI Studio Secrets panel.")
        }

        val prompt = """
            You are 'ভাবকথা' (Bhabkotha), an elite Bengali literature assistant.
            The user wants to generate exactly 3 Bengali captions using the keyword: '$keyword'.
            The preferred style of writing is: '$style'. (Which is one of: Spiritual/আধ্যাত্মিক, Emotional/আবেগীয়, Inspirational/অনুপ্রেরণামূলক, Minimalist/স্বল্পভাষী).
            
            Follow these golden rules:
            - Write deep, highly literary, artistic Bengali sentences (not casual or robotic).
            - Use beautiful bangla typography and aesthetic words.
            - Provide exactly 3 entries.
            - Follow this EXACT text structure for each entry. Separate them using '---'.
            - Do NOT include markdown styling on text (like bold markers inside the text, unless requested).
            - Format strictly as below:
            
            ---
            [Caption Text in Bengali]
            - AI ভাবকথা (${style})
            ~ [A beautiful 1-sentence analysis/explanation of the deep meaning behind the caption in Bengali]
            ---
            
            Never omit the '~' or the '-' character. Keep it simple and strictly structured.
        """.trimIndent()

        val request = GenerateContentRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt)))),
            generationConfig = GenerationConfig(temperature = 0.82f)
        )

        val response = RetrofitClient.service.generateContent(apiKey, request)
        val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            ?: throw Exception("The AI could not return a response. Please try again.")

        return parseGeminiOutput(text, keyword, style)
    }

    private fun parseGeminiOutput(rawText: String, keyword: String, style: String): List<Caption> {
        val captionsList = mutableListOf<Caption>()
        try {
            // Split the raw output by the delimiter "---"
            val blocks = rawText.split("---")
            for (block in blocks) {
                val trimmedBlock = block.trim()
                if (trimmedBlock.length < 15) continue // Skip empty or too short blocks

                val lines = trimmedBlock.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
                if (lines.isEmpty()) continue

                // Find caption text, author line starting with '-', and meaning starting with '~'
                var captionText = ""
                var author = "AI ভাবকথা ($style)"
                var meaning = "বিষয়বস্তু: $keyword"

                val textLines = mutableListOf<String>()
                for (line in lines) {
                    when {
                        line.startsWith("-") -> {
                            author = line.removePrefix("-").trim()
                        }
                        line.startsWith("~") -> {
                            meaning = line.removePrefix("~").trim()
                        }
                        else -> {
                            textLines.add(line)
                        }
                    }
                }

                if (textLines.isNotEmpty()) {
                    captionText = textLines.joinToString("\n")
                }

                if (captionText.isNotEmpty()) {
                    captionsList.add(
                        Caption(
                            text = captionText,
                            category = getMatchingCategoryForStyle(style),
                            author = author,
                            meaning = meaning,
                            isUserSubmitted = false,
                            isApproved = false // Ready to be liked/added locally
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("CaptionRepository", "Error parsing Gemini output: ${e.message}", e)
        }

        // Fallback if parsing failed or returned nothing
        if (captionsList.isEmpty()) {
            captionsList.add(
                Caption(
                    text = "শব্দরা যখন রূপ নেয় বেদনায়, তখন তাকে নীরবতা বলে। আর যখন রূপ নেয় সাহিত্যিক উপমায়, তখন তাকেই বলে '$keyword'।",
                    category = getMatchingCategoryForStyle(style),
                    author = "AI ভাবকথা ($style - Fallback)",
                    meaning = "শব্দের গভীরতা তখনই অনুধাবন সম্ভব যখন তা হৃদয়ের গভীরতম কোণ ছুঁয়ে যায়।"
                )
            )
        }

        return captionsList
    }

    private fun getMatchingCategoryForStyle(style: String): String {
        return when (style) {
            "Spiritual" -> "Spirituality"
            "Emotional" -> "Love & Emotions"
            "Inspirational" -> "Motivation"
            else -> "Life Philosophy"
        }
    }
}
