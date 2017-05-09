package ads_tracking.Services;

import ads_tracking.DAO.PostgreDAO.DAOFactory;
import ads_tracking.Entity.Ad;
import ads_tracking.Entity.Url;
import ads_tracking.Entity.User;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.http.client.HttpClient;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private DAOFactory daoFactory;

    @PostConstruct
    public void launch() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        try {
                            List<Url> listUrls = daoFactory.getUrlDAO().getActiveUrls();
                            for (Url url : listUrls) {
                                List<Ad> adList = daoFactory.getAdsDAO().getNotSendAdsByUrlId(url.getId());
                                for (Ad ad : adList) {
                                    Url eUrl = daoFactory.getUrlDAO().getById(ad.getUrlId());
                                    User user = daoFactory.getUserDAO().getById(eUrl.getUserId());
                                    if (!"".equals(user.getToken())) {
                                        HttpClient httpClient = HttpClientBuilder.create().build();

                                        try {
                                            String message = "{ \"notification\": {\"title\": \"" + ad.getTitle() + "\", \"body\": \"" + ad.getDescription() + "\"}, \"to\" : \"" + user.getToken() + "\"}";
                                            HttpPost request = new HttpPost("https://fcm.googleapis.com/fcm/send");
                                            StringEntity params = new StringEntity(message);
                                            request.addHeader("content-type", "application/json");
                                            request.addHeader("Authorization", "key=AAAAy9NzyJE:APA91bGsVD0BS6mGrTBzBv02scM92ZSgXLQ_TXeYXsMHzECDrriwlrHF7dacV9l4F9rUStkLk39CWx0VJs_0QnvS5Hv-K5ErvWVmWd8Ouoz5mP8k10GnpkhIGB3SPRENApSOjccEsVzN");
                                            request.setEntity(params);
                                            HttpResponse response = httpClient.execute(request);
                                            if (response.getStatusLine().getStatusCode() == 200) {
                                                daoFactory.getAdsDAO().setSendFlagForAds(Collections.singletonList(ad));
                                            }
                                            Thread.sleep(10000);
                                        } catch (Exception ex) {

                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                        }
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }
}
