package ru.netology.nmedia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl

private val emptyPost = Post(id = 0, author = "", content = "", published = "")

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryImpl()
    val data = repository.getAll()
    val edited = MutableLiveData<Post>(emptyPost)

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)
    fun save (content : String) {
        edited.value?.let{ post ->
            val trimmed = content.trim()
            if (trimmed != post.content) {
                repository.save(post.copy(content = content))
            }
        }
        edited.value = emptyPost
    }
    fun edit (post: Post) {
        edited.value = post
    }
    fun dropEdit() {
        edited.value = emptyPost
    }
}
