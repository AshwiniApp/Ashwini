package org.core.asklepius;

import java.util.Objects;

public class User {
    public String imageURL;
    public String userID;
    public String regID;
    public String status;
    public String rejectionReason;
    public String username;

    User() {

    }

    public User(String imageURL, String userID, String regID, String status, String rejectionReason, String username) {
        this.imageURL = imageURL;
        this.userID = userID;
        this.regID = regID;
        this.status = status;
        this.rejectionReason = rejectionReason;
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(imageURL, user.imageURL) &&
                Objects.equals(userID, user.userID) &&
                Objects.equals(regID, user.regID) &&
                Objects.equals(status, user.status) &&
                Objects.equals(rejectionReason, user.rejectionReason) &&
                Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageURL, userID, regID, status, rejectionReason, username);
    }
}
