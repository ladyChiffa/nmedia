package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

interface PostListener {
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
    fun onRemove(post: Post) {}
    fun onEdit(post: Post) {}
}

class PostsAdapter (private val listener: PostListener)
    : ListAdapter<Post, PostViewHolder>(PostDiffCallback) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return PostViewHolder(binding, listener)
    }

    override fun onBindViewHolder(viewHolder: PostViewHolder, position: Int) {
        val post = getItem(position)
        viewHolder.bind(post)
    }
}

class PostViewHolder (private val binding: CardPostBinding,
                      private val listener: PostListener) : RecyclerView.ViewHolder(binding.root) {
    fun bind (post: Post) {
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likes?.setImageResource(if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24)
            qLikes?.text = formatCount(post.likes)
            qShares.text = formatCount(post.shares)
            qViews.text = formatCount(post.views)
            likes?.setOnClickListener {
                listener.onLike(post)
            }
            shares.setOnClickListener {
                listener.onShare(post)
            }
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_post) // заполняем элементами
                    setOnMenuItemClickListener { item ->    // и ждем события клика по меню
                        when (item.itemId) {                // - по идентификатору определяем, что кликнули
                            R.id.remove_post -> {
                                listener.onRemove(post)
                                true
                            }
                            R.id.edit_post -> {
                                listener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show() // и не забываем после настройки меню показать
            }
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

object PostDiffCallback: DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame ( oldItem: Post, newItem: Post ) = oldItem.id == newItem.id
    override fun areContentsTheSame ( oldItem: Post, newItem: Post ) = oldItem == newItem
}
