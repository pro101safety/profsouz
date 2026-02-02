package by.instruction.profsouz.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import by.instruction.profsouz.data.db.AppDatabase;
import by.instruction.profsouz.data.db.FpbSectionEntity;
import by.instruction.profsouz.data.db.NewsEntity;
import by.instruction.profsouz.data.db.TourismEntity;
import by.instruction.profsouz.data.db.UnionEntity;
import by.instruction.profsouz.data.model.FpbSection;
import by.instruction.profsouz.data.model.NewsItem;
import by.instruction.profsouz.data.model.TourismItem;
import by.instruction.profsouz.data.model.UnionDetails;
import by.instruction.profsouz.data.scraper.HtmlScraper;

import org.jsoup.nodes.Document;

public class AppRepository {
    // Cloud backend is currently disabled; repository scrapes directly from sources.
    private final AppDatabase database;
    private final ExecutorService executorService;
    private final HtmlScraper scraper;

    private static final String FPB_STRUCTURE_URL = "https://fpb.1prof.by/organizacionnaya-struktura-fpb/";
    private static final String FPB_TOURISM_URL = "https://fpb.1prof.by/tourism-places/gostinicy/";
    private static final String FPB_NEWS_URL = "https://1prof.by/news/obshhestvo-i-profsoyuzy/";
    private static final List<UnionSource> UNION_SOURCES = Arrays.asList(
            new UnionSource(
                    "prof-gos",
                    "Белорусский профессиональный союз работников государственных и других учреждений",
                    "https://fpb.1prof.by/members/belorusskij-professionalnyj-soyuz-rabotnikov-gosudarstvennyx-i-drugix-uchrezhdenij/"
            ),
            new UnionSource(
                    "prof-torg",
                    "Белорусский профессиональный союз работников торговли, потребительской кооперации и предпринимательства",
                    "https://fpb.1prof.by/members/belorusskij-professionalnyj-soyuz-rabotnikov-torgovli-potrebitelskoj-kooperacii-i-predprinimatelstva/"
            ),
            new UnionSource(
                    "prof-him",
                    "Белорусский профессиональный союз работников химической, горной и нефтяной отраслей промышленности",
                    "https://fpb.1prof.by/members/520/"
            ),
            new UnionSource(
                    "prof-bank",
                    "Белорусский профессиональный союз банковских и финансовых работников",
                    "https://fpb.1prof.by/members/belorusskij-professionalnyj-soyuz-bankovskix-i-finansovyx-rabotnikov/"
            ),
            new UnionSource(
                    "prof-agro",
                    "Белорусский профессиональный союз работников агропромышленного комплекса",
                    "https://fpb.1prof.by/members/belorusskij-professionalnyj-soyuz-rabotnikov-agropromyshlennogo-kompleksa/"
            ),
            new UnionSource(
                    "prof-kultura",
                    "Белорусский профессиональный союз работников культуры, информации, спорта и туризма",
                    "https://fpb.1prof.by/members/belorusskij-professionalnyj-soyuz-rabotnikov-kultury-informacii-sporta-i-turizma/"
            ),
            new UnionSource(
                    "prof-med",
                    "Белорусский профессиональный союз работников здравоохранения",
                    "https://fpb.1prof.by/members/belorusskij-professionalnyj-soyuz-rabotnikov-zdravooxraneniya/"
            ),
            new UnionSource(
                    "prof-les",
                    "Белорусский профессиональный союз работников леса и природопользования",
                    "https://fpb.1prof.by/members/belorusskij-profsoyuz-rabotnikov-lesa-i-prirodopolzovaniya/"
            ),
            new UnionSource(
                    "prof-zhkh",
                    "Белорусский профессиональный союз работников жилищно-коммунального хозяйства и сферы обслуживания",
                    "https://fpb.1prof.by/members/belorusskij-profsoyuz-rabotnikov-mestnoj-promyshlennosti-i-kommunalno-bytovyx-predpriyatij/"
            ),
            new UnionSource(
                    "prof-stroy",
                    "Белорусский профессиональный союз работников строительства и промышленности строительных материалов",
                    "https://fpb.1prof.by/members/belorusskij-profsoyuz-rabotnikov-stroitelstva-i-promyshlennosti-stroitelnyx-materialov/"
            ),
            new UnionSource(
                    "prof-energo",
                    "Белорусский профессиональный союз работников энергетики, газовой и топливной промышленности",
                    "https://fpb.1prof.by/members/belorusskij-profsoyuz-rabotnikov-energetiki-gazovoj-i-toplivnoj-promyshlennosti/"
            ),
            new UnionSource(
                    "prof-trans",
                    "Белорусский профессиональный союз работников транспорта и коммуникаций",
                    "https://fpb.1prof.by/members/belorusskij-professionalnyj-soyuz-rabotnikov-transporta-i-kommunikacij/"
            ),
            new UnionSource(
                    "prof-svyaz",
                    "Белорусский профессиональный союз работников связи",
                    "https://fpb.1prof.by/members/belorusskij-professionalnyj-soyuz-rabotnikov-svyazi/"
            ),
            new UnionSource(
                    "prof-education",
                    "Белорусский профессиональный союз работников образования и науки",
                    "https://fpb.1prof.by/members/belorusskij-profsoyuz-rabotnikov-obrazovaniya-i-nauki/"
            ),
            new UnionSource(
                    "prof-belprofmash",
                    "Белорусский профессиональный союз работников отраслей промышленности «БЕЛПРОФМАШ»",
                    "https://fpb.1prof.by/members/belorusskij-profsoyuz-rabotnikov-otraslej-promyshlennosti-belprofmash/"
            )
    );

    public AppRepository(Context context) {
        this.database = AppDatabase.getInstance(context);
        this.executorService = Executors.newSingleThreadExecutor();
        this.scraper = new HtmlScraper();
    }

    public LiveData<List<NewsEntity>> observeNews() {
        return database.newsDao().observeAll();
    }

    public LiveData<List<FpbSectionEntity>> observeFpbSections() {
        return database.fpbDao().observeAll();
    }

    public LiveData<List<UnionEntity>> observeUnions() {
        return database.unionDao().observeAll();
    }

    public LiveData<List<TourismEntity>> observeTourism() {
        return database.tourismDao().observeAll();
    }

    public void refreshAll(String unionId) {
        executorService.execute(() -> {
            refreshNewsInternal(unionId);
            refreshFpbInternal();
            refreshUnionsInternal();
            refreshTourismInternal();
        });
    }

    public void syncAllBlocking(String unionId) {
        refreshNewsInternal(unionId);
        refreshFpbInternal();
        refreshUnionsInternal();
        refreshTourismInternal();
    }

    public void refreshNews(String unionId) {
        executorService.execute(() -> refreshNewsInternal(unionId));
    }

    public void refreshFpb() {
        executorService.execute(this::refreshFpbInternal);
    }

    public void refreshUnions() {
        executorService.execute(this::refreshUnionsInternal);
    }

    public void refreshTourism() {
        executorService.execute(this::refreshTourismInternal);
    }

    private void refreshNewsInternal(String unionId) {
        try {
            Document baseDoc = scraper.fetchDocument(FPB_NEWS_URL);
            List<NewsItem> items = scraper.parseNews(baseDoc, "1prof.by");
            if (unionId != null && !unionId.trim().isEmpty()) {
                UnionEntity union = resolveUnionById(unionId);
                if (union != null) {
                    if (union.getSiteUrl() != null) {
                        Document unionDoc = scraper.fetchDocument(union.getSiteUrl());
                        items.addAll(scraper.parseNews(unionDoc, union.getName()));
                    }
                    if (union.getTelegramUrl() != null) {
                        String feedUrl = scraper.toTelegramFeedUrl(union.getTelegramUrl());
                        if (feedUrl != null) {
                            Document tgDoc = scraper.fetchDocument(feedUrl);
                            items.addAll(scraper.parseTelegram(tgDoc, union.getName(), union.getTelegramUrl()));
                        }
                    }
                }
            }
            List<NewsEntity> entities = new ArrayList<>();
            for (NewsItem item : items) {
                String id = item.getId();
                if (id == null || id.trim().isEmpty()) {
                    id = item.getUrl();
                }
                if (id == null || id.trim().isEmpty()) {
                    id = item.getTitle();
                }
                entities.add(new NewsEntity(
                        id,
                        item.getTitle(),
                        item.getUrl(),
                        item.getSource(),
                        item.getPublishedAt(),
                        item.getImageUrl(),
                        item.getSummary()
                ));
            }
            database.newsDao().insertAll(entities);
        } catch (IOException ignored) {
        }
    }

    private void refreshFpbInternal() {
        try {
            Document doc = scraper.fetchDocument(FPB_STRUCTURE_URL);
            List<FpbSection> sections = scraper.parseFpb(doc);
            List<FpbSectionEntity> entities = new ArrayList<>();
            for (FpbSection item : sections) {
                String id = item.getId() != null ? item.getId() : item.getTitle();
                entities.add(new FpbSectionEntity(
                        id,
                        item.getTitle(),
                        item.getContent(),
                        item.getImageUrl(),
                        item.getContact(),
                        item.getLinkUrl()
                ));
            }
            database.fpbDao().insertAll(entities);
        } catch (IOException ignored) {
        }
    }

    private void refreshUnionsInternal() {
        List<UnionEntity> entities = new ArrayList<>();
        int position = 0;
        for (UnionSource source : UNION_SOURCES) {
            UnionDetails details = null;
            try {
                Document doc = scraper.fetchDocument(source.pageUrl);
                details = scraper.parseUnionDetails(doc, source.pageUrl);
            } catch (IOException ignored) {
            }
            String name = details != null ? details.getName() : source.title;
            name = normalizeUnionName(name);
            String description = details != null ? details.getDescription() : null;
            String siteUrl = source.pageUrl;
            String email = details != null ? details.getEmail() : null;
            String phone = details != null ? details.getPhone() : null;
            String imageUrl = details != null ? details.getImageUrl() : null;
            entities.add(new UnionEntity(
                    source.id,
                    name,
                    siteUrl,
                    email,
                    phone,
                    null,
                    description,
                    imageUrl,
                    position++
            ));
        }
        database.unionDao().clear();
        database.unionDao().insertAll(entities);
    }

    private void refreshTourismInternal() {
        try {
            Document doc = scraper.fetchDocument(FPB_TOURISM_URL);
            List<TourismItem> tourismItems = scraper.parseTourism(doc);
            List<TourismEntity> entities = new ArrayList<>();
            for (TourismItem item : tourismItems) {
                String id = item.getId() != null ? item.getId() : item.getName();
                entities.add(new TourismEntity(
                        id,
                        item.getName(),
                        item.getRegion(),
                        item.getAddress(),
                        item.getPhone(),
                        item.getEmail(),
                        item.getSiteUrl()
                ));
            }
            database.tourismDao().insertAll(entities);
        } catch (IOException ignored) {
        }
    }

    private UnionEntity resolveUnionById(String unionId) {
        if (unionId == null || unionId.trim().isEmpty()) {
            return null;
        }
        return database.unionDao().getById(unionId);
    }

    private String normalizeUnionName(String name) {
        if (name == null) {
            return null;
        }
        return name.replaceAll("(?iu)\\s*-\\s*Сайт\\s+Федерации\\s+профсоюзов\\s+Беларуси\\s*$", "")
                .replaceAll("(?iu)\\s*-\\s*Сайт\\s+Федерации\\s+профсозов\\s+Беларуси\\s*$", "")
                .trim();
    }

    private static class UnionSource {
        private final String id;
        private final String title;
        private final String pageUrl;

        private UnionSource(String id, String title, String pageUrl) {
            this.id = id;
            this.title = title;
            this.pageUrl = pageUrl;
        }
    }
}
