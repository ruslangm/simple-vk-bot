package simplebot.data;

import java.util.Objects;

public class SimpleOrganization {
    private final String location;
    private final String name;
    private String webAddress;
    private String phoneNumber;

    public SimpleOrganization(String location, String name) {
        this.location = location;
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleOrganization that = (SimpleOrganization) o;
        return Objects.equals(location, that.location) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, name);
    }

    @Override
    public String toString() {
        return name + "\n" + location + "\n" + phoneNumber + "\n" + webAddress + "\n";
    }
}
