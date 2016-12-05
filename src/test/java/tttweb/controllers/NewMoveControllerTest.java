package tttweb.controllers;

import org.junit.Test;

import static httpserver.routing.Method.POST;

public class NewMoveControllerTest {

    @Test
    public void updatesBoardWhenDataIsPresent() {
        RequestDouble requestDouble = new RequestDouble("/new-move", POST);
        requestDouble.addCookie("1");
        requestDouble.addData("0");

        UpdateBoardController newMoveController = new UpdateBoardController(new SessionManagerStub());

    }
}
