package service;
import model.UserData;
import org.junit.jupiter.api.Test;


public class ClearServiceTests {
    @Test
    public void testClear_Positive() {
        UserData data = new UserData("eli", "ile", "eli@ile.com");
        ClearService service = new ClearService();
//        ClearResult rtrn = service.clearGame();
//        assert(rtrn.message().isEmpty());
    }

    // don't need a negative testClear test case
}