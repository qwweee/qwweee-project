package Project.struct;

public class BlackListStruct extends Object{
    public int No;
    public String Name;
    public String Path;
    public String Parametes;
    public String Type;
    public int Status;
    public boolean equals(Object o) {
        boolean isEquals = false;
        if (o instanceof SWRunTableStruct) {
            SWRunTableStruct data = (SWRunTableStruct)o;
            isEquals = data.Name.equalsIgnoreCase(Name) && data.Path.equalsIgnoreCase(Path) && 
            data.Parametes.equalsIgnoreCase(Parametes) && data.Type.equalsIgnoreCase(Type);
        }
        if (o instanceof BlackListStruct) {
            BlackListStruct data = (BlackListStruct) o;
            isEquals = data.Name.equalsIgnoreCase(Name) && data.Path.equalsIgnoreCase(Path) &&
            data.Parametes.equalsIgnoreCase(Parametes) && data.Type.equalsIgnoreCase(Type);
        }
        return isEquals;
    }
    public void print() {
        System.out.println(String.format("%3d %20s %20s\n%20s %20s %2d", No, Name, Path, Parametes, Type, Status));
    }
}
