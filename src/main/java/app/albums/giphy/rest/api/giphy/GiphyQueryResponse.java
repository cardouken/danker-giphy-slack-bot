package app.albums.giphy.rest.api.giphy;

import app.albums.giphy.rest.api.giphy.pojo.GiphyResults;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GiphyQueryResponse {

    private List<GiphyResults.Data> data;

    public List<GiphyResults.Data> getData() {
        return data;
    }

    public GiphyQueryResponse setData(List<GiphyResults.Data> data) {
        this.data = data;
        return this;
    }
}
