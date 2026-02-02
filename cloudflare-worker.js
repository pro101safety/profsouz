// Cloudflare Worker backend (currently disabled in app; kept for rollback).
const FPB_STRUCTURE_URL = "https://fpb.1prof.by/organizacionnaya-struktura-fpb/";
const FPB_MEMBERS_URL = "https://fpb.1prof.by/members/";
const FPB_TOURISM_URL = "https://fpb.1prof.by/tourism-places/gostinicy/";
const FPB_NEWS_URL = "https://1prof.by/";

const CACHE_TTL_MS = 30 * 60 * 1000;

export default {
  async fetch(request, env) {
    const url = new URL(request.url);
    if (url.pathname === "/debug") {
      return jsonResponse(await buildDebugReport());
    }
    if (url.pathname === "/news") {
      const unionId = url.searchParams.get("union") || "all";
      return jsonResponse(await getCachedOrFetch(env, `news:${unionId}`, () => fetchNews(unionId)));
    }
    if (url.pathname === "/fpb") {
      return jsonResponse(await getCachedOrFetch(env, "fpb", fetchFpbSections));
    }
    if (url.pathname === "/unions") {
      return jsonResponse(await getCachedOrFetch(env, "unions", fetchUnions));
    }
    if (url.pathname === "/tourism") {
      return jsonResponse(await getCachedOrFetch(env, "tourism", fetchTourism));
    }
    return new Response("Not found", { status: 404 });
  },

  async scheduled(event, env) {
    event.waitUntil(refreshAll(env));
  }
};

async function refreshAll(env) {
  await Promise.all([
    getCachedOrFetch(env, "fpb", fetchFpbSections, true),
    getCachedOrFetch(env, "unions", fetchUnions, true),
    getCachedOrFetch(env, "tourism", fetchTourism, true),
    getCachedOrFetch(env, "news:all", () => fetchNews("all"), true)
  ]);
}

async function getCachedOrFetch(env, key, fetcher, force = false) {
  const cached = await env.FPB_CACHE.get(key, "json");
  if (!force && cached && cached.timestamp && Date.now() - cached.timestamp < CACHE_TTL_MS) {
    if (!isEmptyData(cached.data)) {
      return cached.data;
    }
  }
  const data = await fetcher();
  if (data != null) {
    await env.FPB_CACHE.put(key, JSON.stringify({ timestamp: Date.now(), data }));
    return data;
  }
  return cached?.data || [];
}

async function fetchFpbSections() {
  const html = await fetchText(FPB_STRUCTURE_URL);
  if (!html) {
    return null;
  }
  const sections = extractListItems(html).map((item, index) => ({
    id: slugify(item.text) || `fpb-${index}`,
    title: item.text,
    content: item.details,
    imageUrl: null,
    contact: null
  }));
  if (sections.length > 0) {
    return sections;
  }
  return extractAnchors(html, FPB_STRUCTURE_URL)
    .filter(item => item.text.length > 6)
    .map((item, index) => ({
      id: slugify(item.text) || `fpb-${index}`,
      title: item.text,
      content: null,
      imageUrl: null,
      contact: null
    }));
}

async function fetchUnions() {
  const html = await fetchText(FPB_MEMBERS_URL);
  if (!html) {
    return null;
  }
  const listItems = extractListItems(html).map((item, index) => ({
    id: slugify(item.text) || `union-${index}`,
    name: item.text,
    siteUrl: item.links.find(link => link.startsWith("http")) || null,
    email: item.links.find(link => link.startsWith("mailto:"))?.replace("mailto:", "") || null,
    phone: item.links.find(link => link.startsWith("tel:"))?.replace("tel:", "") || null,
    telegramUrl: item.links.find(link => link.includes("t.me")) || null,
    description: item.details || null
  }));
  if (listItems.length > 0) {
    return listItems;
  }
  return extractAnchors(html, FPB_MEMBERS_URL)
    .filter(item => item.text.length > 6)
    .map((item, index) => ({
      id: slugify(item.text) || `union-${index}`,
      name: item.text,
      siteUrl: item.href,
      email: null,
      phone: null,
      telegramUrl: item.href.includes("t.me") ? item.href : null,
      description: null
    }));
}

async function fetchTourism() {
  const html = await fetchText(FPB_TOURISM_URL);
  if (!html) {
    return null;
  }
  return extractListItems(html).map((item, index) => ({
    id: slugify(item.text) || `tourism-${index}`,
    name: item.text,
    region: null,
    address: item.details,
    phone: item.links.find(link => link.startsWith("tel:"))?.replace("tel:", "") || null,
    email: item.links.find(link => link.startsWith("mailto:"))?.replace("mailto:", "") || null,
    siteUrl: item.links.find(link => link.startsWith("http")) || null
  }));
}

async function fetchNews(unionId) {
  const baseHtml = await fetchText(FPB_NEWS_URL);
  if (!baseHtml) {
    return null;
  }
  const items = extractNewsFromHtml(baseHtml, "1prof.by", FPB_NEWS_URL);
  if (unionId && unionId !== "all") {
    const unions = await fetchUnions();
    const target = unions.find(union =>
      (union.id && union.id === unionId) ||
      (union.name && union.name.toLowerCase().includes(unionId.toLowerCase()))
    );
    if (target?.siteUrl) {
      const unionHtml = await fetchText(target.siteUrl);
      items.push(...extractNewsFromHtml(unionHtml, target.name, target.siteUrl));
    }
    if (target?.telegramUrl) {
      const telegramFeed = toTelegramFeedUrl(target.telegramUrl);
      if (telegramFeed) {
        const telegramHtml = await fetchText(telegramFeed);
        items.push(...extractTelegramNews(telegramHtml, target.name, target.telegramUrl));
      }
    }
  }
  return items.slice(0, 60);
}

async function fetchText(url) {
  const response = await fetch(url, {
    headers: {
      "User-Agent": "FPB-Aggregator/1.0",
      "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
      "Accept-Language": "ru,en;q=0.8"
    }
  });
  if (!response.ok) {
    return null;
  }
  return response.text();
}

async function fetchTextWithMeta(url) {
  try {
    const response = await fetch(url, {
      headers: {
        "User-Agent": "FPB-Aggregator/1.0",
        "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
        "Accept-Language": "ru,en;q=0.8"
      }
    });
    const contentType = response.headers.get("content-type");
    const text = response.ok ? await response.text() : null;
    return {
      url,
      ok: response.ok,
      status: response.status,
      contentType,
      length: text ? text.length : 0
    };
  } catch (error) {
    return { url, ok: false, status: 0, contentType: null, length: 0, error: String(error) };
  }
}

async function buildDebugReport() {
  const targets = [
    FPB_STRUCTURE_URL,
    FPB_MEMBERS_URL,
    FPB_TOURISM_URL,
    FPB_NEWS_URL
  ];
  const reports = [];
  for (const target of targets) {
    reports.push(await fetchTextWithMeta(target));
  }
  return {
    timestamp: Date.now(),
    reports
  };
}

function extractListItems(html) {
  const results = [];
  const liRegex = /<li[^>]*>([\s\S]*?)<\/li>/gis;
  let match;
  while ((match = liRegex.exec(html)) !== null) {
    const content = match[1];
    const text = stripTags(content).replace(/\s+/g, " ").trim();
    if (!text) {
      continue;
    }
    const links = extractLinks(content);
    results.push({
      text,
      details: null,
      links
    });
  }
  return results;
}

function extractNewsFromHtml(html, sourceName, baseUrl) {
  const items = [];
  const linkRegex = /<a[^>]+href="([^"]+)"[^>]*>(.*?)<\/a>/gis;
  let match;
  while ((match = linkRegex.exec(html)) !== null && items.length < 60) {
    const href = normalizeUrl(match[1], baseUrl);
    const text = stripTags(match[2]).trim();
    if (!text || !href) {
      continue;
    }
    if (!href.includes("/news/") && !href.includes("/profmedia/")) {
      continue;
    }
    items.push({
      id: href,
      title: text,
      url: href,
      source: sourceName,
      publishedAt: Date.now(),
      imageUrl: null,
      summary: null
    });
  }
  return items;
}

function toTelegramFeedUrl(telegramUrl) {
  if (!telegramUrl) {
    return null;
  }
  const normalized = telegramUrl.replace("https://t.me/", "");
  if (normalized.startsWith("s/")) {
    return `https://t.me/${normalized}`;
  }
  return `https://t.me/s/${normalized}`;
}

function extractTelegramNews(html, sourceName, baseUrl) {
  const items = [];
  const messageRegex = /<div class="tgme_widget_message_text[^"]*">([\s\S]*?)<\/div>/gis;
  let match;
  let index = 0;
  while ((match = messageRegex.exec(html)) !== null && items.length < 30) {
    const text = stripTags(match[1]).replace(/\s+/g, " ").trim();
    if (!text) {
      continue;
    }
    items.push({
      id: `${baseUrl}#${index++}`,
      title: text.slice(0, 120),
      url: baseUrl,
      source: sourceName,
      publishedAt: Date.now(),
      imageUrl: null,
      summary: text
    });
  }
  return items;
}

function extractLinks(html) {
  const links = [];
  const linkRegex = /href="([^"]+)"/gi;
  let match;
  while ((match = linkRegex.exec(html)) !== null) {
    const link = match[1];
    if (!links.includes(link)) {
      links.push(link);
    }
  }
  return links.map(link => normalizeUrl(link, FPB_MEMBERS_URL));
}

function extractAnchors(html, baseUrl) {
  const items = [];
  const linkRegex = /<a[^>]+href="([^"]+)"[^>]*>([\s\S]*?)<\/a>/gis;
  let match;
  while ((match = linkRegex.exec(html)) !== null) {
    const href = normalizeUrl(match[1], baseUrl);
    const text = stripTags(match[2]).replace(/\s+/g, " ").trim();
    if (!href || !text) {
      continue;
    }
    items.push({ text, href });
  }
  return items;
}

function normalizeUrl(href, baseUrl) {
  if (!href) {
    return null;
  }
  if (href.startsWith("http")) {
    return href;
  }
  if (href.startsWith("mailto:") || href.startsWith("tel:")) {
    return href;
  }
  try {
    return new URL(href, baseUrl).toString();
  } catch {
    return null;
  }
}

function stripTags(value) {
  return value.replace(/<[^>]*>/g, "");
}

function slugify(value) {
  if (!value) {
    return null;
  }
  return value
    .toLowerCase()
    .replace(/[^a-z0-9а-яё]+/gi, "-")
    .replace(/(^-|-$)+/g, "");
}

function jsonResponse(data) {
  return new Response(JSON.stringify(data), {
    headers: {
      "Content-Type": "application/json; charset=utf-8",
      "Cache-Control": "no-store"
    }
  });
}

function isEmptyData(data) {
  if (data == null) {
    return true;
  }
  if (Array.isArray(data)) {
    return data.length === 0;
  }
  return false;
}
