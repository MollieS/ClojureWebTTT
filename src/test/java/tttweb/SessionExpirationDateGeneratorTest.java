package tttweb;

import org.junit.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;

public class SessionExpirationDateGeneratorTest {

    @Test
    public void adds24hoursToGivenTimeAndDate() {
        SessionExpirationDateGenerator sessionExpirationDateGenerator = new SessionExpirationDateGenerator();
        ZoneId zoneId = ZoneId.of("Europe/London");
        ZonedDateTime startingDate = ZonedDateTime.of(1990, 06, 18, 12, 00, 00, 00, zoneId);
        String expiryDate = sessionExpirationDateGenerator.generateExpiryDate(startingDate);
        assertEquals("Tue, 19 Jun 1990 12:00:00 +0100", expiryDate);
    }
}
