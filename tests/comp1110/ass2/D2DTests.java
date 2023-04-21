package comp1110.ass2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLOutput;

/**
 * This class is used to test BlueLagoon methods/functions.
 * This was completed for Task D2D.
 *
 */
public class D2DTests {

    /**
     * This method is to show how to write a test.
     * It is not a real test as it does not test anything.
     */
    @Test
    public void exampleTest() {
        // Here are variables used in this example test.
        // Change them to see how the test behaves.
        boolean alwaysTrue = true;
        boolean alwaysFalse = false;
        int alwaysOne = 1;
        int alwaysTwo = 2;
        String alwaysHello = "Hello";
        String alwaysWorld = "World";
        int[] alwaysOneTwoThree = {1,2,3};



        // To run a test to see if two values are equal, use the following:

        Assertions.assertTrue(alwaysTrue,"The test failed because alwaysTrue was not true");
        Assertions.assertFalse(alwaysFalse,"The test failed because alwaysFalse was not false");
        Assertions.assertEquals(alwaysOne*2, alwaysTwo, "The test failed because alwaysOne*2 was not equal to alwaysTwo");
        Assertions.assertEquals(alwaysHello + " " + alwaysWorld, "Hello World", "The test failed because alwaysHello + \" \" + alwaysWorld was not equal to \"Hello World\"");
        Assertions.assertArrayEquals(new int[]{1,2,3}, alwaysOneTwoThree, "The test failed because new int[]{1,2,3} was not equal to alwaysOneTwoThree");

    }

    /**
     * This method is to test the isStateStringWellFormed method.
     *
     */
    @Test
    public void testIsStateStringWellFormed() {

        String DEFAULT_GAME = "a 13 2; c 0 E; i 6 0,0 0,1 0,2 0,3 1,0 1,1 1,2 1,3 1,4 2,0 2,1; i 6 0,5 0,6 0,7 1,6 1,7 1,8 2,6 2,7 2,8 3,7 3,8; i 6 7,12 8,11 9,11 9,12 10,10 10,11 11,10 11,11 11,12 12,10 12,11; i 8 0,9 0,10 0,11 1,10 1,11 1,12 2,10 2,11 3,10 3,11 3,12 4,10 4,11 5,11 5,12; i 8 4,0 5,0 5,1 6,0 6,1 7,0 7,1 7,2 8,0 8,1 8,2 9,0 9,1 9,2; i 8 10,3 10,4 11,0 11,1 11,2 11,3 11,4 11,5 12,0 12,1 12,2 12,3 12,4 12,5; i 10 3,3 3,4 3,5 4,2 4,3 4,4 4,5 5,3 5,4 5,5 5,6 6,3 6,4 6,5 6,6 7,4 7,5 7,6 8,4 8,5; i 10 5,8 5,9 6,8 6,9 7,8 7,9 7,10 8,7 8,8 8,9 9,7 9,8 9,9 10,6 10,7 10,8 11,7 11,8 12,7 12,8; s 0,0 0,5 0,9 1,4 1,8 1,12 2,1 3,5 3,7 3,10 3,12 4,0 4,2 5,9 5,11 6,3 6,6 7,0 7,8 7,12 8,2 8,5 9,0 9,9 10,3 10,6 10,10 11,0 11,5 12,2 12,8 12,11; r C B W P S; p 0 0 0 0 0 0 0 S T; p 1 0 0 0 0 0 0 S T;";
        Assertions.assertTrue(BlueLagoon.isStateStringWellFormed(DEFAULT_GAME), "The test failed because DEFAULT_GAME was marked as an invalid state string");
        System.out.println("Test 1 passed");
        // Test adding multiple player with the same id (Caused Error 1)
        String DEFAULT_WITH_ADDED = DEFAULT_GAME + " p 1 0 0 0 0 0 0 S T;";
        Assertions.assertFalse(BlueLagoon.isStateStringWellFormed(DEFAULT_WITH_ADDED), "The test failed because DEFAULT with double Player 1 was marked as a valid state string");
        System.out.println("Test 2 passed");
        DEFAULT_WITH_ADDED = DEFAULT_GAME + " p 0 0 0 0 0 0 0 S T;";
        Assertions.assertFalse(BlueLagoon.isStateStringWellFormed(DEFAULT_WITH_ADDED), "The test failed because DEFAULT with double Player 0 was marked as a valid state string");
        System.out.println("Test 3 passed");

        // Test adding `a 13 2; `
        DEFAULT_WITH_ADDED = "a 13 2; "+ DEFAULT_GAME;
        Assertions.assertFalse(BlueLagoon.isStateStringWellFormed(DEFAULT_WITH_ADDED), "The test failed because DEFAULT with double init was marked as a valid state string");
        System.out.println("Test 4 passed");



        /* This test found Errors as below
        Errors found by this test are:
        1. The method did not check if the player was included twice.
        */
    }

    @Test
    public void testIsMoveStringWellFormed(){
        String moveString = "S 10,11";
        Assertions.assertTrue(BlueLagoon.isMoveStringWellFormed(moveString), "The test failed because \"S 10,11\" is DEEMED an invalid move String even though it should be valid");
        System.out.println("Test moveString is valid");

        // updating the move string to a new one
        moveString = "T 4,5";
        Assertions.assertTrue(BlueLagoon.isMoveStringWellFormed(moveString), "The test failed because \"T 4,5\" is DEEMED an invalid move String even though it should be valid");
        System.out.println("Test 2 passed");

        moveString = "A 1,2";
        Assertions.assertFalse(BlueLagoon.isMoveStringWellFormed(moveString), "The test failed \"A 1,2\" is DEEMED a valid move string even though it should be invalid because the first element is supposed to be only either `S` or `T`");
        System.out.println("Test 3 passed");

        moveString = "S 12";
        Assertions.assertFalse(BlueLagoon.isMoveStringWellFormed(moveString), "The test failed because \"S 12\" is DEEMED a valid move string even though it should be invalid because there should be a `,` between the numbers `1` and `2` to differentiate between rows and cols");
        System.out.println("Test 4 passed");

        moveString = "S 1, 2";
        Assertions.assertFalse(BlueLagoon.isMoveStringWellFormed(moveString), "The test failed because \"S 1, 2\" is DEEMED a valid move string even though it should be invalid because there should not be any whitespace after `,`");
        System.out.println("Test 5 passed");

        moveString = "T  1,2";
        Assertions.assertFalse(BlueLagoon.isMoveStringWellFormed(moveString), "The test failed because \"T  1,2\" is DEEMED a valid move string even though it should be invalid because there should not be 2 whitespaces after the first element (i.e. after `T`)");
        System.out.println("Test 6 passed");

        moveString = "S 3 ,4";
        Assertions.assertFalse(BlueLagoon.isMoveStringWellFormed(moveString), "The test failed because \"S  3,4\" is DEEMED a valid move string even though it should be invalid because there should not be a whitespace after the first number");
        System.out.println("Test 7 passed");
        System.out.println("All Tests passed");

    }

}
