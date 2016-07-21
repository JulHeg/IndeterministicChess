package test.test.IndeterministicChess.IO;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import test.test.IndeterministicChess.Board.*;

@RestController
//@Controller
public class WebAPIController {

    private static final String template = "Hello, %s!";

    @RequestMapping("/getBoard")
    public WebAPI getBoard() {
        return new WebAPI(String.format(template, "Welt"));
    }
    
    @RequestMapping("/squareButtonPressed")
    public void squareButtonPressed(@RequestParam(value="xPos", defaultValue="-1") String x, @RequestParam(value="yPos", defaultValue="-1") String y) {
    	try{
    		Square pressedSquare = new Square(Integer.parseInt(x), Integer.parseInt(y));
    		if(Chessboard.getStandardChessboard().isInBoard(pressedSquare)){
    			System.out.println("The Square " + pressedSquare + " has been pressed!");
    		}
    	}
    	catch(Exception e) {}
    }
    
    @RequestMapping("/moveButtonPressed")
    public void moveButtonPressed() {
    	System.out.println("moveButtonPressed");
    }
    
    @RequestMapping("/splitButtonPressed")
    public void splitButtonPressed() {
    	System.out.println("splitButtonPressed");
    }
    
    @RequestMapping("/redetermineButtonPressed")
    public void redetermineButtonPressed() {
    	System.out.println("redetermineButtonPressed");
    }
    
    @RequestMapping("/endButtonPressed")
    public void endButtonPressed() {
    	System.out.println("endButtonPressed");
    }
}
