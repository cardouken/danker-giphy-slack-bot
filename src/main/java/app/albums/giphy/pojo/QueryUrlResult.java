package app.albums.giphy.pojo;

public class QueryUrlResult {

    private String query;
    private String url;

    public QueryUrlResult(String query, String url) {
        this.query = query;
        this.url = url;
    }

    public String getQuery() {
        return query;
    }

    public QueryUrlResult setQuery(String query) {
        this.query = query;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public QueryUrlResult setUrl(String url) {
        this.url = url;
        return this;
    }
}
