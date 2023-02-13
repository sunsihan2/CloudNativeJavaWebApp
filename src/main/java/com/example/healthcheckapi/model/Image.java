package com.example.healthcheckapi.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private String id;

    @NotEmpty
    @NotNull(message="File name cannot be missing or empty")
    @Column(name = "file_name")
    private String file_name;


    @NotEmpty @NotNull(message="URL cannot be missing or empty")
    @Column(name = "url")
    private String url;

    private String upload_date;

    @Column(name = "user_id", nullable = false, updatable = false)
    private String userId;

    public String getId() {
        return id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(String upload_date) {
        this.upload_date = upload_date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String user_id) {
        this.userId = user_id;
    }

    public Image() {

    }

    public Image(String file_name, String user_id, String url) {
        this.id = UUID.randomUUID().toString();
        this.file_name = file_name;
        this.userId = user_id;
        this.url = url;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        this.upload_date =formatter.format(new Date());
    }


    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Image))
            return false;
        Image employee = (Image) o;
        return Objects.equals(this.id, employee.id)
                && Objects.equals(this.file_name, employee.file_name)
                && Objects.equals(this.userId, employee.userId)
                && Objects.equals(this.url, employee.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.file_name, this.userId, this.url);
    }
}
