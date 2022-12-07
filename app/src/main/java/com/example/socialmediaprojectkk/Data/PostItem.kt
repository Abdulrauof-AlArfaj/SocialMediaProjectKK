package com.example.socialmediaprojectkk.Data


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class PostItem(
    @SerializedName("comments")
    val comments: String, // All hype! I'll believe it when I see it..., I can't wait to get my omnidirectional treadmill and start running around!,if only i can eat vritually :D
    @SerializedName("id")
    val id: Int, // 1
    @SerializedName("likes")
    var likes: String, // Almin, Tina, Zoe,omar
    @SerializedName("text")
    val text: String, // Talk about the metaverse seems to be everywhere these days. Major news networks have been doing stories on it, and I see a new article every day. We've even had movies about it, and there are books that predicted it long before we had the technology...I can't help but get excited about the possibilities, but then there is always that dystopian version of it that makes me rethink my stance.How do you guys picture it all working out? What is your vision of the metaverse?
    @SerializedName("title")
    val title: String, // How do you picture the metaverse?
    @SerializedName("user")
    val user: String // Almin
)