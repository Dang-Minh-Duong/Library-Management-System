package API;

public class GoogleBooksResponse {

    private Item[] items;

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }

    public static class Item {

        private VolumeInfo volumeInfo;

        public VolumeInfo getVolumeInfo() {
            return volumeInfo;
        }

        public void setVolumeInfo(VolumeInfo volumeInfo) {
            this.volumeInfo = volumeInfo;
        }
    }

    public static class VolumeInfo {

        private String title;
        private String[] authors;
        private String description;
        private ImageLinks imageLinks;
        private IndustryIdentifiers[] industryIdentifiers;

        // Getters và Setters
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String[] getAuthors() {
            return authors;
        }

        public void setAuthors(String[] authors) {
            this.authors = authors;
        }

        public String getDescription() { // Thêm phương thức getter 
            return description;
        }

        public void setDescription(String description) { // Thêm phương thức setter 
            this.description = description;
        }

        public ImageLinks getImageLinks() {
            return imageLinks;
        }

        public void setImageLinks(ImageLinks imageLinks) {
            this.imageLinks = imageLinks;
        }

        public IndustryIdentifiers[] getIndustryIdentifiers() { // Thêm phương thức getter
            return industryIdentifiers;
        }

        public void setIndustryIdentifiers(IndustryIdentifiers[] industryIdentifiers) { // Thêm phương thức setter
            this.industryIdentifiers = industryIdentifiers;
        }

        public static class ImageLinks {

            private String thumbnail;

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }
        }

        public static class IndustryIdentifiers {

            private String type;
            private String identifier;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getIdentifier() {
                return identifier;
            }

            public void setIdentifier(String identifier) {
                this.identifier = identifier;
            }
        }
    }

}
