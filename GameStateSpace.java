import java.util.*;

// Encapsulates all the possible game states of a game of Zombie Trap.
public class GameStateSpace implements Iterable<ZombieTrapGame>{

	//possibleStates tracks all of the possible states in the game as
	//	well as each state's previous state
	//	key = a single, possible state
	//	value = (the previous state, what shift was used)
	private HashMap<ZombieTrapGame, ArrayList<Object>> possibleStates;
	private int numOfStates;	//the total amount of possible states found
	private int bestScore;	//the highest possible score
	private ZombieTrapGame bestGame;	//the state with the highest score
										//and the shortest moves
	private ZombieTrapGame initialState;	//the state the game starts with
	
	
  // Creates a new game state space based on the initial game given.
  // Initializes any internal data needed then builds up the state space.
  public GameStateSpace(ZombieTrapGame initial)
  {
  	  //possibleStates is initalized
  	  this.possibleStates = new HashMap<ZombieTrapGame, ArrayList<Object>>();
  	  //all other fields are given values
  	  this.initialState = initial;
  	  this.bestGame = initial.copy();
  	  this.numOfStates = 1;
  	  this.bestScore = 0;
  	  //then the state space is built with a copy of the initial
  	  ZombieTrapGame editer = initial.copy();
  	  breadthSearch(editer);
  	  
  }
 
  //this is a class used only by breadthSearch to build up the state space
  public class ListQueue{
  	  //an arrayList is used as the line-keeper
  	  ArrayList<ZombieTrapGame> theQueue;
  	  public ListQueue()
  	  {
  	  	  //initialization
  	  	  this.theQueue = new ArrayList<ZombieTrapGame>();
  	  }
  	  public boolean enqueue(ZombieTrapGame z)
  	  {
  	  	  //a new state is inserted at the back of the line,
  	  	  //	which happens to be the beginning of the arrayList
  	  	  this.theQueue.add(0,z);
  	  	  return true;
  	  }
  	  public ZombieTrapGame dequeue()
  	  {
  	  	  //furthest state from the beginning of the ArrayList is returned
  	  	  ZombieTrapGame z = this.theQueue.remove(this.theQueue.size()-1);
  	  	  return z;
  	  }
  	  public int size()
  	  {
  	  	  //the number of states present in the ArrayList is returned
  	  	  return this.theQueue.size();
  	  }
  	  //for convenience and testing, a toString method has been built
  	  public String toString()
  	  {
  	  	  StringBuilder stBu = new StringBuilder();
  	  	  String s = " ";
  	  	  for(int i = 0; i<this.theQueue.size();i++)
  	  	  {
  	  	  	  if(this.theQueue.get(i)!=null)
  	  	  	  {
  	  	  	  	  s = this.theQueue.get(i).toString();
  	  	  	  	  stBu.append(s);
  	  	  	  }
  	  	  	  stBu.append("\n");
  	  	  }
  	  	  //toString is often called multiple times in a row, so the dotted
  	  	  //	line below helps to separate them.
  	  	  stBu.append("-------------------------");
  	  	  return stBu.toString();
  	  }
  }
  
  //breadthSearch takes an initial state and shifts it in every way. The
  //	shifted versions are then shifted themselves, one-by-one.
  //this uses a queue to store states that haven't been explored/shifted yet.
  //	The unexplored state at the front of the queue is removed and shifted
  //	about. Any new states found are put in the back of the line/queue.
  //	this goes on until no more new states are found.
  private void breadthSearch(ZombieTrapGame initial) 
  {
  	  //Q is the queue that stores unexplored states
    ListQueue Q = new ListQueue();
    //inital is stored first since it hasn't been explored yet
    Q.enqueue(initial);
    //initial is inserted into possibleStates, because it's new at this point.
    ArrayList<Object> init = new ArrayList<Object>();
  	this.possibleStates.put(initial,init);
  	
  	//the while loop is the main cog that keeps Q and possibleStates searching
  	//	for new states. It ends when there are no new states to explore.
    while(Q.size()!=0)
    {
    	//a single state is grabed from the line to be explored
       ZombieTrapGame zGame = Q.dequeue();
       //the state is immediately check to see if it has a better score than 
       //	all other states
       if(zGame.getScore() > this.bestScore)
       {
       	   //if so, this state is marked as the best game, until a 
       	   //	state with a better score comes along
          this.bestGame = zGame;
          this.bestScore = zGame.getScore();
       }
       
       //now the state gets shifted. The for loop goes up to four because 
       //	of the four possible shifts, up, down, left, and right.
       for(int i = 1; i<=4; i++)
       {
       	   //the state is copied before we make the shift because we
       	   //	still need to have access to it in case a new state is found.
       	   ZombieTrapGame nextGame = zGame.copy();
       	   //the copy is then shifted, depending on what step the for
       	   //	loop is on.
       	   if(i==1)       	   
       	   	   nextGame.shiftUp();       	   
       	   if(i==2)       	   
       	   	   nextGame.shiftDown();       	   
           if(i==3)
       	   	   nextGame.shiftLeft();
       	   if(i==4)
       	   	   nextGame.shiftRight();
       	   //the newly-shifted copy is checked whether or not its state
       	   //	has been reached before.
          if(!this.possibleStates.containsKey(nextGame))
          {
          	  //addList is created to be the value when added to the hashMap
          	  ArrayList<Object> addList = new ArrayList<Object>();
          	  //the number of found states is updated
          	  this.numOfStates += 1;
          	  //first, the original state is added...
              addList.add(zGame);
              //...then, what shift was used to reach the new state
              if(i==1)       	   
              	  addList.add("u");       	   
       	  	  if(i==2)       	   
       	  	  	  addList.add("d");       	   
          	  if(i==3)
          	  	  addList.add("l");
       	   	  if(i==4)
       	   	  	  addList.add("r");
       	   	  //the new state and a list containing information about its 
       	   	  //	predicesor is added to possibleStates
       	   	  this.possibleStates.put(nextGame, addList);
       	   	  //the new state is also added to the end of the queue
       	   	  //	so it can be explored
              Q.enqueue(nextGame);
          }
       }
    }
  }
 
  // Removes any current states. The builds up a state space based on
  // the new initial game that is provided.
  public void buildStateSpace(ZombieTrapGame initial)
  {
  	  //first, the hashMap is cleared of all keys and values
  	  this.possibleStates.clear();
  	  //then all of the other fields' values are reset.
  	  this.bestGame = initial.copy();
  	  this.numOfStates = 0;
  	  this.bestScore = 0;
  	  //finally, the inital state is copied and sent to breadthSearch
  	  //	so that it can be shifted and explored
  	  ZombieTrapGame editer = initial.copy();
  	  breadthSearch(editer);
  	  
  }
  
  // Returns an iterator that enumerates all of reachable states
  // including the initial state.
  public Iterator<ZombieTrapGame> iterator()
  {
  	  //because hashMaps don't have a method that provides
  	  //	an iterator, it's converted into a set, which happens
  	  //	to have such a method.
  	  Set<ZombieTrapGame> setOfKeys = this.possibleStates.keySet();
  	  Iterator<ZombieTrapGame> iter = setOfKeys.iterator();
  	  return iter;
  }

  // Returns true if the game given is reachable through some move
  // sequence from the initial state given.
  public boolean stateReachable(ZombieTrapGame state)
  {
  	  return this.possibleStates.containsKey(state);
  }

  // Returns the shortest move sequence that will move from the initial
  // game stat to the given state.  Moves are single letter strings
  // with "u" for up, "l" for left, "d" for down, and "r" for right
  // and are returned in a list.
  public List<String> movesToReach(ZombieTrapGame targetState)
  {
  	  if(!stateReachable(targetState))
  	  	  return null;
  	  
  	  //find grabs the state and move before targetState
  	  ArrayList<Object> find = this.possibleStates.get(targetState);
  	  //shiftList collects the moves in the order they come
  	  ArrayList<String> shiftList = new ArrayList<String>();
  	  
  	  //the first previous move is added
  	  shiftList.add((String)find.get(1));
  	  //the previous state of the board is captured and copied
  	  ZombieTrapGame temp = (ZombieTrapGame) find.get(0);
  	  targetState = temp.copy();
  	  //The previous state is asked for it's predicecor, and it 
  	  //	continues to ask backwards until the initial state of the 
  	  //	game is reached.
  	  while(!find.get(0).equals(this.initialState))
  	  {
  	  	  find = this.possibleStates.get(targetState);
  	  	  shiftList.add(0,(String)find.get(1));
  	  	  temp = (ZombieTrapGame) find.get(0);
  	  	  targetState = temp.copy();
  	  }
  	  return shiftList;
  }

  // Return the best score that is achievable based on the initial
  // game used in the creation of the GameStateSpace
  public int bestScore()
  {
  	  return this.bestScore;
  }

  // Return how many states are in the state space.
  public int stateCount()
  {
  	  return this.numOfStates;
  }

  // Return a move sequence as in movesToReach() which will go from
  // the inital state to the best scoring game state.  If more than
  // one move sequence will result in a best score, return the
  // shortest set of moves.  If there is a tie for both score and
  // shortness of move sequence, any optimal sequence of moves can be
  // returned.
  public List<String> bestMoves()
  {
  	  return movesToReach(this.bestGame);
  }

}

//This class previously ran using a recursive method. I decided to leave
//	the recursive method here for future references.
 /*
  //recursiveShift is a recursive method that travels through each
  //	possible playthrough and saves newly reached states in the 
  //	main hashmap.
  private boolean recursiveShift(String shift, ZombieTrapGame state, 
  	  int triggerEnd, int moves, ArrayList<String> trackMoves)
  {
  	  //a copy of state is made so that the original isn't
  	  //	accidently changed
  	  ZombieTrapGame editer = state.copy();
  	  int before = editer.getScore();
  	  //the copy is then changed depending on the chosen shift
  	  if(shift.equals("left"))
  	  {
  	  	  editer.shiftLeft();
  	  	  trackMoves.add("l");
  	  }
  	  if(shift.equals("right"))
  	  {
  	  	  editer.shiftRight();
  	  	  trackMoves.add("r");
  	  }
  	  if(shift.equals("up"))
  	  {
  	  	  editer.shiftUp();
  	  	  trackMoves.add("u");
  	  }
  	  if(shift.equals("down"))
  	  {
  	  	  editer.shiftDown();
  	  	  trackMoves.add("d");
  	  }
  	  int after = editer.getScore();
  	  
  	  moves+=1;
  	  boolean te1 = false;
  	  boolean te2 = false;
  	  boolean te3 = false;
  	  boolean te4 = false;
  	  
  	  //stops the recursion if four moves have been made without
  	  //	making a new score or finding a new state;
  	  if(triggerEnd!=4)
  	  {
  	  	  //if this is a new state, reset triggerEnd, update the 
  	  	  //	state count feild, and add it to the hash
  	  	if(!this.possibleStates.containsKey(editer))
  	  	{
  	  		triggerEnd = 0;
  	  		this.numOfStates+=1;
 	  		this.possibleStates.put(editer,trackMoves);
  	  	}
  	  	else
  	  	{
  	  		//Being in this cluster means that this state has already been found.
  	  		//So, we need to see if this found path is shorter than the 
  	  		//	one currently assisgned to this state:
  	  		int compare = this.possibleStates.get(editer).size();
  	  		//if the new path turns out to be shorter, then the whole
  	  		//	object is removed from the hash, and then re-inserted with
  	  		//	the new, shorter past as it's value.
  	  		if(compare>trackMoves.size())
  	  		{
  	  			this.possibleStates.remove(editer);
  	  			this.possibleStates.put(editer, trackMoves); 
  	  		}
  	  	}
  	  	
  	  	//checks whether a score was made or not during the last shift.
  	  	//If a new score wasn't made, then a strike is counted against the game.
  	  	//	Four strikes means the game has no more scorable moves left.
  	  	if(after>before)
  	  		triggerEnd = 0;
  	  	else
  	  	{
  	  		triggerEnd += 1;
  	  		try{
  	  			String e = "whodunit";
  	  		throw new RuntimeException(e);
  	  		}catch(Exception e)
  	  		{
  	  			System.out.println("Am I in here? Trigger is "+String.valueOf(triggerEnd));
  	  		}
  	  		
  	  	}
  	  	
  	  	//if there are no more possible moves...
  	  	if(triggerEnd==4)
  	  	{
  	  		//...and this play has the highest score...
  	  	  if(this.bestScore<editer.getScore())
  	  	  {
  	  	  	  //...and this play has the least number of moves to get 
  	  	  	  //	that score, then the score and the number of moves 
  	  	  	  //	is saved.
  	  	  	  //A new list is set up to record the different shifts
  	  	  	  //	that lead up to the best game.
  	  	  	  if((this.leastMoves==-1)||(moves<this.leastMoves))
  	  	  	  {
  	  	  	  	  this.bestScore = editer.getScore();
  	  	  	  	  this.leastMoves = moves-4;
  	  	  	  	  this.bestMoves.clear();
  	  	  	  	  this.bestMoves.add(shift);
  	  	  	  	  //true is returned to singal the previous play that
  	  	  	  	  //	it's a part of the best game and must be stored.
  	  	  	  	  return true;
  	  	  	  }
  	  	  }
  	  	}
  	  	
  	  	//by refering to every possible shift each time, no
  	  	//	possible path is left unchecked. The te's are collected
  	  	//	in case one of the shifts result in the best game.
  	  	te1 = recursiveShift("left", editer, triggerEnd, moves, (ArrayList<String>) trackMoves.clone());
  	  	te2 = recursiveShift("right", editer, triggerEnd, moves, (ArrayList<String>) trackMoves.clone());
  	  	te3 = recursiveShift("up", editer, triggerEnd, moves, (ArrayList<String>) trackMoves.clone());
  	  	te4 = recursiveShift("down", editer, triggerEnd, moves, (ArrayList<String>) trackMoves.clone());
  	  }
  	  
  	  //checks to see if the best game has been found by one of
  	  // the current call's decendants.
  	  if(((te1)||(te2)||(te3)||(te4))&&(triggerEnd!=4))
  	  {
  	  	  //adds the shift for this call to the beginning of the
  	  	  //	list so that the moves are in order
  	  	  this.bestMoves.add(0,shift);
  	  	  //true is returned to tell the current call's ancestors
  	  	  //	to store themselves.
  	  	  return true;
  	  }
  	  //false is returned only if this call is not the originator
  	  //	of or decendant to a line of calls that hold the best score
  	  return false;
  }*/

