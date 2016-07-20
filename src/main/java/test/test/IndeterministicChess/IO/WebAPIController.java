package test.test.IndeterministicChess.IO;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import test.test.IndeterministicChess.Board.*;

@RestController
public class WebAPIController {

    private static final String template = "Hello, %s!";

    @RequestMapping("/greeting")
    public WebAPI greeting() {
        return new WebAPI(String.format(template, "Welt"));
    }
    
    @RequestMapping("/buttonPressed")
    public void buttonPressed(@RequestParam(value="xPos", defaultValue="-1") String x, @RequestParam(value="yPos", defaultValue="-1") String y) {
    	try{
    		Square pressedSquare = new Square(Integer.parseInt(x), Integer.parseInt(y));
    		if(Chessboard.getInstance().isInBoard(pressedSquare)){
    			System.out.println("The Square " + pressedSquare + " has been pressed!");
    		}
    	}
    	catch(Exception e) {}
		System.out.println("A button has been pressed!" + x + ", " + y);
    }
}
