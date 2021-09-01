package app.albums.giphy.pojo;

public class UserQueryResults {

    private String query;
    private String url;

    public UserQueryResults(String query, String url) {
        this.query = query;
        this.url = url;
    }

    public String getQuery() {
        return query;
    }

    public UserQueryResults setQuery(String query) {
        this.query = query;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public UserQueryResults setUrl(String url) {
        this.url = url;
        return this;
    }
}
