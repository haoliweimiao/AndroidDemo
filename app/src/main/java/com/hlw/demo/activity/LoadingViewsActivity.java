package com.hlw.demo.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hlw.demo.R;
import com.hlw.demo.base.BaseActivity;
import com.hlw.demo.databinding.ActivityLoadingViewsBinding;
import com.hlw.loading.LoadingIndicatorView;

/**
 * LoadingViewsActivity
 *
 * @author hlw
 */
public class LoadingViewsActivity extends BaseActivity<ActivityLoadingViewsBinding> {

    /**
     * start to LoadingViewsActivity
     *
     * @param context context
     */
    public static void start(Context context) {
        Intent intent = new Intent(context, LoadingViewsActivity.class);
        context.startActivity(intent);
    }

    /**
     * loading view indicators
     */
    private static final String[] INDICATORS = new String[]{
            "BallPulseIndicator",
            "BallGridPulseIndicator",
            "BallClipRotateIndicator",
            "BallClipRotatePulseIndicator",
            "SquareSpinIndicator",
            "BallClipRotateMultipleIndicator",
            "BallPulseRiseIndicator",
            "BallRotateIndicator",
            "CubeTransitionIndicator",
            "BallZigZagIndicator",
            "BallZigZagDeflectIndicator",
            "BallTrianglePathIndicator",
            "BallScaleIndicator",
            "LineScaleIndicator",
            "LineScalePartyIndicator",
            "BallScaleMultipleIndicator",
            "BallPulseSyncIndicator",
            "BallBeatIndicator",
            "LineScalePulseOutIndicator",
            "LineScalePulseOutRapidIndicator",
            "BallScaleRippleIndicator",
            "BallScaleRippleMultipleIndicator",
            "BallSpinFadeLoaderIndicator",
            "LineSpinFadeLoaderIndicator",
            "TriangleSkewSpinIndicator",
            "PacmanIndicator",
            "BallGridBeatIndicator",
            "SemiCircleSpinIndicator",
            "BallCircleRotateIndicator",
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_loading_views;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        mBinding.recycler.setLayoutManager(layoutManager);
        mBinding.recycler.setAdapter(new RecyclerView.Adapter<IndicatorHolder>() {

            @NonNull
            @Override
            public IndicatorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = getLayoutInflater().inflate(R.layout.item_indicator, parent, false);
                return new IndicatorHolder(itemView);
            }

            @Override
            public void onBindViewHolder(@NonNull IndicatorHolder holder, @SuppressLint("RecyclerView") final int position) {
                holder.mIndicatorView.setIndicator(INDICATORS[position]);
                holder.mItemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(LoadingViewsActivity.this, INDICATORS[position], Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public int getItemCount() {
                return INDICATORS.length;
            }
        });
    }

    @Override
    protected void initListener() {

    }

    /**
     * IndicatorHolder
     *
     * @author hlw
     */
    static final class IndicatorHolder extends RecyclerView.ViewHolder {

        /**
         * IndicatorView
         */
        LoadingIndicatorView mIndicatorView;
        /**
         * ItemLayout
         */
        View mItemLayout;

        IndicatorHolder(View itemView) {
            super(itemView);
            mItemLayout = itemView.findViewById(R.id.itemLayout);
            mIndicatorView = itemView.findViewById(R.id.indicator);
        }
    }
}
