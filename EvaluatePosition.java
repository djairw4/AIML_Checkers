package checkers;// This package is required - don't remove it

public class EvaluatePosition // This class is required - don't remove it
{
    static private final int WIN=Integer.MAX_VALUE/2;
    static private final int LOSE=Integer.MIN_VALUE/2;
    static private boolean _color; // This field is required - don't remove it
    
    static public void changeColor(boolean color) // This method is required - don't remove it
    {
        _color=color;
    }
    static public boolean getColor() // This method is required - don't remove it
    {
        return _color;
    }
    static public int evaluatePosition(AIBoard board) // This method is required and it is the major heuristic method - type your code here
    {
        int myPieces=0;
        int opponentsPieces=0;
        int myRating=0;
        int opponentsRating=0;
        int rewardOffense=0;
        int rewardDefense=0;
        int myNeighborsRating=0;
        int opponentsNeighborsRating=0;
        int size=board.getSize();
        for (int i=0;i<size;i++)
        {
            for (int j=(i+1)%2;j<size;j+=2)
            {
                if (!board._board[i][j].empty) // field is not empty
                {
                    if (board._board[i][j].white==getColor()) // this is my piece
                    {
                        //piece in the corner is useless (0,7)-yellow (7,0)-red
                        if((getColor() && i==0 && j==7)||((!getColor()) && i==7 && j==0)){
                            rewardOffense-=1;
                        }
                        myPieces+=1;
                        //rewards when pieces stay in a group
                        myNeighborsRating+=neighbors(i,j,board,true);

                        if (board._board[i][j].king) { // this is my king
                            myRating += 50;
                        }
                        else { // this is my piece
                            myRating += 30;
                            //rewards offensive play (attack on the right corner of the opponent)
                            if(getColor()){ //top player - yellow
                                if(i==4 && j==5) rewardOffense+=8;
                                if(i==5 && j==4) rewardOffense+=8;
                                if(i==5 && j==6) rewardOffense+=10;
                                if(i==6 && j==5) rewardOffense+=10;
                                if(i==6 && j==7) rewardOffense+=10;
                                if(i==7 && j==6) rewardOffense+=12;
                                if(i==7 && j==4) rewardOffense+=12;
                            }
                            else{ //bottom player - red
                                if(i==3 && j==2) rewardOffense+=8;
                                if(i==2 && j==3) rewardOffense+=8;
                                if(i==2 && j==1) rewardOffense+=10;
                                if(i==1 && j==2) rewardOffense+=10;
                                if(i==1 && j==0) rewardOffense+=10;
                                if(i==0 && j==1) rewardOffense+=12;
                                if(i==0 && j==3) rewardOffense+=12;
                            }
                        }
                    }
                    else //opponent's piece
                    {
                        if((getColor() && i==0 && j==7)||((!getColor()) && i==7 && j==0)){
                            rewardOffense+=1;
                        }
                        opponentsPieces+=1;
                        opponentsNeighborsRating+=neighbors(i,j,board,false);
                        if (board._board[i][j].king){ // This is opponent's king
                            opponentsRating+=50;
                        }
                        else{
                            opponentsRating+=30;
                            if(getColor()){ //top player - yellow
                                if(i==4 && j==5) rewardOffense-=8;
                                if(i==5 && j==4) rewardOffense-=8;
                                if(i==5 && j==6) rewardOffense-=10;
                                if(i==6 && j==5) rewardOffense-=10;
                                if(i==6 && j==7) rewardOffense-=10;
                                if(i==7 && j==6) rewardOffense-=12;
                                if(i==7 && j==4) rewardOffense-=12;
                            }
                            else{//bottom player - red
                                if(i==3 && j==2) rewardOffense-=8;
                                if(i==2 && j==3) rewardOffense-=8;
                                if(i==2 && j==1) rewardOffense-=10;
                                if(i==1 && j==2) rewardOffense-=10;
                                if(i==1 && j==0) rewardOffense-=10;
                                if(i==0 && j==1) rewardOffense-=12;
                                if(i==0 && j==3) rewardOffense-=12;
                            }
                        }
                    }
                }
            }
        }
        //rewards defensive play
        rewardDefense=rewardDefense(board,myPieces,opponentsPieces,getColor());
        //Judge.updateLog("Type your message here, you will see it in the log window\n");
        if (myRating==0) return LOSE;
        else if (opponentsRating==0) return WIN;
        else return myRating-opponentsRating+myNeighborsRating+opponentsNeighborsRating+rewardOffense+rewardDefense;
    }

    //defensive positions are scored according to the number of pieces left on the board
    static public int rewardDefense(AIBoard board, int my_pieces,int op_pieces, boolean my_color){
        int rate=0;
        if(my_color) {
            if (!board._board[0][1].empty) // pole nie jest puste
            {
                if (board._board[0][1].white) rate+=(my_pieces-2);
            }
            if (!board._board[0][5].empty) // pole nie jest puste
            {
                if (board._board[0][5].white) rate+=(my_pieces-2);
            }
            if (!board._board[0][3].empty) // pole nie jest puste
            {
                if (board._board[0][3].white) rate+=((my_pieces-2)/2);
            }
            if (!board._board[1][0].empty) // pole nie jest puste
            {
                if (board._board[1][0].white) rate+=((my_pieces-2)/2);
            }

            if (!board._board[7][6].empty) // pole nie jest puste
            {
                if (!board._board[7][6].white) rate-=(op_pieces-2);
            }
            if (!board._board[7][2].empty) // pole nie jest puste
            {
                if (!board._board[7][2].white) rate-=(op_pieces-2);
            }
            if (!board._board[7][4].empty) // pole nie jest puste
            {
                if (!board._board[7][4].white) rate-=((op_pieces-2)/2);
            }
            if (!board._board[6][7].empty) // pole nie jest puste
            {
                if (!board._board[6][7].white) rate-=((op_pieces-2)/2);
            }
        }
        else{
            if (!board._board[0][1].empty) // pole nie jest puste
            {
                if (board._board[0][1].white) rate-=(op_pieces-2);
            }
            if (!board._board[0][5].empty) // pole nie jest puste
            {
                if (board._board[0][5].white) rate-=(op_pieces-2);
            }
            if (!board._board[0][3].empty) // pole nie jest puste
            {
                if (board._board[0][3].white) rate-=((op_pieces-2)/2);
            }
            if (!board._board[1][0].empty) // pole nie jest puste
            {
                if (board._board[1][0].white) rate-=((op_pieces-2)/2);
            }

            if (!board._board[7][6].empty) // pole nie jest puste
            {
                if (!board._board[7][6].white) rate+=(my_pieces-2);
            }
            if (!board._board[7][2].empty) // pole nie jest puste
            {
                if (!board._board[7][2].white) rate+=(my_pieces-2);
            }
            if (!board._board[7][4].empty) // pole nie jest puste
            {
                if (!board._board[7][4].white) rate+=((my_pieces-2)/2);
            }
            if (!board._board[6][7].empty) // pole nie jest puste
            {
                if (!board._board[6][7].white) rate+=((my_pieces-2)/2);
            }
        }
        return rate;
    }


    static public int neighbors(int i,int j, AIBoard board,boolean myPiece){
        int rate=0;
        if (i != 7) {
            if (j != 0) {
                if (!board._board[i + 1][j - 1].empty){
                    if ((board._board[i + 1][j - 1].white != getColor())) {//opponent piece
                        if(!myPiece){rate-=5;}
                    }
                    else{
                        if(myPiece){rate+=5;}
                    }
                }
            }
            if (j != 7) {
                if (!board._board[i + 1][j + 1].empty){
                    if ((board._board[i + 1][j + 1].white != getColor())) {//opponent piece
                        if(!myPiece){rate-=5;}
                    }
                    else{
                        if(myPiece){rate+=5;}
                    }
                }
            }
        }
        if (i != 0) {
            if (j != 0) {
                if (!board._board[i - 1][j - 1].empty){
                    if ((board._board[i - 1][j - 1].white != getColor())) {//opponent piece
                        if(!myPiece){rate-=5;}
                    }
                    else{
                        if(myPiece){rate+=5;}
                    }
                }
            }
            if (j != 7) {
                if (!board._board[i - 1][j + 1].empty){
                    if ((board._board[i - 1][j + 1].white != getColor())) {//opponent piece
                        if(!myPiece){rate-=5;}
                    }
                    else{
                        if(myPiece){rate+=5;}
                    }
                }
            }
        }
        return rate;
    }
}

