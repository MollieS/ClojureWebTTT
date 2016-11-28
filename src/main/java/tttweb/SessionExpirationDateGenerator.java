package tttweb;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class SessionExpirationDateGenerator {

    public String generateExpiryDate(ZonedDateTime date) {
        ZonedDateTime expiryDate = date.plusDays(1);
        return (expiryDate.format(DateTimeFormatter.RFC_1123_DATE_TIME));
    }
}
