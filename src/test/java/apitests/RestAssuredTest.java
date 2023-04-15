package apitests;

import constans.Urls;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;
import java.io.File;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class RestAssuredTest {
    @Test
    public void checkUserNotFoundTest() {
        RestAssured
                .given()
                .when()
                .get(Urls.REGRES_URL.concat("users/23"))
                .then()
                .statusCode(404);
    }

    @Test
    public void checkBodyValuesTest() {
        RestAssured
                .given()
                .when()
                .get(Urls.REGRES_URL.concat("unknown"))
                .then()
                .statusCode(200)
                .body("page", instanceOf(Integer.class))
                .body("per_page", equalTo(6));
    }

    @Test
    public void checkStaticResponseTest() {
        JsonPath expectedJson = new JsonPath(new File("src/test/resources/user.json"));
        RestAssured
                .given()
                .when()
                .get(Urls.REGRES_URL.concat("unknown/2"))
                .then()
                .statusCode(200)
                .body("", equalTo(expectedJson.getMap("")));
    }

    @Test
    public void getWithQueryParamTest() {
        RestAssured
                .given()
                .queryParam("page", "2")
                .when()
                .get(Urls.REGRES_URL.concat("users"))
                .then()
                .statusCode(200)
                .body("page", equalTo(2));
    }

    @Test
    public void updateUserTest() {
        UpdateUserModel updateBody = UpdateUserModel
                .builder()
                .name("morpheus")
                .job("zion resident")
                .build();
        ValidatableResponse body = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .and()
                .body(updateBody)
                .when()
                .put(Urls.REGRES_URL.concat("users/2"))
                .then()
                .statusCode(200);
    }

    @Test
    public void createUserTest() {
        UpdateUserModel UserBody = UpdateUserModel
                .builder()
                .name("morpheus")
                .job("leader")
                .build();
        RestAssured.given().
                contentType(ContentType.JSON)
                .and()
                .body(UserBody)
                .post(Urls.REGRES_URL.concat("users"))
                .then()
                .statusCode(201)
                .body("name", equalTo(UserBody.getName()));
    }

    @Test
    public void updateUserPatchTest() {
        UpdateUserModel UserBody = UpdateUserModel
                .builder()
                .name("morpheus")
                .job("zion resident")
                .build();
        RestAssured.given()
                .contentType(ContentType.JSON)
                .and()
                .body(UserBody)
                .when()
                .patch(Urls.REGRES_URL.concat("users/2"))
                .then()
                .statusCode(200)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("zion resident"));
    }

    @Test
    public void deleteUserTest() {
        RestAssured
                .given()
                .when()
                .delete(Urls.REGRES_URL.concat("users/2"))
                .then()
                .statusCode(204);
    }

    @Test
    public void registerUserSuccessTest() {
        RegisterModel registerUser = RegisterModel
                .builder()
                .email("eve.holt@reqres.in")
                .password("pistol")
                .build();
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .and()
                .body(registerUser)
                .when()
                .post(Urls.REGRES_URL.concat("register"))
                .then()
                .statusCode(200)
                .body("token", equalTo("QpwL5tke4Pnpja7X4"))
                .body("id", equalTo(4));
    }

    @Test
    public void registerUserUnSuccessTest() {
        RegisterModel registerUser = RegisterModel
                .builder()
                .email("sydney@fife")
                .password("")
                .build();
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .and()
                .body(registerUser)
                .when()
                .post(Urls.REGRES_URL.concat("register"))
                .then()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @Test
    public void loginSuccessTest() {
        RegisterModel registerUser = RegisterModel
                .builder()
                .email("eve.holt@reqres.in")
                .password("cityslicka")
                .build();
        RestAssured.given()
                .contentType(ContentType.JSON)
                .and().body(registerUser)
                .when().
                post(Urls.REGRES_URL.concat("login"))
                .then()
                .statusCode(200)
                .body("token", equalTo("QpwL5tke4Pnpja7X4"));
    }

    @Test
    public void delayResponseTest() {
        JsonPath expectedJson = new JsonPath(new File("src/test/resources/user.json"));
        RestAssured.given()
                .when()
                .get(Urls.REGRES_URL.concat("users?delay=3"))
                .then()
                .statusCode(200)
                .body("per_page", equalTo(6))
                .body("data.id", notNullValue())
                .body("", equalTo(expectedJson.getMap("")));
    }
}