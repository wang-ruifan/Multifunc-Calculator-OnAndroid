package com.ruifan_wang.calculator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 货币列表的拖拽排序辅助类
 */
public class CurrencyItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final CurrencyAdapter adapter;
    private final OrderChangeListener listener;

    // 定义一个接口，用于通知外部排序已改变
    public interface OrderChangeListener {
        void onOrderChanged();
    }

    public CurrencyItemTouchHelperCallback(CurrencyAdapter adapter, OrderChangeListener listener) {
        this.adapter = adapter;
        this.listener = listener;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        // 我们只通过拖动手柄来触发拖动，而不是长按
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        // 不支持左右滑动删除
        return false;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        // 设置可上下拖动
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        // 当项目被拖动时调用
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();

        // 通知适配器移动项目
        adapter.onItemMove(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // 我们不支持滑动删除操作，所以这里不需要实现
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        // 拖动结束时调用
        adapter.onDragCompleted();

        // 通知监听器排序已改变，以便保存
        if (listener != null) {
            listener.onOrderChanged();
        }
    }
}
