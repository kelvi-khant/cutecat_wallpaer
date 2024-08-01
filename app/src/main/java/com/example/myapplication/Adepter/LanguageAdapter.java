package com.example.myapplication.Adepter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Model.LanguageModel;
import com.example.myapplication.R;

import java.util.List;
import java.util.Locale;
public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {

    private List<LanguageModel> languageList;
    private int lastCheckedPosition = -1;
    private static  Context mContext;

    public LanguageAdapter(Context context, List<LanguageModel> languageList) {
        this.languageList = languageList;
        this.mContext = context;
        loadLastCheckedPosition();
    }
    @Override
    public int getItemCount() {
        return languageList.size();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_language, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LanguageModel languageModel = languageList.get(position);
        holder.flagImageView.setImageResource(languageModel.getLanguageflag());
        holder.tv.setText(languageModel.getLanguageName());

        holder.radioButtonLanguage.setChecked(position == lastCheckedPosition); // Check if this position is last checked position

        holder.radioButtonLanguage.setOnClickListener(v -> {
            handleRadioButtonChecks(holder.getAdapterPosition());
            setLanguage(languageModel.getLanguageCode());
            notifyDataSetChanged(); // Notify adapter about data changes
//            Intent intent = new Intent(mContext, MainActivity.class);
//            mContext.startActivity(intent);
//            if (mContext instanceof Activity) {
//                ((Activity) mContext).finish();
//            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView flagImageView;
        RadioButton radioButtonLanguage;
        TextView tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flagImageView = itemView.findViewById(R.id.flags);
            radioButtonLanguage = itemView.findViewById(R.id.radiobutton);
            tv = itemView.findViewById(R.id.languageNameTextView);
            radioButtonLanguage.setOnClickListener(v -> handleRadioButtonChecks(getAdapterPosition()));
        }
    }

    private void handleRadioButtonChecks(int adapterPosition) {
        if (lastCheckedPosition != -1) {
            languageList.get(lastCheckedPosition).setChecked(false); // Uncheck previous selection
        }
        languageList.get(adapterPosition).setChecked(true); // Check current selection
        lastCheckedPosition = adapterPosition;
        saveLastCheckedPosition(); // Save last checked position to SharedPreferences
    }

    private void saveLastCheckedPosition() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("LAST_CHECKED_POSITION", lastCheckedPosition);
        editor.apply();
    }
    private void loadLastCheckedPosition() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        lastCheckedPosition = sharedPreferences.getInt("LAST_CHECKED_POSITION", -1);
    }
    public void setLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = mContext.getResources();
        Configuration config = resources.getConfiguration();
        config.locale = locale;
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        resources.updateConfiguration(config, displayMetrics);

        SharedPreferences.Editor editor = mContext.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("MY_LANG", languageCode);
        editor.apply();
    }

    public static void setbtn(){

        Intent intent = new Intent(mContext,MainActivity.class);
        mContext.startActivity(intent);
        if (mContext instanceof Activity) {
            ((Activity) mContext).finish();
        }

    }
}
