package CRUDOperations;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class Putoperation {
    static String baseurl = "https://restful-booker.herokuapp.com/";

    @Test
    public  void updateABooking() {
        String payload1 = """
                {
                    "username" : "admin",
                    "password" : "password123"
                }
                """;
        var r = RestAssured.given();
        r.contentType(ContentType.JSON);
        r.baseUri(baseurl);
        r.basePath("auth");
        r.body(payload1);
        var response = r.when().post().then();
        response.log().body();

        String token = response.extract().path("token");

        //Create a new booking
        String payload2 = """
                {
                    "firstname" : "Jim",
                    "lastname" : "Brown",
                    "totalprice" : 111,
                    "depositpaid" : true,
                    "bookingdates" : {
                        "checkin" : "2018-01-01",
                        "checkout" : "2019-01-01"
                    },
                    "additionalneeds" : "Breakfast"
                }
                """;
        r.basePath("booking");
        r.body(payload2);
        var response2 = r.when().post().then();
        response2.log().body();
        Integer bookingid = response2.extract().path("bookingid");

        //Update created booking from previous step
        String payload3 = """
                {
                    "firstname" : "James",
                    "lastname" : "Brown",
                    "totalprice" : 211,
                    "depositpaid" : true,
                    "bookingdates" : {
                        "checkin" : "2018-01-01",
                        "checkout" : "2019-01-01"
                    },
                    "additionalneeds" : "Breakfast"
                }
                """;

        r.cookie("token",token);
        r.basePath("booking/"+bookingid);
        r.body(payload3);
        //System.out.println(r.basePath("booking/"+bookingid));
        var response3 = r.put().then();
        response3.log().body();
        response3.body("firstname", Matchers.equalTo("James"));

        //Delete the created and updated booking
        var response4 = r.when().delete().then();
        response4.log().all();


    }
}
