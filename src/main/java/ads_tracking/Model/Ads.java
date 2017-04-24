package ads_tracking.Model;

import ads_tracking.Entity.Identified;

import java.util.ArrayList;
import java.util.List;

public class Ads {

    public List<Ad> adList = new ArrayList<>();


    public static class Ad implements Identified {
        private int id;
        private String link;
        private String title;
        private String description;
        private String sId;
        private int urlId;
        private boolean send;

        public boolean isSend() {
            return send;
        }

        public void setSend(boolean send) {
            this.send = send;
        }

        public int getUrlId() {
            return urlId;
        }

        public void setUrlId(int urlId) {
            this.urlId = urlId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getsId() {
            return sId;
        }

        public void setsId(String sId) {
            this.sId = sId;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

}
