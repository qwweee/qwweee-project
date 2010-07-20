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
            isEquals = data.Name.equalsIgnoreCase(Name) && pathEquals(data) && 
            data.Parametes.equalsIgnoreCase(Parametes) && data.Type.equalsIgnoreCase(Type);
        }
        if (o instanceof BlackListStruct) {
            BlackListStruct data = (BlackListStruct) o;
            isEquals = data.Name.equalsIgnoreCase(Name) && pathEquals(data) &&
            data.Parametes.equalsIgnoreCase(Parametes) && data.Type.equalsIgnoreCase(Type);
        }
        return isEquals;
    }
    private boolean pathEquals(SWRunTableStruct o) {
        int index = (Path.indexOf(":")==-1)?0:Path.indexOf(":");
        if (((SWRunTableStruct)o).Path.indexOf(":") == -1) {
            return ((SWRunTableStruct)o).Path.equalsIgnoreCase(Path);
        }
        return ((SWRunTableStruct)o).Path.substring(((SWRunTableStruct)o).Path.indexOf(":")).equalsIgnoreCase(Path.substring(index));
    }
    private boolean pathEquals(BlackListStruct o) {
        int index = (Path.indexOf(":")==-1)?0:Path.indexOf(":");
        if (o.Path.indexOf(":") == -1) {
            return o.Path.equalsIgnoreCase(Path);
        }
        return o.Path.substring(o.Path.indexOf(":")).equalsIgnoreCase(Path.substring(index));
    }
    public void print() {
        System.out.println(String.format("%3d %20s %20s\n%20s %20s %2d", No, Name, Path, Parametes, Type, Status));
    }
}
