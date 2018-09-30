package com.inc.mountzoft.jokefactory;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bakerj.infinitecards.AnimationTransformer;
import com.bakerj.infinitecards.CardItem;
import com.bakerj.infinitecards.InfiniteCardView;
import com.bakerj.infinitecards.ZIndexTransformer;
import com.bakerj.infinitecards.transformer.DefaultCommonTransformer;
import com.bakerj.infinitecards.transformer.DefaultTransformerToFront;

import ru.github.igla.ferriswheel.FerrisWheelView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class DisplayJokeActivity extends AppCompatActivity {

    String JOKE_KEY = "joke_key";

    private InfiniteCardView mCardView;
    private FerrisWheelView ferrisWheelView;
    private BaseAdapter mAdapter1, mAdapter2;
    private int[] resId = {R.drawable.pic3, R.drawable.pic1, R.drawable.pic1,
            R.drawable.pic4, R.drawable.pic2};
    private boolean mIsAdapter1 = true;

    InterstitialAd mInterstitialAd;
    private InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_joke);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.string.top_gradient))));

        AdRequest adRequest = new AdRequest.Builder().build();

        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(DisplayJokeActivity.this);
// Insert the Ad Unit ID
        interstitial.setAdUnitId(getString(R.string.ad_unit_id));

        interstitial.loadAd(adRequest);
// Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
// Call displayInterstitial() function
                displayInterstitial();
            }
        });

        Toast.makeText(this, R.string.loading,Toast.LENGTH_LONG).show();

        final TextView textview = findViewById(R.id.joke_text);
        textview.setMovementMethod(new ScrollingMovementMethod());

        final Intent intent = getIntent();
        final String JokeResult = intent.getStringExtra(JOKE_KEY);

        if (JokeResult != null) {
            textview.setText("");
        } else {
            textview.setText(R.string.joke_alt_text);
        }
        textview.setMovementMethod(new ScrollingMovementMethod());

        mCardView = findViewById(R.id.view);
        ferrisWheelView = findViewById(R.id.ferrisWheelView);

        mAdapter1 = new MyAdapter(resId);
        mAdapter2 = new MyAdapter(resId);
        mCardView.setAdapter(mAdapter1);
        mCardView.setCardAnimationListener(new InfiniteCardView.CardAnimationListener() {

            @Override
            public void onAnimationStart() {
                textview.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onAnimationEnd() {
                textview.setText(JokeResult);
                textview.setVisibility(View.VISIBLE);
            }
        });

        new CountDownTimer(7000, 1000) {

            public void onTick(long millisUntilFinished) {
                mCardView.setVisibility(View.INVISIBLE);
                ferrisWheelView.setVisibility(View.VISIBLE);
                //Toast.makeText(getApplicationContext(),"seconds remaining: " + millisUntilFinished / 1000,Toast.LENGTH_SHORT).show();
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                mCardView.setVisibility(View.VISIBLE);
                ferrisWheelView.setVisibility(View.INVISIBLE);
                initButton();
                //Toast.makeText(getApplicationContext(),"Gonna Shuffel!",Toast.LENGTH_SHORT).show();
            }

        }.start();
    }

    public void displayInterstitial() {
// If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    public void initButton() {
        if (mIsAdapter1) {
            setStyle2();
        } else {
            setStyle3();
        }
        if(mCardView != null) {
            mCardView.bringCardToFront(1);
        }
    }

    private void setStyle2() {
        mCardView.setClickable(true);
        mCardView.setAnimType(InfiniteCardView.ANIM_TYPE_SWITCH);
        mCardView.setAnimInterpolator(new OvershootInterpolator(-18));
        mCardView.setTransformerToFront(new DefaultTransformerToFront());
        mCardView.setTransformerToBack(new AnimationTransformer() {
            @Override
            public void transformAnimation(View view, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
                int positionCount = fromPosition - toPosition;
                float scale = (0.8f - 0.1f * fromPosition) + (0.1f * fraction * positionCount);
                view.setScaleX(scale);
                view.setScaleY(scale);
                if (fraction < 0.5) {
                    view.setRotationX(180 * fraction);
                } else {
                    view.setRotationX(180 * (1 - fraction));
                }
            }

            @Override
            public void transformInterpolatedAnimation(View view, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
                int positionCount = fromPosition - toPosition;
                float scale = (0.8f - 0.1f * fromPosition) + (0.1f * fraction * positionCount);
                view.setTranslationY(-cardHeight * (0.8f - scale) * 0.5f - cardWidth * (0.02f *
                        fromPosition - 0.02f * fraction * positionCount));
            }
        });
        mCardView.setZIndexTransformerToBack(new ZIndexTransformer() {
            @Override
            public void transformAnimation(CardItem card, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
                if (fraction < 0.4f) {
                    card.zIndex = 1f + 0.01f * fromPosition;
                } else {
                    card.zIndex = 1f + 0.01f * toPosition;
                }
            }

            @Override
            public void transformInterpolatedAnimation(CardItem card, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {

            }
        });
    }

    private void setStyle3() {
        mCardView.setClickable(false);
        mCardView.setAnimType(InfiniteCardView.ANIM_TYPE_FRONT_TO_LAST);
        mCardView.setAnimInterpolator(new OvershootInterpolator(-8));
        mCardView.setTransformerToFront(new DefaultCommonTransformer());
        mCardView.setTransformerToBack(new AnimationTransformer() {
            @Override
            public void transformAnimation(View view, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
                int positionCount = fromPosition - toPosition;
                float scale = (0.8f - 0.1f * fromPosition) + (0.1f * fraction * positionCount);
                view.setScaleX(scale);
                view.setScaleY(scale);
                if (fraction < 0.5) {
                    view.setTranslationX(cardWidth * fraction * 1.5f);
                    view.setRotationY(-45 * fraction);
                } else {
                    view.setTranslationX(cardWidth * 1.5f * (1f - fraction));
                    view.setRotationY(-45 * (1 - fraction));
                }
            }

            @Override
            public void transformInterpolatedAnimation(View view, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
                int positionCount = fromPosition - toPosition;
                float scale = (0.8f - 0.1f * fromPosition) + (0.1f * fraction * positionCount);
                view.setTranslationY(-cardHeight * (0.8f - scale) * 0.5f - cardWidth * (0.02f *
                        fromPosition - 0.02f * fraction * positionCount));
            }
        });
        mCardView.setZIndexTransformerToBack(new ZIndexTransformer() {
            @Override
            public void transformAnimation(CardItem card, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
                if (fraction < 0.5f) {
                    card.zIndex = 1f + 0.01f * fromPosition;
                } else {
                    card.zIndex = 1f + 0.01f * toPosition;
                }
            }

            @Override
            public void transformInterpolatedAnimation(CardItem card, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {

            }
        });
    }

//=====================================Adapter======================================================
    private static class MyAdapter extends BaseAdapter {
        private int[] resIds = {};

        MyAdapter(int[] resIds) {
            this.resIds = resIds;
        }

        @Override
        public int getCount() {
            return resIds.length;
        }

        @Override
        public Integer getItem(int position) {
            return resIds[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .item_card, parent, false);
            }
            convertView.setBackgroundResource(resIds[position]);
            return convertView;
        }
    }
}
