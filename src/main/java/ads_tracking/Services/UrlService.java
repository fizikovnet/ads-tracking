package ads_tracking.Services;

import ads_tracking.DAO.PostgreDAO.DAOFactory;
import ads_tracking.Entity.Ad;
import ads_tracking.Entity.Url;
import ads_tracking.Exception.DAOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class UrlService {

    public static AtomicBoolean STATUS = new AtomicBoolean(true);

    @Autowired
    private DAOFactory daoFactory;

    @PostConstruct
    private void launch() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (STATUS.get()) {
                            List<Url> urls = daoFactory.getUrlDAO().getActiveUrls();
                            for (Url url : urls) {
                                getAds(url);
                            }
                        }
                        Thread.sleep(10000);
                    } catch (DAOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public boolean validateUrl(Url url) {
        List<Ad> list = getUrlContent(url.getUrl());
        if (list.size() != 0) {
            return true;
        }
        return false;
    }

    public List<Url> handleUrls() throws DAOException {
        List<Url> uriList = daoFactory.getUrlDAO().getAll();
        return uriList;
    }

    public List<Ad> getAds(Url url) throws DAOException {
        List<Ad> ads = getUrlContent(url.getUrl());
        System.out.println("fill Db " + url.getUrl());
        fillDB(ads, url.getId());

        return ads;
    }

    private void fillDB(List<Ad> ads, int urlId) throws DAOException {
        List<Ad> existAds = daoFactory.getAdsDAO().getAdsByUrlId(urlId);
        outer:for (Ad newAd : ads) {
            for (Ad existAd : existAds) {
                if (existAd.getsId().trim().equals(newAd.getsId())) {
                    continue outer;
                }
            }
            System.out.println("-------------------Adding Ad ------------------");
            newAd.setUrlId(urlId);
            daoFactory.getAdsDAO().create(newAd);
        }
    }

    private List<Ad> getUrlContent(String uri) {
        Document doc = null;
        List<Ad> ads = new ArrayList<>();
        try {
            doc = Jsoup.connect(uri).get();
            Elements items = doc.getElementsByClass("item");
            for (Element item : items) {
                Ad ad = new Ad();
                ad.setsId(item.attr("id").trim());
                Elements img = item.getElementsByClass("b-photo");
                Elements description = item.getElementsByClass("item-description-title-link");
                Element title = description.get(0).getElementsByClass("item-description-title-link").get(0);
                try {
                    ad.setImg(img.get(0).getElementsByTag("img").get(0).absUrl("src"));
                } catch (Exception e) {
                    ad.setImg("");
                }
                ad.setTitle(title.text());
                ad.setLink(title.attr("href"));

                Elements about = item.getElementsByClass("about");
                ad.setDescription(about.text());
                ads.add(ad);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ads;
    }

}
