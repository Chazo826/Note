package com.chazo826.memo.list.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.chazo826.core.extensions.TAG
import com.chazo826.data.memo.model.Memo
import com.chazo826.memo.R
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.item_memo.view.*
import java.text.SimpleDateFormat
import java.util.*

class MemoPagedAdapter(
    private val disposable: CompositeDisposable,
    private val onItemClick: (memoUid: Long) -> Unit
) : PagedListAdapter<Memo, MemoPagedAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(disposable, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, onItemClick)}
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Memo>() {
            override fun areItemsTheSame(oldItem: Memo, newItem: Memo): Boolean =
                oldItem.uid == newItem.uid

            override fun areContentsTheSame(oldItem: Memo, newItem: Memo): Boolean =
                oldItem.copy(uid = 0).toString() == newItem.copy(uid = 0).toString()

        }
    }

    class ViewHolder(
        private val disposable: CompositeDisposable, parent: ViewGroup
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_memo, parent, false)
    ) {
        fun bind(memo: Memo, onItemClick: (memoUid: Long) -> Unit) = with(itemView) {
            tv_title?.text = memo.title
            tv_date?.text = SimpleDateFormat("yyyy.mm.dd", Locale.getDefault()).format(
                Date(
                    memo.updatedAt ?: 0
                )
            )
            tv_content?.text = memo.content

            memo.pictures?.firstOrNull()?.let { Uri.parse(it) }?.let {
                Log.d(TAG, "!!!! $it")
                Glide.with(context)
                    .load(it)
                    .placeholder(createCircleProgress(context))
                    .error(R.drawable.ic_error)
                    .into(iv_preview)
            } ?: kotlin.run {
                iv_preview.isVisible = false
            }

            disposable += clicks()
                .subscribe {
                    onItemClick(memo.uid)
                }
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