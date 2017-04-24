package ads_tracking.Model;

import ads_tracking.Entity.Url;

import java.util.ArrayList;
import java.util.List;

public class Urls {

    private List<Url> urlList = new ArrayList<>();

    public List<Url> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<Url> urlList) {
        this.urlList = urlList;
    }
}
