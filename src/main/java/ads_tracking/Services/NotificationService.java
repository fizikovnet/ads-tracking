package ads_tracking.Services;

import ads_tracking.DAO.OracleDAO.DAOFactory;
import ads_tracking.Entity.Ad;
import ads_tracking.Entity.Url;
import ads_tracking.Entity.User;
import ads_tracking.Exception.DAOException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.http.client.HttpClient;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private DAOFactory daoFactory;

    public NotificationService() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    notificate();
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public void notificate() {

        try {
            //ToDo вытаскивать из базы только активные урлы
            List<Url> listUrls = daoFactory.getUrlDAO().getAll();
            for (Url url : listUrls) {
                if (url.isActive()) {
                    //ToDo вытаскивать из базы только не отправленные объявления
                    List<Ad> adList = daoFactory.getAdsDAO().getAdsByUrlId(url.getId());
                    for (Ad ad : adList) {
                        if (!ad.isSend()) {
                            sendNotify(ad);
                        }
                    }
                }
            }
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    private void sendNotify(Ad ad) throws DAOException {

        Url url = daoFactory.getUrlDAO().getById(ad.getUrlId());
        User user = daoFactory.getUserDAO().getById(url.getUserId());

        HttpClient httpClient = HttpClientBuilder.create().build();

        try {
            String message = "{ \"notification\": {" +
                    "    \"title\": \""+ ad.getTitle() +"\"," +
                    "    \"body\": \""+ad.getDescription()+"\"" +
                    "  }," +
                    "  \"to\" : \""+user.getToken()+"" +
                    "}";
            HttpPost request = new HttpPost("https://fcm.googleapis.com/fcm/send");
            StringEntity params =new StringEntity(message);
            request.addHeader("content-type", "application/json");
            request.addHeader("Authorization", "key=AAAA3VEqeqM:APA91bH7IfFaTMg-Mj0jr2lnFuae18_jgGROMxDq26qsOEh07hDh29KSBcR3NKDXAqHH-rUDRb1RMp5vDNylERRQ23Seu4cRq85uSK_nzxpbQzFHUJ8UNbLzIaYDVxDhnVXngeR5ifDA");
            request.setEntity(params);
            httpClient.execute(request);


        }catch (Exception ex) {


        }
    }
}
