package tttweb;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SessionTokenGeneratorTest {

    @Test
    public void generatesASessionToken() {
        int sessionToken = new SessionTokenGenerator().generateToken();

        assertNotNull(sessionToken);
    }
}
