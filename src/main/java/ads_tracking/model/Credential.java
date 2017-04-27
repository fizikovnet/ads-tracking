package ads_tracking.model;

public class Credential {

    private boolean error = false;
    private int userId;
    private String username;
    private String uri;
    private String act_uri;
    private String error_msg;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getAct_uri() {
        return act_uri;
    }

    public void setAct_uri(String act_uri) {
        this.act_uri = act_uri;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}
