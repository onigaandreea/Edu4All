package project;

import java.util.Objects;

public class SocialCase extends Entity<Integer>{
    private String title;
    private String description;

    public SocialCase(String title, String description) {
        this.title = title;
        this.description = description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SocialCase that)) return false;
        return title.equals(that.title) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description);
    }

    @Override
    public String toString() {
        return "SocialCase{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
