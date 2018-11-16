
import java.util.ArrayList;
import java.util.List;

class MyTreeNode
{
    private String name;
    private String state;
    private List<MyTreeNode> children = new ArrayList<MyTreeNode>();
     
    public MyTreeNode() 
    {
    }
     
    public MyTreeNode( String name, String description ) 
    {
        this.name = name;
        this.state = description;
    }
     
    public String getName() 
    {
        return name;
    }
     
    public void setName(String name) 
    {
        this.name = name;
    }
     
    public String getDescription() 
    {
        return this.state;
    }
     
    public void setDescription(String description) 
    {
        this.state = description;
    }
     
    public List<MyTreeNode> getChildren() 
    {
        return children;
    }
     
    public String toString()
    {
        return "MyTreeNode: " + name + ", " + this.state;
    }
}
