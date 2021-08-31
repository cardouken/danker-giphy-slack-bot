package app.albums.giphy.rest.api.giphy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GiphyGifResponse {

    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public GiphyGifResponse setData(List<Data> data) {
        this.data = data;
        return this;
    }

    public static class Data {

        private Images images;

        public Images getImages() {
            return images;
        }

        public Data setImages(Images images) {
            this.images = images;
            return this;
        }
    }

    public static class Images {

        private OriginalImage original;
        private DownsizedImage downsized;

        public OriginalImage getOriginal() {
            return original;
        }

        public Images setOriginal(OriginalImage original) {
            this.original = original;
            return this;
        }

        public DownsizedImage getDownsized() {
            return downsized;
        }

        public Images setDownsized(DownsizedImage downsized) {
            this.downsized = downsized;
            return this;
        }
    }

    public static class OriginalImage {

        private String url;

        public String getUrl() {
            return url;
        }

        public OriginalImage setUrl(String url) {
            this.url = url;
            return this;
        }
    }

    public static class DownsizedImage {

        private String url;

        public String getUrl() {
            return url;
        }

        public DownsizedImage setUrl(String url) {
            this.url = url;
            return this;
        }
    }
}
