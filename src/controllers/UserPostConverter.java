package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class UserPostConverter {
    public static void main(String[] args) {
        UserPost post = new UserPost();
        post.setPhotoUrl("https://new-social-network.site/images/928476864.jpg");
        post.setUserId(97_748);
        post.setDescription("Классное фото!");
        post.setLikesQuantity(753);
        LocalDate publicationDate = LocalDate.of(2020, 12, 25);
        post.setPublicationDate(publicationDate);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        // сериализуем объект в JSON
        String postSerialized = gson.toJson(post);
        // Дата теперь отображается как 25--12--2020
        System.out.println("Serialized post:\n" + postSerialized);

        // заменим дату в JSON на другой формат 
        String jsonWithAnotherDateFormat = postSerialized.replace("25--12--2020", "25.12.2020");

        System.out.println("New json:\n" + jsonWithAnotherDateFormat);

        // сконвертируем дату в формате 25.12.2020 в объект LocalDate
        UserPost postDeserialized = gson.fromJson(jsonWithAnotherDateFormat, UserPost.class);
        System.out.println("Deserialized post:\n" + postDeserialized);
    }
}

// правила конвертации, описанные в TypeAdapter для класса LocalDate
class LocalDateAdapter extends TypeAdapter<LocalDate> {
    private static final DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("dd--MM--yyyy");
    private static final DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDate localDate) throws IOException {
        jsonWriter.value(localDate.format(formatterWriter));
    }

    @Override
    public LocalDate read(final JsonReader jsonReader) throws IOException {
        return LocalDate.parse(jsonReader.nextString(), formatterReader);
    }
}


class UserPost {
    // URL-адрес, по которому можно скачать фото
    private String photoUrl;

    // дата публикации
    private LocalDate publicationDate;

    // уникальный идентификатор автора поста
    private int userId;

    // текстовой комментарий к фото
    private String description;

    // сколько пользователей поставило лайк этому посту
    private int likesQuantity;


    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLikesQuantity() {
        return likesQuantity;
    }

    public void setLikesQuantity(int likesQuantity) {
        this.likesQuantity = likesQuantity;
    }


    @Override
    public String toString() {
        return "UserPost{" +
                "photoUrl='" + photoUrl + '\'' +
                ", publicationDate=" + publicationDate +
                ", userId=" + userId +
                ", description='" + description + '\'' +
                ", likesQuantity=" + likesQuantity +
                '}';
    }
}