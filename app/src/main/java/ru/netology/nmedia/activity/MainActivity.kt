package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left + v.paddingLeft,
                systemBars.top + v.paddingTop,
                systemBars.right + v.paddingRight,
                systemBars.bottom + v.paddingBottom)
            insets
        }

        val viewModel: PostViewModel by viewModels<PostViewModel>()
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                if (post.likedByMe) {
                    likes?.setImageResource(R.drawable.ic_liked_24)
                }
                qLikes?.text = formatCount(post.likes)
                qShares.text = formatCount(post.shares)
                qViews.text = formatCount(post.views)

            }
        }

        binding.likes?.setOnClickListener {
            viewModel.like()
        }

        binding.shares.setOnClickListener {
            viewModel.share()
        }
    }

    fun formatCount(count: Int) : String {
        val ADD_SUFFIX_NONE = 1_000
        val ADD_SUFFIX_KILO = 10_000
        val ADD_SUFFIX_MEGA = 1_000_000

        if(count < ADD_SUFFIX_NONE) return count.toString()
        if(count < ADD_SUFFIX_KILO) {
            val countShown = (count * 10 / ADD_SUFFIX_NONE).toDouble() / 10
            return countShown.toString() + "K"
        }
        if(count < ADD_SUFFIX_MEGA) {
            return (count / 1000).toString() + "K"
        }
        val countShown = (count * 10 / ADD_SUFFIX_MEGA).toDouble() / 10
        return countShown.toString() + "M"
    }
}