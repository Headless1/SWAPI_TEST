package Test;

import data.starships;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import spec.Specifications;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class api_test {

    private final static String URL = "https://swapi.dev/api/";

    public List<starships> getStarships(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecUnique(200));
        return given()
                .when()
                .get("starships/?page=2")
                .then().log().body()
                .extract().jsonPath().getList("results", starships.class);
    }

    @Test
    public void checkStarship(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecUnique(200));
        Response response = given()
                .when()
                .get("starships/9/")
                .then().log().all()
                .body("name", notNullValue())
                .body("model", notNullValue())
                .body("manufacturer", notNullValue())
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        String StarShipName = "Death Star";
        String name = jsonPath.get("name");
        Assert.assertEquals(name, StarShipName);
    }

    @Test
    public void sortHighToLow(){
        List<starships> highToLow = getStarships().stream().sorted(new Comparator<starships>() {
                    @Override
                    public int compare(starships o1, starships o2) {
                        return Integer.compare(Integer.parseInt(o2.getCrew()), Integer.parseInt(o1.getCrew()));
                    }
                }).collect(Collectors.toList());
        List<starships> top5 = highToLow.stream().limit(5).collect(Collectors.toList());
        Assert.assertEquals(top5.get(0).getCrew(), "5400");
    }

    @Test
    public void sortLowToHigh(){
        List<starships> highToLow = getStarships().stream().sorted(new Comparator<starships>() {
            @Override
            public int compare(starships o1, starships o2) {
                return Integer.compare(Integer.parseInt(o1.getCrew()), Integer.parseInt(o2.getCrew()));
            }
        }).collect(Collectors.toList());
        List<starships> top5 = highToLow.stream().limit(5).collect(Collectors.toList());
        Assert.assertEquals(top5.get(0).getCrew(), "1");
    }

    @Test
    public void checkURL(){
        List<starships> urlStarships = getStarships().stream().filter(x->x.getUrl().contains("starships/")).collect(Collectors.toList());
        Assert.assertTrue(urlStarships.stream().allMatch(x->x.getUrl().contains("starships/")));
    }
}
