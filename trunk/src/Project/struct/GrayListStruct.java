package Project.struct;

import Project.StaticManager;

public class GrayListStruct extends BlackListStruct{
    public long startTime;
    public long endTime;
    public GrayListStruct (BlackListStruct data) {
        super.Name = data.Name;
        super.No = data.No;
        super.Parametes = data.Parametes;
        super.Path = data.Path;
        super.Type = data.Type;
        super.Status = StaticManager.GRAYLIST;
        startTime = System.currentTimeMillis();
        endTime = System.currentTimeMillis();
    }
    public GrayListStruct (String Name, int No, String Parametes, String Path, String Type, int Status, long startTime) {
        super.Name = Name;
        super.No = No;
        super.Parametes = Parametes;
        super.Path = Path;
        super.Type = Type;
        super.Status = StaticManager.GRAYLIST;
        this.startTime = startTime;
        endTime = System.currentTimeMillis();
    }
}