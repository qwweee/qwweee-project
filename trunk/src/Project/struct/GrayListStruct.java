package Project.struct;

public class GrayListStruct extends BlackListStruct{
    public long startTime;
    public long endTime;
    public GrayListStruct (BlackListStruct data) {
        super.Name = data.Name;
        super.No = data.No;
        super.Parametes = data.Parametes;
        super.Path = data.Path;
        super.Type = data.Type;
        super.Status = data.Status;
        startTime = System.currentTimeMillis();
    }
}