package Project.struct;

public class BlackListStruct extends Object{
    public int No;
    public String Name;
    public String Path;
    public String Paraments;
    public String Type;
    public int Status;
    public boolean equals(Object o) {
        boolean isEquals = false;
        if (o instanceof SWRunTableStruct) {
            SWRunTableStruct data = (SWRunTableStruct)o;
            isEquals = data.Name.equalsIgnoreCase(Name) && data.Path.equalsIgnoreCase(Path) && 
            data.Parametes.equalsIgnoreCase(Paraments) && data.Type.equalsIgnoreCase(Type);
        }
        return isEquals;
    }
}
