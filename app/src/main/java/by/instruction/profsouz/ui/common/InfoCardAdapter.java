package by.instruction.profsouz.ui.common;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import by.instruction.profsouz.R;
import by.instruction.profsouz.ui.home.NewsDetailActivity;

public class InfoCardAdapter extends RecyclerView.Adapter<InfoCardAdapter.CardViewHolder> {
    private final List<InfoCard> items = new ArrayList<>();
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void submit(List<InfoCard> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        InfoCard item = items.get(position);
        boolean isSection = isSectionHeader(item);
        holder.title.setText(item.getTitle());
        if (item.getSubtitle() == null || item.getSubtitle().trim().isEmpty()) {
            holder.subtitle.setVisibility(View.GONE);
        } else {
            holder.subtitle.setVisibility(View.VISIBLE);
            holder.subtitle.setText(item.getSubtitle());
        }

        bindAction(holder.site, item.getSiteUrl(), ActionType.LINK, "Официальный сайт");
        bindAction(holder.email, item.getEmail(), ActionType.EMAIL, null);
        bindAction(holder.phone, item.getPhone(), ActionType.PHONE, null);
        bindAction(holder.telegram, item.getTelegramUrl(), ActionType.LINK, "Telegram");
        bindImage(holder.image, item.getImageUrl());
        styleSectionHeader(holder, isSection);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void bindAction(TextView view, String value, ActionType actionType, String displayText) {
        if (value == null || value.trim().isEmpty()) {
            view.setVisibility(View.GONE);
            return;
        }
        view.setVisibility(View.VISIBLE);
        view.setText(displayText != null ? displayText : value);
        view.setOnClickListener(v -> {
            if (actionType == ActionType.LINK) {
                v.getContext().startActivity(NewsDetailActivity.createIntent(v.getContext(), normalizeLink(value)));
                return;
            }
            Uri uri = buildUri(value, actionType);
            if (uri != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                v.getContext().startActivity(intent);
            }
        });
    }

    private void bindImage(ImageView view, String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            view.setVisibility(View.GONE);
            return;
        }
        view.setVisibility(View.VISIBLE);
        Glide.with(view.getContext())
                .load(imageUrl)
                .centerCrop()
                .into(view);
    }

    private Uri buildUri(String value, ActionType actionType) {
        if (actionType == ActionType.EMAIL) {
            String email = value.startsWith("mailto:") ? value : "mailto:" + value;
            return Uri.parse(email);
        }
        if (actionType == ActionType.PHONE) {
            String phone = value.startsWith("tel:") ? value : "tel:" + value.replace(" ", "");
            return Uri.parse(phone);
        }
        String link = value.startsWith("http") ? value : "https://" + value;
        return Uri.parse(link);
    }

    private String normalizeLink(String value) {
        if (value == null) {
            return null;
        }
        if (value.startsWith("http")) {
            return value;
        }
        return "https://" + value;
    }

    private boolean isSectionHeader(InfoCard item) {
        return item.getId() != null && item.getId().startsWith("section_");
    }

    private void styleSectionHeader(CardViewHolder holder, boolean isSection) {
        if (isSection) {
            int accentColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.fpb_blue);
            holder.title.setTextColor(accentColor);
            holder.subtitle.setTextColor(accentColor);
            holder.card.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.fpb_blue_container));
            holder.card.setStrokeWidth(holder.defaultStrokeWidth + 2);
            holder.card.setStrokeColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.fpb_blue_light));
        } else {
            holder.title.setTextColor(holder.defaultTitleColor);
            holder.subtitle.setTextColor(holder.defaultSubtitleColor);
            holder.card.setCardBackgroundColor(holder.defaultCardBackground);
            holder.card.setStrokeWidth(holder.defaultStrokeWidth);
            holder.card.setStrokeColor(holder.defaultStrokeColor);
        }
    }

    private enum ActionType {
        LINK,
        EMAIL,
        PHONE
    }

    public interface OnItemClickListener {
        void onItemClick(InfoCard item);
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        final MaterialCardView card;
        final ImageView image;
        final TextView title;
        final TextView subtitle;
        final TextView site;
        final TextView email;
        final TextView phone;
        final TextView telegram;
        final int defaultTitleColor;
        final int defaultSubtitleColor;
        final int defaultStrokeWidth;
        final android.content.res.ColorStateList defaultCardBackground;
        final int defaultStrokeColor;

        CardViewHolder(@NonNull View itemView) {
            super(itemView);
            card = (MaterialCardView) itemView;
            image = itemView.findViewById(R.id.info_image);
            title = itemView.findViewById(R.id.info_title);
            subtitle = itemView.findViewById(R.id.info_subtitle);
            site = itemView.findViewById(R.id.info_site);
            email = itemView.findViewById(R.id.info_email);
            phone = itemView.findViewById(R.id.info_phone);
            telegram = itemView.findViewById(R.id.info_telegram);
            defaultTitleColor = title.getCurrentTextColor();
            defaultSubtitleColor = subtitle.getCurrentTextColor();
            defaultStrokeWidth = card.getStrokeWidth();
            defaultCardBackground = card.getCardBackgroundColor();
            defaultStrokeColor = card.getStrokeColor();
        }
    }
}
