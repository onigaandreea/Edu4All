package project;

import java.util.Objects;

public class SocialCase extends Entity<Integer>{
    private String name;
    private String description;

    public SocialCase(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return name.equals(that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return "SocialCase{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
