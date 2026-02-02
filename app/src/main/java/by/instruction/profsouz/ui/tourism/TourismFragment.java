package by.instruction.profsouz.ui.tourism;

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

public class TourismFragment extends Fragment {
    private FragmentInfoListBinding binding;
    private InfoCardAdapter adapter;
    private final List<Section> sections = new ArrayList<>();

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
        buildSections();
        adapter.submit(buildVisibleCards());
        adapter.setOnItemClickListener(item -> {
            if (item.getId() != null && item.getId().startsWith("section_")) {
                toggleSection(item.getId());
                adapter.submit(buildVisibleCards());
                return;
            }
            if (item.getSiteUrl() == null || item.getSiteUrl().trim().isEmpty()) {
                return;
            }
            startActivity(NewsDetailActivity.createIntent(requireContext(), item.getSiteUrl()));
        });
    }

    private void buildSections() {
        sections.clear();

        Section bases = new Section("section_bases", "БАЗЫ ОТДЫХА");
        bases.items.add(new InfoCard(
                "base_lesnoe_ozero",
                "Туристская база «Лесное озеро»",
                null,
                "https://lesnoeozero.by/",
                null,
                null,
                null,
                null
        ));
        bases.items.add(new InfoCard(
                "base_beloe_ozero",
                "База отдыха «Белое озеро»",
                null,
                "https://beloeozero.by/",
                null,
                null,
                null,
                null
        ));
        bases.items.add(new InfoCard(
                "base_losvido",
                "Туристско-оздоровительный комплекс «Лосвидо»",
                null,
                "https://www.toklosvido.by/",
                null,
                null,
                null,
                null
        ));
        bases.items.add(new InfoCard(
                "base_orsha",
                "Туристско-оздоровительное дочернее унитарное предприятие «Орша»",
                null,
                "http://www.turbaza.by/",
                null,
                null,
                null,
                null
        ));
        bases.items.add(new InfoCard(
                "base_braslav",
                "Туристско-оздоровительное дочернее унитарное предприятие «Браславские озера»",
                null,
                "http://turbras.by/",
                null,
                null,
                null,
                null
        ));
        bases.items.add(new InfoCard(
                "base_pyshki",
                "База отдыха «Пышки»",
                null,
                "http://grodnoturbaza.by/",
                null,
                null,
                null,
                null
        ));
        bases.items.add(new InfoCard(
                "base_vysokiy_bereg",
                "Туристско-оздоровительный комплекс «Высокий берег»",
                null,
                "http://www.vbereg.by/",
                null,
                null,
                null,
                null
        ));
        bases.items.add(new InfoCard(
                "base_fob",
                "Физкультурно-спортивная база ФПБ, Ждановичи",
                null,
                "http://fob.by/",
                null,
                null,
                null,
                null
        ));
        bases.items.add(new InfoCard(
                "base_logoisk",
                "Дом отдыха «Логойский»",
                null,
                "https://belarustourist.by/turbazy/dom-otdykha-logoyskiy/",
                null,
                null,
                null,
                null
        ));

        Section hotels = new Section("section_hotels", "ГОСТИНИЦЫ");
        hotels.items.add(new InfoCard(
                "hotel_gorizont",
                "Гостиница «Горизонт» г. Барановичи",
                null,
                "http://gorizonttour.by/",
                null,
                null,
                null,
                null
        ));
        hotels.items.add(new InfoCard(
                "hotel_belarus_brest",
                "Гостиница «Беларусь» г. Брест",
                null,
                "http://brestturist.by/",
                null,
                null,
                null,
                null
        ));
        hotels.items.add(new InfoCard(
                "hotel_vetraz",
                "Гостиница «Ветразь» г. Витебск",
                null,
                "http://vitebsktourist.by/",
                null,
                null,
                null,
                null
        ));
        hotels.items.add(new InfoCard(
                "hotel_tourist_gomel",
                "Гостиница «Турист» г. Гомель",
                null,
                "http://www.gomeltourist.by/",
                null,
                null,
                null,
                null
        ));
        hotels.items.add(new InfoCard(
                "hotel_tourist_grodno",
                "Гостиница «Турист» г. Гродно",
                null,
                "http://grodnotourist.by/",
                null,
                null,
                null,
                null
        ));
        hotels.items.add(new InfoCard(
                "hotel_tourist_minsk",
                "Гостиница «Турист» г. Минск",
                null,
                "http://www.hotel-tourist.by/",
                null,
                null,
                null,
                null
        ));
        hotels.items.add(new InfoCard(
                "hotel_orbita",
                "Гостиница «Орбита» г. Минск",
                null,
                "http://www.orbita-hotel.com/",
                null,
                null,
                null,
                null
        ));
        hotels.items.add(new InfoCard(
                "hotel_tourist_mogilev",
                "Гостиница «Турист» г. Могилев",
                null,
                "https://mogilevtourist.by/",
                null,
                null,
                null,
                null
        ));

        Section sanatoriums = new Section("section_sanatoriums", "САНАТОРИИ");
        sanatoriums.items.add(new InfoCard(
                "sanatorium_bug",
                "Санаторий «Буг»",
                "Брестская область, Жабинковский район",
                "http://kurort.by/sanatorij/bug.html",
                null,
                null,
                null,
                null
        ));
        sanatoriums.items.add(new InfoCard(
                "sanatorium_letcy",
                "Санаторий «Лётцы»",
                "Витебская область, Витебский район, д. Малые Лётцы",
                "http://kurort.by/sanatorij/ljottsy.html",
                null,
                null,
                null,
                null
        ));
        sanatoriums.items.add(new InfoCard(
                "sanatorium_lesnye_ozera",
                "Санаторий «Лесные озёра»",
                "Витебская обл., Ушачский р-н, дер. Вашково",
                "http://kurort.by/sanatorii/lesnye-ozera.html",
                null,
                null,
                null,
                null
        ));
        sanatoriums.items.add(new InfoCard(
                "sanatorium_pridneprovskiy",
                "Санаторий «Приднепровский»",
                "Гомельская область, Рогачевский район, п. Приднепровский",
                "http://kurort.by/sanatorii/pridneprovskij.html",
                null,
                null,
                null,
                null
        ));
        sanatoriums.items.add(new InfoCard(
                "sanatorium_chenki",
                "Санаторий «Чёнки»",
                "Гомельская обл., Гомельский район, п. Ченки",
                "http://kurort.by/sanatorij/chjonki.html",
                null,
                null,
                null,
                null
        ));
        sanatoriums.items.add(new InfoCard(
                "sanatorium_neman_72",
                "Санаторий «Неман-72»",
                "г. Гродно",
                "http://kurort.by/sanatorij/sanatorij-njoman-72.html",
                null,
                null,
                null,
                null
        ));
        sanatoriums.items.add(new InfoCard(
                "sanatorium_narochanka",
                "Санаторий «Нарочанка»",
                "Минская обл., Мядельский р-н, поселок Нарочь",
                "http://kurort.by/sanatorij/sanatorij-narochanka.html",
                null,
                null,
                null,
                null
        ));
        sanatoriums.items.add(new InfoCard(
                "sanatorium_naroch",
                "Санаторий «Нарочь»",
                "Минская обл., Мядельский р-н, поселок Нарочь",
                "http://kurort.by/sanatorii/naroch.html",
                null,
                null,
                null,
                null
        ));
        sanatoriums.items.add(new InfoCard(
                "sanatorium_krinitsa",
                "Санаторий «Криница»",
                "Минская обл., Минский р-н, а.г. Ждановичи",
                "http://kurort.by/sanatorii/krinitsa.html",
                null,
                null,
                null,
                null
        ));
        sanatoriums.items.add(new InfoCard(
                "sanatorium_belorusochka",
                "Санаторий «Белорусочка»",
                "Минская область, а/г Ждановичи",
                "http://kurort.by/sanatorii/belorusochka.html",
                null,
                null,
                null,
                null
        ));
        sanatoriums.items.add(new InfoCard(
                "sanatorium_svisloch",
                "Детский санаторий «Свислочь»",
                "Могилевская обл., Осиповичский р-н, аг. Свислочь",
                "http://kurort.by/sanatorij/svisloch.html",
                null,
                null,
                null,
                null
        ));
        sanatoriums.items.add(new InfoCard(
                "sanatorium_lenin",
                "Санаторий «Имени В.И. Ленина»",
                "Могилевская область, г. Бобруйск",
                "http://kurort.by/sanatorij/v-i-lenina.html",
                null,
                null,
                null,
                null
        ));

        sections.add(bases);
        sections.add(hotels);
        sections.add(sanatoriums);
    }

    private List<InfoCard> buildVisibleCards() {
        List<InfoCard> cards = new ArrayList<>();
        for (Section section : sections) {
            cards.add(new InfoCard(
                    section.id,
                    section.title,
                    section.expanded ? "Нажмите, чтобы свернуть" : "Нажмите, чтобы раскрыть",
                    null,
                    null,
                    null,
                    null,
                    null
            ));
            if (section.expanded) {
                cards.addAll(section.items);
            }
        }
        return cards;
    }

    private void toggleSection(String sectionId) {
        for (Section section : sections) {
            if (section.id.equals(sectionId)) {
                section.expanded = !section.expanded;
                return;
            }
        }
    }

    private static class Section {
        private final String id;
        private final String title;
        private final List<InfoCard> items = new ArrayList<>();
        private boolean expanded = false;

        private Section(String id, String title) {
            this.id = id;
            this.title = title;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
