public class Rabbit extends Animal {
	
    private boolean canSeeFoxNow = false;
    private int distanceToFox;
    private int directionToFox;
    private boolean atBush = false;
    private int directionToBush;
    private int shortestBushDistance = 100;
    private int directionToEdge;


//Issues: Bush near edge makes rabbit get stuck
    //Rabbit does not adjust properly when at corner and near other bushes
          


    public Rabbit(Model model, int row, int column) {
        super(model, row, column);
    }
    
    //Checks if the rabbit is directly N, E, S, or W of bush
    //AND That that same bush is 1 away
    boolean checkBushPlacement() {
    	for (int i = Model.MIN_DIRECTION; i <= Model.MAX_DIRECTION; i++) {
            if (look(i) == Model.BUSH && distance(i) <= shortestBushDistance) {
                shortestBushDistance = distance(i);
                directionToBush = i;
            }
        }
    	if (shortestBushDistance <= 1 && (directionToBush ==Model.N || directionToBush ==Model.E || directionToBush ==Model.S || directionToBush ==Model.W)) {
              return true;
            }
    	return false;
    }
    
    //Moves clockwise around bush
    int moveClockwise() {
    	int moveDirection = Model.turn(directionToBush, -1);
    	directionToBush = Model.turn(directionToBush, 2);
    	if(canMove(moveDirection)) {
    		return moveDirection;
    	}else {
    		//Accounts for multiple bushes in a row
    		moveDirection = Model.turn(moveDirection, -1);
    		directionToBush = Model.turn(directionToBush, -2);
    		return moveDirection;
    	}
    	
    	
    }
    
    //Moves counter-clockwise around bush
    int moveCounterClockwise() {
    	int moveDirection = Model.turn(directionToBush, 1);
    	directionToBush = Model.turn(directionToBush, -2);
    	if(canMove(moveDirection)) {
    		return moveDirection;
    	}else {
    		//Accounts for multiple bushes in a row
    		moveDirection = Model.turn(moveDirection, 1);
    		directionToBush = Model.turn(directionToBush, 2);
    		return moveDirection;
    	}
    	
    }
    

    
    int decideMove() {
    	//If not next to a bush, move to a bush
    	if(!atBush){
    		//If too close to the edge, move in the opposite direction
    		for (int i = Model.MIN_DIRECTION; i <= Model.MAX_DIRECTION; i++) {
                if (look(i) == Model.EDGE && distance(i) < 3) {
                    directionToEdge = i;
                    int awayFromEdge = Model.turn(directionToEdge, 4);
                    if(canMove(awayFromEdge)) {
                    	return awayFromEdge;
                    }else {
                    	//Handles if close to the edge and can't
                    	//move directly 180 degrees away
                    	for(int j = Model.MIN_DIRECTION; j <= Model.MAX_DIRECTION; j++) {
                    		if(canMove(awayFromEdge + j)) {
                    			return awayFromEdge +j;
                    		}
                    	}
                    }
                    
                }
            }
    		
    		//Otherwise move towards bush
    		for (int i = Model.MIN_DIRECTION; i <= Model.MAX_DIRECTION; i++) {
                if (look(i) == Model.BUSH && distance(i) < shortestBushDistance) {
                    shortestBushDistance = distance(i);
                    directionToBush = i;
                }
            }
    		//Makes sure rabbit is in proper placement around the bush
    		if(shortestBushDistance <= 1 && !checkBushPlacement()) {
    			System.out.println("adjusting");
    			atBush = true;
    			directionToBush = Model.turn(directionToBush, -1);
    			return Model.turn(directionToBush, 1);
    		}else if(shortestBushDistance <= 1) {
    			atBush = true;
    		}
    		return directionToBush;
    	}
    	
    	
    	//If at the bush, look for the fox
    	canSeeFoxNow = false;
    	//Look around for the fox
    	//If fox is found, direction to fox is set
        for (int i = Model.MIN_DIRECTION; i <= Model.MAX_DIRECTION; i++) {
            if (look(i) == Model.FOX) {
                canSeeFoxNow = true;
                directionToFox = i;
                distanceToFox = distance(i);
            }
        }
   
    	//If the fox is one away, move opposite around the bush
        if(canSeeFoxNow) {
        	//Fox is NORTH of rabbit
        	if(directionToFox == Model.N && directionToBush == Model.E) {
        		return moveCounterClockwise();
        	}else if(directionToFox == Model.N && directionToBush == Model.W) {
        		return moveClockwise();
        	}else if(directionToFox == Model.N && directionToBush == Model.S) {
        		return moveClockwise();
        	}       	
        	//Fox is EAST of rabbit
        	else if(directionToFox == Model.E && directionToBush != Model.N) {
        		return moveCounterClockwise();
        	}else if(directionToFox == Model.E && directionToBush == Model.N) {
        		return moveCounterClockwise();
        	}
        	//Fox is WEST of rabbit
        	else if(directionToFox == Model.W && directionToBush != Model.N) {
        		return moveClockwise();
        	}else if(directionToFox == Model.W && directionToBush == Model.N) {
        		return moveCounterClockwise();
        	}
        	//Fox is SOUTH of rabbit
        	else if(directionToFox == Model.S && directionToBush == Model.E) {
        		return moveClockwise();
        	}else if(directionToFox == Model.S && directionToBush == Model.W) {
        		return moveCounterClockwise();
        	}else if(directionToFox == Model.S && directionToBush == Model.N) {
        		return moveClockwise();
        	}
        	
        	//NORTHEAST
        	else if(directionToFox == Model.NE && (directionToBush == Model.S || directionToBush == Model.E)) {
        		return moveCounterClockwise();
        	}else if(directionToFox == Model.NE){
        		return moveClockwise();
        	}
        	//NORTHWEST
        	else if(directionToFox == Model.NW && (directionToBush == Model.S || directionToBush == Model.W)) {
        		return moveClockwise();
        	}else if(directionToFox == Model.NW){
        		return moveCounterClockwise();
        	}
        	//SOUTHEAST
        	else if(directionToFox == Model.SE && (directionToBush == Model.N || directionToBush == Model.E)) {
        		return moveClockwise();
        	}else if(directionToFox == Model.SE){
        		return moveCounterClockwise();
        	}
        	//SOUTHWEST
        	else if(directionToFox == Model.SW && (directionToBush == Model.S || directionToBush == Model.E)) {
        		return moveClockwise();
        	}else if(directionToFox == Model.SW){
        		return moveCounterClockwise();
        	}

        }            
 		return Model.STAY;
    }
    
}
