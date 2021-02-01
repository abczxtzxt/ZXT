package com.liebe.base_lib.adapter;

import android.annotation.SuppressLint
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.core.util.isEmpty
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 使用代理模式，将adapter与holder进行解耦
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
open class BaseRecyclerAdapter(
    open var data: MutableList<Any>? = null,
    open val proxys: SparseArray<Class<*>>,
    open val callbacks: SparseArray<ItemCallBack<*>>,
    open val otherClickCallBack: SparseArray<OtherClickCallBack<*>>,
    open val diffCb: DiffCallBack? = null
) : RecyclerView.Adapter<InnerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        //解决部分view不需要长按的情况
        val proxy = if (otherClickCallBack.isEmpty() || otherClickCallBack[viewType] == null) {
            proxys[viewType].getConstructor(
                ViewGroup::class.java,
                ItemCallBack::class.java,
                this::class.java
            )
                .newInstance(parent, callbacks[viewType], this)
        } else {
            proxys[viewType].getConstructor(
                ViewGroup::class.java,
                ItemCallBack::class.java,
                this::class.java,
                OtherClickCallBack::class.java
            ).newInstance(parent, callbacks[viewType], this, otherClickCallBack[viewType])
        }

        return InnerViewHolder(parent, proxy as ViewHolderProxy<Any>)
    }

    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        holder.proxy.dispatchBindView(data!![position])
    }

    override fun getItemCount() = if (data == null) 0 else data!!.size

    override fun getItemViewType(position: Int): Int {
        return data!![position].javaClass.name.hashCode()
    }

    fun refresh(data: MutableList<Any>?) {
        DiffRefreshHelper().refresh(data)
    }

    open class Builder {
        var data: MutableList<Any>? = null
        var diffCb: DiffCallBack? = null
        private val proxys: SparseArray<Class<*>> = SparseArray()
        private val callbacks: SparseArray<ItemCallBack<*>> = SparseArray()
        private val longClickcallbacks: SparseArray<OtherClickCallBack<*>> = SparseArray()

        fun setData(data: MutableList<Any>?): Builder {
            this.data = data
            return this
        }

        fun setDiffCallBack(diffCb: DiffCallBack): Builder {
            this.diffCb = diffCb
            return this
        }

        fun register(
            cls: Class<*>, proxy: Class<*>, callBack: ItemCallBack<*>? = null,
            otherClickCallBack: OtherClickCallBack<*>? = null
        ): Builder {
            proxys.put(cls.name.hashCode(), proxy)
            callBack?.let {
                callbacks.put(cls.name.hashCode(), callBack)
            }
            otherClickCallBack?.let {
                longClickcallbacks.put(cls.name.hashCode(), otherClickCallBack)
            }
            return this
        }

        open fun build() = BaseRecyclerAdapter(data, proxys, callbacks, longClickcallbacks)
    }

    inner class DiffRefreshHelper {
        private var version = 0

        @SuppressLint("CheckResult")
        fun refresh(newData: MutableList<Any>?) {

            if (diffCb == null || data.empty() || newData.empty()) {
                data = newData
                notifyDataSetChanged()
            } else {

                val currentVersion = ++version
                Flowable.fromCallable {
                    DiffUtil.calculateDiff(
                        InnerCallback(
                            data,
                            newData,
                            diffCb!!
                        )
                    )
                }
                    .singleElement()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        if (version == currentVersion) {
                            data = newData
                            it.dispatchUpdatesTo(this@BaseRecyclerAdapter)
                        }
                    }
            }
        }

        private fun <R> MutableList<R>?.empty() = (this?.size ?: 0) == 0

    }
}

abstract class ViewHolderProxy<D>(
    open val parent: ViewGroup,
    open val callback: ItemCallBack<*>? = null,
    open val adapter: BaseRecyclerAdapter,
    open val otherClickCallBack: OtherClickCallBack<*>? = null
) {

    constructor(
        parent: ViewGroup,
        callback: ItemCallBack<*>? = null,
        adapter: BaseRecyclerAdapter
    ) : this(parent, callback, adapter, null)

    var data: D? = null
    lateinit var holder: RecyclerView.ViewHolder

    fun dispatchBindView(d: D) {
        data = d
        bindView(d)
    }

    abstract fun createView(): View
    open fun bindView(d: D) = Unit
}

interface ItemCallBack<D> {
    fun callback(view: View, pos: Int, data: D)
}

interface OtherClickCallBack<D> {
    fun otherClickCallback(view: View, pos: Int, data: D)
}

class InnerViewHolder(
    val parent: ViewGroup, val proxy: ViewHolderProxy<Any>
) : RecyclerView.ViewHolder(proxy.createView()) {
    init {
        proxy.holder = this@InnerViewHolder
    }
}

class InnerCallback(
    private var oldData: List<Any>? = null,
    private val newData: List<Any>?,
    val callback: DiffCallBack
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldData?.size ?: 0

    override fun getNewListSize() = newData?.size ?: 0

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        callback.areItemsTheSame(
            oldItemPosition,
            oldData!![oldItemPosition],
            newItemPosition,
            newData!![newItemPosition]
        )

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        callback.areContentsTheSame(
            oldItemPosition,
            oldData!![oldItemPosition],
            newItemPosition,
            newData!![newItemPosition]
        )
}

interface DiffCallBack {
    fun areItemsTheSame(oldPos: Int, oldItem: Any, newPos: Int, newItem: Any): Boolean
    fun areContentsTheSame(oldPos: Int, oldItem: Any, newPos: Int, newItem: Any): Boolean
}