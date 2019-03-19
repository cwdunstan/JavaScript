import java.util.*;

public class DFS {
	
	public List<Integer> forbid;
	public int goals;
	public boolean searching = true;
	ArrayList<Tree.Node> expanded = new ArrayList<Tree.Node>();
	LinkedList<Tree.Node> fringe = new LinkedList<Tree.Node>();
	
	public DFS(Tree myTree, List<Integer> forbidden, int goal) {
		forbid=forbidden;
		goals=goal;

		
		//start node
		Tree.Node tempStart = myTree.getStart(); 
		if(tempStart.getDigits()==goal){
			expanded.add(tempStart);
			return;
		}
		else{
		//Start DFS Traversal
		dfs(myTree.getStart());
		//PRINT FOUND PATH
		for(int i=fringe.size()-1;i>=0;i--){
			System.out.print(fringe.get(i).getDigits());
			if(i>0){
				System.out.print(",");
			}else{
				System.out.print("\n");
			}
		}
		System.out.flush();
		//PRINT EXPANDED PATH
		for(int i=0;i<expanded.size();i++){
			System.out.print(expanded.get(i).getDigits());
			if(i<expanded.size()-1){
				System.out.print(",");
			}else{
				System.out.print("\n");
			}
		}
		System.out.flush();
		}
	}
	
	public void dfs(Tree.Node tempN){
		while(searching){
		if(!tempN.isVisited()){
			expanded.add(tempN);
			if(tempN.getDigits()==goals){
				while(tempN.getParent()!=null){
					fringe.add(tempN);
					tempN=tempN.getParent();
				}
				fringe.add(tempN);
				searching = false;
				tempN.generateChildren(forbid);
			}
			
			
		}
		for(Tree.Node kid : tempN.getChildren()){
			if(kid!=null && !kid.isVisited()){
				
				dfs(kid);
			}
			
			tempN.setVisited(true);
		}	
	}
}
}