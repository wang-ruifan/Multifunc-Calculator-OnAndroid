package com.ruifan_wang.calculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ruifan_wang.calculator.api.ExchangeRateClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExchangeActivity extends AppCompatActivity implements CurrencyAdapter.CurrencyValueChangeListener,
        CurrencyItemTouchHelperCallback.OrderChangeListener {

    private RecyclerView recyclerView;
    private CurrencyAdapter adapter;
    private List<Currency> currencies;
    private ActivityResultLauncher<Intent> logActivityLauncher;
    private ItemTouchHelper itemTouchHelper;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    // 添加一个变量来跟踪最后聚焦的货币位置
    private int lastFocusedPosition = 0;

    private static final String PREF_CURRENCY_ORDER = "currency_order";
    private static final String PREFS_NAME = "exchange_preferences";
    private static final String PREF_LAST_UPDATE = "last_update_time";
    private static final String PREF_EXCHANGE_RATES = "exchange_rates";
    private static final String BASE_CURRENCY = "CNY";  // 基准货币，使用人民币

    // 示例汇率数据（作为备用，当网络获取失败时使用）
    private final Map<String, Double> fallbackExchangeRates = new HashMap<String, Double>() {{
        put("CNY", 1.0);        // 人民币
        put("HKD", 1.0787);     // 港币
        put("USD", 0.1382);     // 美元
        put("EUR", 0.1271);     // 欧元
        put("AUD", 0.21);       // 澳元
        put("GBP", 0.1077);     // 英镑
        put("RUB", 12.40);      // 卢布
        put("CAD", 0.1878);     // 加元
        put("KRW", 189.04);     // 韩元
        put("JPY", 21.65);      // 日元
    }};

    // 当前使用的汇率数据
    private Map<String, Double> exchangeRates;

    // 货币图标资源名称映射
    private final Map<String, String> flagResourceNames = new HashMap<String, String>() {{
        put("CNY", "flag_cn");
        put("HKD", "flag_hk");
        put("USD", "flag_us");
        put("EUR", "flag_eu");
        put("AUD", "flag_au");
        put("GBP", "flag_gb");
        put("RUB", "flag_ru");
        put("CAD", "flag_ca");
        put("KRW", "flag_kr");
        put("JPY", "flag_jp");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 获取从MainActivity传来的计算结果（如果有）
        String calculatorResult = getIntent().getStringExtra("calc_result");

        // 初始化UI组件
        recyclerView = findViewById(R.id.recycler_currency);
        progressBar = findViewById(R.id.progress_bar);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        // 设置下拉刷新监听器
        swipeRefreshLayout.setOnRefreshListener(this::fetchExchangeRates);

        // 从SharedPreferences加载之前缓存的汇率，如果有的话
        loadCachedExchangeRates();

        // 初始化货币列表
        initCurrencies();

        // 初始化RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CurrencyAdapter(this, currencies);
        adapter.setCurrencyValueChangeListener(this);

        // 设置焦点变化监听器，以跟踪最后聚焦的位置
        adapter.setFocusChangeListener(position -> lastFocusedPosition = position);

        // 初始化拖拽排序功能
        ItemTouchHelper.Callback callback = new CurrencyItemTouchHelperCallback(adapter, this);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        adapter.setTouchHelper(itemTouchHelper);

        recyclerView.setAdapter(adapter);

        // 获取最新汇率
        fetchExchangeRates();

        // 如果有计算结果，设置到第一个货币
        if (calculatorResult != null && !calculatorResult.isEmpty()) {
            try {
                double initialValue = Double.parseDouble(calculatorResult);
                currencies.get(0).setAmount(initialValue);
                adapter.updateCurrencyValues(0, initialValue);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "输入的金额格式无效", Toast.LENGTH_SHORT).show();
            }
        }

        logActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        if (!data.getBooleanExtra("isempty", true)) {
                            String chosen_answer = data.getStringExtra("chosen_answer");
                            if (chosen_answer != null && !chosen_answer.isEmpty()) {
                                try {
                                    double value = Double.parseDouble(chosen_answer);
                                    // 使用最后聚焦的位置填充历史记录结果
                                    currencies.get(lastFocusedPosition).setAmount(value); // 先更新数据模型
                                    adapter.updateCurrencyValuesAndForceRefresh(lastFocusedPosition, value); // 调用新方法
                                } catch (NumberFormatException e) {
                                    Toast.makeText(this, "输入的金额格式无效", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
        );
    }

    /**
     * 从API获取最新汇率
     */
    private void fetchExchangeRates() {
        progressBar.setVisibility(View.VISIBLE);

        // 准备需要获取的货币代码数组
        String[] currencyCodes = getResources().getStringArray(R.array.currency_array);

        // 创建API客户端并获取汇率
        ExchangeRateClient client = ExchangeRateClient.getInstance();
        client.getLatestRates(BASE_CURRENCY, currencyCodes, new ExchangeRateClient.OnExchangeRateListener() {
            @Override
            public void onSuccess(Map<String, Double> rates) {
                // 保存获取到的汇率
                exchangeRates = rates;

                // 确保基准货币的汇率为1.0
                exchangeRates.put(BASE_CURRENCY, 1.0);

                // 更新货币列表中的汇率
                updateCurrencyRates();

                // 缓存汇率数据
                cacheExchangeRates();

                // 隐藏加载指示器
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(ExchangeActivity.this, "汇率更新成功", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                // 如果获取失败，使用备用汇率
                if (exchangeRates == null) {
                    exchangeRates = new HashMap<>(fallbackExchangeRates);
                    updateCurrencyRates();
                }

                // 隐藏加载指示器
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(ExchangeActivity.this, "获取汇率失败: " + errorMessage, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    /**
     * 从SharedPreferences加载缓存的汇率数据
     */
    private void loadCachedExchangeRates() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Map<String, ?> allEntries = prefs.getAll();

        exchangeRates = new HashMap<>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(PREF_EXCHANGE_RATES)) {
                String currencyCode = key.substring(PREF_EXCHANGE_RATES.length() + 1);
                double rate = Double.parseDouble(entry.getValue().toString());
                exchangeRates.put(currencyCode, rate);
            }
        }

        // 如果没有缓存的汇率或缓存不完整，使用备用汇率
        if (exchangeRates.isEmpty()) {
            exchangeRates = new HashMap<>(fallbackExchangeRates);
        }
    }

    /**
     * 将汇率数据缓存到SharedPreferences
     */
    private void cacheExchangeRates() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // 保存每个货币的汇率
        for (Map.Entry<String, Double> entry : exchangeRates.entrySet()) {
            editor.putFloat(PREF_EXCHANGE_RATES + "_" + entry.getKey(), entry.getValue().floatValue());
        }

        // 保存最后更新时间
        editor.putLong(PREF_LAST_UPDATE, new Date().getTime());

        editor.apply();
    }

    /**
     * 更新货币列表中的汇率
     */
    private void updateCurrencyRates() {
        if (currencies == null || exchangeRates == null) return;

        for (Currency currency : currencies) {
            Double rate = exchangeRates.get(currency.getCode());
            if (rate != null) {
                currency.setRate(rate);
            }
        }

        // 如果有任何货币已经设置了金额，重新计算所有货币金额
        for (int i = 0; i < currencies.size(); i++) {
            if (currencies.get(i).getAmount() > 0) {
                adapter.updateCurrencyValues(i, currencies.get(i).getAmount());
                break;
            }
        }
    }

    private void initCurrencies() {
        currencies = new ArrayList<>();
        String[] currencyCodes = getResources().getStringArray(R.array.currency_array);

        // 尝试从SharedPreferences加载自定义排序
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedOrder = prefs.getString(PREF_CURRENCY_ORDER, null);
        String[] orderedCodes;

        if (savedOrder != null && !savedOrder.isEmpty()) {
            // 如果有保存的顺序，使用它
            orderedCodes = savedOrder.split(",");
        } else {
            // 否则使用默认顺序
            orderedCodes = currencyCodes;
        }

        // 根据顺序创建货币对象
        for (String code : orderedCodes) {
            Double rate = exchangeRates != null ? exchangeRates.get(code) : null;
            if (rate == null) {
                rate = fallbackExchangeRates.get(code);
                if (rate == null) {
                    rate = 1.0;  // 默认汇率
                }
            }

            String flagResName = flagResourceNames.get(code);
            if (flagResName == null) {
                flagResName = "ic_launcher_foreground";  // 默认图标
            }

            currencies.add(new Currency(code, flagResName, rate));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_exchange, menu);
        setTitle("汇率计算器");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_log) {
            Intent intent = new Intent(this, LogActivity.class);
            logActivityLauncher.launch(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCurrencyValueChanged(int position, double value) {
        adapter.updateCurrencyValues(position, value);
    }

    /**
     * 实现OrderChangeListener接口方法，在货币顺序变化时保存顺序
     */
    @Override
    public void onOrderChanged() {
        saveCurrencyOrder();
    }

    /**
     * 保存当前货币的排序顺序到SharedPreferences
     */
    private void saveCurrencyOrder() {
        if (currencies == null || currencies.isEmpty()) {
            return;
        }

        // 构建货币代码的逗号分隔字符串
        StringBuilder orderBuilder = new StringBuilder();
        for (int i = 0; i < currencies.size(); i++) {
            orderBuilder.append(currencies.get(i).getCode());
            if (i < currencies.size() - 1) {
                orderBuilder.append(",");
            }
        }

        // 保存到SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putString(PREF_CURRENCY_ORDER, orderBuilder.toString()).apply();
        Toast.makeText(this, "货币排序已保存", Toast.LENGTH_SHORT).show();
    }
}
