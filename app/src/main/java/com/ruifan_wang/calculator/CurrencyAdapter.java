package com.ruifan_wang.calculator;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> {

    private final List<Currency> currencies;
    private final Context context;
    private int lastChangedPosition = -1;
    private final DecimalFormat decimalFormat;
    // 添加一个变量来跟踪有焦点的EditText
    private EditText focusedEditText = null;
    // 添加触摸帮助器引用
    private ItemTouchHelper touchHelper;

    // 监听器接口，用于通知外部活动金额改变
    public interface CurrencyValueChangeListener {
        void onCurrencyValueChanged(int position, double value);
    }

    private CurrencyValueChangeListener listener;

    public CurrencyAdapter(Context context, List<Currency> currencies) {
        this.context = context;
        this.currencies = currencies;
        this.decimalFormat = new DecimalFormat("#,##0.00");
    }

    public void setTouchHelper(ItemTouchHelper touchHelper) {
        this.touchHelper = touchHelper;
    }

    public void setCurrencyValueChangeListener(CurrencyValueChangeListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_currency, parent, false);
        return new CurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder holder, int position) {
        Currency currency = currencies.get(position);

        // 设置货币代码
        holder.textCurrencyCode.setText(currency.getCode());

        // 设置国旗图像
        int flagResId = context.getResources().getIdentifier(
                currency.getFlagResName(), "drawable", context.getPackageName());
        if (flagResId != 0) {
            holder.imgFlag.setImageResource(flagResId);
        } else {
            // 如果找不到国旗资源，设置一个默认图像
            holder.imgFlag.setImageResource(R.drawable.ic_launcher_foreground);
        }

        // 设置拖动手柄的触摸监听
        holder.imgDragHandle.setOnTouchListener((v, event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                if (touchHelper != null) {
                    touchHelper.startDrag(holder);
                }
                return true;
            }
            return false;
        });

        // 设置焦点变化监听器
        holder.editAmount.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                lastChangedPosition = holder.getAdapterPosition();
                focusedEditText = holder.editAmount;
            }
        });

        // 只有在这个条目不是我们正在编辑的条目时才更新文本
        holder.editAmount.removeTextChangedListener(holder.textWatcher);
        if (position != lastChangedPosition || holder.editAmount != focusedEditText) {
            // 如果这个EditText不是当前有焦点的EditText，则更新其值
            holder.editAmount.setText(currency.getAmount() > 0
                    ? decimalFormat.format(currency.getAmount())
                    : "");
        }
        holder.editAmount.addTextChangedListener(holder.textWatcher);
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    // 处理项目移动
    public void onItemMove(int fromPosition, int toPosition) {
        // 交换位置，更新列表数据
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(currencies, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(currencies, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    // 拖拽完成时调用
    public void onDragCompleted() {
        // 拖拽完成后重新计算所有货币值
        if (lastChangedPosition >= 0 && lastChangedPosition < currencies.size()) {
            Currency baseCurrency = currencies.get(lastChangedPosition);
            if (baseCurrency.getAmount() > 0) {
                updateCurrencyValues(lastChangedPosition, baseCurrency.getAmount());
            }
        }
    }

    // 更新所有货币的金额，基于选定的货币和金额
    public void updateCurrencyValues(int changedPosition, double value) {
        if (changedPosition < 0 || changedPosition >= currencies.size()) {
            return; // 避免数组越界
        }

        // 保存当前的焦点EditText
        EditText currentFocused = focusedEditText;

        Currency baseCurrency = currencies.get(changedPosition);
        double baseValue = value;
        double baseRate = baseCurrency.getRate();

        // 计算结果的基准值（相对于1美元的金额）
        double baseAmount = baseValue / baseRate;

        for (int i = 0; i < currencies.size(); i++) {
            if (i != changedPosition) {
                Currency currency = currencies.get(i);
                // 使用基准值乘以目标货币汇率，得到正确的换算金额
                double newAmount = baseAmount * currency.getRate();
                currency.setAmount(newAmount);
            }
        }

        lastChangedPosition = changedPosition;

        // 使用notifyItemRangeChanged代替notifyDataSetChanged，这样会保留输入框焦点
        notifyItemRangeChanged(0, currencies.size(), "update_values");

        // 确保焦点保持在用户正在编辑的EditText上
        if (currentFocused != null) {
            currentFocused.requestFocus();
        }
    }

    class CurrencyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFlag;
        TextView textCurrencyCode;
        EditText editAmount;
        ImageView imgDragHandle;
        TextWatcher textWatcher;

        public CurrencyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFlag = itemView.findViewById(R.id.img_flag);
            textCurrencyCode = itemView.findViewById(R.id.text_currency_code);
            editAmount = itemView.findViewById(R.id.edit_amount);
            imgDragHandle = itemView.findViewById(R.id.img_drag_handle);

            textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        try {
                            String text = s.toString().replace(",", "");
                            if (text.isEmpty()) {
                                // 如果为空，设置为0并触发更新
                                currencies.get(position).setAmount(0);
                                // 添加触发更新的代码，确保当输入为空时也会更新其他货币值为0
                                if (listener != null && editAmount.hasFocus()) {
                                    listener.onCurrencyValueChanged(position, 0);
                                }
                            } else {
                                double value = Double.parseDouble(text);
                                currencies.get(position).setAmount(value);
                                if (listener != null && editAmount.hasFocus()) {
                                    listener.onCurrencyValueChanged(position, value);
                                }
                            }
                        } catch (NumberFormatException e) {
                            // 忽略格式错误
                        }
                    }
                }
            };

            editAmount.addTextChangedListener(textWatcher);
        }
    }
}
