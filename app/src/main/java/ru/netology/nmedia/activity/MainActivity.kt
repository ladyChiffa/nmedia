package ru.netology.nmedia.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.adapter.PostListener
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
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
        val adapter = PostsAdapter(object : PostListener {
            override fun onLike(post: Post) = viewModel.likeById(post.id)
            override fun onShare(post: Post) = viewModel.shareById(post.id)
            override fun onRemove(post: Post) = viewModel.removeById(post.id)
            override fun onEdit(post: Post) = viewModel.edit(post)
        })

        binding.list.adapter = adapter

        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
        viewModel.edited.observe(this) { edited ->
            if(edited.id != 0L) {
                binding.content.setText(edited.content)
                AndroidUtils.showKeyboard(binding.content)
            }
        }

        binding.save.setOnClickListener {
            val content = binding.content.text?.toString()
            if (content.isNullOrBlank()) {
                Toast.makeText(this, R.string.error_empty_post, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.save(content)
            binding.content.clearFocus()
            binding.content.setText("")

            // и скрыть клавиатуру
            AndroidUtils.hideKeyboard(binding.content)
        }
    }
}