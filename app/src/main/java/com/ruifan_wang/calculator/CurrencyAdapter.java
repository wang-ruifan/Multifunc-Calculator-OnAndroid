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
    private EditText focusedEditText = null;
    private ItemTouchHelper touchHelper;

    private static final String PAYLOAD_FORCE_TEXT_UPDATE = "FORCE_TEXT_UPDATE";
    private static final String PAYLOAD_UPDATE_VALUES = "update_values";

    // 监听器接口，用于通知外部活动金额改变
    public interface CurrencyValueChangeListener {
        void onCurrencyValueChanged(int position, double value);
    }

    // 添加焦点变化监听器接口
    public interface FocusChangeListener {
        void onFocusChanged(int position);
    }

    private CurrencyValueChangeListener listener;
    private FocusChangeListener focusListener;

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

    // 添加设置焦点变化监听器的方法
    public void setFocusChangeListener(FocusChangeListener listener) {
        this.focusListener = listener;
    }

    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_currency, parent, false);
        return new CurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder holder, int position) {
        // This version is called for full binds
        bindViewHolderInternal(holder, position, currencies.get(position), false);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position); // Fallback to full bind
        } else {
            Currency currency = currencies.get(position);
            boolean forceTextUpdate = false;
            boolean valueUpdateSignal = false;

            for (Object payload : payloads) {
                if (PAYLOAD_FORCE_TEXT_UPDATE.equals(payload)) {
                    forceTextUpdate = true;
                }
                if (PAYLOAD_UPDATE_VALUES.equals(payload)) {
                    valueUpdateSignal = true;
                }
            }

            if (forceTextUpdate) {
                bindViewHolderInternal(holder, position, currency, true);
            } else if (valueUpdateSignal) {
                bindViewHolderInternal(holder, position, currency, false);
            } else {
                super.onBindViewHolder(holder, position, payloads);
            }
        }
    }

    private void bindViewHolderInternal(@NonNull CurrencyViewHolder holder, int position, Currency currency, boolean forceTextUpdate) {
        // 设置货币代码
        holder.textCurrencyCode.setText(currency.getCode());

        // 设置国旗图像
        int flagResId = context.getResources().getIdentifier(
                currency.getFlagResName(), "drawable", context.getPackageName());
        if (flagResId != 0) {
            holder.imgFlag.setImageResource(flagResId);
        } else {
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
                if (focusListener != null) {
                    focusListener.onFocusChanged(holder.getAdapterPosition());
                }
            }
        });

        holder.editAmount.removeTextChangedListener(holder.textWatcher);
        if (forceTextUpdate || (position != lastChangedPosition || holder.editAmount != focusedEditText)) {
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

    public void onDragCompleted() {
        if (lastChangedPosition >= 0 && lastChangedPosition < currencies.size()) {
            Currency baseCurrency = currencies.get(lastChangedPosition);
            if (baseCurrency.getAmount() >= 0) {
                updateCurrencyValues(lastChangedPosition, baseCurrency.getAmount());
            }
        }
    }

    public void updateCurrencyValues(int changedPosition, double value) {
        if (changedPosition < 0 || changedPosition >= currencies.size()) {
            return;
        }

        EditText currentFocused = focusedEditText;

        Currency baseCurrency = currencies.get(changedPosition);
        baseCurrency.setAmount(value);

        double baseValue = value;
        double baseRate = baseCurrency.getRate();

        double baseAmount = (baseRate == 0) ? 0 : (baseValue / baseRate);

        for (int i = 0; i < currencies.size(); i++) {
            if (i != changedPosition) {
                Currency currency = currencies.get(i);
                double newAmount = baseAmount * currency.getRate();
                currency.setAmount(newAmount);
            }
        }

        this.lastChangedPosition = changedPosition;

        notifyItemRangeChanged(0, currencies.size(), PAYLOAD_UPDATE_VALUES);

        if (currentFocused != null && currentFocused.hasFocus()) {
            currentFocused.requestFocus();
        }
    }

    public void updateCurrencyValuesAndForceRefresh(int changedPosition, double value) {
        updateCurrencyValues(changedPosition, value);
        notifyItemChanged(changedPosition, PAYLOAD_FORCE_TEXT_UPDATE);
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
                                currencies.get(position).setAmount(0);
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
                        }
                    }
                }
            };

            editAmount.addTextChangedListener(textWatcher);
        }
    }
}
