package com.chazo826.memo.detail.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chazo826.memo.R
import com.chazo826.memo.databinding.ItemMemoAttachImageBinding
import io.reactivex.disposables.CompositeDisposable

class AttachImageAdapter(
    private val disposable: CompositeDisposable,
    private val onDelete: (uri: Uri) -> Unit
) : ListAdapter<Uri, AttachImageAdapter.ViewHolder>(DIFF_CALLBACK) {

    private val isEditable: ObservableField<Boolean> = ObservableField(false)

    fun setIsEditable(isEditable: Boolean) {
        this.isEditable.set(isEditable)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(disposable, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).also {
            holder.bind(it, isEditable, onDelete)
        }
    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Uri>() {
            override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
                return oldItem.toString() == newItem.toString()
            }

        }
    }

    class ViewHolder(private val disposable: CompositeDisposable, parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_memo_attach_image, parent, false)
    ) {
        private val binding: ItemMemoAttachImageBinding? = DataBindingUtil.bind(itemView)

        fun bind(uri: Uri, isEditable: ObservableField<Boolean>, onDelete: (uri: Uri) -> Unit) = with(itemView) {
            binding?.isEditable = isEditable

            binding?.ivPreview?.let {
                Glide.with(context)
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(createCircleProgress(context))
                    .error(R.drawable.ic_error)
                    .into(it)
            }
            binding?.ivRemove?.setOnClickListener {
                onDelete(uri)
            }

            Unit
        }

        private fun createCircleProgress(context: Context): Drawable {
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            return circularProgressDrawable
        }
    }
}