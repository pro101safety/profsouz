package by.instruction.profsouz.data.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import by.instruction.profsouz.data.model.FpbSection;
import by.instruction.profsouz.data.model.NewsItem;
import by.instruction.profsouz.data.model.TourismItem;
import by.instruction.profsouz.data.model.UnionDetails;
import by.instruction.profsouz.data.model.UnionInfo;

public class HtmlScraper {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) FPB-Aggregator/1.0";
    private static final int TIMEOUT_MS = 15000;

    public Document fetchDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent(USER_AGENT)
                .timeout(TIMEOUT_MS)
                .get();
    }

    public List<NewsItem> parseNews(Document document, String sourceName) {
        List<NewsItem> items = new ArrayList<>();
        Elements links = document.select("article a[href], .news-list a[href], .news-item a[href], .post a[href], .posts a[href], .entry-content a[href], .archive a[href]");
        if (links.isEmpty()) {
            Element main = document.selectFirst("main");
            if (main != null) {
                links = main.select("a[href]");
            } else {
                links = document.select("a[href]");
            }
        }
        Set<String> seen = new HashSet<>();
        for (Element link : links) {
            if (link.closest("nav, header, footer, .menu, .menu-item, .navbar") != null) {
                continue;
            }
            String href = link.absUrl("href");
            String text = link.text().trim();
            if (href.isEmpty() || text.length() < 6) {
                continue;
            }
            if (!href.contains("/news/") && !href.contains("/profmedia/")) {
                continue;
            }
            if (!seen.add(href)) {
                continue;
            }
            String title = text;
            Element article = link.closest("article");
            Element container = article != null ? article : link.closest(".news-item, .news, .post, .entry, .archive, .posts, .news-list");
            if (article != null) {
                Element heading = article.selectFirst("h1, h2, h3");
                if (heading != null) {
                    String headingText = heading.text().trim();
                    if (headingText.length() >= 6) {
                        title = headingText;
                    }
                }
            }
            NewsItem item = new NewsItem();
            setField(item, "id", href);
            setField(item, "title", title);
            setField(item, "url", href);
            setField(item, "source", sourceName);
            setField(item, "publishedAt", System.currentTimeMillis());
            String imageUrl = extractImageUrl(link, container != null ? container : article);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                setField(item, "imageUrl", imageUrl);
            }
            if (article != null) {
                Element summary = article.selectFirst("p");
                if (summary != null) {
                    String summaryText = summary.text().trim();
                    if (!summaryText.isEmpty()) {
                        setField(item, "summary", summaryText);
                    }
                }
            }
            items.add(item);
            if (items.size() >= 60) {
                break;
            }
        }
        return items;
    }

    public List<UnionInfo> parseUnions(Document document) {
        List<UnionInfo> unions = new ArrayList<>();
        Elements links = document.select("a[href]");
        for (Element link : links) {
            String href = link.absUrl("href");
            String text = link.text().trim();
            if (href.isEmpty() || text.length() < 6) {
                continue;
            }
            if (!href.contains("1prof.by") && !href.contains("t.me")) {
                continue;
            }
            UnionInfo info = new UnionInfo();
            setField(info, "id", slugify(text));
            setField(info, "name", text);
            if (href.contains("t.me")) {
                setField(info, "telegramUrl", href);
            } else {
                setField(info, "siteUrl", href);
            }
            unions.add(info);
        }
        if (!unions.isEmpty()) {
            return unions;
        }
        Elements listItems = document.select("li");
        for (Element item : listItems) {
            String text = item.text().trim();
            if (text.length() < 6) {
                continue;
            }
            UnionInfo info = new UnionInfo();
            setField(info, "id", slugify(text));
            setField(info, "name", text);
            unions.add(info);
        }
        return unions;
    }

    public UnionDetails parseUnionDetails(Document document, String pageUrl) {
        if (document == null) {
            return null;
        }
        Element content = document.selectFirst("article");
        if (content == null) {
            content = document.selectFirst(".entry-content, .content, .post-content, .page-content");
        }
        if (content == null) {
            content = document.body();
        }

        String name = textOrNull(document.selectFirst("h1"));
        if (name == null || name.isEmpty()) {
            name = textOrNull(document.selectFirst("title"));
        }
        String address = extractValueByLabels(content, "Адрес");
        String phone = extractValueByLabels(content, "Тел", "Тел.", "Тел/Факс", "Телефон");
        String email = extractValueByLabels(content, "E-mail", "Email", "E-mail:");
        String siteUrl = extractValueByLabels(content, "Официальный сайт");
        if (siteUrl != null && !siteUrl.startsWith("http")) {
            siteUrl = "https://" + siteUrl;
        }

        Element chairElement = findChairElement(content);
        String chairName = textOrNull(chairElement);
        String chairTitle = null;
        Element chairTitleElement = findNextTextElement(chairElement);
        if (chairTitleElement != null) {
            chairTitle = chairTitleElement.text().trim();
        }
        String imageUrl = extractImageUrl(chairElement != null ? chairElement : content, content);

        String description = buildUnionDescription(content, chairElement, chairTitleElement);

        return new UnionDetails(
                name,
                address,
                phone,
                email,
                siteUrl,
                chairName,
                chairTitle,
                description,
                imageUrl
        );
    }

    public List<FpbSection> parseFpb(Document document) {
        List<FpbSection> sections = new ArrayList<>();
        Elements links = document.select("a[href]");
        for (Element link : links) {
            String href = link.absUrl("href");
            String text = link.text().trim();
            if (href.isEmpty() || text.length() < 4) {
                continue;
            }
            if (!href.contains("fpb.1prof.by")) {
                continue;
            }
            FpbSection section = new FpbSection();
            setField(section, "id", slugify(text));
            setField(section, "title", text);
            setField(section, "linkUrl", href);
            String imageUrl = extractImageUrl(link);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                setField(section, "imageUrl", imageUrl);
            }
            sections.add(section);
            if (sections.size() >= 50) {
                break;
            }
        }
        if (!sections.isEmpty()) {
            return sections;
        }

        Elements listItems = document.select("li");
        for (Element item : listItems) {
            String text = item.text().trim();
            if (text.length() < 6) {
                continue;
            }
            FpbSection section = new FpbSection();
            setField(section, "id", slugify(text));
            setField(section, "title", text);
            sections.add(section);
        }
        Elements headers = document.select("h1, h2, h3");
        for (Element header : headers) {
            String text = header.text().trim();
            if (text.length() < 6) {
                continue;
            }
            FpbSection section = new FpbSection();
            setField(section, "id", slugify(text));
            setField(section, "title", text);
            sections.add(section);
        }
        return sections;
    }

    public List<TourismItem> parseTourism(Document document) {
        List<TourismItem> items = new ArrayList<>();
        Elements listItems = document.select("li");
        for (Element item : listItems) {
            String text = item.text().trim();
            if (text.length() < 6) {
                continue;
            }
            TourismItem tourism = new TourismItem();
            setField(tourism, "id", slugify(text));
            setField(tourism, "name", text);
            items.add(tourism);
        }
        return items;
    }

    public List<NewsItem> parseTelegram(Document document, String sourceName, String channelUrl) {
        List<NewsItem> items = new ArrayList<>();
        Elements messages = document.select(".tgme_widget_message_text");
        int index = 0;
        for (Element message : messages) {
            String text = message.text().trim();
            if (text.isEmpty()) {
                continue;
            }
            NewsItem item = new NewsItem();
            setField(item, "id", channelUrl + "#" + index++);
            setField(item, "title", truncate(text, 120));
            setField(item, "url", channelUrl);
            setField(item, "source", sourceName);
            setField(item, "summary", text);
            setField(item, "publishedAt", System.currentTimeMillis());
            items.add(item);
            if (items.size() >= 30) {
                break;
            }
        }
        return items;
    }

    public String toTelegramFeedUrl(String telegramUrl) {
        if (telegramUrl == null) {
            return null;
        }
        String normalized = telegramUrl.replace("https://t.me/", "");
        if (normalized.startsWith("s/")) {
            return "https://t.me/" + normalized;
        }
        return "https://t.me/s/" + normalized;
    }

    private String truncate(String value, int max) {
        if (value == null || value.length() <= max) {
            return value;
        }
        return value.substring(0, max);
    }

    private String extractImageUrl(Element link, Element article) {
        Element image = null;
        if (article != null) {
            image = article.selectFirst("img, source");
        }
        if (image == null) {
            image = link.selectFirst("img, source");
        }
        if (image == null && link.parent() != null) {
            image = link.parent().selectFirst("img, source");
        }
        if (image == null && link.parent() != null && link.parent().parent() != null) {
            image = link.parent().parent().selectFirst("img, source");
        }
        String imageUrl = extractImageFromElement(image);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            return imageUrl;
        }
        if (article != null) {
            Element styled = article.selectFirst("[style*=background-image]");
            String styledUrl = extractFromStyle(styled != null ? styled.attr("style") : null, article.baseUri());
            if (styledUrl != null && !styledUrl.isEmpty()) {
                return styledUrl;
            }
        }
        return extractImageFromElement(link);
    }

    private String extractImageFromElement(Element element) {
        if (element == null) {
            return null;
        }
        String[] attrs = new String[] {
                "data-src",
                "data-lazy-src",
                "data-original",
                "data-image",
                "data-thumb",
                "data-srcset",
                "srcset",
                "src"
        };
        for (String attr : attrs) {
            if (!element.hasAttr(attr)) {
                continue;
            }
            String raw = element.attr(attr);
            if (raw == null || raw.trim().isEmpty()) {
                continue;
            }
            String value = raw;
            if ("data-srcset".equals(attr) || "srcset".equals(attr)) {
                value = extractFromSrcset(raw);
            }
            if (value == null || value.trim().isEmpty()) {
                continue;
            }
            String resolved = resolveUrl(element.baseUri(), value.trim());
            if (resolved != null && !resolved.isEmpty()) {
                return resolved;
            }
        }
        return null;
    }

    private String extractFromSrcset(String srcset) {
        if (srcset == null) {
            return null;
        }
        String[] parts = srcset.split(",");
        String candidate = null;
        for (String part : parts) {
            String trimmed = part.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            int spaceIndex = trimmed.indexOf(' ');
            String url = spaceIndex > 0 ? trimmed.substring(0, spaceIndex) : trimmed;
            candidate = url;
        }
        return candidate;
    }

    private String extractFromStyle(String style, String baseUri) {
        if (style == null || style.isEmpty()) {
            return null;
        }
        Pattern pattern = Pattern.compile("url\\(['\"]?(.*?)['\"]?\\)");
        Matcher matcher = pattern.matcher(style);
        if (!matcher.find()) {
            return null;
        }
        String url = matcher.group(1);
        return resolveUrl(baseUri, url);
    }

    private String resolveUrl(String baseUri, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        if (value.startsWith("http")) {
            return value;
        }
        if (baseUri == null || baseUri.isEmpty()) {
            return value;
        }
        try {
            URI base = new URI(baseUri);
            return base.resolve(value).toString();
        } catch (URISyntaxException ignored) {
            return value;
        }
    }

    private String extractImageUrl(Element link) {
        return extractImageUrl(link, null);
    }

    private String extractValueByLabels(Element scope, String... labels) {
        if (scope == null) {
            return null;
        }
        Elements candidates = scope.select("p, li, div, span");
        for (Element candidate : candidates) {
            String text = candidate.text();
            if (text == null || text.isEmpty()) {
                continue;
            }
            for (String label : labels) {
                int index = text.indexOf(label);
                if (index < 0) {
                    continue;
                }
                if (label.toLowerCase().contains("официальный")) {
                    Element link = candidate.selectFirst("a[href]");
                    if (link != null) {
                        String href = link.absUrl("href");
                        if (href == null || href.isEmpty()) {
                            href = link.attr("href");
                        }
                        if (href != null && !href.isEmpty()) {
                            return href;
                        }
                    }
                }
                int colonIndex = text.indexOf(":", index + label.length());
                if (colonIndex < 0) {
                    continue;
                }
                String value = text.substring(colonIndex + 1).trim();
                if (!value.isEmpty()) {
                    return value;
                }
            }
        }
        return null;
    }

    private Element findChairElement(Element scope) {
        if (scope == null) {
            return null;
        }
        Elements candidates = scope.select("h2, h3, strong, b");
        for (Element candidate : candidates) {
            String text = candidate.text();
            if (isLikelyPersonName(text)) {
                return candidate;
            }
        }
        return null;
    }

    private Element findNextTextElement(Element element) {
        if (element == null) {
            return null;
        }
        Element current = element.nextElementSibling();
        while (current != null) {
            if (!current.text().trim().isEmpty()) {
                return current;
            }
            current = current.nextElementSibling();
        }
        Element parent = element.parent();
        if (parent != null) {
            current = parent.nextElementSibling();
            while (current != null) {
                if (!current.text().trim().isEmpty()) {
                    return current;
                }
                current = current.nextElementSibling();
            }
        }
        return null;
    }

    private boolean isLikelyPersonName(String value) {
        if (value == null) {
            return false;
        }
        String trimmed = value.trim();
        if (trimmed.length() < 6 || trimmed.length() > 120 || !trimmed.contains(" ")) {
            return false;
        }
        Pattern pattern = Pattern.compile("[A-ZА-ЯЁ]");
        Matcher matcher = pattern.matcher(trimmed);
        int uppercaseCount = 0;
        while (matcher.find()) {
            uppercaseCount++;
        }
        int letterCount = trimmed.replaceAll("[^A-Za-zА-Яа-яЁё]", "").length();
        if (letterCount == 0) {
            return false;
        }
        double ratio = (double) uppercaseCount / (double) letterCount;
        return ratio > 0.6;
    }

    private String buildUnionDescription(Element scope, Element chairElement, Element chairTitleElement) {
        StringBuilder builder = new StringBuilder();
        if (scope == null) {
            return null;
        }
        String address = extractValueByLabels(scope, "Адрес");
        if (address != null) {
            builder.append("Адрес: ").append(address);
        }
        String chairName = textOrNull(chairElement);
        if (chairName != null) {
            appendNewSection(builder);
            builder.append(chairName);
        }
        if (chairTitleElement != null) {
            String chairTitle = chairTitleElement.text().trim();
            if (!chairTitle.isEmpty()) {
                appendNewSection(builder);
                builder.append(chairTitle);
            }
        }

        List<String> paragraphs = extractParagraphs(scope, chairTitleElement);
        for (String paragraph : paragraphs) {
            appendNewSection(builder);
            builder.append(paragraph);
        }
        String result = builder.toString().trim();
        return result.isEmpty() ? null : result;
    }

    private List<String> extractParagraphs(Element scope, Element afterElement) {
        List<String> paragraphs = new ArrayList<>();
        if (scope == null) {
            return paragraphs;
        }
        Elements candidates = scope.select("p");
        boolean collect = afterElement == null;
        for (Element candidate : candidates) {
            if (candidate == afterElement) {
                collect = true;
                continue;
            }
            if (!collect) {
                continue;
            }
            String text = candidate.text().trim();
            if (text.isEmpty()) {
                continue;
            }
            if (containsLabel(text)) {
                continue;
            }
            if (text.length() < 40) {
                continue;
            }
            paragraphs.add(text);
            if (paragraphs.size() >= 20) {
                break;
            }
        }
        return paragraphs;
    }

    private boolean containsLabel(String text) {
        if (text == null) {
            return false;
        }
        String lowered = text.toLowerCase();
        return lowered.contains("адрес") || lowered.contains("тел") || lowered.contains("e-mail")
                || lowered.contains("email") || lowered.contains("официальный сайт");
    }

    private void appendNewSection(StringBuilder builder) {
        if (builder.length() > 0) {
            builder.append("\n\n");
        }
    }

    private String textOrNull(Element element) {
        if (element == null) {
            return null;
        }
        String text = element.text().trim();
        return text.isEmpty() ? null : text;
    }

    private String slugify(String value) {
        if (value == null) {
            return null;
        }
        return value.toLowerCase()
                .replaceAll("[^a-z0-9а-яё]+", "-")
                .replaceAll("(^-|-$)+", "");
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception ignored) {
        }
    }
}
