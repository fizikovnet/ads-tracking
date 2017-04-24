package ads_tracking.Services;

import ads_tracking.DAO.OracleDAO.DAOFactory;
import ads_tracking.Entity.Url;
import ads_tracking.Exception.DAOException;
import ads_tracking.Model.Ads;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class UrlService {

    @Autowired
    private DAOFactory daoFactory;

    public UrlService() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        List<Url> urls = daoFactory.getUrlDAO().getAll();
                        for (Url url : urls) {
                            if (url.isActive()) {
                                getAds(url.getId());
                                Thread.sleep(60000);
                            }
                        }
                    } catch (DAOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
//        thread.start();
    }

    public List<Url> handleUrls() throws DAOException {
        List<Url> uriList = daoFactory.getUrlDAO().getAll();
        return uriList;
    }

    public Ads getAds(int urlId) throws DAOException {
        Url url = daoFactory.getUrlDAO().getById(urlId);
        Ads ads = getUrlContent(url.getUrl());
        fillDB(ads, urlId);

        return ads;
    }

    private void fillDB(Ads ads, int urlId) throws DAOException {
        List<Ads.Ad> existAds = daoFactory.getAdsDAO().getAdsByUrlId(urlId);
        outer:for (Ads.Ad newAd : ads.adList) {
            for (Ads.Ad existAd : existAds) {
                if (existAd.getsId().trim().equals(newAd.getsId())) {
                    continue outer;
                }
            }
            newAd.setUrlId(urlId);
            daoFactory.getAdsDAO().create(newAd);
        }
    }

    private Ads getUrlContent(String uri) {
        Document doc = null;
        Ads ads = new Ads();
        try {
            doc = Jsoup.connect(uri).get();
            Elements items = doc.getElementsByClass("item");
            for (Element item : items) {
                Ads.Ad ad = new Ads.Ad();
                ad.setsId(item.attr("id").trim());
                Elements description = item.getElementsByClass("item-description-title-link");
                Element title = description.get(0).getElementsByClass("item-description-title-link").get(0);
                ad.setTitle(title.text());
                ad.setLink(title.attr("href"));

                Elements about = item.getElementsByClass("about");
                ad.setDescription(about.text());
                ads.adList.add(ad);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ads;
    }

}
