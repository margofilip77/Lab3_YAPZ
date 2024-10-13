import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.equalTo;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class AirportAPITests {
    private static String token;
    private static final int AIRPORT_ID = 17701;
    private static final int AIRPORT_ID_TO_DELETE = 17738;//17699

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://airportgap.com/api";

        String email = "test@airportgap.com";
        String password = "airportgappassword";

        token = given()
                .contentType(ContentType.URLENC)
                .formParam("email", email)
                .formParam("password", password)
                .when()
                .post("/tokens")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract()
                .path("token");
    }

    @Test
    public void testGetAirports() {
        given()
                .when()
                .get("/airports")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("data[0].type", equalTo("airport"));
    }


    @Test
    public void testGetAirportById() {
        String airportId = "JFK";

        given()
                .when()
                .get("/airports/" + airportId)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("data.id", equalTo(airportId));
    }

    @Test
    public void testObtainApiToken() {
        String email = "test@airportgap.com";
        String password = "airportgappassword";

        token = given()
                .contentType(ContentType.URLENC)
                .formParam("email", email)
                .formParam("password", password)
                .when()
                .post("/tokens")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract()
                .path("token"); 
    }

    @Test
    public void testUpdateAirportNote() {
        String newNote = "My usual layover when visiting family, although it's really far away...";

        given()
                .contentType(ContentType.URLENC)
                .header("Authorization", "Bearer " + token)
                .formParam("note", newNote)
                .when()
                .patch("/favorites/" + AIRPORT_ID)
                .then()
                .statusCode(200)
                .body("data.attributes.note", equalTo(newNote)); // Оновлений шлях до "note"
    }
    //@Test
   // public void testDeleteFavoriteAirport() {
       // given()
               // .header("Authorization", "Bearer " + token)
               // .when()
                //.delete("/favorites/" + AIRPORT_ID_TO_DELETE)
               // .then()
               // .statusCode(204); // Перевірте, що статус-код 204 (No Content) означає успішне видалення
   // }
}
