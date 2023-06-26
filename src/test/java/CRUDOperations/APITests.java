package CRUDOperations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import models.BookingClass;
import models.Bookingdates;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class APITests {

    static String baseurl = "https://restful-booker.herokuapp.com/";
    private Integer bookingid;
    private String token;

    @Test
    public void getBookingIds()
    {
        //String endpoint = "http://localhost/APITesting/API%20TESTING/api_testing/category/read.php";
        //String endpoint = "https://restful-booker.herokuapp.com/booking/";
        var r = RestAssured.given();
        r.baseUri(baseurl);
        r.basePath("booking");
        r.contentType(ContentType.JSON);
        var response = r.when().get().then();
        response.log().headers();
        response.statusCode(200);

        response.headers("Content-Type", equalTo("application/json; charset=utf-8"));
        response.body("records.size()", Matchers.lessThan(6000));
        response.body("bookingid", everyItem(notNullValue()));

    }

    @Test
    public void getBookingid()
    {
        //String endpoint = "http://localhost/APITesting/API%20TESTING/api_testing/product/read.php";
        String endpoint = "https://restful-booker.herokuapp.com/";
        var r = RestAssured.given();
        r.baseUri(endpoint);
        r.basePath("booking/1001");
        var response = r.when(). get().then();
        response.log().body();

        response.statusCode(200);
        response.body("firstname", Matchers.equalTo("Josh"))
                .body("lastname", Matchers.equalTo("Allen"))
                .body("totalprice",Matchers.equalTo(111))
                .body("depositpaid", Matchers.equalTo(true))
                .body("bookingdates.checkin", Matchers.equalTo("2018-01-01"))
                .body("bookingdates.checkout", Matchers.equalTo("2019-01-01"))
                .body("additionalneeds",Matchers.equalTo("super bowls"));

    }

    @Test
    public void getBookingid2()
    {
        //String endpoint = "http://localhost/APITesting/API%20TESTING/api_testing/product/read.php";
        String endpoint = "https://restful-booker.herokuapp.com/";
        var r = RestAssured.given();
        r.baseUri(endpoint);
        r.basePath("booking/1001");
        var response = r.when(). get().then();
        response.log().body();

        response.statusCode(200);
        response.body("firstname", Matchers.equalTo("John"))
                .body("totalprice", Matchers.lessThan(200));

    }


    public void createBooking()
    {
        String payload = """
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
        var r = RestAssured.given();
        r.baseUri(baseurl);
        r.basePath("booking");
        r.contentType(ContentType.JSON);
        r.body(payload);
        var response= r.when().post().then();
        response.log().body();
        bookingid = response.extract().path("bookingid");

    }


    @Test(priority = 2)
    public void createToken()
    {
        String body = "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";
        var r = RestAssured.given();
        r.baseUri(baseurl);
        r.basePath("auth");
        r.contentType(ContentType.JSON);
        r.body(body);
        var response = r.when().post().then();

        token = response.extract().path("token");



    }
    @Test
    public void updateBooking()
    {
        String payload = """
                    {
                        "firstname" : "James",
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

        var r = RestAssured.given();
        r.baseUri(baseurl);
        r.basePath("booking/"+bookingid);
        System.out.println(bookingid);
        r.cookie("token",token);
        System.out.println(token);
        r.contentType(ContentType.JSON);
        var response = r.when().put().then();
        response.log().body();

    }

    @Test
    public void deleteBooking()
    {
        var r = RestAssured.given();
        r.baseUri(baseurl);
        r.basePath("booking/"+bookingid);
        System.out.println(bookingid);
        r.cookie("token", token);
        r.contentType(ContentType.JSON);
        var response = r.when().delete().then();
        response.log().body();

    }

    @Test(priority = 1)
    public void serialisationPost() throws JsonProcessingException {
        BookingClass bs = new BookingClass(
                "Akila",
                "Herath",
                2000,
                true, new Bookingdates("2018-01-01","2019-01-01"),
                "no special needs"
        );

        ObjectMapper objectMapper = new ObjectMapper();
        String convertedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bs);
        var r = RestAssured.given();
        r.baseUri(baseurl);
        r.basePath("booking");
        r.contentType(ContentType.JSON);
        r.body(bs);
        var response= r.when().post().then();
        response.log().body();
        bookingid = response.extract().path("bookingid");
    }

    @Test(priority = 3)
    public void serialisationPut() throws JsonProcessingException {
        BookingClass bc = new BookingClass(
                "Akila",
                "Santhushta",
                2000,
                true,
                 new Bookingdates("2018-01-01","2019-01-01"),
                "big spacias room"
        );

        ObjectMapper objectMapper = new ObjectMapper();
        String convertedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bc);
        System.out.println(convertedJson);
        var r = RestAssured.given();
        r.baseUri(baseurl);
        r.basePath("booking/"+bookingid);
        System.out.println(bookingid);
        r.cookie("token",token);
        System.out.println(token);
        r.contentType(ContentType.JSON);
        var response = r.when().put().then();
        response.log().body();
    }
}
