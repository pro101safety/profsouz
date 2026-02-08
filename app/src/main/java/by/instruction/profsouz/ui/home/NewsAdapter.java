package by.instruction.profsouz.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import by.instruction.profsouz.R;
import by.instruction.profsouz.data.db.NewsEntity;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private final List<NewsEntity> items = new ArrayList<>();

    public void submit(List<NewsEntity> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        sortByTodayFirst(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsEntity item = items.get(position);
        holder.title.setText(item.getTitle());
        if (item.getSource() == null || item.getSource().trim().isEmpty()) {
            holder.source.setVisibility(View.GONE);
        } else {
            holder.source.setVisibility(View.VISIBLE);
            holder.source.setText(item.getSource());
        }
        if (item.getSummary() == null || item.getSummary().trim().isEmpty()) {
            holder.summary.setVisibility(View.GONE);
        } else {
            holder.summary.setVisibility(View.VISIBLE);
            holder.summary.setText(item.getSummary());
        }
        holder.date.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(v -> {
            if (item.getUrl() != null && !item.getUrl().trim().isEmpty()) {
                Intent intent = new Intent(v.getContext(), NewsDetailActivity.class);
                intent.putExtra(NewsDetailActivity.EXTRA_URL, item.getUrl());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void sortByTodayFirst(List<NewsEntity> news) {
        if (news == null || news.size() <= 1) {
            return;
        }
        Collections.sort(news, (left, right) -> {
            boolean leftToday = isToday(left.getPublishedAt());
            boolean rightToday = isToday(right.getPublishedAt());
            if (leftToday != rightToday) {
                return leftToday ? -1 : 1;
            }
            long leftTime = left.getPublishedAt();
            long rightTime = right.getPublishedAt();
            if (leftTime == rightTime) {
                return 0;
            }
            if (leftTime == 0) {
                return 1;
            }
            if (rightTime == 0) {
                return -1;
            }
            return Long.compare(rightTime, leftTime);
        });
    }

    private boolean isToday(long timestamp) {
        if (timestamp <= 0) {
            return false;
        }
        Calendar today = Calendar.getInstance();
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(timestamp);
        return today.get(Calendar.YEAR) == date.get(Calendar.YEAR)
                && today.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR);
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView source;
        final TextView date;
        final TextView summary;
        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.news_title);
            source = itemView.findViewById(R.id.news_source);
            date = itemView.findViewById(R.id.news_date);
            summary = itemView.findViewById(R.id.news_summary);
        }
    }
}
