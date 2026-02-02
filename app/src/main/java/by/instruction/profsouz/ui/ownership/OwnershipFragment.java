package by.instruction.profsouz.ui.ownership;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import by.instruction.profsouz.databinding.FragmentInfoListBinding;
import by.instruction.profsouz.ui.common.InfoCard;
import by.instruction.profsouz.ui.common.InfoCardAdapter;
import by.instruction.profsouz.ui.home.NewsDetailActivity;

public class OwnershipFragment extends Fragment {
    private FragmentInfoListBinding binding;
    private InfoCardAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInfoListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new InfoCardAdapter();
        binding.infoList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.infoList.setAdapter(adapter);
        adapter.submit(buildCards());
        adapter.setOnItemClickListener(item -> {
            if (item.getSiteUrl() == null || item.getSiteUrl().trim().isEmpty()) {
                return;
            }
            startActivity(NewsDetailActivity.createIntent(requireContext(), item.getSiteUrl()));
        });
    }

    private List<InfoCard> buildCards() {
        List<InfoCard> cards = new ArrayList<>();
        cards.add(new InfoCard(
                "property_belarustourist",
                "Беларустурист",
                null,
                "https://belarustourist.by/",
                null,
                null,
                null,
                null
        ));
        cards.add(new InfoCard(
                "property_mitso",
                "Международный университет «МИТСО»",
                null,
                "https://www.mitso.by/ru/",
                null,
                null,
                null,
                null
        ));
        cards.add(new InfoCard(
                "property_prof_press",
                "Унитарное предприятие «Издательский Дом «Проф-Пресс»",
                buildProfPressDescription(),
                "https://fpb.1prof.by/unitarnoe-predpriyatie-izdatelskij-dom-prof-press/",
                "info@prof-press.by",
                "+375 17 203 84 47",
                null,
                null
        ));
        cards.add(new InfoCard(
                "property_kurort",
                "Система санаториев Федерации профсоюзов Беларуси «Белпрофсоюзкурорт»",
                buildKurortDescription(),
                "https://fpb.1prof.by/sanatorno-kurortnoe-unitarnoe-predpriyatie-belprofsoyuzkurort/",
                "marketing@kurort.by",
                "+375 17 352 96 61, +375 (29) 329 09 09, +375 (29) 129 09 09",
                null,
                null
        ));
        cards.add(new InfoCard(
                "property_sport",
                "Спортивный клуб Федерации профсоюзов Беларуси",
                null,
                "https://sportfpb.by/",
                null,
                null,
                null,
                null
        ));
        cards.add(new InfoCard(
                "property_rdkp",
                "Республиканский Дворец культуры профсоюзов",
                null,
                "https://rdkp.by/",
                null,
                null,
                null,
                null
        ));
        return cards;
    }

    private String buildProfPressDescription() {
        StringBuilder builder = new StringBuilder();
        builder.append("Адрес: 220126, Республика Беларусь, г. Минск, пр. Победителей, 21");
        return builder.toString();
    }

    private String buildKurortDescription() {
        return "Получить консультацию по отдыху и лечению в санаториях системы ФПБ, а также по вопросам сотрудничества, можно по телефонам:";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
