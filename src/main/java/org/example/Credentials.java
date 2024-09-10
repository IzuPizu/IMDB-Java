package org.example;

public class Credentials {
    private String email;
    private String password;
    private Credentials(Builder builder) {
        this.email = builder.email;
        this.password = builder.password;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }

    public static class Builder {
        private String email;
        private String password;

        public Builder(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Credentials build() {
            return new Credentials(this);
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
