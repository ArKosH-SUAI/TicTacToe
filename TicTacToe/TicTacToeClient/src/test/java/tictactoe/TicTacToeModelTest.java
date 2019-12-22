package tictactoe;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import tictactoe.game.TicTacToeModel;

public class TicTacToeModelTest
{
	private TicTacToeModel model = new TicTacToeModel();

    @Test
    public void getUserXCoorsTest() {
        assertEquals(0, model.getUserXCoors()); 
    }

    @Test
    public void getUserYCoorsTest() {
        assertEquals(0, model.getUserYCoors()); 
    }

    @Test
    public void setUserCoorsTest() {
        model.setUserCoors(1, 1);
        assertEquals(1, model.getUserYCoors());
        assertEquals(1, model.getUserXCoors()); 
    }

    @Test
    public void getActiveUserTest() {
        assertEquals(1, model.getActiveUser()); 
    }

    @Test
    public void setActiveUserTest() {
        model.setActiveUser(2);
        assertEquals(2, model.getActiveUser()); 
    }

    @Test
    public void getUserSymbolTest() {
        model.setActiveUser(1);
        assertEquals('X', model.getUserSymbol());
        model.setActiveUser(2);
        assertEquals('O', model.getUserSymbol()); 
    }

    @Test
    public void checkEmptyTest() {
        assertEquals(1, model.checkEmpty(5, 5));
        assertEquals(-1, model.checkEmpty(11, 12)); 
    }

    @Test
    public void getFieldTest() {
        assertEquals(0, model.getField(5, 4));
    }

    @Test
    public void setStepTest() {
        model.setUserCoors(1, 1);
        model.setActiveUser(1);
        model.setStep();
        assertEquals('X', model.getField(1, 1));
        model.setUserCoors(2, 2);
        model.setActiveUser(2);
        model.setStep();
        assertEquals('O', model.getField(2, 2)); 
        assertEquals(0, model.getField(7, 8));
    }

    @Test
    public void countStepsTest() {
        assertTrue(!(model.countSteps(0, 0, "down", 0)));
        for (int i = 0; i < 5; i++){
            model.setUserCoors(0, i);
            model.setStep();
        }
        assertTrue(model.countSteps(0, 0, "down", 0));
    }

    @Test
    public void checkStopTest() {
        for (int i = 0; i < 5; i++){
            model.setUserCoors(i, i);
            model.setStep();
        }
        assertTrue(model.checkStop());
        for (int i = 0; i < 5; i++){
            model.setUserCoors(0, i);
            model.setStep();
        }
        assertTrue(model.checkStop());
    }  
}